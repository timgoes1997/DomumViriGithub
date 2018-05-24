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

public enum MapInfo {
    Default {
        @Override
        public String toString(){
            return "\\src\\xml\\level0.xml";
        }
    };

    public static MapInfo fromInteger(int x) {
        switch (x) {
            case 0:
                return Default;
            default:
                return null;
        }
    }
};
