/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.Time;

/**
 *
 * @author Tim
 */
public enum TimeStatus {
    Waiting,
    Warmup,
    Ingame,
    Finished;

    public static TimeStatus fromInteger(int x) {
        switch (x) {
            case 0:
                return Waiting;
            case 1:
                return Warmup;
            case 2:
                return Ingame;
            case 3:
                return Finished;
            default:
                return null;
        }
    }
}
