package ch.nanz.snake.core;

import java.util.ArrayDeque;
import java.util.Deque;

import ch.nanz.snake.model.Direction;
import ch.nanz.snake.model.DirectionStrategy;

public class QueueDirectionStrategy implements DirectionStrategy {

	private final Deque<Direction> queue = new ArrayDeque<>();
	private Direction last = null;

	@Override
	public void setDirection(Direction direction) {
		queue.addLast(last = direction);
	}

	@Override
	public Direction getDirection() {
		if (queue.size() == 0) {
			assert (last != null);
			return last;
		}
		return queue.removeFirst();
	}

}
