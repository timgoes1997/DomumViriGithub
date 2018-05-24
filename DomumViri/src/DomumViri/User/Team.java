/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.User;

/**
 *
 * @author Tim
 */
public enum Team {
    Red,
    Blue;
    
    public Team formInteger(int x){
        switch(x){
            case 0:
                return Red;
            case 1:
                return Blue;
            default:
                return null;
        }
    }
}
