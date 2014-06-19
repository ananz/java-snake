package ch.nanz.snake.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import ch.nanz.snake.core.GameUpdate;
import ch.nanz.snake.core.GameUpdate.State;
import ch.nanz.snake.model.Block;
import ch.nanz.snake.model.LengthBlock;
import ch.nanz.snake.model.SnakeBlock;
import ch.nanz.snake.model.WallBlock;

public class SnakeCanvas extends Canvas {

	private final Object lock = new Object();

	private final PaintListener paintListener = new PaintListener() {
		@Override
		public void paintControl(PaintEvent e) {
			synchronized (lock) {
				if (status == null) {
					return;
				}
				int gameWidth = status.matrix.length;
				int gameHeight = status.matrix[0].length;

				Rectangle bounds = getBounds();
				int xSize = bounds.width / gameWidth;
				int ySize = bounds.height / gameHeight;

				int shiftX = (bounds.width - gameWidth * xSize) / 2;
				int shiftY = (bounds.height - gameHeight * ySize) / 2;

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
							e.gc.fillRectangle(x * xSize + shiftX, y * ySize + shiftY, xSize, ySize);
						}
					}
				}
				if (status.state == State.RUNNING) {
					e.gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GREEN));
					for (SnakeBlock snake : status.snakeHead) {
						e.gc.fillRectangle(snake.coordinate.x * xSize + shiftX, snake.coordinate.y * ySize + shiftY, xSize, ySize);
					}
					e.gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
					e.gc.fillRectangle(status.snakeHead.coordinate.x * xSize + shiftX, status.snakeHead.coordinate.y * ySize + shiftY, xSize, ySize);
				}
			}
		}
	};

	private final Runnable redrawJob = new Runnable() {
		@Override
		public void run() {
			if (!isDisposed()) {
				SnakeCanvas.super.redraw();
			}
		}
	};

	private GameUpdate status = null;

	public SnakeCanvas(Composite parent) {
		super(parent, SWT.NONE);

		addPaintListener(paintListener);
	}

	public void showGameStatus(GameUpdate status) {
		synchronized (lock) {
			this.status = status;
		}
		redraw();
	}

	@Override
	public void redraw() {
		if (Display.getCurrent() != null) {
			redrawJob.run();
		} else if (!isDisposed()) {
			Display.getDefault().asyncExec(redrawJob);
		}
	}
}
