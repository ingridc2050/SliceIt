package sliceit;
import java.awt.Image;
public class Fruit {
	    private float x, y;
	    private float velocityX, velocityY;
	    private boolean isSliced = false;
	    final double gravity=1;
	    private Image image;
	    
	    private void update() {
	    	
	    }
	    
       private void draw() {
	    	
	    }
       public void slice() {
           if (!isSliced) {
               isSliced = true;
    
               System.out.println("Fruit sliced");
           }
       }
	    
       public boolean isOffScreen() {
           return false;
       }
       
       public boolean contains() {
    	   return false;
       }
	    
	public static void main(String[] args) {
		

	}

}
