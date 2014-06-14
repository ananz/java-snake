package ch.nanz.snake.core;

import ch.nanz.snake.model.Block;
import ch.nanz.snake.model.SnakeBlock;


public class GameStatus {

	public static enum State {
		ENDED, RUNNING;
	}

	public final Block[][] matrix;
	public final SnakeBlock snakeHead;
	public final State state;

	public GameStatus(State state, Block[][] matrix, SnakeBlock snakeHead) {
		this.state = state;
		this.matrix = matrix;
		this.snakeHead = snakeHead;
	}

}
