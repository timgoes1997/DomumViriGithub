/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.RMI.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author Jeroen Roovers
 */
public interface IHighscores extends Remote {

    public boolean highscoreRaise(String username) throws RemoteException;

    public List<Highscore> highscoreGetAll() throws RemoteException;

}
