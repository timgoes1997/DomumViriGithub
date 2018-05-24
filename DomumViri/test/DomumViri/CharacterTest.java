/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DomumViri;

import javafx.scene.shape.Rectangle;
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
public class CharacterTest {
    
    public CharacterTest() {
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
     * Test of giveItem method, of class Character.
     */
//    @Test
//    public void testGiveItem() {
//        System.out.println("giveItem");
//        Item item = null;
//        Character instance = null;
//        instance.giveItem(item);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of takeDamage method, of class Character.
     */
    @Test
    public void testTakeDamage() {
        System.out.println("takeDamage");
        int damage = 10; /*
        Character instance = new Bot(new Rectangle(1.0,1.0),1.0F,1.0F,1.0F,1.0F,1.0F);
        instance.takeDamage(damage);
        assertEquals(90,instance.getHealth());*/
    }

    //public class CharacterImpl extends Character {

        //public CharacterImpl() {
            //super(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        //}
    //}
    
}
