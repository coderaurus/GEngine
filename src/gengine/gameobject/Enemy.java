package gengine.gameobject;

import gengine.Direction;
import java.util.List;
import java.util.Random;

/**
 * This is the base class for moving enemy object which is constantly on the move.
 * When colliding with other non-player related objects it randomly heads
 * to other direction.
 *
 * @author Lauri Pirttimaki
 * @version 0.2
 */
public class Enemy extends MovableObject{
    /**
     * Default constructor. Sets objectType to ENEMY and randomizes
     * the initial direction.
     */
    public Enemy() {
        super();
        this.setType(ObjectType.ENEMY);
        randomizeDirection();
    }

    /**
     * Default constructor. Sets objectType to ENEMY and randomizes
     * the initial direction. Takes the coordinates and dimensions as parameters.
     *
     * @param x - x coordinate
     * @param y - y coordinate
     * @param w - width of the enemy object
     * @param h - height of the enemy object
     */
    public Enemy(int x, int y, int w, int h) {
        super(x, y, w, h);
        this.setType(ObjectType.ENEMY);
        randomizeDirection();
    }

    /**
     * Method handles logic. When hit with a non-player related object it turns
     * to a random other direction and proceeds its movement. If hit with player object, reduce its health by one.
     *
     * @param objects - all related objects
     * @param delta - time between frames
     */
    @Override
    public void update(List<GameObject> objects, long delta) {
        GameObject hitObj = null;
        //Check which directions are available
        setMovableDirections(canMove(objects));
        move(getDirection(), delta);
       
        hitObj = collides(getDirection(), objects, delta);
        if(hitObj != null && hitObj.getType() != ObjectType.PLAYER){
            //randomize next direction
            setMovableDirections(canMove(objects));
            moveNextTo(hitObj);
            randomizeDirection();
        }
        else if(hitObj.getClass().toString().equals("Player")) {
            Player player = (Player) hitObj;
            if(player.getHealth() > 0) {
                player.setHealth(hitObj.getHealth() - 1);
            }
        }
    }

    /**
     * Method randomizes a free direction for enemy object.
     * It also sets isMoving to true in case it already wasn't it.
     */
    public void randomizeDirection() {
        boolean dirFound = false;
        Random rand = new Random();
        while(!dirFound) {
            int i = rand.nextInt(4);
            if(getMovableDirection(i)) {
                if(i == 0) {
                    setDirection(Direction.UP);
                }
                else if(i == 1) {
                    setDirection(Direction.DOWN);
                }
                else if(i == 2) {
                    setDirection(Direction.LEFT);
                }
                else if(i == 3) {
                    setDirection(Direction.RIGHT);
                }
                dirFound = true;
            }
        }
        if(!isMoving()) {
            setMoving(true);
        }
    }
}