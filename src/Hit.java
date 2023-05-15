import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Hit{
    private int x;
    private int y;
    private boolean hit;
    private int width;
    private int height;
    private BufferedImage image;
    long hitTimer;


    public Hit(int x, int y, int width, int height) {;
        this.x = x;
        this.y = y;
        hitTimer = -1;
        this.width = width;
        this.height = height;
        try {
            image = ImageIO.read(getClass().getResource("hit.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g) {
        if (hitTimer == -1) {
            hitTimer = System.currentTimeMillis();
        }
        if (System.currentTimeMillis() - hitTimer <= 100) {
            g.drawImage(image, this.x, this.y, this.width, this.height, null);
            System.out.println();
        }
        if (System.currentTimeMillis() - hitTimer > 100) {
            hitTimer = -1;
            hit = false;
        }
    }

    public void setHit(boolean i){
        hit = i;
    }

    public boolean getHit(){
        return hit;
    }
}
