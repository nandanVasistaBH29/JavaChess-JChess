package com.chess.engine.piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.piece.Piece.PieceType;
import com.google.common.collect.ImmutableList;

public class Pawn extends Piece {
	private final static int[] CANDIDATE_MOVE_COORDINATE = { 8, 16, 7, 9 };
	// TODO:first time it can move 2 boxes after reaching other corner it can be
	// promoted

	public Pawn(int piecePosition, Alliance pieceAlliance) {
		super(PieceType.PAWN, piecePosition, pieceAlliance);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		final List<Move> legalMoves = new ArrayList<>();
		for (final int currCandidateOffset : CANDIDATE_MOVE_COORDINATE) {
			int candidateDestinationCoordinate = this.piecePosition
					+ (this.getPieceAlliance().getDirection() * currCandidateOffset);
			// black +8 white means -8 .getDirection -8 or 8
			boolean condition1 = (BoardUtils.SECOND_ROW[this.piecePosition] && this.getPieceAlliance().isBlack());
			boolean condition2 = (BoardUtils.SEVENTH_ROW[this.piecePosition] && this.getPieceAlliance().isWhite());
			if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate))
				continue;

			if (currCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
				legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
			} else if (currCandidateOffset == 16 && this.isFirstMove() && (condition1 || condition2)) {
				final int behindCandiDestCoordinate = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
				if (!board.getTile(behindCandiDestCoordinate).isTileOccupied()
						&& !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
					legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
				}
			} else if (currCandidateOffset == 7
					&& !(BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()
							|| BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack())) {
				if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
					final Piece pawnUnderAttack = board.getTile(candidateDestinationCoordinate).getPiece();
					if (pawnUnderAttack.getPieceAlliance() != this.pieceAlliance) {
						// attack move TODO
						legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
					}
				}
			} else if (currCandidateOffset == 9
					&& !(BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()
							|| BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite())) {
				if (board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
					final Piece pawnUnderAttack = board.getTile(candidateDestinationCoordinate).getPiece();
					if (pawnUnderAttack.getPieceAlliance() != this.pieceAlliance) {
						// attack move TODO
						legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
					}
				}
			}
		}
		return ImmutableList.copyOf(legalMoves);
	}

	@Override
	public String toString() {
		return PieceType.PAWN.toString();
	}

	@Override
	public Pawn movePiece(final Move move) {
		return new Pawn(move.getDestCordinate(), move.getMovedPiece().getPieceAlliance());
	}

}