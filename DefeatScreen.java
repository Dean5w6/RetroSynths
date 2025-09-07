import greenfoot.*;

public class DefeatScreen extends World {

    public DefeatScreen() {
        super(889, 500, 1); 
        GreenfootImage bg = new GreenfootImage("defeat_screen.png");
        bg.scale(getWidth(), getHeight());
        setBackground(bg);
    }
}