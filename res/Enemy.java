import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Enemy extends Rectangle{

    private int dx;
    private int dy;
    private Color color;
    private ArrayList<Bullet> b2 = new ArrayList<Bullet>();

    private BufferedImage image;
    public Enemy(Color color, int x, int y, int width, int height, int dx, int dy) {
        setBounds(x,y,width,height);
        this.color = color;
        this.dx = dx;
        this.dy = dy;
        setDx(-5);
    }


    public void tick(){
        this.x += dx;
        this.y += dy;
//        // add some probability for the enemy to shoot
//        shoot();
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(this.x, this.y, this.width, this.height);
    }

//    public void shoot() {
//        Bullet bullet = new Bullet(Color.BLACK, this.x + this.width / 2, this.y + this.height / 2, 5, 5, 0, 0);
//        b2.add(bullet);
//        bullet.setDx(5);
//    }

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