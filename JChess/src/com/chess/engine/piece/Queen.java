package com.chess.engine.piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.board.Move.AttackMove;
import com.chess.engine.board.Move.MajorMove;
import com.chess.engine.piece.Piece.PieceType;
import com.google.common.collect.ImmutableList;

public class Queen extends Piece {
	private static final int[] CANDIDATE_NEW_COORDINATES = { -9, -8, -7, -1, 1, 7, 8, 9 };

	public Queen(int piecePosition, Alliance pieceAlliance) {
		super(PieceType.QUEEN,piecePosition, pieceAlliance);
	}

	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		final List<Move> legalMoves = new ArrayList<>();
		for (final int candidateCoordinateOffset : CANDIDATE_NEW_COORDINATES) {
			int candidateDestination = this.piecePosition;
			while (BoardUtils.isValidTileCoordinate(candidateDestination)) {
				if (isFirstColumnExclusion(candidateDestination, candidateCoordinateOffset)
						|| isEigthColumnExclusion(candidateDestination, candidateCoordinateOffset)) {
					break;
				}
				candidateDestination += candidateCoordinateOffset;
				if (BoardUtils.isValidTileCoordinate(candidateDestination)) {
					final Tile candiDestTile = board.getTile(candidateDestination);
					if (!candiDestTile.isTileOccupied()) {
						// if its not occupied
						legalMoves.add(new MajorMove(board, this, candidateDestination));// occupy move
						// this -> this particular knight
					} else {
						// it is occupied we nee dto check whether it is of the same alliance then it is
						// not a legal move
						// else we need to add it as a move but of a different kind
						final Piece pieceAtDest = candiDestTile.getPiece();
						final Alliance pieceAtDestAlliance = pieceAtDest.getPieceAlliance();
						if (this.pieceAlliance != pieceAtDestAlliance) {
							legalMoves.add(new AttackMove(board, this, candidateDestination, pieceAtDest));// attacking
																											// move
						}
						break;// you can't jump over pawn so
					}
				}

			}
		}
		return ImmutableList.copyOf(legalMoves);
	}
	@Override
	public String toString() {
		return PieceType.QUEEN.toString();
	}

	private static boolean isFirstColumnExclusion(final int currPos, final int candidateOffset) {
		return BoardUtils.FIRST_COLUMN[currPos]
				&& (candidateOffset == -1 || candidateOffset == -9 | candidateOffset == 7);
	}

	private static boolean isEigthColumnExclusion(final int currPos, final int candidateOffset) {
		return BoardUtils.EIGHTH_COLUMN[currPos]
				&& (candidateOffset == -7 | candidateOffset == 9 || candidateOffset == 9);
	}
	@Override
	public Queen movePiece(final Move move) {
		return new Queen(move.getDestCordinate(), move.getMovedPiece().getPieceAlliance());
	}
}