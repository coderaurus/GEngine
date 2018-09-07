package gengine.gameobject;

import java.util.List;
import gengine.Direction;
import javafx.geometry.Rectangle2D;
import java.util.Optional;
/**
 * This is the base class for moving game objects.
 * Has new attributes like speed and direction for movement and methods for them.
 *
 * @author Lauri Pirttimaki
 * @version 0.2
 */
public class MovableObject extends GameObject {
    private double speed;
    private boolean isMoving;
    private boolean[] movableDirections;

    private Direction direction;

    /**
     * The default constructor. Speed is set to 0 by default.
     * Also intializes movableDirections, which stores directions
     * which are free to move to, and direction to NONE.
     * 
     */
    public MovableObject() {
        super();
        setSpeed(0);
        movableDirections = new boolean[4];
        setMovableDirections(true, true, true, true);
        setDirection(Direction.NONE);
    }


    /**
     * The default constructor, takes coordinates and dimensions as parameters. 
     * Speed is set to 0 by default. Also intializes movableDirections, which stores directions
     * which are free to move to, and direction to NONE.
     * 
     * @param x - x coordinate
     * @param y - y coordinate
     * @param w - width of game object
     * @param h - height of game object
     */
    public MovableObject(int x, int y, int w, int h) {
        super(x, y, w, h);
        setSpeed(0);
        movableDirections = new boolean[4];
        setMovableDirections(true, true, true, true);
        setDirection(Direction.NONE);
    }

    /**
     * Sets speed attribute.
     * 
     * @param s - new speed value
     */
    public void setSpeed(double s) {
        speed = s;
    }

    /**
     * Sets direction attribute.
     * 
     * @param d - new direction of movement
     */
    public void setDirection(Direction d) {
        direction = d;
    }

    /**
     * Sets isMoving attribute
     * 
     * @param isMoving - boolean whether game object is moving.
     */
    public void setMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }

    public void setMovableDirections(boolean up, boolean down, boolean left, boolean right) {
        movableDirections[0] = up;
        movableDirections[1] = down;
        movableDirections[2] = left;
        movableDirections[3] = right;
    }

    public void setMovableDirections(boolean[] directions) {
        movableDirections[0] = directions[0];
        movableDirections[1] = directions[1];
        movableDirections[2] = directions[2];
        movableDirections[3] = directions[3];
    }

    public void setMovableDirections(Direction dir, boolean state) {
        if(dir == Direction.UP) {
            movableDirections[0] = state;
        }
        else if(dir == Direction.DOWN) {
            movableDirections[1] = state;
        }
        else if(dir == Direction.LEFT) {
            movableDirections[2] = state;
        }
        else if(dir == Direction.RIGHT) {
            movableDirections[3] = state;
        }
    }

    /**
     * Returns speed attribute
     *
     * @return speed attribute.
     */
    public double getSpeed() { return speed; }
    /**
     * Returns isMoving attribute
     *
     * @return isMoving attribute.
     */
    public boolean isMoving() { return isMoving; }
    /**
     * Returns direction attribute
     *
     * @return direction attribute.
     */
    public Direction getDirection() { return direction; }
    
    /**
     * Method returns states of free movement of all directions
     * as a boolean array.
     *
     * @return movableDirections 
     */
    public boolean[] getMovableDirections() {
        return movableDirections;
    }

    /**
     * Method returns the state of free movement of a single direction
     * stored in movableDirections. The order is UP, DOWN, LEFT and RIGHT.
     *
     * @param index - the index of wanted direction
     * @return true if direction is free for movement
     */
    public boolean getMovableDirection(int index) {
        return movableDirections[index];
    }

    /**
     * Method returns the state of free movement of a single direction
     * stored in movableDirections. The order is UP, DOWN, LEFT and RIGHT.
     * If none of the four directions is given method returns false.
     *
     * @param d - direction you want to check
     * @return true if direction is free for movement
     */
    public boolean getMovableDirection(Direction d) {
        if(getDirection() == Direction.UP) {
            return movableDirections[0];
        }
        else if(getDirection() == Direction.DOWN) {
            return movableDirections[1];
        }
        else if(getDirection() == Direction.LEFT) {
            return movableDirections[2];
        }
        else if(getDirection() == Direction.RIGHT) {
            return movableDirections[3];
        }
        else {
            return false;
        }
    }

    /**
     * Base method for handling logic. This includes collision, movement and other actions.
     * The logic follows:
     *      1. check where object can move
     *      2. move to direction
     *      3. check for collision
     *          a. if there is collision specifically to BLOCK type object, move next to it.
     *             so there won't be overlapping.
     *
     * @param objects - all of the related objects
     * @param delta - the time between frames 
     */
    @Override
    public void update(List<GameObject> objects, long delta) {
        GameObject hitObj = null;
        // Check which directions are available
        movableDirections = canMove(objects);
        move(getDirection(), delta);
       
        hitObj = collides(getDirection(), objects, delta);
        if(hitObj != null){
            if(hitObj.getType() == ObjectType.BLOCK) {
                moveNextTo(hitObj);
            }
            else {
                System.out.println("Something hit after moving, adjusting...");
            }
        }
    }

    /**
     * Method checks if game object can move to any of the four directions and returns array containing
     * their states.
     *
     * @param objects - all the related objects
     * @return a boolean array for movableDirection
     */
    public boolean[] canMove(List<GameObject> objects) {
        boolean[] dirs = {true, true, true, true};

        for(GameObject o : objects) {
            if(o != this) {
                if(getDirection() == Direction.UP) {
                    if(o.contains(this.getX() + this.getWidth()/2, this.getY() - 1)) {    // Is object "o" touching "this"
                        dirs[0] = false;
                    }
                }
                if(getDirection() == Direction.DOWN) {
                    if(o.contains(this.getX() + this.getWidth()/2, this.getY() + 1)) {    // Is object "o" touching "this"  
                        dirs[1] = false;
                    }
                }
                if(getDirection() == Direction.LEFT) {
                    if(o.contains(this.getX() - 1, this.getY() + this.getHeight()/2)) {    // Is object "o" touching "this"
                        dirs[2] = false;
                    }
                }
                if(getDirection() == Direction.RIGHT) {
                    if(o.contains(this.getX() + 1, this.getY() + this.getHeight()/2)) {    // Is object "o" touching "this"
                        dirs[3] = false;
                    }
                }
            }
        }
        return dirs;
    }

    /**
     * Moves the game object.
     * 
     * @param d - direction to move
     * @param time - time between frames
     */
    public void move(Direction d, long time) {
        setDirection(d);
        //This is taken from the statements && getMovableDirection(d)
        if(d == Direction.UP ) {
            this.setY(this.getY() - this.getSpeed());
        }
        else if(d == Direction.DOWN) {
            this.setY(this.getY() + this.getSpeed());
        }
        else if(d == Direction.LEFT) {
            this.setX(this.getX() - this.getSpeed());
        }
        else if(d == Direction.RIGHT) {
            this.setX(this.getX() + this.getSpeed());
        }
    }

    /**
     * Method moves game object to side of other game object accordingly to direction.
     * 
     * @param obj - object you want to move next to
     */
    public void moveNextTo(GameObject obj) {
        Direction dir = this.getDirection();
        if(dir == Direction.UP) {
           this.setY(obj.getY() + obj.getHeight());
        }
        else if(dir == Direction.DOWN) {
            this.setY(obj.getY() - this.getHeight());
        }
        else if(dir == Direction.LEFT) {
            this.setX(obj.getX() + obj.getWidth());
        }
        else if(dir == Direction.RIGHT) {
            this.setX(obj.getX() - this.getWidth());
        }
    }

    /**
     * Method checks whether game object collides with other game object in specific direction.
     * If collision was detected, it returns the collided game object, otherwise null.
     *
     * @param d - direction you want to check
     * @param objects - all related objects
     * @param time - time between frames
     * @return collided game object and null if there was no collision
     */
    public GameObject collides(Direction d, List<GameObject> objects, long time) {
        for(GameObject o : objects) {
            if(o != this) {    
                if(d == Direction.UP) {
                    if(this.collides(o) && o.getY() < this.getY()){
                        return o;
                    }
                }
                else if(d == Direction.DOWN) {
                    if(this.collides(o) && o.getY() > this.getY()){
                        return o;
                    }
                }
                else if(d == Direction.LEFT) {
                    if(this.collides(o) && o.getX() < this.getX()){
                        return o;
                    }
                }
                else if(d == Direction.RIGHT) {
                    if(this.collides(o) && o.getX() > this.getX()){
                        return o;
                    }
                }
            }
        }
        return null;
    }
}