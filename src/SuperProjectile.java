import bagel.Input;
import bagel.util.Point;

/**
 * A Super Tank Projectile
 */
public class SuperProjectile extends Projectile {

    private static final String IMAGE_FILE = Sprite.getCurPath() + "res/images/supertank_projectile.png";

    /**
     * Creates a new SuperTank Projectile
     *
     */
    public SuperProjectile(Point point, Enemy enemy) {
        super("",point, enemy, IMAGE_FILE);
    }

    @Override
    public void update(Input input) {
        super.update(input);
    }

}
