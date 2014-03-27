package charlie.bot.server;

import charlie.advisor.BasicStrategy;
import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Dealer;
import charlie.dealer.Seat;
import charlie.message.view.from.Bet;
import charlie.message.view.from.DoubleDown;
import charlie.message.view.from.Hit;
import charlie.message.view.from.Request;
import charlie.message.view.from.Stay;
import charlie.plugin.IAdvisor;
import charlie.plugin.IBot;
import charlie.plugin.IPlayer;
import charlie.util.Play;
import com.googlecode.actorom.Actor;
import com.googlecode.actorom.Address;
import com.googlecode.actorom.annotation.OnMessage;
import java.util.List;
import com.googlecode.actorom.remote.ClientTopology;
import java.util.Random;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a class for bot players running in the server.
 * @author Devin Young and Kevin Pietrow
 */
public class Bot implements IBot{
    private final Logger LOG = LoggerFactory.getLogger(Bot.class);
    ClientTopology topology;
    protected Actor courier; 
    protected Hid hid;
    protected Hand playing;
    protected Dealer dealer;
    protected Seat seat;
    protected IAdvisor advisor;
    protected Hid dealerHid;
    protected Card dealerUpCard;
    protected BasicStrategy bs;
    private Address myAddress;
    
    /**
     * Constructor
     */
    public Bot(){
        LOG.info("IN BOT..."); 
        bs = new BasicStrategy();
    }
    
    /**
     * Receives a bet from the courier.
     * @param bet Bet
     */
    @OnMessage(type = Bet.class)
    public void onReceive(Bet bet) {     
        LOG.info("player actor received bet = "+bet.getHid().getAmt());
    }
    
    /**
     * Receives a request from the courier.
     * @param request Request
     */
    @OnMessage(type = Request.class)
    public void onReceive(Request request) {
        LOG.info("received request = "+request);
        Hid hand = request.getHid();
        
            LOG.error("received unknown request: "+request+" for hand = "+hand);
    }
    /**
     * Sets my address since courier doesn't know where it is.
     * @param mine My address
     */
    public void setMyAddress(Address mine) {
        this.myAddress = mine;
        System.out.println("MINE: " + mine);
    }
    
    /**
     * Gets the bots hand.
     * @return Hand
     */
    @Override
    public Hand getHand() {
        return playing;
    }

    /**
     * Sets the dealer for the bot.
     * @param dealer Dealer
     */
    @Override
    public void setDealer(Dealer dealer){
        this.dealer = dealer;
    }
    
    /**
     * Sits the bot in seat.
     * @param seat Seat
     */
    @Override
    public void sit(Seat seat){
        this.seat = seat;
        hid = new Hid(this.seat, 0, 0);
        playing = new Hand(hid);
    }

    /**
     * Starts game with list of hand ids and shoe size.
     * The number of decks is shoeSize / 52.
     * @param hids Hand ids
     * @param shoeSize Starting shoe size
     */
    @Override
    public void startGame(List<Hid> hids, int shoeSize) {
        LOG.info("IN START GAME...");
    }

    /**
     * Ends a game with shoe size.
     * @param shoeSize Shoe size
     */
    @Override
    public void endGame(int shoeSize) {
        LOG.info("IN END GAME...");
    }

    /**
     * Deals a card to player.
     * All players receive all cards which is useful for card counting.
     * @param hid Hand id which might not necessarily belong to player.
     * @param card Card being dealt
     * @param values Hand values, literal and soft
     */
    @Override
    public void deal(Hid hid, Card card, int[] values) {
        LOG.info("IN BOT DEAL...");
        
        if (this.dealerHid == null && hid.getSeat() == Seat.DEALER){
            this.dealerHid = hid;
            this.dealerUpCard = card; 
        }
        if (playing.getHid().equals(hid) && playing.size() > 2 && !(playing.isBroke())){
            play(hid);
        }
    }

    /**
     * Offers player chance to buy insurance.
     */
    @Override
    public void insure() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Tells player the hand is broke.
     * @param hid Hand id
     */
    @Override
    public void bust(Hid hid) {
        LOG.info("busted...");
    }

    /**
     * Tells player the hand won.
     * @param hid Hand id
     */
    @Override
    public void win(Hid hid) {
        LOG.info("IN WIN...");
    }

    /**
     * Tells player the hand won with blackjack.
     * @param hid Hand id
     */ 
    @Override
    public void blackjack(Hid hid) {
        LOG.info("IN BLACKJACK...");
    }

    /**
     * Tells player the hand won with Charlie.
     * @param hid Hand id
     */ 
    @Override
    public void charlie(Hid hid) {
        LOG.info("IN CHARLIE...");
    }

    /**
     * Tells player the hand lost to dealer.
     * @param hid Hand id
     */ 
    @Override
    public void lose(Hid hid) {
        LOG.info("IN LOSE...");
    }

    /**
     * Tells player the hand pushed.
     * @param hid Hand id
     */ 
    @Override
    public void push(Hid hid) {
        LOG.info("IN PUSH...");
    }

    /**
     * Tells player the hand pushed.
     */ 
    @Override
    public void shuffling() {
        LOG.info("IN SHUFFLING...");
    }

    /**
     * Responds when it is my turn.
     */
    protected void respond() {
        LOG.info("IN RESPOND...");
        new Thread(new Responder(this,playing,dealer,dealerUpCard)).start();        
    }
    
    /**
     * Tells player to start playing hand.
     * @param hid Hand id
     */ 
    @Override
    public void play(Hid hid) {
        LOG.info("IN PLAY...");
        if (hid == playing.getHid()) {
            respond();
        }
       /* Random random = new Random();
        final IPlayer bot = this;
        final Hid botHid = hid;
        final int DELAY = random.nextInt(3001 - 1000) + 1000;
        final int randomPlay = random.nextInt(4);
        final int ignoreBS = DELAY % 5;
        final Play[] plays = {Play.DOUBLE_DOWN, Play.HIT, Play.SPLIT, Play.STAY};

        Runnable thread = new Runnable() {
            @Override
            public void run() {
                Play advice;
                
                try {
                    Thread.sleep(DELAY);
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (botHid == playing.getHid()) {
                    if (ignoreBS == 0) {
                        advice = plays[randomPlay];
                    } 
                    else {
                        advice = bs.advise(playing, dealerUpCard);
                    }
                    
                    if (advice == Play.DOUBLE_DOWN && playing.size() == 2) {
                        dealer.doubleDown(bot, botHid);
                    } 
                    else if (advice == Play.SPLIT) {
                        dealer.hit(bot, botHid);
                    } 
                    else if (advice == Play.STAY) {
                        dealer.stay(bot, botHid);
                    } 
                    else {
                        dealer.hit(bot, botHid);
                    }
                }
            }

        };

        new Thread(thread).start(); */
    }
}
