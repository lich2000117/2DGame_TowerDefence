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
    private static final String FONT_PATH = Sprite.getCurPath() + "res/fonts/DejaVuSans-Bold.ttf";

    //basic information for rendering:
    private int Upgrade_PRICE = 100;
    private int SUPERTANK_PRICE = 600;
    private int wallet;
    private Tower selectedTower;

    //Different Font Size:
    private static final int PRICE_SIZE = 25;
    private static final int UPGRADE_SIZE = 20;
    private static final int TEXT_SIZE = 15;

    // objects needed for rendering
    private final Image panelImg;
    private Image UpgradeImg;

    //font information and colours
    private final Font priceFont;
    private final Font upgradeFont;
    private final Font textFont;
    private final DrawOptions RED = new DrawOptions().setBlendColour(Colour.RED);
    private final DrawOptions GREEN = new DrawOptions().setBlendColour(Colour.GREEN);

    //for rendering hovering function
    private Point panelLocation;
    private Level level;
    private static boolean firstCall = true; //to make sure user not missclick upgrade button, set this boolean value



    /**
     * Construct Buy panel.
     *
     * @param player is the player currently connected to, using Observer method here
     * @param level is the current level connected to
     *
     */
    public UpgradePanel(Player player, Level level, Tower selectedTower) {
        this.panelImg = new Image(PANEL);
        this.panelLocation = new Point(panelImg.getWidth()/2, panelImg.getHeight()/2);
        this.level = level;

        this.priceFont = new Font(FONT_PATH,PRICE_SIZE);
        this.upgradeFont = new Font(FONT_PATH, UPGRADE_SIZE);
        this.textFont = new Font(FONT_PATH, TEXT_SIZE);
        this.wallet = player.getMoney();
        this.player = player;
        this.player.attach(this);
        this.level.attach(this);
        this.selectedTower = selectedTower;
        //Check if current tower has upgrade option
        if (selectedTower.getNextTower() == null){
            this.Upgrade_PRICE=-1;
            this.UpgradeImg=null;
        }
        else {
            this.Upgrade_PRICE = selectedTower.getNextTower().getCost() / 2;
            this.UpgradeImg = selectedTower.getNextTower().getImage();
        }
    }

    /**
     * Draw list of costs in this function
     * and rendering red if can afford else green.
     *
     */
    private void drawUpgradeModule(){
        upgradeFont.drawString("UPGRADE", panelLocation.x/16, panelImg.getHeight()/2);
        //draw list of items' price:
        if (wallet >= Upgrade_PRICE){
            priceFont.drawString(DOLLAR+ Upgrade_PRICE, panelLocation.x/3, panelImg.getHeight()/2 +100, GREEN);
        }
        else {
            priceFont.drawString(DOLLAR + Upgrade_PRICE, panelLocation.x/3, panelImg.getHeight()/2 +100, RED);
        }
    }

    /**
     * draw key binding settings
     */
    private void drawTowerInfo() {
        textFont.drawString("Name: \n "+this.selectedTower.getNAME()+"\n" +
                "DMG:   "+this.selectedTower.getDamage()+ "\n" +
                "Range: "+this.selectedTower.getRadius()+"\n" +
                "Rate:   "+String.format("%.1f", this.selectedTower.getFireRate())+"\n",
                panelLocation.x/8, panelImg.getHeight()/4);
    }


    /**
     * Observer functionality
     *
     */
    public void updateInfo(){
        wallet = player.getMoney();
        //if already added, do not add in repeat
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
        // if no next upgrade, don't draw.
        if ((Upgrade_PRICE >= 0 )){
            UpgradeImg.draw(panelLocation.x, panelImg.getHeight()/2 + 50);
            drawUpgradeModule();
            //check if clicked after the initialize, get selected item and upgrade it:
            if (firstCall==false) {
                upgradeTower(input);
            }
        }
        //draw Price and Key binding messages
        drawTowerInfo();
        firstCall = false; //first time initialize without executing buttons
    }

    /**
     * a method that get current upgraded tower
     *
     **/
    public void upgradeTower(Input input){
        if(input.wasPressed(MouseButtons.LEFT)){
            Point mousePos = new Point(input.getMouseX(), input.getMouseY());
            //check if currently selecting tank, super tank or air_support by monitoring mouse center
            if (UpgradeImg.getBoundingBoxAt(new Point(panelLocation.x, panelImg.getHeight()/2 + 50)).intersects(mousePos)){
                //if have sufficient money ot purchase, then select
                if (wallet>=Upgrade_PRICE) {
                    this.level.replaceTower(this.selectedTower, this.selectedTower.getNextTower(), Upgrade_PRICE);
                    //set image displays upon hovering
                }
            }
            /*else if (superTankImg.getBoundingBoxAt(new Point(panelLocation.x, panelImg.getHeight()/2)).intersects(mousePos)){
                if (wallet>=SUPERTANK_PRICE) {
                    System.out.println("sucessfully purchase supertank upgrade");
                }
            }*/
        }
    }

    public Rectangle getUpgradeRectangle() {
        return panelImg.getBoundingBoxAt(panelLocation);
    }

    public void setFirstCall(boolean firstCall) {
        UpgradePanel.firstCall = firstCall;
    }
}
