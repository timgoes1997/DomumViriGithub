/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.Entities.Items;

import DomumViri.Entities.Item;
import DomumViri.Tiles.Tile;
import java.util.Random;

/**
 *
 * @author walter
 */
public class Healthpotion extends Item {
    
    private int healthBoost;
    
    public Healthpotion(Tile t, float gravmod, float speedmod, int duration) {
        super(t, gravmod, speedmod, duration);
        Random random = new Random();
        //tussen de 30 en 70 --> random.nextInt((max - min) + 1) + min;
        this.healthBoost = random.nextInt((70 - 30) + 1) + 30;
    }

    @Override
    public void update() {
        // TODO - implement Item.update
        throw new UnsupportedOperationException();
    }
    
    public int getHealthBoost()
    {
        return healthBoost;
    }
    
    @Override
    public String itemNameToString(){
        return "Healthpotion";
    }
}
