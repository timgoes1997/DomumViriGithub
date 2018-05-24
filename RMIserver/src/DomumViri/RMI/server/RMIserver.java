/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.RMI.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author Jeroen Roovers
 */
public class RMIserver {

    // Set port number
    private static final int PORTNUMBER = 1099;
    // Set binding name for student administration
    private static final String ACCOUNTBINDNAME = "AccountAdmin";
    private static final String SERVERBINDNAME = "ServerAdmin";
    private static final String HISCOREBINDNAME = "HighscoreAdmin";
    // References to registry and student administration
    private Registry registry = null;
    private Accounts accountAdmin = null;
    private GameServers serverAdmin = null;
    private Highscores highscoreAdmin = null;

    public RMIserver() {
        // Print port number for registry
        System.out.println("Server: Port number " + PORTNUMBER);

        // Create student administration
        try {
            accountAdmin = new Accounts();
            System.out.println("Server: Account registration created");
        } catch (RemoteException ex) {
            System.out.println("Server: Cannot create Account registration");
            System.out.println("Server: RemoteException: " + ex.getMessage());
            accountAdmin = null;
        }

        // Create server administration
        try {
            serverAdmin = new GameServers();
            System.out.println("Server: Server registration created");
        } catch (RemoteException ex) {
            System.out.println("Server: Cannot create server registration");
            System.out.println("Server: RemoteException: " + ex.getMessage());
            serverAdmin = null;
        }

        // Create Highscore administration
        try {
            highscoreAdmin = new Highscores();
            System.out.println("Server: Highscore registration created");
        } catch (RemoteException ex) {
            System.out.println("Server: Cannot create highscore registration");
            System.out.println("Server: RemoteException: " + ex.getMessage());
            highscoreAdmin = null;
        }

        // Create registry at port number
        try {
            registry = LocateRegistry.createRegistry(PORTNUMBER);
            System.out.println("Server: Registry created on port number " + PORTNUMBER);
        } catch (RemoteException ex) {
            System.out.println("Server: Cannot create registry");
            System.out.println("Server: RemoteException: " + ex.getMessage());
            registry = null;
        }

        // Bind student administration using registry
        try {
            registry.rebind(ACCOUNTBINDNAME, accountAdmin);
            registry.rebind(SERVERBINDNAME, serverAdmin);
            registry.rebind(HISCOREBINDNAME, highscoreAdmin);
        } catch (RemoteException ex) {
            System.out.println("Server: Cannot bind student administration");
            System.out.println("Server: RemoteException: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        // Welcome message
        System.out.println("SERVER USING REGISTRY");
        // Create server
        RMIserver server = new RMIserver();
    }
}
