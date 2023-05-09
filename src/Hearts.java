import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Hearts {
    private BufferedImage image;
    private boolean dropped;
    private int x;
    private int y;


    public Hearts(int lifePoints) {
        try {
            image = ImageIO.read(getClass().getResource("heart.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void spawnHearts(int x, int y) {
        int random = (int) ((Math.random() * 10) + 1);
        if(random == 1){
            this.x = x;
            this.y = y;
            dropped = true;
        }
    }

//    public void draw(Graphics g){
//        if(dropped) {
//            g.drawImage(g.drawImage(image,x,y,10,10,null);
//        }
//    }
}
