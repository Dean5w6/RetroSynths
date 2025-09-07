import greenfoot.*;
import java.util.List;

public class Level1 extends World {
 
    public static final int TILE_WIDTH = 84;
    public static final int TILE_HEIGHT = 95;
    public static final int GRID_OFFSET_X = 284;
    public static final int GRID_OFFSET_Y = 13;

    private static final int GRID_COLUMNS = 7;
    private static final int GRID_ROWS = 5;

    private int wave2Defeats = 0;
    private static final int ENEMIES_FOR_BOSS = 25;
    private boolean bossHasSpawned = false;

    private int waveMessageTimer = 0;

    private int plutonium = 200;
    public String currentlySelectedCard = "none";

    private GhostPlaceholder ghost;

    private int enemiesDefeated = 0;
    private int currentWave = 1;
    private static final int ENEMIES_FOR_WAVE_2 = 5;

    private boolean firstEnemyHasSpawned = false;

    private int spawnTimer = 1800; // 30 seconds for first enemy
    private int resourceDropTimer = 600;

    private GeigerCard geigerCard;
    private GunnerCard gunnerCard;
    private WrenchCard wrenchCard;

    private int victoryDelay = -1;

    public Level1() {
        super(889, 500, 1, false); 

        GreenfootImage bg = new GreenfootImage("level1_background.png");
        bg.scale(getWidth(), getHeight());
        setBackground(bg);

        geigerCard = new GeigerCard();
        gunnerCard = new GunnerCard();
        wrenchCard = new WrenchCard();
        addObject(geigerCard, 80, 110);
        addObject(gunnerCard, 80, 165);
        addObject(wrenchCard, 55, 460);

        updatePlutoniumDisplay();

        // drawDebugGrid(); 
        // spawnTestBoss();
        prepare();
    }

    private void drawDebugGrid() {
        GreenfootImage grid = getBackground();
        grid.setColor(Color.RED);
        int rows = 5;
        int cols = 7; 

        // Draw horizontal lines
        for (int i = 0; i <= rows; i++) {
            int y = GRID_OFFSET_Y + i * TILE_HEIGHT;
            grid.drawLine(GRID_OFFSET_X, y, GRID_OFFSET_X + cols * TILE_WIDTH, y);
        }
        // Draw vertical lines
        for (int i = 0; i <= cols; i++) {
            int x = GRID_OFFSET_X + i * TILE_WIDTH;
            grid.drawLine(x, GRID_OFFSET_Y, x, GRID_OFFSET_Y + rows * TILE_HEIGHT);
        }
    }

    private void spawnTestBoss() {
        int randomLane = Greenfoot.getRandomNumber(5);
        int spawnY = randomLane * TILE_HEIGHT + GRID_OFFSET_Y + TILE_HEIGHT / 2 - 32;
        addObject(new AnnihilatorTank(), getWidth() + 20, spawnY);
    }

    public void act() {
        handleVictoryCondition();

        if (!bossHasSpawned) {
            handleSpawning();
        }

        handleGridClick();
        handleResourceDrops();
        handleGhostPlaceholder();
        handleWaveMessage();
    }

    public void playerHasWon() {
        victoryDelay = 180; 
    }

    private void handleVictoryCondition() {
        if (victoryDelay > 0) {
            victoryDelay--;
            if (victoryDelay == 0) {
                Greenfoot.setWorld(new VictoryScreen());
            }
        }
    }

    private void handleGhostPlaceholder() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse == null) { return; }

        if (!currentlySelectedCard.equals("none") && ghost == null) {
            if (currentlySelectedCard.equals("geiger")) {
                ghost = new GhostPlaceholder("geiger_ghost.png");
            } else if (currentlySelectedCard.equals("gunner")) {
                ghost = new GhostPlaceholder("gunner_ghost.png");
            } else if (currentlySelectedCard.equals("wrench")) {
                ghost = new GhostPlaceholder("ghost_wrench.png");
            }
            addObject(ghost, mouse.getX(), mouse.getY());
        }

        if (currentlySelectedCard.equals("none") && ghost != null) {
            removeObject(ghost);
            ghost = null;
        }

        if (ghost != null) {
            ghost.setLocation(mouse.getX(), mouse.getY());
        }
    }

    public void enemyDefeated() {
        enemiesDefeated++;

        if (currentWave == 1 && enemiesDefeated >= ENEMIES_FOR_WAVE_2) {
            currentWave = 2;
            showText("Wave 2 has begun!", getWidth() / 2, 50);
            waveMessageTimer = 300; 
        }

        if (currentWave == 2 && !bossHasSpawned) {
            wave2Defeats++;
            if (wave2Defeats >= ENEMIES_FOR_BOSS) {
                spawnBoss();
            }
        }
    }

    private void handleWaveMessage() {
        if (waveMessageTimer > 0) {
            waveMessageTimer--;
            if (waveMessageTimer == 0) {

                showText("", getWidth() / 2, 50); 
                showText("", getWidth() / 2, 50);
            }
        }
    }

    private void spawnBoss() {
        bossHasSpawned = true;
        showText("BOSS INCOMING!!", getWidth() / 2, 50);
        waveMessageTimer = 300; 
        int randomLane = Greenfoot.getRandomNumber(5);
        int spawnY = randomLane * TILE_HEIGHT + GRID_OFFSET_Y + TILE_HEIGHT / 2 - 32;
        addObject(new AnnihilatorTank(), getWidth() + 20, spawnY);
    }

    private void handleResourceDrops() {
        resourceDropTimer--;
        if (resourceDropTimer <= 0) {
            int randomColumn = Greenfoot.getRandomNumber(7); 
            int spawnX = GRID_OFFSET_X + (randomColumn * TILE_WIDTH) + (TILE_WIDTH / 2);

            int spawnY = GRID_OFFSET_Y;

            int stopY = GRID_OFFSET_Y + (4 * TILE_HEIGHT) + (TILE_HEIGHT / 2);

            addObject(new PlutoniumCell(stopY), spawnX, spawnY);

            resourceDropTimer = 1200;
        }
    }

    private void handleSpawning() {
        spawnTimer--;
        if (spawnTimer <= 0) {
            if (!firstEnemyHasSpawned) {
                spawnFirstDrone();
                firstEnemyHasSpawned = true;
                spawnTimer = 2400;  
            } 
          
            else {
                spawnWaveDrone();
            }
        }
    }

    private void spawnFirstDrone() {
        int randomLane = Greenfoot.getRandomNumber(5);
        int spawnY = randomLane * TILE_HEIGHT + GRID_OFFSET_Y + TILE_HEIGHT / 2 - 5;
      
        addObject(new ChromeDrone(true), getWidth() - 50, spawnY);
    }
 
    private void spawnWaveDrone() {
        int randomLane = Greenfoot.getRandomNumber(5);
        int spawnY = randomLane * TILE_HEIGHT + GRID_OFFSET_Y + TILE_HEIGHT / 2 - 5;
        addObject(new ChromeDrone(false), getWidth() - 50, spawnY); // Pass 'false' for normal drone

        if (currentWave == 1) { 
            spawnTimer = 600 + Greenfoot.getRandomNumber(300);
        } else { 
            if (Greenfoot.getRandomNumber(2) == 0) {
                int secondLane = Greenfoot.getRandomNumber(5);
                int secondSpawnY = secondLane * TILE_HEIGHT + GRID_OFFSET_Y + TILE_HEIGHT / 2 - 5;
                addObject(new ChromeDrone(false), getWidth() - 50, secondSpawnY);
            }
            spawnTimer = 240 + Greenfoot.getRandomNumber(120);
        }
    }

    private void handleGridClick() {
        if (Greenfoot.mouseClicked(null)) {
            MouseInfo mouse = Greenfoot.getMouseInfo(); 
            if (mouse == null || mouse.getActor() instanceof UIElement) {
                return;
            }

            if (!currentlySelectedCard.equals("none")) {
                int tileX = (mouse.getX() - GRID_OFFSET_X) / TILE_WIDTH;
                int tileY = (mouse.getY() - GRID_OFFSET_Y) / TILE_HEIGHT;

                if (tileX >= 0 && tileX < GRID_COLUMNS && tileY >= 0 && tileY < GRID_ROWS) {
                    if (currentlySelectedCard.equals("wrench")) {
                        removeDefenderAt(tileX, tileY);
                    } else { 
                        placeDefenderAt(tileX, tileY);
                    }
                } else {
                    currentlySelectedCard = "none";
                }
            }
        }
    }

    private void removeDefenderAt(int tileX, int tileY) {
        int worldX = tileX * TILE_WIDTH + GRID_OFFSET_X + TILE_WIDTH / 2;
        int worldY = tileY * TILE_HEIGHT + GRID_OFFSET_Y + TILE_HEIGHT / 2;

        List<Defender> defendersOnTile = getObjectsAt(worldX, worldY, Defender.class);
        if (!defendersOnTile.isEmpty()) { 
            removeObject(defendersOnTile.get(0)); 
        }
 
        currentlySelectedCard = "none";
    }

    private void placeDefenderAt(int tileX, int tileY) {
        int worldX = tileX * TILE_WIDTH + GRID_OFFSET_X + TILE_WIDTH / 2;
        int worldY = tileY * TILE_HEIGHT + GRID_OFFSET_Y + TILE_HEIGHT / 2;

        List<Defender> defendersOnTile = getObjectsAt(worldX, worldY, Defender.class);
        if (!defendersOnTile.isEmpty()) {
            return;  
        }

        if (currentlySelectedCard.equals("geiger")) {
            if (canAfford(100)) {
                addObject(new GeigerCounter(), worldX, worldY);
                addPlutonium(-100);
                currentlySelectedCard = "none";
                geigerCard.startCooldown();  
            }
        } else if (currentlySelectedCard.equals("gunner")) {
            if (canAfford(200)) {
                addObject(new LaserDiscGunner(), worldX, worldY);
                addPlutonium(-200);
                currentlySelectedCard = "none";
                gunnerCard.startCooldown();  
            }
        }
    }

    public void addPlutonium(int amount) {
        plutonium += amount;
        updatePlutoniumDisplay();
    }

    public boolean canAfford(int cost) {
        return plutonium >= cost;
    }

    public void updatePlutoniumDisplay() {
        showText("" + plutonium, 92, 40);
    }

    private void prepare()
    {
    }
}