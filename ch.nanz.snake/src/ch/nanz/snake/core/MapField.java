package ch.nanz.snake.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ch.nanz.snake.model.Block;
import ch.nanz.snake.model.Coordinate;
import ch.nanz.snake.model.Field;

public class MapField implements Field {

	private final Map<Coordinate, Block> field = new HashMap<>();

	@Override
	public boolean contains(Coordinate coordinate) {
		return field.containsKey(coordinate);
	}

	@Override
	public Block get(Coordinate coordinate) {
		return field.get(coordinate);
	}

	@Override
	public void put(Block block) {
		field.put(block.coordinate, block);
	}

	@Override
	public void remove(Coordinate coordinate) {
		field.remove(coordinate);
	}

	@Override
	public Collection<Block> values() {
		return field.values();
	}

}
