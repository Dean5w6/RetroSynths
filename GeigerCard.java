import greenfoot.*;

public class GeigerCard extends DefenderCard {
    
    GreenfootImage idle = new GreenfootImage("geiger_card_idle.png");
    GreenfootImage selected = new GreenfootImage("geiger_card_selected.png");
    GreenfootImage cooldownImage = new GreenfootImage("geiger_card_cooldown.png");
    
    int cost = 100;
    
    private int cooldownTimer = 0;
    public static final int COOLDOWN_TIME = 420; 

    public GeigerCard() {
        idle.scale(100, 50);
        selected.scale(110, 55);
        cooldownImage.scale(100, 50);
        setImage(idle);
    }
    
    public void act() {
        super.act();
        Level1 world = (Level1) getWorld();
        
        if (cooldownTimer > 0) {
            cooldownTimer--;
            setImage(cooldownImage);
            return;
        }
        
        if (!world.currentlySelectedCard.equals("geiger")) {
            setImage(idle);
        }
        
        if (Greenfoot.mouseClicked(this)) {
            if (world.currentlySelectedCard.equals("geiger")) {
                world.currentlySelectedCard = "none";
                setImage(idle);
            }
            else if (world.canAfford(cost)) {
                world.currentlySelectedCard = "geiger";
                setImage(selected);
            }
        }
    }
    
    public void startCooldown() {
        cooldownTimer = COOLDOWN_TIME;
    }
}