import bagel.Input;
import bagel.util.Point;
import bagel.util.Vector2;

/**
 * A projectile class
 */

public abstract class Projectile extends Sprite{

    private Enemy enemy;
    private boolean finished = false;
    private double speed;
    private Point currentPoint;     //current point if someone died here
    private Point targetPoint;

    /**
     * Creates a new projectile
     *
     *
     * @param imageSrc The image which will be rendered at the entity's point
     * @param damage
     */
    public Projectile(Point fromPoint, Enemy enemy, String imageSrc, int damage, double speed) {
        super(fromPoint, imageSrc, damage);
        this.speed = speed;
        this.enemy = enemy;
    }

    /**
     * for Projectile's movement, Reusable code, derived from sprite class
     *
     */
    @Override
    public void update(Input input){
        super.update(input);
        if (finished) {
            return;
        }
        // Obtain where we currently are, and where we want to be
        currentPoint = getCenter();
        targetPoint = enemy.getCenter();
        // Convert them to vectors to perform some very basic vector math
        Vector2 target = targetPoint.asVector();
        Vector2 current = currentPoint.asVector();
        Vector2 distance = target.sub(current);
        // Check if we have reached the enemy or enemy is finished, not existed any more
        if (enemy.isKilled() || enemy.isFinished() || enemy.getRect().intersects(currentPoint)){
            finished = true;
            return;
        }
        super.move(distance.normalised().mul(speed * ShadowDefend.getTimescale()));
    }

    /**
     * check if current projectile hits
     * @return return true if reach the enemy
     */
    public boolean isFinished() {
        return finished;
    }
}
