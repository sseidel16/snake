import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

class Scores {
	
	static int snakeLength = 0;
	
	public static void drawScores(Graphics2D g) {
		g.setColor(Color.black);
		Font font = new Font("Courier", Font.BOLD, 20);
		g.setFont(font);
		g.drawString("sL: " + snakeLength, 10, 25);
	}
	
}