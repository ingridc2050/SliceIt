package sliceit;

public class Player {
	private int score = 0;
	private int misses = 0;
	private String name = "Player 1";
	
	private void missedFruit() {
		misses++;
	}
	
	private void hitFruit() {
		score+=10;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
