import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Player extends Rectangle{
    private int lives;
    private int dx;
    private int dy;
    private boolean hit;
    private BufferedImage image;

    long hitTimer = -1;

    public Player(int x, int y, int width, int height, int dx, int dy) {
        setBounds(x,y,width,height);
        this.dx = dx;
        this.dy = dy;
        try {
            image = ImageIO.read(getClass().getResource("spaceship.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tick() {
        this.x += dx;
        this.y += dy;
    }


    public void draw(Graphics g) {
        if(hit) {
            if (hitTimer == -1) {
                hitTimer = System.currentTimeMillis();
            } else if(System.currentTimeMillis() - hitTimer > 200){
                hitTimer = -1;
                hit = false;
                try {
                    image = ImageIO.read(getClass().getResource("spaceship.png"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (System.currentTimeMillis() - hitTimer < 200) {
                try {
                    image = ImageIO.read(getClass().getResource("hit.png"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        g.drawImage(image, this.x, this.y, this.width, this.height, null);
    }

    public void setDx(int dx){
        this.dx = dx;
    }

    public void setDy(int dy){
        this.dy = dy;
    }

    public void setHit(boolean i){
        hit = i;
    }

    public int getLives(){
        return lives;
    }
    public void lostLife(){
        lives--;
    }

    public void addLife(int i){
        lives += i;
    }

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
