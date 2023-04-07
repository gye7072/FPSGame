import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Player extends Rectangle{

    private static final int SPEED = 5;
    private int lives;
    private Color color;
    private int dx;
    private int dy;

    /**
     * Constructs a new instance of the Player class.
     * @param color The color of the player's rectangle
     * @param x The x-coordinate of the player's rectangle
     * @param y The y-coordinate of the player's rectangle
     * @param dx The change in x-coordinate of the player's rectangle
     * @param dy The change in y-coordinate of the player's rectangle
     */
    public Player(Color color, int x, int y, int width, int height, int dx, int dy) {
        setBounds(x,y,width,height);
        this.color = color;
        this.dx = dx;
        this.dy = dy;
        lives = 3;
    }

    public void tick(){
        this.x += dx;
        this.y += dy;
    }
    /**
     * Draws the player's circle on the specified Graphics object.
     * @param g The Graphics object used for rendering
     */
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(this.x, this.y, this.width, this.height);
    }
    public void setColor(Color c){
        color = c;
    }
    public void setDx(int dx){
        this.dx = dx;
    }

    public void setDy(int dy){
        this.dy = dy;
    }

    public int getLives(){
        return lives;
    }

    //check collison with enenmyBullets
    public void checkCollisonWithBullets(ArrayList<Bullet> bullets) {
        for (Bullet bullet : bullets) {
            if (this.intersects(bullet)) {
                // player is hit, lose a life
                lives--;
                // remove the bullet
                bullets.remove(bullet);
                break;
            }
        }
    }



        /**
         * Updates the player's position based on user input.
         */
    public void update() {
        if (y <= 0) {
            y = 0;
        }
        if (y >= (GamePanel.PANEL_HEIGHT - width)) {
            y = (GamePanel.PANEL_HEIGHT - width);
        }
        if (x <= 0) {
            x = 0;
        }
        if (x >= (GamePanel.PANEL_WIDTH - width)) {
            x = GamePanel.PANEL_WIDTH - width;
        }
    }



}
