/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testgame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import static testgame.TestGame.height;
import static testgame.TestGame.width;

/**
 *
 * @author Tim
 */
public class Render {

    private int width;
    private int height;
    private double scaleX;
    private double scaleY;

    private GraphicsContext gc;
    private String debugLine;
    private boolean debug;

    public int drawCallsSecond;
    public int drawCallsFrame;

    public double getScaleX() {
        return scaleX;
    }

    public double getScaleY() {
        return scaleY;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebugLine(String debugLine) {
        this.debugLine = debugLine;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public Render(GraphicsContext gc, int width, int height, int requestedWidth, int requestedHeight) {
        this.width = width;
        this.height = height;
        this.scaleX = ((double) width / (double) requestedWidth);
        this.scaleY = ((double) height / (double) requestedHeight);
        this.gc = gc;
        this.debug = true;
        this.drawCallsSecond = 0;
        this.drawCallsFrame = 0;
    }

    /*
     Gevonden op stackoverflow.
     */
    public void drawImage(GraphicsContext gc, Image image, double angle, int posX, int posY) {
        gc.save(); // saves the current state on stack, including the current transform

        double imageXposition = posX + image.getWidth() / 2;
        double imageYposition = posY + image.getHeight() / 2;

        rotate(gc, angle, imageXposition, imageYposition);
        gc.drawImage(image, posX, posY);
        drawCallsSecond++;
        drawCallsFrame++;
        gc.restore(); // back to original state (before rotation)

        debugCirlce(Color.RED, imageXposition, imageYposition);
        debugCirlce(Color.GREEN, posX, posY);
    }

    /*
     Huidige rendering methode met de meeste en beste mogelijkheden!
     */
    public void drawImage(GraphicsContext gc, Image image, double angle, int posX, int posY, int width, int height) {
        gc.save(); // saves the current state on stack, including the current transform

        double imageXposition = posX + ((image.getWidth() * scaleX) / 2);
        double imageYposition = posY + ((image.getHeight() * scaleY) / 2);

        rotate(gc, angle, imageXposition, imageYposition);
        gc.drawImage(image, posX, posY, width * scaleX, height * scaleY);
        drawCallsSecond++;
        drawCallsFrame++;
        gc.restore(); // back to original state (before rotation)

        debugCirlce(Color.RED, imageXposition, imageYposition);
        debugCirlce(Color.GREEN, posX, posY);
    }

    /*
     Gevonden op stackoverflow.
     */
    public void drawImage(GraphicsContext gc, Image image, double angle, int posX, int posY, double scaleX, double scaleY) {
        gc.save(); // saves the current state on stack, including the current transform

        double imageXposition = posX + (image.getWidth() * scaleX) / 2;
        double imageYposition = posY + (image.getHeight() * scaleY) / 2;

        rotate(gc, angle, imageXposition, imageYposition);
        gc.drawImage(image, posX, posY, image.getWidth() * scaleX, image.getHeight() * scaleY);
        drawCallsSecond++;
        drawCallsFrame++;
        gc.restore(); // back to original state (before rotation)

        debugLine(Color.YELLOW, debugLine, 10, 10);
        debugCirlce(Color.RED, imageXposition, imageYposition);
        debugCirlce(Color.GREEN, posX, posY);
    }

    public void drawImage(GraphicsContext gc, String imagePath, double angle, int posX, int posY, int width, int height) {
        gc.save(); // saves the current state on stack, including the current transform

        Image newImage = new Image(imagePath, (double) width, (double) height, false, false);

        double imageXposition = posX + newImage.getWidth() / 2;
        double imageYposition = posY + newImage.getHeight() / 2;

        rotate(gc, angle, imageXposition, imageYposition);
        gc.scale(scaleX, scaleY);
        gc.drawImage(newImage, posX, posY, width, height);
        drawCallsSecond++;
        drawCallsFrame++;
        gc.restore(); // back to original state (before rotation)

        debugLine(Color.YELLOW, debugLine, 10, 10);
        debugCirlce(Color.RED, imageXposition, imageYposition);
        debugCirlce(Color.GREEN, posX, posY);
    }

    /*
     Gevonden op stackoverflow.
     */
    public void rotate(GraphicsContext gc, double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }

    public void debugCirlce(Color c, double x, double y) {
        if (debug) {
            gc.save();
            gc.setFill(c);
            gc.fillOval(x, y, 10, 10);
            drawCallsSecond++;
            drawCallsFrame++;
            gc.restore();
        }
    }

    public void debugLine(Color c, String s, double x, double y) {
        if (debug) {
            gc.save();
            gc.setFill(c);
            gc.fillText("Debug line:" + s, x, y);
            gc.restore();
        }
    }

    public void Update(Level level, int playerX, int playerY) {
        Clear();
        gc.save();

        //drawImage(gc, level.getTileMap().get(0).image, 0, 100, 100); //Dit werkt
        for (Tile t : level.getTileMap()) {
            int h = (level.getLevelHeight() - t.y - 2);
            int x = (int) ((t.x * t.width * scaleX) - playerX);
            int test = (int)((t.height * scaleY) / 4);//(int) (((scaleY) / t.height) * height);//(int) (t.height * (((scaleY * scaleY) / 4)));
            int y = (int) (((h * t.height * scaleY) - playerY) - test); //+16 drawt goed bij 720p(scale 1) +8 drawt goed bij 1080p(scale 1.5) +4 bij 540p(scale 0.75) + 28 bij 900p (scale 1.25)

            if (x >= (0 - (t.width * scaleX)) && y >= ((0 - t.height) * scaleY) && x <= width && y <= height) {
                drawImage(gc, t.image, 0, x, y, t.width, t.height); //
            }
        }
        gc.restore();
    }

    public void Clear() {
        gc.clearRect(0.0, 0.0, width, height);
        gc.setFill(Color.BLACK);
        drawCallsSecond++;
        drawCallsFrame++;
        gc.fillRect(0.0, 0.0, width, height);
    }
}
