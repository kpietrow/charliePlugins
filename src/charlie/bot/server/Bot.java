package charlie.bot.server;

import charlie.actor.RealPlayer;
import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Dealer;
import charlie.dealer.Seat;
import charlie.message.view.from.Arrival;
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
import charlie.plugin.IBot;
import com.googlecode.actorom.Actor;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.googlecode.actorom.Address;
import com.googlecode.actorom.annotation.OnMessage;
import com.googlecode.actorom.remote.ClientTopology;
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
    
    /**
     * Constructor
     * @param dealer
     * @param courierAddress
     */
    public Bot(Dealer dealer, Address courierAddress){
    
        String host = courierAddress.getHost();
        Integer port = courierAddress.getPort();
        LOG.info("courier addr = "+courierAddress);
        
        this.topology = new ClientTopology(host, port, 5, TimeUnit.SECONDS, 3, TimeUnit.SECONDS);

        // Tell courier surrogate's ready
        this.courier = topology.getActor(courierAddress);
        
      
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
        courier.send(new GameStart(hids,shoeSize));
    }

    /**
     * Ends a game with shoe size.
     * @param shoeSize Shoe size
     */
    @Override
    public void endGame(int shoeSize) {
        courier.send(new GameOver(shoeSize));
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
       /* Hand hand = hands.get(hid);
        
        if(hand == null) {
            hand = new Hand(hid);
            
            hands.put(hid, hand);
            
            if(hid.getSeat() == Seat.DEALER)
                this.dealerHand = hand;
        }
            
        hand.hit(card); */
    }
    
    @OnMessage(type = Arrival.class)
    public void onReceive(Arrival arrival) {
        Address courierAddress = arrival.getSource();
        LOG.info("bot test "+courierAddress);
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
        courier.send(new Bust(hid));
    }

    /**
     * Tells player the hand won.
     * @param hid Hand id
     */
    @Override
    public void win(Hid hid) {
        courier.send(new Win(hid));
    }

    /**
     * Tells player the hand won with blackjack.
     * @param hid Hand id
     */ 
    @Override
    public void blackjack(Hid hid) {
        courier.send(new Blackjack(hid) );
    }

    /**
     * Tells player the hand won with Charlie.
     * @param hid Hand id
     */ 
    @Override
    public void charlie(Hid hid) {
        courier.send(new Charlie(hid) );
    }

    /**
     * Tells player the hand lost to dealer.
     * @param hid Hand id
     */ 
    @Override
    public void lose(Hid hid) {
        courier.send(new Loose(hid));
    }

    /**
     * Tells player the hand pushed.
     * @param hid Hand id
     */ 
    @Override
    public void push(Hid hid) {
        courier.send(new Push(hid));
    }

    /**
     * Tells player the hand pushed.
     */ 
    @Override
    public void shuffling() {
        courier.send(new Shuffle());
    }

    /**
     * Tells player to start playing hand.
     * @param hid Hand id
     */ 
    @Override
    public void play(Hid hid) {
        courier.send(new Play(hid));
    }
}
