import bagel.Input;
import bagel.MouseButtons;
import bagel.map.TiledMap;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * a class stores current level's information
 *
 */
public class Level {

    //get a list of string storing waves:
    //since the input is always valid, downcasting
    private List<String> wave_list = Collections.EMPTY_LIST;
    {
        try {
            //try (InputStream resource = this.getClass().getResourceAsStream("levels/waves.txt")) {
              //  wave_list =
                //        new BufferedReader(new InputStreamReader(resource,
                  //              StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
            //}
            wave_list = Files.readAllLines(Paths.get(Sprite.getCurPath() + "res/levels/waves.txt"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final int BASE_WAVE_AWARD = 150;
    private static final int BONUS_WAVE_AWARD = 100;
    private static final String TANK = "tank";
    private static final String SUPERTANK = "supertank";
    private static final String AIRSUPPORT = "airsupport";
    private static final String PLACING = "Placing";

    private TiledMap map;
    private List<Point> polyline;
    private List<Wave> waves;
    private Wave wave;
    private Player player;
    private int currWave;
    private BuyPanel buyPanel;

    //max Wave is the first integer of last line, so use following methods
    private int maxWaves = Integer.parseInt(Arrays.asList(wave_list.get(wave_list.size() - 1).split(",")).get(0));

    private int waveIndex = 0;
    private String status = "Awaiting Start";
    private List<Tower> defenders;
    private List<AirSupport> airSupports;
    private List<Rectangle> panelRects;
    private List<Observer> observers = new ArrayList<Observer>();
    private String currSelection;
    private boolean placing = false;

    /**
     * Construct a Level with corresponding map
     * @param map the current level map file, path defined in ShadowDefend Class
     * @param player the player entity that currently plays on this level
     */
    public Level(String map, Player player) {
        //initialise the map, waves of current level
        this.map = new TiledMap(map);
        this.currWave = 1;
        this.waves = new ArrayList<>();
        this.player = player;
        this.defenders = new ArrayList<Tower>();
        this.polyline = this.map.getAllPolylines().get(0);
        this.panelRects = new ArrayList<Rectangle>();
        this.airSupports = new ArrayList<AirSupport>();

        //create List of waves of total wave numbers
        for (int i = 0; i < maxWaves; i++) {
            this.waves.add(new Wave(this.map, wave_list, player,i+1));
        }

        //start with the first wave, load the first wave.
        this.wave = waves.get(0);
    }

    /**
     * Observer class
     * @param observer
     */
    public void attach(Observer observer){
        this.observers.add(observer);
    }

    /**
     * Observer function, notify all other observers
     */
    public void notifyObserver(){
        for (Observer observer: observers){
            observer.updateInfo();
        }
    }


    /**
     * update current level details, return false if current level has finished.
     *
     * @param input keyboard/mouse input
     * @return return false if finish current level
     */
    protected boolean update(Input input) {
        //get current active wave events, update every single frame
        List<WaveEvent> events = wave.getEvents();

        //render current map:
        map.draw(0, 0, 0, 0, ShadowDefend.WIDTH, ShadowDefend.HEIGHT);

        //update status displaying on status panel
        if (placing) {
            this.status = PLACING;
        }
        else{
            this.status = wave.getStatus();
        }

        // Update all defender, then get nearest enemy in range
        for (int i = defenders.size() - 1; i >= 0; i--) {
            Tower s = defenders.get(i);
            //only if tower has not locked on a target
            if (!s.hasTarget()) {
                getEnemyInRange(events, s);
            }
            s.update(input);
        }

        //update airsupport, bomb all enemies in range
        for (int i = airSupports.size() - 1; i >= 0; i--) {
            AirSupport s = airSupports.get(i);
            for (int m = s.getBombs().size() - 1; m >= 0; m--) {
                Bomb bomb = s.getBombs().get(m);
                //if bomb is detonated, do damage
                if (bomb.isDetonated()) {
                    bombEnemy(events, bomb);
                }
            }
            //update air support
            s.update(input);
            //if finish bombing and flying outside the window, remove
            if (s.isFinished()){
                airSupports.remove(i);
            }
        }

        //placing towers on current level
        if (input.wasPressed(MouseButtons.LEFT)&&(placing)){
            this.placing = !addDefender(input);
            this.buyPanel.setPlacing(this.placing);
        }

        //update current wave, check if we finish current wave
        if (!wave.update(input)){
            //if also finish current level, reset wave status and return false
            if (currWave >= maxWaves) {
                return false;
            }
            //update player, panel status
            this.player.addMoney(BASE_WAVE_AWARD + currWave * BONUS_WAVE_AWARD);
            //move on to the next wave
            waveIndex +=1;
            this.wave = this.waves.get(waveIndex);
            this.currWave ++;
        }

        //notify observers if any changes occurs
        notifyObserver();
        return true;
    }

    /**
     * add a new defender
     *
     * @param input mouseinput
     * @return return true if successfully placed a defender
     */
    public boolean addDefender(Input input){
        Point point = new Point(input.getMouseX(),input.getMouseY());
        //if can be placed at current position, place corresponding defender.
        if (canPlace(point)) {
            if (currSelection == TANK) {
                Tower newTank = new Tank(point);
                if (player.deductMoney(newTank.getCost())) {
                    defenders.add(newTank);
                    return true;
                }
            } else if (currSelection == SUPERTANK) {
                Tower newTank = new SuperTank(point);
                if (player.deductMoney(newTank.getCost())) {
                    defenders.add(newTank);
                    return true;
                }
            } else if (currSelection == AIRSUPPORT) {
                AirSupport airSupport = new AirSupport(point);
                if (player.deductMoney(airSupport.getCost())) {
                    airSupports.add(airSupport);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * get enemy who's in range and lock it for a single tower.
     *
     * @param events a list of wave events containing enemies
     * @param tower current tower.
     */
    private void getEnemyInRange(List<WaveEvent> events, Tower tower) {
        for (int c = events.size() - 1; c >= 0; c--) {
            WaveEvent event = events.get(c);
            if (!event.isEmpty()) {
                for (int d = 0; d < event.getEnemies().size(); d++) {
                    Enemy enemy = event.getEnemies().get(d);
                    //if in range, first target locked
                    if (tower.getCenter().distanceTo(enemy.getCenter()) < tower.getRadius()) {
                        tower.lockEnemy(enemy);
                        break;
                    }
                }
            }
        }
    }

    /**
     * eliminate enemy in bomb radius
     *
     * @param events a list of wave events containing enemies
     * @param bomb current bomb.
     */
    private void bombEnemy(List<WaveEvent> events, Bomb bomb) {
        for (int c = events.size() - 1; c >= 0; c--) {
            WaveEvent event = events.get(c);
            if (!event.isEmpty()) {
                for (int d = 0; d < event.getEnemies().size(); d++) {
                    Enemy enemy = event.getEnemies().get(d);
                    //if in range, all targets deduct health.
                    if (bomb.getCenter().distanceTo(enemy.getCenter()) <= bomb.getRADIUS()) {
                        enemy.deductHealth(bomb.getDamage());
                    }
                }
            }
        }
    }

    /**
     *
     * check if we can add a new defender at current position
     *
     * @return false if cannot place, true if can place
     */
    public boolean canPlace(Point point){
        //check if intersects with other defenders
        for (int i = defenders.size() - 1; i >= 0; i--) {
            Tower s = defenders.get(i);
            if (s.getRect().intersects(point)){
                return false;
            }
        }
        // check if intersects with panels
        for (int i = panelRects.size() - 1; i >= 0; i--) {
            Rectangle r = panelRects.get(i);
            if (r.intersects(point)){
                return false;
            }
        }
        //check if intersects with blocked tile
        if (map.hasProperty((int) point.x, (int) point.y, "blocked")) {
            return false;
        }
        // can place, return true!
        return true;
    }

    /**
     * get rectangles of panels into level class
     *
     * @param rect current object's rectangle
     */
    public void addPanelRects(Rectangle rect){
        this.panelRects.add(rect);
    }

    /**
     * set current Tower selection by strings
     *
     * @param tower Name of current tower.
     */
    public void setCurrSelection(String tower){
        this.currSelection = tower;
    }

    /**
     * Set current status of is placing or not
     *
     * @param placing true if currently placing, false otherwise
     */
    public void setPlacing(boolean placing) {
        this.placing = placing;
    }

    /**
     * Connect current level with a buy panel
     *
     * @param buyPanel the buy panel to link.
     */
    public void setBuyPanel(BuyPanel buyPanel){
        this.buyPanel = buyPanel;
    }



    //getter functions:
    public int getCurrWave() {
        return currWave;
    }

    public String getStatus() {
        return status;
    }
}



