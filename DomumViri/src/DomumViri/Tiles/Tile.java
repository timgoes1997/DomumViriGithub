package DomumViri.Tiles;

import DomumViri.Debugging.Debug;
import java.io.Serializable;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class Tile implements Serializable {

    private int id;
    private String naam;
    private int imageID;
    private boolean collidable;
    private boolean death;
    private int x;
    private int y;
    private int width;
    private int height;
    private TileLocation location;

    public Tile(int id, String naam, int imageID, int width, int height, boolean collidable, boolean death) {
        this.id = id;
        this.naam = naam;
        this.imageID = imageID;
        this.collidable = collidable;
        this.width = width;
        this.height = height;
        this.death = death;
    }

    public Tile(int id, String naam, int imageID, int x, int y, int width, int height, boolean collidable, boolean death) {
        this.id = id;
        this.naam = naam;
        this.imageID = imageID;
        this.collidable = collidable;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.death = death;
        location = new TileLocation((x * width) - width, (y * height) - height, width, height);
    }

    /**
     * Gets the tile id
     *
     * @return the id of the tile
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the tile name
     *
     * @return the tile name
     */
    public String getNaam() {
        return naam;
    }

    /**
     * Gets the image of the tile
     *
     * @return the tiles image
     */
    public int getImageID() {
        return imageID;
    }

    /**
     * Checks if the tile is collidable
     *
     * @return if this tile is collidable
     */
    public boolean isCollidable() {
        return collidable;
    }

    /**
     * Checks if this tile is a death tile
     *
     * @return if this tile is a death tile
     */
    public boolean isDeath() {
        return death;
    }

    /**
     * Gets the x of the tile
     *
     * @return the tiles x
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y of the tile
     *
     * @return the tiles y
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the width of the tile
     *
     * @return the tiles width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of the tile
     *
     * @return the tiles height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gets the tiles location data
     *
     * @return the tiles location data
     */
    public TileLocation getLocation() {
        return location;
    }

    public boolean intersects(Tile t) {
        
        
        if (t.location.getX() >= location.getLeft() && t.location.getX() <= location.getRight()) {
            if (t.location.getY() + location.getHeight() >= location.getBottom() && 
                t.location.getY() + location.getHeight() <= location.getTop()) {
                //Debug.log("Player:" + location.toString() + " CollidedTile: " + t.location.toString());
                return true;
            }
        }
        return false;
        //return !(location.getBottom() < t.getLocation().getTop() || location.getTop() > t.getLocation().getBottom() || location.getLeft() > t.getLocation().getRight() || location.getRight() > t.getLocation().getLeft());
    }
}
