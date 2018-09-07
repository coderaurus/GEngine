package gengine.gameobject;

import java.util.Observable;
import java.util.Optional;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Rectangle2D;
import gengine.graphics.Sprite;


/**
 * This is the base class for game objects.
 * It contains basic attributes and methods for coordinates, size and collision.
 * Also has attributes and methods for Sprite of a game object.
 * 
 * @author Lauri Pirttimaki
 * @version 0.2
 */
public class GameObject extends Observable{
    private double x;
    private double y;
    private double width;
    private double height;
    private boolean collidable;
    private Optional<Sprite> sprite;    
    private ObjectType type;
    
    /**
     * The default constructor. The type of object is set to UNDEFINED and
     * sprite needs to be set separately. The sprite is also stored in an Optional.
     * The default size for game object is 32x32 pixels and it has collidable set to true.
     */
    public GameObject() {
        setX(0);
        setY(0);
        setWidth(32);
        setHeight(32);
        setCollision(true);
        sprite = Optional.empty();
        setType(ObjectType.UNDEFINED);
    }
    /**
     * The constructor taking coordinates and size as parameters. 
     * The type of object is set to UNDEFINED and sprite needs to be set separately.
     * The sprite is also stored in an Optional.
     * 
     * @param x - the x coordinate
     * @param y - the y coordinate
     * @param w - the width of the game object
     * @param h - the height of the game object
     */
    public GameObject(double x, double y, double w, double h) {
        setX(x);
        setY(y);
        setWidth(w);
        setHeight(h);
        setCollision(true);
        sprite = Optional.empty();
        setType(ObjectType.UNDEFINED);
    }

    /**
     * Sets the x coordinate, as long as it is on positive values.
     * This method also updates the coordinate for the sprite.
     *
     * @param n - the new x coordinate
     */
    public void setX(double n) {
        if( n >= 0.0) {
            x = n;
        }
        else {
            x = 0.0;
        }
    }

    /**
     * Sets the y coordinate, as long as it is on positive values.
     * This method also updates the coordinate for the sprite.
     *
     * @param n - the new y coordinate
     */
    public void setY(double n) {
        if( n >= 0.0) {
            y = n;
        }
        else {
            y = 0.0;
        }
    } 

    /**
     * Sets the width as long as it is more than the default size of 32.
     *
     * @param n - the new width
     */
    public void setWidth(double n) {
        if( n > 32) {
            width = n;
        }
        else {
            width = 32;
        }
    }

    /**
     * Sets the heightas long as it is more than the default size of 32.
     * 
     * @param n - the new height
     */
    public void setHeight(double n) {
        if( n > 32) {
            height = n;
        }
        else {
            height = 32;
        }
    }

    /**
     * Sets whether game object has collision or not.
     * 
     * @param doesHaveCollision - new state of collision.
     */
    public void setCollision(boolean doesHaveCollision) {
        collidable = doesHaveCollision;
    }

    /**
     * Sets the type of the game object
     * 
     * @param t - the new type of game object
     */
    public void setType(ObjectType t) {
        type = t;
    }

    /**
     * Sets the sprite for game object. Takes the coordinates and dimensions
     * from the game object.
     * 
     * @param img - the Image for the sprite
     */
    public void setSprite(Image img) {
        sprite = Optional.ofNullable(new Sprite(img, getX(), getY(), getWidth(), getHeight()));
    }

    /**
     * Returns x coordinate
     * 
     * @return x coordinate
     */
    public double getX() { return x; }
    /**
     * Returns y coordinate
     * 
     * @return y coordinate
     */
    public double getY() { return y; }
    /**
     * Returns width of the game object
     * 
     * @return width of the game object
     */
    public double getWidth() { return width; }
    /**
     * Returns height of the game object
     * 
     * @return height of the game object
     */
    public double getHeight() { return height; }
    /**
     * Returns boolean for whether game object has collision
     * 
     * @return collidable boolean
     */
    public boolean isCollidable() { return collidable; }
    /**
     * Returns game objects type
     * 
     * @return x coordinate
     */
    public ObjectType getType() { return type; }
    /**
     * Returns the sprite of the game object
     * 
     * @return the Sprite object of game object
     */
    public Sprite getSprite() { return sprite.get(); }
    
    /**
     * Base method for handling game logic. Empty by default.
     *
     * @param objects - objects in the game
     * @param delta - time between frames
     */
    public void update(List<GameObject> objects, long delta){}

    /**
     * Method returns a boolean whether game object collides with other game objects.
     *
     * @param o - specific object which we want to check collision with
     * @return true if collision was detected, false otherwise.
     */
    public boolean collides(GameObject o) {
        /* Debug code
        if(o.getBounds().intersects(this.getBounds())) {
            System.out.println("Is " + this.getType() + " is colliding with " + o.getType());
        }*/

        return o.getBounds().intersects(this.getBounds());
    }

    /**
     * Method returns a boolean whether game object collides with other game objects.
     *
     * @param objects - a List containing all the other objects
     * @param time - time between frames
     * @return true if collision was detected, false otherwise.
     */
    public boolean collides(List<GameObject> objects, long time) {
        boolean doesCollide = false;

        for(GameObject o : objects) {    
            if(o.isCollidable() && this != o){
                if(collides(o)) {
                    doesCollide = true;
                }
            }
        }
        return doesCollide;
    }
    /**
     * Sets the coordinates of the sprite of the game object to coordinates
     * of the game object, ensuring both classes are in the same position.
     */
    public void updateSpriteCoords() {
        this.getSprite().setX(this.getX());
        this.getSprite().setY(this.getY());  
    }

    /**
     * Method calls for the draw method in Sprite.
     *
     * @param gc - the GraphicsContext which handles drawing in Canvas
     */
    public void draw(GraphicsContext gc) {
        sprite.get().draw(gc);
    }

    /**
     * Method returns the area of game object as a Rectangle2D object.
     *
     * @return Rectangle2D object representing "hit box" of game object
     */
    public Rectangle2D getBounds() {
        return new Rectangle2D(getX(), getY(), getWidth(), getHeight());
    }

    /**
     * Method returns whether given coordinates are in bounds of game object.
     *
     * @param x - x coordinate
     * @param y - y coordinate
     * @return true if coordinates are within bounds
     */
    public boolean contains(double x, double y) {
        return getBounds().contains(x, y);
    }
}