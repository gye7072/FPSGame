import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
public class EnemyController {
    private static ArrayList<Enemy> e = new ArrayList<Enemy>();

    private int enemyCount;
    private int enemyKilled;
    private static int waveNumber;
    private boolean waveOver;

    public EnemyController(int enemyCount, int enemyKilled) {
        this.enemyCount = enemyCount;
        this.enemyKilled = enemyKilled;
        this.waveOver = false;

    }

    public ArrayList<Enemy> getEnemyList() {
        return e;
    }

    public void spawnEnemies() {
        if (enemyKilled == enemyCount) {
            enemyCount++;
            waveNumber++;
            enemyKilled = 0;
            e.clear();

        }


        for (int i = 0; i < enemyCount; i++) {
            int y = (int) (Math.random() * (GamePanel.PANEL_HEIGHT - 50));
            Enemy enemy = new Enemy(Color.GREEN, GamePanel.PANEL_WIDTH-50, y, 25, 25, 0, 0);
            e.add(enemy);
        }
    }


    public void removeEnemy(int i) {
        e.remove(i);
    }

    public void setWaveOver(boolean b){
        waveOver = b;
    }

    public int getEnemyCount() {
        return enemyCount;
    }

    public int getEnemyKilled() {
        return enemyKilled;
    }

    public void addEnemyKilled() {
        enemyKilled++;
    }

    public void tick() {
        if (e.isEmpty()) {
            spawnEnemies();
        } else {
            for (int i = 0; i < enemyCount-1-enemyKilled; i++) {
                e.get(i).tick();
            }
        }
    }
    public void draw(Graphics g) {
        if (e.isEmpty() && !waveOver) {
            spawnEnemies();
        } else {
            for (int i = 0; i < enemyCount-1-enemyKilled; i++) {
                e.get(i).draw(g);
            }
        }
    }
}
