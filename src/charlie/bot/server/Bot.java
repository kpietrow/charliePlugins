package charlie.bot.server;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Dealer;
import charlie.dealer.Seat;
import charlie.plugin.IBot;
import java.util.List;

/**
 * This is a class for bot players running in the server.
 * @author Devin Young and Kevin Pietrow
 */
public class Bot implements IBot{
    protected Hand playing;
    
    /**
     * Constructor
     */
    public Bot(){
        Hid hid = new Hid(Seat.NONE, 0, 0);
        playing = new Hand(hid);
    }
    /**
     * Gets the bots hand.
     * @return Hand
     */
    @Override
    public Hand getHand() {
        return null;
    }

    /**
     * Sets the dealer for the bot.
     * @param dealer Dealer
     */
    @Override
    public void setDealer(Dealer dealer){
        
    }
    
    /**
     * Sits the bot in seat.
     * @param seat Seat
     */
    @Override
    public void sit(Seat seat){
        
    }

    /**
     * Starts game with list of hand ids and shoe size.
     * The number of decks is shoeSize / 52.
     * @param hids Hand ids
     * @param shoeSize Starting shoe size
     */
    @Override
    public void startGame(List<Hid> hids, int shoeSize) {
        
    }

    /**
     * Ends a game with shoe size.
     * @param shoeSize Shoe size
     */
    @Override
    public void endGame(int shoeSize) {
        
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
        
    }

    /**
     * Offers player chance to buy insurance.
     */
    @Override
    public void insure() {
        
    }

    /**
     * Tells player the hand is broke.
     * @param hid Hand id
     */
    @Override
    public void bust(Hid hid) {
        
    }

    /**
     * Tells player the hand won.
     * @param hid Hand id
     */
    @Override
    public void win(Hid hid) {
        
    }

    /**
     * Tells player the hand won with blackjack.
     * @param hid Hand id
     */ 
    @Override
    public void blackjack(Hid hid) {
        
    }

    /**
     * Tells player the hand won with Charlie.
     * @param hid Hand id
     */ 
    @Override
    public void charlie(Hid hid) {
        
    }

    /**
     * Tells player the hand lost to dealer.
     * @param hid Hand id
     */ 
    @Override
    public void lose(Hid hid) {
        
    }

    /**
     * Tells player the hand pushed.
     * @param hid Hand id
     */ 
    @Override
    public void push(Hid hid) {
        
    }

    /**
     * Tells player the hand pushed.
     */ 
    @Override
    public void shuffling() {
        
    }

    /**
     * Tells player to start playing hand.
     * @param hid Hand id
     */ 
    @Override
    public void play(Hid hid) {
        
    }
}
