package sliceit;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * This represents a bomb object in the game. It handles movement, rendering,
 * and hit detection.
 */
public class Bomb {
	/** The x and y coordinates of the bomb */
	private int x, y;
	/** The horizontal and vertical velocity of the bomb */
	private float velocityX, velocityY;
	/** The actual bomb */
	private BufferedImage image;
	/** Was the bomb hit? true of false */
	private boolean bombHit = false;

	/**
	 * Constructs a Bomb Object with specified parameters.
	 *
	 * @param image     Represents the bomb.
	 * @param x         The initial x-coordinate of the bomb.
	 * @param y         The initial y-coordinate of the bomb.
	 * @param velocityX The horizontal velocity of the bomb.
	 * @param velocityY The vertical velocity of the bomb.
	 */
	public Bomb(BufferedImage image, int x, int y, float velocityX, float velocityY) {
		this.image = image;
		this.x = x;
		this.y = y;
		this.velocityX = velocityX;
		this.velocityY = velocityY;
	}

	/**
	 * Checks if the bomb is off the screen or not.
	 * 
	 * @param panelWidth  The width of the game panel
	 * @param panelHeight The height of the game panel
	 * @return returns boolean true or false stating if the bomb is off the screen
	 *         or not
	 */
	public boolean isOffScreen(int panelWidth, int panelHeight) {
		return y > panelHeight;
	}

	/**
	 * Updates the bomb position based on the velocity of the bomb. Applies gravity
	 * to the bomb.
	 */
	public void update() {
		//move bomb horizontally
		x += velocityX;
		//move bomb vertically
		y += velocityY;
		//apply gravity
		velocityY += 0.5f;
	}

	/**
	 * Draws the bomb onto the screen.
	 * 
	 * @param g2d Graphics context used for the game.
	 */
	public void draw(Graphics2D g2d) {
		if (!bombHit) {
			g2d.drawImage(image, x, y, null);
		}
	}

	/**
	 * Checks if a given point is within the bomb's boundaries.
	 *
	 * @param clickX The x-coordinate of the point.
	 * @param clickY The y-coordinate of the point.
	 * @return Returns true if the point is within the bomb's boundaries and false
	 *         otherwise.
	 */
	public boolean isHit(int clickX, int clickY) {
		//Is click in bounds of the bomb?
		return clickX >= x && clickX <= x + image.getWidth() && clickY >= y && clickY <= y + image.getHeight();
	}

	/**
	 * Gets the x-coordinate of the bomb.
	 *
	 * @return The x-coordinate of the bomb.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the y-coordinate of the bomb.
	 *
	 * @return The y-coordinate of the bomb.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Gets the image of the bomb.
	 *
	 * @return The image representing the bomb.
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * Testing for the Bomb Class
	 */
	public static void main(String[] args) {
		boolean testBool = true;
		// Create a Bomb object
		BufferedImage bomb = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);

		Bomb testBomb = new Bomb(bomb, 100, 150, 5.0f, -10.0f);

		// Test Bomb object by checking it's properties
		if (testBomb.getX() != 100) {
			testBool = false;
		}
		if (testBomb.getY() != 150) {
			testBool = false;
		}
		if (testBomb.getImage().getWidth() != 50) {
			testBool = false;
		}
		if (testBomb.getImage().getHeight() != 50) {
			testBool = false;
		}
		if (testBomb.isHit(110, 160) != true) {
			testBool = false;
		}
		if (testBomb.isHit(200, 200) != false) {
			testBool = false;
		}
		if (testBool) {
			System.out.println("All Tests Passed!");
		} else {
			System.out.println("Error in code.");
		}
	}
}
