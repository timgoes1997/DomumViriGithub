package DomumViri.Entities;

import DomumViri.Point2DSerializable.Point2D;
import DomumViri.Tiles.Tile;
import java.io.Serializable;
import java.util.List;

public abstract class Entity implements Serializable {

    protected boolean falling;
    protected Point2D speed;
    protected float gravityModifier;
    protected float movementSpeedModifier;
    protected Tile tile;
    protected Point2D oldPosition;
    protected boolean doublejumped;
    protected long lastjumped;
    protected static final double GRAVITY = 10;

    public Entity(Tile t, float gravmod, float speedmod) {
        this.tile = t;
        falling = true;
        speed = new Point2D(0, 0);
        oldPosition = new Point2D(t.getLocation().getX(), t.getLocation().getY());
        gravityModifier = gravmod;
        movementSpeedModifier = speedmod;
    }

    // -------------------------------------------------
    // Getters
    // -------------------------------------------------
    public boolean hasDoublejumped() {
        return doublejumped;
    }

    public boolean isFalling() {
        return falling;
    }

    public Point2D getSpeed() {
        return speed;
    }
    
    public void setSpeed(Point2D speed){
        this.speed = speed;
    }

    public Point2D getOldPosition() {
        return oldPosition;
    }

    public float getGravityModifier() {
        return gravityModifier;
    }

    public float getMovementSpeedModifier() {
        return movementSpeedModifier;
    }

    public Tile getTile() {
        return tile;
    }

    /**
     *
     * @param seconds Het aantal seconden sinds de vorige frame Alle entiteiten
     * zijn onder invloed van zwaartekracht.
     */
    public void update(float seconds, List<Entity> entities, Player player) {
        gravity(seconds);
    }

    /**
     *
     * @param x pixels om naar rechts te bewegen
     * @param y pixels om naar boven te bewegen beweegt de tile van het entiteit
     * met de opgegeven aantal pixels.
     */
    public void move(float x, float y) {
        //tile.rect.setX(tile.rect.xProperty().floatValue() + x);
        oldPosition = new Point2D(tile.getLocation().getX(), tile.getLocation().getY());
        if (x < 0) {
            if (speed.getX() > -6) {
                if (falling) {
                    speed = speed.add(x / 6, 0);
                } else {
                    speed = speed.add(x, 0);
                }
            }
        } else if (x > 0) {
            if (speed.getX() < 6) {
                if (falling) {
                    speed = speed.add(x / 6, 0);
                } else {
                    speed = speed.add(x, 0);
                }
            }
        }
//        if (speed.getX() < 10 && speed.getX() > -10) {
//            speed = speed.add(x, 0);
//        }
        if (!falling) {
            speed = speed.add(0, y);
            falling = true;
            lastjumped = System.currentTimeMillis();
            doublejumped = false;

        } else if (!doublejumped && System.currentTimeMillis() - lastjumped > 300 && y > 0) {
            speed = speed.add(0, y);
            falling = true;
            doublejumped = true;
            if (y > 0) {
                tile.getLocation().setY(tile.getLocation().getY() + 1);
            }
        }
    }

    /**
     *
     * @param seconds Het aantal seconden sinds de vorige frame
     * @return altijd true
     */
    private boolean gravity(float seconds) {
        oldPosition = new Point2D(tile.getLocation().getX(), tile.getLocation().getY());
        if (falling) {
            double oldspeed = speed.getY();
            speed = speed.add(0, seconds * -GRAVITY);
            tile.getLocation().setY((float)(tile.getLocation().getY() + ((oldspeed + speed.getY()) / 2)));

        }
        tile.getLocation().setX((float)(tile.getLocation().getX() + speed.getX()));
        return true;
    }

    /**
     * Zet de snelheid op 0 en stop met vallen.
     */
    public void setNotFalling() {
        this.falling = false;
        speed = speed.multiply(0);
    }

    /**
     * Begin met vallen.
     */
    public void setFalling() {
        this.falling = true;
    }
}
