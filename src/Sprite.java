import bagel.DrawOptions;
import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Vector2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represents a game entity
 */
public abstract class Sprite {

    private final Image image;
    private Rectangle rect;
    private double angle;
    private int damage;

    /**
     * Creates a new Sprite (game entity)
     *
     * @param point    The starting point for the entity
     * @param imageSrc The image which will be rendered at the entity's point
     *
     */
    public Sprite(Point point, String imageSrc, int damage) {
        InputStream inputStream = getClass().getResourceAsStream(imageSrc);
        String tempDir = System.getProperty("java.io.tmpdir");
        File temp = new File(tempDir+File.separator+"tmp.txt");
        String imgPath = "";
        {
            try {
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                OutputStream outStream = new FileOutputStream(temp);
                outStream.write(buffer);
                imgPath = temp.getAbsolutePath();
            }
            catch (Exception e) {
                // 如果有错误输出内容
                e.printStackTrace();
            }
        }

        this.image = new Image(imgPath);
        this.rect = image.getBoundingBoxAt(point);
        this.angle = 0;
        this.damage = damage;
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

    /**
     * Updates the Sprite. Default behaviour is to render the Sprite at its current position.
     */
    public void update(Input input) {
        image.draw(getCenter().x, getCenter().y, new DrawOptions().setRotation(angle));
    }

}