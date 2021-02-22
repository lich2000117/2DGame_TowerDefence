import bagel.Input;
import bagel.util.Point;
import java.util.List;

/**
 * A regular slicer.
 */
public class Slicer extends Enemy {

    private static final String IMAGE_FILE = Sprite.getCurPath() + "res/images/slicer.png";
    private static final double SPEED = 1;
    private static final int DAMAGE = 1;
    private static final int REWARD = 2;
    private static final int HEALTH = 1;

    /**
     * Creates a new Slicer
     *
     * @param polyline The polyline that the slicer must traverse (must have at least 1 point)
     */
    public Slicer(List<Point> polyline, Point spawnPoint) {
        super(polyline, spawnPoint, IMAGE_FILE, DAMAGE, SPEED, REWARD, HEALTH);
    }

    /**
     * Updates the current state of the slicer. The slicer moves towards its next target point in
     * the polyline at its specified movement rate.
     */
    @Override
    public void update(Input input) {
        super.update(input);
    }

}
