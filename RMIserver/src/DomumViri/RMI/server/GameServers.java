/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.RMI.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import DomumViri.RMI.shared.GameServer;
import DomumViri.RMI.shared.IGameServers;

/**
 * @author Jeroen Roovers
 */
public class GameServers extends UnicastRemoteObject implements IGameServers {

    public GameServers() throws RemoteException {

    }

    @Override
    public boolean serverAdd(String name, String ip, int port) throws RemoteException {
        boolean ok = false;
        try {
            ok = DatabaseConnection.addServer(name, ip, port);
        } catch (Exception e) {
        }
        return ok;
    }

    @Override
    public boolean serverRemove(String ip) throws RemoteException {
        boolean ok = false;
        try {
            ok = DatabaseConnection.deleteServer(ip);
        } catch (Exception e) {
        }
        return ok;
    }

    @Override
    public List<GameServer> getAllServers() throws RemoteException {
        return DatabaseConnection.getGames();
    }

}
