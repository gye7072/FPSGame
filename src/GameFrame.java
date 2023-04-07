import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The GameFrame class represents the main frame for the 1v1 shooter game.
 */
public class GameFrame extends JFrame implements ActionListener {

    private JPanel mainPanel;
    private JButton playButton;
    private JButton helpButton;
    private JButton leaderBoardButton;
    private JButton quitButton;

    /**
     * Constructs a new instance of the GameFrame class.
     */
    public GameFrame() {
        setContentPane(mainPanel);
        setTitle("Invaders");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
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
            GamePanel gamePanel = new GamePanel();
            JFrame gameFrame = new JFrame("Invaders");
            gameFrame.add(gamePanel);
            gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gameFrame.setResizable(false);
            gameFrame.pack();
            gameFrame.setLocationRelativeTo(null);
            gameFrame.setVisible(true);
        } else if (e.getSource() == helpButton) {
            // Show the help dialog
            JOptionPane.showMessageDialog(this, "Instructions:\nUse arrow keys to move your player\nPress space to shoot\nAvoid enemy bullets and try to hit the other player", "Help", JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource() == leaderBoardButton) {
            // Show the leaderboard dialog
            JOptionPane.showMessageDialog(this, "High Scores:\nPlayer 1: 100\nPlayer 2: 80\nPlayer 3: 60", "Leaderboard", JOptionPane.INFORMATION_MESSAGE);
        }
        else if(e.getSource() == quitButton){
            System.exit(0);
        }
    }
}
