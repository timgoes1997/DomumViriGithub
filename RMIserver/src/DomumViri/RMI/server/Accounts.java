/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.RMI.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import DomumViri.RMI.shared.IAccounts;

/**
 *
 * @author Jeroen Roovers
 */
public class Accounts extends UnicastRemoteObject implements IAccounts {

    public Accounts() throws RemoteException {
    }

    public boolean accountCreate(String username, String password) throws RemoteException {
        return DatabaseConnection.NewAccount(username, password);
    }

    public boolean accountLogin(String username, String password) throws RemoteException {
        return DatabaseConnection.login(username, password);
    }

}
