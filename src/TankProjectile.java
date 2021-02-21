import bagel.Input;
import bagel.util.Point;

/**
 * A normal Tank Projectile
 */
public class TankProjectile extends Projectile {

    private static final String IMAGE_FILE = "/images/tank_projectile.png";
    private static final int DAMAGE = 1;
    private static final double SPEED = 10;

    /**
     * Creates a new Tank Projectile
     *
     */
    public TankProjectile(Point point, Enemy enemy) {
        super(point, enemy, IMAGE_FILE, DAMAGE, SPEED);
    }

    @Override
    public void update(Input input) {
    super.update(input);
    }

}
