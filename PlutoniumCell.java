// === PlutoniumCell (COMPLETE REVISED CODE with Correct Scaling) ===
import greenfoot.*;

public class PlutoniumCell extends Actor {
    
    private GreenfootImage image = new GreenfootImage("plutonium_cell_ui.png"); // Make sure this filename is correct
    
    // Behavior constants
    private int fallSpeed = 1;
    private int lifetime = 600; // 10 seconds * 60 acts/sec
    
    // State variables
    private int stopY;
    private boolean hasLanded = false;

    /**
     * Creates a new PlutoniumCell that will fall and stop at a specific height.
     * @param stopY The Y-coordinate of the floor.
     */
    public PlutoniumCell(int stopY) {
        this.stopY = stopY;
        
        // --- REVISED: Scaled to 44x60 to maintain the 598x817 aspect ratio ---
        image.scale(44, 60);
        setImage(image);
    }
    
    public void act() {
        if (!hasLanded) {
            // --- FALLING LOGIC ---
            setLocation(getX(), getY() + fallSpeed);
            if (getY() >= stopY) {
                setLocation(getX(), stopY);
                hasLanded = true;
            }
        } else {
            // --- DESPAWN LOGIC (only starts after landing) ---
            lifetime--;
            if (lifetime <= 0) {
                getWorld().removeObject(this);
                return;
            }
        }
        
        // --- CLICK DETECTION LOGIC ---
        if (Greenfoot.mouseClicked(this)) {
            Level1 world = (Level1) getWorld();
            world.addPlutonium(50);
            getWorld().removeObject(this);
        }
    }
}