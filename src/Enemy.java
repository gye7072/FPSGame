import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Enemy extends Rectangle{

    private int dx;
    private int dy;

    private ArrayList<Bullet> enemyBullets = new ArrayList<Bullet>();
    private Color color;
    private boolean shoot;
    private boolean isDead;

    long shootTimer = -1;
    long shootInterval;
    private BufferedImage image;
    public Enemy(Color color, int x, int y, int width, int height, int dx, int dy) {
        setBounds(x,y,width,height);
        this.color = color;
        this.dx = dx;
        this.dy = dy;
        setDx(-3);
        shoot = true;
        shootInterval = 2000;
        try {
            image = ImageIO.read(getClass().getResource("enemy.png"));
        } catch (Exception e) {
            System.out.println("Failed to set enemy image " + e.getMessage());
        }

    }


    public void tick() {
        if(GamePanel.waveOver && shootInterval > 1000){
            shootInterval -= 50;
        }
        this.x += dx;
        this.y += dy;
        if(!isDead) {
            if (shootTimer == -1) {
                shootTimer = System.currentTimeMillis();
            }

            if (System.currentTimeMillis() - shootTimer >= shootInterval) {
                shoot = true;
                shootTimer = System.currentTimeMillis();
            }

            if (System.currentTimeMillis() - shootTimer > shootInterval) {
                shootTimer = -1;
                shoot = false;
            }
        }
    }


    public void draw(Graphics g) {
        if(!isDead){
            g.drawImage(image, this.x, this.y, this.width, this.height,null);
        }
        if(shoot){
            Bullet bullet = new Bullet(Color.GREEN, this.x + this.width / 2, this.y + this.height / 2, 5, 5, (this.dx * 2), 0);
            enemyBullets.add(bullet);
            shoot = false;
        }

    }

    public ArrayList<Bullet> getEnemyBullets(){
        return enemyBullets;
    }

    public void isDead(boolean i){
        isDead = i;
    }

    public boolean getIsDead(){
        return isDead;
    }

    public void setDx(int dx){
        this.dx = dx;
    }

    public void addDx(int dx){
        this.dx += dx;
    }

}