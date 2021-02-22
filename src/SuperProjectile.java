import bagel.Input;
import bagel.util.Point;

/**
 * A Super Tank Projectile
 */
public class SuperProjectile extends Projectile {

    private static final String IMAGE_FILE = Sprite.getCurPath() + "res/images/supertank_projectile.png";
    private static final int DAMAGE = 3;
    private static final double SPEED = 10;

    /**
     * Creates a new SuperTank Projectile
     *
     */
    public SuperProjectile(Point point, Enemy enemy) {
        super(point, enemy, IMAGE_FILE, DAMAGE, SPEED);
    }

    @Override
    public void update(Input input) {
        super.update(input);
    }

}
