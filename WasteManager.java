import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Vector;

class WasteManager {
	
	Vector<Waste> wastePieces = new Vector<Waste>(20, 50);
	
	public void drawWaste(Graphics2D g) {
		g.setColor(new Color(128, 64, 0));
		for (int i = 0; i < wastePieces.size(); ++i) {
			g.fillOval(
				(int)(wastePieces.get(i).position.x - wastePieces.get(i).radius),
				(int)(wastePieces.get(i).position.y - wastePieces.get(i).radius),
				(int)(2 * wastePieces.get(i).radius), (int)(2 * wastePieces.get(i).radius)
			);
		}
	}
	
	public void addWaste(double x, double y) {
		wastePieces.add(new Waste(x, y, 5));
	}
	
	public boolean touchesWaste(Snake snake) {
		boolean snakeTouching = false;
		for (int i = 0; i < wastePieces.size(); ++i) {
			boolean wastePieceTouching = false;
			for (int ii = 0; ii < snake.chunks.size(); ++ii) {
				if (snake.chunks.get(ii).center.distance(wastePieces.get(i).position) <
					snake.chunks.get(ii).radius + wastePieces.get(i).radius) {
					System.out.println("Problem!");
					wastePieceTouching = true;
					break;
				}
			}
			if (wastePieceTouching && !wastePieces.get(i).beingPooped) {
				snakeTouching = true;
			} else if (!wastePieceTouching && wastePieces.get(i).beingPooped) {
				wastePieces.get(i).beingPooped = false;
			}
		}
		return snakeTouching;
	}
	
	static class Waste {
		
		Point2D.Double position;
		double radius;
		boolean beingPooped;
		
		public Waste(double x, double y, double r) {
			position = new Point2D.Double(x, y);
			radius = r;
			beingPooped = true;
		}
	
	}
	
}