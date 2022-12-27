
public class Rook extends ChessPiece{

	public Rook(boolean white, int posX, int posY) {
		super(white, posX, posY);
		if (super.isWhite()) {
			super.setImage(1);
		} else {
			super.setImage(7);
		}
	}
	
}

