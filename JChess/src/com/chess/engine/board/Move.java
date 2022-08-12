package com.chess.engine.board;

import java.util.Objects;


import com.chess.engine.board.Board.Builder;
import com.chess.engine.piece.Pawn;
import com.chess.engine.piece.Piece;
import com.chess.engine.piece.Rook;

public abstract class Move {
	final Board board;
	final Piece movedPiece;
	final int destCord;
	public static final Move NULL_MOVE=new NullMove();

	@Override
	public int hashCode() {
		return Objects.hash(board, destCord, movedPiece);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Move other = (Move) obj;
		return Objects.equals(board, other.board) && destCord == other.destCord
				&& Objects.equals(movedPiece, other.movedPiece);
	}

	private Move(final Board board, final Piece movedPiece, final int destCord) {
		this.board = board;
		this.movedPiece = movedPiece;
		this.destCord = destCord;
	}

	// getter
	public Piece getMovedPiece() {
		return movedPiece;
	}
	public int getCurrentCoordinate() {
		return this.getMovedPiece().getPiecePosition();
	}
	public int getDestCordinate() {
		return destCord;
	}
	//helpers
	public boolean isAttack() {
		return false;
	}
	public boolean isCastlingMove() {
		return false;
	}
	public Piece getAttackedPiece() {
		return null;
	}
	// move is of 2 types
	// this the type of move which u would call if the tile is empty
	public static final class MajorMove extends Move {
		public MajorMove(final Board board, final Piece movedPiece, final int destCord) {
			super(board, movedPiece, destCord);
		}
	}

	// end
	public static class AttackMove extends Move {
		final Piece attackedPiece;

		public AttackMove(final Board board, final Piece movedPiece, final int destCord, final Piece attackedPiece) {
			super(board, movedPiece, destCord);
			this.attackedPiece = attackedPiece;
		}
		@Override
		public boolean isCastlingMove() {
			return true;
		}
		@Override
		public Piece getAttackedPiece() {
			return this.attackedPiece;
		}
		@Override
		public int hashCode() {
			return this.attackedPiece.hashCode()+super.hashCode();
			//not ide generated
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			AttackMove other = (AttackMove) obj;
			return Objects.equals(attackedPiece, other.attackedPiece);
		}
		
		
	}
	// end


	public Board execute() {
		final Builder builder = new Builder();
		// every other piece other than the movedPiece will be remaining same for the
		// current player
		for (final Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
			if (!this.movedPiece.equals(movedPiece)) {
				builder.setPiece(piece);
			}
		}
		for (final Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
			builder.setPiece(piece);// this is a non attack move so not to worry
		}
		// move the movedPiece
		builder.setPiece(this.movedPiece.movePiece(this)); // this refers to the piece which has to be moved
		builder.setNextMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());// next is opponent turn
		return builder.build();
		// when we change something on the board we return a new board
	}

	public static final class PawnMove extends Move {
		final Piece attackedPiece;

		public PawnMove(final Board board, final Piece movedPiece, final int destCord, final Piece attackedPiece) {
			super(board, movedPiece, destCord);
			this.attackedPiece = attackedPiece;
		}
	}

	public static class PawnMoveAttack extends AttackMove {
		public PawnMoveAttack(final Board board, final Piece movedPiece, final int destCord,
				final Piece attackedPiece) {
			super(board, movedPiece, destCord, attackedPiece);
		}
	}

	public static final class PawnEnPassantMoveAttack extends PawnMoveAttack {
		public PawnEnPassantMoveAttack(final Board board, final Piece movedPiece, final int destCord,
				final Piece attackedPiece) {
			super(board, movedPiece, destCord, attackedPiece);
		}
	}

	public static final class PawnJump extends Move {
		public PawnJump(final Board board, final Piece movedPiece, final int destCord, final Piece attackedPiece) {
			super(board, movedPiece, destCord);
		}
		@Override
		public Board execute() {
			final Builder builder =new Builder();
			for(final Piece piece:this.board.getCurrentPlayer().getActivePieces()) {
				if(!this.movedPiece.equals(piece)) {
					builder.setPiece(piece);
				}
			}
			for(final Piece piece:this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
				builder.setPiece(piece);
			}
			final Pawn movedPawn=(Pawn)this.movedPiece.movePiece(this);
			builder.setPiece(movedPawn);
			builder.setEnPassantPawn(movedPawn);
			builder.setNextMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
			return builder.build();
		}
	}

	static abstract class CastleMove extends Move {
		protected final Rook castleRook;
		protected final int castleRookStart;
		protected final int castleRookDest;
		public CastleMove(final Board board, final Piece movedPiece, final int destCord,final Rook castleRook,final int castleRookStart,final int castleRookDest) {
			super(board, movedPiece, destCord);
			this.castleRook=castleRook;
			this.castleRookStart=castleRookStart;
			this.castleRookDest=castleRookDest;
		}
		//getters
		public Rook getCastleRook() {
			return this.castleRook;
		}
		@Override
		public boolean isCastlingMove() {
			return true;
		}
		public Board execute() {
			final Builder builder=new Builder();
			for(final Piece piece:this.board.getCurrentPlayer().getActivePieces()) {
				//here moved piece is the king
				if(!this.movedPiece.equals(piece)&&!this.castleRook.equals(piece)) {
					builder.setPiece(piece);//basically don't set the rook and the king
				}
			}
			for(final Piece piece:this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
				builder.setPiece(piece);//nothing to do with opponents keep them as it is 
			}
			//now we have to set 2 pieces of current player that is king and the rook
			builder.setPiece(this.movedPiece.movePiece(this));//king set
			builder.setPiece(new Rook(this.castleRookDest,this.castleRook.getPieceAlliance()));
			builder.setNextMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
			return builder.build();
		}
	}

	public static final class KingSideCastleMove extends CastleMove {
		public KingSideCastleMove(final Board board, final Piece movedPiece, final int destCord,final com.chess.engine.piece	.Rook rook,final int castleRookStart,final int castleRookDest) {
			super(board, movedPiece, destCord, rook, castleRookStart, castleRookDest);
		}
		@Override
		public String toString() {
			return "O-O";
		}
	}

	public static final class QueenSideCastleMove extends CastleMove {
		public QueenSideCastleMove(final Board board, final Piece movedPiece, final int destCord,final Rook castleRook,final int castleRookStart,final int castleRookDest) {
			super(board, movedPiece, destCord, castleRook, castleRookStart, castleRookDest);
		}
		@Override
		public String toString() {
			return "O-O-O";
		}
	}
	public static final class NullMove extends Move {
		public NullMove() {
			super(null,null,-1);
		}
		@Override
		public Board execute() {
			throw new RuntimeException("can not exec null move");
		}
	}
	public static final class MoveFactory{
		//I added final keyword
		private MoveFactory() {
			throw new RuntimeException("not instantiable");
		}
		public static Move createMove(final Board board,final int currentCoordinate,final int destCoordinate) {
			for(final Move move:board.getAllLegalMoves()) {
				if(move.getCurrentCoordinate()==currentCoordinate&&move.getDestCordinate()==destCoordinate) return move;
			}
			return NULL_MOVE;
		}
	}
}