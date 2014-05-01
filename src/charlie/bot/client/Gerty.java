package charlie.bot.client;

import charlie.actor.Courier;
import charlie.card.Card;
import charlie.card.Hand;
import charlie.card.Hid;
import charlie.dealer.Seat;
import charlie.plugin.IGerty;
import charlie.util.Constant;
import charlie.view.AMoneyManager;
import java.awt.Graphics2D;
import java.util.List;
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
    
    /**
     * Constructor
     */
    public Gerty(){
        LOG.info("new auto-player generated...");
    }
    
    /**
     * Tells bot it's time to make a bet to start a game.
     */
    @Override
    public void go( ){
        LOG.info("auto-player is asked for bet...");
        
        //clears any old bets and makes a new one (for now...)
        moneyManager.clearBet();
        moneyManager.upBet(Constant.MIN_BET);
        
        //sends bet to courier and gets auto-players hand id
        myHid = courier.bet(Constant.MIN_BET, 0);
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
        //LOG.info("auto-player updated...");
    }
    
    /**
     * Renders the bot.
     * @param g Graphics context.
     */
    @Override
    public void render(Graphics2D g){
        //LOG.info("rendering auto-player...");
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
    }
    
    /**
     * Ends a game with shoe size.
     * @param shoeSize Shoe size
     */
    @Override
    public void endGame(int shoeSize){
        LOG.info("auto-player alerted of end game...");
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
       
        /*if (this.dealerHid == null && hid.getSeat() == Seat.DEALER){
            this.dealerHid = hid;
            this.dealerUpCard = card; 
        }
        if (myHand.getHid() == hid && myHand.size() > 2 && !(myHand.isBroke()) && oldBet == this.hid.getAmt()){
            respond();
        }*/
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
    }
    
    /**
     * Tells player the hand won.
     * @param hid Hand id
     */    
    @Override
    public void win(Hid hid){
        LOG.info("auto-player alerted of win...");
    }
    
    /**
     * Tells player the hand won with blackjack.
     * @param hid Hand id
     */     
    @Override
    public void blackjack(Hid hid){
        LOG.info("auto-player alerted of blackjack...");
    }
    
    /**
     * Tells player the hand won with Charlie.
     * @param hid Hand id
     */     
    @Override
    public void charlie(Hid hid){
        LOG.info("auto-player alerted of charlie...");
    }
    
    /**
     * Tells player the hand lost to dealer.
     * @param hid Hand id
     */     
    @Override
    public void lose(Hid hid){
        LOG.info("auto-player alerted of lose...");
    }
    
    /**
     * Tells player the hand pushed.
     * @param hid Hand id
     */     
    @Override
    public void push(Hid hid){
        LOG.info("auto-player alerted of push...");
    }
    
    /**
     * Tells player the hand pushed.
     */     
    @Override
    public void shuffling(){
        LOG.info("auto-player alerted of shuffling...");
    }
    
    /**
     * Tells player to start playing hand.
     * @param hid Hand id
     */     
    @Override
    public void play(Hid hid){
        LOG.info("auto-player reponding if it is its turn...");
        /*if (myHand.getHid() == hid) {
            oldBet = this.hid.getAmt();
            respond();
        }*/
    }
    
}
