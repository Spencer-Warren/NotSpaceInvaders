import java.awt.Graphics2D;
import java.awt.Image;

public abstract class Invader extends Ship {
	private int points;
	private Image A;
	private Image B;
	private boolean alternate;
	
	protected Invader(int x, int y, int points, String a, String b) {
		super(x, y, 30, 24, "img_invaderhit.gif");
		this.points = points;
		A = getImage(this, a);
		B = getImage(this, b);
		alternate = true;
	}
	
	protected Invader(int x, int y, int points, String image) {
		this(x, y, points, image, image);
	}

	@Override
	public void move(Direction d, int rate) {
		super.move(d, 5);
		// Flip images per move
		alternate = !alternate;
	}

	@Override
	void draw(Graphics2D g2) {
		// Draw image based on state
		super.draw(g2, alternate ? A : B);
	}

	public int getPoints() {
		return points;
	}
}
