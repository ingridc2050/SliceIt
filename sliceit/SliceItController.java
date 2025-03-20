package sliceit;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;



public class SliceItController implements ActionListener {
	private final JFrame gameJFrame;
	private JPanel mainPanel;
	private JPanel rulesPanel;
	private JPanel gamePanel;
	private JPanel leaderBoardPanel;
	private BufferedImage spriteSheet;
	private BufferedImage[] unslicedFruits;
	private JButton gameButton;
	private JButton rulesButton;
	private JButton leaderButton;
	


	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	new SliceItController();
            }
        });
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
	    
	    loadFruitImages();
	    
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
	


	    

	private void loadFruitImages() {
	    try {
	        spriteSheet = ImageIO.read(new File("images/fruits.png"));
	        int fruitWidth = 101;  
	        int fruitHeight = 85;
	        int rows = 7;
	        int cols = 1;
	        unslicedFruits = new BufferedImage[rows * cols];

	        for (int i = 0; i < rows; i++) {
	            for (int j = 0; j < cols; j++) {
	                BufferedImage original = spriteSheet.getSubimage(j * fruitWidth, i * fruitHeight, fruitWidth, fruitHeight);
	                unslicedFruits[i * cols + j] = original;
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
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
	    JPanel gamePanel = new JPanel() {
	    	// List to store the active fruits
	        private List<Fruit> fruits = new ArrayList<>();
	        // Random generator for fruit spawn
	        private Random rand = new Random();
	        
	    	private BufferedImage backgroundImage;
	    	{
	            try {
	                backgroundImage = ImageIO.read(new File("images/background.png"));
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        
	    	}
	        
	        // Timer to update game logic every 20ms
	        private Timer timer = new Timer(20, new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                // Update each fruit's position
	                Iterator<Fruit> it = fruits.iterator();
	                while (it.hasNext()) {
	                    Fruit f = it.next();
	                    f.update();
	                    // Remove the fruit if it has left the screen
	                    if (f.isOffScreen(getWidth(), getHeight())) {
	                        it.remove();
	                    }
	                }
	                // Spawn a new fruit occasionally 
	                if (rand.nextDouble() < 0.05) {
	                    int panelWidth = getWidth();
	                    int panelHeight = getHeight();
	                    int fruitIndex = rand.nextInt(unslicedFruits.length);
	                    BufferedImage img = unslicedFruits[fruitIndex];
	                    int fruitWidth = img.getWidth();
	                    // Choose a random x-coordinate ensuring the fruit is fully visible
	                    int x = rand.nextInt(Math.max(panelWidth - fruitWidth, 1));
	                    // Position the fruit at the bottom edge
	                    int y = panelHeight - img.getHeight();
	                    // For upward motion, set a negative y velocity.
	                    // You can adjust the range as needed
	                    float velocityY = -(float)(rand.nextDouble() * 5 + 10); 
	                    // Horizontal drift
	                    float velocityX = (float) (rand.nextDouble() * 4 - 2); // from -2 to 2
	                    Fruit fruit = new Fruit(img, x, y, velocityX, velocityY);
	                    fruits.add(fruit);
	                }
	                repaint();
	            }
	        });

	        // Override paintComponent to draw the fruits
	        @Override
	        protected void paintComponent(Graphics g) {
	            super.paintComponent(g);
	            if (backgroundImage != null) {
	                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
	            }
	            Graphics2D g2d = (Graphics2D) g;
	            for (Fruit f : fruits) {
	                f.draw(g2d);
	            }
	        }

	        // Instance initializer block to start the timer as soon as the panel is created
	        {
	            timer.start();
	        }
	    };

	    gamePanel.setLayout(null);
	    gamePanel.setBackground(Color.pink);
	    gamePanel.setPreferredSize(new Dimension(500, 500));

	    gameJFrame.getContentPane().removeAll();
	    gameJFrame.getContentPane().add(gamePanel);
	    gameJFrame.pack();
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

