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
public interface IGameServers extends Remote {

    public boolean serverAdd(String name, String ip, int port) throws RemoteException;

    public boolean serverRemove(String ip) throws RemoteException;

    public List<GameServer> getAllServers() throws RemoteException;
}
