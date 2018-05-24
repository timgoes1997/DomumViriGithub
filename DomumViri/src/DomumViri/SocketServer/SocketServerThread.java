/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.SocketServer;

import DomumViri.Entities.Character;
import DomumViri.Entities.Entity;
import DomumViri.Entities.NetworkedPlayer;
import DomumViri.Main.GameMode;
import DomumViri.Tiles.Tile;
import DomumViri.Time.TimeStatus;
import DomumViri.User.Team;
import java.awt.BorderLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tim
 */
public class SocketServerThread implements Runnable {

    private Socket socket;
    private SocketServer server;
    private Boolean running;

    //Playerinfo
    private NetworkedPlayer player;

    //Server info
    private static final int SECONDINNANOS = 1000000000;
    private int tps;
    private long currentTicks;

    private ObjectInputStream serverInputStream;
    private ObjectOutputStream serverOutputStream;

    public SocketServerThread(SocketServer server, Socket socket) {
        this.socket = socket;
        this.server = server;
        this.tps = 0;
        this.currentTicks = System.nanoTime();
    }

    @Override
    public void run() {
        try {
            //System.out.println("new server thread");
            serverInputStream = new ObjectInputStream(socket.getInputStream());
            serverOutputStream = new ObjectOutputStream(socket.getOutputStream());
            running = true;
            while (running && socket.isConnected() && !socket.isClosed()) {
                serverOutputStream.reset();
                //System.out.println("Reset");
                Object o = serverInputStream.readObject();
                //System.out.println("Object streamed in");
                if (o instanceof NetworkedPlayer) {
                    //System.out.println("Instance found");
                    player = (NetworkedPlayer) o;
                    if (server.getMode() == GameMode.MPFFA && server.getServerWorld().getPlayerCount() < server.getPlayerCap() && server.getServerWorld().getTime().getStatus() != TimeStatus.Finished) {
                        server.getServerWorld().addEntity(player);
                        String connectString = "Player: " + player.getNaam() + " has connected to the server";
                        System.out.println(connectString);
                        serverOutputStream.writeObject(player);
                        serverOutputStream.flush();
                    } else if (server.getMode() == GameMode.MPTeams && server.getServerWorld().getPlayerCount() < server.getPlayerCap() && server.getServerWorld().getTime().getStatus() != TimeStatus.Finished) {
                        System.out.println("Checking teams");
                        checkTeams(player);
                    } else {
                        serverOutputStream.writeObject(null);
                        serverOutputStream.flush();
                    }
                } else if (o instanceof String) {
                    serverOutputStream.writeObject(findCommand((String) o));
                    serverOutputStream.flush();
                } else if (o instanceof List) {
                    List<Entity> entities = (List<Entity>) o;
                    List<Entity> serverEntities = updateEntities(entities);
                    serverOutputStream.reset();
                    serverOutputStream.writeObject(serverEntities);
                    serverOutputStream.flush();
                } else {
                    String output = "PlayerNotFound";
                    serverOutputStream.writeObject(output);
                    serverOutputStream.flush();
                }

                tps++;
                if (System.nanoTime() >= currentTicks + SECONDINNANOS) {
                    currentTicks = System.nanoTime();
                    tps = 0;
                }
            }
            socket.close();
        } catch (Exception ex) {
            Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Throwable ex) {
            Logger.getLogger(SocketServerThread.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                socket.close();
                server.getServerWorld().removeEntity(player);
                this.finalize();
            } catch (IOException ex) {
                Logger.getLogger(SocketServerThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Throwable ex) {
                Logger.getLogger(SocketServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Updates the entities on the server with the list of received entities
     *
     * @param receivedEntities The entities that the server received
     * @return The updated entity list.
     */
    private synchronized List<Entity> updateEntities(List<Entity> receivedEntities) {
        if (receivedEntities.size() != server.getServerWorld().getAmountOfEntities()) {
            if (playerPickedUpItem(receivedEntities)) {
                updateOwnPlayer(receivedEntities);
                NetworkedPlayer nPlayer = checkPlayerItemChanged(receivedEntities);
                updatePlayer(nPlayer);
                return server.getServerWorld().getEntities();
            } else {
                updatePlayers(receivedEntities);
                return server.getServerWorld().getEntities();
            }
        } else {
            updatePlayers(receivedEntities);
            return server.getServerWorld().getEntities();
        }
    }

    /**
     * Checks if the player could have picked up the item.
     *
     * @param receivedEntities the list with the received entities
     * @return the checked NetworkedPlayer
     */
    private NetworkedPlayer checkPlayerItemChanged(List<Entity> receivedEntities) {
        NetworkedPlayer received = getPlayer(receivedEntities);
        if (received.getItem() != null) {
            for (Entity e : new ArrayList<>(server.getServerWorld().getEntities())) {
                if (received.getItem() == e && server.getServerWorld().withinBoundries(received.getTile(), player.getTile())) {
                    return received;
                }
            }
            received.setItem(null);
            return received;
        } else {
            return received;
        }
    }

    /**
     * checks if the player has pickedup a item.
     *
     * @param receivedEntities the list with the received items
     * @return wether a player has picked up a item or not.
     */
    private boolean playerPickedUpItem(List<Entity> receivedEntities) {
        NetworkedPlayer received = getPlayer(receivedEntities);
        if (received.getItem() == null && player.getItem() == null) {
            return false;
        } else if (received.getItem() == null && player.getItem() != null) {
            return true;
        } else if (received.getItem() != null && player.getItem() == null) {
            return true;
        } else {
            return false;
        }
    }

    private void updatePlayers(List<Entity> receivedEntities) {
        for (Entity client : receivedEntities) {
            if (client instanceof NetworkedPlayer) {
                NetworkedPlayer clientNPlayer = (NetworkedPlayer) client;
                NetworkedPlayer serverNPlayer = server.getServerWorld().getPlayer(clientNPlayer);

                if (clientNPlayer.getAccount().getUserName().equals(player.getAccount().getUserName())) {
                    updateOwnPlayer(clientNPlayer);
                } else if (serverNPlayer != null && serverNPlayer.getHealth() != clientNPlayer.getHealth()) {
                    clientNPlayer = updatePlayer(clientNPlayer);
                    System.out.println("Update: " + clientNPlayer.getAccount().getUserName());
                }

            }
        }
    }

    /**
     * Updates the player from the received entities list.
     *
     * @param receivedEntities
     */
    private void updateOwnPlayer(List<Entity> receivedEntities) {
        NetworkedPlayer received = getPlayer(receivedEntities);
        if (received != null) {
            //System.out.println("Update");
            player = server.getServerWorld().updatePlayer(received, player);
            //System.out.println("After " + player.getTile().getLocation().toString());
        }
    }

    /**
     * Updates the player from the received entities list.
     *
     * @param receivedEntities
     */
    private void updateOwnPlayer(NetworkedPlayer own) {
        if (own != null) {
            //System.out.println("Update");
            player = server.getServerWorld().updatePlayer(own, player);
            //System.out.println("After " + player.getTile().getLocation().toString());
        }
    }

    /**
     * Updates the given nPlayer in de world entities list to the given nPlayer.
     *
     * @param nPlayer The player you want to update.
     */
    private NetworkedPlayer updatePlayer(NetworkedPlayer nPlayer) {
        if (nPlayer != null) {
            return server.getServerWorld().updatePlayer(nPlayer, player); //Fout hier voor updaten andere player.
        } else {
            return null;
        }
    }

    /**
     * Gets the player running on this server thread;
     *
     * @param receivedEntities the entities the server has received from this
     * thread.
     * @return
     */
    private NetworkedPlayer getPlayer(List<Entity> receivedEntities) {
        for (Entity e : receivedEntities) {
            if (e instanceof NetworkedPlayer) {
                NetworkedPlayer nPlayer = (NetworkedPlayer) e;
                if (nPlayer.getAccount().getUserName().equals(player.getAccount().getUserName())) {
                    return nPlayer;
                }
            }
        }
        return null;
    }

    public Object findCommand(String command) {
        try {
            switch (command) {
                case Commands.GET_GAME_MODE:
                    return server.getMode();
                case Commands.GET_MAP:
                    return server.getServerWorld().getMapData();
                case Commands.PING:
                    return true;
                case Commands.SERVER_FULL:
                    return server.getServerWorld().getPlayerCount() < server.getPlayerCap();
                case Commands.GET_TIME:
                    return server.getServerWorld().getTime();
            }
            return null;
        } catch (Exception ex) {
            Logger.getLogger(SocketServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private void checkTeams(NetworkedPlayer player) throws IOException {
        Team playerTeam = null;
        if (player.getAccount().getTeam() == null) {
            playerTeam = Team.Red;
        } else {
            playerTeam = player.getAccount().getTeam();
        }
        
        Team opposite = null;
        if (playerTeam == Team.Blue) {
            opposite = Team.Red;
        } else {
            opposite = Team.Blue;
        }

        if (server.getServerWorld().isTeamAvaible(playerTeam)) {
            if(player.getAccount().getTeam() != playerTeam){
                player.getAccount().setTeam(playerTeam);
            }
            server.getServerWorld().addEntity(player);
            System.out.println(playerTeam.toString());
            String connectString = "Player: " + player.getNaam() + " has connected to the server";
            System.out.println(connectString);
            serverOutputStream.writeObject(player);
            serverOutputStream.flush();
        } else if (server.getServerWorld().isTeamAvaible(opposite)) {
            if (player.getAccount().getTeam() != opposite) {
                player.getAccount().setTeam(Team.Blue);
            }
            server.getServerWorld().addEntity(player);
            System.out.println(playerTeam.toString());            
            String connectString = "Player: " + player.getNaam() + " has connected to the server";
            System.out.println(connectString);
            serverOutputStream.writeObject(player);
            serverOutputStream.flush();
        } else {
            serverOutputStream.writeObject(null);
            serverOutputStream.flush();
        }
    }
}
