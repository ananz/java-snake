package ch.nanz.snake.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import ch.nanz.snake.core.Game;
import ch.nanz.snake.core.GameUpdate;
import ch.nanz.snake.core.GameUpdate.State;
import ch.nanz.snake.core.RectangularLevel;
import ch.nanz.snake.model.Direction;

public class SnakeView extends ViewPart {

	public final static String VIEW_ID = "ch.nanz.snake.ui.view";

	private static final int MINIMAL_SLEEP_TIME = 70;
	private static final int INITIAL_SLEEP_TIME = 200;
	private static final double SLEEP_DECREASE_CHANCE = 0.5;
	private final static int BLOCK_SIZE = 25;

	private Game game;
	private SnakeCanvas content;

	@Override
	public void createPartControl(final Composite parent) {
		parent.setLayout(new FillLayout());

		content = new SnakeCanvas(parent);
		content.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == ' ') {
					getSite().getPage().hideView(SnakeView.this);
					return;
				} else if (game == null) {
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
	}

	private void start() {
		Rectangle bounds = content.getBounds();
		game = new Game(new RectangularLevel(bounds.width / BLOCK_SIZE, bounds.height / BLOCK_SIZE));
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					int wait = INITIAL_SLEEP_TIME;
					GameUpdate status = null;
					do {
						if (content.isDisposed()) {
							break;
						}
						content.showGameStatus(status = game.tick());
						Thread.sleep(wait);
						if (wait > MINIMAL_SLEEP_TIME && Math.random() < SLEEP_DECREASE_CHANCE) {
							wait -= 1;
						}
					} while (status.state != State.ENDED);
				} catch (InterruptedException e) {
				}
				game = null;
			}
		}).start();
	}

	@Override
	public void setFocus() {
		content.setFocus();
	}

}
