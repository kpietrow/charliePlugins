package charlie.sidebet.rule;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.plugin.ISideBetRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the side bet rule for Super 7,
 * Royal Match and Exactly 13.
 * 
 * @author Devin Young and Kevin Pietrow
 */
public class SideBetRule implements ISideBetRule {
    private final Logger LOG = LoggerFactory.getLogger(SideBetRule.class);
    
    private final Double PAYOFF_SUPER7 = 3.0;
    private final Double PAYOFF_ROYAL_MATCH = 25.0;
    private final Double PAYOFF_EXACTLY_13 = 1.0;
    
    private double sevenPayout;
    private double royalPayout;
    private double exactlyPayout;

    /**
     * Apply rule to the hand and return the payout if the rule matches
     * and the negative bet if the rule does not match.
     * 
     * @param hand 
     *          Hand to analyze.
     * @return 
     *          the side bet payout
     */
    @Override
    public double apply(Hand hand) {
        //reset payout amounts between games
        sevenPayout = 0.0;
        royalPayout = 0.0;
        exactlyPayout = 0.0;
        
        Double bet = hand.getHid().getSideAmt();
        LOG.info("side bet amount = "+bet);
        
        if(bet == 0)
            return 0.0;
        
        LOG.info("side bet rule applying hand = "+hand);
        
        //checks for Super 7 side bet rule match
        if (hand.getCard(0).getRank() == 7){
            LOG.info("checking for Super 7...");
            sevenPayout = superSevenCheck(hand, bet);
        }
        
        //checks for Royal Match side bet rule match
        if (hand.getCard(0).getRank() == Card.KING && hand.getCard(1).getRank() == Card.QUEEN ||
            hand.getCard(0).getRank() == Card.QUEEN && hand.getCard(1).getRank() == Card.KING){
            LOG.info("checking for Royal Match...");
            royalPayout = royalMatchCheck(hand, bet);
        }
        
        //checks for Exactly 13 side bet rule match
        if (hand.getValue() == 13){
            LOG.info("checking for Exactly 13...");
            exactlyPayout = exactlyThirteenCheck(hand, bet);
        }
        
        //return highest payout or amount lost
        if (royalPayout > 0) {
            LOG.info("entering royal payout...");
            return royalPayout;
        }
        else if(sevenPayout > 0){
            LOG.info("entering seven payout...");
            return sevenPayout;
        }
        else if (exactlyPayout > 0) {
            LOG.info("entering exactly payout...");
            return exactlyPayout;
        }
        else {
            LOG.info("losing side bet returned...");
            return -bet; 
        }
    }
    
    /**
     * Checks if Super 7 side bet applies.
     * 
     * @param hand
     *          Hand to analyze
     * @param bet
     *          bet made by player
     * @return 
     *          the side bet payout
     */
    private double superSevenCheck(Hand hand, Double bet){
        Card card = hand.getCard(0);
 
        if(card.getRank() == 7) {
            LOG.info("side bet SUPER 7 matches");
            return bet * PAYOFF_SUPER7;
        }
        
        LOG.info("side bet SUPER 7 no match");
        
        return 0.0;
    }
    
    /**
     * Checks if Royal Match side bet applies.
     * 
     * @param hand
     *          Hand to analyze
     * @param bet
     *          bet made by player
     * @return 
     *          the side bet payout
     */
    private double royalMatchCheck(Hand hand, Double bet){
        Card card1 = hand.getCard(0);
        Card card2 = hand.getCard(1);
        
        // Tests if there is a royal match
        if (card1.getSuit() == card2.getSuit()) {
            LOG.info("side bet Royal Match matches");
            return bet * PAYOFF_ROYAL_MATCH;
        }
        
        LOG.info("side bet Royal Match no match");
        
        return 0.0;
    }
    
    /**
     * Checks if Exactly 13 side bet applies.
     * 
     * @param hand
     *          Hand to analyze
     * @param bet
     *          bet made by player
     * @return 
     *          the side bet payout
     */
    private double exactlyThirteenCheck(Hand hand, Double bet){
        // Tests if hand value equals 13
        if (hand.getValue() == 13) {
            LOG.info("side bet Exactly 13 matches");
            return bet * PAYOFF_EXACTLY_13;
        }
        
        LOG.info("side bet Exactly 13 no match");
        
        return 0.0;
    }
}

