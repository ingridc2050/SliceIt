package sliceit;
import java.awt.Graphics;


import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class Fruit {
	    private int x, y;
	    private float width, height;
	    private float velocityX, velocityY;
	    private BufferedImage image;
	    private int sliceFrame = 0;
	    private BufferedImage slicedImage;
	    public boolean isSliced = false;
	    private Timer sliceTimer;
	    
	    
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

	    public void update() {
	         x += velocityX;
	         y += velocityY;
	         velocityY += 0.3f; // gravity application
	    }
	    
	    public void draw(Graphics2D g2d) {
	        if (isSliced) {
	            g2d.drawImage(slicedImage, x, y, null);
	        } else {
	            g2d.drawImage(image, x, y, null);
	        }
	    }
            
	    public void slice() {
	        if (!isSliced) {
	            isSliced = true; // Switch to sliced image
	        }
	    }
       
	    public boolean isOffScreen(int panelWidth, int panelHeight) {
	        return (x + width < 0 || x - width > panelWidth || y - height > panelHeight);
	    }

	    public boolean contains(int mx, int my) {
	        return mx >= x && mx <= x + width && my >= y && my <= y + height;
	    }
}
