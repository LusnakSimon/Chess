import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseInputs implements MouseListener{
	private Chessboard board;
	private int counter = 0;
	public MouseInputs(Chessboard board) {
		this.board = board;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			if (e.getX() >= 625 && e.getY() < 10) {
				this.board.resetBoard();
				this.board.repaint();
				this.board.setTurn();
				this.board.setSelected();
			}
			if (this.counter == 0) {
				this.board.selectPiece(e.getX() / 80, e.getY() / 80);
				this.counter = 1;
			} 
			else if (this.counter == 1){
				this.board.movePiece(e.getX() / 80, e.getY() / 80);
				if (this.board.getPromotion()) {
					this.counter = 2;
				} else {
					this.counter = 0;
				}
			}
			else if (this.counter == 2) {
				this.board.promoteTo(e.getX(), e.getY());
				this.board.repaint();
				if (!this.board.getPromotion()) {
					this.counter = 0;
				}
			}
			
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {	
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
	
}
