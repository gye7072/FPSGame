import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * The GameFrame class represents the main frame for the 1v1 shooter game.
 */
public class GameFrame extends JFrame implements ActionListener {

    private JPanel mainPanel;
    private JButton playButton;
    private JButton helpButton;
    private JButton leaderBoardButton;
    private JButton quitButton;
    private static final String SCORES_FILE = "scores.txt";
    private static final int NUM_HIGH_SCORES = 10;
    private static int firstTime;
    private static GamePanel gamePanel;
    private static JFrame gameFrame;
    private int score;

    public GameFrame() {
        setLayout(new BorderLayout());
        JLabel background=new JLabel(new ImageIcon("menubackground.png"));
        add(background);
        background.setLayout(new FlowLayout());


        setContentPane(mainPanel);
        setTitle("Defend the Base");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        this.pack();
        this.setVisible(true);
        // Center the frame on the screen
        setLocationRelativeTo(null);
        playButton.addActionListener(this);
        helpButton.addActionListener(this);
        leaderBoardButton.addActionListener(this);
        quitButton.addActionListener(this);

    }
    public void actionPerformed(ActionEvent e) {
        // Handle button clicks
        if (e.getSource() == playButton) {
            // Start the game
            this.dispose();
            if(firstTime == 0) {
                gamePanel = new GamePanel();
                gameFrame = new JFrame("Defend the Base");
                gameFrame.add(gamePanel);
                gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                gameFrame.setResizable(false);
                gameFrame.pack();
                gameFrame.setLocationRelativeTo(null);
                gameFrame.setVisible(true);
                firstTime++;
            } else{
                gamePanel.removeAll();
                gamePanel.restartGame();
                gameFrame.setVisible(true);
            }
        } else if (e.getSource() == helpButton) {
            // Show the help dialog
            JOptionPane.showMessageDialog(this, "Controls:\nW- to go up \nA- to go left \nS- to go down \nD - to go right \nSpace Bar - to shoot" +
                            "\n\nObjective: \nProtect your base for as long as possible by shooting any enemies that appear and earn a high score\n\nLose Conditions:" +
                            "\n-Number of lives hit 0\n-Base health points hits 0\n\nAdditional Notes: \nYou start with three lives and your base " +
                            "starts at a hundred health points\nYour high score is calculated by the number of enemies" +
                            " you defeated and the number of waves you cleared\nEnemies appear in waves, each increasing in difficulty" +
                            "\nEach time you clear a wave enemies get faster, spawn quicker, and the amount of damage enemies can do to your base increases " +
                            "\nEvery time you clear a wave your base also recovers 10 health points",
                    "Help", JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource() == leaderBoardButton) {
            int[] scores = loadHighScores();
            int[] newScores = new int[scores.length + 1];
            for (int i = 0; i < scores.length; i++) {
                newScores[i] = scores[i];
            }
            score = newScores[newScores.length - 1];
            newScores[newScores.length - 1] = this.score; // update the last score with the current game score
            Arrays.sort(newScores);
            saveHighScores(newScores);
            String message = "High Scores:";
            int count = 0;
            for (int i = newScores.length - 1; i >= 0 && count < NUM_HIGH_SCORES; i--) {
                message += "\nPlayer " + (count + 1) + ": " + newScores[i];
                count++;
            }
            JOptionPane.showMessageDialog(this, message, "Leaderboard", JOptionPane.INFORMATION_MESSAGE);
        } else if(e.getSource() == quitButton){
            System.exit(0);
        }
    }


    public int[] loadHighScores() {
        try (Scanner scanner = new Scanner(new File(SCORES_FILE))) {
            ArrayList<Integer> scores = new ArrayList<Integer>();
            while (scanner.hasNextInt()) {
                scores.add(scanner.nextInt());
            }
            int[] result = new int[scores.size()];
            for (int i = 0; i < result.length; i++) {
                result[i] = scores.get(i);
            }
            Arrays.sort(result);
            return result;
        } catch (IOException e) {
            // Return an empty array if the file can't be read
            return new int[0];
        }
    }

    public void saveHighScores(int[] scores) {
        try {
            // Open the scores file for writing
            java.io.FileWriter writer = new java.io.FileWriter(SCORES_FILE, false);

            // Write each score to the file, up to the top ten high scores
            for (int i = 0; i < Math.min(NUM_HIGH_SCORES, scores.length); i++) {
                writer.write(scores[scores.length - 1 - i] + "\n");
            }

            // Close the file
            writer.close();
        } catch (IOException e) {
            System.out.println("Failed to save high scores: " + e.getMessage());
        }
    }



}
