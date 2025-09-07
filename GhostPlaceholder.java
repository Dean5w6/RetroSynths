// === GhostPlaceholder (NEW CLASS) ===
import greenfoot.*;

/**
 * A visual placeholder that follows the mouse when a defender card is selected.
 * It has no gameplay logic of its own; it is entirely controlled by the World.
 */
public class GhostPlaceholder extends Actor {

    public GhostPlaceholder(String imageName) {
        GreenfootImage image = new GreenfootImage(imageName);
        
        // Scale the ghost to the same size as the actual defender for accurate placement
        int width = Level1.TILE_WIDTH - 10;
        int height = Level1.TILE_HEIGHT - 10;
        image.scale(width, height);
        
        // Make the image semi-transparent (ghostly!)
        image.setTransparency(150); // Value from 0 (invisible) to 255 (opaque)
        
        setImage(image);
    }
}