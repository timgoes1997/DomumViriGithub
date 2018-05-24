/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.Point2DSerializable;

import java.io.Serializable;
import javafx.beans.NamedArg;


// PENDING_DOC_REVIEW of this whole class
/**
 * A 2D geometric point that usually represents the x, y coordinates.
 * It can also represent a relative magnitude vector's x, y magnitudes.
 * @since JavaFX 2.0
 */
public class Point2D implements Serializable{

    /**
     * Point or vector with both coordinates set to 0.
     */
    public static final Point2D ZERO = new Point2D(0.0, 0.0);

    /**
     * Cache the hash code to make computing hashes faster.
     */
    private int hash = 0;
    
    /**
     * The x coordinate.
     *
     * @defaultValue 0.0
     */
    private double x;

    /**
     * The y coordinate.
     *
     * @defaultValue 0.0
     */
    private double y;
    


    /**
     * Creates a new instance of {@code Point2D}.
     * @param x the x coordinate of the point
     * @param y the y coordinate of the point
     */
    public Point2D(@NamedArg("x") double x, @NamedArg("y") double y) {
        this.x  = x;
        this.y = y;
    }

    /**
     * The x coordinate.
     * @return the x coordinate
     */
    public final double getX() {
        return x;
    }  

    /**
     * The y coordinate.
     * @return the y coordinate
     */
    public final double getY() {
        return y;
    }
    
    /**
     * Computes the distance between this point and point {@code (x1, y1)}.
     *
     * @param x1 the x coordinate of other point
     * @param y1 the y coordinate of other point
     * @return the distance between this point and point {@code (x1, y1)}.
     */
    public double distance(double x1, double y1) {
        double a = getX() - x1;
        double b = getY() - y1;
        return Math.sqrt(a * a + b * b);
    }

    /**
     * Computes the distance between this point and the specified {@code point}.
     *
     * @param point the other point
     * @return the distance between this point and the specified {@code point}.
     * @throws NullPointerException if the specified {@code point} is null
     */
    public double distance(Point2D point) {
        return distance(point.getX(), point.getY());
    }

    /**
     * Returns a point with the specified coordinates added to the coordinates
     * of this point.
     * @param x the X coordinate addition
     * @param y the Y coordinate addition
     * @return the point with added coordinates
     * @since JavaFX 8.0
     */
    public Point2D add(double x, double y) {
        return new Point2D(
                getX() + x,
                getY() + y);
    }

    /**
     * Returns a point with the coordinates of the specified point added to the
     * coordinates of this point.
     * @param point the point whose coordinates are to be added
     * @return the point with added coordinates
     * @throws NullPointerException if the specified {@code point} is null
     * @since JavaFX 8.0
     */
    public Point2D add(Point2D point) {
        return add(point.getX(), point.getY());
    }

    /**
     * Returns a point with the specified coordinates subtracted from
     * the coordinates of this point.
     * @param x the X coordinate subtraction
     * @param y the Y coordinate subtraction
     * @return the point with subtracted coordinates
     * @since JavaFX 8.0
     */
    public Point2D subtract(double x, double y) {
        return new Point2D(
                getX() - x,
                getY() - y);
    }

    /**
     * Returns a point with the coordinates of this point multiplied
     * by the specified factor
     * @param factor the factor multiplying the coordinates
     * @return the point with multiplied coordinates
     * @since JavaFX 8.0
     */
    public Point2D multiply(double factor) {
        return new Point2D(getX() * factor, getY() * factor);
    }

    /**
     * Returns a point with the coordinates of the specified point subtracted
     * from the coordinates of this point.
     * @param point the point whose coordinates are to be subtracted
     * @return the point with subtracted coordinates
     * @throws NullPointerException if the specified {@code point} is null
     * @since JavaFX 8.0
     */
    public Point2D subtract(Point2D point) {
        return subtract(point.getX(), point.getY());
    }
    
    /**
     * Returns a hash code value for the point.
     * @return a hash code value for the point.
     */
    @Override 
    public int hashCode() {
        if (hash == 0) {
            long bits = 7L;
            bits = 31L * bits + Double.doubleToLongBits(getX());
            bits = 31L * bits + Double.doubleToLongBits(getY());
            hash = (int) (bits ^ (bits >> 32));
        }
        return hash;
    }
    
    /**
     * Checks if the object equals this
     * @param obj
     * @return Wether the given object equals this object.
     */
    @Override 
    public boolean equals(Object obj) {
        return obj == this;
    }

    /**
     * Returns a string representation of this {@code Point2D}.
     * This method is intended to be used only for informational purposes.
     * The content and format of the returned string might vary between
     * implementations.
     * The returned string might be empty but cannot be {@code null}.
     */
    @Override 
    public String toString() {
        return "Point2D [x = " + getX() + ", y = " + getY() + "]";
    }
}
