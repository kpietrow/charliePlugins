package charlie.bot.test;

import java.util.Random;

/**
 * This class implements a test scenario.
 * 
 * @author Devin Young and Kevin Pietrow
 */
public class Shoe01 extends charlie.card.Shoe {   
    @Override
    public void init() { 
        super.ran = new Random(1);
        
        super.numDecks = 1;
        
        super.load();
        
        super.shuffle();
    }
}
