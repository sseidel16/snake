import javax.swing.JFrame;

class Screen extends JFrame {
	
	public Screen(int width, int height) {
        super("Snake");
        setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Board board = new Board(width, height);
		
        this.add(board);
        this.pack();
		
		board.resetGame();
	}
	
}