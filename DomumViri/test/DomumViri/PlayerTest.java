/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri;

import DomumViri.Entities.Character;
import DomumViri.Entities.Player;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Wesley
 */
public class PlayerTest {
    
    public PlayerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of move method, of class Player.
     */
    @Test
    public void testMove() {
        System.out.println("move");
        Player instance = null;
        //instance.move();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of attack method, of class Player.
     */
    @Test
    public void testAttack() {
        System.out.println("attack");
        Character target = null;
        Player instance = null;
        boolean expResult = false;
        boolean result = instance.attack(target);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of block method, of class Player.
     */
    @Test
    public void testBlock() {
        System.out.println("block");
        Player instance = null;
        boolean expResult = false;
        boolean result = instance.block();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of damage method, of class Player.
     */
    @Test
    public void testDamage() {
        System.out.println("damage");
        int amount = 0;
        Player instance = null;
        boolean expResult = false;
        boolean result = instance.damage(amount);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of pickUp method, of class Player.
     
    @Test
    public void testPickUp() {
        System.out.println("pickUp");
        Player instance = null;
        boolean expResult = false;
        boolean result = instance.pickUp();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of normalAttack method, of class Player.
     */
    @Test
    public void testNormalAttack() {
        System.out.println("normalAttack");
        Player instance = null;
        boolean expResult = false;
        boolean result = instance.normalAttack();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of fastAttack method, of class Player.
     */
    @Test
    public void testFastAttack() {
        System.out.println("fastAttack");
        Player instance = null;
        boolean expResult = false;
        boolean result = instance.fastAttack();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of heavyAttack method, of class Player.
     */
    @Test
    public void testHeavyAttack() {
        System.out.println("heavyAttack");
        Player instance = null;
        boolean expResult = false;
        boolean result = instance.heavyAttack();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of useItem method, of class Player.
     */
    @Test
    public void testUseItem() {
        System.out.println("useItem");
        Player instance = null;
        boolean expResult = false;
        boolean result = instance.useItem();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
