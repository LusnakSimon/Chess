
public class Queen extends ChessPiece{

	public Queen(boolean white, int posX, int posY) {
		super(white, posX, posY);
		if (super.isWhite()) {
			super.setImage(4);
		} else {
			super.setImage(10);
		}
	}

}
