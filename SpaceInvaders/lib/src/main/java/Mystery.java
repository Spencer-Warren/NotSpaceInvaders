import java.util.Random;

import javax.sound.sampled.Clip;

public class Mystery extends Invader{
	private boolean left;
	private Clip mysterySound;
	protected Mystery() {
		super(init(), 30, point(), "img_mystery.gif");
		left = getX() == 440;
		mysterySound = getSound(this, "aud_mystery.wav");
		HelperMethods.restartSound(mysterySound);
	}
	private static int point() {
		int[] scores = {50, 100, 150, 300};
		return scores[new Random().nextInt(scores.length)];
	}
	
	private static int init() {
		return new Random().nextBoolean() ? 10 : 440;
	}
	
	public void move() {
		if(!mysterySound.isRunning() && isVisable()) {
			HelperMethods.restartSound(mysterySound);
		}
		super.move(left ? Direction.LEFT : Direction.RIGHT, 1);
	}
	public void stopSound() {
		mysterySound.stop();
		}
	
	public void hit() {
		mysterySound.stop();
		super.hit();
	}
}
