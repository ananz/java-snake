package ch.nanz.snake.model;

public abstract class Block {
	public final Coordinate coordinate;

	public Block(Coordinate coordinate) {
		this.coordinate = coordinate;
	}
}
