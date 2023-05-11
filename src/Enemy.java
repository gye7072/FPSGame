import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Enemy extends Rectangle{

    private int dx;
    private int dy;

    private ArrayList<Bullet> b2 = new ArrayList<Bullet>();
    private Color color;
    private boolean shoot;

    long timer = -1;
    private BufferedImage image;
    public Enemy(Color color, int x, int y, int width, int height, int dx, int dy) {
        setBounds(x,y,width,height);
        this.color = color;
        this.dx = dx;
        this.dy = dy;
        setDx(-5);
        shoot = true;
        try {
            image = ImageIO.read(getClass().getResource("enemy.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void tick(){

        this.x += dx;
        this.y += dy;
        if(timer == -1){
            timer = System.currentTimeMillis();
        }
        if(System.currentTimeMillis() - timer == 1000){
            int random = (int) ((Math.random() * 10) + 1);
            if(random == 1) {
                shoot = true;
            }
        }
        if(System.currentTimeMillis() - timer > 1000){
            timer = -1;
            shoot = false;
        }
    }
    public void draw(Graphics g) {
        g.drawImage(image, this.x, this.y, this.width, this.height,null);
        if(shoot){
            Bullet bullet = new Bullet(Color.GREEN, this.x + this.width / 2, this.y + this.height / 2, 5, 5, (this.dx * 2), 0);
            b2.add(bullet);
            shoot = false;
        }

    }

    public ArrayList<Bullet> getB2(){
        return b2;
    }

    public void setDx(int dx){
        this.dx = dx;
    }

    public void addDx(int dx){
        this.dx += dx;
    }

    public int getDx(){
        return dx;
    }

    public void setDy(int dy){
        this.dy = dy;
    }

    public void update(){

    }

}