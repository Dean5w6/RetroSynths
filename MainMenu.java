import greenfoot.*;

public class MainMenu extends World {

    public MainMenu() {
        super(889, 500, 1); 
        
        GreenfootImage bg = new GreenfootImage("main_menu_screen.png");
        bg.scale(getWidth(), getHeight());
        setBackground(bg);
    }
    
    public void act() {
        if (Greenfoot.mouseClicked(null)) {
            Greenfoot.setWorld(new Level1());
        }
    }
}