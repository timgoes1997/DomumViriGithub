/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.RMI.shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Jeroen Roovers
 */
public interface IAccounts extends Remote {

    public boolean accountCreate(String username, String password) throws RemoteException;

    public boolean accountLogin(String username, String password) throws RemoteException;
}
