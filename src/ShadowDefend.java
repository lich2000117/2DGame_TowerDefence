import bagel.*;
import bagel.Window;

import java.util.ArrayList;
import java.util.List;


/**
 * ShadowDefend, a tower defence game.
 */
public class ShadowDefend extends AbstractGame {

    // make window configuration public since it's useful.
    public static final int HEIGHT = 768;
    public static final int WIDTH = 1024;

    //storing each map's file:
    //private static final String MAP_FILE1 = ShadowDefend.class.getResource("/levels/1.tmx").toString();
    //private static final String MAP_FILE2 = ShadowDefend.class.getResource("/levels/2.tmx").toString();
    private static final String MAP_FILE1 = Sprite.getCurPath() + "res/levels/1.tmx";
    private static final String MAP_FILE2 = Sprite.getCurPath() + "res/levels/2.tmx";

    //we make the following variables public since they are necessary public settings.
    public static final double FPS = 60;
    public static final Keys SLOW_DOWN = Keys.K;
    public static final Keys SPEED_UP = Keys.L;
    public static final Keys START_WAVE = Keys.S;
    //set up the maximum levels have here
    public static final int MAX_LEVELS = 2;
    private static final int START_MONEY = 600;
    private static final int START_HEALTH = 25;

    // The spawn delay (in seconds) to spawn slicers
    private static final int INITIAL_TIMESCALE = 1;
    public static final int MAX_TIMESCALE = 5;
    // Timescale is made static because it is a universal property of the game and the specification
    private static int timescale = INITIAL_TIMESCALE;
    private static int currLevelNum = 0;

    //get a list of levels using lists, so it can be extended many times
    private List<Level> levels;
    private Level currLevel;
    private StatusPanel statusPanel;
    private BuyPanel buyPanel;
    private Player player;

    /**
     * Creates a new instance of the ShadowDefend game, with 2 levels
     */
    public ShadowDefend() {
        super(WIDTH, HEIGHT, "ShadowDefend");
        levels = new ArrayList<>();

        //player:
        this.player = Player.setPlayer(START_MONEY, START_HEALTH);

        //add 2 levels into our levels lists
        levels.add(new Level(MAP_FILE1,player, 1));
        levels.add(new Level(MAP_FILE2,player, 2));

        //start with first level, index at 0
        this.currLevel = levels.get(0);

        //initialize panels
        this.statusPanel = new StatusPanel(player, currLevel);
        this.buyPanel = new BuyPanel(player, currLevel);
    }

    /**
     * The entry-point for the game
     *
     * @param args Optional command-line arguments
     */
    public static void main(String[] args) {
        new ShadowDefend().run();
    }

    /**
     * get current timescale
     * @return timescale
     */
    public static int getTimescale() {
        return timescale;
    }

    /**
     * Increases the timescale
     */
    private void increaseTimescale() {
        if (timescale<MAX_TIMESCALE) {
            timescale++;
        }
    }

    /**
     * Decreases the timescale but doesn't go below the base timescale
     */
    private void decreaseTimescale() {
        if (timescale > INITIAL_TIMESCALE) {
            timescale--;
        }
    }

    /**
     * private method to call for the next level.
     *
     */
    private void nextLevel() {

        //if we finish all the levels, WIN! display winner!
        if (this.currLevelNum >= MAX_LEVELS - 1) {
            this.statusPanel.setWin();
        }
        //proceed to next level:
        else{
            currLevelNum += 1;
            this.currLevel = levels.get(currLevelNum);
            //reset player status and panels and panel rectangles lists:
            this.player = Player.setPlayer(START_MONEY, START_HEALTH);
            this.statusPanel.reset(currLevel);
            this.buyPanel.reset(currLevel);
        }
    }

    /**
     * Update the state of the game, potentially reading from input
     *
     * @param input The current mouse/keyboard state
     */
    @Override
    protected void update(Input input) {

        //check if player is died, close window
        if (player.getHealth()<=0){
            Window.close();
        }

        // update on current level, if finish current level, move on to the next level
        if (currLevel.update(input) == false){
            nextLevel();
        }

        // Handle key presses for controlling time
        if (input.wasPressed(SPEED_UP)) {
            increaseTimescale();
        }
        if (input.wasPressed(SLOW_DOWN)) {
            decreaseTimescale();
        }

        //render status and buy panel
        statusPanel.update(getTimescale());
        buyPanel.update(input);
    }

}
