import java.awt.Image;

import javax.swing.ImageIcon;

public class ChessPiece {
	private ImageIcon image;
	private String[] chessPcs = {"res/whitePawn.png", "res/whiteRook.png",
									"res/whiteKnight.png", "res/whiteBishop.png", 
									"res/whiteQueen.png", "res/whiteKing.png", 
									"res/blackPawn.png", "res/blackRook.png",
									"res/blackKnight.png", "res/blackBishop.png",
									"res/blackQueen.png", "res/blackKing.png"};
	private boolean white;
	private int currentPositionX;
	private int currentPositionY;
	private String type;
	private boolean firstMove = true;
	private int moves;
	
	public ChessPiece(boolean white, int posX, int posY) {
		this.white = white;
		this.currentPositionX = posX;
		this.currentPositionY = posY;
		
	}
	
	public void setImage(int index) {
		this.image = new ImageIcon(chessPcs[index]);
		switch (index) {
			case 0:
			case 6:
				this.type = "pawn";
				break;
			case 1:
			case 7:
				this.type = "rook";
				break;
			case 2:
			case 8:
				this.type = "knight";
				break;
			case 3:
			case 9:
				this.type = "bishop";
				break;
			case 4:
			case 10:
				this.type = "queen";
				break;
			case 5:
			case 11:
				this.type = "king";
				break;
		}
	}
	
	public String getType() {
		return this.type;
	}
	
	public boolean isWhite() {
		return this.white;
	}
	
	public boolean isFirstMove() {
		return this.firstMove;
	}
	
	public void hasMoved() {
		this.firstMove = false;
		this.moves++;
	}
	
	public int getMoves() {
		return this.moves;
	}
	
	public Image getImage() {
		return this.image.getImage();
	}
	
	public int getCurrentX() {
		return this.currentPositionX;
	}
	
	public int getCurrentY() {
		return this.currentPositionY;
	}	
	
	public void changePos(int x, int y) {
		this.currentPositionX = x;
		this.currentPositionY = y;
		
		
	}
}
