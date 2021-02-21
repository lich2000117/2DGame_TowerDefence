import bagel.DrawOptions;
import bagel.Image;
import bagel.Font;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * A status panel that shows status
 *
 * **/
public class StatusPanel extends Observer{

    //Status Strings:
    private static final String WIN = "Winner!";
    private static final String FONT_NAME = "/fonts/DejaVuSans-Bold.ttf";

    InputStream inputStream = getClass().getResourceAsStream(FONT_NAME);
    String tempDir = System.getProperty("java.io.tmpdir");
    File temp = new File(tempDir+File.separator+"tempfont1.ttf");
    String FONT_PATH = "";
    {
        try {
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            OutputStream outStream = new FileOutputStream(temp);
            outStream.write(buffer);
            FONT_PATH = temp.getAbsolutePath();
        }
        catch (Exception e) {
            // 如果有错误输出内容
            e.printStackTrace();
        }
    }


    private static final String IMG_NAME = "/images/statuspanel.png";
    private static final int PANEL_WIDTH = 25;
    private static final int DEFAULT_TC = 1;
    private static final int LOW_HEALTH = 15;
    private static final int EXTREME_LOW_HEALTH = 10;

    private final Image image;
    private final Font font;
    private final int fontSize = 18;
    private final Rectangle rect;

    private int wave;
    private double timeScale = 2;
    private int lives;
    private String status;
    private Point panelLocation;
    private static boolean rectAddedToLevel = false;

    /**
     * Constructor:
     * @param player is the corresponding player entity class, stores money and health
     * @param level is the corresponding current level class, in order to get current wave and status
     */
    public StatusPanel(Player player, Level level) {
        InputStream inputStream = getClass().getResourceAsStream(IMG_NAME);
        String tempDir = System.getProperty("java.io.tmpdir");
        File temp = new File(tempDir+File.separator+"tmp.txt");
        String IMG_PATH = "";
        {
            try {
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                OutputStream outStream = new FileOutputStream(temp);
                outStream.write(buffer);
                IMG_PATH = temp.getAbsolutePath();
            }
            catch (Exception e) {
                // 如果有错误输出内容
                e.printStackTrace();
            }
        }
        this.image = new Image(IMG_PATH);
        this.font = new Font(FONT_PATH, fontSize);
        this.lives = player.getHealth();
        this.status = level.getStatus();
        this.wave = level.getCurrWave();
        this.panelLocation = new Point(ShadowDefend.WIDTH / 2, ShadowDefend.HEIGHT - PANEL_WIDTH / 2);
        this.rect = image.getBoundingBoxAt(panelLocation);
        //observer pattern:
        this.player = player;
        this.player.attach(this);
        this.level = level;
        this.level.attach(this);
    }

    /**
     * observer design pattern functionality
     */
    public void updateInfo(){
        this.lives = player.getHealth();
        this.wave = level.getCurrWave();
        this.status = level.getStatus();
        //if already added current panel's rectangle to level, do not add in repeat
        if (!rectAddedToLevel) {
            this.level.addPanelRects(rect);
            this.rectAddedToLevel = true;
        }
    }

    /**
     * Set Winner Status!
     */
    public void setWin(){
        this.status = WIN;
    }


    /**
     * update information and render panel
     *
     * @param timeScale get timeScale and print it on the screen
     */
    protected void update(int timeScale) {
        this.timeScale = timeScale;
        drawBackground();
        drawWaves();
        drawTimeScale();
        drawStatus();
        drawLives();
    }


    /**Drawing functions:
     *
     * draw all elements needed in following
     * functions!
     *
     */
    private void drawBackground() {
        image.draw(panelLocation.x, panelLocation.y);
    }

    private void drawLives() {
        //lives, if we are running low on lives, turns yellow, and if gets severe, turns red:
        if (this.lives < EXTREME_LOW_HEALTH) {
            font.drawString("Lives: " +lives, 910, 762,
                    new DrawOptions().setBlendColour(Colour.RED));
        } else if (this.lives < LOW_HEALTH) {
            font.drawString("Lives: " + lives, 910, 762,
                    new DrawOptions().setBlendColour(255, 255, 0));
        } else {
            //we have many lives, dont worry! goes green
            font.drawString("Lives: " +lives, 910, 762,
                    new DrawOptions().setBlendColour(Colour.GREEN));
        }
    }

    private void drawWaves() {
        font.drawString("Wave: " + wave, 15, 762);
    }

    private void drawTimeScale() {
        //timescale checks:
        if (timeScale > DEFAULT_TC) {
            font.drawString("Time Scale: " + timeScale, 200, 762,
                    new DrawOptions().setBlendColour(Colour.GREEN));
        } else {
            font.drawString("Time Scale: " + timeScale, 200, 762);
        }
    }

    private void drawStatus() {
        //status:
        font.drawString("Status: " + status, 450, 762);
    }


    /**
     * reset status panel if new level reached
     *
     * @param level reconnect to another level's class, to get wave and status information
     */
    public void reset(Level level){
        this.status = level.getStatus();
        this.wave = level.getCurrWave();
        this.level = level;
        this.level.attach(this);
    }
}