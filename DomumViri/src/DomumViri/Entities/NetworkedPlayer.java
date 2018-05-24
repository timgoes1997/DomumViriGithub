/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.Entities;

import DomumViri.Point2DSerializable.Point2D;
import DomumViri.Tiles.Tile;
import DomumViri.User.Account;
import java.io.Serializable;

/**
 *
 * @author Tim
 */
public class NetworkedPlayer extends Character implements Serializable {

    private boolean healthChanged;
    private boolean healthStolen;
    private boolean stoleHealth;
    private boolean reset;

    public boolean isStoleHealth() {
        return stoleHealth;
    }
    
    public boolean isHealthChanged() {
        return healthChanged;
    }

    public boolean isHealthStolen() {
        return healthStolen;
    }

    public boolean isReset() {
        return reset;
    }

    public NetworkedPlayer(Tile t, float gravmod, float speedmod, float dmgmod, float healthmod, float armormod, Account account, boolean facingLeft) {
        super(t, gravmod, speedmod, dmgmod, healthmod, armormod, account);
        this.deaths = 0;
        this.kills = 0;
        this.stoleHealth = false;
        if (facingLeft) {
            this.facingLeft = true;
        } else {
            this.facingRight = true;
        }
    }
    

    /**
     * Sets the current stats of the player for networking.
     *
     * @param t The tile of the player (stores the imagepath and position of the
     * player)
     * @param gravmod The gravitymodifier of the player
     * @param speedmod The speedmodifier of the player
     * @param dmgmod The damagemodifier of the player
     * @param healthmod The healthmodifier of the player
     * @param armormod The armormodifier of the player
     * @param isFacingLeft The direction which the player is facing. if
     * isFacingLeft is false then the character will be facing to the right.
     * @param health The current health of the player
     * @param speed The current speed of the character
     * @param lastDamageDone Only use serializable characters which can be send
     * to the server, like NetworkedPlayers or Bots
     * @param stoleHealth
     * @param isBlocking
     * @param reset
     */
    public void setNetworkedPlayer(Tile t, float gravmod, float speedmod, float dmgmod, float healthmod, float armormod, boolean isFacingLeft, int health, Point2D speed, Character lastDamageDone, boolean stoleHealth, boolean isBlocking, boolean reset) {
        this.tile = t;
        this.gravityModifier = gravmod;
        this.movementSpeedModifier = speedmod;
        this.damageModifier = dmgmod;
        this.healthModifier = healthmod;
        this.armorModifier = armormod;
        this.facingLeft = isFacingLeft;
        this.health = health;
        this.speed = speed;
        this.healthChanged = false;
        this.lastDamageDone = lastDamageDone;
        this.stoleHealth = stoleHealth;
        this.blocking = isBlocking;
        this.reset = reset;
    }

    public void updateHealth(int health, Point2D speed) {
        this.health = health;
        this.speed = speed;
        this.healthChanged = true;
        this.healthStolen = false;
    }

    public void stealHealth(int health){
        this.health += health;
        this.healthChanged = true;
    }
    
    /**
     * Updates the health of the player
     *
     * @param health The health you want to give to the new player.
     * @param speed The new speed of the player.
     * @param lastDamageDone The player which has done the last damage to this player.
     */
    public void updateHealth(int health, Point2D speed, Character lastDamageDone) {
        this.health = health;
        this.speed = speed;
        this.healthChanged = true;
        this.lastDamageDone = lastDamageDone;
    }
    
    public void increaseKills(){
        kills++;
    }
    
    public void increaseDeaths(){
        deaths++;
    }
    
    public void setResetFalse(){
        reset = false;
    }
}
