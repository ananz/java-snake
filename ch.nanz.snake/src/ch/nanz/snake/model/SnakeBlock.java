package ch.nanz.snake.model;

import java.util.Iterator;

public class SnakeBlock extends Block implements Iterable<SnakeBlock> {
	private final Direction direction;
	private final SnakeBlock next;

	public SnakeBlock(SnakeBlock next, Coordinate coordinate, Direction direction) {
		super(coordinate);
		this.next = next;
		this.direction = direction;
	}

	public SnakeBlock changeDirection(Direction newDirection) {
		if (direction == newDirection) {
			return this;
		} else {
			return new SnakeBlock(next, coordinate, newDirection);
		}
	}

	public boolean collidesWith(Coordinate coordinate) {
		for (SnakeBlock each : this) {
			if (coordinate.equals(each.coordinate)) {
				return true;
			}
		}
		return false;
	}

	public boolean collidesWithTail() {
		if (!isEnd()) {
			return next.collidesWith(coordinate);
		}
		return false;
	}

	public SnakeBlock move() {
		return modify(direction, false);
	}

	public SnakeBlock enlarge() {
		return modify(direction, true);
	}

	private SnakeBlock modify(Direction newDirection, boolean appendTail) {
		if (isEnd()) {
			SnakeBlock tail = appendTail ? new SnakeBlock(null, coordinate, direction) : null;
			return new SnakeBlock(tail, direction.apply(coordinate), newDirection);
		} else {
			return new SnakeBlock(next.modify(direction, appendTail), direction.apply(coordinate), newDirection);
		}
	}

	public Direction getDirection() {
		return direction;
	}

	private boolean isEnd() {
		return next == null;
	}

	public int length() {
		return isEnd() ? 1 : 1 + next.length();
	}

	@Override
	public Iterator<SnakeBlock> iterator() {
		return new Iterator<SnakeBlock>() {
			private SnakeBlock pointer = new SnakeBlock(SnakeBlock.this, null, null);

			@Override
			public boolean hasNext() {
				return !pointer.isEnd();
			}

			@Override
			public SnakeBlock next() {
				SnakeBlock next = pointer.next;
				pointer = next;
				return next;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

}
