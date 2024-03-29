import bagel.DrawOptions;
import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Vector2;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Represents a game entity
 */
public abstract class Sprite {

    private final Image image;
    private Rectangle rect;
    private double angle;
    private int damage;
    private String NAME;


    /**
     * Creates a new Sprite (game entity)
     *
     * @param point    The starting point for the entity
     * @param imageSrc The image which will be rendered at the entity's point
     */
    public Sprite(String NAME, Point point, String imageSrc, int damage) {
        this.image = new Image(imageSrc);
        this.rect = image.getBoundingBoxAt(point);
        this.angle = 0;
        this.damage = damage;
        this.NAME = NAME;
    }

    //private static File f = new File(System.getProperty("java.class.path"));
    //private static File dir = f.getAbsoluteFile().getParentFile();
    //private static String curPath = f.toString();


    public static String getCurPath(){
        String curPath = "";
        {
            try {
                curPath = new File(Sprite.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().getPath();
                //System.out.println(curPath);
            } catch (URISyntaxException e) {
                System.out.println(e);
            }
        }
        return curPath + File.separator;
    }


    public Rectangle getRect() {
        return new Rectangle(rect);
    }

    /**
     * Moves the Sprite by a specified delta
     *
     * @param dx The move delta vector
     */
    public void move(Vector2 dx) {
        rect.moveTo(rect.topLeft().asVector().add(dx).asPoint());
    }

    public Point getCenter() {
        return getRect().centre();
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public int getDamage() {
        return damage;
    }

    public void updateCenter(Point point){
        this.rect = this.image.getBoundingBoxAt(point);
    }

    public Image getImage() {
        return image;
    }

    public String getNAME() {
        return NAME;
    }

    /**
     * Updates the Sprite. Default behaviour is to render the Sprite at its current position.
     */
    public void update(Input input) {
        image.draw(getCenter().x, getCenter().y, new DrawOptions().setRotation(angle));
    }

}