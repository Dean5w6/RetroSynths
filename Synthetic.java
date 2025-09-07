import greenfoot.*;

public abstract class Synthetic extends LivingEntity {
    
    private static final int DEFEAT_LINE_X = 234;

    public Synthetic(int startingHealth) {
        super(startingHealth);
    }
    
    @Override
    protected void onDefeated() {
        Level1 world = (Level1) getWorld();
        if (world == null) { 
            return; 
        } 

        if (this instanceof AnnihilatorTank) {
            world.playerHasWon();
        } else {
            world.enemyDefeated();
        }
    }
    
    protected void checkDefeatCondition() {
        if (getX() <= DEFEAT_LINE_X) {
            Greenfoot.setWorld(new DefeatScreen());
        }
    }
}