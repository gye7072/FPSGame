import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Hearts {
    private BufferedImage image;
    private boolean dropped;
    private int x;
    private int y;
    private int lifePoints;


    public Hearts(int lifePoints, int x, int y) {
        this.x = x;
        this.y = y;
        this.lifePoints = lifePoints;
        try {
            image = ImageIO.read(getClass().getResource("heart.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getLifePoints(){
        return lifePoints;
    }

    public void draw(Graphics g){
        g.drawImage(image,x,y,25,25,null);
    }
}