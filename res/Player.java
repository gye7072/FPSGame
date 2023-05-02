import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Player extends Rectangle{
    private int lives;
    private int dx;
    private int dy;
    private BufferedImage image;


    public Player(int x, int y, int width, int height, int dx, int dy) {
        setBounds(x,y,width,height);
        this.dx = dx;
        this.dy = dy;
        lives = 3;
        try {
            image = ImageIO.read(getClass().getResource("spaceship.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tick(){
        this.x += dx;
        this.y += dy;
    }


    public void draw(Graphics g) {
        g.drawImage(image, this.x, this.y, this.width, this.height, null);
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
//
//    //check collison with enemy bullets
//    public void checkCollisonWithBullets(ArrayList<Bullet> bullets) {
//        for (Bullet bullet : bullets) {
//            if (this.intersects(bullet)) {
//                // player is hit, lose a life
//                lives--;
//                // remove the bullet
//                bullets.remove(bullet);
//                break;
//            }
//        }
//    }

    public void setLives(int i){
        lives = i;
    }


    public void update() {
        if (y <= 0) {
            y = 0;
        }
        if (y >= (GamePanel.PANEL_HEIGHT - height)) {
            y = (GamePanel.PANEL_HEIGHT - height);
        }
        if (x <= 0) {
            x = 0;
        }
        if (x >= (GamePanel.PANEL_WIDTH - width)) {
            x = GamePanel.PANEL_WIDTH - width;
        }
    }

}
