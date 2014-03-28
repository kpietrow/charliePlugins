package charlie.bot.server;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Dealer;
import charlie.dealer.Seat;
import charlie.plugin.IBot;
import charlie.util.Constant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a class for bot players running in the server.
 * @author Devin Young and Kevin Pietrow
 */
public class B9 implements IBot{
    private final Logger LOG = LoggerFactory.getLogger(B9.class);
    protected Hid hid;
    protected Hand myHand;
    protected Dealer dealer;
    protected Seat seat;
    protected Hid dealerHid;
    protected Card dealerUpCard;
    
    /**
     * Constructor
     */
    public B9(){
        LOG.info("new instance of b9 created..."); 
    }
    
    /**
     * Gets the bots hand.
     * @return Hand
     */
    @Override
    public Hand getHand() {
        return myHand;
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
        hid = new Hid(this.seat, Constant.BOT_MIN_BET, 0);
        myHand = new Hand(hid);
    }

    /**
     * Starts game with list of hand ids and shoe size.
     * The number of decks is shoeSize / 52.
     * @param hids Hand ids
     * @param shoeSize Starting shoe size
     */
    @Override
    public void startGame(List<Hid> hids, int shoeSize) {
        LOG.info("b9 alerted of start game...");
    }

    /**
     * Ends a game with shoe size.
     * @param shoeSize Shoe size
     */
    @Override
    public void endGame(int shoeSize) {
        LOG.info("b9 alerted of end game...");
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
        LOG.info("b9 alerted of dealt card...");
        
        if (this.dealerHid == null && hid.getSeat() == Seat.DEALER){
            this.dealerHid = hid;
            this.dealerUpCard = card; 
        }
        if (myHand.getHid().equals(hid) && myHand.size() > 2 && !(myHand.isBroke())){
            respond();
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
        LOG.info("b9 alerted of bust...");
    }

    /**
     * Tells player the hand won.
     * @param hid Hand id
     */
    @Override
    public void win(Hid hid) {
        LOG.info("b9 alerted of win...");
    }

    /**
     * Tells player the hand won with blackjack.
     * @param hid Hand id
     */ 
    @Override
    public void blackjack(Hid hid) {
        LOG.info("b9 alerted of blackjack...");
    }

    /**
     * Tells player the hand won with Charlie.
     * @param hid Hand id
     */ 
    @Override
    public void charlie(Hid hid) {
        LOG.info("b9 alerted of charlie...");
    }

    /**
     * Tells player the hand lost to dealer.
     * @param hid Hand id
     */ 
    @Override
    public void lose(Hid hid) {
        LOG.info("b9 alerted of lose...");
    }

    /**
     * Tells player the hand pushed.
     * @param hid Hand id
     */ 
    @Override
    public void push(Hid hid) {
        LOG.info("b9 alerted of push...");
    }

    /**
     * Tells player the hand pushed.
     */ 
    @Override
    public void shuffling() {
        LOG.info("b9 alerted of shuffling...");
    }

    /**
     * Responds when it is my turn.
     */
    protected void respond() {
        LOG.info("b9 reponding to its turn...");
        new Thread(new Responder(this,myHand,dealer,dealerUpCard)).start();        
    }
    
    /**
     * Tells player to start playing hand.
     * @param hid Hand id
     */ 
    @Override
    public void play(Hid hid) {
        LOG.info("b9 reponding if it is its turn...");
        if (hid.equals(myHand.getHid())) {
            respond();
        }
    }
}
