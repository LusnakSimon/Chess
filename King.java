
public class King extends ChessPiece{

	public King(boolean white, int posX, int posY) {
		super(white, posX, posY);
		if (super.isWhite()) {
			super.setImage(5);
		} else {
			super.setImage(11);
		}
	}

}
