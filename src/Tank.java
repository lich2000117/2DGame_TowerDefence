import bagel.Input;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * A normal tank
 */

public class Tank extends Tower{

    private static final String IMAGE_FILE = Sprite.getCurPath() + "res/images/tank.png";
    private static final int DAMAGE = 1;
    private static final int COST = 250;
    private static final int RADIUS = 100;
    private static final Double COOLDOWN = 1000.0;
    private static final String NAME = "TANK";
    private List<Projectile> projectiles = new ArrayList<Projectile>();

    /**
     * Creates a new tank
     *
     * @param point    The starting point for the entity
     *
     */
    public Tank(Point point) {
        super(point, IMAGE_FILE, DAMAGE, COST, RADIUS, COOLDOWN);
    }

    @Override
    public void update(Input input) {
        super.update(input,projectiles,NAME);
    }
}
