/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.RMI.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import DomumViri.RMI.shared.Highscore;
import DomumViri.RMI.shared.IHighscores;

/**
 *
 * @author Jeroen Roovers
 */
public class Highscores extends UnicastRemoteObject implements IHighscores {

    public Highscores() throws RemoteException {
    }

    @Override
    public boolean highscoreRaise(String username) throws RemoteException {
        boolean ok = false;
        try {
            ok = DatabaseConnection.raiseHighscore(username);
        } catch (Exception e) {
        }
        return ok;
    }

    @Override
    public List<Highscore> highscoreGetAll() throws RemoteException {
        return DatabaseConnection.getHighscores();
    }

}
