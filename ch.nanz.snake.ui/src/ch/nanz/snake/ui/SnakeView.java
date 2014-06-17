package ch.nanz.snake.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;

import ch.nanz.snake.core.Game;
import ch.nanz.snake.core.GameStatus;
import ch.nanz.snake.core.GameStatus.State;
import ch.nanz.snake.core.RectangularLevel;
import ch.nanz.snake.model.Block;
import ch.nanz.snake.model.Direction;
import ch.nanz.snake.model.LengthBlock;
import ch.nanz.snake.model.SnakeBlock;
import ch.nanz.snake.model.WallBlock;

public class SnakeView extends ViewPart {

	public final static String VIEW_ID = "ch.nanz.snake.ui.view";

	private static final int MINIMAL_SLEEP_TIME = 70;
	private static final int INITIAL_SLEEP_TIME = 200;
	private final static int BLOCK_SIZE = 25;

	private Game game;
	private GameStatus status = null;
	private Composite content;

	@Override
	public void createPartControl(final Composite parent) {
		parent.setLayout(new FillLayout());

		content = new Composite(parent, SWT.NULL);
		content.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (game == null) {
					start();
				}

				if (e.keyCode == SWT.ARROW_UP) {
					game.setDirection(Direction.UP);
				} else if (e.keyCode == SWT.ARROW_DOWN) {
					game.setDirection(Direction.DOWN);
				} else if (e.keyCode == SWT.ARROW_LEFT) {
					game.setDirection(Direction.LEFT);
				} else if (e.keyCode == SWT.ARROW_RIGHT) {
					game.setDirection(Direction.RIGHT);
				}
			}
		});

		content.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				if (status == null) {
					return;
				}
				GameStatus status = SnakeView.this.status;
				int gameWidth = status.matrix.length;
				int gameHeight = status.matrix[0].length;

				Rectangle bounds = content.getBounds();
				int xSize = bounds.width / gameWidth;
				int ySize = bounds.height / gameHeight;

				for (int x = 0; x < gameWidth; ++x) {
					for (int y = 0; y < gameHeight; ++y) {
						Block block = status.matrix[x][y];
						if (block instanceof SnakeBlock) {
							e.gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
						} else if (block instanceof LengthBlock) {
							e.gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_MAGENTA));
						} else if (block instanceof WallBlock) {
							e.gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
						} else {
							e.gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
						}
						if (block != null) {
							e.gc.fillRectangle(x * xSize, y * ySize, xSize, ySize);
						}
					}
				}
				if (status.state == State.RUNNING) {
					e.gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GREEN));
					for (SnakeBlock snake : status.snakeHead) {
						e.gc.fillRectangle(snake.coordinate.x * xSize, snake.coordinate.y * ySize, xSize, ySize);
					}
					e.gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
					e.gc.fillRectangle(status.snakeHead.coordinate.x * xSize, status.snakeHead.coordinate.y * ySize, xSize, ySize);
				}
			}
		});
	}

	private void start() {
		Rectangle bounds = content.getBounds();
		game = new Game(new RectangularLevel(bounds.width / BLOCK_SIZE, bounds.height / BLOCK_SIZE));
		new Thread(new Runnable() {

			private final Runnable redrawJob = new Runnable() {
				@Override
				public void run() {
					if (!content.isDisposed()) {
						content.redraw();
					}
				}
			};

			@Override
			public void run() {
				try {
					int wait = INITIAL_SLEEP_TIME;
					do {
						if (content.isDisposed()) {
							break;
						}
						status = game.tick();
						Display.getDefault().asyncExec(redrawJob);
						Thread.sleep(wait);
						if (wait > MINIMAL_SLEEP_TIME && Math.random() < 0.5) {
							wait -= 1;
						}
					} while (status.state != State.ENDED);
				} catch (InterruptedException e) {
				}
				game = null;
				status = null;
			}
		}).start();
	}

	@Override
	public void setFocus() {
		content.setFocus();
	}

}
