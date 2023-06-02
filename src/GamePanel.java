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
    public static final Dimension PANEL_SIZE = new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
    private final Player player1;

    private EnemyController enemyController;
    private HeartsController heartsController;
    public static boolean waveOver;
    private int planetHP;
    private long waveOverTimer = -1;
    public static int waveNumber;
    private JButton retryButton;
    private JButton mainMenuButton;
    private int killCount;
    private int highScore;

    public static boolean gameOver;

    private String gameOverMessage;
    private boolean alreadyExecuted;
    private boolean alreadyExecuted2;
    private int enemyDamage;
    private BufferedImage image;
    private ArrayList<Bullet> playerBullets = new ArrayList<Bullet>();
    Timer t = new Timer(10, this);



    public GamePanel() {
        setPreferredSize(PANEL_SIZE);
        setBackground(Color.BLACK);
        try {
            image = ImageIO.read(getClass().getResource("background.png"));
        } catch (Exception e) {
            System.out.println("Failed to set background image: " + e.getMessage());
        }
        player1 = new Player( 50, PANEL_HEIGHT / 2, 50,50, 0,0);
        enemyController = new EnemyController(5, 0);
        heartsController = new HeartsController(enemyController.getEnemyList(), player1);
        player1.setLives(3);
        planetHP = 100;
        enemyDamage = 1;
        killCount = 0;
        highScore = 0;
        alreadyExecuted = false;
        alreadyExecuted2 = false;
        waveNumber = 1;
        enemyController.getEnemyList().clear();
        playerBullets.clear();
        gameOverMessage = "";
        setDoubleBuffered(true);
        gameOver = false;
        addKeyListener(this);
        setFocusable(true);
        t.start();

    }

    public void actionPerformed(ActionEvent e) {
        repaint();
        if (!gameOver) {
            player1.tick();
            for (int i = 0; i < playerBullets.size(); i++) {
                playerBullets.get(i).tick();
            }
            for (Enemy enemy : enemyController.getEnemyList()) {
                enemy.tick();
                for(int i = 0; i < enemy.getEnemyBullets().size(); i++){
                    enemy.getEnemyBullets().get(i).tick();
                }
            }
        }
        if (player1.getLives() <= 0) {
            player1.setLives(0);
            enemyController.getEnemyList().clear();
            gameOver = true;
            gameOverMessage = "Your spaceship was destroyed!";
        } else if (planetHP <= 0) {
            planetHP = 0;
            gameOver = true;
            gameOverMessage = "The planet was destroyed!";
        }
        if (gameOver) {
            playerBullets.clear();
            if (!alreadyExecuted) {
                Sound sound = new Sound();
                sound.setFile(5);
                sound.play();
                highScore = killCount + waveNumber;
                updateScores(9);
                retryButton = new JButton("Retry");
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

            String[] names = new String[index +1];
            int[] scores = new int[index + 1];
                int count = 0;
                while (scanner.hasNextLine() && count < scores.length) {
                    names[count] = scanner.nextLine();
                    scores[count] = Integer.parseInt(scanner.nextLine());
                count++;
            }
            scanner.close();

            if (highScore > scores[index]) {
                names[index] = GameFrame.userName;
                scores[index] = highScore;

                for(int i = scores.length-1; i > 0; i--){
                    if(scores[i] > scores[i-1]){
                        String tempName = names[i-1];
                        int tempScore = scores[i-1];
                        names[i-1] = names[i];
                        scores[i-1] = scores[i];
                        names[i] = tempName;
                        scores[i] = tempScore;
                    }
                }

                FileWriter writer = new FileWriter(file);
                for (int i = 0; i < scores.length; i++) {
                    writer.write(names[i] + "\n");
                    writer.write(scores[i] + "\n");
                }
                writer.close();
            }
        } catch (IOException e) {
            System.out.println("Failed to update score " + e.getMessage());
        }
    }


    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        checkCollision(g);
        if(!gameOver) {
            g.drawImage(image, 0, 0, null);
            player1.update();
            player1.draw(g);
        }

        enemyController.draw(g);
        heartsController.draw(g);
        for (Bullet bullet : playerBullets) {
            bullet.draw(g);
        }
        // Draw the enemies
        for (Enemy enemy : enemyController.getEnemyList()) {
            enemy.draw(g);
            for(int i = 0; i < enemy.getEnemyBullets().size(); i++){
                enemy.getEnemyBullets().get(i).draw(g);
            }
        }
        drawStats(g);
        if (!gameOver) {
            drawWaveOver(g);
        }
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
                // Enemy has gone off-screen, remove it and decrement planet HP
                enemies.remove(i);
                enemyController.addEnemyKilled();
                i--;
                if(enemyDamage < 5){
                    enemyDamage++;
                }
                planetHP = planetHP - enemyDamage;
            }
        }

        //check if player bullets have gone off-screen
        //removes player 1 bullets if its off-screen
        for (int i = 0; i < playerBullets.size(); i++) {
            if (playerBullets.get(i).x >= PANEL_WIDTH || playerBullets.get(i).x <= 0 ||
                    playerBullets.get(i).y >= PANEL_HEIGHT || playerBullets.get(i).y <= 0) {
                playerBullets.remove(i);
                i--;
            }
        }

        //keeps displaying bullets that were shot before the enemy was killed
        for (int i = 0; i < enemies.size(); i++) {
            Enemy enemy = enemies.get(i);
            if(enemy.getIsDead()) {
                for (int j = 0; j < enemy.getEnemyBullets().size(); j++) {
                    Bullet bullet = enemy.getEnemyBullets().get(j);
                    if (bullet.x >= PANEL_WIDTH || bullet.x <= 0 ||
                            bullet.y >= PANEL_HEIGHT || bullet.y <= 0) {
                        enemy.getEnemyBullets().remove(bullet);
                        j--;
                    }
                }
                if (enemy.getEnemyBullets().isEmpty()) {
                    enemyController.removeEnemy(i);
                    i--;
                }
            }
        }


        // Check for collisions between player bullets and enemies
        for (int i = 0; i < playerBullets.size(); i++) {
            Bullet bullet = playerBullets.get(i);
            for (int j = 0; j < enemies.size(); j++) {
                Enemy enemy = enemies.get(j);
                if (enemy != null && !enemy.getIsDead() && bullet.x >= enemy.x && bullet.x <= enemy.x + enemy.width &&
                        bullet.y >= enemy.y && bullet.y <= enemy.y + enemy.height) {
                    Sound sound = new Sound();
                    sound.setFile(1);
                    sound.play();
                    while(playerBullets.contains(bullet)) {
                        playerBullets.remove(bullet);
                        if(i > 0) {
                            i--;
                        }
                    }
                    if(!alreadyExecuted2 && !enemy.getIsDead()) {
                        heartsController.spawnHearts(enemy.x, enemy.y);
                        alreadyExecuted2 = true;
                    }
                    enemyController.setEnemyDead(j);
                    enemyController.addEnemyKilled();
                    killCount++;
                }
                alreadyExecuted2 = false;
            }
        }
        if (enemyController.getEnemyKilled() == enemyController.getEnemyCount()) {
            waveNumber++;
            waveOver = true;
        }

        //check collision between enemy bullets and player
        for(Enemy e : enemies){
            for(int i = 0; i < e.getEnemyBullets().size(); i++){
                if(e.getEnemyBullets().get(i).x >= player1.x && e.getEnemyBullets().get(i).x <= player1.x + player1.width &&
                        e.getEnemyBullets().get(i).y >= player1.y && e.getEnemyBullets().get(i).y <= player1.y + player1.height){
                    Sound sound = new Sound();
                    sound.setFile(2);
                    sound.play();
                    player1.setHit(true);
                    player1.draw(g);
                    player1.lostLife();
                    e.getEnemyBullets().remove(0);
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
            Sound sound = new Sound();
            sound.setFile(0);
            sound.play();
            Bullet bullet1 = new Bullet(Color.WHITE, player1.x + player1.width / 2, player1.y + player1.height / 2, 5, 5, 0, 0);
            playerBullets.add(bullet1);
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
                planetHP += 0;
            }
            if (System.currentTimeMillis() - waveOverTimer < 2000) {
                Sound sound = new Sound();
                sound.setFile(4);
                sound.play();
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
        String planetStr = "Planet HP: " + planetHP;
        g.drawString(planetStr, 15, 100);
        String waveStr = "Wave: " + waveNumber;
        g.drawString(waveStr, 15, 130);
    }


    private void drawGameOver(Graphics g) {
        g.setColor(Color.WHITE);
        int strWidth = g.getFontMetrics().stringWidth(gameOverMessage);
        g.setFont(new Font("Impact", Font.BOLD, 100));
        g.drawString("G A M E  O V E R", PANEL_WIDTH / 2 - strWidth, PANEL_HEIGHT / 2 - 50);
        g.setFont(new Font("Impact", Font.BOLD, 25));
        g.drawString(gameOverMessage, PANEL_WIDTH / 2 - strWidth + 140, PANEL_HEIGHT / 2 - 10);
        g.drawString("HIGH SCORE: " + highScore, PANEL_WIDTH / 2 - strWidth + 212, PANEL_HEIGHT/2 + 25);
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
        planetHP = 100;
        enemyDamage = 1;
        enemyController.getEnemyList().clear();
        heartsController.getH1().clear();
        playerBullets.clear();
        heartsController.getH1().clear();
        gameOver = false;
        gameOverMessage = "";
        enemyController = new EnemyController(3, 0);
        repaint();
    }


}