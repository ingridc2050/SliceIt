package sliceit;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * The Bomb class represents a bomb object in the game.
 * It handles movement, rendering, and hit detection.
 */
public class Bomb {
	private int x, y;
    private float width, height;
    private float velocityX, velocityY;
    private BufferedImage image;
    private boolean bombHit = false;
	
    /**
     * Constructs a Bomb Object with specified parameters.
     *
     * @param image     The image representing the bomb.
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
        this.width = image.getWidth();
        this.height = image.getHeight();
	}
	/**
     * Checks if the bomb is off the screen or not.
     * @param panelWidth The width of the game panel
     * @param panelHeight The height of the game panel
     * @return returns boolean true or false stating if the bomb is off the screen or not
     */
	public boolean isOffScreen(int panelWidth, int panelHeight) {
        return y > panelHeight;
    }
	
    /**
     * Updates the bomb position based on the velocity of the bomb.
     * Applies gravity to the bomb. 
     */
	public void update() {
        x += velocityX;
        y += velocityY;
        velocityY += 0.5f;
   }
	/**
     * Draws the bomb onto the screen.
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
     * @return Returns true if the point is within the bomb's boundaries and false otherwise.
     */
	public boolean isHit(int clickX, int clickY) {
        return clickX >= x && clickX <= x + image.getWidth() &&
               clickY >= y && clickY <= y + image.getHeight();
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

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	}
}
