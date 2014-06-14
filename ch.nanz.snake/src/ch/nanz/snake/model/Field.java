package ch.nanz.snake.model;

import java.util.Collection;

public interface Field {

	boolean contains(Coordinate coordinate);

	Block get(Coordinate coordinate);

	void put(Block block);

	void remove(Coordinate coordinate);

	Collection<Block> values();
}
