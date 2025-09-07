import greenfoot.*;

public class ChromeDrone extends Synthetic {

    private GreenfootImage walkingFrame = new GreenfootImage("drone_walk.png");
    private GreenfootImage idleFrame = new GreenfootImage("drone_idle.png");
    private GreenfootImage attackFrame = new GreenfootImage("drone_attack.png");
    
    private int moveTimer = 0;
    private int pauseDuration = 180;
    private int moveSpeed = 2;
    private int distanceMoved = 0;
    
    private int attackTimer = 0;
    private static final int ATTACK_INTERVAL = 90;
    
    private int damage; 
    
    private boolean isAttacking = false;

    public ChromeDrone(boolean isFirst) {
        super(200);
        
        if (isFirst) {
            this.damage = 40;
        } else {
            this.damage = 75; 
        }
        
        walkingFrame.scale(120, 96);
        idleFrame.scale(120, 96);
        attackFrame.scale(120, 96);
        setImage(idleFrame);
    }

    public void act() {
        checkDefeatCondition();
        LivingEntity defender = (LivingEntity) getOneObjectAtOffset(-50, 0, Defender.class);
        
        if (isAttacking) {
            handleAttackAnimation();
        }
        else if (defender != null) {
            isAttacking = true;
            handleAttackAnimation();
        }
        else {
            handleMovement();
        }
    }
    
    private void handleAttackAnimation() {
        attackTimer++;
        if (attackTimer == 1) {
            setImage(attackFrame);
            LivingEntity target = (LivingEntity) getOneObjectAtOffset(-50, 0, Defender.class);
            if (target != null) {
                target.takeDamage(this.damage);
            }
        } else if (attackTimer == 30) {
            setImage(idleFrame);
        } else if (attackTimer >= ATTACK_INTERVAL) {
            attackTimer = 0;
            isAttacking = false;
        }
    }
    
    private void handleMovement() {
        moveTimer++;
        if (moveTimer > pauseDuration) {
            setImage(walkingFrame);
            setLocation(getX() - moveSpeed, getY());
            distanceMoved += moveSpeed;
            if (distanceMoved >= 30) {
                moveTimer = 0;
                distanceMoved = 0;
                setImage(idleFrame);
            }
        }
    }
}