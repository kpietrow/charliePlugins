/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package charlie.bs;

import charlie.advisor.BasicStrategy;
import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Seat;
import charlie.util.Play;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Devin's Work
 */
public class Test00_22_2 {
    
    public Test00_22_2() {
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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
     public void test1() {
         BasicStrategy bs = new BasicStrategy();
         Hid hid = new Hid(Seat.YOU, 125.0, 25.0);
         Hand newHand = new Hand(hid);
         Card card1 = new Card(4, Card.Suit.DIAMONDS);
         Card card2 = new Card(4, Card.Suit.CLUBS);
         newHand.hit(card1);
         newHand.hit(card2);
         Card card3 = new Card(5, Card.Suit.SPADES);
         assertEquals(Play.SPLIT, bs.advise(newHand, card3));   
     }
}
