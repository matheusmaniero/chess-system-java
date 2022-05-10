package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {

	private ChessMatch chessMatch;

	public King(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch = chessMatch;
	}

	public String toString() {
		return "K";
	}

	private boolean canMove(Position position) {
		ChessPiece p = (ChessPiece) this.getBoard().piece(position);
		return (p == null || p.getColor() != this.getColor());
	}

	private boolean testRookCastling(Position position) {
		ChessPiece p = (ChessPiece) this.getBoard().piece(position);
		return (p != null && p instanceof Rook && p.getColor() == this.getColor() && p.getMoveCount() == 0);
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[this.getBoard().getRows()][this.getBoard().getColumns()];

		Position p = new Position(0, 0);

		// above

		p.setValues(this.position.getRow() - 1, this.position.getColumn());
		if (this.getBoard().positionExists(p) && this.canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// below

		p.setValues(this.position.getRow() + 1, this.position.getColumn());
		if (this.getBoard().positionExists(p) && this.canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// left

		p.setValues(this.position.getRow(), this.position.getColumn() - 1);
		if (this.getBoard().positionExists(p) && this.canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// right

		p.setValues(this.position.getRow(), this.position.getColumn() + 1);
		if (this.getBoard().positionExists(p) && this.canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// nw

		p.setValues(this.position.getRow() - 1, this.position.getColumn() - 1);
		if (this.getBoard().positionExists(p) && this.canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// ne

		p.setValues(this.position.getRow() - 1, this.position.getColumn() + 1);
		if (this.getBoard().positionExists(p) && this.canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// sw

		p.setValues(this.position.getRow() + 1, this.position.getColumn() - 1);
		if (this.getBoard().positionExists(p) && this.canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// se

		p.setValues(this.position.getRow() + 1, this.position.getColumn() + 1);
		if (this.getBoard().positionExists(p) && this.canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		// #special move castling

		if (this.getMoveCount() == 0 && !chessMatch.getCheck()) {
			// #special move kingside rook
			Position posT1 = new Position(this.position.getRow(), this.position.getColumn() + 3);
			if (this.testRookCastling(posT1)) {
				Position p1 = new Position(this.position.getRow(), this.position.getColumn() + 1);
				Position p2 = new Position(this.position.getRow(), this.position.getColumn() + 2);
				if (this.getBoard().piece(p1) == null && this.getBoard().piece(p2) == null) {
					mat[this.position.getRow()][this.position.getColumn() + 2] = true;
				}

			}
			// #special move queenside rook
			Position posT2 = new Position(this.position.getRow(), this.position.getColumn()-4);
			if (this.testRookCastling(posT2)) {
				Position p1 = new Position(this.position.getRow(), this.position.getColumn() - 1);
				Position p2 = new Position(this.position.getRow(), this.position.getColumn() - 2);
				Position p3 = new Position(this.position.getRow(), this.position.getColumn() - 3);
				if (this.getBoard().piece(p1) == null && this.getBoard().piece(p2) == null && this.getBoard().piece(p3) == null) {
					mat[this.position.getRow()][this.position.getColumn() - 2] = true;
				}
				
				
			}
			
		}

		return mat;
	}

}
