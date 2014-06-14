package ch.nanz.snake.ui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import ch.nanz.snake.core.Game;
import ch.nanz.snake.core.GameStatus;
import ch.nanz.snake.core.GameStatus.State;
import ch.nanz.snake.core.RectangularLevel;
import ch.nanz.snake.model.Direction;
import ch.nanz.snake.model.LengthBlock;
import ch.nanz.snake.model.SnakeBlock;
import ch.nanz.snake.model.WallBlock;

public class SnakeFrame extends JFrame {

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
	private final int width, height;

	private Game game = null;

	public SnakeFrame(int width, int height) {
		this.width = width;
		this.height = height;

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
				if (game == null) {
					start();
				} else if (e.getKeyCode() == KeyEvent.VK_UP) {
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
		// start();
	}

	private void start() {
		game = new Game(new RectangularLevel(width, height));
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					int wait = 200;
					GameStatus status = null;
					do {
						fieldUpdated(status = game.tick());
						Thread.sleep(Math.max(80, Math.random() < 0.5 ? wait-- : wait));
					} while (status.state != State.ENDED);
				} catch (InterruptedException e) {
				}
				game = null;
			}
		}).start();
	}

	private void fieldUpdated(GameStatus status) {
		for (int x = 0; x < status.matrix.length; ++x) {
			for (int y = 0; y < status.matrix[0].length; ++y) {
				JLabel label = labels[x][y];
				label.setForeground(Color.BLACK);
				if (status.matrix[x][y] instanceof SnakeBlock) {
					label.setText("O");
				} else if (status.matrix[x][y] instanceof LengthBlock) {
					label.setForeground(Color.MAGENTA);
					label.setText("o");
				} else if (status.matrix[x][y] instanceof WallBlock) {
					label.setForeground(Color.RED);
					label.setText("X");
				} else {
					label.setText(".");
				}
			}
		}
		if (status.state == State.RUNNING) {
			for (SnakeBlock snake : status.snakeHead) {
				JLabel label = labels[snake.coordinate.x][snake.coordinate.y];
				label.setForeground(Color.GREEN);
			}
		}
	}
}
