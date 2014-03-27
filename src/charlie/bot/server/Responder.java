/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

    public Responder(IPlayer bot, Hand myHand, Dealer dealer, Card dealerUpCard) {
        this.bot = bot;
        this.myHand = myHand;
        this.dealer = dealer;
        this.dealerUpCard = dealerUpCard;
        bs = new BasicStrategy();
    }

    public void run() {
        Random random = new Random();
        final Hid botHid = myHand.getHid();
        final int DELAY = random.nextInt(3001 - 1000) + 1000;
        final int randomPlay = random.nextInt(4);
        final int ignoreBS = DELAY % 5;
        final Play[] plays = {Play.DOUBLE_DOWN, Play.HIT, Play.SPLIT, Play.STAY};

        Play advice;

        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (botHid == myHand.getHid()) {
            if (ignoreBS == 0) {
                advice = plays[randomPlay];
            } else {
                advice = bs.advise(myHand, dealerUpCard);
            }

            if (advice == Play.DOUBLE_DOWN && myHand.size() == 2) {
                dealer.doubleDown(bot, botHid);
            } else if (advice == Play.SPLIT) {
                dealer.hit(bot, botHid);
            } else if (advice == Play.STAY) {
                dealer.stay(bot, botHid);
            } else {
                dealer.hit(bot, botHid);
            }
        }

    }
}
