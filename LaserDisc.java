// === LaserDisc (Projectile) - COMPLETE REVISED CODE ===
import greenfoot.*;

public class LaserDisc extends Actor {

    private GreenfootImage normalFrame = new GreenfootImage("disc_normal.png");
    private GreenfootImage explodingFrame = new GreenfootImage("disc_exploding.png");
    
    private static final int DAMAGE = 20;
    private static final int MOVE_SPEED = 8;
 
    private boolean isExploding = false;
    private LivingEntity targetEnemy = null; 
    private int penetrationDistance = 0;
     
    private int penetrationLimit;
    
    private int explosionTimer = 5;

    public LaserDisc() {
        normalFrame.scale(54, 45);
        explodingFrame.scale(54, 45);
        setImage(normalFrame);
    }
    
    public void act() {
        if (isExploding) {
            explosionTimer--;
            if (explosionTimer <= 0) {
                getWorld().removeObject(this);
            }
            return;
        }

        setLocation(getX() + MOVE_SPEED, getY());
        
        if (targetEnemy != null) { 
            penetrationDistance += MOVE_SPEED; 
            if (penetrationDistance >= penetrationLimit) {
                isExploding = true;
                setImage(explodingFrame);
            }
        } 
        else { 
            Synthetic enemy = (Synthetic) getOneIntersectingObject(Synthetic.class);
            if (enemy != null) { 
                enemy.takeDamage(DAMAGE);
                targetEnemy = enemy; 
                 
                if (enemy instanceof AnnihilatorTank) {
                    penetrationLimit = 70; 
                } else {
                    penetrationLimit = 70;  
                }
                
            } else if (isAtEdge()) {
                getWorld().removeObject(this);
            }
        }
    }
}