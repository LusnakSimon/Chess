import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class Chessboard extends JPanel {
	
	private ChessPiece[][] chessPieces;
	private MouseInputs mouseInputs;
	private boolean selected;
	private int x,y;
	private boolean turnWhite = true;
	private boolean castleL;
	private boolean castleR;
	private int lastMoveX;
	private int lastMoveY;
	private boolean enPassant;
	private boolean promotion;
	private boolean promoting;
	private boolean check;
	private int wKingX = 4;
	private int wKingY = 7;
	private int bKingX = 4;
	private int bKingY = 0;
	private boolean canCastle;
	public Chessboard() {
		
		this.setPanelSize();
		this.mouseInputs = new MouseInputs(this);
		this.addMouseListener(this.mouseInputs);
		this.resetBoard();
	}
	
	
	public void resetBoard() {
		this.chessPieces = new ChessPiece[8][8];
		this.chessPieces[0][0] = new Rook(false, 0, 0);
		this.chessPieces[1][0] = new Knight(false, 1, 0);
		this.chessPieces[2][0] = new Bishop(false, 2, 0);
		this.chessPieces[3][0] = new Queen(false, 3, 0);
		this.chessPieces[4][0] = new King(false, 4, 0);
		this.chessPieces[5][0] = new Bishop(false, 5, 0);
		this.chessPieces[6][0] = new Knight(false, 6, 0);
		this.chessPieces[7][0] = new Rook(false, 7, 0);
		this.chessPieces[0][1] = new Pawn(false, 0, 1);
		this.chessPieces[1][1] = new Pawn(false, 1, 1);
		this.chessPieces[2][1] = new Pawn(false, 2, 1);
		this.chessPieces[3][1] = new Pawn(false, 3, 1);
		this.chessPieces[4][1] = new Pawn(false, 4, 1);
		this.chessPieces[5][1] = new Pawn(false, 5, 1);
		this.chessPieces[6][1] = new Pawn(false, 6, 1);
		this.chessPieces[7][1] = new Pawn(false, 7, 1);
		this.chessPieces[0][6] = new Pawn(true, 0, 6);
		this.chessPieces[1][6] = new Pawn(true, 1, 6);
		this.chessPieces[2][6] = new Pawn(true, 2, 6);
		this.chessPieces[3][6] = new Pawn(true, 3, 6);
		this.chessPieces[4][6] = new Pawn(true, 4, 6);
		this.chessPieces[5][6] = new Pawn(true, 5, 6);
		this.chessPieces[6][6] = new Pawn(true, 6, 6);
		this.chessPieces[7][6] = new Pawn(true, 7, 6);
		this.chessPieces[0][7] = new Rook(true, 0, 7);
		this.chessPieces[1][7] = new Knight(true, 1, 7);
		this.chessPieces[2][7] = new Bishop(true, 2, 7);
		this.chessPieces[3][7] = new Queen(true, 3, 7);
		this.chessPieces[4][7] = new King(true, 4, 7);
		this.chessPieces[5][7] = new Bishop(true, 5, 7);
		this.chessPieces[6][7] = new Knight(true, 6, 7);
		this.chessPieces[7][7] = new Rook(true, 7, 7);
		this.check = false;
	}
	
	public void setPanelSize( ) {
		Dimension size = new Dimension(640, 640);
		this.setPreferredSize(size);
	}
	
	public void movePiece(int x, int y) {
		boolean validForPiece = false;
		if (this.chessPieces[this.x][this.y] != null) {
			validForPiece = this.isMoveValidForPiece(x, y, this.chessPieces[this.x][this.y]);
		}
		boolean inCheck = false;
		if (this.chessPieces[this.x][this.y] != null) {
			if (this.check || this.chessPieces[this.x][this.y].getType().equals("king")) {
				inCheck = this.isLegalInCheck(x, y, this.chessPieces[this.x][this.y]);
			}
		}
		boolean legal = this.isMoveLegal(x, y, this.chessPieces[this.x][this.y]);
		boolean valid = this.isMoveValid(x, y, this.chessPieces[this.x][this.y]);
		if (this.chessPieces[this.x][this.y] != null) {
			if (this.chessPieces[this.x][this.y].isWhite()) {
				if (this.chessPieces[this.x][this.y].getType().equals("pawn") && y == 0) {
					this.promotion = true;
				}
			}
			else {
				if (this.chessPieces[this.x][this.y].getType().equals("pawn")  && y == 7) {
					this.promotion = true;
				}
			}
		}
		if (this.x == 4 && this.y == 7 && this.canCastle(0, 7)) {
			if (this.castleL && x == 1 && y == 7) {
				this.chessPieces[this.x][this.y].changePos(x, y);
				this.chessPieces[x][y] = this.chessPieces[this.x][this.y];
				this.chessPieces[this.x][this.y] = null;
				this.chessPieces[x][y].hasMoved();
				this.chessPieces[0][7].changePos(2, 7);
				this.chessPieces[2][7] = this.chessPieces[0][7];
				this.chessPieces[0][7] = null;
				this.repaint();
				this.lastMoveX = x;
				this.lastMoveY = y;
				this.turnWhite = !this.turnWhite;
				return;
				
			}
			if (this.castleR && x == 6 && y == 7 && this.canCastle(7, 7)) {
				this.chessPieces[this.x][this.y].changePos(x, y);
				this.chessPieces[x][y] = this.chessPieces[this.x][this.y];
				this.chessPieces[this.x][this.y] = null;
				this.chessPieces[x][y].hasMoved();
				this.chessPieces[7][7].changePos(5, 7);
				this.chessPieces[5][7] = this.chessPieces[7][7];
				this.chessPieces[7][7] = null;
				this.repaint();
				this.lastMoveX = x;
				this.lastMoveY = y;
				this.turnWhite = !this.turnWhite;
				return;
			}
		}
		else if (this.x == 4 && this.y == 0 && this.canCastle(0, 0)) {
			if (this.castleL && x == 1 && y == 0) {
				this.chessPieces[this.x][this.y].changePos(x, y);
				this.chessPieces[x][y] = this.chessPieces[this.x][this.y];
				this.chessPieces[this.x][this.y] = null;
				this.chessPieces[x][y].hasMoved();
				this.chessPieces[0][0].changePos(2, 0);
				this.chessPieces[2][0] = this.chessPieces[0][0];
				this.chessPieces[0][0] = null;
				this.repaint();
				this.lastMoveX = x;
				this.lastMoveY = y;
				this.turnWhite = !this.turnWhite;
				return;
			}
			if (this.castleR && x == 6 && y == 0 && this.canCastle(7, 0)) {
				this.chessPieces[this.x][this.y].changePos(x, y);
				this.chessPieces[x][y] = this.chessPieces[this.x][this.y];
				this.chessPieces[this.x][this.y] = null;
				this.chessPieces[x][y].hasMoved();
				this.chessPieces[7][0].changePos(5, 0);
				this.chessPieces[5][0] = this.chessPieces[7][0];
				this.chessPieces[7][0] = null;
				this.repaint();
				this.lastMoveX = x;
				this.lastMoveY = y;
				this.turnWhite = !this.turnWhite;
				return;
			}
		}
		if ((this.chessPieces[this.x][this.y] != null) && validForPiece && valid && legal && !inCheck) {
			this.check = false;
			if (this.enPassant) {
				this.chessPieces[this.lastMoveX][this.lastMoveY] = null;
			}
			if (this.chessPieces[this.x][this.y].getType().equals("king")) {
				if (this.chessPieces[this.x][this.y].isWhite()) {
					this.wKingX = x;
					this.wKingY = y;
				} else {
					this.bKingX = x;
					this.bKingY = y;
				}
			}
			this.chessPieces[this.x][this.y].changePos(x, y);
			this.chessPieces[x][y] = this.chessPieces[this.x][this.y];
			this.chessPieces[this.x][this.y] = null;
			this.chessPieces[x][y].hasMoved();
			this.repaint();
			this.turnWhite = !this.turnWhite;
			this.lastMoveX = x;
			this.lastMoveY = y;
			if (this.promotion) {
				this.setPromoting();
			}
			this.isCheck();			
		}
		
	
	}
	
	public void setTurn() {
		this.turnWhite = true;
	}
	
	public int getPossibleMoves() {
		int moves = 0;
		if (this.turnWhite) {
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					if (this.chessPieces[j][i] != null) {
						if (this.chessPieces[j][i].isWhite()) {
							for (int k = 0; k < 8; k++) {
								for (int m = 0; m < 8; m++) {
									boolean	validForPiece = this.isMoveValidForPiece(m, k, this.chessPieces[j][i]);
									boolean legal = this.isMoveLegal(m, k, this.chessPieces[j][i]);
									boolean valid = this.isMoveValid(m, k, this.chessPieces[j][i]);
									boolean inCheck = false;
									if (this.chessPieces[j][i] != null) {
										if (this.check || this.chessPieces[j][i].getType().equals("king")) {
											inCheck = this.isLegalInCheck(m, k, this.chessPieces[j][i]);
										}
									}
									if (validForPiece && valid && legal && !inCheck) {
										moves++;
									}
								}
							}
						}
					}
				}
			}
			return moves;
		} else {
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					if (this.chessPieces[j][i] != null) {
						if (!this.chessPieces[j][i].isWhite()) {
							for (int k = 0; k < 8; k++) {
								for (int m = 0; m < 8; m++) {
									boolean	validForPiece = this.isMoveValidForPiece(m, k, this.chessPieces[j][i]);
									boolean legal = this.isMoveLegal(m, k, this.chessPieces[j][i]);
									boolean valid = this.isMoveValid(m, k, this.chessPieces[j][i]);
									boolean inCheck = false;
									if (this.chessPieces[j][i] != null) {
										if (this.check || this.chessPieces[j][i].getType().equals("king")) {
											inCheck = this.isLegalInCheck(m, k, this.chessPieces[j][i]);
										}
									}
									if (validForPiece && valid && legal && !inCheck) {
										moves++;
									}
								}
							}
						}
					}
				}
			}
			return moves;
		}
	}
	
	public void isCheck() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				ChessPiece piece = this.chessPieces[j][i];
				if (piece != null) {
					if (piece.isWhite()) {
						if (this.isMoveValid(this.bKingX, this.bKingY, piece)) {
							if (this.isMoveValidForPiece(this.bKingX, this.bKingY, piece)) {
								if (this.isMoveLegal(this.bKingX, this.bKingY, piece)) {
									this.check = true;
									return;
								}
							}
						}
					} else {
						if (this.isMoveValid(this.wKingX, this.wKingY, piece)) {
							if (this.isMoveValidForPiece(this.wKingX, this.wKingY, piece)) {
								if (this.isMoveLegal(this.wKingX, this.wKingY, piece)) {
									this.check = true;
									return;
								}
							}
						}
					}
				}
			}
		}
		this.check = false;
	}
	
	
	public boolean canCastle(int x, int y) {
		for (int i = 0; i < 8 ; i++) {
			for (int j = 0; j < 8; j++) {
				if(this.chessPieces[j][i] != null) {
					ChessPiece piece = this.chessPieces[j][i];
					if (x == 0 && y == 0 && piece.isWhite()) {
						for (int k = 1; k < 5; k++) {
							if (this.isMoveValid(k, 0, piece)) {
								if (this.isMoveValidForPiece(k, 0, piece)) {
									if (this.isMoveLegal(k, 0, piece)) {
										this.canCastle = false;
										return false;
									}
								}
							}
						}
					}
					else if (x == 7 && y == 0 && piece.isWhite()) {
						for (int k = 4; k < 7; k++) {
							if (this.isMoveValid(k, 0, piece)) {
								if (this.isMoveValidForPiece(k, 0, piece)) {
									if (this.isMoveLegal(k, 0, piece)) {
										this.canCastle = false;
										return false;
									}
								}
							}
						}
					}
					else if (x == 0 && y == 7 && !piece.isWhite()) {
						for (int k = 1; k < 5; k++) {
							if (this.isMoveValid(k, 7, piece)) {
								if (this.isMoveValidForPiece(k, 7, piece)) {
									if (this.isMoveLegal(k, 7, piece)) {
										this.canCastle = false;
										return false;
									}
								}
							}
						}
					}
					else if (x == 7 & y == 7 && !piece.isWhite()) {
						for (int k = 4; k < 7; k++) {
							if (this.isMoveValid(k, 7, piece)) {
								if (this.isMoveValidForPiece(k, 7, piece)) {
									if (this.isMoveLegal(k, 7, piece)) {
										this.canCastle = false;
										return false;
									}
								}
							}
						}
					}
				}
			}
		}
		this.canCastle = true;
		return true;
	}
	
	public boolean isLegalInCheck(int x, int y, ChessPiece piece) {
		if (piece != null) {
			ChessPiece temp = this.chessPieces[x][y];
			int currentX = piece.getCurrentX();
			int currentY = piece.getCurrentY();
			if (piece.getType().equals("king")) {
				if (piece.isWhite()) {
					this.wKingX = x;
					this.wKingY = y;
				} else {
					this.bKingX = x;
					this.bKingY = y;
				}
			}
			this.chessPieces[x][y] = piece;
			this.chessPieces[currentX][currentY] = null;
			this.isCheck();
			boolean legal = this.check;
			this.chessPieces[x][y] = temp;
			this.chessPieces[currentX][currentY] = piece;
			if (piece.getType().equals("king")) {
				if (piece.isWhite()) {
					this.wKingX = piece.getCurrentX();
					this.wKingY = piece.getCurrentY();
				} else {
					this.bKingX = piece.getCurrentX();
					this.bKingY = piece.getCurrentY();
				}
			}
			this.check = true;
			return legal;
		}
		return false;
	}
	
	public void promoteTo(int x, int y) {
		if ((y > 240) && (y < 320) && (x > 160) && (x < 480)) {
			switch (x / 80) {
				case 2 :
					if (this.chessPieces[this.lastMoveX][this.lastMoveY].isWhite()) {
						this.chessPieces[this.lastMoveX][this.lastMoveY] = new Queen(true, this.lastMoveX, this.lastMoveY);
					} else {
						this.chessPieces[this.lastMoveX][this.lastMoveY] = new Queen(false, this.lastMoveX, this.lastMoveY);
					}
					this.setPromoting();
					this.setPromotion();
					break;
				case 3 :
					if (this.chessPieces[this.lastMoveX][this.lastMoveY].isWhite()) {
						this.chessPieces[this.lastMoveX][this.lastMoveY] = new Knight(true, this.lastMoveX, this.lastMoveY);
					} else {
						this.chessPieces[this.lastMoveX][this.lastMoveY] = new Knight(false, this.lastMoveX, this.lastMoveY);
					}
					this.setPromoting();
					this.setPromotion();
					break;
				case 4 :
					if (this.chessPieces[this.lastMoveX][this.lastMoveY].isWhite()) {
						this.chessPieces[this.lastMoveX][this.lastMoveY] = new Rook(true, this.lastMoveX, this.lastMoveY);
					} else {
						this.chessPieces[this.lastMoveX][this.lastMoveY] = new Rook(false, this.lastMoveX, this.lastMoveY);
					}
					this.setPromoting();
					this.setPromotion();
					break;
				case 5 :
					if (this.chessPieces[this.lastMoveX][this.lastMoveY].isWhite()) {
						this.chessPieces[this.lastMoveX][this.lastMoveY] = new Bishop(true, this.lastMoveX, this.lastMoveY);
					} else {
						this.chessPieces[this.lastMoveX][this.lastMoveY] = new Bishop(false, this.lastMoveX, this.lastMoveY);
					}
					this.setPromoting();
					this.setPromotion();
					break;
			}
		}
	}
	
	public boolean getPromotion() {
		return this.promotion;
	}
	
	public void setPromotion() {
		this.promotion = !this.promotion;
	}
	
	public void setPromoting() {
		this.promoting = !this.promoting;
	}
	
	public boolean isMoveLegal(int x, int y, ChessPiece piece) {
		if (piece != null && this.isMoveValidForPiece(x, y, piece)) {
			if (piece.getType().equals("king") || piece.getType().equals("knight")) {
				return true;
			}
			switch (this.getDirection(x, y, piece)) {
				case "U":
					int piecesInTheWayUp = 0;
					for (int i = y; i < piece.getCurrentY(); i++) {
						if (this.chessPieces[x][i] != null) {
							piecesInTheWayUp++;
						}
					}
					return (piecesInTheWayUp <= 1 && this.chessPieces[x][y] != null) || piecesInTheWayUp == 0;
				case "D":
					int piecesInTheWayDown = 0;
					for (int i = piece.getCurrentY() + 1; i < y + 1; i++) {
						if (this.chessPieces[x][i] != null) {
							piecesInTheWayDown++;
						}
					}
					return (piecesInTheWayDown <= 1 && this.chessPieces[x][y] != null) || piecesInTheWayDown == 0;
				case "L":
					int piecesInTheWayLeft = 0;
					for (int i = x; i < piece.getCurrentX(); i++) {
						if (this.chessPieces[i][y] != null) {
							piecesInTheWayLeft++;
						}
					}
					return (this.chessPieces[x][y] != null && piecesInTheWayLeft <= 1) || piecesInTheWayLeft == 0;
				case "R":
					int piecesInTheWayRight = 0;
					for (int i = piece.getCurrentX() + 1; i < x + 1; i++) {
						if (this.chessPieces[i][y] != null) {
							piecesInTheWayRight++;
						}
					}
					return (this.chessPieces[x][y] != null && piecesInTheWayRight <= 1) || piecesInTheWayRight == 0;
				case "TL":
					int piecesInTheWayTopLeft = 0;
					for (int i = 0; (i + x) < piece.getCurrentX(); i++) {
						if (this.chessPieces[i + x][i + y] != null) {
							piecesInTheWayTopLeft++;
						}
					}
					return (this.chessPieces[x][y] != null && piecesInTheWayTopLeft <= 1) || piecesInTheWayTopLeft == 0;
				case "TR":
					int piecesInTheWayTopRight = 0;
					for (int i = 0; (i + y) < piece.getCurrentY(); i++) {
						if (this.chessPieces[x -i][i + y] != null) {
							piecesInTheWayTopRight++;
						}
					}
					return (this.chessPieces[x][y] != null && piecesInTheWayTopRight <= 1) || piecesInTheWayTopRight == 0;
				case "BL":
					int piecesInTheWayBottomLeft = 0;
					for (int i = 0; (i + x) < piece.getCurrentX(); i++) {
						if (this.chessPieces[x + i][y -i] != null) {
							piecesInTheWayBottomLeft++;
						}
					}
					return (this.chessPieces[x][y] != null && piecesInTheWayBottomLeft <= 1) || piecesInTheWayBottomLeft == 0;
				case "BR":
					int piecesInTheWayBottomRight = 0;
					for (int i = 1; (i + piece.getCurrentX()) < x + 1 ; i++) {
						if (this.chessPieces[piece.getCurrentX() + i][piece.getCurrentY() + i] != null) {
							piecesInTheWayBottomRight++;
						}
					}
					return (this.chessPieces[x][y] != null && piecesInTheWayBottomRight <= 1) || piecesInTheWayBottomRight == 0;
			}
		}
		
		return false;
	}
	
	
	public String getDirection(int x, int y, ChessPiece piece) {
		if (x == piece.getCurrentX()) {
			if (y < piece.getCurrentY()) {
				return "U";
			} else {
				return "D";
			}
		}
		else if (y == piece.getCurrentY()) {
			if (x < piece.getCurrentX()) {
				return "L";
			} else {
				return "R";
			}
		}
		else if (y < piece.getCurrentY()) {
			if (x < piece.getCurrentX()) {
				return "TL";
			} else {
				return "TR";
			}
		}
		else if (y > piece.getCurrentY()) {
			if (x < piece.getCurrentX()) {
				return "BL";
			} else {
				return "BR";
			}
		}
		return "";
	}
	
	public boolean isMoveValid(int x, int y, ChessPiece piece) {
		if (this.chessPieces[x][y] != null && piece != null) {
			if (piece.isWhite()) {
				if (this.chessPieces[x][y].isWhite()) {
					return false;
				} else {
					return true;
				}
			} else {
				if (!this.chessPieces[x][y].isWhite()) {
					return false;
				} else {
					return true;
				}
			}
		}
		return true;	
		}


	public boolean isMoveValidForPiece(int x, int y, ChessPiece piece) {
		boolean moveRook = x == piece.getCurrentX() || y == piece.getCurrentY();
		boolean moveBishop = Math.abs(x - piece.getCurrentX()) == Math.abs(y - piece.getCurrentY());
		boolean movePawn;
		if (piece.isWhite()) {
			movePawn = x == piece.getCurrentX() && (piece.getCurrentY() - y) == 1 && (this.chessPieces[x][y] == null);
		} else {
			movePawn = x == piece.getCurrentX() && (y - piece.getCurrentY()) == 1 && (this.chessPieces[x][y] == null);
		}
		boolean pawnCapture = false;
		if ((piece.getCurrentY() != 0) && (piece.getCurrentY() != 7) && (this.chessPieces[x][y] != null)) {
			if (piece.isWhite()) {
				if (piece.getCurrentX() - x == 1 && piece.getCurrentY() - y == 1) {
					pawnCapture = true;
				}
				else if (piece.getCurrentX() - x == -1 && piece.getCurrentY() - y == 1) {
					pawnCapture = true;
				} else {
					pawnCapture = false;
				}
			} else {
				if (piece.getCurrentX() - x == 1 && piece.getCurrentY() - y == -1) {
					pawnCapture = true;
				}
				else if (piece.getCurrentX() - x == -1 && piece.getCurrentY() - y == -1) {
					pawnCapture = true;
				} else {
					pawnCapture = false;
				}
			}
			
		}
		boolean enPassant = false;
		if (piece.isWhite() && piece.getCurrentY() == 3 && piece.getType().equals("pawn")) {
			if (this.chessPieces[this.lastMoveX][this.lastMoveY].getMoves() == 1) {
				if (this.chessPieces[this.lastMoveX][this.lastMoveY].getCurrentY() == 3) {
					if (this.chessPieces[this.lastMoveX][this.lastMoveY].getCurrentX() - piece.getCurrentX() == -1) {
						if (this.chessPieces[this.lastMoveX][this.lastMoveY].getType().equals("pawn")) {
							enPassant = piece.getCurrentX() - x == 1  && piece.getCurrentY() - y == 1;
						}
					}
					else if (this.chessPieces[this.lastMoveX][this.lastMoveY].getCurrentX() - piece.getCurrentX() == 1) {
						if (this.chessPieces[this.lastMoveX][this.lastMoveY].getType().equals("pawn")) {
							enPassant = piece.getCurrentX() - x == -1  && piece.getCurrentY() - y == 1;
						}
						
					}
				}
			}
		}
		else if (!piece.isWhite() && piece.getCurrentY() == 4 && piece.getType().equals("pawn")) {
			if (this.chessPieces[this.lastMoveX][this.lastMoveY].getMoves() == 1) {
				if (this.chessPieces[this.lastMoveX][this.lastMoveY].getCurrentY() == 4) {
					if (this.chessPieces[this.lastMoveX][this.lastMoveY].getCurrentX() - piece.getCurrentX() == -1) {
						if (this.chessPieces[this.lastMoveX][this.lastMoveY].getType().equals("pawn")) {
							enPassant = piece.getCurrentX() - x == 1  && piece.getCurrentY() - y == -1;
						}
						
					}
					else if (this.chessPieces[this.lastMoveX][this.lastMoveY].getCurrentX() - piece.getCurrentX() == 1) {
						if (this.chessPieces[this.lastMoveX][this.lastMoveY].getType().equals("pawn")) {
							enPassant = piece.getCurrentX() - x == -1  && piece.getCurrentY() - y == -1;
						}
					}
				}
			}
		}
		this.enPassant = enPassant;
		boolean moveKnightH = Math.abs(x - piece.getCurrentX()) == 1 && Math.abs(y - piece.getCurrentY() ) == 2;
		boolean moveKnightV = Math.abs(x - piece.getCurrentX()) == 2 && Math.abs(y - piece.getCurrentY() ) == 1;
		boolean moveKnight = moveKnightH || moveKnightV;
		boolean moveKing = Math.abs(x - piece.getCurrentX()) <= 1 && Math.abs(y - piece.getCurrentY()) <= 1;
		boolean castleLeft = false;
		boolean castleRight = false;
		if (piece.isWhite()) {
			if (piece.getType().equals("king") && piece.isFirstMove() && this.canCastle) {
				if (this.chessPieces[0][7] != null) {
					if (this.chessPieces[1][7] == null && this.chessPieces[2][7] == null && this.chessPieces[3][7] == null && this.chessPieces[0][7].isFirstMove()) {
						castleLeft = y == piece.getCurrentY() && piece.getCurrentX() - x == 3;
					}
				}
				if (this.chessPieces[7][7] != null) {
					if (this.chessPieces[5][7] == null && this.chessPieces[6][7] == null && this.chessPieces[7][7].isFirstMove()) {
						castleRight = y == piece.getCurrentY() && x - piece.getCurrentX() == 2;
					}
				}
				
			}
		} else {
			if (piece.getType().equals("king") && piece.isFirstMove() && this.canCastle) {
				if (this.chessPieces[0][0] != null) {
					if (this.chessPieces[1][0] == null && this.chessPieces[2][0] == null && this.chessPieces[3][0] == null && this.chessPieces[0][0].isFirstMove()) {
						castleLeft = y == piece.getCurrentY() && piece.getCurrentX() - x == 3;
					}
				}
				if (this.chessPieces[7][0] != null) {
					if (this.chessPieces[5][0] == null && this.chessPieces[6][0] == null && this.chessPieces[7][0].isFirstMove()) {
						castleRight = y == piece.getCurrentY() && x - piece.getCurrentX() == 2;
					}
				}
				
			}
		}
		boolean moveQueen = moveRook || moveBishop;
		boolean pawnFirstMove = movePawn || (x == piece.getCurrentX() && Math.abs(y - piece.getCurrentY()) == 2 && (this.chessPieces[x][y] == null));
		this.castleL = castleLeft;
		this.castleR = castleRight;
		switch (piece.getType()) {
			case "rook" :
				return moveRook;
			case "bishop" :
				return moveBishop;
			case "pawn" :
				if (piece.isFirstMove()) {
					return pawnFirstMove || pawnCapture;
				} else {
					return movePawn || pawnCapture || enPassant;
				}
			case "knight" :
				return moveKnight;
			case "king" :
				return moveKing || castleLeft || castleRight;
			case "queen" :
				return moveQueen;
			default :
				return false;
		}	
		
	}
	
	public void setSelected() {
		this.selected = !this.selected;
	}
	
	public void selectPiece(int x, int y) {
		if (this.chessPieces[x][y] != null) {
			if (this.chessPieces[x][y].isWhite() && this.turnWhite) {
				this.x = x;
				this.y = y;
				this.selected = true;
				this.repaint();		
			} 
			else if (!this.chessPieces[x][y].isWhite() && !this.turnWhite) {
				this.x = x;
				this.y = y;
				this.selected = true;
				this.repaint();
				
			}
			
		}
		
	}
	
	public boolean isSelected() {
		return this.selected;
	}
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		int counter = 0;
		for (int i = 0; i < 8; i++) {
			counter++;
			for (int j =0; j < 8; j++) {
				
				if (counter % 2 == 0) {
					g.setColor(new Color(30, 75, 140));
				} else {
					g.setColor(new Color(245, 245, 200));
				}
				g.fillRect(j*80, 80*i, 80, 80);
				counter++;
				if (this.isSelected()) {
					if (this.chessPieces[this.x][this.y] != null) {
						g.setColor(new Color(240, 210, 105));
						g.fillRect(this.chessPieces[this.x][this.y].getCurrentX()*80, this.chessPieces[this.x][this.y].getCurrentY()*80, 80, 80);
					}
					boolean validForPiece = false;
					if (this.chessPieces[this.x][this.y] != null) {
						validForPiece = this.isMoveValidForPiece(j, i, this.chessPieces[this.x][this.y]);
					}
					boolean legal = this.isMoveLegal(j, i, this.chessPieces[this.x][this.y]);
					boolean valid = this.isMoveValid(j, i, this.chessPieces[this.x][this.y]);
					boolean inCheck = false;
					if (this.chessPieces[this.x][this.y] != null) {
						if (this.check || this.chessPieces[this.x][this.y].getType().equals("king")) {
							inCheck = this.isLegalInCheck(j, i, this.chessPieces[this.x][this.y]);
						}
					}
					if (validForPiece && valid && legal && !inCheck) {
						g.setColor(new Color(100, 100, 200));
						if (this.chessPieces[j][i] == null) {
							g.fillOval(j*80 + 30, i*80 + 30, 20, 20);
						} else {
							g.drawRect(j*80 + 10, i*80 + 10, 60, 60);
						}
						
					}
					
				}
				
				
			}
		}
		
		for (int k = 0; k < 8; k++) {
			for (int m = 0; m < 8; m++) {
				if (this.chessPieces[k][m] != null) {
					g.drawImage(this.chessPieces[k][m].getImage(),
								this.chessPieces[k][m].getCurrentX()*80, 
								this.chessPieces[k][m].getCurrentY()*80,
								80, 80, null);
				}
				
			}
		}
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8 ; j++) {
				if (i == 0) {
					if (j % 2 == 0) {
						g.setColor(new Color(30, 75, 140));
					} else {
						g.setColor(new Color(245, 245, 200));
					}
					g.setFont(new Font("serif", Font.BOLD, 13));
					g.drawString(Integer.toString(Math.abs(8 - j)), i*80 + 5, j*80 + 15);
				}
				if (j == 7) {
					if (i % 2 == 1) {
						g.setColor(new Color(30, 75, 140));
					} else {
						g.setColor(new Color(245, 245, 200));
					}
					String[] chars = {"a", "b", "c", "d", "e", "f", "g", "h"};
					g.setFont(new Font("serif", Font.BOLD, 14));
					g.drawString(chars[i], i*80 + 68, j*80 + 75);
				}
			}
		}
		g.drawImage(new ImageIcon("res/reset.png").getImage(), 625, 0, 15, 15, null);
		
		if (this.promoting) {
			g.setColor(new Color(100, 100, 150));
			g.fillRect(160, 240, 320, 80);
			if (this.chessPieces[this.lastMoveX][this.lastMoveY].isWhite()) {
				g.drawImage(new ImageIcon("res/whiteQueen.png").getImage(), 160, 240, 80, 80, null);
				g.drawImage(new ImageIcon("res/whiteKnight.png").getImage(), 240, 240, 80, 80, null);
				g.drawImage(new ImageIcon("res/whiteRook.png").getImage(), 320, 240, 80, 80, null);
				g.drawImage(new ImageIcon("res/whiteBishop.png").getImage(), 400, 240, 80, 80, null);
			} else {
				g.drawImage(new ImageIcon("res/blackQueen.png").getImage(), 160, 240, 80, 80, null);
				g.drawImage(new ImageIcon("res/blackKnight.png").getImage(), 240, 240, 80, 80, null);
				g.drawImage(new ImageIcon("res/blackRook.png").getImage(), 320, 240, 80, 80, null);
				g.drawImage(new ImageIcon("res/blackBishop.png").getImage(), 400, 240, 80, 80, null);
			}
			
		}
		
		if (this.getPossibleMoves() == 0) {
			if (this.check) {
				if (this.turnWhite) {
					g.setColor(new Color(100, 100, 100));
					g.fillRect(120, 280, 460, 80);
					g.setColor(new Color(0, 0, 0));
					g.setFont(new Font("serif", Font.BOLD, 40));
					g.drawString("Black wins by checkmate!", 120, 320);
				} else {
					g.setColor(new Color(100, 100, 100));
					g.fillRect(120, 280, 460, 80);
					g.setColor(new Color(0, 0, 0));
					g.setFont(new Font("serif", Font.BOLD, 40));
					g.drawString("White wins by checkmate!", 120, 320);
				}
			} else {
				g.setColor(new Color(100, 100, 100));
				g.fillRect(120, 280, 460, 80);
				g.setColor(new Color(0, 0, 0));
				g.setFont(new Font("serif", Font.BOLD, 40));
				g.drawString("Stalemate!", 120, 320);
			}
		}
	}
}
