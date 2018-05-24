package DomumViri.Entities;

import DomumViri.Entities.Character;
import DomumViri.Entities.Entity;
import DomumViri.Entities.Player;
import DomumViri.Interfaces.IDamageable;
import DomumViri.Tiles.Tile;
import java.util.ArrayList;
import java.util.List;
import DomumViri.World.IGameWorld;

public class Bot extends Character implements IDamageable {

    private Item item;
    private long lastheavy;
    private boolean isAttacking;
    private long attackDelay;

    public Bot(Tile t, float gravmod, float speedmod, float dmgmod, float healthmod, float armormod, String naam) {
        super(t, gravmod, speedmod, dmgmod, healthmod, armormod, naam);
        lastheavy = System.currentTimeMillis();
        isAttacking = false;
    }

    public void guessNextMove() {
        // TODO - implement Bot.guessNextMove
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param damage
     * @param damageModifier
     */
    public boolean attack(int damage, float damageModifier) {
        // TODO - implement Bot.attack
        throw new UnsupportedOperationException();
    }

    public boolean block() {
        // TODO - implement Bot.block
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param amount
     */
    public boolean damage(int amount) {
        // TODO - implement Bot.damage
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(float seconds, List<Entity> entities, Player player) {
        if (this.tile.getLocation().getX() < player.tile.getLocation().getX()) { // Bot is links van de speler.
            // Loop naar rechts
            this.facingLeft = false;
            this.facingRight = true;
            if (this.tile.getLocation().getX() - player.tile.getLocation().getX() < 5) {
                move(5, 0);
            }
        } else if (this.tile.getLocation().getX() > player.tile.getLocation().getX()) { // Bot is rechts van de speler.
            // Loop naar links
            this.facingLeft = true;
            this.facingRight = false;
            if (this.tile.getLocation().getX() - player.tile.getLocation().getX() > 5) {
                move(-5, 0);
            }
        }

        // Val aan als er een speler in de buurt is.
        for (Entity e : new ArrayList<>(entities)) {
            if (e.tile.getLocation().getX() < this.getTile().getLocation().getX() + 64
                    && e.tile.getLocation().getX() > this.getTile().getLocation().getX() - 64
                    && e.tile.getLocation().getY() < this.getTile().getLocation().getY() + 32
                    && e.tile.getLocation().getY() > this.getTile().getLocation().getY() - 32
                    && e instanceof Player) {
                if (System.currentTimeMillis() - lastheavy > 500) {
                    lastheavy = System.currentTimeMillis();
                    if (this.facingLeft == true) {
                        this.attackingLeft = true;
                        this.attackingRight = false;
                    } else if (this.facingLeft == false) {
                        this.attackingLeft = false;
                        this.attackingRight = true;
                    }

//                    if (!isAttacking) {
//                        isAttacking = true;
//                        attackDelay = System.currentTimeMillis() + (long) (Math.random() * 0);
//                    }
//                    if (isAttacking) {
//                        if (System.currentTimeMillis() > attackDelay) {
                    normalAttack((Player) e);
//                            isAttacking = false;
//                        }
//                    }
                }
            } else if (e instanceof Player) {
                isAttacking = false;
            }
        }
        super.update(seconds, entities, player);
    }

    public boolean pickUp() {
        // TODO - implement Bot.pickUp
        throw new UnsupportedOperationException();
    }

    public boolean normalAttack(Character target) {
        attack(target, 2);
        return true;
    }

    public boolean fastAttack(Character target) {
        // TODO - implement Bot.fastAttack
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean heavyAttack(Character target) {
        // TODO - implement Bot.heavyAttack
        throw new UnsupportedOperationException();
    }

    public boolean useItem() {
        // TODO - implement Bot.useItem
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean attack(Character target, int multiplier) {
        if (this.facingLeft) {
            float damage = super.getDamage() * damageModifier * multiplier;
            target.takeDamageFromRight((int) damage);
        } else if (this.facingRight) {
            float damage = super.getDamage() * damageModifier *  multiplier;
            target.takeDamageFromLeft((int) damage);
        }
        target.lastDamageDone = this;
        return true;
    }
}
