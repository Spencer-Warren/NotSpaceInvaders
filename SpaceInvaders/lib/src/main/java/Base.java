import java.awt.Graphics2D;
import java.awt.Image;
import javax.sound.sampled.Clip;

public class Base extends Ship {
	private Image baseImage;
	private Clip fire;

	protected Base(int x, int y) {
		super(x, y, 22, 26, "img_basehit.gif");
		baseImage = getImage(this, "img_base.gif");
		fire = getSound(this,"aud_basefire.wav");
	}

	public void move(Direction d) {
		int x = getX();
		switch (d) {
			case LEFT: if (x > 0) { x -= 5; } break;
			case RIGHT: if (x < SpaceInvaders.WIDTH - 45) { x += 5; } break;
			default: break;
		}
		setX(x);
	}

	public Clip getFire() {
		return fire;
	}
	
	public void draw(Graphics2D g2) {
		super.draw(g2, baseImage);
	}
}
