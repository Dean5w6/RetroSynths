import greenfoot.*;

public class VictoryScreen extends World {

    public VictoryScreen() {
        super(889, 500, 1); 
        GreenfootImage bg = new GreenfootImage("victory_screen.png");
        bg.scale(getWidth(), getHeight());
        setBackground(bg);
    }
}