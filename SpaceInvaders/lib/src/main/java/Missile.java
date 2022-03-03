import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.Color;

public class Missile extends Drawable {
	protected Missile(int x, int y) {
		// Missiles are 10 h x 2 w
		super(x, y, 10, 2);
	}

	void draw(Graphics2D g2) {
		Rectangle2D missle = new Rectangle2D.Double(getX(), getY(), getWidth(), getHeight());
		g2.setColor(Color.WHITE);
		g2.fill(missle);
	}

	public boolean moveMissile(Direction d) {
		if (isVisable()) {
			move(d, 5);
		}
		return isVisable();
	}
}
