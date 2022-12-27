
public class Knight extends ChessPiece{

	public Knight(boolean white, int posX, int posY) {
		super(white, posX, posY);
		if (super.isWhite()) {
			super.setImage(2);
		} else {
			super.setImage(8);
		}
	}

}
