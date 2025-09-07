// === PlasmaBall (COMPLETE REVISED CODE) ===
import greenfoot.*;
import java.util.List;
import java.util.ArrayList;

public class PlasmaBall extends Actor {
    
    private GreenfootImage normalImg = new GreenfootImage("boss_plasma_normal.png");
    private GreenfootImage explode1Img = new GreenfootImage("boss_plasma_exploding1.png");
    private GreenfootImage explode2Img = new GreenfootImage("boss_plasma_exploding2.png");
    
    // --- REVISED: Projectile now moves left ---
    private static final int MOVE_SPEED = -5; 
    private static final int DAMAGE = 500;
    
    private List<Defender> targets = null;
    private int penetrationDistance = 0;
    private boolean isExploding = false;
    private int explosionTimer = 0;
    private static final int EXPLOSION_FRAME_DURATION = 5;

    public PlasmaBall() {
        int width = 130;
        int height = 104;
        normalImg.scale(width, height);
        explode1Img.scale(width, height);
        explode2Img.scale(width, height);
        
        setImage(normalImg);
    }
    
    public void act() {
        if (isExploding) {
            handleExplosionAnimation();
            return;
        }
        
        setLocation(getX() + MOVE_SPEED, getY());
        
        if (targets != null) {
            // We have a target list, so we are penetrating
            penetrationDistance += Math.abs(MOVE_SPEED);
            if (penetrationDistance >= 42) {
                // --- REVISED: Damage all targets in the list ---
                for (Defender target : targets) {
                    if (target.getWorld() != null) { // Make sure the target hasn't already been destroyed
                        target.takeDamage(DAMAGE);
                    }
                }
                isExploding = true;
                explosionTimer = EXPLOSION_FRAME_DURATION;
                setImage(explode1Img);
            }
        } else {
            Defender primaryTarget = (Defender) getOneIntersectingObject(Defender.class);
            if (primaryTarget != null) {
                // --- THE FIX: Create our own modifiable list ---
                // 1. Create a new, empty ArrayList that we can control.
                targets = new ArrayList<Defender>();
                
                // 2. Add the defender we directly hit.
                targets.add(primaryTarget);
                
                // 3. Find any defenders in the tile directly above.
                List<Defender> defendersAbove = getWorld().getObjectsAt(primaryTarget.getX(), primaryTarget.getY() - Level1.TILE_HEIGHT, Defender.class);
                
                // 4. Add all of them to our list. This is safe even if the list is empty.
                targets.addAll(defendersAbove);
                // --- END OF FIX ---
                
            } else if (isAtEdge()) {
                getWorld().removeObject(this);
            }
        }
    }
    
    private void handleExplosionAnimation() {
        explosionTimer--;
        if (explosionTimer == 0) {
            setImage(explode2Img);
            explosionTimer = -EXPLOSION_FRAME_DURATION; // Use negative to mark frame 2
        } else if (explosionTimer < -EXPLOSION_FRAME_DURATION) {
            getWorld().removeObject(this);
        }
    }
}