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

public class BlackPlayer extends Player {
	Collection<Move> legalMovesBlack;

	public BlackPlayer(Board board, Collection<Move> legalMovesBlack, Collection<Move> legalMovesWhite) {
		super(board, legalMovesBlack, legalMovesWhite);
		this.legalMovesBlack = legalMovesBlack;
	}

	@Override
	public Collection<Piece> getActivePieces() {
		// TODO Auto-generated method stub
		return this.board.getBlackPieces();
	}

	@Override
	public Alliance getAlliance() {
		return Alliance.WHITE;
	}

	@Override
	public Player getOpponent() {
		return this.board.getWhitePlayer();
	}

	public Collection<Move> getLegalMoves() {
		return this.legalMovesBlack;
	}

	@Override
	public Collection<Move> calculateKingCastles(Collection<Move> legaMoves, Collection<Move> opponentLegalMoves) {

		final List<Move> kingCastles = new ArrayList<>();
		// black king side castle
		if (this.playerKing.isFirstMove() && !this.isInCheck()) {
			// white king side castle
			if (!this.board.getTile(5).isTileOccupied() && !this.board.getTile(6).isTileOccupied()) {
				final Tile rookTile = this.board.getTile(7);
				if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
					// now should check for opponents attack you can't castle into a check
					if (Player.calculateAttacksOnTile(5, opponentLegalMoves).isEmpty()
							&& Player.calculateAttacksOnTile(6, opponentLegalMoves).isEmpty()
							&& rookTile.getPiece().getPieceType().isRook()) {
						kingCastles.add(new Move.KingSideCastleMove(this.board, this.playerKing, 6,
								(Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 5));
					}
				}
			}
		}
		// black queen side castle
		if (this.playerKing.isFirstMove() && !this.isInCheck()) {
			// white king side castle
			if (!this.board.getTile(1).isTileOccupied() && !this.board.getTile(2).isTileOccupied()
					&& !this.board.getTile(3).isTileOccupied()) {
				final Tile rookTile = this.board.getTile(0);// queen side rook
				if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
					// now should check for opponents attack you can't castle into a check
					if (Player.calculateAttacksOnTile(2, opponentLegalMoves).isEmpty()
							&& Player.calculateAttacksOnTile(3, opponentLegalMoves).isEmpty()
							&& rookTile.getPiece().getPieceType().isRook()) {
						kingCastles.add(new Move.QueenSideCastleMove(this.board, this.playerKing, 2,
								(Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 3));
					}
				}
			}
		}
		return ImmutableList.copyOf(kingCastles);

	}
}