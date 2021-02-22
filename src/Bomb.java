import bagel.Input;
import bagel.util.Point;

/**
 * A bomb class
 */

public class Bomb extends Sprite{

    private static final String IMAGE_FILE = Sprite.getCurPath() + "res/images/explosive.png";
    private static final int DAMAGE = 500;
    private static final double DETONATEDELAY = 2.0;
    private static final int RADIUS = 200;
    private double detonateCountDown;
    private boolean detonated = false;

    /**
     * Creates a new Bomb
     *
     * @param point    The starting point for the entity
     */
    public Bomb(Point point) {
        super(point, IMAGE_FILE, DAMAGE);
    }

    @Override
    public void update(Input input) {
        super.update(input);
        detonateCountDown += ShadowDefend.getTimescale();
        //if reach the time delay, set detonated to true and return, disappear
        if (detonateCountDown / ShadowDefend.FPS >= DETONATEDELAY) {
            this.detonated = true;
            detonateCountDown = 0;
            return;
        }

    }

    /**
     * return detonated status
     * @return status
     */
    public boolean isDetonated() {
        return detonated;
    }

    //getter
    public static int getRADIUS() {
        return RADIUS;
    }
}
