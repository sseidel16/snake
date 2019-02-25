import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

public class Board extends JPanel {
	
	//represents the snake
    Snake snake;
	
	//Manages the creation of food of random sizes at certain times
	FoodManager foodManager;
	
	//Manages the creation of waste
	WasteManager wasteManager;
	
    Graphics2D panelG;
	Graphics2D imageG;
	Timer timer;
	BufferedImage image;
	
	boolean paused;
	
	boolean gameRunning;
	
    public Board(int w, int h) {
        //set up panel
        setPreferredSize(new Dimension(w, h));
        
    }
    
    public void resetGame() {
        gameRunning = true;
        paused = true;
        
        //set up graphics
        panelG = (Graphics2D)getGraphics();
        image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        imageG = image.createGraphics();
        
        //create food manager
        foodManager = new FoodManager(getWidth(), getHeight());
        
        //create waste manager
        wasteManager = new WasteManager();
        
        //create the snake
        snake = new Snake(250, 250, this);
        
        //create mouse
        Mouse mouse = new Mouse(this);
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        
        //initialize timer
        timer = new Timer(50, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (!paused) {
                    snake.move();
                    foodManager.update(wasteManager);
                }
                paintPanel();
            }
        });
        timer.setDelay(50);
        timer.setRepeats(true);
        timer.start();
    }
	
	public void stopGame() {
		gameRunning = false;
		paused = true;
	}
	
	public void setPaused(boolean paused) {
		this.paused = paused;
	}
	
	private void paintPanel() {
		imageG.setColor(Color.white);
		imageG.fillRect(0, 0, getWidth(), getHeight());
		snake.drawSnake(imageG);
		foodManager.drawFood(imageG);
		wasteManager.drawWaste(imageG);
		Scores.drawScores(imageG);
		panelG.drawImage(image, 0, 0, null);
	}
	
}