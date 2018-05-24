package DomumViri.Entities;

import DomumViri.Entities.Entity;
import DomumViri.Tiles.Tile;

public abstract class Item extends Entity {

    private int duration;
    
    public Item(Tile t, float gravmod, float speedmod, int d) {
        super(t, gravmod, speedmod);
        this.duration = d;
    }

    public void update() {
        // TODO - implement Item.update
        throw new UnsupportedOperationException();
    }
    
    public abstract String itemNameToString();
}
