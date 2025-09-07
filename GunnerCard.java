import greenfoot.*;

public class GunnerCard extends DefenderCard {
    
    GreenfootImage idle = new GreenfootImage("gunner_card_idle.png");
    GreenfootImage selected = new GreenfootImage("gunner_card_selected.png");
    GreenfootImage cooldownImage = new GreenfootImage("gunner_card_cooldown.png");
    
    int cost = 200;

    private int cooldownTimer = 0;
    public static final int COOLDOWN_TIME = 300;

    public GunnerCard() {
        idle.scale(100, 50);
        selected.scale(100, 50);
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
        
        if (!world.currentlySelectedCard.equals("gunner")) {
            setImage(idle);
        }
         
        if (Greenfoot.mouseClicked(this)) { 
            if (world.currentlySelectedCard.equals("gunner")) {
                world.currentlySelectedCard = "none";
                setImage(idle);
            }

            else if (world.canAfford(cost)) {
                world.currentlySelectedCard = "gunner";
                setImage(selected);
            }
        }
    }
    
    public void startCooldown() {
        cooldownTimer = COOLDOWN_TIME;
    }
}