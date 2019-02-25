import java.awt.event.MouseEvent;
import javax.swing.event.MouseInputAdapter;

class Mouse extends MouseInputAdapter {
	
	Board board;
	
	public Mouse(Board board) {
		this.board = board;
	}
	
	//public void mousePressed(MouseEvent e) {
    //}

    //public void mouseReleased(MouseEvent e) {
    //}

    //public void mouseEntered(MouseEvent e) {
    //}

    //public void mouseExited(MouseEvent e) {
    //}

    public void mouseClicked(MouseEvent e) {
        if (board.gameRunning) {
            board.paused = !board.paused;
        } else {
            board.resetGame();
        }
    }

    //public void mouseDragged(MouseEvent e) {
	//	
    //}

    public void mouseMoved(MouseEvent e) {
        if (board.gameRunning) {
            board.snake.mousePoint.x = e.getX();
            board.snake.mousePoint.y = e.getY();
        }
    }

}