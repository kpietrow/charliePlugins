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
import java.util.HashMap;
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
    protected double oldBet;
    protected Play advice;
    protected BasicStrategy bs;
    protected Random random;
    protected final int DELAY;
    protected int randomPlay;
    protected int ignoreBS;
    protected int dealerSeenCount;
    protected int shoeSize;
    protected double decksRemaining;
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
    protected HashMap<Integer, Double> advantages;
    public final static int X = 400;
    public final static int Y = 200;
    protected Font descFont = new Font("Arial", Font.BOLD, 15);
    protected Font titleFont = new Font("Arial", Font.BOLD, 20);
    protected long startTime;
    protected long endTime;
    protected long elapsedTime;
    protected DecimalFormat formatter;
    
    /**
     * Constructor
     */
    public Gerty(){
        LOG.info("new auto-player generated...");
        formatter = new DecimalFormat("##.##");
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
        advantages = new HashMap();
        
        // Builds a hash map of trueCount to player advantage.
        advantages.put(0, 0.0);
        advantages.put(1, 0.0099);
        advantages.put(2, 0.0145);
        advantages.put(3, 0.02);
        advantages.put(4, 0.022);
        advantages.put(5, 0.03);
        advantages.put(6, 0.0385);
        advantages.put(7, 0.045);
        advantages.put(8, 0.05);
        advantages.put(9, 0.0615);
        advantages.put(10, 0.0615);
        advantages.put(11, 0.0715);
        advantages.put(12, 0.08);
        advantages.put(13, 0.095);
        advantages.put(14, 0.0925);
        advantages.put(15, 0.1);
        advantages.put(16, 0.1085);
        advantages.put(17, 0.1195);
        advantages.put(18, 0.125);
        advantages.put(19, 0.135);
        advantages.put(20, 0.14);
        advantages.put(21, 0.1395);
        advantages.put(22, 0.1415);
        advantages.put(23, 0.155);
        advantages.put(24, 0.16);
        advantages.put(25, 0.17);
        advantages.put(26, 0.18);
        
    }
    
    /**
     * Tells bot it's time to make a bet to start a game.
     */
    @Override
    public void go( ){
        LOG.info("auto-player is asked for bet...");
        dealerSeenCount = 0;
        int adjustedTrueCount;
        
        // f = a / v
        // where f = fraction of bankroll to wager, a = player advantage, v = games variance.
        // An approximation of Kelly's Criterion (because there are many 
        // different bets and payouts, causing odds to shift for each game).
        trueCount = (int) Math.ceil(runningCount / decksRemaining);
        
        // Player's advantage is 0.0% where true count is negative.
        adjustedTrueCount = trueCount;
        if (trueCount < 0)
            adjustedTrueCount = 0;
        
        // Player's advantage is capped at 18% when true count is > 26.
        if (trueCount > 26)
            adjustedTrueCount = 26;
        
        // Gets player's advantage from HashMap and calculates 
        // percent of bankroll to wager.
        double a = advantages.get(adjustedTrueCount);
        double f = a / 1.3225;
        int currentBet = 0;
        
        // Clears old bets when necessary and bets the minimum
        // when f <= 0 or games played == 0.
        if (f <= 0 || gamesPlayed == 0) {
            if (moneyManager.getWager() != Constant.MIN_BET) {
                moneyManager.clearBet();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(Gerty.class.getName()).log(Level.SEVERE, null, ex);
                }
                moneyManager.upBet(Constant.MIN_BET);
            }
            totalInitialBets += Constant.MIN_BET;
            currentBet += Constant.MIN_BET;
        }
        // Clears old bets when necessary and bets 
        // f * bankroll.
        else {
            double wagerAmt = f * moneyManager.getBankroll();
            int chipsToWager = (int) wagerAmt / Constant.MIN_BET;
            
            if (moneyManager.getWager() != wagerAmt) {
                moneyManager.clearBet();
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
                totalInitialBets += chipsToWager * Constant.MIN_BET;
                currentBet += chipsToWager * Constant.MIN_BET;
            }
            
        }
        
        // Maintains maximum bet made across all games.
        if (currentBet > maxBetAmount){
            maxBetAmount = currentBet;
        }
        
        meanBetPerGame = (totalInitialBets * 1.0) / (gamesPlayed + 1.0);
        
        // Places calculated bet and creates a new empty hand.
        myHid = courier.bet(currentBet, 0);
        myHand = new Hand(myHid);
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
        endTime = System.nanoTime();
        elapsedTime = endTime - startTime;
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
        g.drawString("Shoe Size: " + shoeSize, X - 395, Y + 15);
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
        if (hid.getSeat().equals(Seat.YOU) && myHand.size() > 2 && !(myHand.isBroke()) && oldBet == hid.getAmt()){
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
        runningCount = 0;
        
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
            
            // Sets old bet amount in order to determine if
            // auto-player doubled down.
            oldBet = hid.getAmt();
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
                courier.stay(myHid);
            } else if (myHand.getValue() <= 10 || (myHand.getValue() <= 16 && dealerUpCard.value() >= 7 && dealerUpCard.value() <= 11)) {
                courier.hit(myHid);
            } else if (myHand.getValue() == 11 && myHand.size() == 2) {
                courier.dubble(myHid);
            }
        } else if (advice == Play.STAY) {
            courier.stay(myHid);
        } else {
            courier.hit(myHid);
        }
    }
    
}
