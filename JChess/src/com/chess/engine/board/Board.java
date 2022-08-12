package com.chess.engine.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.chess.engine.Alliance;
import com.chess.engine.piece.Bishop;
import com.chess.engine.piece.King;
import com.chess.engine.piece.Knight;
import com.chess.engine.piece.Pawn;
import com.chess.engine.piece.Piece;
import com.chess.engine.piece.Queen;
import com.chess.engine.piece.Rook;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

public class Board {
	
	private final  List<Tile> gameBoard;
	//why is it a list of tiles(64 tiles) not an array
	//arrays are not immutable in java
	private final Collection<Piece> whiteActivePieces;
	private final Collection<Piece> blackActivePieces;
	private  WhitePlayer whitePlayer=null;
	private BlackPlayer blackPlayer=null;
	private final Player currentPlayer;
	//active piece
	
	//the constructor is made private 
	//basically we just want one instance of the board
	private Board(Builder builder) {
		this.gameBoard=createGameBoard(builder);
		this.whiteActivePieces=calculateActivePiece(this.gameBoard,Alliance.WHITE);
		this.blackActivePieces=calculateActivePiece(this.gameBoard,Alliance.BLACK);
		this.currentPlayer=builder.nextMoveMaker.choosePlayer(this.whitePlayer,this.blackPlayer);
		
		Collection<Move> LegalMovesWhite=calculateLegalMoves(this.whiteActivePieces);
		Collection<Move> LegalMovesBlack=calculateLegalMoves(this.blackActivePieces);
		
		this.blackPlayer=new BlackPlayer(this,LegalMovesBlack,LegalMovesWhite);
		this.whitePlayer=new WhitePlayer(this,LegalMovesWhite,LegalMovesBlack);
	}
	
	// is this static ????????????
	private  Collection<Piece> calculateActivePiece(final List<Tile> gameBoard,final Alliance alliance) {
		final List<Piece> activeOnes=new ArrayList<>();
		
		for(final Tile tile:gameBoard) {
			if(!tile.isTileOccupied()) continue;
			Piece currPiece=tile.getPiece();
			if(currPiece.getPieceAlliance()==alliance) activeOnes.add(currPiece);
		}
		return ImmutableList.copyOf(activeOnes);
	}
	//calculate legal moves for paritcular type of alliance for all its active pieces
	
	private Collection<Move> calculateLegalMoves(final Collection<Piece> activePieces ){
		final List<Move> legalMoves=new ArrayList<>();
		for(Piece currPiece:activePieces) {
			Collection<Move> currPieceMoves=currPiece.calculateLegalMoves(this); //this is board (this class)
			if(currPieceMoves!=null)legalMoves.addAll(currPieceMoves);
		}
		return ImmutableList.copyOf(legalMoves);
	}
	
	//create initial layout
	public static Board createStandardBoard() {
		final Builder builder = new Builder();
        // Black Layout
        builder.setPiece(new Rook(0, Alliance.BLACK));
        builder.setPiece(new Knight(1, Alliance.BLACK));
        builder.setPiece(new Bishop(2, Alliance.BLACK));
        builder.setPiece(new Queen(3, Alliance.BLACK));
        builder.setPiece(new King(4, Alliance.BLACK));
        builder.setPiece(new Bishop(5, Alliance.BLACK));
        builder.setPiece(new Knight(6,Alliance.BLACK));
        builder.setPiece(new Rook(7, Alliance.BLACK));
        builder.setPiece(new Pawn(8, Alliance.BLACK));
        builder.setPiece(new Pawn(9, Alliance.BLACK));
        builder.setPiece(new Pawn(10, Alliance.BLACK));
        builder.setPiece(new Pawn(11, Alliance.BLACK));
        builder.setPiece(new Pawn(12, Alliance.BLACK));
        builder.setPiece(new Pawn(13, Alliance.BLACK));
        builder.setPiece(new Pawn(14, Alliance.BLACK));
        builder.setPiece(new Pawn(15, Alliance.BLACK));
        // White Layout
        builder.setPiece(new Pawn(48,Alliance.WHITE));
        builder.setPiece(new Pawn(49,Alliance.WHITE));
        builder.setPiece(new Pawn(50,Alliance.WHITE));
        builder.setPiece(new Pawn(51,Alliance.WHITE));
        builder.setPiece(new Pawn(52,Alliance.WHITE));
        builder.setPiece(new Pawn(53,Alliance.WHITE));
        builder.setPiece(new Pawn(54,Alliance.WHITE));
        builder.setPiece(new Pawn(55,Alliance.WHITE));
        builder.setPiece(new Rook(56,Alliance.WHITE));
        builder.setPiece(new Knight(57,Alliance.WHITE));
        builder.setPiece(new Bishop(58,Alliance.WHITE));
        builder.setPiece(new Queen(59,Alliance.WHITE));
        builder.setPiece(new King(60,Alliance.WHITE));
        builder.setPiece(new Bishop(61,Alliance.WHITE));
        builder.setPiece(new Knight(62,Alliance.WHITE));
        builder.setPiece(new Rook(63,Alliance.WHITE));
        //white to move
        builder.setNextMoveMaker(Alliance.WHITE);
        //build the board
        return builder.build();
	}
	private List<Tile> createGameBoard(Builder builder){
		final Tile[] tiles=new Tile[64];//64 tiles in total
		for(int i=0;i<64;i++) {
			tiles[i]=Tile.createTile(i, builder.boardConfig.get(i));
		}
		return ImmutableList.copyOf(tiles);
	}
	public  Tile getTile(final int tileCoordinate) {
		return gameBoard.get(tileCoordinate);
	}
	@Override
	public String toString() {
		final StringBuilder sb=new StringBuilder();
		for(int i=0;i<64;i++) {
			final String tileText=this.gameBoard.get(i).toString(); 
			sb.append(String.format("%3s", tileText));
			if((i+1)%8==0) sb.append("\n");
		}
		return sb.toString();
	}
	public static class Builder{
		//inner class Builder which is used to build the board 
		//builder builds the board once and then for the next subsequent times
		//it will just return the ref of it
		
		//integer is piece position	and piece is piece
		Map<Integer, Piece> boardConfig;
		Alliance nextMoveMaker;//whose is the next move
		Pawn enPassantPawn;
		public Builder(){
			this.boardConfig=new HashMap<Integer, Piece>();
		}
		public Builder setPiece(final Piece piece) {
			this.boardConfig.put(piece.getPiecePosition(), piece);
			return this;//return curr builder which is modified
		}
		public Builder setNextMoveMaker(final Alliance nextMoveMaker) {
			this.nextMoveMaker=nextMoveMaker;
			//they will send it is white or black antha u need to just set it
			//after setting it you need send back the reference 
			return this;
		}
		public Board build() {
			return new Board(this);
		}
		public void setEnPassantPawn(Pawn enPassantPawn) {
			this.enPassantPawn=enPassantPawn;	
		}
		
	}
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	public Collection<Piece> getWhitePieces() {
		return whiteActivePieces;
	}
	public Collection<Piece> getBlackPieces() {
		return blackActivePieces;
	}
//called by Players
	public Player getBlackPlayer() {
		return blackPlayer;
	}
	public Player getWhitePlayer() {
		return whitePlayer;
	}

	public Iterable<Move> getAllLegalMoves() {
		//its in guava
		//just add both white legal moves and black legal moves
		Collection<Move> whiteLegalMoves=this.whitePlayer.getLegalMoves();
		Collection<Move> blackLegalMoves=this.blackPlayer.getLegalMoves();
		return Iterables.concat(whiteLegalMoves, blackLegalMoves);
	}
}