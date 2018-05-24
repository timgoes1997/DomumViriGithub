/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testgame;

import java.io.File;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

/**
 *
 * @author tim
 */
public class TestGame extends Application {

    private AnimationTimer game;
    public static final int width = 1280;
    public static final int height = 720;
    public static final int secondInNanos = 1000000000;

    public int fps;
    private long lastFrame;
    private long lastSecond;
    private long ms;
    private GraphicsContext gc;
    private Stage currentStage;
    private InputManager im;

    private Image image;
    private File file;
    private Level currentLevel;
    private Render render;

    private int currentX;
    private int currentY;
    private int spriteHeight;

    private boolean debugPressed;
    private boolean falling;
    private float gravity;
    private float speedY;

    @Override
    public void start(Stage primaryStage) {
        currentStage = primaryStage;
        Canvas canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();

        Group root = new Group();
        root.getChildren().add(canvas);

        Scene gameScene = new Scene(root);
        gameScene.setFill(Color.BLACK);
        im = new InputManager(gameScene);

        currentStage.setTitle("Hello World!");
        currentStage.setResizable(true);
        currentStage.setScene(gameScene);
        currentStage.show();

        debugPressed = false;
        gravity = 9.81f;
        lastSecond = System.nanoTime();

        String url = System.getProperty("user.dir") + "\\src\\Images\\player.png";
        file = new File(url); //Deze handel werkt F:\\Shit IMG\\Kappa.png

        //String urltest = ClassLoader.getSystemResource("Kappa.png").getPath();
        image = new Image(file.toURI().toString());

        currentLevel = new Level("\\src\\xml\\level0.xml", 12, 20);
        render = new Render(gc, width, height, 1280, 720);

        spriteHeight = (int) ((int) image.getHeight() * render.getScaleY());

        game = new AnimationTimer() {

            @Override
            public void handle(long now) {
                ms = (now - lastFrame);
                fps = (int) (secondInNanos / ms);
                currentStage.setTitle("FPS:" + fps + " | TestGame |");
                Update();
                lastFrame = now;

                render.drawCallsFrame = 0;
                if ((lastSecond + secondInNanos) <= now) {
                    lastSecond = now;
                    render.drawCallsSecond = 0;
                }
            }

            @Override
            public void start() {
                lastFrame = System.nanoTime();
                super.start();
            }

        };
        game.start();
    }

    public void CheckInput() {
        if (im.isMoveUp()) {
            Move(0, -5);
        }
        if (im.isMoveDown()) {
            Move(0, 5);
        }
        if (im.isMoveLeft()) {
            Move(-5, 0);
        }
        if (im.isMoveRight()) {
            Move(5, 0);
        }
        if (im.isDebug() && !debugPressed) {
            render.setDebug(!render.isDebug());
            debugPressed = true;
        }
        if (!im.isDebug() && debugPressed) {
            debugPressed = false;
        }
    }

    public void Move(int x, int y) {
        currentX += x;
        if (!falling) {
            speedY += y;
            falling = true;
            if (y < 0) {
                currentY -= 1;
            }
        }
    }

    public void Gravity() {
        if (falling) {
            if (currentY < (height - spriteHeight)) {
                float oldspeed = speedY;
                speedY += (ms / (float) secondInNanos) * gravity;
                currentY += (oldspeed + speedY) / 2;
            } else {
                currentY = height - spriteHeight;
                speedY = 0;
                falling = false;
            }
        }
    }

    public boolean collision() {
        return currentLevel.hasCollision(currentX, currentY);
    }

    public void Update() {
        Gravity();
        CheckInput();

        int midX = (width / 2) - (int) (image.getWidth() * render.getScaleX() / 2);
        int midY = (height / 2) - (int) (image.getHeight() * render.getScaleY() / 2);

        render.Update(currentLevel, (currentX - midX), (currentY - (height / 2)));
        render.drawImage(gc, image, 0, midX, midY, (int) image.getWidth(), (int) image.getHeight());
        String debugLine = "currentX:" + currentX + " | currentY:" + currentY + " | ScaleX:" + render.getScaleX() + " | ScaleY: " + render.getScaleY() + " | tileSoorten: "
                + currentLevel.getTileSoorten().size() + " | tiles: " + currentLevel.getTileMap().size() + " | drawCallsSecond: " + render.drawCallsSecond + " | drawCallsFrame: " + render.drawCallsFrame;
        render.debugLine(Color.ORANGE, debugLine, 10, 10);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
