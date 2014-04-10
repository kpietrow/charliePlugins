/*
 Copyright (c) 2014 Ron Coleman

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package charlie.sidebet.rule;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.plugin.ISideBetRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the side bet rule for Super 7.
 * @author Ron Coleman
 */
public class SideBetRule implements ISideBetRule {
    private final Logger LOG = LoggerFactory.getLogger(SideBetRule.class);
    
    private final Double PAYOFF_SUPER7 = 3.0;
    private final Double PAYOFF_ROYAL_MATCH = 25.0;
    private final Double PAYOFF_EXACTLY_13 = 1.0;
    private double sevenPayout = 0.0;
    private double royalPayout = 0.0;
    private double exactlyPayout = 0.0;

    /**
     * Apply rule to the hand and return the payout if the rule matches
     * and the negative bet if the rule does not match.
     * @param hand 
     *          Hand to analyze.
     * @return 
     *          the sidebet payout
     */
    @Override
    public double apply(Hand hand) {
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
        if (royalPayout > 0)
            return royalPayout;
        else if(sevenPayout > 0)
            return sevenPayout;
        else if (exactlyPayout > 0)
            return exactlyPayout;
        else
            return -bet;   
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
        return 0.0;
    }
}

