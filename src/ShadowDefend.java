import bagel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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
    //private static final String MAP_FILE1 = ShadowDefend.class.getResource("/levels/1.tmx").getPath();
    //private static final String MAP_FILE2 = ShadowDefend.class.getResource("/levels/2.tmx").getPath();

    private static final InputStream MAP1_Stream = ShadowDefend.class.getResourceAsStream("/levels/1.tmx");
    private static final InputStream MAP2_Stream = ShadowDefend.class.getResourceAsStream("/levels/2.tmx");

    private static String MAP_FILE1 = "";
    private static String MAP_FILE2 = "";
    File map1Temp =null;
    File map2Temp =null;
    File sheet1Temp =null;
    File sheet2Temp =null;
    File sheet1png =null;
    File sheet2png =null;
    private static final InputStream sheet1_Stream = ShadowDefend.class.getResourceAsStream("/levels/sheet.tsx");
    private static final InputStream sheet2_Stream = ShadowDefend.class.getResourceAsStream("/levels/sheet2.tsx");
    private static final InputStream sheetpng_Stream = ShadowDefend.class.getResourceAsStream("/levels/sheet.png");
    private static final InputStream sheet2png_Stream = ShadowDefend.class.getResourceAsStream("/levels/sheet2.png");

    //Create temp file for loading map purpose
    // Get the temporary directory.
    String tempDir = System.getProperty("java.io.tmpdir");
    {
        try {
            // 创建临时文件
            map1Temp = new File(tempDir+File.separator+"tmp.txt");
            map2Temp = new File(tempDir+File.separator+"tmp2.txt");
            sheet1Temp = new File(tempDir+File.separator+"sheet.tsx");
            sheet1png = new File(tempDir+File.separator+"sheet.png");
            sheet2Temp = new File(tempDir+File.separator+"sheet2.tsx");
            sheet2png = new File(tempDir+File.separator+"sheet2.png");
            //写入地图数据
            byte[] buffer = new byte[MAP1_Stream.available()];
            MAP1_Stream.read(buffer);
            OutputStream outStream = new FileOutputStream(map1Temp);
            outStream.write(buffer);

            byte[] buffer2 = new byte[MAP2_Stream.available()];
            MAP2_Stream.read(buffer2);
            OutputStream outStream2 = new FileOutputStream(map2Temp);
            outStream2.write(buffer2);

            byte[] buffer3 = new byte[sheet1_Stream.available()];
            sheet1_Stream.read(buffer3);
            OutputStream outStream3 = new FileOutputStream(sheet1Temp);
            outStream3.write(buffer3);

            byte[] buffer4 = new byte[sheet2_Stream.available()];
            sheet2_Stream.read(buffer4);
            OutputStream outStream4 = new FileOutputStream(sheet2Temp);
            outStream4.write(buffer4);

            byte[] buffer5 = new byte[sheetpng_Stream.available()];
            sheetpng_Stream.read(buffer5);
            OutputStream outStream5 = new FileOutputStream(sheet1png);
            outStream5.write(buffer5);

            byte[] buffer6 = new byte[sheet2png_Stream.available()];
            sheet2png_Stream.read(buffer6);
            OutputStream outStream6 = new FileOutputStream(sheet2png);
            outStream6.write(buffer6);

            // 输出绝对路径
            MAP_FILE1 = map1Temp.getAbsolutePath();
            MAP_FILE2 = map2Temp.getAbsolutePath();
            System.out.println(MAP_FILE1);
            // 终止后删除临时文件
            map1Temp.deleteOnExit();
            map2Temp.deleteOnExit();
        } catch (Exception e) {
            // 如果有错误输出内容
            e.printStackTrace();
        }
    }

    //private static final String MAP_FILE1 = "res/levels/1.tmx";
    //private static final String MAP_FILE2 = "res/levels/2.tmx";

    //we make the following variables public since they are necessary public settings.
    public static final double FPS = 60;
    public static final Keys SLOW_DOWN = Keys.K;
    public static final Keys SPEED_UP = Keys.L;
    public static final Keys START_WAVE = Keys.S;
    //set up the maximum levels have here
    public static final int MAX_LEVELS = 2;
    private static final int START_MONEY = 500;
    private static final int START_HEALTH = 25;

    // The spawn delay (in seconds) to spawn slicers
    private static final int INITIAL_TIMESCALE = 1;
    public static final int MAX_TIMESCALE = 10;
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
        levels.add(new Level(MAP_FILE1,player));
        levels.add(new Level(MAP_FILE2,player));

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
