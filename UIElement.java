import greenfoot.*;

public abstract class UIElement extends Actor {
    
    private int fixedX;
    private int fixedY;
    private boolean hasBeenPlaced = false;

    @Override
    protected void addedToWorld(World world) {
        fixedX = getX();
        fixedY = getY();
        hasBeenPlaced = true;
    }
    
    public void act() { 
        if (hasBeenPlaced && (getX() != fixedX || getY() != fixedY)) {
            setLocation(fixedX, fixedY);
        }
    }
}