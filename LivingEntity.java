import greenfoot.*;

public abstract class LivingEntity extends Actor {
    
    protected int health;

    public LivingEntity(int startingHealth) {
        this.health = startingHealth;
    }
    
    protected abstract void onDefeated();
    
    public void takeDamage(int amount) {
        health -= amount;
        if (health <= 0) {
            onDefeated(); 
            
            getWorld().removeObject(this);
        }
    }
}