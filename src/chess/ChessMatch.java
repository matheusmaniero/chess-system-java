package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch {

	private Board board;
	private int turn;
	private Color currentPlayer;
	private List<ChessPiece> piecesOnTheBoard = new ArrayList<>();
	private List<ChessPiece> capturedPieces = new ArrayList<>();
	private boolean check;
	private boolean checkMate;
	private ChessPiece enPassantVulnerable;

	public ChessMatch() {
		board = new Board(8, 8);
		this.turn = 1;
		this.currentPlayer = Color.WHITE;
		initialSetup();

	}

	public ChessPiece getEnPassantVulnerable() {
		return enPassantVulnerable;
	}

	public boolean getCheckMate() {
		return checkMate;
	}

	public boolean getCheck() {
		return check;
	}

	public int getTurn() {
		return turn;
	}

	public Color getCurrentPlayer() {
		return currentPlayer;
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

	public boolean[][] possibleMoves(ChessPosition sourcePosition) {
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return this.board.piece(position).possibleMoves();
	}

	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target);
		if (testCheck(this.currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("You can't put yourself in check!");
		}

		ChessPiece movedPiece = (ChessPiece) this.board.piece(target);

		this.check = testCheck(this.opponent(this.currentPlayer)) ? true : false;
		if (testCheckMate(opponent(currentPlayer))) {
			this.checkMate = true;
		} else {
			nextTurn();
		}

		// #specialmove en passant
		if (movedPiece instanceof Pawn && target.getRow() == source.getRow() - 2
				|| target.getRow() == source.getRow() + 2) {
			this.enPassantVulnerable = movedPiece;
		} else {
			this.enPassantVulnerable = null;
		}

		return (ChessPiece) capturedPiece;
	}

	public void validateSourcePosition(Position position) {
		if (!this.board.thereIsAPiece(position)) {
			throw new ChessException("There is no piece on source position.");
		}
		if (currentPlayer != ((ChessPiece) this.board.piece(position)).getColor()) {
			throw new ChessException("The chosen piece is not yours.");
		}

		if (!this.board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("There is no possible moves for the chosen piece.");
		}
	}

	public void validateTargetPosition(Position source, Position target) {
		if (!this.board.piece(source).possibleMove(target)) {
			throw new ChessException("The chosen piece can't move to target position.");
		}
	}

	private Piece makeMove(Position source, Position target) {
		ChessPiece p = (ChessPiece) this.board.removePiece(source);
		p.increaseMoveCount();
		Piece capturedPiece = this.board.removePiece(target);
		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add((ChessPiece) capturedPiece);
		}
		this.board.placePiece(p, target);
		// #specialmove kingside rook
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece) this.board.removePiece(sourceT);
			this.board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}
		// #specialmove queenside rook
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece) this.board.removePiece(sourceT);
			this.board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}

		// #specialmove en passant
		if (p instanceof Pawn) {
			if (source.getColumn() != target.getColumn() && capturedPiece == null) {
				Position capturedPawnPosition;
				if (p.getColor() == Color.WHITE) {
					capturedPawnPosition = new Position(target.getRow() + 1, target.getColumn());
				} else {
					capturedPawnPosition = new Position(target.getRow() - 1, target.getColumn());
				}

				capturedPiece = this.board.removePiece(capturedPawnPosition);
				capturedPieces.add((ChessPiece) capturedPiece);
				piecesOnTheBoard.remove(capturedPiece);
			}
		}

		return capturedPiece;
	}

	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece) this.board.removePiece(target);
		p.decreaseMoveCount();
		this.board.placePiece(p, source);
		if (capturedPiece != null) {
			this.board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add((ChessPiece) capturedPiece);
		}

		// #specialmove kingside rook
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece) this.board.removePiece(targetT);
			this.board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}
		// #specialmove queenside rook
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece) this.board.removePiece(targetT);
			this.board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}
		// #specialmove en passant
		if (p instanceof Pawn) {
			if (source.getColumn() != target.getColumn() && capturedPiece == this.enPassantVulnerable) {
				Position capturedPawnPosition;
				ChessPiece pawn = (ChessPiece)this.board.removePiece(target);
				if (p.getColor() == Color.WHITE) {
					capturedPawnPosition = new Position(target.getRow() + 1, target.getColumn());
				} else {
					capturedPawnPosition = new Position(target.getRow() - 1, target.getColumn());
				}

				this.board.placePiece(pawn, capturedPawnPosition);
				
			}
		}

	}

	private Color opponent(Color color) {
		return (color == color.WHITE) ? color.BLACK : color.WHITE;
	}

	private ChessPiece king(Color color) {
		List<ChessPiece> list = piecesOnTheBoard.stream().filter(x -> x.getColor() == color)
				.collect(Collectors.toList());
		for (ChessPiece p : list) {
			if (p instanceof King) {
				return p;
			}
		}
		throw new IllegalStateException("There is no " + color + " king on the board.");

	}

	private boolean testCheck(Color color) {
		List<ChessPiece> opponentPieces = piecesOnTheBoard.stream().filter(x -> x.getColor() == this.opponent(color))
				.collect(Collectors.toList());
		Position kingPosition = king(color).getChessPosition().toPosition();
		for (ChessPiece p : opponentPieces) {
			if (p.possibleMove(kingPosition)) {
				return true;
			}

		}
		return false;

	}

	private boolean testCheckMate(Color color) {
		if (!testCheck(color)) {
			return false;
		}
		List<ChessPiece> list = piecesOnTheBoard.stream().filter(x -> x.getColor() == color)
				.collect(Collectors.toList());
		for (ChessPiece p : list) {
			boolean[][] mat = p.possibleMoves();
			for (int i = 0; i < mat.length; i++) {
				for (int j = 0; j < mat[i].length; j++) {
					if (mat[i][j]) {
						Position source = p.getChessPosition().toPosition();
						Position target = new Position(i, j);
						Piece capturedPiece = makeMove(source, target);
						boolean isCheckMate = testCheck(color);
						undoMove(source, target, capturedPiece);
						if (!isCheckMate) {
							return false;
						}

					}
				}
			}
		}
		return true;

	}

	protected void placeNewPiece(char column, int row, ChessPiece piece) {
		this.board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}

	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;

	}

	private void initialSetup() {
		placeNewPiece('a', 1, new Rook(board, Color.WHITE));
		placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('b', 1, new Knight(board, Color.WHITE));
		placeNewPiece('g', 1, new Knight(board, Color.WHITE));
		placeNewPiece('d', 1, new Queen(board, Color.WHITE));
		placeNewPiece('e', 1, new King(board, Color.WHITE, this));
		placeNewPiece('h', 1, new Rook(board, Color.WHITE));
		placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this));

		placeNewPiece('a', 8, new Rook(board, Color.BLACK));
		placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('d', 8, new Queen(board, Color.BLACK));
		placeNewPiece('b', 8, new Knight(board, Color.BLACK));
		placeNewPiece('g', 8, new Knight(board, Color.BLACK));
		placeNewPiece('e', 8, new King(board, Color.BLACK, this));
		placeNewPiece('h', 8, new Rook(board, Color.BLACK));
		placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));

	}

}
