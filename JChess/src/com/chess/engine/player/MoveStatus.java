package com.chess.engine.player;

public enum MoveStatus {
	DONE {
		@Override
		boolean isDone() {
			return true;
		}
	},
	ILLEGAL_MOVE {
		@Override
		boolean isDone() {
			return false;
		}
	}, LEAVES_PLAYER_IN_CHECK {
		@Override
		boolean isDone() {
			// TODO Auto-generated method stub
			return false;
		}
	};

	abstract boolean isDone();
}