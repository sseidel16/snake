import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Vector;

class Digestion {
	
	static double minimumPieceRadius = 5.0;
	Snake snake;
	//double deltaRadius;
	//double foodRadius;
	Vector<Piece> pieces = new Vector<Piece>(10, 10);
	
	public Digestion (Snake snake) {
		this.snake = snake;
		
	}
	
	public void addFood(FoodManager.Food food) {
		double distanceToFood = food.position.distance(snake.chunks.get(0).center);
		double framesToFood = distanceToFood / snake.delta;
		
		pieces.add(new Piece((int)(-framesToFood), food.radius));
	}
	
	public void update() {
		for (int i = 0; i < snake.chunks.size(); ++i) {
			snake.chunks.get(i).radius = Snake.tailRadius +
				(Snake.headRadius - Snake.tailRadius) * ((double)(snake.chunks.size() - i) / snake.chunks.size());
		}
		
		for (int i = 0; i < pieces.size(); ++i) {
			pieces.get(i).chunkIndex++;
			if (pieces.get(i).chunkIndex >= snake.chunks.size()) {
				snake.board.wasteManager.addWaste(
					snake.chunks.get(snake.chunks.size() - 1).center.x,
					snake.chunks.get(snake.chunks.size() - 1).center.y
				);
				pieces.removeElementAt(i);
				--i;
				//add 5 more chunks
				snake.addChunk();
				snake.addChunk();
				snake.addChunk();
			} else {
				if (pieces.get(i).radius > minimumPieceRadius) pieces.get(i).radius *= 0.98;
				
				//upgrade the chunks as necessary
				for (int ii = 0; ii < snake.chunks.size(); ++ii) {
					upgradeChunkRadius(ii, (1.5 * pieces.get(i).radius) - Math.abs(ii - pieces.get(i).chunkIndex));
				}
			}
		}
		
	}
	
	public void drawDigestion(Graphics2D g) {
		for (int i = 0; i < pieces.size(); ++i) {
			if (pieces.get(i).chunkIndex >= 0 && pieces.get(i).chunkIndex < snake.chunks.size()) {
				
				double percentage = (double)pieces.get(i).chunkIndex / snake.chunks.size();
				g.setColor(new Color(
								(int)(128 * percentage),		//red	0 - 128
								(int)(255 - 191 * percentage),	//green	255 - 64
								0	//blue	doesn't change
						   ));
				g.fillOval(
					(int)(snake.chunks.get(pieces.get(i).chunkIndex).center.x -
						pieces.get(i).radius),
					(int)(snake.chunks.get(pieces.get(i).chunkIndex).center.y -
						pieces.get(i).radius),
					(int)(2 * pieces.get(i).radius),
					(int)(2 * pieces.get(i).radius)
				);
			}
		}
	}
	
	public void upgradeChunkRadius(int index, double radius) {
		if (index < 0 || index >= snake.chunks.size()) return;
		
		//max function ensures an upgrade
		snake.chunks.get(index).radius = Math.max(snake.chunks.get(index).radius, radius);
	}
	
	public static class Piece {
		double radius;
		
		int chunkIndex;//can be negative if jaw is opening
		
		public Piece(int chunkIndex, double radius) {
			this.chunkIndex = chunkIndex;;
			this.radius = radius;
		}
		
	}

}