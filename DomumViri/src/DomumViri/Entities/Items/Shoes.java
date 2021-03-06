/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.Entities.Items;

import DomumViri.Debugging.Debug;
import DomumViri.Entities.Item;
import DomumViri.Tiles.Tile;
import java.util.Random;

/**
 *
 * @author walter
 */
public class Shoes extends Item {
    
    private float movementBoost;
    
    public Shoes(Tile t, float gravmod, float speedmod, int duration) {
        super(t, gravmod, speedmod, duration);
        Random random = new Random();
        float rand = (random.nextFloat() / 2) + 1f;
        Debug.log(String.valueOf(rand));
        this.movementBoost = rand;
    }
    
    @Override
    public void update() {
        // TODO - implement Item.update
        throw new UnsupportedOperationException();
    }
    
    public float getMovementBoost()
    {
        return movementBoost;
    }
    
    @Override
    public String itemNameToString(){
        return "Shoes";
    }
}
