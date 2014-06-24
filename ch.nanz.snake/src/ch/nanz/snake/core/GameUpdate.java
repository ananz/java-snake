package ch.nanz.snake.core;

import ch.nanz.snake.model.Block;
import ch.nanz.snake.model.SnakeBlock;

public class GameUpdate {

	public final Block[][] matrix;
	public final SnakeBlock snakeHead;
	public final boolean alive;

	public GameUpdate(boolean alive, Block[][] matrix, SnakeBlock snakeHead) {
		this.alive = alive;
		this.matrix = matrix;
		this.snakeHead = snakeHead;
	}

}
