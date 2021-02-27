import bagel.Input;
import bagel.util.Point;

/**
 * A normal Tank Projectile
 */
public class TankProjectile extends Projectile {

    private static final String IMAGE_FILE = Sprite.getCurPath() + "res/images/tank_projectile.png";

    /**
     * Creates a new Tank Projectile
     *
     */
    public TankProjectile(Point point, Enemy enemy) {
        super("",point, enemy, IMAGE_FILE);
    }

    @Override
    public void update(Input input) {
    super.update(input);
    }

}
