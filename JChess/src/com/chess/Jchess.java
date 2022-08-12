package com.chess;

import com.chess.engine.board.Board;

public class Jchess {

	public static void main(String[] args) {
		// Board gameBoard=new Board(); you can't do this constructor is private of
		// board
		Board gameBoard = Board.createStandardBoard();
		System.out.println(gameBoard);
//		Table table = new Table();// c
	}

}