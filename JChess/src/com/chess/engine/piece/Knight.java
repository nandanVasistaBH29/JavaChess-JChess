package com.chess.engine.piece;

import java.util.ArrayList;
import java.util.Collection;
import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.board.Tile;
import com.chess.engine.piece.Piece;
import com.chess.engine.piece.Piece.PieceType;
import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.Immutable;

public class Knight extends Piece {
	private final static int[] CANDIDATE_NEW_COORDINATES = { -17, -15, -10, -6, 6, 10, 15, 17 };

	// when u add these above numbers from you get new possible positions for knight
	// it is possible cuz after adding the new position might be >63 or <0 so
	// and the new position could be occupied by the same alliance
	public Knight(final int piecePosition, final Alliance pieceAlliance) {
		super(PieceType.KNIGHT, piecePosition, pieceAlliance);
	}

	@Override
	public Collection<Move> calculateLegalMoves(Board board) {

		final Collection<Move> legalMoves = new ArrayList<>();
		for (int currentCandiOffset : CANDIDATE_NEW_COORDINATES) {
			final int candidateDestinationCoordinate = this.piecePosition + currentCandiOffset;

			if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
				if (isFirstColumnExclusion(this.piecePosition, currentCandiOffset)
						|| isEighthColumnExclusion(this.piecePosition, currentCandiOffset)
						|| isSecondColumnExclusion(this.piecePosition, currentCandiOffset)
						|| isSeventhColumnExclusion(this.piecePosition, currentCandiOffset))
					continue;
				final Tile candiDestTile = board.getTile(candidateDestinationCoordinate);
				if (!candiDestTile.isTileOccupied()) {
					// if its not occupied
					legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));// occupy move
					// this -> this particular knight
				} else {
					// it is occupied we nee dto check whether it is of the same alliance then it is
					// not a legal move
					// else we need to add it as a move but of a different kind
					final Piece pieceAtDest = candiDestTile.getPiece();
					final Alliance pieceAtDestAlliance = pieceAtDest.getPieceAlliance();
					if (this.pieceAlliance != pieceAtDestAlliance) {
						legalMoves.add(new AttackMove(board, this, candidateDestinationCoordinate, pieceAtDest));// attacking
																													// move
					}
				}
			}
		}
		return ImmutableList.copyOf(legalMoves);
	}

	@Override
	public String toString() {
		return PieceType.KNIGHT.toString();
	}

	// all these are to prevent going out of the chess board we could have used
	// matrix instead of normal arrays
	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.FIRST_COLUMN[currentPosition] && ((candidateOffset == -17) || (candidateOffset == -10)
				|| (candidateOffset == 6) || (candidateOffset == 15));
	}

	private static boolean isSecondColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.SECOND_COLUMN[currentPosition] && ((candidateOffset == -10) || (candidateOffset == 6));
	}

	private static boolean isSeventhColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.SEVENTH_COLUMN[currentPosition] && ((candidateOffset == -6) || (candidateOffset == 10));
	}

	private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.EIGHTH_COLUMN[currentPosition] && ((candidateOffset == -15) || (candidateOffset == -6)
				|| (candidateOffset == 10) || (candidateOffset == 17));
	}

	@Override
	public Knight movePiece(final Move move) {
		return new Knight(move.getDestCordinate(), move.getMovedPiece().getPieceAlliance());
	}

}