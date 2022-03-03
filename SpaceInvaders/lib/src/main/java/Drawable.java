import java.awt.Graphics2D;
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;

public abstract class Drawable {
	private int x;
	private int y;
	private int width;
	private int height;

	protected Drawable(int x, int y, int height, int width) {
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
	}

	protected int getX() {
		return x;
	}

	protected int getY() {
		return y;
	}

	protected void setX(int x) {
		this.x = x;
	}

	protected void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void move(Direction d, int rate) {
		switch (d) {
		case UP:
			y -= rate;
			break;
		case DOWN:
			y += rate;
			break;
		case LEFT:
			x -= rate;
			break;
		case RIGHT:
			x += rate;
			break;
		}
	}

	public boolean isVisable() {
		return getY() > 0 && getY() < SpaceInvaders.HEIGHT && getX() > 0 && getX() < SpaceInvaders.WIDTH;
	}

	public static Image getImage(Object o, String filename) {
		URL url = o.getClass().getResource(filename);
		ImageIcon icon = new ImageIcon(url);
		return icon.getImage();
	}

	public static Clip getSound(Object o, String filename) {
		Clip clip = null;
		try {
			InputStream in = o.getClass().getResourceAsStream(filename);
			InputStream buf = new BufferedInputStream(in);
			AudioInputStream stream = AudioSystem.getAudioInputStream(buf);
			clip = AudioSystem.getClip();
			clip.open(stream);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
		return clip;
	}
	abstract void draw(Graphics2D g2);
}
