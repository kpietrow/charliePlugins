package charlie.bot.client;

import charlie.actor.Courier;
import charlie.card.Card;
import charlie.card.Hid;
import charlie.plugin.IGerty;
import charlie.view.AMoneyManager;
import java.awt.Graphics2D;
import java.util.List;

/**
 * This class implements the behavior of an artificial player.
 * 
 * @author Devin Young and Kevin Pietrow
 */
public class Gerty implements IGerty{
    
    /**
     * Constructor
     */
    public Gerty(){
        
    }
    
    /**
     * Tells bot it's time to make a bet to start a game.
     */
    @Override
    public void go( ){
        
    }
    
    /**
     * Sets the courier actor through which we communicate with the controller.
     * @param courier Courier
     */
    @Override
    public void setCourier(Courier courier){
        
    }
    
    /**
     * Sets the money manager for managing bets.
     * @param moneyManager Money manager
     */
    @Override
    public void setMoneyManager(AMoneyManager moneyManager){
        
    }
    
    /**
     * Updates the bot.
     */
    @Override
    public void update(){
        
    }
    
    /**
     * Renders the bot.
     * @param g Graphics context.
     */
    @Override
    public void render(Graphics2D g){
        
    }
    
    /**
     * Starts game with list of hand ids and shoe size.
     * The number of decks is shoeSize / 52.
     * @param hids Hand ids
     * @param shoeSize Starting shoe size
     */
    @Override
    public void startGame(List<Hid> hids,int shoeSize){
        
    }
    
    /**
     * Ends a game with shoe size.
     * @param shoeSize Shoe size
     */
    @Override
    public void endGame(int shoeSize){
        
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
        
    }
    
    /**
     * Offers player chance to buy insurance.
     */
    @Override
    public void insure(){
        
    }
    
    /**
     * Tells player the hand is broke.
     * @param hid Hand id
     */
    @Override
    public void bust(Hid hid){
        
    }
    
    /**
     * Tells player the hand won.
     * @param hid Hand id
     */    
    @Override
    public void win(Hid hid){
        
    }
    
    /**
     * Tells player the hand won with blackjack.
     * @param hid Hand id
     */     
    @Override
    public void blackjack(Hid hid){
        
    }
    
    /**
     * Tells player the hand won with Charlie.
     * @param hid Hand id
     */     
    @Override
    public void charlie(Hid hid){
        
    }
    
    /**
     * Tells player the hand lost to dealer.
     * @param hid Hand id
     */     
    @Override
    public void lose(Hid hid){
        
    }
    
    /**
     * Tells player the hand pushed.
     * @param hid Hand id
     */     
    @Override
    public void push(Hid hid){
        
    }
    
    /**
     * Tells player the hand pushed.
     */     
    @Override
    public void shuffling(){
        
    }
    
    /**
     * Tells player to start playing hand.
     * @param hid Hand id
     */     
    @Override
    public void play(Hid hid){
        
    }
    
}
