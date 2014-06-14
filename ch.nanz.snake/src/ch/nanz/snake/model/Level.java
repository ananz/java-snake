package ch.nanz.snake.model;

public interface Level {

	SnakeBlock getInitialSnake();

	Block[][] toMatrix();

	boolean isReachable(Coordinate coordinate);

	Coordinate findRandomCoordinate();

	void tick();
}
