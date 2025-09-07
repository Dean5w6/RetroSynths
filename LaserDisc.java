// === LaserDisc (Projectile) - COMPLETE REVISED CODE ===
import greenfoot.*;

public class LaserDisc extends Actor {

    private GreenfootImage normalFrame = new GreenfootImage("disc_normal.png");
    private GreenfootImage explodingFrame = new GreenfootImage("disc_exploding.png");
    
    private static final int DAMAGE = 20;
    private static final int MOVE_SPEED = 8;

    // State variables
    private boolean isExploding = false;
    private LivingEntity targetEnemy = null; // Renamed for clarity
    private int penetrationDistance = 0;
    
    // --- NEW: Variable to hold the penetration limit for the current target ---
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
            // We have hit something and are now penetrating.
            penetrationDistance += MOVE_SPEED;
            // --- REVISED: Check against the dynamic penetration limit ---
            if (penetrationDistance >= penetrationLimit) {
                isExploding = true;
                setImage(explodingFrame);
            }
        } 
        else {
            // --- THE FIX: We now search for ANY Synthetic ---
            Synthetic enemy = (Synthetic) getOneIntersectingObject(Synthetic.class);
            if (enemy != null) {
                // First contact!
                enemy.takeDamage(DAMAGE);
                targetEnemy = enemy; // Store the enemy we hit.
                
                // --- NEW: Set the penetration limit based on the enemy type ---
                if (enemy instanceof AnnihilatorTank) {
                    penetrationLimit = 70; // Boss has low penetration
                } else {
                    penetrationLimit = 70; // Drones have high penetration
                }
                
            } else if (isAtEdge()) {
                getWorld().removeObject(this);
            }
        }
    }
}