import java.awt.Graphics2D;
import java.awt.Image;

import javax.sound.sampled.Clip;

public abstract class Ship extends Drawable {
	private boolean isHit;
	private boolean destroy;
	private Clip hitSound;
	private Image hitImage;
	private int hitImageTicks;
	private int hitImageMax;

	protected Ship(int x, int y, int width, int height, String hit) {
		super(x, y, width, height);
		hitSound = getSound(this, "aud_hit.wav");
		hitImage = getImage(this, hit);
		hitImageMax = 30;
		hitImageTicks = 0;
		isHit = false;
	}

	public void draw(Graphics2D g2, Image shipImage) {
		if (!isHit) {
			g2.drawImage(shipImage, getX(), getY(), null);
		} else {
			g2.drawImage(hitImage, getX(), getY(), null);
			destroy = hitImageTicks++ == hitImageMax;
		}
	}

	public void hit() {
		HelperMethods.restartSound(hitSound);
		destroy = true;
		isHit = true;
	}
	
	public boolean isDestroy() {
		return destroy;
	}

	public Clip getHitSound() {
		return hitSound;
	}

	public Image getHitImage() {
		return hitImage;
	}

}
