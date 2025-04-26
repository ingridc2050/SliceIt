package sliceit;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.sound.sampled.*;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * This controller class serves as the controller for the SliceIt game. It
 * handles the main GUI components, game logic, and user interactions.
 * <p>
 * This class implements ActionListener and manages navigation between the game,
 * rules, and leaderboard panels. It also loads images for fruits, bombs, and
 * animations, and controls game events such as spawning fruits, handling
 * slices, and bomb explosions.
 * </p>
 */
public class SliceItController implements ActionListener {

	/** The main game window. */
	private final JFrame gameJFrame;
	/** The main menu panel. */
	private JPanel mainPanel;
	/** Panel displaying game rules. */
	private JPanel rulesPanel;
	/** Panel displaying the leaderboard. */
	private JPanel leaderBoardPanel;
	/** The image representing a bomb object in the game. */
	private BufferedImage bomb;
	/** Array holding images of unsliced fruits. */
	private BufferedImage[] unslicedFruits;
	/** Array holding images of sliced fruits. */
	private BufferedImage[] slicedFruits;
	/** Array holding frames for bomb explosion animation. */
	private BufferedImage[] bombExplosionFrames;
	/** Background image used in gameplay screen. */
	private BufferedImage backgroundImage;
	/** Button to start the game. */
	private JButton gameButton;
	/** Button to show the rules panel. */
	private JButton rulesButton;
	/** Button to show the leaderboard panel. */
	private JButton leaderButton;
	/** Player's current score during the game. */
	private int points = 0;
	/** Label to display the current score. */
	private JLabel pointLabel;
	/** Stores the username entered by the player. */
	private String username;
	/** Stores leaderboard entries containing usernames and scores. */
	private List<String> leaderboardData = new ArrayList<>();
	/** Background music clip playing during the game. */
	private Clip backgroundClip;
	/** X-coordinate of the bomb explosion animation. */
	private int explosionX;
	/** Y-coordinate of the bomb explosion animation. */
	private int explosionY;
	/** Flag indicating whether the explosion should be shown. */
	private boolean showExplosion = false;
	/** Index of the current explosion animation frame. */
	private int explosionFrame = 0;
	/** Timer controlling the explosion animation. */
	private Timer explosionTimer;
	/** Flag indicating whether the game is over. */
	private boolean gameOver = false;
	/** List containing all the fruits currently on the screen. */
	private List<Fruit> fruits = new ArrayList<>();
	/** List containing all the bombs currently on the screen. */
	private List<Bomb> bombs = new ArrayList<>();
	/** Random number generator for spawning fruits and bombs. */
	private Random rand = new Random();
	/** Timer controlling the overall game duration. */
	private Timer gameTimer;
	/** Time remaining in the game (in seconds). */
	private int timeRemaining = 60;
	/** Label displaying the remaining time. */
	private JLabel timeLabel;
	/** Timer updating the countdown clock every second. */
	private Timer countdownTimer;
	/** Custom panel where the gameplay graphics are drawn. */
	private GamePanel gamePanel;

	/**
	 * The main entry point for the SliceIt game.
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
	 * Constructs a new SliceItController object.
	 *
	 * This constructor initializes the main JFrame, sets up the primary menu panel,
	 * loads necessary images, and creates the navigation buttons.
	 */
	public SliceItController() {
		//create your frame
		gameJFrame = new JFrame();
		gameJFrame.setSize(500, 500);
		gameJFrame.setLocation(50, 50);
		gameJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		// Create a main panel that shows a welcome background.
		mainPanel = new BackgroundPanel("images/welcomee.png");

		// Add main panel to the frame (only once)
		gameJFrame.getContentPane().add(mainPanel);

		// Load images needed for the game.
		loadSlicedFruitImages();
		loadBombImage();
		loadBombExplosionImages();

		// Set up the main buttons with transparent backgrounds.

		//play button
		ImageIcon playIcon = new ImageIcon("images/playButton.png");
		gameButton = new JButton(playIcon);
		gameButton.setBounds(185, 200, 140, 37);
		gameButton.addActionListener(this);
		mainPanel.add(gameButton);

		//rule button
		ImageIcon ruleIcon = new ImageIcon("images/rulesButton.png");
		rulesButton = new JButton(ruleIcon);
		rulesButton.setBounds(185, 240, 140, 37);
		rulesButton.addActionListener(this);
		mainPanel.add(rulesButton);

		//leaderboard button
		ImageIcon leadIcon = new ImageIcon("images/leaderBoardButton.png");
		leaderButton = new JButton(leadIcon);
		leaderButton.setBounds(176, 280, 158, 37);
		leaderButton.addActionListener(this);
		mainPanel.add(leaderButton);

		/**
		 * Background Music Credit:
		 * "Pocketful of Sunshine" by Natasha Bedingfield
		 * © 2008 Sony Music Entertainment / Epic Records
		 * This song is used for educational/non-commercial purposes only.
		 */
		playBackgroundMusic("songs/Pocketful of Sunshine.wav");

		gameJFrame.setVisible(true);
	}

	/**
	 * A helper panel that draws a background image.
	 */
	class BackgroundPanel extends JPanel {
		/** Background image for background of any window*/
		private BufferedImage bgImage;

		public BackgroundPanel(String imagePath) {
			// Load background image 
			try {
				bgImage = ImageIO.read(new File(imagePath));
			} catch (IOException e) {
				e.printStackTrace();
			}
			setLayout(null);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (bgImage != null) {
				g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
			}
		}
	}

	/**
	 * Plays the specified background music file.
	 */
	private void playBackgroundMusic(String filepath) {
		try {
			//load the file and grab the clip from the filepath
			File audioFile = new File(filepath);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
			backgroundClip = AudioSystem.getClip();
			backgroundClip.open(audioStream);
			//makes sure the song is always playing
			backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads fruit images (both unsliced and sliced) from a sprite sheet.
	 */
	private void loadSlicedFruitImages() {
		try {
			//sheet for sliced fruits
			BufferedImage spriteSheet = ImageIO.read(new File("images/fruits.png"));
			int fruitWidth = 101;
			int fruitHeight = 85;
			int rows = 7;

			unslicedFruits = new BufferedImage[rows];
			slicedFruits = new BufferedImage[rows];

			//grab the image based on what fruit is sliced
			for (int i = 0; i < rows; i++) {
				unslicedFruits[i] = spriteSheet.getSubimage(0, i * fruitHeight, fruitWidth, fruitHeight);
				slicedFruits[i] = spriteSheet.getSubimage(105, i * fruitHeight, fruitWidth + 10, fruitHeight);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads the bomb image.
	 */
	private void loadBombImage() {
		try {
			bomb = ImageIO.read(new File("images/bombimg.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads the bomb explosion images from a sprite sheet.
	 */
	private void loadBombExplosionImages() {
		try {
			//sheet for bomb explosion
			BufferedImage spriteSheet = ImageIO.read(new File("images/bombSprites.png"));
			int rows = 4;
			int cols = 4;
			int totalFrames = rows * cols;

			int frameWidth = spriteSheet.getWidth() / cols;
			int frameHeight = spriteSheet.getHeight() / rows;

			bombExplosionFrames = new BufferedImage[totalFrames];
			int index = 0;
			//gets image based on what frame it is and displays it on screen
			for (int row = 0; row < rows; row++) {
				for (int col = 0; col < cols; col++) {
					bombExplosionFrames[index] = spriteSheet.getSubimage(col * frameWidth, row * frameHeight,
							frameWidth, frameHeight);
					index++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Displays the rules panel.
	 */
	private void rulesPanel() {
		rulesPanel = new BackgroundPanel("images/rulesScreen.png");
		rulesPanel.setLayout(null);

		// Back button to return to the main panel.
		JButton backButton = new JButton("BACK");
		backButton.setBounds(380, 400, 100, 40);
		backButton.setBackground(Color.WHITE); 
		backButton.setForeground(new Color(255, 105, 180));

		//when you hit the back button you will go back to the main menu
		backButton.addActionListener(e -> returnToMainPanel());
		rulesPanel.add(backButton);

		//make main menu pop up
		gameJFrame.getContentPane().removeAll();
		gameJFrame.getContentPane().add(rulesPanel);
		gameJFrame.revalidate();
		gameJFrame.repaint();
	}

	/**
	 * Returns to the main menu panel.
	 */
	private void returnToMainPanel() {
		gameJFrame.getContentPane().removeAll();
		gameJFrame.getContentPane().add(mainPanel);
		gameJFrame.revalidate();
		gameJFrame.repaint();
	}

	/**
	 * Initializes and displays the game panel where gameplay occurs.
	 */
	private void gamePanel() {
		// Create a new instance of our custom GamePanel.
		gamePanel = new GamePanel();

		// Start a timer that updates the game logic.
		Timer timer = new Timer(20, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Occasionally spawn a new fruit.
				if (rand.nextDouble() < 0.05) {
					int panelWidth = gamePanel.getWidth();
					int fruitIndex = rand.nextInt(unslicedFruits.length);
					BufferedImage img = unslicedFruits[fruitIndex];
					int x = rand.nextInt(Math.max(panelWidth - img.getWidth(), 1));
					int y = gamePanel.getHeight() - img.getHeight();
					float velocityY = -(float) (rand.nextDouble() * 5 + 10);
					float velocityX = (float) (rand.nextDouble() * 4 - 2);
					BufferedImage slicedImg = slicedFruits[fruitIndex];
					Fruit fruit = new Fruit(img, slicedImg, x, y, velocityX, velocityY);
					fruits.add(fruit);
				}

				// Update fruit positions
				for (int i = fruits.size() - 1; i >= 0; i--) {
					Fruit f = fruits.get(i);
					f.update();
					//remove off-screen fruits
					if (f.isOffScreen(gamePanel.getWidth(), gamePanel.getHeight())) {
						fruits.remove(i);
					}
				}

				// Go through bombs
				Iterator<Bomb> bombIterator = bombs.iterator();
				while (bombIterator.hasNext()) {
					Bomb b = bombIterator.next();
					b.update();
					//remove when off screen
					if (b.getY() > gamePanel.getHeight()) {
						bombIterator.remove();
					}
				}

				// Occasionally spawn a new bomb.
				if (rand.nextDouble() < 0.02 && bomb != null) {
					int panelWidth = gamePanel.getWidth();
					int x = rand.nextInt(Math.max(panelWidth - bomb.getWidth(), 1));
					int y = gamePanel.getHeight() - bomb.getHeight();
					float velocityY = -(float) (rand.nextDouble() * 5 + 10);
					float velocityX = (float) (rand.nextDouble() * 4 - 2);
					Bomb newBomb = new Bomb(bomb, x, y, velocityX, velocityY);
					bombs.add(newBomb);
				}
				// Repaint the game panel.
				gamePanel.repaint();
			}
		});
		timer.start();

		// Game timer (one minute duration).
		gameTimer = new Timer(60000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//stop time if game is over
				if (!gameOver) {
					timer.stop();
					//give player their points and add to leaderboard
					JOptionPane.showMessageDialog(gameJFrame, "You won! Your score: " + points);
					leaderboardData.add(username + " - " + points + " pts");
					returnToMainPanel();
				}
			}
		});
		gameTimer.setRepeats(false);
		gameTimer.start();

		// Time remaining label and countdown timer.
		timeLabel = new JLabel("Time: " + timeRemaining);
		timeLabel.setForeground(Color.black);
		timeLabel.setBounds(300, -7, 120, 30);
		Font titleFontTime = new Font("Arial Black", Font.BOLD, 24);
		timeLabel.setFont(titleFontTime);
		gamePanel.add(timeLabel);
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

		// Add mouse motion listener to detect dragging (slicing).
		gamePanel.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (gameOver) {
					return;
				}
				//variables for coordinates of mouse clicking
				int mouseX = e.getX();
				int mouseY = e.getY();

				// Check if held down mouse is in same coordinate as fruit
				for (Fruit f : fruits) {
					if (!f.getIsSliced() && f.contains(mouseX, mouseY)) {
						f.slice();
						//if so add points
						points += 10;
						pointLabel.setText("Score: " + points);
					}
				}

				// Check for bomb slicing.
				for (Iterator<Bomb> it = bombs.iterator(); it.hasNext();) {
					Bomb b = it.next();
					// coordinates of bomb
					int bombLeft = b.getX();
					int bombRight = b.getX() + b.getImage().getWidth(null);
					int bombTop = b.getY();
					int bombBottom = b.getY() + b.getImage().getHeight(null);
					//check if mouse has hit the bomb
					if (mouseX >= bombLeft && mouseX <= bombRight && mouseY >= bombTop && mouseY <= bombBottom) {
						explosionX = b.getX() + (b.getImage().getWidth(null) - bombExplosionFrames[0].getWidth()) / 2;
						explosionY = b.getY() + (b.getImage().getHeight(null) - bombExplosionFrames[0].getHeight()) / 2;
						it.remove();
						gameOver = true;
						timer.stop();
						if (gameTimer != null) {
							gameTimer.stop();
						}
						if (countdownTimer != null) {
							countdownTimer.stop();
						}
						//if so then start the explosion
						startExplosionAnimation();
						break;
					}
				}
			}
		});

		// Score label.
		pointLabel = new JLabel("Score: " + points);
		pointLabel.setForeground(Color.black);
		Font titleFont = new Font("Arial Black", Font.BOLD, 24);
		pointLabel.setFont(titleFont);
		pointLabel.setBounds(0, -7, 160, 30);
		gamePanel.add(pointLabel);

		// Switch the frame content to the game panel.
		gameJFrame.getContentPane().removeAll();
		gameJFrame.getContentPane().add(gamePanel);
		gameJFrame.pack();
		gameJFrame.revalidate();
		gameJFrame.repaint();
	}

	/**
	 * Custom GamePanel class that handles all game drawing.
	 */
	private class GamePanel extends JPanel {
		public GamePanel() {
			setLayout(null);
			setBackground(Color.pink);
			setPreferredSize(new Dimension(500, 500));
			// Load background image 
			try {
				backgroundImage = ImageIO.read(new File("images/playBackgrnd.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * Draws background, fruits, bombs, and explosion onto the game panel.
		 */
		@Override
		protected void paintComponent(Graphics g) {
			//clears panel
			super.paintComponent(g);
			//draw background image
			if (backgroundImage != null) {
				g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
			}

			//draw fruit
			Graphics2D g2d = (Graphics2D) g;
			for (Fruit f : fruits) {
				f.draw(g2d);
			}
			//draw bombs
			for (Bomb b : bombs) {
				g2d.drawImage(b.getImage(), b.getX(), b.getY(), null);
			}
			//draw explosion if needed
			if (showExplosion && bombExplosionFrames != null && explosionFrame < bombExplosionFrames.length) {
				g2d.drawImage(bombExplosionFrames[explosionFrame], explosionX, explosionY, null);
			}
		}
	}
	
	
	

	/**
	 * Starts the bomb explosion animation.
	 */
	private void startExplosionAnimation() {
		showExplosion = true;
		explosionFrame = 0;

		explosionTimer = new Timer(10, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//go through the frames of the explosion
				explosionFrame++;
				//this tells when to stop going through frames
				if (explosionFrame >= bombExplosionFrames.length) {
					//stop game and show points
					explosionTimer.stop();
					JOptionPane.showMessageDialog(gameJFrame, "Game Over! You sliced a bomb!");
					//leaderboardData.add(username + " - " + points + " pts");
					 updateLeaderboard();
					int reply = JOptionPane.showConfirmDialog(gameJFrame, "Would you like to play again?", "Play again?", JOptionPane.YES_NO_OPTION);
					if (reply == 1) {
						returnToMainPanel();
					} else {
						points = 0;
						timeRemaining = 60;
						fruits.clear();
						bombs.clear();
						gameOver = false;
						gamePanel();
					}
					
				} else {
					gamePanel.repaint();
				}
			}
		});
		explosionTimer.start();

		//stop game and countdown timers
		if (gameTimer != null) { 
			gameTimer.stop();
		}
		if (countdownTimer != null) { 
			countdownTimer.stop();
		}
	}
	
	

	/**
	 * Updates the leaderboard with the player's latest score.
	 */
	private void updateLeaderboard() {
	    // Create a flag to check if the username already exists in the leaderboard
	    boolean userFound = false;
	    
	    // Iterate over the leaderboard data to check if the username exists
	    for (int i = 0; i < leaderboardData.size(); i++) {
	        String entry = leaderboardData.get(i);
	        String existingUsername = entry.split(" - ")[0].trim(); // Extract the username from the string
	        if (existingUsername.equalsIgnoreCase(username)) {
	            // If username found, update the score
	            leaderboardData.set(i, username + " - " + points + " pts");
	            userFound = true;
	            break;
	        }
	    }
	    
	    // If the username doesn't exist, add a new entry
	    if (!userFound) {
	        leaderboardData.add(username + " - " + points + " pts");
	    }
	    
	    // Sort the leaderboard by score in descending order
	    leaderboardData.sort((a, b) -> {
	        int scoreA = Integer.parseInt(a.split(" - ")[1].replace(" pts", "").trim());
	        int scoreB = Integer.parseInt(b.split(" - ")[1].replace(" pts", "").trim());
	        return Integer.compare(scoreB, scoreA);  // Sort in descending order
	    });
	    
	    // Keep only the top 5 scores
	    if (leaderboardData.size() > 5) {
	        leaderboardData = leaderboardData.subList(0, 5);
	    }
	}
	
	



	
	/**
	 * Displays the leaderboard panel with top 5 scores.
	 */
	private void leaderboardPanel() {
	    // Initialize panel
	    leaderBoardPanel = new BackgroundPanel("images/leaderboardbackgrnd.png");
	    leaderBoardPanel.setLayout(null);
	    leaderBoardPanel.setBackground(Color.pink);

	    // Create a JList to display the top 5 leaderboard entries
	    JList<String> leaderBoard = new JList<>(leaderboardData.toArray(new String[0]));
	    leaderBoard.setFont(new Font("Arial", Font.PLAIN, 18));
	    leaderBoard.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    
	    // Add leaderboard list to scroll pane
	    JScrollPane scrollPane = new JScrollPane(leaderBoard);
	    scrollPane.setBounds(135, 165, 210, 180);
	    leaderBoardPanel.add(scrollPane);
	    
	    // Add "Back" button
	    JButton backButton = new JButton("BACK");
	    backButton.setBounds(185, 370, 130, 40);
	    backButton.addActionListener(e -> returnToMainPanel());
	    backButton.setBackground(Color.WHITE);
	    backButton.setForeground(new Color(255, 105, 180));
	    leaderBoardPanel.add(backButton);

	    // Update the main frame with leaderboard panel
	    gameJFrame.getContentPane().removeAll();
	    gameJFrame.getContentPane().add(leaderBoardPanel);
	    gameJFrame.revalidate();
	    gameJFrame.repaint();
	}


	/**
	 * Handles button click events from the main menu.
	 */
	@Override
	
	public void actionPerformed(ActionEvent e) {
	    Object source = e.getSource();
	    //brings you to game panel
	    if (source == gameButton) {
	        username = JOptionPane.showInputDialog(gameJFrame, "Enter your name:");

	        // Check if the username is empty or null
	        if (username == null || username.trim().isEmpty()) {
	            JOptionPane.showMessageDialog(gameJFrame, "Please enter something in the box or . You cannot have a blank username.");
	            username = JOptionPane.showInputDialog(gameJFrame, "Enter your name:");
                points = 0;
                timeRemaining = 60;
                fruits.clear();
                bombs.clear();
                gameOver = false;
                gamePanel();
	        } else {
	            boolean usernameTaken = false;

	            // Check if the entered username already exists in the leaderboard
	            for (String entry : leaderboardData) {
	                String existingUsername = entry.split(" - ")[0].trim();  // Assuming the format "username - score"
	                if (existingUsername.equalsIgnoreCase(username.trim())) {
	                    usernameTaken = true;
	                    break;
	                }
	            }

	            // If the username is taken, ask the user to choose another one
	            if (usernameTaken) {
	                JOptionPane.showMessageDialog(gameJFrame, "That username is taken. Please choose another username.");
	                username = JOptionPane.showInputDialog(gameJFrame, "Enter your name:");
	                points = 0;
	                timeRemaining = 60;
	                fruits.clear();
	                bombs.clear();
	                gameOver = false;
	                gamePanel();
	            } else {
	                // Reset game state and start the game if the username is unique
	                points = 0;
	                timeRemaining = 60;
	                fruits.clear();
	                bombs.clear();
	                gameOver = false;
	                gamePanel();
	            }
	        }
	    } else if (source == rulesButton) {
	        rulesPanel();
	    } else if (source == leaderButton) {
	        leaderboardPanel();
	    }
	}
}