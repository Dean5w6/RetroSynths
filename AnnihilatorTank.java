// === AnnihilatorTank (COMPLETE REVISED CODE WITH STATE-BASED RAMMING) ===
import greenfoot.*;
import java.util.List;

public class AnnihilatorTank extends Synthetic {

    private enum State { MOVING, IDLE, CHARGING, ATTACKING }
    private State currentState = State.MOVING;

    private GreenfootImage movingImg = new GreenfootImage("boss_tank_moving.png");
    private GreenfootImage idleImg = new GreenfootImage("boss_tank_idle.png");
    private GreenfootImage chargingImg = new GreenfootImage("boss_tank_charging.png");
    private GreenfootImage attack1Img = new GreenfootImage("boss_tank_attack1.png");
    private GreenfootImage attack2Img = new GreenfootImage("boss_tank_attack2.png");

    private int stateTimer = 0;
    private static final int MOVE_DURATION = 300;
    private static final int IDLE_DURATION = 180;
    private static final int CHARGE_DURATION = 180;
    private static final int ATTACK_DURATION_1 = 15;
    private static final int ATTACK_DURATION_2 = 15;
    
    private double trueX;
    private static final double MOVE_SPEED = 0.10;
    
    private static final int ATTACK_SCAN_RANGE = 700;

    private static final int RAM_DAMAGE = 5000;
    private LivingEntity ramTarget = null;
    private int ramPenetration = 0;

    public AnnihilatorTank() {
        super(1800);
        
        int width = 230;
        int height = 141;
        movingImg.scale(width, height);
        idleImg.scale(width, height);
        chargingImg.scale(width, height);
        attack1Img.scale(width, height);
        attack2Img.scale(width, height);
        
        setImage(movingImg);
        stateTimer = MOVE_DURATION;
    }
    
    @Override
    protected void addedToWorld(World world) {
        super.addedToWorld(world);
        this.trueX = getX();
    }

    public void act() {
        checkDefeatCondition();
        
        stateTimer--;
        switch (currentState) {
            case MOVING:
                handleRamming();
                
                trueX -= MOVE_SPEED;
                setLocation((int) Math.round(trueX), getY());
                
                if (stateTimer <= 0) {
                    if (scanForTarget()) {
                        currentState = State.IDLE;
                        stateTimer = IDLE_DURATION;
                        setImage(idleImg);
                    } else {
                        stateTimer = MOVE_DURATION;
                    }
                }
                break;
            case IDLE:
                if (stateTimer <= 0) {
                    currentState = State.CHARGING;
                    stateTimer = CHARGE_DURATION;
                    setImage(chargingImg);
                }
                break;
            case CHARGING:
                if (stateTimer <= 0) {
                    currentState = State.ATTACKING;
                    stateTimer = ATTACK_DURATION_1;
                    setImage(attack1Img);
                    getWorld().addObject(new PlasmaBall(), getX() - 80, getY() - 15);
                }
                break;
            case ATTACKING:
                if (stateTimer == 0) {
                    setImage(attack2Img);
                    stateTimer = -ATTACK_DURATION_2;
                }
                if (stateTimer < -ATTACK_DURATION_2) {
                    currentState = State.MOVING;
                    stateTimer = MOVE_DURATION;
                    setImage(movingImg);
                }
                break;
        }
    }
    
    private boolean scanForTarget() {
        List<Defender> potentialTargets = getObjectsInRange(ATTACK_SCAN_RANGE, Defender.class);
        for (Defender defender : potentialTargets) {
            boolean isInFront = defender.getX() < this.getX();
            boolean isInLane = Math.abs(defender.getY() - this.getY()) < Level1.TILE_HEIGHT / 2;
            
            if (isInFront && isInLane) {
                return true;
            }
        }
        return false;
    }
    
    private void handleRamming() {
        if (ramTarget != null) {
            ramPenetration++;
            if (ramPenetration >= 10) {
                ramTarget.takeDamage(RAM_DAMAGE);
                ramTarget = null;
                ramPenetration = 0;
            }
        } else {
            LivingEntity defender = (LivingEntity) getOneIntersectingObject(Defender.class);
            if (defender != null) {
                ramTarget = defender;
            }
        }
    }
}