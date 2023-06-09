import java.awt.*;
import java.util.ArrayList;

public class HeartsController {
    private ArrayList<Hearts> h1 = new ArrayList<Hearts>();
    ArrayList<Enemy> enemies;
    private Player player;

    private Hearts hearts;
    private boolean dropped;
    private Sound sound;

    public HeartsController(ArrayList<Enemy> enemies, Player player) {
        this.enemies = enemies;
        this.player = player;
    }


    public void spawnHearts(int x, int y) {
        int random = (int) ((Math.random() * 3) + 1);
        if (random == 1) {
            hearts = new Hearts(1,x,y);
            h1.add(hearts);
            dropped = true;
        }
    }

    public void draw(Graphics g){
        if(!GamePanel.gameOver) {
            if (dropped) {
                for (int i = 0; i < h1.size(); i++) {
                    hearts = h1.get(i);
                    if (hearts.getSpawnTime() < 3000 && hearts.getSpawnTime() > 200) {
                        hearts.draw(g);
                        if (hearts != null && hearts.getX() >= player.x && hearts.getX() <= player.x + player.width &&
                                hearts.getY() >= player.y && hearts.getY() <= player.y + player.height) {
                            sound = new Sound();
                            sound.setFile(3);
                            sound.play();
                            player.addLife(hearts.getLifePoints());
                            h1.remove(hearts);
                            i--;
                        }
                    }
                }
                if (hearts.getSpawnTime() > 3000) {
                    h1.remove(hearts);
                }
            }
        }
    }

    public ArrayList<Hearts> getH1() {
        return h1;
    }
}
