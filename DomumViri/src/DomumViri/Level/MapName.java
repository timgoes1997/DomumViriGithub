/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.Level;

/**
 *
 * @author Tim
 */
public enum MapName {
    Default {
        @Override
        public String toString(){
            return "\\src\\level_layout\\level0.txt";
        }
    };

    public static MapName fromInteger(int x) {
        switch (x) {
            case 0:
                return Default;
            default:
                return null;
        }
    }
};
