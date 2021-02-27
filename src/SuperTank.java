import bagel.Input;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * A Super tank
 */

public class SuperTank extends Tower{

    private static final String IMAGE_FILE = Sprite.getCurPath() + "res/images/supertank.png";
    private static final int DAMAGE = 2;
    private static final int COST = 400;
    private static final int RADIUS = 130;
    private static final Double COOLDOWN = 700.0;
    private static final String NAME = "SUPERTANK";
    private List<Projectile> projectiles = new ArrayList<Projectile>();

    /**
     * Creates a new tank
     *
     * @param point    The starting point for the entity
     *
     */
    public SuperTank(Point point) {
        super(point, IMAGE_FILE, DAMAGE, COST, RADIUS, COOLDOWN, new MegaTank(point));
    }

    @Override
    public void update(Input input) {
        super.update(input,projectiles,NAME);
    }
}
