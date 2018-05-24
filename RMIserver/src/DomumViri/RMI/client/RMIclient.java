package DomumViri.RMI.client;

import DomumViri.RMI.shared.Highscore;
import DomumViri.RMI.shared.IHighscores;
import DomumViri.RMI.shared.IGameServers;
import DomumViri.RMI.shared.GameServer;
import DomumViri.RMI.shared.IAccounts;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

/**
 * @author Jeroen Roovers
 */
public class RMIclient {

// Set host and port 
    public static final String HOSTNAME = "188.166.2.36";
    public static final int PORTNUMBER = 1099;

    // Set binding name for administrations
    private static final String ACCOUNTBINDNAME = "AccountAdmin";
    private static final String SERVERBINDNAME = "ServerAdmin";
    private static final String HISCOREBINDNAME = "HighscoreAdmin";

    // References to registry and administration
    private Registry registry = null;
    private IHighscores highscoreAdmin = null;
    private IAccounts accountsAdmin = null;
    private IGameServers serversAdmin = null;

    // Constructor
    /**
     * Creates a new instance of the RMI Client. If you are unsure what
     * IP-adress or port to use you can use the static fields HOSTNAME and
     * PORTNUMBER from this class.
     *
     * @param ipAddress The hostname or IP adress of the server as string
     * @param portNumber The portnumber of the server as integer
     */
    public RMIclient(String ipAddress, int portNumber) {

        // Locate registry at IP address and port number
        try {
            registry = LocateRegistry.getRegistry(ipAddress, portNumber);
        } catch (RemoteException ex) {
            System.out.println("Client: Cannot locate registry");
            System.out.println("Client: RemoteException: " + ex.getMessage());
            registry = null;
        }

        // Print result locating registry
        if (registry != null) {
            System.out.println("Client: Registry located");
        } else {
            System.out.println("Client: Cannot locate registry");
            System.out.println("Client: Registry is null pointer");
        }

        // Bind  administrations using registry
        if (registry != null) {
            try {
                accountsAdmin = (IAccounts) registry.lookup(ACCOUNTBINDNAME);
                serversAdmin = (IGameServers) registry.lookup(SERVERBINDNAME);
                highscoreAdmin = (IHighscores) registry.lookup(HISCOREBINDNAME);
            } catch (RemoteException ex) {
                System.out.println("Client: Cannot bind an administration");
                System.out.println("Client: RemoteException: " + ex.getMessage());
                highscoreAdmin = null;
                accountsAdmin = null;
                serversAdmin = null;
            } catch (NotBoundException ex) {
                System.out.println("Client: Cannot bind an administration");
                System.out.println("Client: NotBoundException: " + ex.getMessage());
                highscoreAdmin = null;
                accountsAdmin = null;
                serversAdmin = null;
            }
        }

        // Bind accounts administration using registry
        if (registry != null) {
            try {
                accountsAdmin = (IAccounts) registry.lookup(ACCOUNTBINDNAME);
            } catch (RemoteException ex) {
                System.out.println("Client: Cannot bind administration");
                System.out.println("Client: RemoteException: " + ex.getMessage());
                highscoreAdmin = null;
                accountsAdmin = null;
            } catch (NotBoundException ex) {
                System.out.println("Client: Cannot bind administration");
                System.out.println("Client: NotBoundException: " + ex.getMessage());
                highscoreAdmin = null;
                accountsAdmin = null;
            }
        }

        // Print result binding administration
        if (accountsAdmin != null) {
            System.out.println("Client: account administration bound");
        } else {
            System.out.println("Client: account administration is null pointer");
        }

        if (highscoreAdmin != null) {
            System.out.println("Client: hi-score administration bound");
        } else {
            System.out.println("Client: hi-score administration is null pointer");
        }

        if (highscoreAdmin != null) {
            System.out.println("Client: server administration bound");
        } else {
            System.out.println("Client: server administration is null pointer");
        }
    }

    /**
     * Registers a new account on the database. In case this returns false it
     * probaly means the username is already taken (unique constraint). Take
     * appropiate action so the user is aware of this.
     *
     * @param username the username of the new account, size < 50
     * @param password the password of the new account
     * @return true if created
     */
    public boolean register(String username, String password) {
        boolean returnvalue = false;
        try {
            returnvalue = accountsAdmin.accountCreate(username, password);
        } catch (RemoteException ex) {
            System.out.println("Client: Cannot Register");
            System.out.println("Client: RemoteException: " + ex.getMessage());
        }
        return returnvalue;
    }

    /**
     * Checks authentication for an account against the database.
     *
     * @param username username of account to authenticate
     * @param password password of account to authenticate
     * @return true if valid credentials (combination username AND password)
     */
    public boolean login(String username, String password) {
        boolean returnvalue = false;
        try {
            returnvalue = accountsAdmin.accountLogin(username, password);
        } catch (RemoteException ex) {
            System.out.println("Client: Cannot login");
            System.out.println("Client: RemoteException: " + ex.getMessage());
        }
        return returnvalue;
    }

    /**
     * Gets all the highscores on the server.
     *
     * @return List of highscores
     */
    public List<Highscore> getAllHighscores() {
        try {
            return highscoreAdmin.highscoreGetAll();
        } catch (RemoteException ex) {
            System.out.println("Client: Cannot get highscores");
            System.out.println("Client: RemoteException: " + ex.getMessage());
        }
        return null;
    }

    /**
     * Raises the highscore of an account
     *
     * @param username the username of the account
     * @return true if succesfully raised
     */
    public boolean raiseHighscore(String username) {
        boolean returnvalue = false;
        try {
            returnvalue = highscoreAdmin.highscoreRaise(username);
        } catch (RemoteException ex) {
            System.out.println("Client: Cannot raise highscore");
            System.out.println("Client: RemoteException: " + ex.getMessage());
        }
        return returnvalue;
    }

    /**
     * Gets all the game servers from the database
     *
     * @return list of gameservers
     */
    public List<GameServer> getAllGameServers() {
        try {
            return serversAdmin.getAllServers();
        } catch (RemoteException ex) {
            System.out.println("Client: Cannot get gameservers");
            System.out.println("Client: RemoteException: " + ex.getMessage());
        }
        return null;
    }

    /**
     * Registers a server on the database.
     *
     * @param name Name to be shown in the lobby (make sure this is not
     * empty!!!)
     * @param ip IP adress of the server you are about to add
     * @param port port of the server you are about to add
     * @return true if succesfully registered
     */
    public boolean addServer(String name, String ip, int port) {
        boolean returnvalue = false;
        try {
            returnvalue = serversAdmin.serverAdd(name, ip, port);
        } catch (RemoteException ex) {
            System.out.println("Client: Cannot add gameserver");
            System.out.println("Client: RemoteException: " + ex.getMessage());
        }

        return returnvalue;
    }

    /**
     * Unregisters an server from the databsae
     *
     * @param ip ip adress of server to unregister
     * @return true if succesfully unregistered
     */
    public boolean deleteServer(String ip) {
        boolean returnvalue = false;
        try {
            returnvalue = serversAdmin.serverRemove(ip);
        } catch (RemoteException ex) {
            System.out.println("Client: Cannot delete gameserver");
            System.out.println("Client: RemoteException: " + ex.getMessage());
        }

        return returnvalue;
    }

    // Main method DISABLED
    public static void main(String[] args) {

        // Welcome message
        System.out.println("RMI CLIENT STARTED");

        // Create client
        RMIclient client = new RMIclient(HOSTNAME, PORTNUMBER);
//        TESTING
        System.out.println("good login (expected true): " + client.login("henk", "123"));
        System.out.println("good login (expected false): " + client.login("henk", "1233"));
        System.out.println("highscore fetch (expected 5+): " + client.getAllHighscores().size());
        System.out.println("highscore raise (expected true): " + client.raiseHighscore("henk"));
        System.out.println("server added (expected true): " + client.addServer("testserver", "48.50.50.50", 123));
        System.out.println("highscore fetch (expected 3): " + client.getAllGameServers().size());
        System.out.println("server added (expected false): " + client.addServer("testserver", "48.50.50.50", 123));
        System.out.println("server deleted (expected true): " + client.deleteServer("48.50.50.50"));
    }
}
