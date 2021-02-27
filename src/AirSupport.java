import bagel.Input;
import bagel.util.Point;
import bagel.util.Vector2;

import javax.naming.Name;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * An airsupport class
 */

public class AirSupport extends Tower{

    private static final int DAMAGE = 500;
    private static final int SPEED = 3;
    private static final int COST = 500;
    private static final int RADIUS = 200;
    private static final String IMAGE = Sprite.getCurPath() + "res/images/airsupport.png";
    private static boolean direction = true;
    private boolean finishedFly = false;
    private boolean allDetonated = false;
    private final Point targetPoint;
    private Point currentPoint;
    private static final double MINDELAY = 1;
    private double dropDelay = MINDELAY + new Random().nextDouble();
    private List<Bomb> bombs = new ArrayList<Bomb>();
    private double dropCountDown;

    /**
     * Creates a new Sprite (game entity)
     *
     * @param point The starting point for the entity
     *
     */
    public AirSupport(Point point) {
        super("AirSuport",point, IMAGE, DAMAGE, COST, RADIUS, MINDELAY, null);
        if (direction) {
            updateCenter(new Point(0,getCenter().y));
            // plus 10 here to avoid been stuck at the edge of the window
            targetPoint = new Point(ShadowDefend.WIDTH+50, getCenter().y);

        }
        else{
            updateCenter(new Point(getCenter().x,0));
            targetPoint = new Point(getCenter().x,ShadowDefend.HEIGHT);
        }
        currentPoint = getCenter();
        //everytime we create an airplane, its direction changes
        this.direction = !direction;
    }



    @Override
    public void update(Input input) {

        dropCountDown += ShadowDefend.getTimescale();

        //if finish, return;
        if (finishedFly && allDetonated) {
            return;
        }
        //if reach the time delay, and within frame, drop bomb
        if ((dropCountDown / ShadowDefend.FPS >= dropDelay)&&(!finishedFly)) {
            bombs.add(new Bomb(this.getCenter()));
            dropDelay = MINDELAY + new Random().nextDouble();
            dropCountDown = 0;
        }

        //update bombs
        for (int i = bombs.size() - 1; i >= 0; i--) {
            Bomb b = bombs.get(i);
            if (b.isDetonated()){
                bombs.remove(i);
                allDetonated = true;
            }
            else{
                allDetonated = false;
            }
            b.update(input);
        }

        move();
        super.update(input);
    }


    /**
     * check if plane is finished.
     * @return true if plane is finished.
     */
    public boolean isFinished(){
        return (finishedFly && allDetonated);
    }


    /**
     * Move function for plane
     */
    private void move(){
        // Obtain where we currently are, and where we want to be
        currentPoint = getCenter();
        //to vectors to perform some very basic vector math
        Vector2 target = targetPoint.asVector();
        Vector2 current = currentPoint.asVector();
        Vector2 distance = target.sub(current);
        // Distance we are (in pixels) away from our target point
        double magnitude = distance.length();
        // Check if we are close to the target point
        if (magnitude < SPEED * ShadowDefend.getTimescale()) {
            // Check if we have reached the outside window

            if ((current.y >= ShadowDefend.HEIGHT)||(current.x >= ShadowDefend.WIDTH)) {
                finishedFly = true;
                return;
            }
        }
        super.move(distance.normalised().mul(SPEED * ShadowDefend.getTimescale()));
        // Update current rotation angle to face target point
        setAngle(Math.atan2(targetPoint.y - currentPoint.y, targetPoint.x - currentPoint.x)+Math.PI/2);
    }


    //getters
    public List<Bomb> getBombs(){
        return bombs;
    }

    public int getCost() {
        return COST;
    }

}
