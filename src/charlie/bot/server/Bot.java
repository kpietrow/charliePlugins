package charlie.bot.server;

import charlie.actor.RealPlayer;
import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Dealer;
import charlie.dealer.Seat;
import charlie.message.view.from.Arrival;
import charlie.message.view.from.DoubleDown;
import charlie.message.view.from.Hit;
import charlie.message.view.from.Request;
import charlie.message.view.from.Stay;
import charlie.message.view.to.Blackjack;
import charlie.message.view.to.Bust;
import charlie.message.view.to.Charlie;
import charlie.message.view.to.Deal;
import charlie.message.view.to.GameOver;
import charlie.message.view.to.GameStart;
import charlie.message.view.to.Loose;
import charlie.message.view.to.Play;
import charlie.message.view.to.Push;
import charlie.message.view.to.Shuffle;
import charlie.message.view.to.Win;
import charlie.plugin.IAdvisor;
import charlie.plugin.IBot;
import charlie.plugin.IPlayer;
import charlie.server.Ticket;
import com.googlecode.actorom.Actor;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.googlecode.actorom.Address;
import com.googlecode.actorom.annotation.OnMessage;
import com.googlecode.actorom.remote.ClientTopology;
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
    
    /**
     * Constructor
     */
    public Bot(){
        LOG.info("IN BOT..."); 
        
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
        //courier.send(new GameStart(hids,shoeSize));
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
        if (playing.getHid() == hid && playing.size() > 2 && !(playing.isBroke())){
            System.out.println("PLAY SIZE > 2");
            play(hid);
        }
       /* Hand hand = hands.get(hid);
        
        if(hand == null) {
            hand = new Hand(hid);
            
            hands.put(hid, hand);
            
            if(hid.getSeat() == Seat.DEALER)
                this.dealerHand = hand;
        }
            
        hand.hit(card); */
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
     * Tells player to start playing hand.
     * @param hid Hand id
     */ 
    @Override
    public void play(Hid hid) {
        final IPlayer b = this;
        final Hid h = hid;
        Runnable thread = new Runnable() {
            @Override
            public void run() {
                LOG.info("started bot worker thread...");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (h == playing.getHid())
                    dealer.hit(b, h);

        //charlie.util.Play advice = advisor.advise(playing,dealerHand.getCard(1));

        //if(advice == charlie.util.Play.NONE)
           // return true;
            }  
        };

        new Thread(thread).start();
        //courier.send(new Play(hid));
    }
}
