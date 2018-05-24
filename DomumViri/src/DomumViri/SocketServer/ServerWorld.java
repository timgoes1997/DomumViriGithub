/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.SocketServer;

import DomumViri.Entities.Entity;
import DomumViri.Entities.Items.Healthpotion;
import DomumViri.Entities.NetworkedPlayer;
import DomumViri.Level.Map;
import DomumViri.Level.MapData;
import DomumViri.Level.MapInfo;
import DomumViri.Level.MapName;
import DomumViri.Level.SpawnPoint;
import DomumViri.Point2DSerializable.Point2D;
import DomumViri.Tiles.Tile;
import DomumViri.Time.Time;
import DomumViri.User.Team;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Tim
 */
public class ServerWorld {

    private MapName name;
    private MapInfo info;
    private List<Entity> entities;
    private Time time;

    public ServerWorld(MapName name, MapInfo info, Time time) {
        //this.map = new Map(info);
        this.name = name;
        this.info = info;
        entities = new ArrayList<>();
        this.time = time;
    }

    public Time getTime() {
        return time;
    }

    public MapData getMapData() {
        return new MapData(name, info);
    }

    public synchronized boolean isTeamAvaible(Team team) {
        int red = 0;
        int blue = 0;
        synchronized (entities) {
            for (Entity e : entities) {
                if (e instanceof NetworkedPlayer) {
                    NetworkedPlayer server = (NetworkedPlayer) e;
                    if (server.getAccount().getTeam() != null && server.getAccount().getTeam() == Team.Red) {
                        red++;
                    } else if (server.getAccount().getTeam() != null && server.getAccount().getTeam() == Team.Blue) {
                        blue++;
                    }
                }
            }
        }
        if (team == Team.Red && red < 2) {
            System.out.println("Team available");
            return true;
        } else if (team == Team.Blue && blue < 2) {
            System.out.println("Team available");
            return true;
        } else {
            System.out.println("Team not available");
            return false;
        }
    }

    public synchronized int getAmountOfEntities() {
        synchronized (entities) {
            return entities.size();
        }
    }

    public synchronized List<Entity> getEntities() {
        synchronized (entities) {
            return entities;
        }
    }

    public synchronized void addEntity(Entity e) {
        synchronized (entities) {
            this.entities.add(e);
        }
    }

    public synchronized void removeEntity(Entity e) {
        synchronized (entities) {
            this.entities.remove(e);
        }
    }

    public int getPlayerCount() {
        int playerCount = 0;
        synchronized (entities) {
            for (Entity e : entities) {
                if (e instanceof NetworkedPlayer) {
                    playerCount++;
                }
            }
        }
        return playerCount;
    }

    /**
     * Checks if two tiles are within boundries, pre-defined boundry is 80.
     *
     * @param received the received tile.
     * @param current the current tile.
     * @return if they are within boundry.
     */
    public boolean withinBoundries(Tile received, Tile current) {
        return current.getLocation().getX() - 80 < received.getLocation().getX()
                && current.getLocation().getX() + 80 > received.getLocation().getX()
                && current.getLocation().getY() - 80 < received.getLocation().getY()
                && current.getLocation().getY() + 80 > received.getLocation().getY();
    }

    public synchronized NetworkedPlayer getPlayer(NetworkedPlayer player) {
        synchronized (entities) {
            for (Entity e : entities) {
                if (e instanceof NetworkedPlayer) {
                    NetworkedPlayer nPlayer = (NetworkedPlayer) e;
                    if (nPlayer.getAccount().getUserName().equals(player.getAccount().getUserName())) {
                        return nPlayer;
                    }
                }
            }
            return null;
        }
    }

    /**
     * Updates the player
     *
     * @param player
     * @return The new updatePlayer.
     */
    public synchronized NetworkedPlayer updatePlayer(NetworkedPlayer player, NetworkedPlayer clientPlayer) {
        synchronized (entities) {
            for (Entity e : entities) {
                if (e instanceof NetworkedPlayer) {
                    NetworkedPlayer server = (NetworkedPlayer) e;
                    if (server.getAccount().getUserName().equals(player.getAccount().getUserName())) {
                        //System.out.println(server.getAccount().toString() + ": " + server.getLastDamageDone() != null);
                        if (updateHealth(server, player, clientPlayer)) {
                            System.out.println("Health difference:" + server.getHealth() + "," + player.getHealth() + "," + clientPlayer.getHealth() + ", speed: " + player.getSpeed());
                            if (server.getLastDamageDone() != player.getLastDamageDone()) {
                                server.updateHealth(player.getHealth(), player.getSpeed(), player.getLastDamageDone()); //player.getLastDamageDone uiteindelijk null nog effe na kijken.
                            } else {
                                server.updateHealth(player.getHealth(), player.getSpeed());
                            }
                        } else if (player.getDeaths() > server.getDeaths()) {
                            if (server.getLastDamageDone() != null) {
                                System.out.println("Lastdamage");
                                NetworkedPlayer serverLastDamage = (NetworkedPlayer) server.getLastDamageDone();
                                updateKills(serverLastDamage);
                                server.updateHealth(player.getHealth(), player.getSpeed(), null);
                                server.addDeath();
                            } else {
                                System.out.println("Enter deaths");
                                server.updateHealth(player.getHealth(), player.getSpeed(), null);
                                server.addDeath();
                            }
                        } else if (clientPlayer.getAccount().getUserName().equals(server.getAccount().getUserName())) {
                            if (server.getHealth() != clientPlayer.getHealth() || server.isHealthChanged() && !server.isReset()) {
                                System.out.println("speed Changed");
                                server.setNetworkedPlayer(player.getTile(), player.getGravityModifier(),
                                        player.getMovementSpeedModifier(), player.getDamageModifier(), player.getHealthModifier(),
                                        player.getArmorModifier(), player.isFacingLeft(), server.getHealth(), server.getSpeed(), server.getLastDamageDone(),
                                        server.isHealthStolen(), server.isBlocking(), false);
                            } else if(!server.isReset()){
                                server.setNetworkedPlayer(player.getTile(), player.getGravityModifier(), player.getMovementSpeedModifier(),
                                        player.getDamageModifier(), player.getHealthModifier(), player.getArmorModifier(), player.isFacingLeft(),
                                        server.getHealth(), player.getSpeed(), player.getLastDamageDone(), player.isHealthStolen(), player.isBlocking(), false);
                            }else{
                                server.setResetFalse();
                            }
                        }
                        e = server;
                        return server;
                    }
                }
            }
        }
        return null;
    }

    private synchronized void updateKills(NetworkedPlayer update) {
        synchronized (entities) {
            for (Entity e : entities) {
                if (e instanceof NetworkedPlayer) {
                    NetworkedPlayer server = (NetworkedPlayer) e;
                    if (server.getAccount().getUserName().equals(update.getAccount().getUserName())) {
                        System.out.println("increase");
                        server.increaseKills();
                        e = server;
                    }
                }
            }
        }
    }

    public synchronized boolean updateHealth(NetworkedPlayer server, NetworkedPlayer client, NetworkedPlayer clientPlayer) {
        if (server.getHealth() != client.getHealth()) {
            if (server.getHealth() > client.getHealth() && server.getItem() instanceof Healthpotion) {
                return true;
            } else if (client.getHealth() > server.getHealth() && withinBoundries(client.getTile(), clientPlayer.getTile()) && !server.isBlocking()) {
                return true;
            } else if (server.getHealth() > client.getHealth() && withinBoundries(client.getTile(), clientPlayer.getTile()) && client.isStoleHealth()) {
                return true;
            } else if (client.getHealth() > server.getHealth() && withinBoundries(client.getTile(), clientPlayer.getTile()) && client.isHealthStolen()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void reset() {
        this.entities = new ArrayList<>();
    }

    public void resetEntities() {
        /*
        synchronized (entities) {
            for (Entity e : entities) {
                if (e instanceof NetworkedPlayer) {
                    NetworkedPlayer server = (NetworkedPlayer) e;
                    server.setNetworkedPlayer(server.getTile(), server.getGravityModifier(), server.getMovementSpeedModifier(), server.getDamageModifier(), 
                            server.getHealthModifier(), server.getArmorModifier(), server.isFacingLeft(), 0, Point2D.ZERO, null, false, false, true);
                }
            }
        }*/
    }

    
    /**
     * Werkt niet op de server op dit moment
     * @return 
     *//*
    public SpawnPoint getRandomSpawnPoint() {
        Random rand = new Random(); //Randomizer om een willekeurig spawnpoint te bepalen.
        int rnd = rand.nextInt(map.getPlayerSpawnPoints().size());
        return map.getPlayerSpawnPoints().get(rnd);
    }*/
}
