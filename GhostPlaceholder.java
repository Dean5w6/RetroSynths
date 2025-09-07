import greenfoot.*;

/**
 * A visual placeholder that follows the mouse when a defender card is selected.
 * It has no gameplay logic of its own; it is entirely controlled by the World.
 */
public class GhostPlaceholder extends Actor {

    public GhostPlaceholder(String imageName) {
        GreenfootImage image = new GreenfootImage(imageName);
        
        int width = Level1.TILE_WIDTH - 10;
        int height = Level1.TILE_HEIGHT - 10;
        image.scale(width, height);
        
        image.setTransparency(150); 
        
        setImage(image);
    }
}