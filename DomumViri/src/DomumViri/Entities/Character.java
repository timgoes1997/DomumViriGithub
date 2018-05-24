/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.Entities;

import DomumViri.Entities.Entity;
import DomumViri.Tiles.Tile;
import DomumViri.User.Account;
import java.io.Serializable;
import java.util.Random;
import DomumViri.World.IGameWorld;
import java.util.List;

/**
 *
 * @author jeroe
 */
public abstract class Character extends Entity implements Serializable {

    protected Item item;
    protected int itemTick;
    protected boolean usingItem;
    protected int damage;
    protected int health;
    protected int kills;
    protected int deaths;
    protected Character lastDamageDone;

    protected boolean facingLeft, facingRight, attackingLeft, attackingRight, blocking;
    protected long lastBlock, lastHeavyAttack;
    protected int armor;
    protected float damageModifier;
    protected float healthModifier;
    protected float armorModifier;

    protected String naam;
    protected Account account;

    /**
     *
     * @param t
     * @param gravmod
     * @param speedmod
     * @param dmgmod
     * @param healthmod
     * @param armormod
     */
    public Character(Tile t, float gravmod, float speedmod, float dmgmod, float healthmod, float armormod, String naam) {
        super(t, gravmod, speedmod);
        this.damageModifier = dmgmod;
        this.healthModifier = healthmod;
        this.armorModifier = armormod;
        this.naam = naam;
        this.damage = 10;
        this.health = 0;
        this.armor = 0;
        this.attackingLeft = false;
        this.attackingRight = false;
        this.facingLeft = false;
        this.facingRight = true;
        this.blocking = false;
        this.lastBlock = System.currentTimeMillis();
        this.lastHeavyAttack = System.currentTimeMillis();
    }

    /**
     *
     * @param t
     * @param gravmod
     * @param speedmod
     * @param dmgmod
     * @param healthmod
     * @param armormod
     */
    public Character(Tile t, float gravmod, float speedmod, float dmgmod, float healthmod, float armormod, Account account) {
        super(t, gravmod, speedmod);
        this.damageModifier = dmgmod;
        this.healthModifier = healthmod;
        this.armorModifier = armormod;
        this.account = account;
        this.naam = account.getUserName();
        this.damage = 10;
        this.health = 0;
        this.armor = 0;
        this.attackingLeft = false;
        this.attackingRight = false;
        this.facingLeft = false;
        this.facingRight = true;
        this.blocking = false;
        this.lastBlock = System.currentTimeMillis();
        this.lastHeavyAttack = System.currentTimeMillis();
    }

    // -------------------------------------------------
    // Getters
    // -------------------------------------------------
    public String getNaam() {
        return naam;
    }

    public Account getAccount() {
        return account;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public boolean isFacingLeft() {
        return facingLeft;
    }

    public boolean isFacingRight() {
        return facingRight;
    }

    public boolean isAttackingLeft() {
        return attackingLeft;
    }

    public boolean isAttackingRight() {
        return attackingRight;
    }

    public boolean isBlocking() {
        return blocking;
    }

    public boolean isUsingItem() {
        return usingItem;
    }

    public Item getItem() {
        return item;
    }

    public int getDamage() {
        return damage;
    }

    public int getArmor() {
        return armor;
    }

    public float getDamageModifier() {
        return damageModifier;
    }

    public float getHealthModifier() {
        return healthModifier;
    }

    public float getArmorModifier() {
        return armorModifier;
    }

    public Character getLastDamageDone() {
        return lastDamageDone;
    }

    // -------------------------------------------------
    // Setters
    // -------------------------------------------------
    public void setHealth(int health) {
        this.health = health;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public void setLastDamageDone(Character lastDamageDone) {
        this.lastDamageDone = lastDamageDone;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    // -------------------------------------------------
    // Methodes
    // ------------------------------------------------- 
    @Override
    public void update(float seconds, List<Entity> entities, Player player) {
        if (usingItem) {
            if (itemTick > 0) {
                itemTick--;
            } else {
                item = null;
            }
        }
        super.update(seconds, entities, player);
    }

    public void giveItem(Item item) {
        usingItem = false;
        Random rand = new Random();
        itemTick = 600 + rand.nextInt(600);
        resetModifiers();
        this.item = item;
    }

    public void takeDamageFromRight(int damage) {
        if (!blocking) {
            this.tile.getLocation().setY(tile.getLocation().getY() + 1);
            this.health += damage - (armor / 10);
            this.setFalling();
            this.speed = speed.add(-damage / 2 * ((float) this.health / 100), 2);
        }
    }

    public void takeDamageFromLeft(int damage) {
        if (!blocking) {
            this.tile.getLocation().setY(tile.getLocation().getY() + 1);
            this.health += damage - (armor / 10);
            this.setFalling();
            this.speed = speed.add(damage / 2 * ((float) this.health / 100), 2);
        }
    }

    public int getHealth() {
        return health;
    }

    public void addDeath() {
        deaths++;
    }

    public void addKills() {
        kills++;
    }
    
    public void setKills(int kills){
        this.kills = kills;
    }
    
    public void setDeaths(int deaths){
        this.deaths = deaths;
    }

    public void resetModifiers() {
        this.damageModifier = 1;
        this.movementSpeedModifier = 1;
        this.armor = 0;
    }
}
