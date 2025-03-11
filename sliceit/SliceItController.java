package sliceit;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class SliceItController implements ActionListener {
	private final JFrame gameJFrame;
	private JFrame gameWindow;
	private JFrame rulesWindow;
	private JFrame leaderWindow;
	private JButton gameButton;
	private JButton rulesButton;
	private JButton leaderButton;


	public static void main(String[] args) {
		new SliceItController();

	}
	
	public SliceItController() {
		gameJFrame = new JFrame();
		gameJFrame.setSize(500, 500);
		gameJFrame.setLocation(50, 50);
		gameJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container gameContentPane = gameJFrame.getContentPane();
		gameContentPane.setLayout(null);
		gameContentPane.setBackground(Color.orange);
		gameJFrame.setVisible(true);
		
		gameButton = new JButton("Play");
		gameButton.setBounds(150, 120, 200, 40);
		gameButton.addActionListener(this);
		gameContentPane.add(gameButton);
		
		rulesButton = new JButton("Rules");
		rulesButton.setBounds(150, 170, 200, 40);
		rulesButton.addActionListener(this);
		gameContentPane.add(rulesButton);
		
		leaderButton = new JButton("Leaderboard");
		leaderButton.setBounds(150, 220, 200, 40);
		leaderButton.addActionListener(this);
		gameContentPane.add(leaderButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == gameButton) {
            // Create a new JFrame for the game window
            gameWindow = new JFrame("Game Window");
            gameWindow.setSize(600, 400);
            gameWindow.setLocation(100, 100);
            gameWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close this window when done

            // You can add any content to the new window as needed.
            Container gameWindowContent = gameWindow.getContentPane();
            gameWindowContent.setLayout(null);
            gameWindowContent.setBackground(Color.lightGray);

            // Show the new game window
            gameWindow.setVisible(true);
        }
        
        // You can add additional logic for rulesButton and leaderButton
        if (e.getSource() == rulesButton) {
            // Handle rules button action
        	rulesWindow = new JFrame("Rules Window");
        	rulesWindow.setSize(600, 400);
        	rulesWindow.setLocation(100, 100);
        	rulesWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close this window when done

            // You can add any content to the new window as needed.
            Container rulesWindowContent = rulesWindow.getContentPane();
            rulesWindowContent.setLayout(null);
            rulesWindowContent.setBackground(Color.lightGray);

            rulesWindow.setVisible(true);
        }

        if (e.getSource() == leaderButton) {
            // Handle leaderboard button action
        	leaderWindow = new JFrame("Leaderboard Window");
        	leaderWindow.setSize(600, 400);
        	leaderWindow.setLocation(100, 100);
        	leaderWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close this window when done

            // You can add any content to the new window as needed.
            Container leaderWindowContent = rulesWindow.getContentPane();
            leaderWindowContent.setLayout(null);
            leaderWindowContent.setBackground(Color.lightGray);

            leaderWindow.setVisible(true);
        }
    }
		
}

