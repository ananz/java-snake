package ch.nanz.snake.ui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import ch.nanz.snake.core.GameFacade;
import ch.nanz.snake.core.GameFacade.GameUpdateListener;
import ch.nanz.snake.core.GameFacade.State;
import ch.nanz.snake.core.GameUpdate;
import ch.nanz.snake.core.RectangularLevel;
import ch.nanz.snake.model.Direction;
import ch.nanz.snake.model.LengthBlock;
import ch.nanz.snake.model.SnakeBlock;
import ch.nanz.snake.model.WallBlock;

public class SnakeFrame extends JFrame implements GameUpdateListener {

	private static final long serialVersionUID = 1L;

	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				int width = Integer.parseInt(args[0]);
				int height = Integer.parseInt(args[1]);
				SnakeFrame frame = new SnakeFrame(width, height);
				frame.setVisible(true);
			}
		});
	}

	private final JLabel[][] labels;

	private final GameFacade game = new GameFacade(this);

	public SnakeFrame(final int width, final int height) {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(20 * width, 20 * height);
		setLayout(new GridLayout(height, width));

		labels = new JLabel[width][height];
		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				JLabel label = new JLabel();
				add(label);
				labels[x][y] = label;
				label.setText(".");
			}
		}

		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (game.getState() != State.RUNNING) {
					game.start(new RectangularLevel(width, height));
				}

				if (e.getKeyCode() == KeyEvent.VK_UP) {
					game.setDirection(Direction.UP);
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					game.setDirection(Direction.DOWN);
				} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					game.setDirection(Direction.LEFT);
				} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					game.setDirection(Direction.RIGHT);
				}
			}
		});
	}

	public void update(GameUpdate update, State state) {
		for (int x = 0; x < update.matrix.length; ++x) {
			for (int y = 0; y < update.matrix[0].length; ++y) {
				JLabel label = labels[x][y];
				if (update.matrix[x][y] instanceof SnakeBlock) {
					label.setForeground(update.alive ? Color.GREEN : Color.BLACK);
					label.setText("O");
				} else if (update.matrix[x][y] instanceof LengthBlock) {
					label.setForeground(Color.MAGENTA);
					label.setText("o");
				} else if (update.matrix[x][y] instanceof WallBlock) {
					label.setForeground(Color.RED);
					label.setText("X");
				} else {
					label.setForeground(Color.WHITE);
					label.setText(".");
				}
			}
		}
	}
}
