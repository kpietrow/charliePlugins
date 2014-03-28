package charlie.bot.server;

import charlie.advisor.BasicStrategy;
import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Dealer;
import charlie.plugin.IPlayer;
import charlie.util.Play;
import java.util.Random;
import java.util.logging.Level;

/**
 *
 * @author Devin Young and Kevin Pietrow
 */
public class Responder implements Runnable {

    private final IPlayer bot;
    private final Hand myHand;
    private final Dealer dealer;
    private final Card dealerUpCard;
    private final BasicStrategy bs;
    private final Random random;
    private final Hid botHid;
    private final int DELAY;
    private final int randomPlay;
    private final int ignoreBS;

    public Responder(IPlayer bot, Hand myHand, Dealer dealer, Card dealerUpCard) {
        this.bot = bot;
        this.myHand = myHand;
        this.dealer = dealer;
        this.dealerUpCard = dealerUpCard;
        bs = new BasicStrategy();
        random = new Random();
        botHid = myHand.getHid();
        DELAY = random.nextInt(2501 - 1000) + 1000;
        randomPlay = random.nextInt(4);
        ignoreBS = DELAY % 5;
    }

    @Override
    public void run() {
        final Play[] plays = {Play.DOUBLE_DOWN, Play.HIT, Play.SPLIT, Play.STAY};
        Play advice;

        // Sets a random delay to make b9 appear to think.
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(B9.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // B9 will randomly choose to ignore the basic strategy.
        if (ignoreBS == 0) {
            advice = plays[randomPlay];
        } else {
            advice = bs.advise(myHand, dealerUpCard);
        }

        // Tells the dealer what play b9 is making.
        if (advice == Play.DOUBLE_DOWN && myHand.size() == 2) {
            dealer.doubleDown(bot, botHid);
        } else if (advice == Play.SPLIT) {
            if (myHand.getValue() >= 17 || (myHand.getValue() <= 16 && dealerUpCard.value() <= 6)) {
                dealer.stay(bot, botHid);
            } else if (myHand.getValue() <= 10 || (myHand.getValue() <= 16 && dealerUpCard.value() >= 7 && dealerUpCard.value() <= 11)) {
                dealer.hit(bot, botHid);
            } else if (myHand.getValue() == 11 && myHand.size() == 2) {
                dealer.doubleDown(bot, botHid);
            }
        } else if (advice == Play.STAY) {
            dealer.stay(bot, botHid);
        } else {
            dealer.hit(bot, botHid);
        }

    }
}
