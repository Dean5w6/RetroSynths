import greenfoot.*;
import java.util.List;
import java.util.ArrayList;

public class PlasmaBall extends Actor {
    
    private GreenfootImage normalImg = new GreenfootImage("boss_plasma_normal.png");
    private GreenfootImage explode1Img = new GreenfootImage("boss_plasma_exploding1.png");
    private GreenfootImage explode2Img = new GreenfootImage("boss_plasma_exploding2.png");
     
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
            penetrationDistance += Math.abs(MOVE_SPEED);
            if (penetrationDistance >= 42) { 
                for (Defender target : targets) {
                    if (target.getWorld() != null) {  
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
                targets = new ArrayList<Defender>();
                 
                targets.add(primaryTarget);
                 
                List<Defender> defendersAbove = getWorld().getObjectsAt(primaryTarget.getX(), primaryTarget.getY() - Level1.TILE_HEIGHT, Defender.class);
                 
                targets.addAll(defendersAbove); 
                
            } else if (isAtEdge()) {
                getWorld().removeObject(this);
            }
        }
    }
    
    private void handleExplosionAnimation() {
        explosionTimer--;
        if (explosionTimer == 0) {
            setImage(explode2Img);
            explosionTimer = -EXPLOSION_FRAME_DURATION; 
        } else if (explosionTimer < -EXPLOSION_FRAME_DURATION) {
            getWorld().removeObject(this);
        }
    }
}