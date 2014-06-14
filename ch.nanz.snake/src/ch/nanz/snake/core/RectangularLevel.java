package ch.nanz.snake.core;

import java.util.Random;

import ch.nanz.snake.model.Block;
import ch.nanz.snake.model.Coordinate;
import ch.nanz.snake.model.Direction;
import ch.nanz.snake.model.Level;
import ch.nanz.snake.model.SnakeBlock;

public class RectangularLevel implements Level {

	private final int width, height;

	public RectangularLevel(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@Override
	public Coordinate findRandomCoordinate() {
		Random random = new Random();
		return new Coordinate(random.nextInt(width), random.nextInt(height));
	}

	@Override
	public SnakeBlock getInitialSnake() {
		return new SnakeBlock(null, new Coordinate(width / 2, height / 2), Direction.RIGHT);
	}

	@Override
	public boolean isReachable(Coordinate coordinate) {
		return coordinate.x >= 0 && coordinate.x < width && coordinate.y >= 0 && coordinate.y < height;
	}

	@Override
	public Block[][] toMatrix() {
		return new Block[width][height];
	}

	@Override
	public void tick() {
	}

}
