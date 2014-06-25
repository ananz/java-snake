package ch.nanz.snake.core;

import ch.nanz.snake.model.Direction;
import ch.nanz.snake.model.Level;

public class GameFacade {

	public static interface GameUpdateListener {
		void update(GameUpdate update);
	}

	public static enum State {
		RUNNING, PAUSED, STOPPED;
	}

	private class Runner implements Runnable {
		private int targetedSleepTime = initialSleepTime;

		@Override
		public void run() {
			state = State.RUNNING;
			try {
				GameUpdate status = null;
				do {
					long start = System.currentTimeMillis();
					listener.update(status = game.tick());
					long end = System.currentTimeMillis();
					int effectiveSleepTime = targetedSleepTime - (int) (end - start);
					if (effectiveSleepTime > 0) {
						Thread.sleep(effectiveSleepTime);
					}
					if (targetedSleepTime > minimalSleepTime && Math.random() < sleepDecreaseChance) {
						--targetedSleepTime;
					}
				} while (status.alive);
			} catch (InterruptedException e) {
				return;
			}
			state = State.STOPPED;
		}
	}

	private final GameUpdateListener listener;
	private final int initialSleepTime;
	private final int minimalSleepTime;
	private final double sleepDecreaseChance;

	private Game game = null;
	private Thread thread = null;
	private Runner runner = null;
	private State state = State.STOPPED;

	public GameFacade(GameUpdateListener listener) {
		this(listener, 200, 70, 0.5);
	}

	public GameFacade(GameUpdateListener listener, int initialSleepTime, int minimalSleepTime, double sleepDecreaseChance) {
		this.listener = listener;
		this.initialSleepTime = initialSleepTime;
		this.minimalSleepTime = minimalSleepTime;
		this.sleepDecreaseChance = sleepDecreaseChance;
	}

	public State getState() {
		return state;
	}

	public void setDirection(Direction direction) {
		if (game != null && state == State.RUNNING) {
			game.setDirection(direction);
		}
	}

	public void start(Level level) {
		start(new Game(level), new Runner());
	}

	public void resume() {
		if (game != null && runner != null) {
			pause();
			start(game, this.runner);
		}
	}

	private void start(Game game, Runner runner) {
		stop();
		this.game = game;
		this.thread = new Thread(this.runner = runner);
		thread.setName("Snake Thread");
		thread.setDaemon(true);
		thread.start();
	}

	public void stop() {
		pause();
		game = null;
		runner = null;
		state = State.STOPPED;
	}

	public void pause() {
		if (thread != null) {
			thread.interrupt();
			thread = null;
		}
		state = State.PAUSED;
	}
}
