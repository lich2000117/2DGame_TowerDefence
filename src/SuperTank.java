import bagel.Input;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * A Super tank
 */

public class SuperTank extends Tower{

    private static final String IMAGE_FILE = "/images/supertank.png";
    private static final int DAMAGE = 3;
    private static final int COST = 600;
    private static final int RADIUS = 150;
    private static final Double COOLDOWN = 500.0;
    private static final String NAME = "SUPERTANK";
    private List<Projectile> projectiles = new ArrayList<Projectile>();

    /**
     * Creates a new tank
     *
     * @param point    The starting point for the entity
     *
     */
    public SuperTank(Point point) {
        super(point, IMAGE_FILE, DAMAGE, COST, RADIUS, COOLDOWN);
    }

    @Override
    public void update(Input input) {
        super.update(input,projectiles,NAME);
    }
}
