package ch.nanz.snake.core;

import ch.nanz.snake.model.Direction;
import ch.nanz.snake.model.DirectionStrategy;


public class LeftRightAttributeDirectionStrategy implements DirectionStrategy {

	private Direction direction;

	@Override
	public Direction getDirection() {
		return direction;
	}

	@Override
	public void setDirection(Direction direction) {
		if (getDirection() == null) {
			this.direction = direction;
		} else if (direction == Direction.LEFT) {
			this.direction = getDirection().turnLeft();
		} else if (direction == Direction.RIGHT) {
			this.direction = getDirection().turnRight();
		}
	}

}
