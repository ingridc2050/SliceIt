package sliceit;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SliceItController implements ActionListener {
	private final JFrame gameJFrame;
	private JPanel mainPanel;
	private JPanel rulesPanel;
	private JPanel gamePanel;
	private JPanel leaderBoardPanel;
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
		
		mainPanel = new JPanel();
	    mainPanel.setLayout(null);
	    mainPanel.setBackground(Color.orange);

	    gameJFrame.getContentPane().add(mainPanel);
        gameJFrame.setVisible(true);
		
		gameButton = new JButton("Play");
		gameButton.setBounds(150, 120, 200, 40);
		gameButton.addActionListener(this);
		mainPanel.add(gameButton);
		
		rulesButton = new JButton("Rules");
		rulesButton.setBounds(150, 170, 200, 40);
		rulesButton.addActionListener(this);
		mainPanel.add(rulesButton);
		
		leaderButton = new JButton("Leaderboard");
		leaderButton.setBounds(150, 220, 200, 40);
		leaderButton.addActionListener(this);
		mainPanel.add(leaderButton);
	}
	
	private void rulesPanel() {
		rulesPanel = new JPanel();
		rulesPanel.setLayout(null);
        rulesPanel.setBackground(Color.pink);
        // You can add any content to the new window as needed.
        
        JLabel rulesLabel = new JLabel(
        	    "<html>Game Rules:<br/>" +
        	    "1. Players need to slice as many fruits as they can while avoiding bombs.<br/>" +
        	    "2. The game is lost if the player misses a single fruit.<br/>" +
        	    "3. The game is lost if the player slices a bomb.</html>"
        	);

        rulesLabel.setBounds(50, 50, 400, 200);
        rulesPanel.add(rulesLabel);
        // Back button to return to the main menu
        JButton backButton = new JButton("Back");
        backButton.setBounds(200, 300, 100, 40);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnToMainPanel();
            }
        });
        rulesPanel.add(backButton);
        
        gameJFrame.getContentPane().removeAll();
        gameJFrame.getContentPane().add(rulesPanel);
        gameJFrame.revalidate();
        gameJFrame.repaint();
    }
	
    
	private void returnToMainPanel() {
        
        gameJFrame.getContentPane().removeAll();
        gameJFrame.getContentPane().add(mainPanel);
        gameJFrame.revalidate();
        gameJFrame.repaint();
    }
	
	
	private void gamePanel() {
		gamePanel = new JPanel();
		gamePanel.setLayout(null);
        gamePanel.setBackground(Color.pink);
        JButton backButton = new JButton("Back");
        backButton.setBounds(200, 300, 100, 40);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnToMainPanel();
            }
        });
        gamePanel.add(backButton);
        
        gameJFrame.getContentPane().removeAll();
        gameJFrame.getContentPane().add(gamePanel);
        gameJFrame.revalidate();
        gameJFrame.repaint();
    
	}
	private void leaderboardPanel() {
		leaderBoardPanel = new JPanel();
		leaderBoardPanel.setLayout(null);
		leaderBoardPanel.setBackground(Color.pink);
        JButton backButton = new JButton("Back");
        backButton.setBounds(200, 300, 100, 40);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnToMainPanel();
            }
        });
        leaderBoardPanel.add(backButton);
        
        gameJFrame.getContentPane().removeAll();
        gameJFrame.getContentPane().add(leaderBoardPanel);
        gameJFrame.revalidate();
        gameJFrame.repaint();
    
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == gameButton) {
           gamePanel();
        }
        
        // You can add additional logic for rulesButton and leaderButton
        if (e.getSource() == rulesButton) {
     
        	rulesPanel();
        }
        if (e.getSource() == leaderButton) {
            leaderboardPanel();
        }
    }
		
}

