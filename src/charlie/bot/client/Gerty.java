package charlie.bot.client;

import charlie.actor.Courier;
import charlie.advisor.BasicStrategy;
import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Seat;
import charlie.plugin.IGerty;
import charlie.util.Constant;
import charlie.util.Play;
import charlie.view.AMoneyManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the behavior of an artificial player.
 * 
 * @author Devin Young and Kevin Pietrow
 */
public class Gerty implements IGerty{
    private final Logger LOG = LoggerFactory.getLogger(Gerty.class);
    protected Courier courier;
    protected AMoneyManager moneyManager;
    protected Hid myHid;
    protected Hand myHand;
    protected Card dealerUpCard;
    protected Play advice;
    protected BasicStrategy bs;
    protected Random random;
    protected final int DELAY;
    protected int randomPlay;
    protected int ignoreBS;
    protected int dealerSeenCount;
    protected int shoeSize;
    protected double decksRemaining;
    protected int startingShoeSize;
    protected int runningCount;
    protected int trueCount;
    protected int gamesPlayed;
    protected int maxBetAmount;
    protected double meanBetPerGame;
    protected int blackjacks;
    protected int charlies;
    protected int wins;
    protected int breaks;
    protected int loses;
    protected int pushes;
    protected int totalInitialBets;
    public final static int X = 400;
    public final static int Y = 200;
    protected Font descFont = new Font("Arial", Font.BOLD, 15);
    protected Font titleFont = new Font("Arial", Font.BOLD, 20);
    protected long startTime;
    protected long endTime;
    protected long elapsedTime;
    protected DecimalFormat formatter;
    protected boolean beginTimer;
    
    /**
     * Constructor
     */
    public Gerty(){
        LOG.info("new auto-player generated...");
        formatter = new DecimalFormat("#0.00");
        bs = new BasicStrategy();
        random = new Random();
        DELAY = random.nextInt(2501 - 1000) + 1000;
        randomPlay = random.nextInt(4);
        ignoreBS = DELAY % 5;
        decksRemaining = 1;
        runningCount = 0;
        maxBetAmount = 0;
        gamesPlayed = 0;
        blackjacks = 0;
        charlies = 0;
        wins = 0;
        breaks = 0;
        loses = 0;
        pushes = 0;
        totalInitialBets = 0;
        startTime = System.nanoTime();
        endTime = System.nanoTime();
        beginTimer = false;
    }
    
    /**
     * Tells bot it's time to make a bet to start a game.
     */
    @Override
    public void go( ){
        LOG.info("auto-player is asked for bet...");
        advice = null;
        dealerSeenCount = 0;
        
        trueCount = (int) Math.ceil(runningCount / decksRemaining);
        
        // Resets current bet.
        int currentBet = 0;
        
        // Makes initial bet for the first game.
        if (gamesPlayed == 0) {
            beginTimer = true;
            startTime = System.nanoTime();
            moneyManager.upBet(Constant.MIN_BET);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(Gerty.class.getName()).log(Level.SEVERE, null, ex);
            }
            totalInitialBets += Constant.MIN_BET;
            currentBet += Constant.MIN_BET;
        }
        // Clears old bets when necessary and bets 
        // based on trueCount.
        else {
            double wagerAmt = Math.max(1, 1 + trueCount) * Constant.MIN_BET;
            int chipsToWager = (int) wagerAmt / Constant.MIN_BET;
            
            // If new bet and last bet don't match, clear chips and up bet.
            if (moneyManager.getWager() != (chipsToWager * Constant.MIN_BET)) {
                moneyManager.clearBet();
                // Allows sounds to play before adding next chip.
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(Gerty.class.getName()).log(Level.SEVERE, null, ex);
                }

                for (int j = 0; j < chipsToWager; j++) {
                    moneyManager.upBet(Constant.MIN_BET);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        java.util.logging.Logger.getLogger(Gerty.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            totalInitialBets += chipsToWager * Constant.MIN_BET;
            currentBet += chipsToWager * Constant.MIN_BET;
        }
        
        // Maintains maximum bet made across all games.
        if (currentBet > maxBetAmount){
            maxBetAmount = currentBet;
        }
        
        meanBetPerGame = (totalInitialBets * 1.0) / (gamesPlayed + 1.0);
        
        // Places calculated bet and creates a new empty hand.
        myHid = courier.bet(currentBet, 0);
        myHand = new Hand(myHid);
        
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(Gerty.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Sets the courier actor through which we communicate with the controller.
     * @param courier Courier
     */
    @Override
    public void setCourier(Courier courier){
        this.courier = courier;
    }
    
    /**
     * Sets the money manager for managing bets.
     * @param moneyManager Money manager
     */
    @Override
    public void setMoneyManager(AMoneyManager moneyManager){
        this.moneyManager = moneyManager;
    }
    
    /**
     * Updates the bot.
     */
    @Override
    public void update(){
        // Keeps track of total elapsed time for the game.
        if (beginTimer = true) {
            endTime = System.nanoTime();
            elapsedTime = endTime - startTime;
        }
    }
    
    /**
     * Renders the bots game statistics on the table.
     * @param g Graphics context.
     */
    @Override
    public void render(Graphics2D g){
        // Draws "Count Statistics" on the table
        g.setFont(titleFont);
        g.setColor(Color.BLACK);
        g.drawString("Count Statistics", X - 395, Y - 20);
        
        // Draws the running count statistics on the table.
        g.setFont(descFont);
        g.setColor(Color.BLACK);
        g.drawString("------------------------------", X - 395, Y - 12);
        g.drawString("Counting System: Hi-Lo", X - 395, Y);
        g.drawString("Shoe Size: " + formatter.format(decksRemaining), X - 395, Y + 15);
        g.drawString("Running Count: " + runningCount, X - 395, Y + 30);
        g.drawString("True Count: " + trueCount, X - 395, Y + 45);
        g.drawString("Games Played: " + gamesPlayed, X - 395, Y + 60);
        g.drawString("Minutes Played: " + TimeUnit.MINUTES.convert(elapsedTime, TimeUnit.NANOSECONDS), X - 395, Y + 75);
        g.drawString("Max Bet Amount: " + maxBetAmount, X - 395, Y + 90);
        g.drawString("Mean Bet Per Game: " + formatter.format(meanBetPerGame), X - 395, Y + 105);
        g.drawString("Blackjacks: " + blackjacks, X - 395, Y + 120);
        g.drawString("Charlies: " + charlies, X - 395, Y + 135);
        g.drawString("Wins: " + wins, X - 395, Y + 150);
        g.drawString("Loses: " + loses, X - 395, Y + 165);
        g.drawString("Breaks: " + breaks, X - 395, Y + 180);
        g.drawString("Pushes: " + pushes, X - 395, Y + 195);
    }
    
    /**
     * Starts game with list of hand ids and shoe size.
     * The number of decks is shoeSize / 52.
     * @param hids Hand ids
     * @param shoeSize Starting shoe size
     */
    @Override
    public void startGame(List<Hid> hids,int shoeSize){
        LOG.info("auto-player alerted of start game...");
        if (gamesPlayed == 0) {
            startingShoeSize = shoeSize;
        }
        decksRemaining = shoeSize / 52.0;
        this.shoeSize = shoeSize;
    }
    
    /**
     * Ends a game with shoe size.
     * @param shoeSize Shoe size
     */
    @Override
    public void endGame(int shoeSize){
        LOG.info("auto-player alerted of end game...");
        decksRemaining = shoeSize / 52.0;
        this.shoeSize = shoeSize;
        gamesPlayed++;
        
        // Allows game to completely end before next game starts.
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(Gerty.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Pause game in order to take screenshot of 100th game.
        if (gamesPlayed == 100) {
            try {
                Thread.sleep(30000000);
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(Gerty.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Deals a card to player.
     * All players receive all cards which is useful for card counting.
     * @param hid Hand id which might not necessarily belong to player.
     * @param card Card being dealt
     * @param values Hand values, literal and soft
     */
    @Override
    public void deal(Hid hid, Card card, int[] values){
        LOG.info("auto-player alerted of dealt card...");
        
        // Maintains the running shoe size.
        this.shoeSize--;
        
        decksRemaining = shoeSize / 52.0;
        
        // Maintains the running Hi-Lo count.
        if (card.value() >= 2 && card.value() <= 6) {
            runningCount += 1;
        }
        else if (card.value() >= 7 && card.value() <= 9) {
            runningCount += 0;
        }
        else {
            runningCount += -1;
        }
        
        // Maintains the running true count.
        trueCount = (int) Math.ceil(runningCount / decksRemaining);
        
        // Stores dealers up card.
        if (hid.getSeat().equals(Seat.DEALER) && dealerSeenCount == 0){
            this.dealerUpCard = card; 
            dealerSeenCount++;
        }
        
        // Maintains auto-players hand. 
        if (hid.getSeat().equals(Seat.YOU)) {
            myHand.hit(card);
        }
        
        // Determines if it is auto-players turn to make a play.
        if (hid.getSeat().equals(Seat.YOU) && myHand.size() > 2 && !(myHand.isCharlie()) && !(myHand.isBlackjack()) && !(myHand.isBroke()) && advice != Play.DOUBLE_DOWN){
            LOG.info("confirmed auto-players turn...");
            respond();
        }
    }
    
    /**
     * Offers player chance to buy insurance.
     */
    @Override
    public void insure(){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
     * Tells player the hand is broke.
     * @param hid Hand id
     */
    @Override
    public void bust(Hid hid){ 
        LOG.info("auto-player alerted of bust...");
        breaks++;
    }
    
    /**
     * Tells player the hand won.
     * @param hid Hand id
     */    
    @Override
    public void win(Hid hid){
        LOG.info("auto-player alerted of win...");
        wins++;
    }
    
    /**
     * Tells player the hand won with blackjack.
     * @param hid Hand id
     */     
    @Override
    public void blackjack(Hid hid){
        LOG.info("auto-player alerted of blackjack...");
        blackjacks++;
    }
    
    /**
     * Tells player the hand won with Charlie.
     * @param hid Hand id
     */     
    @Override
    public void charlie(Hid hid){
        LOG.info("auto-player alerted of charlie...");
        charlies++;
    }
    
    /**
     * Tells player the hand lost to dealer.
     * @param hid Hand id
     */     
    @Override
    public void lose(Hid hid){
        LOG.info("auto-player alerted of lose...");
        loses++;
    }
    
    /**
     * Tells player the hand pushed.
     * @param hid Hand id
     */     
    @Override
    public void push(Hid hid){
        LOG.info("auto-player alerted of push...");
        pushes++;
    }
    
    /**
     * Tells player the hand pushed.
     */     
    @Override
    public void shuffling(){
        LOG.info("auto-player alerted of shuffling...");
        
        // Reset running count and shoe size.
        runningCount = 0;
        decksRemaining = startingShoeSize / 52.0;
        shoeSize = startingShoeSize;
        
        // Allows shuffling of deck to complete before anf plays are made.
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(Gerty.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Tells player to start playing hand.
     * @param hid Hand id
     */     
    @Override
    public void play(Hid hid){
        LOG.info("auto-player reponding if it is its turn...");
        if (hid.getSeat().equals(Seat.YOU)) {
            LOG.info("confirmed auto-players turn...");
            respond();
        }
    }
    
    /**
     * Responds when it is my turn.
     */
    protected void respond() {
        LOG.info("auto-player reponding to its turn...");
        final Play[] plays = {Play.DOUBLE_DOWN, Play.HIT, Play.SPLIT, Play.STAY};

        // Sets a random delay to make auto-player appear to think.
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException ex) {
            java.util.logging.Logger.getLogger(Gerty.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Auto-player will randomly choose to ignore the basic strategy.
        // Uncomment if-else for random-random play decision making.
        // Comment if-else for strict adherence to basic strategy card.
        /*if (ignoreBS == 0) {
            advice = plays[randomPlay];
        } else {
            advice = bs.advise(myHand, dealerUpCard);
        }*/
        
        // Get appropriate play according to the basic strategy card.
        advice = bs.advise(myHand, dealerUpCard);
        
        // Tells the dealer what play auto-player is making.
        if (advice == Play.DOUBLE_DOWN && myHand.size() == 2) {
            courier.dubble(myHid);
        } else if (advice == Play.SPLIT) {
            if (myHand.getValue() >= 17 || (myHand.getValue() <= 16 && dealerUpCard.value() <= 6)) {
                advice = Play.STAY;
                courier.stay(myHid);
            } else if (myHand.getValue() <= 10 || (myHand.getValue() <= 16 && dealerUpCard.value() >= 7 && dealerUpCard.value() <= 11)) {
                advice = Play.HIT;
                courier.hit(myHid);
            } else if (myHand.getValue() == 11 && myHand.size() == 2) {
                advice = Play.DOUBLE_DOWN;
                courier.dubble(myHid);
            }
        } else if (advice == Play.STAY) {
            advice = Play.STAY;
            courier.stay(myHid);
        } else {
            advice = Play.HIT;
            courier.hit(myHid);
        }
    }
    
}
