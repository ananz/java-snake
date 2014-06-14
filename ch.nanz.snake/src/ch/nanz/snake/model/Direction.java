package ch.nanz.snake.model;

public enum Direction {
	UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0);

	private final int deltaX, deltaY;

	private Direction(int deltaX, int deltaY) {
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}

	public Coordinate apply(Coordinate in) {
		return new Coordinate(in.x + deltaX, in.y + deltaY);
	}

	public Direction opposite() {
		if (this == UP) {
			return DOWN;
		} else if (this == DOWN) {
			return UP;
		} else if (this == RIGHT) {
			return LEFT;
		} else if (this == LEFT) {
			return RIGHT;
		}
		throw new IllegalStateException();
	}

	public Direction turnLeft() {
		return turnRight().opposite();
	}

	public Direction turnRight() {
		if (this == UP) {
			return RIGHT;
		} else if (this == RIGHT) {
			return DOWN;
		} else if (this == DOWN) {
			return LEFT;
		} else if (this == LEFT) {
			return UP;
		}
		throw new IllegalStateException();
	}
}
