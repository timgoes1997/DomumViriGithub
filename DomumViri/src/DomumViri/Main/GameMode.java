/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.Main;

/**
 *
 * @author Tim
 */
public enum GameMode {
    SinglePlayer,
    MPFFA,
    MPTeams,
    MP;

    public static GameMode fromInteger(int x) {
        switch (x) {
            case 0:
                return SinglePlayer;
            case 1:
                return MPFFA;
            case 2:
                return MPTeams;
            case 3:
                return MP;
            default:
                return null;
        }
    }
}
