package sliceit;

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
 * 90% of the comments including JavaDoc, have been written by OpenAI(2025), ChatGPT.
 * Credit for the fruit images:
 * https://www.pinterest.com/pin/450852612678865395/
 */


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
	 * The main entry point launches the Swing application for the SliceIt game.
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
		//creating main frame
		gameJFrame = new JFrame();
		gameJFrame.setSize(500, 500);
		gameJFrame.setLocation(50, 50);
		gameJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

		/**
		 * Background Image Credit:
		 * OpenAI.(2025). ChatGPT [Large Language Model]
		 * Creates a main panel that shows a welcome background.
		 * This image is used for educational/non-commercial purposes only.
		 */
		mainPanel = new BackgroundPanel("images/welcomee.png");

		// Add main panel to the frame (only once)
		gameJFrame.getContentPane().add(mainPanel);

		// Load images needed for the game.
		loadFruitImages();
		loadBombImage();
		loadBombExplosionImages();

		/**
		 * Background Image Credit:
		 * OpenAI.(2025). ChatGPT [Large Language Model]
		 * It is an image for the play button
		 * This image is used for educational/non-commercial purposes only.
		 */
		ImageIcon playIcon = new ImageIcon("images/playButton.png");
		gameButton = new JButton(playIcon);
		gameButton.setBounds(185, 200, 140, 37);
		gameButton.addActionListener(this);
		mainPanel.add(gameButton);

		/**
		 * Background Image Credit:
		 * OpenAI.(2025). ChatGPT [Large Language Model]
		 * It is an image for the rules button
		 * This image is used for educational/non-commercial purposes only.
		 */
		ImageIcon ruleIcon = new ImageIcon("images/rulesButton.png");
		rulesButton = new JButton(ruleIcon);
		rulesButton.setBounds(185, 240, 140, 37);
		rulesButton.addActionListener(this);
		mainPanel.add(rulesButton);

		/**
		 * Background Image Credit:
		 * OpenAI.(2025). ChatGPT [Large Language Model]
		 * It is an image for the leaderboard button
		 * This image is used for educational/non-commercial purposes only.
		 */
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
	 * OpenAI.(2025). ChatGPT [Large Language Model]
	 * When prompted with, "How do I read from my spritesheet attached above?" , this was mostly the generated code. I edited the for loop to handle sliced and unsliced fruit at the same time (the spritesheet was provided) 
	 * Loads fruit images (both unsliced and sliced) from a sprite sheet.
	 */
	private void loadFruitImages() {
		try {
			//sheet for sliced fruits
			BufferedImage spriteSheet = ImageIO.read(new File("images/fruits.png"));
			int fruitWidth = 101;
			int fruitHeight = 85;
			int rows = 7;

			unslicedFruits = new BufferedImage[rows];
			slicedFruits = new BufferedImage[rows];

			
			for (int i = 0; i < rows; i++) {
				// store unsliced fruit image
				unslicedFruits[i] = spriteSheet.getSubimage(0, i * fruitHeight, fruitWidth, fruitHeight);
				 // store sliced fruit image 
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
		/**
		 * Background Image Credit:
		 * OpenAI.(2025). ChatGPT [Large Language Model]
		 * It is an image for the rules screen.
		 * This image is used for educational/non-commercial purposes only.
		 */
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
     * Initializes and starts the gameplay panel with spawning, timers,
     * mouse listeners, and game logic.
     * 
     * OpenAI.(2025). ChatGPT [Large Language Model]
	 * When prompted with, "How do I create objects of fruit and bomb using the images from the lists? I also want them to have velocityX and velocityY", the generated code includes the if statements
     * 
     */
	private void gamePanel() {
		// Create a new instance of our custom GamePanel.
		gamePanel = new GamePanel();

		// Start a timer that updates the game logic.
		Timer timer = new Timer(20, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// spawn a new fruit with a 5% chance each frame
				if (rand.nextDouble() < 0.05) {
					int panelWidth = gamePanel.getWidth();
					// choose a random index for which fruit to spawn
					int fruitIndex = rand.nextInt(unslicedFruits.length);
					 // load the unsliced fruit image at that index
					BufferedImage img = unslicedFruits[fruitIndex];
					// computes a random x‐position so the fruit appears fully within the panel
				    // Math.max ensures the bound is at least 1 to avoid IllegalArgumentException
					int x = rand.nextInt(Math.max(panelWidth - img.getWidth(), 1));
					// start the fruit at the bottom of the panel
					int y = gamePanel.getHeight() - img.getHeight();
					// gives the fruit an initial upward velocity between 10 and 15 pixels/frame
					float velocityY = -(float) (rand.nextDouble() * 5 + 10);
					// give the fruit a random horizontal velocity between -2 and +2 pixels/frame
					float velocityX = (float) (rand.nextDouble() * 4 - 2);
					// load the  sliced‐fruit image for when it’s cut
					BufferedImage slicedImg = slicedFruits[fruitIndex];
					// construct a new Fruit object with the chosen images and velocities
					Fruit fruit = new Fruit(img, slicedImg, x, y, velocityX, velocityY);
					// add the new fruit to the list of active fruits so it will be updated and drawn
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

				// Spawn a new bomb with a 2% chance each frame
				if (rand.nextDouble() < 0.02 && bomb != null) {
					// get the current width of the game panel
					int panelWidth = gamePanel.getWidth();
					// choose a random x‐position so the bomb appears fully within the panel
				    // math.max ensures the bound is at least 1 to avoid an exception
					int x = rand.nextInt(Math.max(panelWidth - bomb.getWidth(), 1));
					// position the bomb so its bottom edge sits at the bottom of the panel
					int y = gamePanel.getHeight() - bomb.getHeight();
					// give the bomb an initial upward velocity between 10 and 15 pixels/frame
					float velocityY = -(float) (rand.nextDouble() * 5 + 10);
					// give the bomb a random horizontal velocity between -2 and +2 pixels/frame
					float velocityX = (float) (rand.nextDouble() * 4 - 2);
					// create a new Bomb object with the chosen position and velocities
					Bomb newBomb = new Bomb(bomb, x, y, velocityX, velocityY);
					// adds the new bomb to the list so it gets updated and drawn each frame
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
					JOptionPane.showMessageDialog(gameJFrame, "You won! Your score: " + points, "CONGRATULATIONS", JOptionPane.INFORMATION_MESSAGE);
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
		//total time decreases by one second every second
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

		 /** 
		  * Add mouse motion listener to detect dragging (slicing).
		  * OpenAI.(2025). ChatGPT [Large Language Model]
		  * When prompted with, "How can I check if mouse has been dragged on the fruit and bomb?" , I provided the fruit and bomb class as well and this was the generated code. 
		  */
		gamePanel.addMouseMotionListener(new MouseAdapter() {
			@Override
			
			/**
			 * Checks if the user sliced a bomb or fruit using mouse events.
			 * @param e       MouseEvent containing details of  drag event
			 */
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
			
			/**
			 * Background Image Credit:
			 * OpenAI.(2025). ChatGPT [Large Language Model]
			 * It is an image for the game screen
			 * This image is used for educational/non-commercial purposes only.
			 */
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
			//OpenAI.(2025). ChatGPT [Large Language Model]
			//When prompted with, "How do I draw the explosion frames for my bomb explosion animation?" , this was the generated code. 
			//draw explosion if needed
			if (showExplosion && bombExplosionFrames != null && explosionFrame < bombExplosionFrames.length) {
				g2d.drawImage(bombExplosionFrames[explosionFrame], explosionX, explosionY, null);
			}
		}
	}
	
	
	

	/**
	 * Handles the bomb explosion animation.
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
					JOptionPane.showMessageDialog(gameJFrame, "Game Over! You sliced a bomb!", "YOU LOST!", JOptionPane.WARNING_MESSAGE);
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
	 * OpenAI.(2025). ChatGPT [Large Language Model]
	 * When prompted with, "How do I make sure that the user's latest score is displayed in the leaderboard and only the top 5 are shown?" , this was the generated code. 
	 */
	private void updateLeaderboard() {
	    // Create a flag to check if the username already exists in the leaderboard
	    boolean userFound = false;
	    
	    // Iterate over the leaderboard data to check if the username exists
	    for (int i = 0; i < leaderboardData.size(); i++) {
	        String entry = leaderboardData.get(i);
	        // Extract the username from the string
	        String existingUsername = entry.split(" - ")[0].trim(); 
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
	    
		/**
		 * Background Image Credit:
		 * OpenAI.(2025). ChatGPT [Large Language Model]
		 * It is an image for the leaderboard screen
		 * This image is used for educational/non-commercial purposes only.
		 */
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
	        // 1) keep prompting until we get a non‐empty, new username
	        while (true) {
	            String input = JOptionPane.showInputDialog(gameJFrame, "Enter your name:", "Name Input", JOptionPane.INFORMATION_MESSAGE );
	            if (input == null) {
	                // user hit “Cancel” → just go back to main menu
	                returnToMainPanel();
	                return;
	            }
	            // Disallow leading or trailing spaces in the username
	            if (!input.equals(input.trim())) {
	                JOptionPane.showMessageDialog(gameJFrame,"Usernames cannot start or end with spaces. Please enter a name without leading or trailing spaces.","Error",JOptionPane.ERROR_MESSAGE);
	                continue;
	            }
	           // Disallow blank usernames (after trimming)
	            if (input.trim().isEmpty()) {
	                JOptionPane.showMessageDialog(gameJFrame,"Please enter something in the box. You cannot have a blank username.","Error", JOptionPane.ERROR_MESSAGE);
	                continue;   // back to top of loop
	            }
	            
	           // Check whether the chosen username already exists in the leaderboard
	            boolean taken = false;
	            for (String entry : leaderboardData) {
	                String existing = entry.split(" - ")[0].trim();
	                if (existing.equalsIgnoreCase(input.trim())) {
	                    taken = true;
	                    break;
	                }
	            }
	           // If the username is already taken, show an error and loop again
	            if (taken) {
	                JOptionPane.showMessageDialog(gameJFrame, "That username is taken. Please choose another username.","Error", JOptionPane.ERROR_MESSAGE );
	                continue;   // back to top of loop
	            }
	            // if we reach here, the name is valid
	            username = input;
	            break;
	        }

	        // 2) now that username is valid, reset your game state and launch
	        points = 0;
	        timeRemaining = 60;
	        fruits.clear();
	        bombs.clear();
	        gameOver = false;
	        gamePanel();
	    }
	
	     else if (source == rulesButton) {
	        rulesPanel();
	    }
	    else if (source == leaderButton) {
	        leaderboardPanel();
	    }
	}
}