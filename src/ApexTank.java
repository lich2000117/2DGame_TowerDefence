import bagel.Input;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * A normal tank
 */

public class ApexTank extends Tower{

    private static final String IMAGE_FILE = Sprite.getCurPath() + "res/images/apextank.png";
    private static final int DAMAGE = 4;
    private static final int COST = 1500;
    private static final int RADIUS = 180;
    private static final Double COOLDOWN = 350.0;
    private static final String NAME = "SUPERTANK";
    private List<Projectile> projectiles = new ArrayList<Projectile>();

    /**
     * Creates a new tank
     *
     * @param point    The starting point for the entity
     *
     */
    public ApexTank(Point point) {
        super(point, IMAGE_FILE, DAMAGE, COST, RADIUS, COOLDOWN, null);
    }

    @Override
    public void update(Input input) {
        super.update(input,projectiles,NAME);
    }
}
