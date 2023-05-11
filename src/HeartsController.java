import java.awt.*;
import java.util.ArrayList;

public class HeartsController {
    private ArrayList<Bullet> bullets;
    private ArrayList<Hearts> h1 = new ArrayList<Hearts>();
    ArrayList<Enemy> enemies;
    private Player player;

    private Hearts hearts;

    private boolean dropped;

    long timer = -1;

    public HeartsController(ArrayList<Enemy> enemies, ArrayList<Bullet> bullets, Player player) {
        this.enemies = enemies;
        this.bullets = bullets;
        this.player = player;
    }


    public void spawnHearts(Graphics g, int x, int y) {
        int random = (int) ((Math.random() * 10) + 1);
        if (random == 1) {
            hearts = new Hearts(1,x,y);
            h1.add(hearts);
            dropped = true;
        }
    }

    public void draw(Graphics g){
        if(dropped) {
            for(int i =0; i < h1.size(); i++) {
                hearts = h1.get(i);
                if (hearts.getSpawnTime() < 3000) {
                    hearts.draw(g);
                    if (hearts != null && hearts.getX() >= player.x && hearts.getX() <= player.x + player.width &&
                            hearts.getY() >= player.y && hearts.getY() <= player.y + player.height) {
                        player.addLife(hearts.getLifePoints());
                        h1.remove(hearts);
                        dropped = false;
                    }
                }
            }
            if (hearts.getSpawnTime() > 3000) {
                timer = -1;
                h1.remove(hearts);
                dropped = false;
            }
        }
    }

    public void setDropped(boolean b){
        dropped = b;
    }
}
