package gengine;

import javafx.scene.Scene;
import javafx.event.*;
import javafx.scene.input.KeyEvent;
import gengine.gameobject.Player;
import java.util.Set;
import java.util.HashSet;
/**
 * This is the class for player controls.
 * By default it sets two EventHandler objects for
 * OnKeyPressed and OnKeyReleased, both containing basic four direction movement
 * and P for pausing the game and SPACE for special action. The latter two do nothing
 * at this point. 
 *
 * @author Lauri Pirttimaki
 * @version 0.2
 */
public class Controls {
    private Player player;
    private Set<String> input; //List that holds information on keys that are being pressed. 
    /**
     * The default constructor. Takes two parameters which are used.
     *
     * @param scene - the scene where event handlers are set
     * @param manager - the game manager in use.
     */
    public Controls(Scene scene, GameManager manager) {
        input = new HashSet<>();
        player = manager.getPlayer();
        scene.setOnKeyPressed(
            new EventHandler<KeyEvent>() {
                public void handle(KeyEvent e) {
                    String code = e.getCode().toString();
                    addKey(code);
                    switch(code) {
                        case "UP":
                            player.setDirection(Direction.UP);
                            player.setMoving(true);
                            break;
                        case "RIGHT":
                            player.setDirection(Direction.RIGHT);
                            player.setMoving(true);
                            break;
                        case "DOWN":
                            player.setDirection(Direction.DOWN);
                            player.setMoving(true);
                            break;
                        case "LEFT":
                            player.setDirection(Direction.LEFT);
                            player.setMoving(true);
                            break;
                        case "P": // Key for Pause, implemented if time allows
                            // manager.PauseGame() or something.
                            break;
                        case "SPACE": // Key for Player Action, implemented if time allows
                            // manager.player().doSomething() maybe.
                            break;
                        default:
                            break;    
                    }
                }
            }
        );

        scene.setOnKeyReleased(
            new EventHandler<KeyEvent>() {
                public void handle(KeyEvent e) {
                    String code = e.getCode().toString();
                    releaseKey(code);
                    if(code.equals("UP") || code.equals("RIGHT") 
                            || code.equals("DOWN") || code.equals("LEFT")) {
                                if(!isMoving()){
                                    System.out.println("Stopped moving");
                                    player.setDirection(Direction.NONE);
                                    player.setMoving(false);
                                }
                    }
                    else if(code.equals("P")) {
                    }
                    else if(code.equals("R")) {
                        // Game Reset has been disabled until proper map handling is implemented.
                        // manager.resetGame();
                    }
                    else if(code.equals("SPACE")) {
                    }
                    else {

                    }
                }
            }
        );
    }
    /**
     * This method adds a key as String to a list.
     *
     * @param code - the key code pressed
     * @return true if key code was added to the list
     */
    public boolean addKey(String code) {
        System.out.println(code + " pressed.");
        return input.add(code);
    }

    /**
     * This method removes a key from the list of inputs.
     *
     * @param code - the key code released
     * @return true if key was succesfully removed from the list
     */
    public boolean releaseKey(String code) {
        System.out.println(code + " released.");
        return input.remove(code);
    }

    /**
     * This method checks if a certain key code is pressed
     *
     * @param code - the key code wanted 
     * @return true if key code was found from the input list
     */
    public boolean isKeyPressed(String code) {
        /*
        // This is debug code
        // ------------------
        if(input.contains(code)) {
            System.out.println(code + " is still pressed.");
        }
        else {
            System.out.println(code + " is not pressed.");
        }*/
        
        return input.contains(code);
    }

    /**
     * This method checks whether keys for movement are still being pressed.
     *
     * @return true if player is still on the move, false if not.
     */
    public boolean isMoving() {
        Direction d = player.getDirection();
        // Check that you have correct key and direction combo.
        if(isKeyPressed("UP") && d == Direction.UP 
            || isKeyPressed("DOWN") && d == Direction.DOWN 
            || isKeyPressed("RIGHT") && d == Direction.RIGHT
            || isKeyPressed("LEFT") && d == Direction.LEFT) {
                return true;
        }
        else {
            return false;
        }
    }
}