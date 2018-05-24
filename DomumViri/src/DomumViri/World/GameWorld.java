/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.World;

import DomumViri.Entities.Bot;
import DomumViri.Entities.Entity;
import DomumViri.Entities.Character;
import DomumViri.Entities.Item;
import DomumViri.Entities.Items.Healthpotion;
import DomumViri.Entities.Items.Shield;
import DomumViri.Entities.Items.Shoes;
import DomumViri.Entities.Items.Sword;
import DomumViri.Entities.Player;
import DomumViri.Level.Map;
import DomumViri.Level.MapInfo;
import DomumViri.Level.MapName;
import DomumViri.Level.SpawnPoint;
import DomumViri.Main.GameMode;
import DomumViri.Managers.InputManager;
import DomumViri.Tiles.Tile;
import DomumViri.Time.Time;
import DomumViri.User.Account;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tim
 */
public class GameWorld implements IGameWorld, Serializable {

    private static final String userDir = System.getProperty("user.dir");

    protected Map map;
    protected List<Entity> entities;
    protected Player player;
    protected Bot bot;
    protected int itemTick;
    protected GameMode mode;
    protected Time time; 
    protected boolean stopped = false;

    /**
     * Maakt een nieuwe gamewereld aan.
     *
     * @param levelPath Het pad naar het level.
     * @param infoPath Het pad naar het levellayout
     * @param playerImagePath Het pad naar de image van de player
     * @param im De inputmanager
     * @param bots Hoeveel bots er mee doen aan deze wereld.
     */
    public GameWorld(MapName name, MapInfo info, InputManager im, GameMode mode, Time time, int bots) {
        this.time = time;        
        this.mode = mode;
        map = new Map(name, info);
        initializePlayer(im);
        entities = new ArrayList<>();
        initializeBot();
    }

    /**
     *
     * @param levelPath Het pad naar het level.
     * @param infoPath Het pad naar het levellayout
     * @param playerImagePath Het pad naar de image van de player
     * @param im De inputmanager
     */
    public GameWorld(MapName name, MapInfo info, InputManager im, GameMode mode, Time time) {
        this.time = time;        
        this.mode = mode;
        map = new Map(name, info);
        initializePlayer(im);
        entities = new ArrayList<>();
        itemTick = 600;
        initializeBot();
    }

    /**
     *
     * @param levelPath Het pad naar het level.
     * @param infoPath Het pad naar het levellayout
     * @param playerImagePath Het pad naar de image van de player
     * @param im De inputmanager
     */
    public GameWorld(MapName name, MapInfo info, InputManager im, GameMode mode, Account account, Time time) {
        this.time = time;       
        this.mode = mode;
        map = new Map(name, info);
        initializePlayer(im, account);
        entities = new ArrayList<>();
        itemTick = 600;
    }

    /**
     * Maakt een nieuwe gamewereld aan.
     *
     * @param levelPath Het pad naar het level.
     * @param infoPath Het pad naar het levellayout
     * @param playerImagePath Het pad naar de image van de player
     * @param im De inputmanager
     * @param bots Hoeveel bots er mee doen aan deze wereld.
     */
    public GameWorld(InputManager im, GameMode mode, Time time, int bots) {
        this.time = time;        
        this.mode = mode;
        initializePlayer(im);
        entities = new ArrayList<>();
        initializeBot();
    }

    /**
     *
     * @param levelPath Het pad naar het level.
     * @param infoPath Het pad naar het levellayout
     * @param playerImagePath Het pad naar de image van de player
     * @param im De inputmanager
     */
    public GameWorld(InputManager im, GameMode mode, Time time) {
        this.time = time;
        this.mode = mode;
        initializePlayer(im);
        entities = new ArrayList<>();
        itemTick = 600;
        initializeBot();
    }

    /**
     * Default constructor die uiteindelijk voor GWM wordt gebruikt.
     */
    protected GameWorld() {
        entities = new ArrayList<>();
    }

    /**
     *
     * @param levelPath Het pad naar het level.
     * @param infoPath Het pad naar het levellayout
     * @param playerImagePath Het pad naar de image van de player
     * @param im De inputmanager
     */
    public GameWorld(InputManager im, GameMode mode, Account account) {
        this.mode = mode;
        initializePlayer(im, account);
        entities = new ArrayList<>();
        itemTick = 600;
    }

    public GameMode getMode() {
        return mode;
    }

    public Map getMap() {
        return map;
    }

    @Override
    public List<Entity> getEntities() {
        return entities;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    public boolean isStopped() {
        return stopped;
    }

    public Time getTime() {
        return time;
    }

    @Override
    public void resetInputManager(InputManager im) {
        player.resetInputManager(im);
    }

    @Override
    public void initializePlayer(InputManager im) {
        Random rand = new Random(); //Randomizer om een willekeurig spawnpoint te bepalen.
        int rnd = rand.nextInt(map.getPlayerSpawnPoints().size());
        SpawnPoint sp = map.getPlayerSpawnPoints().get(rnd);
        int entityID = 1;
        Tile t = new Tile(entityID, "Player", entityID, (int) sp.getX(), (int) sp.getY(), (int) map.getEntityImage(entityID).getWidth(), (int) map.getEntityImage(entityID).getHeight(), false, false); //De playertile.
        player = new Player(im, t, 9.81f, 1, 1, 1, 1, "Player", mode); //De speler zelf.
    }

    @Override
    public void initializePlayer(InputManager im, Account account) {
        Random rand = new Random(); //Randomizer om een willekeurig spawnpoint te bepalen.
        int rnd = rand.nextInt(map.getPlayerSpawnPoints().size());
        SpawnPoint sp = map.getPlayerSpawnPoints().get(rnd);
        int entityID = 1;
        Tile t = new Tile(entityID, account.getUserName(), entityID, (int) sp.getX(), (int) sp.getY(), (int) map.getEntityImage(entityID).getWidth(), (int) map.getEntityImage(entityID).getHeight(), false, false); //De playertile.
        player = new Player(im, t, 9.81f, 1, 1, 1, 1, account, mode); //De speler zelf.
    }

    @Override
    public void initializeBot() {
        Random rand = new Random();
        int rnd = rand.nextInt(map.getPlayerSpawnPoints().size());
        SpawnPoint sp = map.getPlayerSpawnPoints().get(rnd);
        int entityID = 2;
        Tile t = new Tile(entityID, "bot", entityID, (int) sp.getX(), (int) sp.getY(), (int) map.getEntityImage(entityID).getWidth(), (int) map.getEntityImage(entityID).getHeight(), false, false);
        bot = new Bot(t, 9.81f, 1, 1, 1, 1, "Bot" + entities.size());
        entities.add(bot);
    }

    @Override
    public void initializeHealthPotion() {
        Random rand = new Random(); //Randomizer om een willekeurig spawnpoint te bepalen.
        int rnd = rand.nextInt(map.getItemSpawnPoints().size());
        SpawnPoint sp = map.getItemSpawnPoints().get(rnd);
        int entityID = 3;
        Tile t = new Tile(1, "HealthPotion", entityID, (int) sp.getX(), (int) sp.getY(), 64, 64, false, false); //De playertile.
        Healthpotion hp = new Healthpotion(t, 9.81f, 1, 100); //De speler zelf.
        entities.add(hp);
    }

    @Override
    public void initializeShoes() {
        Random rand = new Random(); //Randomizer om een willekeurig spawnpoint te bepalen.
        int rnd = rand.nextInt(map.getItemSpawnPoints().size());
        SpawnPoint sp = map.getItemSpawnPoints().get(rnd);
        int entityID = 4;
        Tile t = new Tile(1, "Shoes", entityID, (int) sp.getX(), (int) sp.getY(), 64, 64, false, false); //De playertile.
        Shoes shoes = new Shoes(t, 9.81f, 1, 100); //De speler zelf.
        entities.add(shoes);
    }

    @Override
    public void initializeSword() {
        Random rand = new Random(); //Randomizer om een willekeurig spawnpoint te bepalen.
        int rnd = rand.nextInt(map.getItemSpawnPoints().size());
        SpawnPoint sp = map.getItemSpawnPoints().get(rnd);
        int entityID = 5;
        Tile t = new Tile(1, "Sword", entityID, (int) sp.getX(), (int) sp.getY(), 64, 64, false, false); //De playertile.
        Sword sword = new Sword(t, 9.81f, 1, 100); //De speler zelf.
        entities.add(sword);
    }

    @Override
    public void initializeShield() {
        Random rand = new Random(); //Randomizer om een willekeurig spawnpoint te bepalen.
        int rnd = rand.nextInt(map.getItemSpawnPoints().size());
        SpawnPoint sp = map.getItemSpawnPoints().get(rnd);
        int entityID = 6;
        Tile t = new Tile(1, "Shield", entityID, (int) sp.getX(), (int) sp.getY(), 64, 64, false, false); //De playertile.
        Shield shield = new Shield(t, 9.81f, 1, 100); //De speler zelf.
        entities.add(shield);
    }

    @Override
    public void update(float seconds) {
        for (Entity e : new ArrayList<>(entities)) {
            checkCollision(e, seconds);
        }
        //checkCollision(player, seconds);
    }

    public void checkCollision(Entity e, float seconds) {
        //if(e instanceof Item){
        //    Debug.log("Found entity");
        //}
        // Valt de speler uit de map. 
        if (e.getTile().getLocation().getY() < -64) {
            SpawnPoint sp = getRandomSpawnPoint();
            e.getTile().getLocation().setX((sp.getX() * e.getTile().getWidth()) - e.getTile().getWidth());
            e.getTile().getLocation().setY((sp.getY() * e.getTile().getHeight()) - e.getTile().getHeight());
        }

        e.update(seconds, entities, player); //Update de entities.
        for (Tile t : map.getTileMap()) { //Doorloopt iedere tile in de tilemap.
            if (t.isCollidable() && e.getTile().intersects(t)) { //Kijkt of de tile collidable is
                e.getTile().getLocation().setY(t.getLocation().getY() + e.getTile().getLocation().getHeight()); //Zet de y coord van de player omhoog.
                e.setNotFalling(); //Zorgt er voor dat de player niet meer kan vallen.
                if (t.isDeath()) {
                    respawnEntity(e);
                }
            }
        }
    }

    @Override
    public boolean endGame() {
        for (Entity e : entities) {
            if (e instanceof Character) {
                Character c = (Character) e;
                if (c.getKills() >= 5) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void respawnEntity(Entity e) {
        SpawnPoint sp = getRandomSpawnPoint();
        e.getTile().getLocation().setX((sp.getX() * e.getTile().getWidth()) - e.getTile().getWidth());
        e.getTile().getLocation().setY((sp.getY() * e.getTile().getHeight()) - e.getTile().getHeight());
        if (e instanceof Character) {
            Character c = (Character) e;
            c.addDeath();
            c.resetModifiers();
            if (c.getLastDamageDone() != null) {
                c.getLastDamageDone().addKills();
            }
            c.setLastDamageDone(null);
            c.setItem(null);
            c.setHealth(0);
            c.setArmor(0);
        }
    }

    @Override
    public SpawnPoint getRandomSpawnPoint() {
        Random rand = new Random(); //Randomizer om een willekeurig spawnpoint te bepalen.
        int rnd = rand.nextInt(map.getPlayerSpawnPoints().size());
        return map.getPlayerSpawnPoints().get(rnd);
    }

    @Override
    public void deleteEntity(Item item) {
        this.entities.remove(item);
    }

    @Override
    public void shutdown() {
        try {
            this.stopped = true;
            //this.finalize();
        } catch (Throwable ex) {
            Logger.getLogger(GameWorld.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
