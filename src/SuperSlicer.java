import bagel.Input;
import bagel.util.Point;
import bagel.util.Vector2;
import java.util.List;

/**
 * A Super slicer.
 */
public class SuperSlicer extends Enemy {

    private static final String IMAGE_FILE = "res/images/superslicer.png";
    private static final double SPEED = 0.75;
    private static final int DAMAGE = 2;
    private static final int REWARD = 15;
    private static final int HEALTH = 1;
    private static final int KIDSNUM = 2;
    private List<Point> polyline;

    /**
     * Creates a new Super Slicer
     *
     * @param polyline The polyline that the slicer must traverse (must have at least 1 point)
     */
    public SuperSlicer(List<Point> polyline, Point spawnPoint) {
        super(polyline, spawnPoint, IMAGE_FILE, DAMAGE, SPEED, REWARD, HEALTH);
        this.polyline = polyline;
    }

    /**
     * Updates the current state of the slicer. The slicer moves towards its next target point in
     * the polyline at its specified movement rate.
     */
    @Override
    public void update(Input input) {
        super.update(input);
    }

    /**
     * Spawn enemy if died
     * @param enemies whole enemy list
     */
    @Override
    public void spawnKids(List<Enemy> enemies){
        for (int i = 0; i < KIDSNUM; i++){
            enemies.add(new Slicer(polyline.subList(this.getCurrentIndex(), polyline.size()),this.getCenter()));
        }

    }
}
