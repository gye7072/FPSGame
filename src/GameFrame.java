import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class GameFrame extends JFrame implements ActionListener {


    public static final int FRAME_WIDTH = 600;

    public static final int FRAME_HEIGHT = 600;
    private JPanel mainPanel;
    private JButton playButton;
    private JButton helpButton;
    private JButton leaderBoardButton;
    private JButton quitButton;
    private JTextField textField;
    private JButton musicButton;
    private static final String SCORES_FILE = "scores.txt";
    private static final int NUM_HIGH_SCORES = 10;
    private static int firstTime;
    private int count;
    private static GamePanel gamePanel;
    private static JFrame gameFrame;
    public static String userName;
    public GameFrame() {
        setContentPane(mainPanel);
        setTitle("Defend the Planet");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        this.pack();
        this.setVisible(true);
        File scoresFile = new File(SCORES_FILE);
        if (!scoresFile.exists()) {
            try {
                scoresFile.createNewFile();
                FileWriter writer = new FileWriter(scoresFile);
                for (int i = 0; i < NUM_HIGH_SCORES; i++) {
                    writer.write("Computer\n");
                    writer.write("0\n");
                }
                writer.close();
            } catch (IOException e) {
                System.out.println("Failed to create scores file: " + e.getMessage());
            }
        }
        setLocationRelativeTo(null);
        playButton.addActionListener(this);
        helpButton.addActionListener(this);
        leaderBoardButton.addActionListener(this);
        musicButton.addActionListener(this);
        quitButton.addActionListener(this);
    }
    public void actionPerformed(ActionEvent e) {
        // Handle button clicks
        if (e.getSource() == playButton) {
            userName = textField.getText();
            if(textField.getText().equals("")){
                userName = "Blank";
            }
            // Start the game
            this.dispose();
            if(firstTime == 0) {
                gamePanel = new GamePanel();
                gameFrame = new JFrame("Defend the Planet");
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
                            "\n\nObjective: \nProtect the planet for as long as possible by shooting any enemies that appear and earn a high score\n\nLose Conditions:" +
                            "\nNumber of lives hit 0\nPlanet health points hits 0\n\nAdditional Notes: \nYou start with three lives and the planet " +
                            "starts at a hundred health points\nYour high score is calculated by the number of enemies" +
                            " you defeated and the number of waves you cleared\nEnemies appear in waves, each increasing in difficulty" +
                            "\nEach time you clear a wave enemies get faster, spawn quicker, and the amount of damage enemies can do to the planet increases " +
                            "\nEvery time you clear a wave the planet recovers ten health points\nEach enemy has a chance to drop a heart which can increase the amount" +
                            " of lives you have left by one",
                    "Help", JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource() == leaderBoardButton) {
            try (Scanner scanner = new Scanner(new File(SCORES_FILE))) {
                String message = "Top 10 High Scores:\n";
                int count = 0;
                while (scanner.hasNextLine() && count < NUM_HIGH_SCORES) {
                    String name = scanner.nextLine();
                    int score = Integer.parseInt(scanner.nextLine());
                    message += (count+1) + "—" + name + ": " + score + "\n";
                    count++;
                }
                scanner.close();
                JOptionPane.showMessageDialog(this, message, "Leaderboard", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                System.out.println("Failed to read scores file: " + ex.getMessage());
            }
        } else if(e.getSource() == musicButton){
            Sound s = new Sound();
            count++;
            if(count % 2 == 1) {
                s.setMute(true);
                JOptionPane.showMessageDialog(this, "Music will not be playing", "Music is Off", JOptionPane.INFORMATION_MESSAGE);
            } else if(count % 2 == 0){
                s.setMute(false);
                JOptionPane.showMessageDialog(this, "Music will be playing", "Music is On", JOptionPane.INFORMATION_MESSAGE);
            }
        }else if(e.getSource() == quitButton){
            System.exit(0);
        }
    }

}
