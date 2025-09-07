import greenfoot.*;
import java.util.List;

public class Level1 extends World {

    // Grid Calibration Controls
    public static final int TILE_WIDTH = 84;
    public static final int TILE_HEIGHT = 95;
    public static final int GRID_OFFSET_X = 284;
    public static final int GRID_OFFSET_Y = 13;

    private static final int GRID_COLUMNS = 7;
    private static final int GRID_ROWS = 5;

    // --- NEW: Wave 3 / Boss Wave state ---
    private int wave2Defeats = 0;
    private static final int ENEMIES_FOR_BOSS = 25;
    private boolean bossHasSpawned = false;

    // --- NEW: Timer for fading text ---
    private int waveMessageTimer = 0;

    // Game State
    private int plutonium = 200;
    public String currentlySelectedCard = "none";

    private GhostPlaceholder ghost;

    // --- NEW: Wave System Variables ---
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
        grid.setColor(Color.RED); // You can change this color if you want
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
        // Start the 3-second victory timer
        victoryDelay = 180; // 3 seconds * 60 acts/sec
    }

    private void handleVictoryCondition() {
        if (victoryDelay > 0) { // Is the timer active?
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

    // --- NEW: Public method for entities to report their defeat ---
    public void enemyDefeated() {
        enemiesDefeated++;

        if (currentWave == 1 && enemiesDefeated >= ENEMIES_FOR_WAVE_2) {
            currentWave = 2;
            showText("Wave 2 has begun!", getWidth() / 2, 50);
            waveMessageTimer = 300; // Start the 5-second fade timer
        }

        // --- NEW: Check for boss spawn condition ---
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
                // After the timer runs out, clear both potential message spots.
                showText("", getWidth() / 2, 50); // Clears "Wave 2" message
                showText("", getWidth() / 2, 50); // Clears "BOSS INCOMING!!" message
            }
        }
    }

    private void spawnBoss() {
        bossHasSpawned = true;
        showText("!! BOSS INCOMING!!", getWidth() / 2, 50);
        // --- NEW: Reuse the wave message timer to make this text disappear ---
        waveMessageTimer = 300; // 5 seconds

        int randomLane = Greenfoot.getRandomNumber(5);
        int spawnY = randomLane * TILE_HEIGHT + GRID_OFFSET_Y + TILE_HEIGHT / 2 - 32;
        addObject(new AnnihilatorTank(), getWidth() + 20, spawnY);
    }

    // --- NEW METHOD: Handles the spawning of free resources ---
    private void handleResourceDrops() {
        resourceDropTimer--;
        if (resourceDropTimer <= 0) {
            // Calculate a random X position within the playable grid area
            int randomColumn = Greenfoot.getRandomNumber(7); // 7 columns, 0-6
            int spawnX = GRID_OFFSET_X + (randomColumn * TILE_WIDTH) + (TILE_WIDTH / 2);

            // The starting Y position at the top of the grid
            int spawnY = GRID_OFFSET_Y;

            // The Y position of the center of the bottom-most lane (lane 4)
            int stopY = GRID_OFFSET_Y + (4 * TILE_HEIGHT) + (TILE_HEIGHT / 2);

            // Create the cell and tell it where to stop falling
            addObject(new PlutoniumCell(stopY), spawnX, spawnY);

            // Reset the timer for the next drop (20 seconds)
            resourceDropTimer = 1200;
        }
    }

    private void handleSpawning() {
        spawnTimer--;
        if (spawnTimer <= 0) {
            // CASE 1: The very first enemy has NOT spawned yet.
            if (!firstEnemyHasSpawned) {
                spawnFirstDrone();
                firstEnemyHasSpawned = true;
                // Set the special 40-second delay before Wave 1 truly begins.
                spawnTimer = 2400; // 40 seconds * 60 acts/sec
            } 
            // CASE 2: The first enemy has spawned, now we do normal waves.
            else {
                spawnWaveDrone();
            }
        }
    }

    // A new helper method to spawn the weak first drone
    private void spawnFirstDrone() {
        int randomLane = Greenfoot.getRandomNumber(5);
        int spawnY = randomLane * TILE_HEIGHT + GRID_OFFSET_Y + TILE_HEIGHT / 2 - 5;
        // We pass 'true' to the constructor to make this a weak drone
        addObject(new ChromeDrone(true), getWidth() - 50, spawnY);
    }

    // A new helper method for all subsequent wave spawns
    private void spawnWaveDrone() {
        int randomLane = Greenfoot.getRandomNumber(5);
        int spawnY = randomLane * TILE_HEIGHT + GRID_OFFSET_Y + TILE_HEIGHT / 2 - 5;
        addObject(new ChromeDrone(false), getWidth() - 50, spawnY); // Pass 'false' for normal drone

        if (currentWave == 1) {
            // Wave 1: 10 to 15 seconds
            spawnTimer = 600 + Greenfoot.getRandomNumber(300);
        } else {
            // Wave 2: 4 to 6 seconds with 50% bonus drone
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
            // Expanded this check for safety
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
                        // This now calls the fixed method
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
            // Found a defender, remove the first one in the list.
            removeObject(defendersOnTile.get(0));
            // Optional: You could add a refund here, e.g., addPlutonium(50);
        }

        // Always deselect the wrench after one use attempt (success or failure).
        currentlySelectedCard = "none";
    }

    private void placeDefenderAt(int tileX, int tileY) {
        int worldX = tileX * TILE_WIDTH + GRID_OFFSET_X + TILE_WIDTH / 2;
        int worldY = tileY * TILE_HEIGHT + GRID_OFFSET_Y + TILE_HEIGHT / 2;

        List<Defender> defendersOnTile = getObjectsAt(worldX, worldY, Defender.class);
        if (!defendersOnTile.isEmpty()) {
            return; // Tile is occupied
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
    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    private void prepare()
    {
    }
}