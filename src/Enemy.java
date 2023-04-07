import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * AlienManager class:
 * Manages a group of aliens by spawning them in,
 * it moves them gradually down the screen moving left
 * to right.
 */
public class Enemy extends Rectangle{
    /**
     * List of aliens that are currently spawned in and alive.
     */
    private int dx;
    private int dy;
    private Color color;
    private ArrayList<Bullet> b2 = new ArrayList<Bullet>();
    private final int MOVE_SPEED = 5;

    public Enemy(Color color, int x, int y, int width, int height, int dx, int dy) {
        setBounds(x,y,width,height);
        this.color = color;
        this.dx = dx;
        this.dy = dy;
    }


    public void tick(){
        this.x += dx;
        this.y += dy;
        setDx(-1);
        // add some probability for the enemy to shoot
        shoot();
    }
    /**
     * Draws the player's circle on the specified Graphics object.
     * @param g The Graphics object used for rendering
     */
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(this.x, this.y, this.width, this.height);
    }

    public void shoot() {
        Bullet bullet = new Bullet(Color.BLACK, this.x + this.width / 2, this.y + this.height / 2, 5, 5, 0, 0, 1);
        b2.add(bullet);
        bullet.setDx(5);
    }

    public void setDx(int dx){
        this.dx = dx;
    }

    public void setDy(int dy){
        this.dy = dy;
    }

    public void update(){

    }

}