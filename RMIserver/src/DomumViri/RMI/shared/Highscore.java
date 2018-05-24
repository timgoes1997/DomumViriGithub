/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.RMI.shared;

import java.io.Serializable;

/**
 *
 * @author Jeroen Roovers
 */
public class Highscore implements Serializable {

    private String username;
    private int wins;

    public Highscore() {
    }

    public Highscore(String account, int wins) {
        this.username = account;
        this.wins = wins;
    }

    public String getUsername() {
        return username;
    }

    public int getWins() {
        return wins;
    }

    @Override
    public String toString() {
        return this.username + " - " + wins + " wins";
    }
}
