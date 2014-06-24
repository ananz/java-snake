package ch.nanz.snake.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import ch.nanz.snake.core.GameFacade;
import ch.nanz.snake.core.RectangularLevel;
import ch.nanz.snake.model.Direction;

public class SnakeView extends ViewPart {

	public final static String VIEW_ID = "ch.nanz.snake.ui.view";
	private final static int BLOCK_SIZE = 25;

	private GameFacade game;
	private SnakeCanvas content;

	@Override
	public void createPartControl(final Composite parent) {
		parent.setLayout(new FillLayout());

		content = new SnakeCanvas(parent, SWT.NONE);
		game = new GameFacade(content);

		content.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == ' ') {
					// BOSS KEY ...
					getSite().getPage().hideView(SnakeView.this);
					return;
				}

				if (GameFacade.State.STOPPED == game.getState()) {
					Rectangle bounds = content.getBounds();
					game.start(new RectangularLevel(bounds.width / BLOCK_SIZE, bounds.height / BLOCK_SIZE));
				}

				if (game.getState() == GameFacade.State.RUNNING) {
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
			}
		});
	}

	@Override
	public void setFocus() {
		content.setFocus();
	}

}
