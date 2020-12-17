import bagel.Input;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.List;

/**
 * An abstract class that responsible for all slicers' movement function
 */

public abstract class Enemy extends Sprite{

    private final List<Point> polyline;
    private int targetPointIndex;
    private boolean finished = false;
    private double speed;
    private Point currentPoint;     //current point if someone died here
    private boolean isKilled;
    private int reward;
    private int health;

    /**
     * Creates a new Sprite (game entity)
     *
     * @param polyline  traversal line
     * @param spawnPoint the point of spawn
     * @param imageSrc image path
     * @param damage damage it deals
     * @param speed moving speed
     * @param reward moment player will get if destroyed
     * @param health its health
     */
    public Enemy(List<Point> polyline, Point spawnPoint,  String imageSrc, int damage, double speed, int reward, int health) {
        super(spawnPoint, imageSrc, damage);
        this.polyline = polyline;
        if (this.polyline.size()==1){
            this.targetPointIndex = 0;
        }
        else {
            this.targetPointIndex = 1;    //target is at the end of line
        }
        this.speed = speed;
        this.isKilled = false;
        this.reward = reward;
        this.health = health;
    }


    /**
     * for Slicer's movement, Reusable code
     * From rohylj-project-1-master in Gitlab
     *
     */
    @Override
    public void update(Input input){
        super.update(input);
        if (finished) {
            return;
        }
        // Obtain where we currently are, and where we want to be

        currentPoint = getCenter();

        Point targetPoint = polyline.get(targetPointIndex);
        // Convert them to vectors to perform some very basic vector math
        Vector2 target = targetPoint.asVector();
        Vector2 current = currentPoint.asVector();
        Vector2 distance = target.sub(current);
        // Distance we are (in pixels) away from our target point
        double magnitude = distance.length();
        // Check if we are close to the target point
        if (magnitude < speed * ShadowDefend.getTimescale()) {
            // Check if we have reached the end
            if (targetPointIndex == polyline.size() - 1) {
                finished = true;
                return;
            } else {
                // Make our focus the next point in the polyline
                targetPointIndex += 1;
            }
        }
        // Move towards the target point
        // We do this by getting a unit vector in the direction of our target, and multiplying it
        // by the speed of the slicer (accounting for the timescale)
        super.move(distance.normalised().mul(speed * ShadowDefend.getTimescale()));
        // Update current rotation angle to face target point
        setAngle(Math.atan2(targetPoint.y - currentPoint.y, targetPoint.x - currentPoint.x));
    }

    /**
     *
     * @return get curren point of a slicer if he dies.
     */
    public int getCurrentIndex() {
        return targetPointIndex;
    }

    /**
     * check if current enemy is arrived at base
     * @return return true if reach the base
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Check if entity is died
     * @return status
     */
    public boolean isKilled(){
        return isKilled;
    }

    /**
     * Set entity's status killed or live
     */
    public void killed(){
        isKilled = true;
    }

    // getter
    public int getReward(){
        return reward;
    }

    /**
     * Deduct entity's health
     * @param damage damage that entity got
     */
    public void deductHealth(int damage){
        this.health -= damage;
        if (health<=0){
            this.killed();
        }
    }

    //method for override
    /**
     * spawn enemies at current location
     * @param enemies list of enemies.
     */
    public void spawnKids(List<Enemy> enemies){
    }

}
