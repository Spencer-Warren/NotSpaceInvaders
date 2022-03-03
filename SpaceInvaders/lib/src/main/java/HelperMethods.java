import javax.sound.sampled.Clip;

public class HelperMethods {

	// Helper method to restart or start sound
	public static void restartSound(Clip c) {
		if (c.isRunning()) {
			c.stop();
		}
		c.setFramePosition(0);
		c.start();
	}

	// Given ship and Missile: are they intersecting?
	public static boolean hitCheck(Drawable a, Drawable b) {
		if (a == null || b == null) {
			return false;
		}
		return a.getX() < b.getX() + b.getWidth() && a.getX() + a.getWidth() > b.getX()
				&& a.getY() < b.getY() + b.getHeight() && a.getY() + a.getHeight() > b.getY();
	}
}
