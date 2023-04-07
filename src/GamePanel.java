import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    public static final int PANEL_WIDTH = 800;
    public static final int PANEL_HEIGHT = 600;

    static final Dimension PANEL_SIZE = new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
    private Player player1;
    private Player player2;
    private Graphics graphics;
    private Image image;
    private EnemyController enemyController;
    public static int direction;
    private boolean waveOver;
    private long waveOverTimer = -1;
    private static int waveNumber;

    /**
     * The current score.
     */
    private int score;
    /**
     * When true the game has ended as a win or loss represented by the gameOverMessage.
     */
    private boolean gameOver;
    /**
     * Shows the message when the game has ended that will be set as either a win or loss.
     */
    private String gameOverMessage;

    private ArrayList<Bullet> b1 = new ArrayList<Bullet>();
    Timer t = new Timer(10, this);


    /**
     * Constructs a new instance of the GamePanel class.
     */
    public GamePanel() {
        setPreferredSize(PANEL_SIZE);
        setBackground(Color.black);
        player1 = new Player(Color.RED, 50, PANEL_HEIGHT / 2, 25, 25, 0, 0);
        player2 = new Player(Color.BLUE, PANEL_WIDTH - 50, PANEL_HEIGHT / 2, 25, 25, 0, 0);
        enemyController = new EnemyController(3, 0);
        score = 0;
        direction = 4;
        waveOver = false;
        gameOver = false;
        addKeyListener(this);
        setFocusable(true);
        t.start();
    }

    public void actionPerformed(ActionEvent a) {
        if(!gameOver) {
            player1.tick();
            player2.tick();

            for (int i = 0; i < b1.size(); i++) {
                b1.get(i).tick();
            }
            for (Enemy enemy : enemyController.getEnemyList()) {
                enemy.tick();
//            player1.checkCollisonWithBullets(enemyController.getEnemyList(),b1);
            }
            // check for collision between player and enemy bullets
        }
        if(player1.getLives() == 0) {
            gameOver = true;
            gameOverMessage = "GAME OVER! You lost all your lives!";
        } else if(score < 0) {
            gameOver = true;
            gameOverMessage = "GAME OVER! Your base was destroyed!";
        }
        repaint();
    }


    /**
     * Renders the game graphics.
     *
     * @param g The Graphics object used for rendering
     */
    public void paint(Graphics g) {

        super.paint(g);
        g.clearRect(0, 0, getWidth(), getHeight());
        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        g.drawImage(image, 0, 0, this);
        // Draw the players
        player1.update();
        player1.draw(g);
        player2.update();
        player2.draw(g);
        enemyController.draw(g);
        for (int i = 0; i < b1.size(); i++) {
            b1.get(i).draw(g);
        }
        // Draw the enemies
        for (Enemy enemy : enemyController.getEnemyList()) {
            enemy.draw(g);
        }
        drawLives(g);
        drawScore(g);
        drawWaveNum(g);
        if(!gameOver) {
            drawWaveOver(g);
        }
        checkCollison();
        if(gameOver) {
            drawGameOver(g);
        }
    }

    public void checkCollison() {
        //checks if player1's bullet hit player2
        //if it does it adds a score to player1 and removes the bullet
        for (int i = 0; i < b1.size(); i++) {
            if (b1.get(i).x >= player2.x && b1.get(i).x < player2.x + player2.width && b1.get(i).y > player2.y && b1.get(i).y < player2.y + player2.height) {
                increaseScore(1);
                b1.remove(i);
                player2.x = PANEL_WIDTH - 50;
                player2.y = PANEL_HEIGHT / 2;
                i--;
            }
        }


        //checks if player1's bullet hit enemy
        //if it does it's supposed to delete the enemy from the array and the gui screen
        //Also if the variable enemyKilled in the EnemyController class is equal to enemyCount, then its supposed to generate a new wave of enemies that is one more than the previous wave.
        // if the enemy is outside the screen, decrease the score by 1 and add enemyKilled and remove Enemy from array
        if (enemyController.getEnemyList().size() != 0) {
            for (int j = 0; j < (enemyController.getEnemyCount() - enemyController.getEnemyKilled()); j++) {
                Enemy enemy = enemyController.getEnemyList().get(j);
                for (int i = 0; i < b1.size(); i++) {
                    if (enemy != null && b1.get(i).x >= enemy.x && b1.get(i).x < enemy.x + enemy.width && b1.get(i).y > enemy.y && b1.get(i).y < enemy.y + enemy.height) {
                        increaseScore(1);
                        enemyController.addEnemyKilled();
                        enemyController.removeEnemy(j);
                        b1.remove(i);
                        i--;
                    }
                }
            }
        }


        //removes enemies if their off-screen
        for (int i = 0; i < enemyController.getEnemyList().size(); i++) {
            Enemy enemy = enemyController.getEnemyList().get(i);
            if (enemy.x >= PANEL_WIDTH || enemy.x <= 0 ||
                    enemy.y >= PANEL_HEIGHT || enemy.y <= 0) {
                increaseScore(-1);
                enemyController.addEnemyKilled();
                enemyController.removeEnemy(i);
                i--;
            }
        }
        if (enemyController.getEnemyKilled() == enemyController.getEnemyCount()) {
            waveNumber++;
            waveOver = true;
        }

        //removes player 1 bullets if its off-screen
        for (int i = 0; i < b1.size(); i++) {
            if (b1.get(i).x >= PANEL_WIDTH || b1.get(i).x <= 0 ||
                    b1.get(i).y >= PANEL_HEIGHT || b1.get(i).y <= 0) {
                b1.remove(i);
                i--;
            }
        }

    }


    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent k) {

        //player 1's bullet movements
        if (k.getKeyCode() == KeyEvent.VK_SPACE) {
            Bullet bullet1 = new Bullet(Color.BLACK, player1.x + player1.width / 2, player1.y + player1.height / 2, 5, 5, 0, 0, direction);
            b1.add(bullet1);
            bullet1.setDx(10);
        }

        //player1 movements
        if (k.getKeyCode() == KeyEvent.VK_W) {
            player1.setDy(-10);
            direction = 1;
        }
        if (k.getKeyCode() == KeyEvent.VK_A) {
            player1.setDx(-10);
            direction = 2;
        }
        if (k.getKeyCode() == KeyEvent.VK_S) {
            player1.setDy(10);
            direction = 3;
        }
        if (k.getKeyCode() == KeyEvent.VK_D) {
            player1.setDx(10);
            direction = 4;
        }


        //player 2's movements
        if (k.getKeyCode() == KeyEvent.VK_UP) {
            player2.setDy(-10);
        }
        if (k.getKeyCode() == KeyEvent.VK_LEFT) {
            player2.setDx(-10);
        }
        if (k.getKeyCode() == KeyEvent.VK_DOWN) {
            player2.setDy(10);
        }
        if (k.getKeyCode() == KeyEvent.VK_RIGHT) {
            player2.setDx(10);
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

        //player 2
        if (k.getKeyCode() == KeyEvent.VK_UP) {
            player2.setDy(0);
        }
        if (k.getKeyCode() == KeyEvent.VK_RIGHT) {
            player2.setDx(0);
        }
        if (k.getKeyCode() == KeyEvent.VK_DOWN) {
            player2.setDy(0);
        }
        if (k.getKeyCode() == KeyEvent.VK_LEFT) {
            player2.setDx(0);
        }
    }

    private void drawWaveOver(Graphics g) {
        if (waveOver) {
            if (waveOverTimer == -1) {
                waveOverTimer = System.currentTimeMillis();
            }
            g.setFont(new Font("Century Gothic", Font.PLAIN, 18));
            String s = "-  W A V E  " + waveNumber + "  -";
            int length = (int) g.getFontMetrics().getStringBounds(s, g).getWidth();
            g.setColor(new Color(5, 62, 234));
            g.drawString(s, PANEL_WIDTH / 2 - length / 2, PANEL_HEIGHT / 2);

            long elapsed = System.currentTimeMillis() - waveOverTimer;
            if (elapsed >= 3000) {
                waveOverTimer = -1;
                waveOver = false;
                enemyController.setWaveOver(false);
            }
        }
    }


    /**
     * Increases the score by the amount specified.
     *
     * @param amount Amount to increase the score by.
     */
    public void increaseScore(int amount) {
        score += amount;
    }


    /**
     * Draws the score centred at the top of the panel.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    private void drawScore(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        String scoreStr = "Score: " + score;
        g.drawString(scoreStr, 15,70);
    }

    private void drawWaveNum(Graphics g){
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        String scoreStr = "Wave: " + waveNumber;
        g.drawString(scoreStr, 15,100);
    }

    /**
     * Draws the lives left aligned in the top left corner.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    private void drawLives(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        String livesStr = "Lives: " + player1.getLives();
        g.drawString(livesStr, 15, 40);
    }

    /**
     * Draws a background with game over message centred in the middle of the panel.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    private void drawGameOver(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0,PANEL_HEIGHT/2-20, PANEL_WIDTH, 40);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        int strWidth = g.getFontMetrics().stringWidth(gameOverMessage);
        g.drawString(gameOverMessage, PANEL_WIDTH/2-strWidth/2, PANEL_HEIGHT/2+10);
    }


}
