package sliceit;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * This controller class serves as the controller for the SliceIt game.
 * It handles the main GUI components, game logic, and user interactions.
 * <p>
 * This class implements ActionListener and manages navigation between the game, rules, 
 * and leaderboard panels. It also loads images for fruits, bombs, and animations, and controls 
 * game events such as spawning fruits, handling slices, and bomb explosions.
 * </p>
 */
public class SliceItController implements ActionListener  {
	  /** The main game window. */
    private final JFrame gameJFrame;
    /** The main menu panel. */
    private JPanel mainPanel;
    /** Panel displaying game rules. */
    private JPanel rulesPanel;
    /** Panel where the actual game is played. */
    private JPanel gamePanel;
    /** Panel displaying the leaderboard. */
    private JPanel leaderBoardPanel;
    /** The sprite sheet image containing fruit images. */
    private BufferedImage spriteSheet;
    /** The image representing a bomb. */
    private BufferedImage bomb;
    /** Array holding unsliced fruit images. */
    private BufferedImage[] unslicedFruits;
    /** Array holding sliced fruit images. */
    private BufferedImage[] slicedFruits;
    /** Button to start the game. */
    private JButton gameButton;
    /** Button to show the rules panel. */
    private JButton rulesButton;
    /** Button to show the leaderboard panel. */
    private JButton leaderButton;
    /** Array holding frames of bomb explosion animation. */
    private BufferedImage[] bombExplosionFrames;
    /** Not used in this snippet but reserved for additional fruit slice animation frames. */
    private BufferedImage fruitSliceFrames;
    /** The current score for the game. */
    private int points = 0;
    /** Label to display the current score. */
    private JLabel pointLabel;
    
    /**
     * The main entry point for the SliceIt game. It schedules the creation of the GUI on the
     * Event Dispatch Thread (EDT).
     *
     * @param args command line arguments (not used)
     */
	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new SliceItController();

			}
		});
	}

	 /**
     * Constructs a new  SliceItController object.
     * <p>
     * This constructor initializes the main JFrame, sets up the primary menu panel, loads necessary images,
     * and creates the navigation buttons for starting the game, viewing rules, and showing the leaderboard.
     * </p>
     */
	public SliceItController() {
		class BackgroundPanel extends JPanel {
		    private BufferedImage backgroundImage;

		    public BackgroundPanel(String imagePath) {
		        try {
		            backgroundImage = ImageIO.read(new File(imagePath));
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		        setLayout(null); // retain your layout preference
		    }
		    

		    @Override
		    protected void paintComponent(Graphics g) {
		        super.paintComponent(g);
		        if (backgroundImage != null) {
		            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
		        }
		    }
		}
		
		

		gameJFrame = new JFrame();
		gameJFrame.setSize(500, 500);
		gameJFrame.setLocation(50, 50);
		gameJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mainPanel = new BackgroundPanel("images/welcomee.png"); // Change to your actual image path

		gameJFrame.getContentPane().add(mainPanel);

		loadSlicedFruitImages();

		loadBombImage();
		loadBombExplosionImages();
		
		
		gameJFrame.setVisible(true);
	    gameJFrame.getContentPane().add(mainPanel);
	    
	    
        gameJFrame.setVisible(true);
		
        ImageIcon playIcon = new ImageIcon("images/playyy.png");
        gameButton = new JButton(playIcon);
        gameButton.setBounds(150, 200, 200, 37);
        gameButton.setBorderPainted(false);
        gameButton.setContentAreaFilled(false);
        gameButton.setFocusPainted(false);
        gameButton.setOpaque(false);
        gameButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gameButton.addActionListener(this);
        mainPanel.add(gameButton);
        
        ImageIcon ruleIcon = new ImageIcon("images/rules.png");
        rulesButton = new JButton(ruleIcon);
        rulesButton.setBounds(150, 240, 200, 37);
        rulesButton.setBorderPainted(false);
        rulesButton.setContentAreaFilled(false);
        rulesButton.setFocusPainted(false);
        rulesButton.setOpaque(false);
        rulesButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        rulesButton.addActionListener(this);
        mainPanel.add(rulesButton);

		ImageIcon leadIcon = new ImageIcon("images/leaderboardbutton.png");
        leaderButton = new JButton(leadIcon);
        leaderButton.setBounds(150, 280, 200, 37);
        leaderButton.setBorderPainted(false);
        leaderButton.setContentAreaFilled(false);
        leaderButton.setFocusPainted(false);
        leaderButton.setOpaque(false);
        leaderButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        leaderButton.addActionListener(this);
        mainPanel.add(leaderButton);
        
	}
	
	
	 /**
     * Loads both unsliced and sliced fruit images from the sprite sheet.
     * <p>
     * This method extracts two sets of images from "images/fruits.png". It uses one section for
     * unsliced fruits and another for their sliced counterparts.
     * </p>
     */
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
	 /**
     * Loads the bomb image from a file.
     */
	private void loadBombImage() {

		try {
			bomb = ImageIO.read(new File("images/bombimg.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	  /**
     * Loads the bomb explosion frames from a sprite sheet.
     * <p>
     * This method reads the "images/bombSprites.png" file and splits it into a grid of images (frames)
     * which form an animation sequence.
     * </p>
     */
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
	 /**
     * Initializes and displays the rules panel.
     * <p>
     * This panel explains the game rules and includes a back button for returning to the main menu.
     * </p>
     */
	private void rulesPanel() {

		rulesPanel = new JPanel();
		rulesPanel.setLayout(null);
		rulesPanel.setBackground(Color.pink);

		// You can add any content to the new window as needed.
		JLabel rulesLabel = new JLabel(
				"<html>Game Rules:<br/>" +
						"1. Players need to slice as many fruits as they can while avoiding bombs.<br/>" +
						
						"2. The game is lost if the player slices a bomb.</html>"
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
	 /**
     * Returns to the main menu panel.
     * <p>
     * This method removes all components from the content pane of the frame and adds the main panel back.
     * </p>
     */
	private void returnToMainPanel() {

		gameJFrame.getContentPane().removeAll();
		gameJFrame.getContentPane().add(mainPanel);
		gameJFrame.revalidate();
		gameJFrame.repaint();

	}
	/**
     * Initializes and displays the game panel where the gameplay takes place.
     * <p>
     * This method defines an anonymous inner {@code JPanel} subclass that handles fruit and bomb spawning,
     * game animations, mouse interactions, scoring, and countdown timers.
     * </p>
     */
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
			

			 // Declare the one-minute game timer.
	        private Timer gameTimer;
	        // Countdown variables to display time remaining.
	        private int timeRemaining = 60;
	        private JLabel timeLabel;
	        private Timer countdownTimer;
	        

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

			
			
	        
	        // Instance initializer: load background, start timers, and add mouse listener.
	        {
	            try {
	                backgroundImage = ImageIO.read(new File("images/better_background.png"));
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            timer.start();
	            
	            
	            gameTimer = new Timer(60000, new ActionListener() {
	                @Override
	                public void actionPerformed(ActionEvent e) {
	                    if (!gameOver) {
	                        timer.stop();
	                        JOptionPane.showMessageDialog(gameJFrame, "You won! Your score: " + points);
	                        returnToMainPanel();
	                    }
	                }
	            });
	            gameTimer.setRepeats(false);
	            gameTimer.start();
	            
	         
	            timeLabel = new JLabel("Time: " + timeRemaining);
	            timeLabel.setForeground(Color.WHITE);
	            timeLabel.setBounds(350, 20, 100, 30);
	            add(timeLabel);
	            // Countdown timer that updates every second.
	            countdownTimer = new Timer(1000, new ActionListener() {
	                public void actionPerformed(ActionEvent e) {
	                    timeRemaining--;
	                    timeLabel.setText("Time: " + timeRemaining);
	                    if (timeRemaining <= 0) {
	                        countdownTimer.stop();
	                    }
	                }
	            });
	            countdownTimer.start();
	            
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
				            if (!f.getIsSliced() && f.contains(mouseX, mouseY)) {
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
								
	                            if (gameTimer != null) {
	                                gameTimer.stop();
	                            }
	                            if (countdownTimer != null) {
	                                countdownTimer.stop();
	                            }
								startExplosionAnimation();
								points = 0;
								break;
							}
						}
					}
				});
			}

	        /**
             * Starts the bomb explosion animation.
             * <p>
             * This method sets the explosion flag and initiates a timer that updates the explosion frame
             * at regular intervals. Once the animation is complete, the game is declared over and the user is returned to the main menu.
             * </p>
             */
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
			
			/**
             * Overrides the  paintComponent method to render the game components.
             * <p>
             * This method draws the background, fruits, bombs, and explosion animation (if active).
             * </p>
             *
             * @param g is the Graphics object to protect
             */
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
	
	/**
     * Initializes and displays the leaderboard panel.
     * <p>
     * This panel currently contains a back button for returning to the main menu.
     * </p>
     */
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
	/**
     * Invoked when an action occurs.
     * <p>
     * Handles button click events from the main menu. Depending on the source,
     * it transitions to the game panel, rules panel, or leaderboard panel.
     * </p>
     *
     * @param e the event to be processed
     */
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
