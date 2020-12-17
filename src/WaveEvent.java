import bagel.Input;
import bagel.map.TiledMap;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * A class stores each wave event information
 *
 * **/

public class WaveEvent{

    private List<Point> polyline;
    private int spawnedSlicers;
    private double spawnCountDown;
    private double delayCount;
    private final List<Enemy> enemies;
    private List<String> infoString;
    private String enemyType;
    private Player player;
    private boolean delayWave = false;
    private double spawnDelay;
    private boolean isFinished = false;

    //Final Constants
    private int maxEnemy;
    private static final String DELAY = "delay";
    private static final String SLICER = "slicer";
    private static final String SUPER_SLICER = "superslicer";
    private static final String MEGA_SLICER = "megaslicer";
    private static final String APEX_SLICER = "apexslicer";


    /**
     *
     * Construct a list of wave event
     *
     * @param map  map file
     * @param player player entity
     * @param info the content of wave event details containing in a text file.
     */
    public WaveEvent(TiledMap map, Player player, String info) {
        this.polyline = map.getAllPolylines().get(0);
        this.enemies = new ArrayList<>();
        this.spawnedSlicers = 0;
        this.player = player;
        infoString = Arrays.asList(info.split(","));
        // put information into current wave
        if (!constructWave(infoString)) {
            this.delayWave = true;
        }
    }


    /** return false if the current wave is a delay wave, else, construct a new wave according to Waves.text
     *
     * @param infoString a line of string extracted from text file
     * @return return false is current wave event is a delay event.
     */
    private boolean constructWave(List infoString){
        // if it is Delay signal:
        if (String.valueOf(infoString.get(1)).equals(DELAY)){
            this.delayCount = Double.parseDouble(infoString.get(2).toString());
            return false;
        }

        // construct wave information
        this.maxEnemy = Integer.parseInt(infoString.get(2).toString());
        this.enemyType = String.valueOf(infoString.get(3));
        this.spawnDelay = Double.parseDouble(infoString.get(4).toString()) / 1000;
        return true;
    }

    /**
     * update method
     * @param input key/keyboard/mouse action
     */
    protected void update(Input input) {
        // Increase the frame counter by the current timescale
        spawnCountDown += ShadowDefend.getTimescale();

        // waiting for delay wave to complete
        if (delayWave == true) {
            delayCount -= ShadowDefend.getTimescale();
            //if complete delay wave, reset status
            if (delayCount <= 0){
                delayWave = false;
            }
        }

        // Check if it is time to spawn a new slicer (and we have some left to spawn)
        // Also Spawn slicer with different types
        if (spawnCountDown / ShadowDefend.FPS >= spawnDelay && spawnedSlicers != maxEnemy) {

            if (this.enemyType.equals(SLICER)) {
                spawnSlicer(1);
            }
            else if (this.enemyType.equals(SUPER_SLICER)) {
                spawnSuperSlicer(1);
            }
            else if (this.enemyType.equals(MEGA_SLICER)) {
                spawnMegaSlicer(1);
            }
            else if (this.enemyType.equals(APEX_SLICER)) {
                spawnApexSlicer(1);
            }
        }

        //if finish current wave event reset wave status and return false,move on to the next wave
        if ((spawnedSlicers == maxEnemy) && (!delayWave) && (enemies.size() == 0)) {
            isFinished = true;
        }

        // Update all sprites, and remove them if they've finished
        for (int i = enemies.size() - 1; i >= 0; i--) {
            Enemy s = enemies.get(i);
            s.update(input);
            //check if it is killed, add money
            if (s.isKilled()){
                this.player.addMoney(s.getReward());
                //if enemy can spawn kids, do it!
                s.spawnKids(enemies);
                //remove current enemy
                enemies.remove(i);
            }
            //check if reach the base, deduct health and remove the enemy
            else if (s.isFinished()) {
                player.deductHealth(enemies.get(i).getDamage());
                enemies.remove(i);
            }
        }
    }

    /**
     * check if finish current wave event
     * @return true if finished, false otherwise.
     */
    public boolean isFinished(){
        return isFinished;
    }


    /**
     * check if all units are eliminated.
     * @return true if all enemies gone.
     */
    public boolean isEmpty(){
        return enemies.isEmpty();
    }

    /**
     * Spawn slicers function:
     * @param num number of slicers wish to spawn
     */
    private void spawnSlicer(int num){
        int i;
        for (i=0;i<num;i++) {
            enemies.add(new Slicer(polyline, polyline.get(0)));
            spawnedSlicers += 1;
            // Reset frame counter
            spawnCountDown = 0;
        }
    }

    /**
     * Spawn superslicers function:
     * @param num number of slicers wish to spawn
     */
    private void spawnSuperSlicer(int num){
        int i;
        for (i=0;i<num;i++) {
            enemies.add(new SuperSlicer(polyline,polyline.get(0)));
            spawnedSlicers += 1;
            // Reset frame counter
            spawnCountDown = 0;
        }
    }

    /**
     * Spawn Megaslicers function:
     * @param num number of slicers wish to spawn
     */
    private void spawnMegaSlicer(int num) {
        int i;
        for (i = 0; i < num; i++) {
            enemies.add(new MegaSlicer(polyline, polyline.get(0)));
            spawnedSlicers += 1;
            // Reset frame counter
            spawnCountDown = 0;
        }
    }

    /**
     * Spawn different ApexSlicer function:
     * @param num number of slicers wish to spawn
     */
    private void spawnApexSlicer(int num) {
        int i = 0;
        for (i = 0; i < num; i++) {
            enemies.add(new ApexSlicer(polyline, polyline.get(0)));
            spawnedSlicers += 1;
            // Reset frame counter
            spawnCountDown = 0;
        }
    }

    //getters
    public List<Enemy> getEnemies() {
        return enemies;
    }
}
