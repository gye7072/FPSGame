import java.awt.*;
import java.awt.Graphics;
import java.util.ArrayList;
public class Bullet extends Rectangle{

    private Color color;
    public int x;
    public int y;
    private int dx;
    private int dy;
    private int width;
    private int height;

    public Bullet(Color color, int x, int y, int width, int height, int dx, int dy) {
        this.color = color;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.dx =dx;
        this.dy = dy;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x,y,width,height);
    }

    public void tick(){
        this.x += dx;
        this.y += dy;
    }


    public void setDx(int dx){
        this.dx = dx;
    }



}