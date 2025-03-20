package sliceit;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
public class Fruit {
	    private int x, y;
	    private float width, height;
	    private float velocityX, velocityY;
	    private BufferedImage image;
	    
	    
	    public Fruit(BufferedImage image, int x, int y, float velocityX, float velocityY) {
	        this.image = image;
	        this.x = x;
	        this.y = y;
	        
	        this.velocityX = velocityX;
	        this.velocityY = velocityY;
	        this.width = image.getWidth();
	        this.height = image.getHeight();
	    }

	    
	    public void update() {
	         x += velocityX;
	         y += velocityY;
	         velocityY += 0.5f;
	    }
	    
	    public void draw(Graphics2D g2d) {
  
            g2d.drawImage(image, x,y,null);
            
        }
       
	    
	    public boolean isOffScreen(int panelWidth, int panelHeight) {
            return (x + width < 0 || x - width > panelWidth || y - height > panelHeight);
        }
       
	 // Check if the point (mx, my) is inside this object
        public boolean contains(int mx, int my) {
            int dx = mx - x;
            int dy = my - y;
            return dx * dx + dy * dy <= width* height;
        }
	    
	

}
