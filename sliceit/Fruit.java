package sliceit;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;


/**
 * The Fruit class represents a fruit object in the game that moves with
 * a given velocity and is affected by gravity. Each fruit has a position, size,
 * image, and velocity in both x and y directions.
 * 
 * <p>Fruits can be updated (to move their position), drawn on the screen,
 * checked for off-screen status, and tested for whether a point lies within them
 */
public class Fruit {
	    private int x, y;
	    private float width, height;
	    private float velocityX, velocityY;
	    private BufferedImage image;
	    
	    /**
	     * Constructs a new Fruit object with the specified image, position,
	     * and velocity.
	     * 
	     * @param image the image representing the fruit
	     * @param x the initial x-coordinate of the fruit
	     * @param y the initial y-coordinate of the fruit
	     * @param velocityX the horizontal velocity of the fruit
	     * @param velocityY the vertical velocity of the fruit
	     */
	    public Fruit(BufferedImage image, int x, int y, float velocityX, float velocityY) {
	        this.image = image;
	        this.x = x;
	        this.y = y;
	        
	        this.velocityX = velocityX;
	        this.velocityY = velocityY;
	        this.width = image.getWidth();
	        this.height = image.getHeight();
	    }
	    


	    /**
	     * This method updates the fruit's position based on its current velocity and applies
	     * gravity to the vertical velocity.
	     */
	    public void update() {
	         x += velocityX;
	         y += velocityY;
	         velocityY += 0.5f;
	    }
	    
	    
	    /**
	     * Draws the fruit on the screen using the provided Graphics2D object.
	     * 
	     * @param g2d the graphics context used to draw the fruit
	     */
	    public void draw(Graphics2D g2d) {
  
            g2d.drawImage(image, x,y,null);
            
        }
       
	    /**
	     * Determines whether the fruit has moved off-screen based on the dimensions
	     * of the panel.
	     * 
	     * @param panelWidth the width of the panel
	     * @param panelHeight the height of the panel
	     * @return true if the fruit is completely off-screen; false otherwise
	     */
	    public boolean isOffScreen(int panelWidth, int panelHeight) {
            return (x + width < 0 || x - width > panelWidth || y - height > panelHeight);
        }
       

	    
	    
	

}
