package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.piece.Piece;
import com.chess.engine.piece.Rook;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class WhitePlayer extends Player {
	Collection<Move> legalMovesWhite;

	public WhitePlayer(Board board, Collection<Move> legalMovesWhite, Collection<Move> legalMovesBlack) {
		super(board, legalMovesWhite, legalMovesBlack);
		this.legalMovesWhite = legalMovesWhite;
	}

	@Override
	public Collection<Piece> getActivePieces() {
		return this.board.getWhitePieces();
	}

	@Override
	public Alliance getAlliance() {
		return Alliance.WHITE;
	}

	@Override
	public Player getOpponent() {
		return this.board.getBlackPlayer();
	}

	public Collection<Move> getLegalMoves() {
		return this.legalMovesWhite;
	}

	@Override
	public Collection<Move> calculateKingCastles(Collection<Move> legaMoves, Collection<Move> opponentLegalMoves) {

		final List<Move> kingCastles = new ArrayList<>();
		// white king side castle
		if (this.playerKing.isFirstMove() && !this.isInCheck()) {
			// white king side castle
			if (!this.board.getTile(61).isTileOccupied() && !this.board.getTile(62).isTileOccupied()) {
				final Tile rookTile = this.board.getTile(63);
				if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
					// now should check for opponents attack you can't castle into a check
					if (Player.calculateAttacksOnTile(61, opponentLegalMoves).isEmpty()
							&& Player.calculateAttacksOnTile(62, opponentLegalMoves).isEmpty()
							&& rookTile.getPiece().getPieceType().isRook()) {
						kingCastles.add(new Move.KingSideCastleMove(this.board, this.playerKing, 62,
								(Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 61));
					}
				}
			}
		}
		// white queen side castle
		if (this.playerKing.isFirstMove() && !this.isInCheck()) {
			// white king side castle
			if (!this.board.getTile(59).isTileOccupied() && !this.board.getTile(58).isTileOccupied()
					&& !this.board.getTile(57).isTileOccupied()) {
				final Tile rookTile = this.board.getTile(56);// queen side rook
				if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
					if (Player.calculateAttacksOnTile(58, opponentLegalMoves).isEmpty()
							&& Player.calculateAttacksOnTile(59, opponentLegalMoves).isEmpty()
							&& rookTile.getPiece().getPieceType().isRook()) {
						// this inner if added my me
						kingCastles.add(new Move.QueenSideCastleMove(this.board, this.playerKing, 58,
								(Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 59));
					}
				}
			}
		}
		return ImmutableList.copyOf(kingCastles);

	}

}