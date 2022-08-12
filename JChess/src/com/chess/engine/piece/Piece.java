package com.chess.engine.piece;

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.piece.Piece.PieceType;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import com.chess.engine.Alliance;

public abstract class Piece {
	protected final PieceType pieceType;
	protected final int piecePosition;// where is this piece located
	protected final Alliance pieceAlliance;// white or black alliance p1 or p2
	protected final boolean isFirstMove;
	private final int cachedHashCode;

	public Piece(final PieceType pieceType, final int piecePosition, final Alliance pieceAlliance) {
		this.pieceType = pieceType;
		this.piecePosition = piecePosition;
		this.pieceAlliance = pieceAlliance;
		this.cachedHashCode = computeHashCode();
		this.isFirstMove = false;
	}

	public Alliance getPieceAlliance() {
		return this.pieceAlliance;
	}

	public boolean isFirstMove() {
		return this.isFirstMove;
	}

	// all the legal moves the piece have
	public abstract Collection<Move> calculateLegalMoves(final Board board);
	// all types of pawn or piece will extend this class and override this method
	// as moves are different for each video

	public int getPiecePosition() {
		return this.piecePosition;
	}

	public PieceType getPieceType() {
		return this.pieceType;
	}

	// Completely browser generated
	@Override
	public int hashCode() {
		return this.cachedHashCode;
	}

	// default implementation for equals is reference equality
	// return this==obj // we don't want this
	// we are comparing 2 different objs so there reference will be different
	// we are checking whether 2 diff objs have same values or not
	// object equality not reference equality
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Piece other = (Piece) obj;
		return isFirstMove == other.isFirstMove && pieceAlliance == other.pieceAlliance
				&& piecePosition == other.piecePosition && pieceType == other.pieceType;
	}

	private int computeHashCode() {
		return Objects.hash(isFirstMove, pieceAlliance, piecePosition, pieceType);
	}

	public abstract Piece movePiece(Move move);

	public enum PieceType {
		PAWN("P") {
			@Override
			public boolean isKing() {
				return false;
			}
			@Override
			public boolean isRook() {
				return false;
			}
		},
		KNIGHT("K") {
			@Override
			public boolean isKing() {
				return false;
			}
			@Override
			public boolean isRook() {
				return false;
			}
		},
		BISHOP("B") {
			@Override
			public boolean isKing() {
				return false;
			}
			@Override
			public boolean isRook() {
				return false;
			}
		},
		ROOK("R") {
			@Override
			public boolean isKing() {
				return false;
			}
			@Override
			public boolean isRook() {
				return true;
			}
		},
		QUEEN("Q") {
			@Override
			public boolean isKing() {
				return false;
			}
			@Override
			public boolean isRook() {
				return false;
			}
		},
		KING("K") {
			@Override
			public boolean isKing() {
				return true;
			}
			@Override
			public boolean isRook() {
				return false;
			}
		};

		private String pieceName;

		PieceType(final String pieceName) {
			this.pieceName = pieceName;
		}

		public abstract boolean isKing();

		@Override
		public String toString() {
			return this.pieceName;
		}

		public abstract boolean isRook();
	}

}