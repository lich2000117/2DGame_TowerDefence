import bagel.Input;
import bagel.util.Point;

import java.util.List;

/**
 * A tower collection
 */

public abstract class Tower extends Sprite{

    private int damage;
    private int cost;
    private int radius;
    private boolean lockedOnEnemy;
    private Enemy target;
    private double coolDownCount;
    private double coolDownDelay;
    private static final String TANK = "TANK";
    private static final String SUPERTANK = "SUPERTANK";
    private String NAME;

    /**
     * Create a new Tower
     *
     * @param point point spawned
     * @param imageSrc image path
     * @param damage damage that it can cause
     * @param cost cost of deployment
     * @param radius radius of damage
     * @param coolDownDelay delay of cooling down
     */
    public Tower(Point point, String imageSrc, int damage, int cost, int radius, double coolDownDelay) {
        super(point, imageSrc, damage);
        this.cost = cost;
        this.radius = radius;
        this.damage = damage;
        this.coolDownDelay = coolDownDelay/1000;
    }

    /**
     * update method
     *
     * @param input keyboard input
     * @param projectiles lists of projectiles launched
     * @param towerName identification of tower
     */
    public void update(Input input, List<Projectile> projectiles, String towerName) {
        coolDownCount += ShadowDefend.getTimescale();

        //if doesn't have a target for our tower, set one.
        if (target != null) {
            Projectile newProjectile = getProjectileType(towerName);
            this.lockedOnEnemy = true;
            setAngle(Math.atan2(target.getCenter().y - this.getCenter().y, target.getCenter().x - this.getCenter().x) + Math.PI / 2);
            super.update(input);
            //if cooldown countdown is finished
            if (coolDownCount / ShadowDefend.FPS >= coolDownDelay) {
                //locked on an enemy, aim towards him
                projectiles.add(newProjectile);
                coolDownCount = 0;
            }

            //update each projectile
            for (int i = projectiles.size() - 1; i >= 0; i--) {
                Projectile p = projectiles.get(i);
                p.update(input);
                //if projectile hits or enemy is already died
                if (p.isFinished()) {
                    projectiles.remove(i);
                    target.deductHealth(damage);
                    if (target.isKilled()) {
                        this.lockedOnEnemy = false;
                        this.target = null;
                    }
                    return;
                }
            }

            //if out of range, don't have enemy any more
            if ((target.getCenter().distanceTo(this.getCenter())>this.getRadius())) {
                this.lockedOnEnemy = false;
                this.target = null;
            }

        }
        //double render to avoid framerate drop.
        super.update(input);
    }

    /**
     * Check if current tower has target
     * @return status of targeting
     */
    public boolean hasTarget() {
         return lockedOnEnemy;
    }

    /**
     * Set an enemy as target
     * @param enemy enemy entity
     */
    public void lockEnemy(Enemy enemy){
        this.target = enemy;
    }


    /**
     * get the particular projectile (tank or super tank) by their identification string
     *
     * @param towerName the identification of tower
     * @return a specified type of projectile
     */
    private Projectile getProjectileType(String towerName) {
        if (towerName == TANK) {
            return new TankProjectile(this.getCenter(), target);
        } else if (towerName == SUPERTANK) {
            return new SuperProjectile(this.getCenter(), target);
        }
        else {
            throw new ArithmeticException("unknown tower type, unknown projectile");
        }
    }

    /**
     * Getters
     *
     */
    public int getCost(){
        return cost;
    }

    public int getRadius() {
        return radius;
    }

    public String getNAME() {
        return NAME;
    }
}
