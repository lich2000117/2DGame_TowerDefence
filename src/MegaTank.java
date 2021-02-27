import bagel.Input;
import bagel.util.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * A mega tank
 */

public class MegaTank extends Tower{

    private static final String IMAGE_FILE = Sprite.getCurPath() + "res/images/megatank.png";
    private static final int DAMAGE = 30;
    private static final int COST = 800;
    private static final int RADIUS = 150;
    private static final Double COOLDOWN = 500.0;
    private static final String NAME = "MEGATANK";
    private List<Projectile> projectiles = new ArrayList<Projectile>();

    /**
     * Creates a new tank
     *
     * @param point    The starting point for the entity
     *
     */
    public MegaTank(Point point) {
        super(NAME, point, IMAGE_FILE, DAMAGE, COST, RADIUS, COOLDOWN, new ApexTank(point));
    }

    @Override
    public void update(Input input) {
        super.update(input,projectiles,NAME);
    }
}
