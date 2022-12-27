
public class Chess {
	private Chessboard board;
	
	public Chess() {
		this.board = new Chessboard();
		new GameWindow(this.board);
		this.board.requestFocus();
	}
}
