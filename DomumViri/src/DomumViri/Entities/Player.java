package DomumViri.Entities;

import DomumViri.Debugging.Debug;
import DomumViri.Managers.InputManager;
import DomumViri.Interfaces.IDamageable;
import DomumViri.Entities.Items.Healthpotion;
import DomumViri.Entities.Items.Shield;
import DomumViri.Entities.Items.Shoes;
import DomumViri.Entities.Items.Sword;
import DomumViri.Main.GameMode;
import DomumViri.Tiles.Tile;
import DomumViri.User.Account;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import DomumViri.World.IGameWorld;

public class Player extends Character implements IDamageable, Serializable {

    private InputManager im;
    public boolean debugPressed = false;
    private long lastheavy;
    private GameMode gameMode;
    private static int STEAL_HEALTH = 50;
    private boolean stoleHealth;
    private long lastSteal;

    public boolean isStoleHealth() {
        return stoleHealth;
    }

    /**
     *
     * @param im
     * @param t
     * @param gravmod
     * @param speedmod
     * @param dmgmod
     * @param healthmod
     * @param armormod
     * @param naam
     * @param mode
     */
    public Player(InputManager im, Tile t, float gravmod, float speedmod, float dmgmod, float healthmod, float armormod, String naam, GameMode mode) {
        super(t, gravmod, speedmod, dmgmod, healthmod, armormod, naam);
        this.im = im;
        this.gameMode = mode;
        debugPressed = true;
        lastheavy = System.currentTimeMillis();
        lastSteal = 0;
    }

    /**
     *
     * @param im
     * @param t
     * @param gravmod
     * @param speedmod
     * @param dmgmod
     * @param healthmod
     * @param armormod
     * @param account
     * @param mode
     */
    public Player(InputManager im, Tile t, float gravmod, float speedmod, float dmgmod, float healthmod, float armormod, Account account, GameMode mode) {
        super(t, gravmod, speedmod, dmgmod, healthmod, armormod, account);
        this.im = im;
        this.gameMode = mode;
        debugPressed = true;
        lastheavy = System.currentTimeMillis();
        lastSteal = 0;
    }

    public void setPlayer(Tile t, float gravmod, float speedmod, float dmgmod, float healthmod, float armormod) {
        this.tile = t;
        this.gravityModifier = gravmod;
        this.movementSpeedModifier = speedmod;
        this.damageModifier = dmgmod;
        this.healthModifier = healthmod;
        this.armorModifier = armormod;
    }

    public boolean pickUp(Item item, List<Entity> entities) {
        // TODO - implement Player.pickUp
        if (item instanceof Healthpotion) {
            Debug.log("hp opgepakt");
            super.giveItem(item);
            entities.remove(item);
            return true;
        } else if (item instanceof Shield) {
            Debug.log("shield opgepakt");
            super.giveItem(item);
            entities.remove(item);
            return true;
        } else if (item instanceof Shoes) {
            Debug.log("schoen opgepakt");
            super.giveItem(item);
            entities.remove(item);
            return true;
        } else if (item instanceof Sword) {
            Debug.log("zwaard opgepakt");
            super.giveItem(item);
            entities.remove(item);
            return true;
        } else {
            return false;
        }
    }

    public boolean useItem() {
        if (item != null && !usingItem) {
            if (item instanceof Healthpotion) {
                Healthpotion healthpotion = (Healthpotion) item;
                this.health -= healthpotion.getHealthBoost();
                if (super.health < 0) {
                    super.health = 0;
                }
                item = null;
                return true;
            } else if (item instanceof Shield) {
                Shield shield = (Shield) item;
                this.armor = shield.getArmorBoost();
                this.usingItem = true;
                return true;
            } else if (item instanceof Shoes) {
                Shoes shoes = (Shoes) item;
                Debug.log(String.valueOf(shoes.getMovementBoost()));
                this.movementSpeedModifier = shoes.getMovementBoost();
                this.usingItem = true;
                return true;
            } else if (item instanceof Sword) {
                Sword sword = (Sword) item;
                this.damageModifier = sword.getDamageBoost();
                this.usingItem = true;
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public void resetInputManager(InputManager im) {
        this.im = im;
    }

    public void checkInput(List<Entity> entities) {
        if (im.isMoveUp()) {
            Debug.log("W Pressed");
            move(0, 6 * movementSpeedModifier);
        }
        if (im.isMoveDown()) {
            Debug.log("S Pressed");
            move(0, -5 * movementSpeedModifier);
        }
        if (im.isMoveLeft()) {
            Debug.log("A Pressed");
            this.facingLeft = true;
            this.facingRight = false;
            move(-6 * movementSpeedModifier, 0);
        }
        if (im.isMoveRight()) {
            Debug.log("D Pressed");
            this.facingLeft = false;
            this.facingRight = true;
            move(6 * movementSpeedModifier, 0);
        }
        if (im.isFireItem()) {
            Debug.log("Item picked up / used");
            for (Entity e : new ArrayList<>(entities)) {
                if (e.tile.getLocation().getX() < this.getTile().getLocation().getX() + 16
                        && e.tile.getLocation().getX() > this.getTile().getLocation().getX() - 16
                        && e.tile.getLocation().getY() < this.getTile().getLocation().getY() + 16
                        && e.tile.getLocation().getY() > this.getTile().getLocation().getY() - 16
                        && e instanceof Item) {
                    pickUp((Item) e, entities);
                }
            }
        }
        if (im.isUseItem()) {
            useItem();
        }
        if (im.isFireNormalAttack()) {
            Debug.log("Attacked");
            for (Entity e : new ArrayList<>(entities)) {
                if (e.tile.getLocation().getX() < this.getTile().getLocation().getX() + 64
                        && e.tile.getLocation().getX() > this.getTile().getLocation().getX() - 64
                        && e.tile.getLocation().getY() < this.getTile().getLocation().getY() + 32
                        && e.tile.getLocation().getY() > this.getTile().getLocation().getY() - 32
                        && e instanceof Character && e instanceof Player == false) {
                    if (e instanceof NetworkedPlayer) {
                        NetworkedPlayer n = (NetworkedPlayer) e;
                        if (!n.getAccount().getUserName().equals(account.getUserName())) {
                            if (System.currentTimeMillis() - lastheavy > 500) {
                                lastheavy = System.currentTimeMillis();
                                if (this.facingLeft == true) {
                                    this.attackingLeft = true;
                                    this.attackingRight = false;
                                } else if (this.facingLeft == false) {
                                    this.attackingLeft = false;
                                    this.attackingRight = true;
                                }
                                normalAttack((Character) e);
                            }
                        }
                    } else if (System.currentTimeMillis() - lastheavy > 500) {
                        lastheavy = System.currentTimeMillis();
                        if (this.facingLeft == true) {
                            this.attackingLeft = true;
                            this.attackingRight = false;
                        } else if (this.facingLeft == false) {
                            this.attackingLeft = false;
                            this.attackingRight = true;
                        }
                        normalAttack((Character) e);
                    }
                }
            }
        }
        if (im.isFireBlock()) {
            block();
        }
        if (im.isFireHeavyAttack()) //Code for take damage
        {
            Debug.log("Heavy Attacked");
            for (Entity e : new ArrayList<>(entities)) {
                if (e.tile.getLocation().getX() < this.getTile().getLocation().getX() + 64
                        && e.tile.getLocation().getX() > this.getTile().getLocation().getX() - 64
                        && e.tile.getLocation().getY() < this.getTile().getLocation().getY() + 32
                        && e.tile.getLocation().getY() > this.getTile().getLocation().getY() - 32
                        && e instanceof Character && e instanceof Player == false) {
                    if (e instanceof NetworkedPlayer) {
                        NetworkedPlayer n = (NetworkedPlayer) e;
                        if (!n.getAccount().getUserName().equals(account.getUserName())) {
                            if (System.currentTimeMillis() - lastheavy > 1500) {
                                lastheavy = System.currentTimeMillis();
                                if (this.facingLeft == true) {
                                    this.attackingLeft = true;
                                    this.attackingRight = false;
                                } else if (this.facingLeft == false) {
                                    this.attackingLeft = false;
                                    this.attackingRight = true;
                                }
                                heavyAttack((Character) e);
                            }
                        }
                    } else if (System.currentTimeMillis() - lastHeavyAttack > 1500) {
                        lastHeavyAttack = System.currentTimeMillis();
                        if (this.facingLeft == true) {
                            this.attackingLeft = true;
                            this.attackingRight = false;
                        } else if (this.facingLeft == false) {
                            this.attackingLeft = false;
                            this.attackingRight = true;
                        }
                        heavyAttack((Character) e);
                    }
                }
            }
        }
        if (im.isStealHealth()) {
            Debug.log("Steal health");
            if (gameMode == GameMode.MPTeams && (System.currentTimeMillis() - lastSteal > 2000)) {
                for (Entity e : new ArrayList<>(entities)) {
                    if (e.tile.getLocation().getX() < this.getTile().getLocation().getX() + 64
                            && e.tile.getLocation().getX() > this.getTile().getLocation().getX() - 64
                            && e.tile.getLocation().getY() < this.getTile().getLocation().getY() + 32
                            && e.tile.getLocation().getY() > this.getTile().getLocation().getY() - 32
                            && e instanceof Character && e instanceof Player == false) {
                        if (e instanceof NetworkedPlayer) {
                            NetworkedPlayer n = (NetworkedPlayer) e;
                            if (!n.getAccount().getUserName().equals(account.getUserName())) {
                                stealHealth((Character) e);
                                lastSteal = System.currentTimeMillis();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void update(float seconds, List<Entity> entities, Player player) {
        if (!blocking) {
            checkInput(entities);
        }
        if (System.currentTimeMillis() - lastBlock > 400) {
            blocking = false;
        }
        if (System.currentTimeMillis() - lastheavy > 200) {
            this.attackingRight = false;
            this.attackingLeft = false;
        }
        super.update(seconds, entities, player);
    }

    @Override
    public boolean attack(Character target, int multiplier) {
        if (!target.isBlocking()) {
            if (this.facingLeft) {
                float damage = (super.getDamage() * damageModifier * multiplier);
                Debug.log(String.valueOf(damage));
                target.takeDamageFromRight((int) damage);
            } else if (this.facingRight) {
                float damage = (super.getDamage() * damageModifier * multiplier);
                Debug.log(String.valueOf(damage));
                target.takeDamageFromLeft((int) damage);
            }
            Debug.log("Geraakt!");
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean block() {
        if (System.currentTimeMillis() - lastBlock > 600) {
            Debug.log("Blocked");
            lastBlock = System.currentTimeMillis();
            this.blocking = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean normalAttack(Character target) {
        if (gameMode == GameMode.SinglePlayer) {
            target.lastDamageDone = this;
            attack(target, 1);
        } else if (gameMode == GameMode.MPFFA) {
            target.lastDamageDone = new NetworkedPlayer(this.getTile(), this.getGravityModifier(), this.getMovementSpeedModifier(), this.getDamageModifier(), this.getHealthModifier(), this.getArmorModifier(), this.getAccount(), this.facingLeft);
            attack(target, 1);
        } else if (gameMode == GameMode.MPTeams) {
            if (target.getAccount().getTeam() != this.account.getTeam()) {
                target.lastDamageDone = new NetworkedPlayer(this.getTile(), this.getGravityModifier(), this.getMovementSpeedModifier(), this.getDamageModifier(), this.getHealthModifier(), this.getArmorModifier(), this.getAccount(), this.facingLeft);
                attack(target, 1);
            }
        }
        return true;
    }

    @Override
    public boolean fastAttack(Character target) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean heavyAttack(Character target) {
        if (gameMode == GameMode.SinglePlayer) {
            target.lastDamageDone = this;
            attack(target, 2);
        } else if (gameMode == GameMode.MPFFA) {
            target.lastDamageDone = new NetworkedPlayer(this.getTile(), this.getGravityModifier(), this.getMovementSpeedModifier(), this.getDamageModifier(), this.getHealthModifier(), this.getArmorModifier(), this.getAccount(), this.facingLeft);
            attack(target, 2);
        } else if (gameMode == GameMode.MPTeams) {
            if (target.getAccount().getTeam() != this.account.getTeam()) {
                attack(target, 2);
                target.lastDamageDone = new NetworkedPlayer(this.getTile(), this.getGravityModifier(), this.getMovementSpeedModifier(), this.getDamageModifier(), this.getHealthModifier(), this.getArmorModifier(), this.getAccount(), this.facingLeft);
            }
        }
        Debug.log("Geraakt!");
        return true;
    }

    public boolean stealHealth(Character target) {
        if (gameMode == GameMode.MPTeams) {
            if (target.getAccount().getTeam() == account.getTeam()) {
                if (target instanceof NetworkedPlayer) {
                    NetworkedPlayer other = (NetworkedPlayer) target;
                    if (this.getHealth() < STEAL_HEALTH) {
                        other.stealHealth(this.getHealth());
                        this.health = 0;
                    } else {
                        other.stealHealth(STEAL_HEALTH);
                        this.health -= STEAL_HEALTH;
                    }
                    stoleHealth = true;
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
