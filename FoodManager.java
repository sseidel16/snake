import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Vector;

class FoodManager {
	
	Vector<Food> foods = new Vector<Food>(10, 10);
	long lastReleased;		//food is released every 10 seconds
	int width;
	int height;
	
	public FoodManager(int w, int h) {
		width = w;
		height = h;
		
		lastReleased = System.currentTimeMillis() - 5 * 1000;
	}
	
    public void update(WasteManager wasteManager) {
        long currentTime = System.currentTimeMillis();
        if (currentTime >= lastReleased + 5 * 1000) {
            lastReleased = currentTime;
            while (true) {
                double possibleX = 50 + (width - 100) * Math.random();
                double possibleY = 50 + (height - 100) * Math.random();
                double possibleR = 8 + 4 * Math.random();
                boolean overlapping = false;
                for (int i = 0; i < foods.size(); ++i) {
                    if (Point2D.distance(possibleX, possibleY,
                            foods.get(i).position.x, foods.get(i).position.y)
                            < possibleR + foods.get(i).radius) {
                        overlapping = true;
                    }
                    if (overlapping) break;
                }
                for (int i = 0; i < wasteManager.wastePieces.size(); ++i) {
                    if (Point2D.distance(possibleX, possibleY,
                            wasteManager.wastePieces.get(i).position.x,
                            wasteManager.wastePieces.get(i).position.y)
                            < 2 * possibleR + wasteManager.wastePieces.get(i).radius) {
                        overlapping = true;
                    }
                    if (overlapping) break;
                }
                if (!overlapping) {
                    foods.add(new Food(possibleX, possibleY, possibleR));
                    break;
                }
            }
        }
    }
	
	public void drawFood(Graphics2D g) {
		g.setColor(Color.green);
		for (int i = 0; i < foods.size(); ++i) {
			g.fillOval(
				(int)(foods.get(i).position.x - foods.get(i).radius),
				(int)(foods.get(i).position.y - foods.get(i).radius),
				(int)(2 * foods.get(i).radius), (int)(2 * foods.get(i).radius)
			);
		}
	}
	
	public static class Food {
		
		Point2D.Double position;
		
		double radius;
		public Food(double x, double y, double r) {
			position = new Point2D.Double(x, y);
			radius = r;
		}
		
		public boolean eatable(Snake snake) {
			return (Point2D.distance(
				snake.chunks.get(0).center.x, snake.chunks.get(0).center.y,
				position.x, position.y) < snake.headRadius + radius);
		}
		
	}
	
}