package gengine;

import java.util.Observable;
import java.util.Observer;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.Scene;
import gengine.gameobject.*;

/**
 * This is the manager class where overall logic of the game is handled.
 *
 * @author Lauri Pirttimaki
 * @version 0.3
 */
public class GameManager extends Thread implements Observer{
    private boolean gameOver;
    private int score;
    
    private Player player;
    private Controls controls;
    private List<GameObject> objects;
    
    private long previousTime;
    private long currentTime;

    private double startX;
    private double startY;

    private List<Map> maps;
    private Map currentMap;

    private List<Image> images;

    /**
     * The default constructor.
     * Sets up gameOver, player and list of other game objects.
     */
    public GameManager() {
        setGameOver(false);
        setScore(0);
        objects = new ArrayList<>();
        player = new Player();
    }

    /** 
    * Method sets next map as current one and calls for getMap which 
    * initializes everything. 
    * Note that in its current state, this method loops around the list.
    */
    public synchronized void nextLevel() {
        int currentIndex = maps.indexOf(currentMap);
        if(currentIndex + 1 < maps.size()) {
            setCurrentMap(maps.indexOf(maps.get(currentIndex)) + 1);
        }
        else {
            setCurrentMap(0);
        }
        getMap();
    }

    /**
     * Method sets the list containing all the images
     *
     * @param list - list of Image objects used
     */
    public void setImages(List<Image> list) {
        images = list;
    }

    /**
     * Metohd returns all the images used in a List
     */
    public List<Image> getImages() {
        return images;
    }

    /**
     * Method sets all the maps used in the game. Takes list of Map objects as parameter.
     * 
     * @param m - list of all available maps
     */
    public void setMaps(List<Map> m) {
        maps = m;
    }

    /**
     * Method sets a Map from specified index as the current map level.
     *
     * @param index - index of Map in list of maps
     */
    public void setCurrentMap(int index) {
        //Check the index is within bounds of the list
        if(index >= 0 && index < getMaps().size()) {
            currentMap = getMaps().get(index);
        }
    }

    /**
     * Method returns list of the maps.
     *
     * @return all the Map objects as a List
     */
    public List<Map> getMaps() {
        return maps;
    }

    /**
     * Method returns the current map
     *
     * @return currentMap object
     */
    public Map getCurrentMap() {
        return currentMap;
    }

    /**
     * Method sets score. Takes the value as parameter. Score cannot be
     * lower than zero.
     *
     * @param s - the new score value
     */
    public void setScore(int s) {
        if(s >= 0) {
            score = s;
        }
        else {
            score = 0;
        }
    }

    /**
     * Method sets starting x coordinate of player object.
     *
     * @param x  - x coordinate
     */
    public void setStartX(double x) {
        startX = x;
    }

    /**
     * Method sets starting y coordinate of player object.
     *
     * @param y  - y coordinate
     */
    public void setStartY(double y) {
        startY = y;
    }

    /**
     * Sets gameOver attribute.
     *
     * @param  isOver - wanted boolean for the attribute.
     */
    public void setGameOver(boolean isOver) {
        gameOver = isOver;
    }

    /**
     * Method sets the Controls object. Takes a Scene where we want control
     * as parameter.
     *
     * @param scene - controllable Scene
     */
    public void setControls(Scene scene) {
        controls = new Controls(scene, this);
    }

    /**
     * Returns gameOver attribute.
     *
     * @return gameOver - the attribute controlling game loop.
     */
    public boolean gameOver() { return gameOver; }
    /**
     * Returns the Player object.
     *
     * @return  player - object keeping track of player's attributes and status.
     */
    public Player getPlayer() { return player; }

    /**
     * Returns the starting x coordinate of player.
     *
     * @return x coordinate
     */
    public double getStartX() { return startX; }

    /**
     * Returns the starting y coordinate of player.
     *
     * @return y coordinate
     */
    public double getStartY() { return startY; }

    /**
     * Returns the current score.
     *
     * @return game score
     */
    public int getScore() { return score; }

    /**
     * Sets the previousTime
     *
     * Takes the value from currentTime
     */
    public void setPreviousTime() {
        previousTime = currentTime;
    }

    /**
     * Sets the currentTime
     *
     * Takes the current time from system.
     */
    public void setCurrentTime() {
        currentTime = System.nanoTime();
    }

    /**
     * Returns previousTime.
     *
     * @return previousTime value
     */
    public long getPreviousTime() { return previousTime; }

    /**
     * Returns currentTime.
     *
     * @return currentTime value
     */
    public long getCurrentTime() { return currentTime; }

    /**
     * Returns the substraction of currentTime and previousTime.
     *
     * @return time elapsed since last time in nanosecods
     */
    public long getDeltaTime() { return (long) ((getCurrentTime() - getPreviousTime()) / 1000000); }

    /**
     * Method adds gameManager as observer to all non-BLOCK objects.
     *
     * @param objects - all related objects
     */
    public void addObservers(List<GameObject> objects) {
        for( GameObject o : objects ) {
            if(o.getType() != ObjectType.BLOCK) {
                o.deleteObservers();
                o.addObserver(this);
            }
        }
    }

    /**
     * Method that creates and places map objects according to the current map.
     * images given represent Map marks in following order:
     *  1. P (player)
     *  2. x (wall)
     *  3. e (enemy)
     *  4. o (collectable)
     *  5. s (portal to next level)
     */
    public void getMap() {
        Map map = currentMap;
        objects = new ArrayList<>();
        char [][] mapCoords = map.coordinates;
        System.out.println("Map |  " + mapCoords.length);
        System.out.println("Map -- " + mapCoords[0].length);
        for(int i = 0; i < mapCoords.length; i++) {
            for(int j = 0; j < mapCoords[0].length; j++) {
                if(mapCoords[i][j] == 'P'){   // Player
                    player = new Player(0 + (32 * j), 32 + (32 * i), 32, 32);
                    player.setSprite(getImages().get(0));
                    setStartX(player.getX());
                    setStartY(player.getY());
                    objects.add(player);
                }
                else if(mapCoords[i][j] == 'x'){  // Blocks, obstacles
                    GameObject tile = new GameObject(0 + (32 * j), 32 + (32 * i), 32, 32);
                    tile.setSprite(getImages().get(1));
                    tile.setType(ObjectType.BLOCK);
                    objects.add(tile);
                }
                else  if(mapCoords[i][j] == 'e'){ // Enemies
                    Enemy tile = new Enemy(0 + (32 * j), 32 + (32 * i), 32, 32);
                    Random rand = new Random();
                    tile.setSpeed(rand.nextDouble() * 2.5 + 1.0);
                    tile.setSprite(getImages().get(2));
                    objects.add(tile);
                }
                else  if(mapCoords[i][j] == 'o'){ // Collectables
                    Collectable tile = new Collectable(0 + (32 * j), 32 + (32 * i), 32, 32);
                    tile.collectType = Collectable.Type.SCORE;
                    tile.setValue(100);
                    tile.setSprite(getImages().get(3));
                    objects.add(tile);
                }
                else  if(mapCoords[i][j] == 's'){ // Stairs to next level
                    GameObject tile = new GameObject(0 + (32 * j), 32 + (32 * i), 32, 32);
                    tile.setType(ObjectType.PORTAL);
                    tile.setSprite(getImages().get(4));
                    objects.add(tile);
                }
            }
        }
        addObservers(objects);
    }

    

    /**
     * Root drawing method for game objects.
     * It calls draw method from all the objects.
     *
     * @param  gc - object used for drawing to canvas.
     */
    public void draw(GraphicsContext gc) {
        for(GameObject o : objects) {
            o.draw(gc);
        }
    }

    /**
     * This method starts the game loop.
     */
    public void startGame() {
        System.out.println("Starting game loop..");
        setCurrentTime();
        setPreviousTime();
        this.start();
    }

    /**
     * The observing update method. When collectable is picked up its
     * value is added to score and then it is remove from the list of objects.
     *
     * @param o - Observable object
     * @param arg - notify argument of Observable object
     */
    @Override
    public void update(Observable o, Object arg) {
        System.out.println(o.getClass().toString());
        if(o.getClass().toString().contains("Collectable")){
            Collectable collectable = (Collectable) o;
            setScore(getScore() + collectable.getValue());

            // Delete picked up object from the map
            new Thread() {
                public void run() {
                    synchronized(objects) {
                        objects.remove(collectable);
                    }
                }
            }.start();

            System.out.println("Score: " + getScore());
        }
        else if(o.getClass().toString().contains("Portal")) {
            nextLevel();
        }
    }

    /**
     * Method is the master method for game logic handling. Calls for
     * all the other update methods in objects and tracks state of the game.
     *
     * @param delta - time between frames
     */
    public void updateGame(long delta) {
        for(GameObject o : objects) {
            if(o.getType() != ObjectType.BLOCK) {
                o.update(objects, delta);  // checkc collision in here
                o.updateSpriteCoords();
            }
            setGameOver(isGameOver());
        }
    }
    /**
     * Method checks whether any game ending conditions are met.
     * Currently checks if player has any health left.
     *
     * @return true if player has 0 health.
     */
    public boolean isGameOver() {
        if( player.getHealth() == 0 ) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * This method is the main game loop.
     * Calls for updateGame method frequently.
     */
    public void run() {
        try {
            setPreviousTime();
            while(!gameOver()) {
                setCurrentTime();
                synchronized(objects) {
                    updateGame(getDeltaTime()); // check collision, move, actions, ... , update sprites
                }
                Thread.sleep(16);
                setPreviousTime(); 
            }
            System.out.println("- - - - -");
            System.out.println("GAME OVER");
            System.out.println("- - - - -");
        }
        catch (InterruptedException e){ 
            e.printStackTrace();
        }
    }

    /*
    // Game Reset - WIP
    // ----------------

    public void resetGame() {
        System.out.println("GAME RESET");
        setGameOver(false);
        resetObjects();
    }

    public void resetObjects() {
        synchronized(objects) {
            for(GameObject o : objects) {

                if(o.getType() != ObjectType.BLOCK) {

                }
            }
        }
    }*/ 
}