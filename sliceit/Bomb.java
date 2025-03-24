package sliceit;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Bomb {
	private int x, y;
    private float width, height;
    private float velocityX, velocityY;
    private BufferedImage image;
    private boolean bombHit = false;
	
	
	public Bomb(BufferedImage image, int x, int y, float velocityX, float velocityY) {
		this.image = image;
        this.x = x;
        this.y = y;
        
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.width = image.getWidth();
        this.height = image.getHeight();
	}
	
	public boolean isOffScreen(int panelWidth, int panelHeight) {
        return y > panelHeight;
    }
	
	public void update() {
        x += velocityX;
        y += velocityY;
        velocityY += 0.5f;
   }
	
	public void draw(Graphics2D g2d) {
        if (!bombHit) {
            g2d.drawImage(image, x, y, null);
        }
    }
	
	public boolean isHit(int clickX, int clickY) {
        return clickX >= x && clickX <= x + image.getWidth() &&
               clickY >= y && clickY <= y + image.getHeight();
    }
	
	public int getX() { return x; }
    public int getY() { return y; }
    public BufferedImage getImage() { return image; }

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
