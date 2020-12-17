import bagel.Input;
import bagel.map.TiledMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * A class stores each wave information
 *
 * **/
public class Wave{

    private List<WaveEvent> events;
    private boolean waveStarted;
    private boolean waveFinished = false;
    private List<String> wave_list;
    private TiledMap map;
    private int maxEventIndex = 0;
    private int minEventIndex = -1;
    private int currIndex;
    private boolean lastEventFinished = true;
    private Player player;
    private String status = "Awaiting Start";
    private List<Enemy> enemies = new ArrayList<Enemy>();


    /**
     *
     * Construct wave according to input file
     *
     * @param map  map file
     * @param wave_list  a list of wave that constructed
     * @param player player entity that plays the game
     * @param waveNum current wave number
     */
    public Wave(TiledMap map, List<String> wave_list, Player player, int waveNum) {
        this.waveStarted = false;
        //construct each event in a new wave:
        this.events = new ArrayList<>();
        this.wave_list = wave_list;
        this.map = map;
        this.player = player;
        // get the range of corresponding event
        for (int i = 0; i <wave_list.size(); i++) {
            //if event is for current wave number, add to our event list:
            if (Integer.parseInt(Arrays.asList(wave_list.get(i).split(",")).get(0)) == waveNum){
                //set up event index range
                maxEventIndex = Integer.max(i, maxEventIndex);
                if (minEventIndex == -1){
                    minEventIndex = i;
                }
            }
            //save computation time, break if event is in next wave
            else if (Integer.parseInt(Arrays.asList(wave_list.get(i).split(",")).get(0)) > waveNum){
                break;
            }
        }
        //add events from minEvent Index
        this.currIndex = minEventIndex;
    }


    /**return true if it's still on going, false if current wave is over.
     *
     * @param input key/mouse
     * @return true if it's still in current wave, else the wave is oever
     */
    protected boolean update(Input input) {
        // Handle wave start and update status.
        if (input.wasPressed(ShadowDefend.START_WAVE) && !waveStarted) {
            waveStarted = true;
            this.status = "Wave In Progress";
        }
        // Check if it is time to start a new wave event after previous one complete
        if (waveStarted && currIndex <= maxEventIndex && lastEventFinished) {
            events.add(new WaveEvent(this.map, this.player, wave_list.get(currIndex)));
            currIndex += 1;
        }

        //check if we finish wave event and pudate them
        checkAndUpdateEvent(input);

        //check if we got all wave events
        if (currIndex != maxEventIndex+1){
            waveFinished = false;
        }

        //if finish current wave, no enemy inside and last event is finished, and S has been pressed
        // reset wave status and return false, move on to the next wave and reset status
        if (lastEventFinished && waveFinished && waveStarted){
            this.status = "Awaiting Start";
            return false;
        }
        return true;
    }


    /**
     * Update all events and check if last event is finished
     *
     * @param input keyboard input
     */
    private void checkAndUpdateEvent(Input input) {
        waveFinished = true;
        for (WaveEvent e : events) {
            e.update(input);
            lastEventFinished = false;
            //if prev event finished
            if (e.isFinished()) {
                lastEventFinished = true;
            }
            //if enemy]ies left on screen, wave still not finished
            if (!e.isEmpty()) {
                waveFinished = false;
            }
        }
    }


    //getters:
    public String getStatus() {
        return status;
    }

    public List<WaveEvent> getEvents(){
        return events;
    }
}
