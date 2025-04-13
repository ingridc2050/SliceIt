package sliceit;

import java.awt.Graphics;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The Fruit class represents a fruit object in the game that moves with a given
 * velocity and is affected by gravity. Each fruit has a position, size, image,
 * and velocity in both x and y directions.
 * 
 * <p>
 * Fruits can be updated (to move their position), drawn on the screen, checked
 * for off-screen status, and tested for whether a point lies within them
 */
public class Fruit {
	/** The x-coordinate and y-coordinate of the fruit */
	private int x, y;
	/** The width and height of the fruit */
	private float width, height;
	/** The horizontal and vertical velocity of the fruit. */
	private float velocityX, velocityY;
	/** The image representing the fruit. */
	private BufferedImage image;
	/** The image representing the fruit after it has been sliced. */
	private BufferedImage slicedImage;
	/** boolean that indicates whether the fruit has been sliced. */
	private boolean isSliced = false;

	/**
	 * Constructs a new Fruit object with the specified image, position, and
	 * velocity.
	 * 
	 * @param the       image representing the fruit
	 * @param x         the initial x-coordinate of the fruit
	 * @param y         the initial y-coordinate of the fruit
	 * @param velocityX the horizontal velocity of the fruit
	 * @param velocityY the vertical velocity of the fruit
	 */
	public Fruit(BufferedImage image, BufferedImage slicedImage, int x, int y, float velocityX, float velocityY) {
		this.image = image;
		this.x = x;
		this.y = y;

		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.width = image.getWidth();
		this.height = image.getHeight();

		this.slicedImage = slicedImage;

	}

	/**
	 * This method updates the fruit's position based on its current velocity and
	 * applies gravity to the vertical velocity.
	 */
	public void update() {
		x += velocityX;
		y += velocityY;
		velocityY += 0.3f; // gravity application
	}

	/**
	 * Draws the fruit on the screen using the provided Graphics2D object.
	 * 
	 * @param g2d the graphics context used to draw the fruit
	 */
	public void draw(Graphics2D g2d) {
		if (isSliced) {
			g2d.drawImage(slicedImage, x, y, null);
		} else {
			g2d.drawImage(image, x, y, null);
		}
	}

	/**
	 * Slices the fruit by setting its sliced state. If the fruit has not been
	 * sliced yet, it updates the state to indicate that the fruit is now sliced.
	 */
	public void slice() {
		if (!isSliced) {
			isSliced = true; // Switch to sliced image
		}
	}

	/**
	 * Determines whether the fruit has moved off-screen based on the dimensions of
	 * the panel.
	 * 
	 * @param panelWidth  the width of the panel
	 * @param panelHeight the height of the panel
	 * @return true if the fruit is completely off-screen; false otherwise
	 */
	public boolean isOffScreen(int panelWidth, int panelHeight) {
		return (x + width < 0 || x - width > panelWidth || y - height > panelHeight);
	}

	/**
	 * Checks whether the fruit contains the specified point. This is for detecting
	 * if a user interaction occurred over the fruit.
	 * 
	 * @param mx is the x-coordinate of the point to check
	 * @param my is the y-coordinate of the point to check
	 * @return true if the specified point is within the fruit's boundaries; false
	 *         otherwise
	 */
	public boolean contains(int mx, int my) {
		return mx >= x && mx <= x + width && my >= y && my <= y + height;
	}

	/**
	 * Returns the current x-coordinate of the fruit.
	 *
	 * @return the current x-coordinate
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the x-coordinate of the fruit.
	 *
	 * @param x the new x-coordinate to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Returns the current y-coordinate of the fruit.
	 *
	 * @return the current y-coordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the y-coordinate of the fruit.
	 *
	 * @param y the new y-coordinate to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * Returns the width of the fruit. The width is derived from the fruit's image.
	 *
	 * @return the width of the fruit
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * Returns the height of the fruit. The height is derived from the fruit's
	 * image.
	 *
	 * @return the height of the fruit
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * Returns the horizontal velocity of the fruit.
	 *
	 * @return the current horizontal velocity
	 */
	public float getVelocityX() {
		return velocityX;
	}

	/**
	 * Returns the vertical velocity of the fruit.
	 *
	 * @return the current vertical velocity
	 */
	public float getVelocityY() {
		return velocityY;
	}

	/**
	 * Returns the image representing the fruit.
	 *
	 * @return the fruit's image
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * Returns the image representing the sliced state of the fruit.
	 *
	 * @return the sliced fruit's image
	 */
	public BufferedImage getSlicedImage() {
		return slicedImage;
	}

	/**
	 * Checks whether the fruit has been sliced.
	 *
	 * @return true if the fruit has been sliced; false otherwise
	 */
	public boolean getIsSliced() {
		return isSliced;
	}

}
