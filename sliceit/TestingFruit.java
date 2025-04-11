package sliceit;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class TestingFruit {
    public static void main(String[] args) {
        testConstructor();
        testUpdate();
        testSlice();
        testIsOffScreen();
        testContains();
        testDraw();
    }

    // Helper method to create a dummy BufferedImage for testing.
    static BufferedImage createTestImage(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    static void testConstructor() {
        BufferedImage img = createTestImage(50, 50);
        BufferedImage slicedImg = createTestImage(50, 50);
        Fruit fruit = new Fruit(img, slicedImg, 100, 200, 5.0f, -3.0f);

            // Since the images have a width/height of 50 and the constructor is passed x, y, velocityX, velocityY,
            // we expect: x = 100, y = 200, velocityX = 5.0, velocityY = -3.0 and width & height = 50.
         if (fruit.getX() == 100 && fruit.getY()== 200 && fruit.getVelocityX() == 5.0f && fruit.getVelocityY() == -3.0f 
              && fruit.getWidth() == 50 && fruit.getHeight() == 50) {
              System.out.println("testConstructor passed");
         } else {
              System.out.println("testConstructor failed");
            }
         
    }

    static void testUpdate() {
        BufferedImage img = createTestImage(50, 50);
        BufferedImage slicedImg = createTestImage(50, 50);
        // Create fruit with x=100, y=200, velocityX=5.0f, velocityY=-3.0f.
        Fruit fruit = new Fruit(img, slicedImg, 100, 200, 5.0f, -3.0f);
        float initialX=fruit.getX();
        float initialY=fruit.getY();
        float initialVelocityY=fruit.getVelocityY();
      
       fruit.update();

           
           
       if (fruit.getX() == (initialX + 5) && fruit.getY() == (initialY - 3) && Math.abs(fruit.getVelocityY() - (initialVelocityY + 0.3f)) < 0.0001) {
             System.out.println("testUpdate passed");
       } else {
            System.out.println("testUpdate failed");
        }
        
    }

    static void testSlice() {
        BufferedImage img = createTestImage(50, 50);
        BufferedImage slicedImg = createTestImage(50, 50);
        Fruit fruit = new Fruit(img, slicedImg, 100, 200, 5.0f, -3.0f);

        // Initially, the fruit should not be sliced.
        if (!fruit.getIsSliced()) {
            fruit.slice();
            if (fruit.getIsSliced()) {
                System.out.println("testSlice passed");
            } else {
                System.out.println("testSlice failed - isSliced not set to true after slicing");
            }
        } else {
            System.out.println("testSlice failed - fruit already sliced by default");
        }
    }

    static void testIsOffScreen() {
        BufferedImage img = createTestImage(50, 50);
        BufferedImage slicedImg = createTestImage(50, 50);
        // Create a fruit at (100, 200) with width=50 and height=50.
        Fruit fruit = new Fruit(img, slicedImg, 100, 200, 0f, 0f);

        // For a panel of size 400x400, the fruit should be on-screen.
        if (!fruit.isOffScreen(400, 400)) {
            // Now force the fruit to be off-screen on the left by setting x to -60.
            fruit.setX(-60);
            // With x = -60 and width = 50, (x + width) = -10 which is less than 0.
            if (fruit.isOffScreen(400, 400)) {
            	fruit.setX(100);
            	fruit.setY(500);
            	if (fruit.isOffScreen(400, 400)) {
            		System.out.println("testIsOffScreen passed");
            	}else {
            		System.out.println("testIsOffScreen failed - fruit should be off-screen at the top");
            	}
            } else {
                System.out.println("testIsOffScreen failed - fruit should be off-screen on the left");
            }
        } else {
            System.out.println("testIsOffScreen failed - fruit should be on screen initially");
        }
    }

    static void testContains() {
        BufferedImage img = createTestImage(50, 50);
        BufferedImage slicedImg = createTestImage(50, 50);
        // Create a fruit positioned at (100, 200) with width=50 and height=50.
        Fruit fruit = new Fruit(img, slicedImg, 100, 200, 0f, 0f);

        // The fruit covers a rectangle from (100,200) to (150,250).
        // (120,220) should be inside, while (90,190) should be outside.
        if (fruit.contains(120, 220) && !fruit.contains(90, 190)) {
            System.out.println("testContains passed");
        } else {
            System.out.println("testContains failed");
        }
    }

    static void testDraw() {
        // Ensure that the draw method runs without throwing an exception.
        BufferedImage img = createTestImage(50, 50);
        BufferedImage slicedImg = createTestImage(50, 50);
        Fruit fruit = new Fruit(img, slicedImg, 100, 200, 0f, 0f);

        // Create a dummy canvas to get a Graphics2D context.
        BufferedImage canvas = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = canvas.createGraphics();

        try {
            // Draw the unsliced fruit.
            fruit.draw(g2d);
            // Slice the fruit and draw again.
            fruit.slice();
            fruit.draw(g2d);
            System.out.println("testDraw passed");
        } catch (Exception e) {
            System.out.println("testDraw failed due to exception: " + e.getMessage());
        } finally {
            g2d.dispose();
        }
    }
}
