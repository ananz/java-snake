package ch.nanz.snake.core;

import ch.nanz.snake.core.GameStatus.State;
import ch.nanz.snake.model.Block;
import ch.nanz.snake.model.Coordinate;
import ch.nanz.snake.model.Direction;
import ch.nanz.snake.model.DirectionStrategy;
import ch.nanz.snake.model.Field;
import ch.nanz.snake.model.LengthBlock;
import ch.nanz.snake.model.Level;
import ch.nanz.snake.model.SnakeBlock;
import ch.nanz.snake.model.WallBlock;

public class Game {
	private final static int MAX_RANDOM_TRIES = 1000;

	private final Level level;
	private final Field field;
	private final DirectionStrategy directionStrategy;

	private SnakeBlock head;

	public Game(Level level) {
		this(level, new MapField(), new QueueDirectionStrategy());
	}

	public Game(Level level, Field field, DirectionStrategy directionStrategy) {
		this.level = level;
		this.field = field;
		this.directionStrategy = directionStrategy;
		setSnake(level.getInitialSnake());
		directionStrategy.setDirection(head.getDirection());
		putLengthBlock(head);
	}

	private Block[][] createBlockMatrix() {
		Block[][] result = level.toMatrix();
		for (Block each : field.values()) {
			result[each.coordinate.x][each.coordinate.y] = each;
		}
		return result;
	}

	private boolean isLengthIncreaser(Coordinate coordinate) {
		return (field.get(coordinate) instanceof LengthBlock);
	}

	private boolean isUnreachable(Coordinate coordinate) {
		return (!level.isReachable(coordinate)) || (field.get(coordinate) instanceof WallBlock);
	}

	private void putLengthBlock(SnakeBlock head) {
		for (int i = 0; i < MAX_RANDOM_TRIES; ++i) {
			Coordinate randomCoordinate = level.findRandomCoordinate();
			if (!isUnreachable(randomCoordinate) && !head.collidesWith(randomCoordinate)) {
				field.put(new LengthBlock(randomCoordinate));
				return;
			}
		}
		throw new IllegalStateException("No free block found!");
	}

	private void setSnake(SnakeBlock newSnake) {
		if (head != null) {
			for (SnakeBlock each : head) {
				field.remove(each.coordinate);
			}
		}
		if (newSnake != null) {
			for (SnakeBlock each : newSnake) {
				field.put(each);
			}
		}
		head = newSnake;
	}

	public synchronized void setDirection(Direction direction) {
		assert (direction != null);
		directionStrategy.setDirection(direction);
	}

	public synchronized GameStatus tick() {
		level.tick();
		SnakeBlock direction = head.changeDirection(directionStrategy.getDirection());
		SnakeBlock moved = direction.move();
		if (isLengthIncreaser(moved.coordinate)) {
			moved = direction.enlarge();
			putLengthBlock(moved);
		}
		if (moved.collidesWithTail() || isUnreachable(moved.coordinate)) {
			return new GameStatus(State.ENDED, createBlockMatrix(), moved);
		}
		setSnake(moved);
		return new GameStatus(State.RUNNING, createBlockMatrix(), moved);
	}
}
