package DomumViri.World;

import DomumViri.Managers.InputManager;
import DomumViri.Level.SpawnPoint;
import DomumViri.Entities.Item;
import DomumViri.Entities.Entity;
import DomumViri.Entities.Player;
import DomumViri.User.Account;
import java.io.Serializable;
import java.util.List;

public interface IGameWorld extends Serializable{

    /**
     * Verkrijgt alle entiteiten uit de map.
     *
     * @return
     */
    List<Entity> getEntities();

    /**
     * Verkrijgt de speler.
     *
     * @return
     */
    Player getPlayer();

    /**
     * Reset de inputManager van de huidigespeler. (Dit zorgt er voor dat de
     * inputmanager zich juist gedraagt bij het hervatten van een level)
     *
     * @param im De nieuwe inputmanager.
     */
    void resetInputManager(InputManager im);

    /**
     * Verkrijgt het plaatje van de player en stelt zijn spawnpoint in.
     */
    void initializePlayer(InputManager im);
 
    /**
     * Doet het zelfde als initializaPlayer maar dan voor multiplayer.
     */
    void initializePlayer(InputManager im, Account account);
    /**
     * Gets the bot
     */
    void initializeBot();

    /**
     * Verkrijgt het plaatje van een item en stelt zijn spawnpoint in.
     */
    void initializeHealthPotion();

    /**
     * Verkrijgt het plaatje van een item en stelt zijn spawnpoint in.
     */
    void initializeShoes();

    /**
     * Verkrijgt het plaatje van een item en stelt zijn spawnpoint in.
     */
    void initializeSword();

    /**
     * Verkrijgt het plaatje van een item en stelt zijn spawnpoint in.
     */
    void initializeShield();

    /**
     * Wordt iedere frame geupdate en zorgt er voor dat alle entities in de
     * gameworld zich verplaatsen etc.
     *
     * @param seconds
     */
    void update(float seconds);

    /**
     * Kijkt of een entity al 5 kills heeft en eindigt dan het spel.
     */
    boolean endGame();

    /**
     * Stops the game.
     */
    void shutdown();
    
    /**
     * Herspawnt een entity
     *
     * @param e
     */
    void respawnEntity(Entity e);

    /**
     * Verkrijgt een willekeurig spawnpoint
     */
    SpawnPoint getRandomSpawnPoint();

    /**
     * Verwijdert een entity van de lijst met enititeiten
     */
    void deleteEntity(Item item);
}
