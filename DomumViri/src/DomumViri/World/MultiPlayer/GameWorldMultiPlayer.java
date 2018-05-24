/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.World.MultiPlayer;

import DomumViri.Debugging.Debug;
import DomumViri.Entities.Entity;
import DomumViri.Entities.NetworkedPlayer;
import DomumViri.Entities.Player;
import DomumViri.Level.Map;
import DomumViri.Level.MapData;
import DomumViri.Level.MapInfo;
import DomumViri.Level.MapName;
import DomumViri.Main.GameMode;
import DomumViri.Managers.InputManager;
import DomumViri.SocketServer.Commands;
import DomumViri.Time.Time;
import DomumViri.User.Account;
import DomumViri.World.GameWorld;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tim
 */
public class GameWorldMultiPlayer extends GameWorld {

    private Socket socketConnection;
    private ObjectOutputStream clientOutputStream;
    private ObjectInputStream clientInputStream;
    private boolean updating;
    private NetworkedPlayer nPlayer;

    /**
     *
     * @param im
     * @param ip
     * @param port
     * @throws java.lang.Exception
     */
    public GameWorldMultiPlayer(InputManager im, Account acc, InetAddress ip, int port) throws Exception {
        super();
        getSocket(ip, port);
        getGameMode();
        getServerMap();
        initializePlayer(im, acc);
        nPlayer = new NetworkedPlayer(player.getTile(), player.getGravityModifier(), player.getMovementSpeedModifier(), player.getDamageModifier(), player.getHealthModifier(), player.getArmorModifier(), player.getAccount(), player.isFacingLeft());
        updating = true;
        entities.add(nPlayer);
        getGameTime();
        connectPlayer();
    }

    private void getSocket(InetAddress ip, int port) throws Exception {
        socketConnection = new Socket(ip.getHostAddress(), port);
        if (socketConnection.isConnected()) {
            Debug.log("Connecting to: " + ip.getHostAddress());
            clientOutputStream = new ObjectOutputStream(socketConnection.getOutputStream());
            clientInputStream = new ObjectInputStream(socketConnection.getInputStream());
        }
    }
    
    public void getGameTime() throws Exception{
        clientOutputStream.reset();
        clientOutputStream.writeObject(Commands.GET_TIME);
        clientOutputStream.flush();
        time = (Time) clientInputStream.readObject();
        Debug.log("Time: " + time.getStatus());
    }

    private void getGameMode() throws Exception {
        clientOutputStream.reset();
        clientOutputStream.writeObject(Commands.GET_GAME_MODE);
        clientOutputStream.flush();
        mode = (GameMode) clientInputStream.readObject();
        Debug.log("GameMode: " + mode.toString());
    }

    private void getServerMap() throws Exception {
        clientOutputStream.reset();
        clientOutputStream.writeObject(Commands.GET_MAP);
        clientOutputStream.flush();
        MapData data = (MapData) clientInputStream.readObject();
        if (data == null) {
            throw new Exception("Map not found!");
        } else {
            map = new Map(data.getName(), data.getInfo());
        }

        Debug.log("Map: " + data.getName().toString());
    }

    private void connectPlayer() throws Exception {
        clientOutputStream.reset();
        clientOutputStream.writeObject(nPlayer);
        clientOutputStream.flush();
        NetworkedPlayer nPlayer = (NetworkedPlayer) clientInputStream.readObject();
        player.getAccount().setTeam(nPlayer.getAccount().getTeam());
        if (nPlayer == null) {
            throw new Exception("Server full");
        }
        Debug.log("Connected as " + nPlayer.getAccount().getUserName());
    }

    /**
     * Wordt iedere frame geupdate en zorgt er voor dat alle entities in de
     * gameworld zich verplaatsen etc.
     *
     * @param seconds
     */
    @Override
    public void update(float seconds) {
        super.update(seconds);
        checkCollision(player, seconds);
        updateServer();
        long now = System.nanoTime();
        if(time.getRemainingTime(now) < 0){
            try {
                getGameTime();
            } catch (Exception ex) {
                this.shutdown();
                Logger.getLogger(GameWorldMultiPlayer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Updates the socket server
     */
    public void updateServer() {
        try {
            updateEntities();

            if (!updating) {
                clientOutputStream.close();
                clientInputStream.close();
            }
        } catch (Exception ex) {
            Logger.getLogger(GameWorldMultiPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateEntities() {
        try {
            for (Entity e : entities) {
                if (e instanceof NetworkedPlayer) {
                    NetworkedPlayer n = (NetworkedPlayer) e;
                    System.out.println(n.getHealth());
                    if (n.getAccount().getUserName().equals(player.getAccount().getUserName())) {
                        //System.out.println(n.getTile().getLocation().toString());
                        n.setNetworkedPlayer(player.getTile(), player.getGravityModifier(), player.getMovementSpeedModifier(), player.getDamageModifier(), player.getHealthModifier(), player.getArmorModifier(), player.isFacingLeft(), player.getHealth(), player.getSpeed(), player.getLastDamageDone(), player.isStoleHealth(), player.isBlocking(), false);
                        e = n;
                        //System.out.println(e.getTile().getLocation().toString() + " UPDATED");
                    }
                }
                if (e instanceof Player) {
                    Logger.getLogger(GameWorldMultiPlayer.class.getName()).log(Level.SEVERE, null, "Player may not be in entities");
                }
            }
            clientOutputStream.reset();
            clientOutputStream.writeUnshared(this.entities);

            Object o = clientInputStream.readObject();
            if (o instanceof List) {
                System.out.println("Found list");
                List<Entity> serverEntities = (List<Entity>) o;
                if (serverEntities != null) {
                    entities = serverEntities;
                    for (Entity e : entities) {
                        if (e instanceof NetworkedPlayer) {
                            NetworkedPlayer networked = (NetworkedPlayer) e;
                            Debug.log(networked.getAccount().getUserName() + ", " + networked.getTile().getLocation().toString());
                            Debug.log(player.getAccount().getUserName() + ", " + player.getTile().getLocation().toString());
                            //player.setKills(networked.getKills());
                            if (networked.getAccount().getUserName().equals(player.getAccount().getUserName())) {
                                System.out.println("equals");
                                player.setDeaths(networked.getDeaths());
                                player.setKills(networked.getKills());
                                player.setLastDamageDone(networked.getLastDamageDone());
                                if (networked.getHealth() != player.getHealth()) {
                                    player.setHealth(networked.getHealth());
                                    player.setSpeed(networked.getSpeed());
                                }
                            }
                        }
                    }
                } else {
                    shutdown();
                }
            }
            if (o instanceof String) {
                Debug.log((String) o);
            }
            if (o instanceof NetworkedPlayer) {
                System.out.println("Something went wrong");
            }
        } catch (IOException ex) {
            Logger.getLogger(GameWorldMultiPlayer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GameWorldMultiPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void shutdown() {
        try {
            this.stopped = true;
            socketConnection.close();
            //this.finalize();
        } catch (IOException ex) {
            Logger.getLogger(GameWorldMultiPlayer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Throwable ex) {
            Logger.getLogger(GameWorldMultiPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
