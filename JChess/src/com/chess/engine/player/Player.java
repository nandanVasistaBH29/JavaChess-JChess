package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.piece.King;
import com.chess.engine.piece.Piece;
import com.chess.engine.piece.Piece.PieceType;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public abstract class Player {

	protected final Board board;
	protected final King playerKing;
	// we need to specially keep track of king
	// to detect checks to the king
	
	private final boolean isInCheck;
	protected final Collection<Move> legalMoves;
	protected final Collection<Move> opponentLegalMoves;

	public Player(final Board board, final Collection<Move> legalMoves, final Collection<Move> opponentLegalMoves) {
		this.board = board;
		this.playerKing = establishKing();
		this.legalMoves =Lists.newArrayList(Iterables.concat(legalMoves, calculateKingCastles(legalMoves, opponentLegalMoves)));
		this.opponentLegalMoves=opponentLegalMoves;
		this.isInCheck=!Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(),opponentLegalMoves).isEmpty();
	}
	protected static Collection<Move> calculateAttacksOnTile(final int kingsPiecePos,final Collection<Move> movesByOpponent){
		final List<Move> kingUnderAttack=new ArrayList<>();
		for(Move currMove:movesByOpponent) {
			if(kingsPiecePos==currMove.getDestCordinate()) kingUnderAttack.add(currMove);
		}
		return ImmutableList.copyOf(kingUnderAttack);
	}
	private King establishKing() {
		for (final Piece piece : getActivePieces()) {
			if (piece.getPieceType().isKing()) {
				return (King) piece;
			}
		}
		throw new RuntimeException("Shouldn't reach here");
	}
	public abstract Collection<Piece> getActivePieces();
	public boolean isMoveLegal(final Move move) {
		return this.legalMoves.contains(move);
	}
	public boolean isInCheck() {
		return this.isInCheck;
	}
	public boolean isInCheckMate() {
		return this.isInCheck&&!hasEscapeMoves();
	}
	public boolean isInStaleMate() {
		return !this.isInCheck&&!hasEscapeMoves();
		//youre not in check but you don't have anything to do
	}
	public boolean isCastled() {
		return false;
	}
	
	protected boolean hasEscapeMoves() {
		for(final Move move:this.legalMoves) {
			final MoveTransition transition=makeMove(move);
			if(transition.getMoveStatus().isDone())return true;
		}
		return false;
	}
	public King getPlayerKing() {
		return this.playerKing;
	}
	
	
	public MoveTransition makeMove(final Move move) {
		if(!isMoveLegal(move)) {
			return new MoveTransition(this.board,move,MoveStatus.ILLEGAL_MOVE);
		}
		//legal
		final Board transitionBoard=move.execute();
		final Collection<Move> kingAttacks=Player.calculateAttacksOnTile(transitionBoard.getCurrentPlayer().getOpponent().getPlayerKing().getPiecePosition(), legalMoves);
		if(!kingAttacks.isEmpty()) {
			return new MoveTransition(this.board,move,MoveStatus.LEAVES_PLAYER_IN_CHECK);
		}
		return new MoveTransition(transitionBoard,move,MoveStatus.DONE);
	}
	public abstract Alliance getAlliance();
	public abstract Player getOpponent();
	public abstract Collection<Move> calculateKingCastles(Collection<Move> legaMoves,Collection<Move> opponentLegalMoves);
}