import bagel.*;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 *
 * A Buy panel that generate buying options and
 * rendering selection animation
 *
 **/

public class UpgradePanel extends Observer{

    //resource paths:
    private static final String DOLLAR = "$";
    private static final String PANEL = Sprite.getCurPath() + "res/images/upgradepanel.png";
    private static final String TANK = Sprite.getCurPath() + "res/images/tank.png";
    private static final String SUPERTANK = Sprite.getCurPath() + "res/images/supertank.png";
    private static final String FONT_PATH = Sprite.getCurPath() + "res/fonts/DejaVuSans-Bold.ttf";

    //basic information for rendering:
    private static final int TANK_PRICE = 250;
    private static final int SUPERTANK_PRICE = 600;
    private static final int AIRSUPPORT_PRICE = 500;
    private static final int INI_WIDTH = 64;
    private static final int GAP = 120;
    private static final int WALLET_Y = 65;
    private int wallet;
    private Tower selectedTower;

    //Different Font Size:
    private static final int WALLET_SIZE = 50;
    private static final int LIST_SIZE = 25;
    private static final int KEY_SIZE = 15;

    // objects needed for rendering
    private final Image panelImg;
    private final Image tankImg;
    private final Image superTankImg;

    //font information and colours
    private final Font listFont;
    private final Font moneyFont;
    private final Font keyFont;
    private final DrawOptions RED = new DrawOptions().setBlendColour(Colour.RED);
    private final DrawOptions GREEN = new DrawOptions().setBlendColour(Colour.GREEN);

    //for rendering hovering function
    private Point panelLocation;
    private Level level;
    private static boolean placing = false;



    /**
     * Construct Buy panel.
     *
     * @param player is the player currently connected to, using Observer method here
     * @param level is the current level connected to
     *
     */
    public UpgradePanel(Player player, Level level, Tower selectedTower) {
        this.panelImg = new Image(PANEL);
        this.tankImg = new Image(TANK);
        this.superTankImg = new Image(SUPERTANK);
        this.panelLocation = new Point(panelImg.getWidth()/2, panelImg.getHeight()/2);
        this.level = level;

        this.listFont = new Font(FONT_PATH,LIST_SIZE);
        this.moneyFont = new Font(FONT_PATH, WALLET_SIZE);
        this.keyFont = new Font(FONT_PATH, KEY_SIZE);
        this.wallet = player.getMoney();
        this.player = player;
        this.player.attach(this);
        this.level.attach(this);
        this.selectedTower = selectedTower;
        //this.level.setBuyPanel(this);
    }

    /**
     * Draw list of costs in this function
     * and rendering red if can afford else green.
     *
     */
    private void drawPrice(){
        //draw list of items' price:
        if (wallet >= TANK_PRICE){
            listFont.drawString(DOLLAR+ TANK_PRICE, panelLocation.x/3, panelImg.getHeight()/4 + 50, GREEN);
        }
        else {
            listFont.drawString(DOLLAR + TANK_PRICE, panelLocation.x/3, panelImg.getHeight()/4 + 50, RED);
        }

        if (wallet >= SUPERTANK_PRICE){
            listFont.drawString(DOLLAR+SUPERTANK_PRICE, panelLocation.x/3, panelImg.getHeight()/2 + 50, GREEN);
        }
        else {
            listFont.drawString(DOLLAR +SUPERTANK_PRICE, panelLocation.x/3, panelImg.getHeight()/2 + 50, RED);
        }
    }

    /**
     * draw key binding settings
     */
    private void drawKeyBinds() {
        keyFont.drawString("Key binds: \n\nS - Start Wave\nL - Increase TimeScale\nK - Decrease TimeScale",
                panelImg.getWidth() / 2-40, 20);

    }


    /**
     * Observer functionality
     *
     */
    public void updateInfo(){
        wallet = player.getMoney();
        //if already added, do not add in repeat
    }

    /**reset buy panel with connection to new level if new level reached
     *
     * @param level reconnect to another new level class
     */
    public void reset(Level level){
        this.level = level;
        //this.level.setBuyPanel(this);
        this.placing = false;
    }


    /**
     * update method that called every frame
     *
     * @param input keyboard/mouse input
     *
     */
    protected void update(Input input) {
        //draw panel
        panelImg.draw(panelLocation.x, panelLocation.y);

        //draw object images:
        tankImg.draw(panelLocation.x, panelImg.getHeight()/4);
        superTankImg.draw(panelLocation.x, panelImg.getHeight()/2);

        //draw Price and Key binding messages
        drawPrice();
        drawKeyBinds();

        //if clicked, get selected item and drawing hovering image:
        selectItem(input);
        Point mousePos = new Point(input.getMouseX(), input.getMouseY());
    }

    /**
     * a method that get current item selecting and deselecting when right button is clicked.
     *
     **/
    public void selectItem(Input input){
        if(input.wasPressed(MouseButtons.LEFT)){
            Point mousePos = new Point(input.getMouseX(), input.getMouseY());
            //check if currently selecting tank, super tank or air_support by monitoring mouse center
            if (tankImg.getBoundingBoxAt(new Point(panelLocation.x, panelImg.getHeight()/4)).intersects(mousePos)){
                //if have sufficient money ot purchase, then select
                if (wallet>=TANK_PRICE) {
                    System.out.println("sucessfully purchase tank upgrade");
                    //set image displays upon hovering
                }
            }
            else if (superTankImg.getBoundingBoxAt(new Point(panelLocation.x, panelImg.getHeight()/2)).intersects(mousePos)){
                if (wallet>=SUPERTANK_PRICE) {
                    System.out.println("sucessfully purchase supertank upgrade");
                    Tower superTank = new SuperTank(this.selectedTower.getCenter());
                    this.level.replaceTower(this.selectedTower, superTank);
                }
            }
        }
    }

    public Rectangle getUpgradeRectangle() {
        return panelImg.getBoundingBoxAt(panelLocation);
    }
}
