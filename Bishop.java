
public class Bishop extends ChessPiece{

	public Bishop(boolean white, int posX, int posY) {
		super(white, posX, posY);
		if (super.isWhite()) {
			super.setImage(3);
		} else {
			super.setImage(9);
		}
	}

}
