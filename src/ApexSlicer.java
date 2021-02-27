import bagel.Input;
import bagel.util.Point;
import java.util.List;

/**
 * An Apex slicer.
 */
public class ApexSlicer extends Enemy {

    private static final String IMAGE_FILE = Sprite.getCurPath() + "res/images/apexslicer.png";
    private static final double SPEED = 0.375;
    private static final int DAMAGE = 10;
    private static final int REWARD = 10;
    private static final int HEALTH = 200;
    private static final int KIDSNUM = 4;
    private List<Point> polyline;

    /**
     * Create ApexSlicer
     *
     * @param polyline traversal line
     * @param spawnPoint point of the spawn location
     */
    public ApexSlicer(List<Point> polyline, Point spawnPoint) {
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
            enemies.add(new SuperSlicer(polyline.subList(this.getCurrentIndex(), polyline.size()),this.getCenter()));
        }

    }
}
