package gengine.gameobject;

/**
 * This is the base class for collectable objects.
 * It has value which represents the score points at this point,
 * and enumerator Type for its type. Current types are: SCORE,
 * POWERUP, ITEM and NONE.
 *
 * @author Lauri Pirttimaki
 * @version 0.2
 */
public class Collectable extends GameObject{
    public static enum Type {
        SCORE,
        POWERUP,
        ITEM,
        NONE
    }
    public Type collectType;

    private boolean pickedUp = false;
    private int value = 0;

    /**
     * Default constructor. Sets objectType to COLLECTABLE
     * and collectType to NONE.
     */
    public Collectable() {
        super();
        this.setType(ObjectType.COLLECTABLE);
        collectType = Type.NONE;
    }

    /**
     * Default constructor. Sets objectType to COLLECTABLE
     * and collectType to NONE. Takes coordinates and dimensions as
     * parameters.
     *
     * @param x - x coordinate
     * @param y - y coordinate
     * @param w - width of the object
     * @param h - height of the object
     */
    public Collectable(int x, int y, int w, int h) {
        super(x, y, w, h);
        this.setType(ObjectType.COLLECTABLE);
        collectType = Type.NONE;
    }

    /**
     * Method sets the value attirubute.
     *
     * @param v - wanted value
     */
    public void setValue(int v) {
        value = v;
    }

    /**
     * Method changes state of pickedUP attribute to true and
     * notifies observers of object.
     */
    public void pickUp() {
        System.out.println(this + " was picked up!");
        pickedUp = true;
        setChanged();
        notifyObservers("was picked up");
    }
    /**
     * Returns value of object.
     *
     * @return value attribute
     */
    public int getValue() { return value; }

    /**
     * Returns whether object is picked up or not.
     *
     * @return true if object is picked up.
     */
    public boolean isPickedUp() { return pickedUp; }
}