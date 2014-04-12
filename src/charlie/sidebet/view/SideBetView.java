package charlie.sidebet.view;

import charlie.audio.Effect;
import charlie.audio.SoundFactory;
import charlie.card.Hid;
import charlie.plugin.ISideBetView;
import charlie.util.Constant;
import charlie.view.AMoneyManager;
import static charlie.view.AMoneyManager.PLACE_HOME_X;
import static charlie.view.AMoneyManager.PLACE_HOME_Y;
import static charlie.view.AMoneyManager.STAKE_HOME_X;
import static charlie.view.AMoneyManager.STAKE_HOME_Y;
import charlie.view.sprite.AtStakeSprite;
import static charlie.view.sprite.AtStakeSprite.DIAMETER;
import charlie.view.sprite.Chip;

import charlie.view.sprite.ChipButton;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.ImageIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the side bet view
 * 
 * @author Devin Young and Kevin Pietrow
 */
public class SideBetView implements ISideBetView {
    private final Logger LOG = LoggerFactory.getLogger(SideBetView.class);
    
    public final static int X = 400;
    public final static int Y = 200;
    public final static int DIAMETER = 50;
    protected Integer[] amounts = { 100, 25, 5 };
    protected List<Chip> chips = new ArrayList<>();
    
    protected Font font = new Font("Arial", Font.BOLD, 18);
    protected Font descFont = new Font("Arial", Font.BOLD, 15);
    protected BasicStroke stroke = new BasicStroke(3);
    
    // See http://docs.oracle.com/javase/tutorial/2d/geometry/strokeandfill.html
    protected float dash1[] = {10.0f};
    protected BasicStroke dashed
            = new BasicStroke(3.0f,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER,
                    10.0f, dash1, 0.0f);   

    protected List<ChipButton> buttons;
    protected int amt = 0;
    private final int width;
    protected final static String[] UP_FILES =
        {"chip-100-1.png","chip-25-1.png","chip-5-1.png"};
    protected Random ran = new Random();
    protected AMoneyManager moneyManager;
    protected AtStakeSprite wager = new AtStakeSprite(X-DIAMETER/2,Y-DIAMETER/2 - 5,50);
    public final static int PLACE_HOME_X = X + AtStakeSprite.DIAMETER + 10;
    public final static int PLACE_HOME_Y = Y + AtStakeSprite.DIAMETER / 4;
    private double finalBet;
    private boolean startGame;
    protected Color looseColorFg = new Color(250,58,5);
    protected Color looseColorBg = Color.WHITE;
    protected Color winColorBg = Color.BLACK;
    protected Color winColorFg = new Color(116,255,4);
    protected Font outcomeFont = new Font("Arial", Font.BOLD, 18);

    public SideBetView() {
        LOG.info("side bet view constructed");
        ImageIcon icon = new ImageIcon(Constant.DIR_IMGS+UP_FILES[0]);

        Image img = icon.getImage();
        this.width = img.getWidth(null);
    }
    
    /**
     * Sets the money manager.
     * @param moneyManager 
     */
    @Override
    public void setMoneyManager(AMoneyManager moneyManager) {
        this.moneyManager = moneyManager;
        this.buttons = moneyManager.getButtons();
    }
    
    /**
     * Registers a click for the side bet.
     * @param x X coordinate
     * @param y Y coordinate
     */
    @Override
    public void click(int x, int y) {
        int oldAmt = amt;
        
        // Test if any chip button has been pressed.
        for(ChipButton button: buttons) {
            if(button.isPressed(x, y)) {
                amt += button.getAmt();
                LOG.info("A. side bet amount "+button.getAmt()+" updated new amt = "+amt);
                
                int n = chips.size();
                int placeX = PLACE_HOME_X + n * width/3 + ran.nextInt(10)-10;
                
                int placeY = PLACE_HOME_Y + ran.nextInt(5)-5;
                
                Chip chip = new Chip(button.getImage(),placeX - 20,placeY - 34,button.getAmt());
                chips.add(chip);
                SoundFactory.play(Effect.CHIPS_IN);
            } 
        }
        
        if(this.wager.isPressed(x, y)) {
                if (oldAmt == amt) {
                    amt = 0;
                    LOG.info("B. side bet amount cleared");
                    
                    chips.clear();
                    
                    SoundFactory.play(Effect.CHIPS_OUT);
                }
        }
        
    }

    /**
     * Informs view the game is over and it's time to update the bankroll for the hand.
     * @param hid Hand id
     */
    @Override
    public void ending(Hid hid) {
        double bet = hid.getSideAmt();
        this.startGame = false;
        this.finalBet = bet;
        if(bet == 0)
            return;

        LOG.info("side bet outcome = "+bet);
        
        // Update the bankroll
        moneyManager.increase(bet);
        
        LOG.info("new bankroll = "+moneyManager.getBankroll());
    }

    /**
     * Informs view the game is starting
     */
    @Override
    public void starting() {
        this.startGame = true;
    }

    /**
     * Gets the side bet amount.
     * @return Bet amount
     */
    @Override
    public Integer getAmt() {
        return amt;
    }

    /**
     * Updates the view
     */
    @Override
    public void update() {
    }

    /**
     * Renders the view
     * @param g Graphics context
     */
    @Override
    public void render(Graphics2D g) {
        // Draw the at-stake place on the table
        g.setColor(Color.RED); 
        g.setStroke(dashed);
        g.drawOval(X-DIAMETER/2, Y-DIAMETER/2 - 6, DIAMETER, DIAMETER);
        
        // Draw the side bet descriptions on the table
        g.setFont(descFont);
        g.setColor(Color.BLACK);
        g.drawString("SUPER 7 pays 3:1", X + 40, Y - 15);
        g.drawString("ROYAL MATCH pays 25:1", X + 40, Y);
        g.drawString("EXACTLY 13 pays 1:1", X + 40, Y + 15);
        
        // Draw the at-stake amount
        g.setFont(font);
        g.setColor(Color.WHITE);
        
        String text = amt + "";
        FontMetrics fm = g.getFontMetrics(font);

        int x = X-DIAMETER/2 + DIAMETER/2 - fm.charsWidth(text.toCharArray(), 0, text.length()) / 2;
        int y = Y-DIAMETER/2 - 6 + DIAMETER/2 + fm.getHeight() / 4;
        
        g.drawString(amt+"", x, y);
        
        for(int i=0; i < chips.size(); i++) {
            Chip chip = chips.get(i);
            chip.render(g);
        }
        
        if (this.finalBet != 0 && this.startGame != true){
            System.out.println("FINAL BET..................." + this.finalBet);
            String outcomeText = "";
            Color background;
            Color foreground;
            
            if (this.finalBet > 0){
                outcomeText = " WIN ! ";
                background = winColorBg;
                foreground = winColorFg;
            }
            else {
                outcomeText = " LOSE ! ";
                background = looseColorBg;
                foreground = looseColorFg;
            }
            
            int n = chips.size() - 1;
            
            //keeps win/lose banner centered over at stake chips
            int placeX = ((X + (AtStakeSprite.DIAMETER / 2) + 5)+(X + (AtStakeSprite.DIAMETER / 2) + 5 + (n * width/3)))/2;
            int placeY = y;
            
            fm = g.getFontMetrics(outcomeFont);
            int w = fm.charsWidth(outcomeText.toCharArray(), 0, outcomeText.length());
            int h = fm.getHeight();
            
            g.setColor(background);
            g.fillRoundRect(placeX, placeY-h+5, w, h, 5, 5);
            
            g.setColor(foreground);
            g.setFont(outcomeFont);
            
            g.drawString(outcomeText,placeX,placeY);
        }
        
    }
}

