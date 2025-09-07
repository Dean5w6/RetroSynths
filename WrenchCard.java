import greenfoot.*;

public class WrenchCard extends UIElement {
    
    private GreenfootImage idleImage = new GreenfootImage("wrench_ui.png");
    private GreenfootImage selectedImage = new GreenfootImage("wrench_ui.png"); 

    public WrenchCard() {
        idleImage.scale(50, 50);
        selectedImage.scale(50, 50);
        setImage(idleImage);
    }
    
    public void act() {
        super.act(); 
        
        Level1 world = (Level1) getWorld();
        
        if (world.currentlySelectedCard.equals("wrench")) {
            setImage(selectedImage);
        } else {
            setImage(idleImage);
        }
        
        if (Greenfoot.mouseClicked(this)) {
            if (world.currentlySelectedCard.equals("wrench")) {
                world.currentlySelectedCard = "none"; 
            } else {
                world.currentlySelectedCard = "wrench"; 
            }
        }
    }
}