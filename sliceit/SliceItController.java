package sliceit;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class SliceItController implements ActionListener  {
	private final JFrame gameJFrame;
	private JPanel mainPanel;
	private JPanel rulesPanel;
	private JPanel gamePanel;
	private JPanel leaderBoardPanel;
	private BufferedImage spriteSheet;
	private BufferedImage bomb;
	private BufferedImage[] unslicedFruits;
	private BufferedImage[] slicedFruits;
	private JButton gameButton;
	private JButton rulesButton;
	private JButton leaderButton;
	private BufferedImage[] bombExplosionFrames;
	private BufferedImage fruitSliceFrames;
	private int points = 0;
	private JLabel pointLabel;

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
		loadBombImage();
		loadBombExplosionImages();
		loadSlicedFruitImages();

		gameJFrame.setVisible(true);
	    gameJFrame.getContentPane().add(mainPanel);
	    
	    loadFruitImages();
	    loadBombImage();
	    loadBombExplosionImages();
	    
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
	        unslicedFruits = new BufferedImage[rows]; // Initialize the array
	        for (int i = 0; i < rows; i++) {
	            unslicedFruits[i] = spriteSheet.getSubimage(0, i * fruitHeight, fruitWidth, fruitHeight);
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadSlicedFruitImages() {
		try {
	        BufferedImage spriteSheet = ImageIO.read(new File("images/fruits.png"));
	        int fruitWidth = 101;
	        int fruitHeight = 85;
	        int rows = 7;
	        
	        unslicedFruits = new BufferedImage[rows];
	        slicedFruits = new BufferedImage[rows];	        
	        
	        for (int i = 0; i<rows;i++) {
	            unslicedFruits[i] = spriteSheet.getSubimage(0, i * fruitHeight, fruitWidth, fruitHeight);  // Whole fruit
	            slicedFruits[i] = spriteSheet.getSubimage(105, i * fruitHeight, fruitWidth+10, fruitHeight); // Sliced fruit
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	private void loadBombImage() {

		try {
			bomb = ImageIO.read(new File("images/bombimg.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	private void loadBombExplosionImages() {
	    try {
	        BufferedImage spriteSheet = ImageIO.read(new File("images/bombSprites.png"));

	        
	        int rows = 4;
	        int cols = 4;
	        int totalFrames = rows * cols;

	        int frameWidth = spriteSheet.getWidth() / cols;
	        int frameHeight = spriteSheet.getHeight() / rows;

	        bombExplosionFrames = new BufferedImage[totalFrames];
	        int index = 0;

	        for (int row = 0; row < rows; row++) {
	            for (int col = 0; col < cols; col++) {
	                bombExplosionFrames[index] = spriteSheet.getSubimage(col * frameWidth, row * frameHeight, frameWidth,frameHeight);
	                index++;
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
			// List to store bombs
			private List<Bomb> bombs = new ArrayList<>();
			// Random generator for fruit spawn
			private Random rand = new Random();
			
			private BufferedImage backgroundImage;
			// Fields to handle bomb slicing.
			private boolean gameOver = false;
			private int explosionX, explosionY;
			private boolean showExplosion = false;
			private int explosionFrame = 0;
			private Timer explosionTimer;
			
			// Fields to handle bomb slicing.
			private boolean addPoint = false;
			private int sliceX, sliceY;
			private boolean showSlice = false;
			private int sliceFrame = 0;
			private Timer sliceTimer;

			// Timer to update game logic every 20ms.
			private Timer timer = new Timer(20, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Spawn a new fruit occasionally.
					if (rand.nextDouble() < 0.05) {
						int panelWidth = getWidth();
						int fruitIndex = rand.nextInt(unslicedFruits.length);
						BufferedImage img = unslicedFruits[fruitIndex];
						int x = rand.nextInt(Math.max(panelWidth - img.getWidth(), 1));
						int y = getHeight() - img.getHeight();
						float velocityY = -(float) (rand.nextDouble() * 5 + 10);
						float velocityX = (float) (rand.nextDouble() * 4 - 2);
						BufferedImage slicedImg = slicedFruits[fruitIndex]; // use matching slice
						Fruit fruit = new Fruit(img, slicedImg, x, y, velocityX, velocityY);
						

						fruits.add(fruit);
						
					}

					// Update each fruit's position.
					for (int i = fruits.size() - 1; i >= 0; i--) {
						Fruit f = fruits.get(i);
						f.update();
						if (f.isOffScreen(getWidth(), getHeight())) {
							fruits.remove(i);
						}
					}

					// Update each bomb's position.
					Iterator<Bomb> bombIterator = bombs.iterator();
					while (bombIterator.hasNext()) {
						Bomb b = bombIterator.next();
						b.update();
						if (b.getY() > getHeight()) {
							bombIterator.remove();
						}
					}

					// Spawn bombs at a lower probability than fruits.
					if (rand.nextDouble() < 0.02) {
						int panelWidth = getWidth();
						if (bomb != null) {
							int x = rand.nextInt(Math.max(panelWidth - bomb.getWidth(), 1));
							int y = getHeight() - bomb.getHeight();
							float velocityY = -(float) (rand.nextDouble() * 5 + 10);
							float velocityX = (float) (rand.nextDouble() * 4 - 2);
							Bomb newBomb = new Bomb(bomb, x, y, velocityX, velocityY);
							bombs.add(newBomb);
						} else {
							System.err.println("Error: Bomb image is not loaded.");
						}
					}
					repaint();
				}
			});

			// Instance initializer: load background, start timer, and add mouse listener.

			{
				try {
					backgroundImage = ImageIO.read(new File("images/background.png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				timer.start();
				// Add a MouseMotionListener using an anonymous inner class.
				addMouseMotionListener(new java.awt.event.MouseAdapter() {
					@Override
					public void mouseDragged(MouseEvent e) {
						if (gameOver) {
							return;
						}
						int mouseX = e.getX();
						int mouseY = e.getY();
						
						for (Fruit f : fruits) {
				            if (!f.isSliced && f.contains(mouseX, mouseY)) {
				                f.slice();
				                points+=10;
				                pointLabel.setText("Score: " + points);
				            }
				        }
						
						for (Bomb b : bombs) {
							int bombLeft = b.getX();
							int bombRight = b.getX() + b.getImage().getWidth(null);
							int bombTop = b.getY();
							int bombBottom = b.getY() + b.getImage().getHeight(null);
							
							// Check if mouse is within bomb bounds
							if (mouseX >= bombLeft && mouseX <= bombRight &&
									mouseY >= bombTop && mouseY <= bombBottom) {
								explosionX = b.getX()
										+ (b.getImage().getWidth(null) - bombExplosionFrames[0].getWidth()) / 2;
								explosionY = b.getY()
										+ (b.getImage().getHeight(null) - bombExplosionFrames[0].getHeight()) / 2;

								// Remove the bomb so it doesn't keep drawing
								bombs.remove(b);
								gameOver = true;
								timer.stop(); // Stop game updates
								startExplosionAnimation();
								points = 0;
								break;
							}
						}
					}
				});
			}

			// This method simulates the explosion animation.
			private void startExplosionAnimation() {
				showExplosion = true;
				explosionFrame = 0;

				// Set up a timer to update the explosion frame every 10ms
				explosionTimer = new Timer(10, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						explosionFrame++;
						if (explosionFrame >= bombExplosionFrames.length) {
							explosionTimer.stop();
							JOptionPane.showMessageDialog(gameJFrame, "Game Over! You sliced a bomb!");
							returnToMainPanel();
						} else {
							repaint();
						}
					}
				});
				explosionTimer.start();
				
			}
			

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

				for (Bomb b : bombs) {
					g2d.drawImage(b.getImage(), b.getX(), b.getY(), null);
				}

				if (showExplosion && bombExplosionFrames != null && explosionFrame < bombExplosionFrames.length) {
					g2d.drawImage(bombExplosionFrames[explosionFrame], explosionX, explosionY, null);
				}
			}
		};
		

		gamePanel.setLayout(null);
		gamePanel.setBackground(Color.pink);
		gamePanel.setPreferredSize(new Dimension(500, 500));
		
		pointLabel = new JLabel("Score: " + points);
		pointLabel.setForeground(Color.WHITE);
		pointLabel.setBounds(20, 20, 100, 30);
		gamePanel.add(pointLabel);
		
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

		if (e.getSource() == leaderButton) {
			leaderboardPanel();
		}
	}
}
