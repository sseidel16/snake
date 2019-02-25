import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Vector;

class Snake {

    static int headRadius = 7;
    static int tailRadius = 4;
	static int initialPoints = 50;
	static int chunkLength = 3;
	
    Vector<Chunk> chunks = new Vector<Chunk>(50, 50);
    double delta;		//change per frame
	
	Point2D.Double mousePoint;
	int foodIndexBeingEaten;	//food being eaten
	
	Digestion digestion;
	Board board;
	boolean eating = false;

    public Snake(int x, int y, Board board) {
		double direction = Math.PI / 2;
		delta = 7;
		mousePoint = new Point2D.Double(0, 0);
		
		chunks.add(new Chunk(x, y, direction, headRadius));
		for (int i = 0; i < initialPoints; ++i) {
			addChunk();
		}
		
		this.board = board;
		
		digestion = new Digestion(this);
    }
	
	public void move() {
		//check for nearby food first
		if (!eating) lookForFood();
		
		//set the attractive point
		Point2D.Double attractivePoint = null;
		if (eating) attractivePoint = board.foodManager.foods.get(foodIndexBeingEaten).position;
		else attractivePoint = mousePoint;
		
		digestion.update();
		
		double nextDelta = 0;
		double chunkDirection  = getAngle(
			chunks.get(0).center.x,
			chunks.get(0).center.y,
			attractivePoint.x,
			attractivePoint.y
		);
		double chunkDistance = chunks.get(0).center.distance(attractivePoint);
		
		if (chunkDistance > delta) {
			chunks.get(0).moveTo(
				(float)(chunks.get(0).center.x + delta * Math.cos(chunkDirection)),
				(float)(chunks.get(0).center.y + delta * Math.sin(chunkDirection)),
				chunkDirection
			);
		} else {
                    if (eating) {
				board.foodManager.foods.removeElementAt(foodIndexBeingEaten);
				eating = false;
                    } else {
                        board.stopGame();
                    }
		}//possible game over
		
		//move the body
		for (int i = 1; i < chunks.size(); ++i) {
			chunkDirection = getAngle(
				chunks.get(i).center.x,
				chunks.get(i).center.y,
				chunks.get(i - 1).center.x,
				chunks.get(i - 1).center.y
			);
			chunkDistance = chunks.get(i).center.distance(chunks.get(i - 1).center);
			nextDelta = Math.max(chunkDistance - chunkLength, 0);
			chunks.get(i).moveTo(
				(float)(chunks.get(i).center.x + nextDelta * Math.cos(chunkDirection)),
				(float)(chunks.get(i).center.y + nextDelta * Math.sin(chunkDirection)),
				chunkDirection
			);
		}
		
		//check for waste contact
		if (board.wasteManager.touchesWaste(this)) board.stopGame();
	}
	
	public void lookForFood() {
		for (int i = 0; i < board.foodManager.foods.size(); ++i) {
			if (board.foodManager.foods.get(i).eatable(this)) {
				digestion.addFood(board.foodManager.foods.get(i));
				foodIndexBeingEaten = i;
				eating = true;
			}
		}
	}
	
	public void drawSnake(Graphics2D g) {
		
		//draw head
		g.setColor(Color.gray);
		g.fillOval(
			(int)(chunks.get(0).center.x - chunks.get(0).radius),
			(int)(chunks.get(0).center.y - chunks.get(0).radius),
			(int)(2 * chunks.get(0).radius), (int)(2 * chunks.get(0).radius)
		);
		
		//draw neck
		g.setColor(Color.gray);
		g.drawLine(
			(int)chunks.get(1).getLeftX(), (int)chunks.get(1).getLeftY(),
			(int)chunks.get(0).center.x, (int)chunks.get(0).center.y
		);
		g.drawLine(
			(int)chunks.get(1).getRightX(), (int)chunks.get(1).getRightY(),
			(int)chunks.get(0).center.x, (int)chunks.get(0).center.y
		);
		
		//draw body
		g.setColor(Color.black);
		for (int i = 2; i < chunks.size(); ++i) {
			g.fillOval(
				(int)(chunks.get(i).center.x - chunks.get(i).radius),
				(int)(chunks.get(i).center.y - chunks.get(i).radius),
				(int)(2 * chunks.get(i).radius), (int)(2 * chunks.get(i).radius)
			);
			/*g.drawLine(
				(int)chunks.get(i - 1).getLeftX(), (int)chunks.get(i - 1).getLeftY(),
				(int)chunks.get(i).getLeftX(), (int)chunks.get(i).getLeftY()
			);
			g.drawLine(
				(int)chunks.get(i - 1).getRightX(), (int)chunks.get(i - 1).getRightY(),
				(int)chunks.get(i).getRightX(), (int)chunks.get(i).getRightY()
			);*/
		}
		
		//draw mouse point
		g.drawOval((int)mousePoint.x - 3, (int)mousePoint.y - 3, 6, 6);
		
		//draw digestion
		digestion.drawDigestion(g);
	}
	
	public static double fixAngle(double angle) {
		if (angle < 0)
			return fixAngle(angle + (2 * Math.PI));
		else if (angle >= (2 * Math.PI))
			return fixAngle(angle - (2 * Math.PI));
		else
			return angle;
	}
	
	public static double getAngle(double x1, double y1, double x2, double y2) {
		double angle;
		if (x1 == x2) {
			angle = Math.PI / 2;
			if (y2 < y1) angle += Math.PI;
		} else {
			angle = Math.atan((y2 - y1) / (x2 - x1));
			if (x2 < x1) angle += Math.PI;
		}
		return fixAngle(angle);
	}
	
	public void addChunk() {
		chunks.add(new Chunk(chunks.get(chunks.size() - 1)));
		Scores.snakeLength++;
	}
	
	public class Chunk {
		Point2D.Double center;
		double direction;
		double radius;
		
		public Chunk(Chunk otherChunk) {
			center = new Point2D.Double(otherChunk.center.x, otherChunk.center.y);
			direction = otherChunk.direction;
			radius = otherChunk.radius;
		}
		
		public Chunk(double x, double y, double direction, double radius) {
			center = new Point2D.Double(x, y);
			this.direction = direction;
			this.radius = radius;
		}
		
		public double getLeftX() {
			return center.x + (float)(radius * Math.cos(fixAngle(direction - (Math.PI / 2))));
		}
		
		public double getLeftY() {
			return center.y + (float)(radius * Math.sin(fixAngle(direction - (Math.PI / 2))));
		}

		public double getRightX() {
			return center.x + (float)(radius * Math.cos(fixAngle(direction + (Math.PI / 2))));
		}
		
		public double getRightY() {
			return center.y + (float)(radius * Math.sin(fixAngle(direction + (Math.PI / 2))));
		}
		
		public void moveTo(double x, double y, double newDirection) {
			direction = newDirection;//getAngle(center.x, center.y, x, y);
			center.x = x;
			center.y = y;
		}
			
	}

}