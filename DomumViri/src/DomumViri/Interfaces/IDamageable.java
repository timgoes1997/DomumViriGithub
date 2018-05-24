/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri.Interfaces;

import DomumViri.Entities.Character;

/**
 *
 * @author Tim
 */
public interface IDamageable {

    boolean attack(Character target, int multiplier);

    boolean block();

    boolean normalAttack(Character target);

    boolean fastAttack(Character target);

    boolean heavyAttack(Character target);
}
