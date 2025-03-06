package sliceit;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Controller extends JFrame{
	 
	public Controller() {
		GamePanel panel = new GamePanel();
	}
	
	class GamePanel extends JPanel implements ActionListener, MouseMotionListener {
		 private final Timer timer = null;
		 private int missCount = 0;
	     private boolean gameOver = false;
	     private ArrayList<Fruit> fruits;
		
	     
		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}

		private void spawnFruit() {
		 
	 }
		@Override
        public void mouseMoved(MouseEvent e) {
            // Not used in this game
        }
		
	 
	}
		
	
	 
	 
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}



	
}
