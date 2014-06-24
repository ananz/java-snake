package ch.nanz.snake.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import ch.nanz.snake.core.GameFacade.GameUpdateListener;
import ch.nanz.snake.core.GameFacade.State;
import ch.nanz.snake.core.GameUpdate;
import ch.nanz.snake.model.Block;
import ch.nanz.snake.model.LengthBlock;
import ch.nanz.snake.model.SnakeBlock;
import ch.nanz.snake.model.WallBlock;

public class SnakeCanvas extends Canvas implements GameUpdateListener {

	private final PaintListener paintListener = new PaintListener() {
		@Override
		public void paintControl(PaintEvent e) {
			if (update == null) {
				return;
			}
			int gameWidth = update.matrix.length;
			int gameHeight = update.matrix[0].length;

			Rectangle bounds = getBounds();
			int xSize = bounds.width / gameWidth;
			int ySize = bounds.height / gameHeight;

			int shiftX = (bounds.width - gameWidth * xSize) / 2;
			int shiftY = (bounds.height - gameHeight * ySize) / 2;

			for (int x = 0; x < gameWidth; ++x) {
				for (int y = 0; y < gameHeight; ++y) {
					Block block = update.matrix[x][y];
					if (block instanceof SnakeBlock) {
						if (update.alive) {
							e.gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_GREEN));
							if (block == update.snakeHead) {
								e.gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN));
							}
						} else {
							e.gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
						}
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

	private GameUpdate update = null;

	public SnakeCanvas(Composite parent, int style) {
		super(parent, style);
		addPaintListener(paintListener);
	}

	public void update(GameUpdate update, State state) {
		if (!isDisposed()) {
			this.update = update;
			Display.getDefault().syncExec(redrawJob);
		}
	}
}
