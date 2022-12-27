
public class Pawn extends ChessPiece{

	public Pawn(boolean white, int posX, int posY) {
		super(white, posX, posY);
		if (super.isWhite()) {
			super.setImage(0);
		} else {
			super.setImage(6);
		}
	}

}
