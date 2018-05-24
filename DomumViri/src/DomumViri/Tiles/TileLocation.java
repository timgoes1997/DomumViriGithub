/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.Tiles;

import java.io.Serializable;

/**
 *
 * @author tim
 */
public class TileLocation implements Serializable{
    
    private float x;
    private float y;
    private float width;
    private float height;
    
    public TileLocation(float x, float y, float width, float height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }
    
    public float getY() {
        return y;
    }
    
    public void setLoc(float x, float y){
        this.x = x;
        this.y = y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
    
    public float getTop(){
        return y + height;
    }
    
    public float getBottom(){
        return y;
    }
    
    public float getLeft(){
        return x;
    }
    
    public float getRight(){
        return x + width;
    }
    
    @Override
    public String toString(){
        return "X: " + x + " Y: " + y;
    }
}
