package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {

	private ChessMatch chessMatch;

	public Pawn(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch = chessMatch;
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[this.getBoard().getRows()][this.getBoard().getColumns()];
		Position p = new Position(0, 0);
		if (this.getColor() == Color.WHITE) {
			p.setValues(position.getRow() - 1, position.getColumn());
			if (this.getBoard().positionExists(p) && !this.getBoard().thereIsAPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}

			p.setValues(position.getRow() - 2, position.getColumn());
			Position p2 = new Position(position.getRow() - 1, position.getColumn());
			if (this.getBoard().positionExists(p) && !this.getBoard().thereIsAPiece(p) && getMoveCount() == 0
					&& !this.getBoard().thereIsAPiece(p2) && this.getBoard().positionExists(p2)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			p.setValues(position.getRow() - 1, position.getColumn() - 1);
			if (this.getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			p.setValues(position.getRow() - 1, position.getColumn() + 1);
			if (this.getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}

			// #special move en passant white

			if (this.position.getRow() == 3) {
				Position left = new Position(this.position.getRow(), this.position.getColumn() - 1);
				if (this.getBoard().positionExists(left) && this.isThereOpponentPiece(left)
						&& (ChessPiece) this.getBoard().piece(left) == this.chessMatch.getEnPassantVulnerable()) {
					mat[left.getRow() - 1][left.getColumn()] = true;
				}
				Position right = new Position(this.position.getRow(), this.position.getColumn() + 1);
				if (this.getBoard().positionExists(right) && this.isThereOpponentPiece(right)
						&& (ChessPiece) this.getBoard().piece(right) == this.chessMatch.getEnPassantVulnerable()) {
					mat[right.getRow() - 1][right.getColumn()] = true;
				}
			}

		} else {

			p.setValues(position.getRow() + 1, position.getColumn());
			if (this.getBoard().positionExists(p) && !this.getBoard().thereIsAPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}

			p.setValues(position.getRow() + 2, position.getColumn());
			Position p2 = new Position(position.getRow() + 1, position.getColumn());
			if (this.getBoard().positionExists(p) && !this.getBoard().thereIsAPiece(p) && getMoveCount() == 0
					&& !this.getBoard().thereIsAPiece(p2) && this.getBoard().positionExists(p2)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			p.setValues(position.getRow() + 1, position.getColumn() - 1);
			if (this.getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
			p.setValues(position.getRow() + 1, position.getColumn() + 1);
			if (this.getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}

			// #special move en passant black

			if (this.position.getRow() == 4) {
				Position left = new Position(this.position.getRow(), this.position.getColumn() - 1);
				if (this.getBoard().positionExists(left) && this.isThereOpponentPiece(left)
						&& (ChessPiece) this.getBoard().piece(left) == this.chessMatch.getEnPassantVulnerable()) {
					mat[left.getRow() + 1][left.getColumn()] = true;
				}
				Position right = new Position(this.position.getRow(), this.position.getColumn() + 1);
				if (this.getBoard().positionExists(right) && this.isThereOpponentPiece(right)
						&& (ChessPiece) this.getBoard().piece(right) == this.chessMatch.getEnPassantVulnerable()) {
					mat[right.getRow() + 1][right.getColumn()] = true;
				}
			}

		}

		return mat;

	}

	@Override
	public String toString() {
		return "P";
	}

}
