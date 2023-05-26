import java.awt.*;
import java.util.ArrayList;

public class EnemyController {
    private static ArrayList<Enemy> e = new ArrayList<Enemy>();

    private int enemyCount;
    private int enemyKilled;
    private long lastSpawnTime;
    private long spawnDelay;
    private int enemySpeed;
    private boolean spawnEnabled;
    private int numSpawned;

    
    public EnemyController(int enemyCount, int enemyKilled) {
        this.enemyCount = enemyCount;
        this.enemyKilled = enemyKilled;
        this.spawnEnabled = true;
        this.lastSpawnTime = System.currentTimeMillis();
        enemySpeed = 0;
        this.spawnDelay = 2000;
    }

    public ArrayList<Enemy> getEnemyList() {
        return e;
    }

    public void spawnEnemies() {
        if(spawnEnabled) {
            if (!GamePanel.gameOver && (System.currentTimeMillis() - lastSpawnTime >= spawnDelay) && (numSpawned < enemyCount)) {
                lastSpawnTime = System.currentTimeMillis();
                int y = (int) (Math.random() * (GamePanel.PANEL_HEIGHT - 50));
                Enemy enemy = new Enemy(Color.GREEN, GamePanel.PANEL_WIDTH - 50, y, 50,50, GamePanel.waveNumber, 0);
                enemy.addDx(enemySpeed);
                e.add(enemy);
                numSpawned++;
            }
        }

        if (!GamePanel.gameOver && (enemyKilled == enemyCount)) {
            enemyCount++;
            enemyKilled = 0;
            numSpawned = 0;
            if(enemySpeed != 15) {
                enemySpeed -= 1;
            }
            if(GamePanel.waveNumber == 10){
                if(enemySpeed != 15) {
                    enemySpeed -= 1;
                }
            }
            if(spawnDelay != 500) {
                spawnDelay -= 100;
            }
            e.clear();
        }
    }
    public int getEnemySpeed(){
        return enemySpeed;
    }

    public void setSpawnEnabled(boolean b){
        spawnEnabled = b;
    }

    public void setEnemyDead(int i) {
        e.get(i).isDead(true);
    }
    public void removeEnemy(int i) {
        e.remove(i);
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
        if (!GamePanel.gameOver) {
            spawnEnemies();
        }
        if (!e.isEmpty()) {
            for (int i = 0; i < e.size(); i++) {
                e.get(i).tick();
            }
        }
    }

    public void draw(Graphics g) {
        if (!GamePanel.gameOver) {
            spawnEnemies();
        }
        if (!e.isEmpty() || !GamePanel.waveOver) {
            for (int i = 0; i < e.size(); i++) {
                e.get(i).draw(g);
            }
        }
    }
}
