import greenfoot.*;
import java.util.List;

public class LaserDiscGunner extends Defender {

    private GreenfootImage idleImage = new GreenfootImage("gunner_idle.png");
    private GreenfootImage attackFrame1 = new GreenfootImage("gunner_attack1.png");
    private GreenfootImage attackFrame2 = new GreenfootImage("gunner_attack2.png");
    
    private int attackCooldown = 0;
    private int animationTimer = 0;
    private static final int ATTACK_RANGE = 600;

    public LaserDiscGunner() {
        super(300);
        
        int width = Level1.TILE_WIDTH - 10;
        int height = Level1.TILE_HEIGHT - 10;

        idleImage.scale(width, height);
        attackFrame1.scale(width, height);
        attackFrame2.scale(width, height);
        
        setImage(idleImage);
    }

    public void act() {
        attackCooldown++;
        
        if (animationTimer > 0) {
            animationTimer--;
            if (animationTimer == 5) {
                setImage(attackFrame2);
            } else if (animationTimer == 0) {
                setImage(idleImage);
            }
            return;
        }
        
        List<Synthetic> enemies = getObjectsInRange(ATTACK_RANGE, Synthetic.class);
        
        Synthetic target = null;
        for (Synthetic enemy : enemies) {
            boolean isInRange = enemy.getX() > getX();
            boolean isInLane = Math.abs(enemy.getY() - this.getY()) < Level1.TILE_HEIGHT / 2;
            
            if (isInRange && isInLane) {
                target = enemy;
                break;
            }
        }
        
        if (target != null && attackCooldown >= 90) {
            animationTimer = 10;
            setImage(attackFrame1);
            
            getWorld().addObject(new LaserDisc(), getX() + 40, getY() - 15);
            
            attackCooldown = 0; 
        }
    }
}