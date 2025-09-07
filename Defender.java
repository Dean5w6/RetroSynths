import greenfoot.*;

public abstract class Defender extends LivingEntity {
    
    public Defender(int health) {
        super(health);
    }
    
    @Override
    protected void onDefeated() {
    }
}