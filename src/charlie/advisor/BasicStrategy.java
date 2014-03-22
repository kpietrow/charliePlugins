/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package charlie.advisor;

import charlie.card.Card;
import charlie.card.Hand;
import charlie.plugin.IAdvisor;
import charlie.util.Play;
import java.util.HashMap;
import java.util.Map;

/**
 * The BasicStrategy class implements the full 468 cell
 * Basic Strategy card. The rules are implemented so that
 * they can be re-used.
 * 
 * @author Devin Young And Kevin Pietrow
 */
public class BasicStrategy implements IAdvisor {
    protected Map<String, Play> stratCard1;
    protected Map<String, Play> stratCard2;
    protected Map<String, Play> stratCard3;
    
    /**
     * Instantiates stratCard1, 
     * stratCard2 and stratCard3 
     * to hold the basic strategy card.
     */
    public BasicStrategy(){
           stratCard1 = new HashMap<>();
           stratCard2 = new HashMap<>();
           stratCard3 = new HashMap<>();
           buildStratCard();
    }
    /**
     * The advise method takes as parameters the human 
     * player's hand and the dealers up card and returns
     * NONE, HIT, STAY, DOUBLE_DOWN, or SPLIT
     * 
     * @param myHand
     *          the human player's hand
     * @param upCard
     *          the dealer's up-card
     * @return Play
     *          an enumerated type in the charlie.util.package
     */
    @Override
    public Play advise(Hand myHand,Card upCard){
       return getPlay(myHand, upCard);
    }
    
    /**
     * The getPlay method takes as parameters the human 
     * player's hand and the dealers up card and returns
     * NONE, HIT, STAY, DOUBLE_DOWN, or SPLIT
     * 
     * @param myHand
     *          the human player's hand
     * @param upCard
     *          the dealer's up-card
     * @return Play
     *          an enumerated type in the charlie.util.package
     */
    public Play getPlay(Hand myHand,Card upCard){
        String hashKey = Integer.toString(myHand.getValue()) + "." +
                         Integer.toString(upCard.value());
        
        if (myHand.size() == 2 && myHand.getCard(0).isAce() || myHand.getCard(1).isAce())
            return stratCard2.get(hashKey);
        else if (myHand.isPair())
            return stratCard3.get(hashKey);
        else
            return stratCard1.get(hashKey);
    }
    
    /**
     * Helper method to build the basic strategy card.
     */
    private void buildStratCard() {
        //key: playersHandValue.upCard
        //value: play to make
        for (int i = 2; i < 12; i++)
            for (int j = 2; j < 12; j++)
                for (int k = 2; k < 12; k++){
                    int pHandValue = i + j;
                    String totalHandValue = Integer.toString(pHandValue);
                    String upCard;
                    if (k == 11)
                        upCard = Integer.toString(k - 10);
                    else 
                        upCard = Integer.toString(k);
                    String hashKey = totalHandValue + "." + upCard;                        
                    
                    switch (pHandValue){
                        case 4:
                            if (k >= 2 && k <= 7)
                                stratCard3.put(hashKey, Play.SPLIT);
                            else
                                stratCard3.put(hashKey, Play.HIT);
                            break;
                        case 5:
                            stratCard1.put(hashKey, Play.HIT);
                            break;
                        case 6:
                            if ((i == 3 && j == 3) && (k >= 2 && k <= 7))
                                stratCard3.put(hashKey, Play.SPLIT);
                            else if ((i == 3 && j == 3) && (k < 2 || k > 7))
                                stratCard3.put(hashKey, Play.HIT);
                            else
                                stratCard1.put(hashKey, Play.HIT);
                            break;
                        case 7:
                            stratCard1.put(hashKey, Play.HIT);
                            break;
                        case 8:
                            if ((i == 4 && j == 4) && (k >= 5 && k <= 6))
                                stratCard3.put(hashKey, Play.SPLIT);
                            else if ((i == 4 && j == 4) && (k < 5 || k > 6))
                                stratCard3.put(hashKey, Play.HIT);
                            else
                                stratCard1.put(hashKey, Play.HIT);
                            break;
                        case 9:
                            if (k >= 3 && k <= 6)
                                stratCard1.put(hashKey, Play.DOUBLE_DOWN);
                            else
                                stratCard1.put(hashKey, Play.HIT);
                            break;
                        case 10:
                            if (k >= 2 && k <= 9){
                                stratCard1.put(hashKey, Play.DOUBLE_DOWN);
                                stratCard3.put(hashKey, Play.DOUBLE_DOWN);
                            }
                            else{
                                stratCard1.put(hashKey, Play.HIT);
                                stratCard3.put(hashKey, Play.HIT);
                            }
                            break;
                        case 11:
                            if (k >= 2 && k <= 10)
                                stratCard1.put(hashKey, Play.DOUBLE_DOWN);
                            else 
                                stratCard1.put(hashKey, Play.HIT);
                            break;
                        case 12:
                            if ((i == 6 && j == 6) && (k >= 2 && k <= 6))
                                stratCard3.put(hashKey, Play.SPLIT);
                            else if ((i == 6 && j == 6) && (k < 2 || k > 6))
                                stratCard3.put(hashKey, Play.HIT);
                            else if (k >= 4 && k <= 6)
                                stratCard1.put(hashKey, Play.STAY);
                            else
                                stratCard1.put(hashKey, Play.HIT);
                            break;
                        case 13:
                            if ((i == 11 && j == 2) && (k >= 5 && k <= 6))
                                stratCard2.put(hashKey, Play.DOUBLE_DOWN);
                            else if ((i == 11 && j == 2) && (k < 5 || k > 6))
                                stratCard2.put(hashKey, Play.HIT);
                            else if (k >= 2 && k <= 6)
                                stratCard1.put(hashKey, Play.STAY);
                            else 
                                stratCard1.put(hashKey, Play.HIT);
                            break;
                        case 14:
                            if ((i == 7 && j == 7) && (k >= 2 && k <= 7))
                                stratCard3.put(hashKey, Play.SPLIT);
                            else if ((i == 7 && j == 7) && (k < 2 || k > 7))
                                stratCard3.put(hashKey, Play.HIT);
                            else if ((i == 11 && j == 3) && (k >= 5 && k <= 6))
                                stratCard2.put(hashKey, Play.DOUBLE_DOWN);
                            else if ((i == 11 && j == 3) && (k < 5 || k > 6))
                                stratCard2.put(hashKey, Play.HIT);
                            else if (k >= 2 && k <= 6)
                                stratCard1.put(hashKey, Play.STAY);
                            else 
                                stratCard1.put(hashKey, Play.HIT);
                            break;
                        case 15:
                            if ((i == 11 && j == 4) && (k >= 4 && k <= 6))
                                stratCard2.put(hashKey, Play.DOUBLE_DOWN);
                            else if ((i == 11 && j == 4) && (k < 4 || k > 6))
                                stratCard2.put(hashKey, Play.HIT);
                            else if (k >= 2 && k <= 6)
                                stratCard1.put(hashKey, Play.STAY);
                            else {
                                stratCard1.put(hashKey, Play.HIT);
                                
                            }
                            break;
                        case 16:
                            if (i == 8 && j == 8)
                                stratCard3.put(hashKey, Play.SPLIT);
                            else if ((i == 11 && j == 5) && (k >= 4 && k <= 6))
                                stratCard2.put(hashKey, Play.DOUBLE_DOWN);
                            else if ((i == 11 && j == 5) && (k < 4 || k > 6))
                                stratCard2.put(hashKey, Play.HIT);
                            else if (k >= 2 && k <= 6)
                                stratCard1.put(hashKey, Play.STAY);
                            else {
                                stratCard1.put(hashKey, Play.HIT);
                                
                            }
                            break;
                        case 17:
                            if ((i == 11 && j == 6) && (k >= 3 && k <= 6))
                                stratCard2.put(hashKey, Play.DOUBLE_DOWN);
                            else if ((i == 11 && j == 6) && (k < 3 || k > 6))
                                stratCard2.put(hashKey, Play.HIT);
                            else{
                                stratCard1.put(hashKey, Play.STAY);
                            }
                            break;
                        case 18:
                            if ((i == 9 && j == 9) && ((k >= 2 && k <= 6) || (k == 8 || k == 9)))
                                stratCard3.put(hashKey, Play.SPLIT);
                            else if ((i == 9 && j == 9) && (k < 2 || k > 6))
                                stratCard3.put(hashKey, Play.STAY);
                            else if ((i == 11 && j == 7) && (k == 9 || k == 10 || k == 11))
                                stratCard2.put(hashKey, Play.HIT);
                            else if ((i == 11 && j == 7) && (k >= 3 && k <= 6))
                                stratCard2.put(hashKey, Play.DOUBLE_DOWN);
                            else if ((i == 11 && j == 7) && (k == 2 || k == 7 || k == 8))
                                stratCard2.put(hashKey, Play.STAY);
                            else 
                                stratCard1.put(hashKey, Play.STAY);
                            break;
                        case 22:
                            if (i == 11 && j == 11){
                                hashKey = Integer.toString(i + 1) + "." + upCard;
                                stratCard2.put(hashKey, Play.SPLIT);
                            }
                            break;
                        default:
                            stratCard1.put(hashKey, Play.STAY);
                            stratCard2.put(hashKey, Play.STAY);
                            stratCard3.put(hashKey, Play.STAY);
                            break;
                    }
                }
    }
}
