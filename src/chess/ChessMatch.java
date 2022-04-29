package chess;

import boardgame.Board;
import boardgame.Position;
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
	
	public void initialSetup() {
		board.placePiece(new King(this.board,Color.WHITE), new Position(2,5));
		board.placePiece(new Rook(this.board,Color.BLACK), new Position(6,5));
		board.placePiece(new King(this.board,Color.BLACK), new Position(6,6));
	}

}
