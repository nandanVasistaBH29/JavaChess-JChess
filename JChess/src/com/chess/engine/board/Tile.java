package com.chess.engine.board;
import com.chess.engine.Alliance;
import com.chess.engine.piece.*;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

public abstract class Tile {
	// chess is 64 cells grid so we can imagine it as a 8X8 2D Matrix
	// this tileCoordinate variable is there so as to identify which tile are we
	// referring to
	protected final int tileCoordinate;// for that particular tile the tile coordinate is fixed
	// we shall create an immutable map which store key as tileCoordinate and value
	// the corresponding empty tile
	private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();
	// there can be 2 states to the Tile class either when the tile is occupied or
	// it is not occupied

	// to get to know whether a tile is occupied or not
	public abstract boolean isTileOccupied();
	
	//getter
	public int getTileCoordinate() {
		return this.tileCoordinate;
	}
	private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
		final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();
		for (int i = 0; i < 64; i++)
			emptyTileMap.put(i, new EmptyTile(i));
		return ImmutableMap.copyOf(emptyTileMap);// just returning emptyTileMap one could access and change the map to
													// hold some one might increase the number of tiles
		// to avoid these changes we make it immutable
	}

	// to get the piece or the pawn which is occupied its null if no piece occupy's
	// it
	public abstract Piece getPiece();

	private Tile(int tileCoordinate) {
		this.tileCoordinate = tileCoordinate;
	}

	// all the 64 empty times has been created you should't call anymore empty tiles
	public static Tile createTile(final int tileCoordinate, final Piece piece) {
		return piece != null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
	}

	// state 0
	public static final class EmptyTile extends Tile {
		private EmptyTile(final int tileCoordinate) {
			super(tileCoordinate);
		}

		// those abstract methods need to be implemented/defined
		@Override
		public boolean isTileOccupied() {
			return false;
		}
		@Override 
		public String toString() {
			return "-";
		}
		@Override
		public Piece getPiece() {
			return null;
		}
	}

	// state1
	public static final class OccupiedTile extends Tile {
		private final Piece pieceOnTile;// the pawn which is currently occupying the tile
		// why is it final ? piece on the tile can change from time to time

		private OccupiedTile(int tileCoordinate, Piece pieceOnTile) {
			super(tileCoordinate);
			this.pieceOnTile = pieceOnTile;
		}

		@Override
		public boolean isTileOccupied() {
			return true;
		}
		@Override
		public String toString() {
			return this.getPiece().getPieceAlliance()==Alliance.BLACK ? this.getPiece().toString().toLowerCase():this.getPiece().toString();
		}
		@Override
		public Piece getPiece() {
			return this.pieceOnTile;
		}
		// we could have defined these 2 inner classes in a separate file
		// (instead of making them as inner class separate file for each of these 2)
		// as the classes are inner class
		// these have to be declared as static because state of the class must be
		// independent of the super class
	}

}