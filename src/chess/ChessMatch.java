package chess;

import boardgame.Board;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {

	private Board board;

	public ChessMatch() {
		board = new Board(8, 8);
		initialSetup();
	}

	public ChessPiece[][] getPieces() {
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[i].length; j++) {
				mat[i][j] = (ChessPiece) this.board.piece(i, j);
			}
		}

		return mat;
	}
	
	protected void placeNewPiece(char column, int row, ChessPiece piece) {
		this.board.placePiece(piece, new ChessPosition(column,row).toPosition());
	}
	
	public void initialSetup() {
		this.placeNewPiece('h',8,new King(this.board,Color.WHITE));
		this.placeNewPiece('b',4,new Rook(this.board,Color.BLACK));
		this.placeNewPiece('g',7,new King(this.board,Color.BLACK));
	}

}
