package com.chess.engine.board;

public class BoardUtils {
	//remember our board is not a matrix but an array 0-63
	public static final boolean[] FIRST_ROW=initRow(0);
	public static final boolean[] SECOND_ROW=initRow(8);//the tile which begins a row
	public static final boolean[] THIRD_ROW=initRow(16);
	public static final boolean[] FORTH_ROW=initRow(24);
	public static final boolean[] FIFTH_ROW=initRow(32);
	public static final boolean[] SIXTH_ROW=initRow(40);
	public static final boolean[] SEVENTH_ROW=initRow(48);
	public static final boolean[] EIGTH_ROW=initRow(56);
	
	
	//cols
	public static final boolean[] EIGHTH_COLUMN = initCol(7);
	public static final boolean[] SEVENTH_COLUMN =  initCol(6);
	public static final boolean[] SECOND_COLUMN =  initCol(1);
	public static final boolean[] FIRST_COLUMN =  initCol(0);
	//as this a utility class we should not create any methods of it 
	private BoardUtils() {
		throw new RuntimeException("you should not instantiate me");
	}
	private static boolean[] initRow(int rowNo) {
		//mark each element in that RowNo
		boolean[] row=new boolean[64];
		do {
			row[rowNo]=true;
			rowNo++;
		}while(rowNo%8!=0);
		return row;
	}
	private static boolean[] initCol(int colNo) {
		//mark each element in that colNo
		boolean[] col=new boolean[64];
		do {
			col[colNo]=true;
			colNo+=8;
		}while(colNo<64);
		return col;
		
	}
	public static boolean isValidTileCoordinate(int coordinate) {
		return coordinate>=0 && coordinate<64;
	}
}