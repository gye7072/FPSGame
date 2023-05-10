import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    public static final int PANEL_WIDTH = 1000;
    public static final int PANEL_HEIGHT = 600;
    static final Dimension PANEL_SIZE = new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
    private Player player1;

    private EnemyController enemyController;
    private HeartsController heartsController;
    private boolean waveOver;
    private int baseHP;
    private long waveOverTimer = -1;
    private static int waveNumber;
    private JButton retryButton;
    private JButton mainMenuButton;
    private long timer = -1;
    private int killCount;
    private int highScore;

    private boolean gameOver;

    private String gameOverMessage;
    private boolean alreadyExecuted;
    private BufferedImage image;
    private boolean dropped;
    private Hearts hearts;
    private ArrayList<Bullet> b1 = new ArrayList<Bullet>();
    Timer t = new Timer(10, this);



    public GamePanel() {
        setPreferredSize(PANEL_SIZE);
        try {
            image = ImageIO.read(getClass().getResource("background.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        player1 = new Player( 50, PANEL_HEIGHT / 2, 50,50, 0,0);
        enemyController = new EnemyController(3, 0);
        heartsController = new HeartsController(enemyController.getEnemyList(), b1, player1);
        player1.setLives(3);
        killCount = 0;
        highScore = 0;
        alreadyExecuted = false;
        dropped = false;
        waveNumber = 1;
        baseHP = 100;
        enemyController.getEnemyList().clear();
        b1.clear();
        gameOverMessage = "";
        setDoubleBuffered(true);
        gameOver = false;
        addKeyListener(this);
        setFocusable(true);
        t.start();
    }

    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            player1.tick();
            for (int i = 0; i < b1.size(); i++) {
                b1.get(i).tick();
            }
            for (Enemy enemy : enemyController.getEnemyList()) {
                enemy.tick();
                for(int i = 0; i < enemy.getB2().size(); i++){
                    enemy.getB2().get(i).tick();
                }
            }
        }
        if (player1.getLives() <= 0) {
            player1.setLives(0);
            enemyController.getEnemyList().clear();
            gameOver = true;
            gameOverMessage = "You lost all your lives!";
        } else if (baseHP <= 0) {
            baseHP = 0;
            gameOver = true;
            gameOverMessage = "Your base was destroyed!";
        }
        if (gameOver) {
            enemyController.setGameOver(true);
            b1.clear();
            if (!alreadyExecuted) {
                highScore = killCount + waveNumber;
                updateScores(9);
                retryButton = new JButton("Retry");
                retryButton.setBackground(Color.WHITE);
                retryButton.setBounds(PANEL_WIDTH / 2 - 100, PANEL_HEIGHT / 2 + 50, 200, 50);
                add(retryButton);
                mainMenuButton = new JButton("Main Menu");
                mainMenuButton.setBounds(PANEL_WIDTH / 2 - 100, PANEL_HEIGHT / 2 + 120, 200, 50);
                add(mainMenuButton);
                retryButton.addActionListener(this);
                mainMenuButton.addActionListener(this);
                alreadyExecuted = true;
            }
            retryButton.setBounds(PANEL_WIDTH / 2 - 100, PANEL_HEIGHT / 2 + 50, 200, 50);
            mainMenuButton.setBounds(PANEL_WIDTH / 2 - 100, PANEL_HEIGHT / 2 + 120, 200, 50);
            if (e.getSource() == retryButton) {
                // Start the game
                removeAll();
                restartGame();
            } else if (e.getSource() == mainMenuButton) {
                switchToMainMenu();
            }
        }

        repaint();
    }
    public void updateScores(int index) {
        try {
            File file = new File("scores.txt");
            Scanner scanner = new Scanner(file);

            int[] scores = new int[index + 1];
            int count = 0;
            while(scanner.hasNextInt()){
                scores[count] = scanner.nextInt();
                count++;
            }
            scanner.close();

            if (highScore > scores[index]) {
                FileWriter writer = new FileWriter(file);
                if(count == 0){
                    writer.write(highScore + "\n");
                } else{
                    scores[count-1] = highScore;
                }
                for (int i = 0; i < count; i++) {
                    writer.write(scores[i] + "\n");
                }
                writer.close();
            }

        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
            e.printStackTrace();
        }
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the players
        g.drawImage(image, 0, 0, null);
        if(!gameOver) {
            player1.update();
            player1.draw(g);
        }
        enemyController.draw(g);
        heartsController.draw(g);
        for (Bullet bullet : b1) {
            bullet.draw(g);
        }
        // Draw the enemies
        for (Enemy enemy : enemyController.getEnemyList()) {
            enemy.draw(g);
            for(int i = 0; i < enemy.getB2().size(); i++){
                enemy.getB2().get(i).draw(g);
            }
        }
        drawStats(g);
        if (!gameOver) {
            drawWaveOver(g);
        }
        checkCollision(g);
        if (gameOver) {
            drawGameOver(g);
        }
    }


    public void checkCollision(Graphics g) {

        // Check if any enemies have gone off-screen
        ArrayList<Enemy> enemies = enemyController.getEnemyList();
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            if (enemy != null && enemy.x + enemy.width < 0) {
                // Enemy has gone off-screen, remove it and decrement base HP
                enemies.remove(i);
                enemyController.addEnemyKilled();
                i--;
                baseHP = baseHP - waveNumber;
            }
        }

        //check if player bullets have gone off-screen
        //removes player 1 bullets if its off-screen
        for (int i = 0; i < b1.size(); i++) {
            if (b1.get(i).x >= PANEL_WIDTH || b1.get(i).x <= 0 ||
                    b1.get(i).y >= PANEL_HEIGHT || b1.get(i).y <= 0) {
                b1.remove(i);
                i--;
            }
        }


        // Check for collisions between player bullets and enemies
        for (int i = 0; i < b1.size(); i++) {
            Bullet bullet = b1.get(i);
            for (int j = 0; j < enemies.size(); j++) {
                Enemy enemy = enemies.get(j);
                if (enemy != null && bullet.x >= enemy.x && bullet.x <= enemy.x + enemy.width &&
                        bullet.y >= enemy.y && bullet.y <= enemy.y + enemy.height) {
                    // Collision detected, remove bullet and enemy
                    if (!b1.isEmpty()) {
                        b1.remove(0);
                    }
                    heartsController.spawnHearts(g, enemy.x, enemy.y);
                    enemyController.removeEnemy(j);
                    enemyController.addEnemyKilled();
                    killCount++;
                }
            }
        }
        if (enemyController.getEnemyKilled() == enemyController.getEnemyCount()) {
            waveNumber++;
            waveOver = true;
        }

        //check collision between enemy bullets and player
        for(Enemy e : enemies){
            for(int i = 0; i < e.getB2().size(); i++){
                if(e.getB2().get(i).x >= player1.x && e.getB2().get(i).x <= player1.x + player1.width &&
                        e.getB2().get(i).y >= player1.y && e.getB2().get(i).y <= player1.y + player1.height){
                    player1.lostLife();
                    e.getB2().remove(0);
                    i--;
                }
            }
        }

    }

    public void keyTyped(KeyEvent e) {

    }
    public void keyPressed(KeyEvent k) {

        //player 1's bullet movements
        if (k.getKeyCode() == KeyEvent.VK_SPACE) {
            Bullet bullet1 = new Bullet(Color.WHITE, player1.x + player1.width / 2, player1.y + player1.height / 2, 5, 5, 0, 0);
            b1.add(bullet1);
            bullet1.setDx(10);
        }

        //player1 movements
        if (k.getKeyCode() == KeyEvent.VK_W) {
            player1.setDy(-10);
        }
        if (k.getKeyCode() == KeyEvent.VK_A) {
            player1.setDx(-10);
        }
        if (k.getKeyCode() == KeyEvent.VK_S) {
            player1.setDy(10);
        }
        if (k.getKeyCode() == KeyEvent.VK_D) {
            player1.setDx(10);
        }

    }


    public void keyReleased(KeyEvent k) {
        //player 1
        if (k.getKeyCode() == KeyEvent.VK_W) {
            player1.setDy(0);
        }
        if (k.getKeyCode() == KeyEvent.VK_A) {
            player1.setDx(0);
        }
        if (k.getKeyCode() == KeyEvent.VK_S) {
            player1.setDy(0);
        }
        if (k.getKeyCode() == KeyEvent.VK_D) {
            player1.setDx(0);
        }
    }

    private void drawWaveOver(Graphics g) {
        if (waveOver) {
            if (waveOverTimer == -1) {
                waveOverTimer = System.currentTimeMillis();
                enemyController.setSpawnEnabled(false); // stop enemy spawning
            } else if (System.currentTimeMillis() - waveOverTimer > 5000) {
                // 5 seconds have passed, resume enemy spawning
                waveOver = false;
                waveOverTimer = -1;
                enemyController.setSpawnEnabled(true);
                baseHP += 10;
            }
            if (System.currentTimeMillis() - waveOverTimer < 2000) {
                g.setColor(Color.WHITE);
                g.setFont(new Font("Century Gothic", Font.PLAIN, 18));
                String s = "-  W A V E  " + (waveNumber - 1) + "  C L E A R E D  -";
                int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
                g.drawString(s, PANEL_WIDTH / 2 - length / 2, PANEL_HEIGHT / 2);
            } else {
                g.setColor(Color.WHITE);
                g.setFont(new Font("Century Gothic", Font.PLAIN, 18));
                String s = "-  E N T E R   W A V E  " + waveNumber + "  -";
                int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
                g.drawString(s, PANEL_WIDTH / 2 - length / 2, PANEL_HEIGHT / 2);
            }
        }
    }



    private void drawStats(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Futura", Font.BOLD, 20));
        String livesStr = "Lives: " + player1.getLives();
        g.drawString(livesStr, 15, 40);
        String killCountStr  = "Kill Count: " + killCount;
        g.drawString(killCountStr, 15, 70);
        String baseStr = "Base HP: " + baseHP;
        g.drawString(baseStr, 15, 100);
        String waveStr = "Wave: " + waveNumber;
        g.drawString(waveStr, 15, 130);
    }


    private void drawGameOver(Graphics g) {
        g.setColor(Color.WHITE);
        int strWidth = g.getFontMetrics().stringWidth(gameOverMessage);
        g.setFont(new Font("Impact", Font.BOLD, 100));
        g.drawString("GAME OVER", PANEL_WIDTH / 2 - strWidth, PANEL_HEIGHT / 2 - 50);
        g.setFont(new Font("Impact", Font.BOLD, 25));
        g.drawString(gameOverMessage, PANEL_WIDTH / 2 - strWidth + 100, PANEL_HEIGHT / 2 - 10);
        g.drawString("HIGH SCORE: " + highScore, PANEL_WIDTH / 2 - strWidth + 140, PANEL_HEIGHT/2 + 25);

    }


    public void switchToMainMenu() {
        SwingUtilities.getWindowAncestor(this).setVisible(false);

        // Create a new instance of the main menu game frame
        GameFrame mainMenuFrame = new GameFrame();
        mainMenuFrame.setSize(GameFrame.FRAME_WIDTH, GameFrame.FRAME_HEIGHT);
        mainMenuFrame.setLocationRelativeTo(null);

        // Show the main menu game frame
        mainMenuFrame.setVisible(true);
    }


    public void restartGame() {
        player1.x = 50;
        player1.y = PANEL_WIDTH/2;
        player1.setLives(3);
        highScore = 0;
        killCount = 0;
        alreadyExecuted = false;
        waveNumber = 1;
        baseHP = 100;
        enemyController.getEnemyList().clear();

        b1.clear();
        gameOver = false;
        gameOverMessage = "";
        enemyController.setGameOver(false);
        enemyController = new EnemyController(3, 0);
        repaint();
    }


}