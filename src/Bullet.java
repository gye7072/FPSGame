import java.awt.*;
import java.awt.Graphics;
import java.util.ArrayList;
/**
 * The Bullet class represents a bullet fired by a player in the 1v1 shooter game.
 */
public class Bullet extends Rectangle{

    private static final int SPEED = 10;

    private Color color;
    public int x;
    public int y;
    private int dx;
    private int dy;
    private int width;
    private int height;
    private int direction;
    /**
     * Constructs a new instance of the Bullet class.
     * @param color The color of the bullet's circle
     * @param x The x-coordinate of the bullet's circle
     * @param y The y-coordinate of the bullet's circle
     * @param radius The radius of the bullet's circle
     * @param direction The direction of the bullet's movement in radians
     */
    public Bullet(Color color, int x, int y, int width, int height, int dx, int dy, int direction) {
        this.color = color;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.dx =dx;
        this.dy = dy;
        this.direction = direction;
    }

    /**
     * Draws the bullet's circle on the specified Graphics object.
     * @param g The Graphics object used for rendering
     */
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x,y,width,height);
    }

    public void tick(){
        this.x += dx;
        this.y += dy;
//        if(direction == 1){
//            y -= 10;
//        }
//        if(direction == 2){
//            x -= 10;
//        }
//        if(direction == 3){
//            y += 10;
//        }
//        if(direction == 4){
//            x += 10;
//        }
    }

    //protect the base
    //if dies lose 100 hp

    public void setDx(int dx){
        this.dx = dx;
    }

    public void setDy(int dy){
        this.dy = dy;
    }



}