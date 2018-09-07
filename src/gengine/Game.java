package gengine;

import java.util.List;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.image.Image;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.*;
import javafx.animation.AnimationTimer;

/**
 * This is the class where graphics are handled.
 * It doesn't serve much wanted reusability at its current state.
 *
 * @author Lauri Pirttimaki
 * @version 0.2
 */
public class Game extends Application {
    String gameTitle;
    GameManager manager;
    Image playerImg;
    Image enemyImg;
    Image blockImg;
    Image ballImg;
    Image stairsImg;
    Scene scene;
    Group root;
    Canvas canvas;

    char[][] map = {
        {'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x'},
        {'x', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', 'x', '-', '-', '-', 'x'},
        {'x', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', 'x', '-', '-', '-', 'x'},
        {'x', '-', '-', 'e', '-', '-', '-', '-', '-', '-', 'o', '-', '-', '-', '-', '-', '-', '-', '-', 'x'},
        {'x', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', 'x'},
        {'x', '-', '-', '-', '-', '-', 'x', 'x', '-', '-', '-', 'x', 'x', '-', '-', '-', '-', '-', '-', 'x'},
        {'x', '-', '-', '-', '-', '-', 'x', '-', '-', '-', '-', '-', 'x', '-', '-', '-', '-', '-', '-', 'x'},
        {'x', '-', '-', '-', '-', '-', 'x', '-', '-', '-', '-', '-', 'x', '-', '-', '-', '-', '-', '-', 'x'},
        {'x', '-', '-', '-', '-', '-', 'x', '-', '-', '-', '-', '-', 'x', '-', '-', '-', '-', 'e', '-', 'x'},
        {'x', '-', '-', '-', '-', '-', 'x', '-', '-', 'P', '-', '-', 'x', 'x', 'x', 'x', '-', '-', '-', 'x'},
        {'x', '-', '-', '-', '-', '-', 'x', '-', '-', '-', '-', '-', 'x', '-', '-', '-', '-', '-', '-', 'x'},
        {'x', '-', '-', '-', '-', '-', 'x', '-', '-', '-', '-', '-', 'x', 's', '-', '-', '-', '-', '-', 'x'},
        {'x', '-', '-', '-', '-', '-', 'x', '-', '-', '-', '-', '-', 'x', '-', '-', 'x', '-', '-', '-', 'x'},
        {'x', '-', '-', '-', '-', '-', 'x', 'x', '-', '-', '-', 'x', 'x', 'x', 'x', 'x', '-', '-', '-', 'x'},
        {'x', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', 'x'},
        {'x', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', 'x'},
        {'x', '-', '-', '-', 'o', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', 'x', 'x'},
        {'x', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', 'x'},
        {'x', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', 'x'},
        {'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x'}
        };

    char[][] map2 = {
        {'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x'},
        {'x', '-', '-', 'x', '-', '-', '-', '-', '-', '-', '-', 'x', '-', '-', '-', '-', 'x', '-', 'P', 'x'},
        {'x', '-', '-', 'x', '-', '-', '-', '-', '-', 'e', '-', 'x', '-', '-', '-', '-', 'x', '-', '-', 'x'},
        {'x', '-', '-', 'x', '-', '-', '-', '-', '-', '-', '-', 'x', '-', '-', '-', '-', 'x', 'x', '-', 'x'},
        {'x', '-', '-', 'x', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', '-', 'x'},
        {'x', '-', 'x', 'x', '-', '-', 'x', 'x', '-', '-', '-', 'x', 'x', '-', '-', '-', '-', '-', '-', 'x'},
        {'x', '-', '-', '-', '-', '-', 'x', '-', '-', '-', '-', '-', 'x', '-', 'x', '-', '-', '-', '-', 'x'},
        {'x', 'x', 'x', 'x', '-', 'x', 'x', '-', '-', '-', '-', '-', 'x', '-', 'x', 'x', 'x', 'x', '-', 'x'},
        {'x', '-', '-', '-', '-', '-', 'x', '-', '-', '-', '-', '-', 'x', '-', '-', '-', 'x', '-', '-', 'x'},
        {'x', '-', 'x', '-', 'x', '-', 'x', 'x', 'x', 'x', '-', '-', 'x', '-', '-', '-', '-', '-', '-', 'x'},
        {'x', '-', 'x', '-', 'x', '-', 'x', '-', '-', 'x', '-', '-', 'x', '-', '-', '-', '-', '-', '-', 'x'},
        {'x', '-', 'x', '-', 'x', '-', 'x', 's', '-', 'x', '-', '-', 'x', 'x', 'x', 'x', '-', 'x', 'x', 'x'},
        {'x', '-', 'x', '-', 'x', 'x', 'x', '-', '-', 'x', '-', '-', 'x', '-', '-', '-', '-', '-', '-', 'x'},
        {'x', '-', 'x', '-', '-', '-', 'x', 'x', '-', 'x', '-', 'x', 'x', '-', '-', '-', '-', '-', '-', 'x'},
        {'x', '-', 'x', '-', '-', '-', 'x', '-', '-', 'x', '-', '-', '-', '-', '-', 'x', 'x', 'x', '-', 'x'},
        {'x', '-', 'x', '-', 'x', 'x', 'x', '-', '-', 'x', '-', '-', '-', '-', '-', 'x', '-', '-', '-', 'x'},
        {'x', '-', '-', '-', 'x', '-', '-', '-', '-', 'x', 'x', 'x', 'x', 'x', '-', 'x', '-', '-', '-', 'x'},
        {'x', '-', 'x', 'x', 'x', '-', '-', 'e', '-', 'x', '-', 'x', '-', '-', '-', 'x', '-', 'x', '-', 'x'},
        {'x', '-', '-', '-', '-', '-', 'x', '-', '-', '-', '-', '-', '-', 'x', '-', '-', '-', '-', '-', 'x'},
        {'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'x'}
        };
    
    List<Map> maps;
    List<Image> images;

    // Dimensions for 20x20 grid + status bar at top
    final int WINDOW_WIDTH = 640;
    final int WINDOW_HEIGHT = 672;
    /**
     * The default constructor. Sets up window title and game maanger. 
     */
    public Game() {
        gameTitle = "Gengine 0.3";
        manager = new GameManager();
        maps = new ArrayList<>();
        maps.add(new Map(map));
        maps.add(new Map(map2));
    }

    /**
     * Method which is called before start method. 
     * Used to preload files before they are used. 
     */
    public void init() {
        playerImg = new Image("img/hero.png");
        enemyImg = new Image("img/monster.png");
        blockImg = new Image("img/stoneBlock.png");
        ballImg = new Image("img/ball.png");
        stairsImg = new Image("img/stairs.png");
    }

    /**
     * The method where all is set up for the game.
     * Also starts and handles the "repaint loop".
     * 
     * @param stage - the top level container for JavaFX objects.
     */
    public void start(Stage stage) {
        GraphicsContext gc;
        root = new Group();
        scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setTitle(gameTitle);
        stage.setScene(scene);
        
        images = new ArrayList<>();
        images.add(playerImg);
        images.add(blockImg);
        images.add(enemyImg);
        images.add(ballImg);
        images.add(stairsImg);

        canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        manager.setImages(images);
        manager.setMaps(maps);
        manager.setCurrentMap(0);
        manager.getMap();
        manager.getPlayer().setSpeed(5.0);
        manager.setControls(scene);

        gc = canvas.getGraphicsContext2D();
        
        // Try to paint the player center of the screen
        // manager.getPlayer().draw(gc);
        root.getChildren().add(canvas);

        stage.show();
        manager.startGame();

        // Repaint loop
        new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                // Clear the canvas
                gc.clearRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
                // Draw the player on the canvas
                manager.draw(gc);
            }
        }.start();
    }

    /**
     * This method handles proper closing by stopping game loop.
     */
    public void stop(){
        if(!manager.gameOver()){
            manager.setGameOver(true);
        }
        Platform.exit();
    }
}