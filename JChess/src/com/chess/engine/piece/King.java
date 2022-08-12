package com.chess.engine.piece;

import java.util.ArrayList;
import java.util.Collection;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.piece.Piece.PieceType;
import com.google.common.collect.ImmutableList;

public class King extends Piece {
	final static int[] CANDIDATE_NEW_COORDINATES = { -9, -8, -7, -1, 1, 7, 8, 9 };

	public King(int piecePosition, Alliance pieceAlliance) {
		super(PieceType.KING, piecePosition, pieceAlliance);
	}

	@Override
	public Collection<Move> calculateLegalMoves(Board board) {

		final Collection<Move> legalMoves = new ArrayList<>();
		for (int currentCandiOffset : CANDIDATE_NEW_COORDINATES) {
			final int candidateDestinationCoordinate = this.piecePosition + currentCandiOffset;

			if (BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
				if (isFirstColumnExclusion(this.piecePosition, currentCandiOffset)
						|| isEighthColumnExclusion(this.piecePosition, currentCandiOffset))
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
		return PieceType.KING.toString();
	}

	private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.FIRST_COLUMN[currentPosition]
				&& ((candidateOffset == -9) || (candidateOffset == -1) || (candidateOffset == 7));
	}

	private static boolean isEighthColumnExclusion(final int currentPosition, final int candidateOffset) {
		return BoardUtils.FIRST_COLUMN[currentPosition]
				&& ((candidateOffset == -7) || (candidateOffset == 1) || (candidateOffset == 9));
	}

	@Override
	public King movePiece(final Move move) {
		return new King(move.getDestCordinate(), move.getMovedPiece().getPieceAlliance());
	}
}