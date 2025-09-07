import greenfoot.*;

public class GeigerCounter extends Defender {
    
    private GreenfootImage idleImage = new GreenfootImage("geiger_idle.png");
    private GreenfootImage generatingImage = new GreenfootImage("geiger_generating.png");
    
    private boolean isReadyToCollect = false;
    private int generationTimer = 0;
    
    public GeigerCounter() {
        super(300);
        
        int width = Level1.TILE_WIDTH - 10;
        int height = Level1.TILE_HEIGHT - 10;
        
        idleImage.scale(width, height);
        generatingImage.scale(width, height);
        
        setImage(idleImage);
    }

    public void act() {
        if (!isReadyToCollect) {
            generationTimer++;
            if (generationTimer >= 1200) { 
                isReadyToCollect = true;
                setImage(generatingImage);
                generationTimer = 0;
            }
        } else {
            if (Greenfoot.mouseClicked(this)) {
                Level1 world = (Level1) getWorld();
                world.addPlutonium(50);
                isReadyToCollect = false;
                setImage(idleImage);
            }
        }
    }
}