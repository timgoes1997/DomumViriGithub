/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.Level;

import java.io.Serializable;

/**
 *
 * @author Tim
 */
public class MapData implements Serializable {
    private MapName name;
    private MapInfo info;
    
    public MapData(MapName name, MapInfo info){
        this.name = name;
        this.info = info;
    }

    public MapName getName() {
        return name;
    }

    public MapInfo getInfo() {
        return info;
    }
}
