import java.awt.*;
import java.util.ArrayList;

public class EnemyController {
    private static ArrayList<Enemy> e = new ArrayList<Enemy>();

    private int enemyCount;
    private int enemyKilled;
    private static int waveNumber;
    private boolean waveOver;
    private boolean gameOver;
    private long lastSpawnTime;
    private long spawnDelay;
    private int enemySpeed;
    private boolean spawnEnabled;

    
    public EnemyController(int enemyCount, int enemyKilled) {
        this.enemyCount = enemyCount;
        this.enemyKilled = enemyKilled;
        this.waveOver = false;
        this.gameOver = false;
        this.spawnEnabled = true;
        this.lastSpawnTime = System.currentTimeMillis();
        enemySpeed = 0;
        this.spawnDelay = 2000;
    }

    public ArrayList<Enemy> getEnemyList() {
        return e;
    }

    public void spawnEnemies() {
        if (!gameOver && (enemyKilled == enemyCount)) {
            waveNumber++;
            enemyCount++;
            waveOver = true;
            enemyKilled = 0;
            enemySpeed -= 1;
            if(waveNumber == 10){
                enemySpeed -= 1;
            }
            spawnDelay -= 100;
            e.clear();
        }
        if(spawnEnabled) {
            if (!gameOver && (System.currentTimeMillis() - lastSpawnTime >= spawnDelay) && (enemyKilled < enemyCount)) {
                lastSpawnTime = System.currentTimeMillis();
                int y = (int) (Math.random() * (GamePanel.PANEL_HEIGHT - 50));
                Enemy enemy = new Enemy(Color.GREEN, GamePanel.PANEL_WIDTH - 50, y, 25, 25, waveNumber, 0);
                enemy.addDx(enemySpeed);
                System.out.println(enemy.getDx());
                e.add(enemy);
            }
        }
    }

    public void setSpawnEnabled(boolean b){
        spawnEnabled = b;
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

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void tick() {
        if (!gameOver) {
            spawnEnemies();
        }
        if (!e.isEmpty()) {
            for (int i = 0; i < e.size(); i++) {
                e.get(i).tick();
            }
        }
    }

    public void draw(Graphics g) {
        if (!gameOver) {
            spawnEnemies();
        }
        if (!e.isEmpty() || !waveOver) {
            for (int i = 0; i < e.size(); i++) {
                e.get(i).draw(g);
            }
        }
    }
}
