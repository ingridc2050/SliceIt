package sliceit;

import java.awt.Color;
import javax.sound.sampled.*;
import java.awt.Dimension;
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
	/** The sprite sheet image containing fruit graphics. */
	private BufferedImage spriteSheet;
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

		ImageIcon playIcon = new ImageIcon("images/playButton.png");
		gameButton = new JButton(playIcon);
		gameButton.setBounds(185, 200, 140, 37);
		gameButton.addActionListener(this);
		mainPanel.add(gameButton);

		ImageIcon ruleIcon = new ImageIcon("images/rulesButton.png");
		rulesButton = new JButton(ruleIcon);
		rulesButton.setBounds(185, 240, 140, 37);
		rulesButton.addActionListener(this);
		mainPanel.add(rulesButton);

		ImageIcon leadIcon = new ImageIcon("images/leaderBoardButton.png");
		leaderButton = new JButton(leadIcon);
		leaderButton.setBounds(176, 280, 158, 37);
		leaderButton.addActionListener(this);
		mainPanel.add(leaderButton);

		playBackgroundMusic("songs/Pocketful of Sunshine.wav");

		gameJFrame.setVisible(true);
	}

	/**
	 * A helper panel that draws a background image.
	 */
	class BackgroundPanel extends JPanel {
		private BufferedImage bgImage;

		public BackgroundPanel(String imagePath) {
			// Load background image (inlined try-catch; no extra block)
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
			File audioFile = new File(filepath);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
			backgroundClip = AudioSystem.getClip();
			backgroundClip.open(audioStream);
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
			BufferedImage spriteSheet = ImageIO.read(new File("images/fruits.png"));
			int fruitWidth = 101;
			int fruitHeight = 85;
			int rows = 7;

			unslicedFruits = new BufferedImage[rows];
			slicedFruits = new BufferedImage[rows];

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
		rulesPanel = new BackgroundPanel("images/rulesBackground.png");
		rulesPanel.setLayout(null);

		// Back button to return to the main panel.
		JButton backButton = new JButton("BACK");
		backButton.setBounds(380, 400, 100, 40);
		backButton.setBackground(Color.WHITE); // You can use any predefined color or create your own

		backButton.setForeground(new Color(255, 105, 180));

		backButton.addActionListener(e -> returnToMainPanel());
		rulesPanel.add(backButton);

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

				// Update fruit positions and remove off-screen fruits.
				for (int i = fruits.size() - 1; i >= 0; i--) {
					Fruit f = fruits.get(i);
					f.update();
					if (f.isOffScreen(gamePanel.getWidth(), gamePanel.getHeight())) {
						fruits.remove(i);
					}
				}

				// Update bombs.
				Iterator<Bomb> bombIterator = bombs.iterator();
				while (bombIterator.hasNext()) {
					Bomb b = bombIterator.next();
					b.update();
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
				if (!gameOver) {
					timer.stop();
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
		timeLabel.setForeground(Color.WHITE);
		timeLabel.setBounds(350, 20, 100, 30);
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
				int mouseX = e.getX();
				int mouseY = e.getY();

				// Check for fruit slicing.
				for (Fruit f : fruits) {
					if (!f.getIsSliced() && f.contains(mouseX, mouseY)) {
						f.slice();
						points += 10;
						pointLabel.setText("Score: " + points);
					}
				}

				// Check for bomb slicing.
				for (Iterator<Bomb> it = bombs.iterator(); it.hasNext();) {
					Bomb b = it.next();
					int bombLeft = b.getX();
					int bombRight = b.getX() + b.getImage().getWidth(null);
					int bombTop = b.getY();
					int bombBottom = b.getY() + b.getImage().getHeight(null);
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
						startExplosionAnimation();
						points = 0;
						break;
					}
				}
			}
		});

		// Score label.
		pointLabel = new JLabel("Score: " + points);
		pointLabel.setForeground(Color.WHITE);
		pointLabel.setBounds(20, 20, 100, 30);
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
			// Load background image (inlined try-catch)
			try {
				backgroundImage = ImageIO.read(new File("images/playBackground.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
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
				explosionFrame++;
				if (explosionFrame >= bombExplosionFrames.length) {
					explosionTimer.stop();
					JOptionPane.showMessageDialog(gameJFrame, "Game Over! You sliced a bomb!");
					leaderboardData.add(username + " - " + points + " pts");
					returnToMainPanel();
				} else {
					gamePanel.repaint();
				}
			}
		});
		explosionTimer.start();
	}

	/**
	 * Displays the leaderboard panel.
	 */
	private void leaderboardPanel() {
		leaderBoardPanel = new BackgroundPanel("images/leaderBoardbackground.png");
		leaderBoardPanel.setLayout(null);
		leaderBoardPanel.setBackground(Color.pink);

		JList<String> leaderBoard = new JList<>(leaderboardData.toArray(new String[0]));
		JScrollPane scrollPane = new JScrollPane(leaderBoard);
		scrollPane.setBounds(135, 145, 210, 180);
		leaderBoardPanel.add(scrollPane);

		JButton backButton = new JButton("BACK");
		backButton.setBounds(185, 350, 130, 40);
		backButton.addActionListener(e -> returnToMainPanel());

		backButton.setBackground(Color.WHITE); // You can use any predefined color or create your own
		backButton.setForeground(new Color(255, 105, 180)); // Neon pink vibe

		leaderBoardPanel.add(backButton);

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
		if (e.getSource() == gameButton) {
			// Prompt for username until a non-blank value is entered.
			while (username == null || username.trim().isEmpty()) {
				username = JOptionPane.showInputDialog(null, "Enter your username");
				if (username == null) {
					JOptionPane.showMessageDialog(gameJFrame, "You must enter a username to play!");
				} else if (username.trim().isEmpty()) {
					JOptionPane.showMessageDialog(gameJFrame, "Username can't be blank!");
				}
			}
			gamePanel(); // launch the game panel
		} else if (e.getSource() == rulesButton) {
			rulesPanel();
		} else if (e.getSource() == leaderButton) {
			leaderboardPanel();
		}
	}
}
