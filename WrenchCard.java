// === WrenchCard (NEW UI CLASS) ===
import greenfoot.*;

public class WrenchCard extends UIElement {
    
    // You can use two different images if you have a "selected" version
    private GreenfootImage idleImage = new GreenfootImage("wrench_ui.png");
    private GreenfootImage selectedImage = new GreenfootImage("wrench_ui.png"); // Using the same for now

    public WrenchCard() {
        // Scale images to the requested 100x100 size
        idleImage.scale(50, 50);
        selectedImage.scale(50, 50);
        // Optional: Add a visual effect to the selected image, like a glow or border
        // selectedImage.setColor(Color.YELLOW);
        // selectedImage.drawRect(2, 2, 95, 95);
        
        setImage(idleImage);
    }
    
    public void act() {
        super.act(); // This calls the parent UIElement's snap-back logic
        
        Level1 world = (Level1) getWorld();
        
        // Update our appearance based on the world's state
        if (world.currentlySelectedCard.equals("wrench")) {
            setImage(selectedImage);
        } else {
            setImage(idleImage);
        }
        
        // Handle clicks to toggle the remove mode
        if (Greenfoot.mouseClicked(this)) {
            if (world.currentlySelectedCard.equals("wrench")) {
                world.currentlySelectedCard = "none"; // Deselect if already selected
            } else {
                world.currentlySelectedCard = "wrench"; // Select the wrench
            }
        }
    }
}