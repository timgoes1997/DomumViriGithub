/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testgame;

import javafx.scene.image.Image;

/**
 *
 * @author Tim
 */
public class Tile {
    public int id;
    public String naam;
    public Image image;
    public boolean collidable;
    public int x;
    public int y;
    public int width;
    public int height;
    
    public Tile(int id, String naam, Image image, boolean collidable){
        this.id = id;
        this.naam = naam;
        this.image = image;
        this.collidable = collidable;
    }
    
    public Tile(int id, String naam, Image image, int x, int y, int width, int height, boolean collidable){
        this.id = id;
        this.naam = naam;
        this.image = image;
        this.collidable = collidable;        
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
}
