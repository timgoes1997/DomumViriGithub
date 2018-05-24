/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.World.SinglePlayer;

import DomumViri.Entities.Entity;
import DomumViri.Entities.Item;
import DomumViri.Level.Map;
import DomumViri.Level.MapInfo;
import DomumViri.Level.MapName;
import DomumViri.Level.SpawnPoint;
import DomumViri.Main.GameMode;
import DomumViri.Managers.InputManager;
import DomumViri.Time.Time;
import DomumViri.User.Account;
import DomumViri.World.GameWorld;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Tim
 */
public class GameWorldSinglePlayer extends GameWorld {

    /**
     * Maakt een nieuwe gamewereld aan.
     *
     * @param name
     * @param info
     * @param im De inputmanager
     * @param bots Hoeveel bots er mee doen aan deze wereld.
     */
    public GameWorldSinglePlayer(MapName name, MapInfo info, InputManager im, GameMode mode, Time time, int bots) {
        super(name, info, im, mode, time, bots);
        entities.add(player);
        initializeBot();
    }

    /**
     *
     * @param name
     * @param info
     * @param im De inputmanager
     */
    public GameWorldSinglePlayer(MapName name, MapInfo info, InputManager im, GameMode mode, Time time) {
        super(name, info, im, mode, time);
        entities.add(player);
        initializeBot();
    }

    /**
     *
     * @param name
     * @param info
     * @param account
     * @param mode
     * @param im De inputmanager
     */
    public GameWorldSinglePlayer(MapName name, MapInfo info, InputManager im, GameMode mode, Account account, Time time) {
        super(name, info, im, mode, account, time);
        entities.add(player);
        initializeBot();
    }

    /**
     * Reset de inputManager van de huidigespeler. (Dit zorgt er voor dat de
     * inputmanager zich juist gedraagt bij het hervatten van een level)
     *
     * @param im De nieuwe inputmanager.
     */
    public void resetInputManager(InputManager im) {
        player.resetInputManager(im);
    }

    /**
     * Wordt iedere frame geupdate en zorgt er voor dat alle entities in de
     * gameworld zich verplaatsen etc.
     *
     * @param seconds
     */
    public void update(float seconds) {
        super.update(seconds);
        itemTick++;
        spawnItem();
    }

    /**
     * Kijkt of een entity al 5 kills heeft en eindigt dan het spel.
     *
     * @return kijkt of de game beeindigt moet worden
     */
    @Override
    public boolean endGame() {
        for (Entity e : entities) {
            if (e instanceof DomumViri.Entities.Character) {
                DomumViri.Entities.Character c = (DomumViri.Entities.Character) e;
                if (c.getKills() >= 5) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Spawnt een item
     */
    public void spawnItem() {
        if (itemTick > 600) {
            Random rand = new Random(); //Randomizer om een willekeurig spawnpoint te bepalen.
            int chance = rand.nextInt(400);

            if (chance == 0) {
                initializeHealthPotion();
                itemTick = 0;
            }
            if (chance == 1) {
                initializeShoes();
                itemTick = 0;
            }
            if (chance == 2) {
                initializeSword();
                itemTick = 0;
            }
            if (chance == 3) {
                initializeShield();
                itemTick = 0;
            }
        }
    }

    /**
     * Herspawnt een entity
     *
     * @param e
     */
    public void respawnEntity(Entity e) {
        SpawnPoint sp = getRandomSpawnPoint();
        e.getTile().getLocation().setX((sp.getX() * e.getTile().getWidth()) - e.getTile().getWidth());
        e.getTile().getLocation().setY((sp.getY() * e.getTile().getHeight()) - e.getTile().getHeight());
        if (e instanceof DomumViri.Entities.Character) {
            DomumViri.Entities.Character c = (DomumViri.Entities.Character) e;
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

    /**
     * Verkrijgt een willekeurig spawnpoint
     *
     * @return
     */
    public SpawnPoint getRandomSpawnPoint() {
        Random rand = new Random(); //Randomizer om een willekeurig spawnpoint te bepalen.
        int rnd = rand.nextInt(map.getPlayerSpawnPoints().size());
        return map.getPlayerSpawnPoints().get(rnd);
    }

    /**
     * Verwijdert een entity van de lijst met enititeiten
     *
     * @param item Het item dat je wilt verwijderen.
     */
    public void deleteEntity(Item item) {
        this.entities.remove(item);
    }

    public void addItems() {
        // TODO - implement GameWorld.addItems
        throw new UnsupportedOperationException();
    }

    public void addBots() {
        // TODO - implement GameWorld.addBots
        throw new UnsupportedOperationException();
    }
}
