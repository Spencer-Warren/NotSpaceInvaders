import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.sound.sampled.Clip;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Panel extends JPanel {
	private boolean left;
	private boolean right;
	private boolean moveFrame;
	private boolean invadersRight;
	private boolean gameOver;
	private int score;
	private int tick;
	private int invaderMoves;
	private int maxHorizontalMoves;
	private int rate;
	private Random rand;
	private Base base;
	private List<List<Invader>> invaders;
	private Mystery mystery;
	private Missile baseMissile;
	private List<Missile> missiles;
	private Clip baseFire;
	private Timer timer;

	public Panel() {
		// Start capturing key events
		startKey();
		// Create timer
		timer = new Timer(10, e -> actionPreformed());
		rand = new Random();
		missiles = new ArrayList<>();
		invaders = new ArrayList<>();
		setBackground(Color.BLACK);
		setFocusable(true);
	}

	public void pause(boolean p) {
		if (p) {
			timer.stop();
		} else {
			timer.start();
		}
	}

	public void start() {
		// Create Base
		base = new Base(getWidth() / 2, getHeight() - 20);
		baseFire = base.getFire();
		// Initialize all the things
		missiles = new ArrayList<>();
		invaders = new ArrayList<>(5);
		tick = 0;
		score = 0;
		invaderMoves = 0;
		invadersRight = false;
		rate = 40;
		maxHorizontalMoves = 15;
		gameOver = false;
		
		newWave();

		// Unpause to start game
		timer.start();
	}

	private void newWave() {
		invaderMoves = 0;
		mystery = null;
		invadersRight = false;
		rate = 40;
		for (int i = 0; i < 5; i++) {
			List<Invader> row = new ArrayList<>(10);
			for (int j = 0; j < 10; j++) {
				int indent = (getWidth() / 2 - 190) + (j * 35);
				int height = 80 + i * 25;
				Invader temp = null;
				switch (i) {
				case 0:
					temp = new InvaderTop(indent, height);
					break;
				case 1:
				case 2:
					temp = new InvaderMiddle(indent, height);
					break;
				case 3:
				case 4:
					temp = new InvaderBottom(indent, height);
					break;
				}
				row.add(temp);
			}
			invaders.add(row);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		if (base != null) {
			base.draw(g2);
		}
		if (baseMissile != null) {
			baseMissile.draw(g2);
		}
		if (mystery != null) {
			mystery.draw(g2);
			if (mystery.isDestroy()) {
				mystery = null;
			}
		}
		if (!missiles.isEmpty()) {
			for (Missile m : missiles) {
				m.draw(g2);
			}
		}
		paintInvaders(g2);
		paintStrings(g2);
	}

	private void paintStrings(Graphics2D g2) {
		String str = "score: " + score;
		var font1 = new Font("Sans Serif", Font.PLAIN, 15);
		FontMetrics fm = g2.getFontMetrics(font1);
		var width1 = fm.stringWidth(str);
		g2.setFont(font1);
		g2.setColor(Color.GREEN);
		g2.drawString(str, getWidth() - width1 - 10, 20);
		if (gameOver) {
			var font2 = new Font("Comic Sans", Font.BOLD, 50);
			FontMetrics fm2 = g2.getFontMetrics(font2);
			var width2 = fm2.stringWidth("Game Over");
			g2.setFont(font2);
			g2.setColor(Color.GREEN);
			g2.drawString("Game Over", getWidth() / 2 - width2 / 2, getHeight() / 2 + 50);
		}
	}

	private void paintInvaders(Graphics2D g2) {
		if (invaders.isEmpty() && mystery == null && missiles.isEmpty()) {
			newWave();
		}
		for (int i = 0; i < invaders.size(); i++) {
			List<Invader> row = invaders.get(i);
			for (int j = 0; j < row.size(); j++) {
				Invader invader = row.get(j);
				invader.draw(g2);
				if (invader.isDestroy()) {
					row.remove(invader);
				}
			}
			if (row.isEmpty()) {
				invaders.remove(row);
			}
		}
	}

	private void startKey() {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					left = true;
					break;
				case KeyEvent.VK_RIGHT:
					right = true;
					break;
				case KeyEvent.VK_SPACE:
					// On space create missile if there isn't one with sound
					if (baseMissile == null) {
						// Manual adjustment to front middle of base
						baseMissile = new Missile(base.getX() + 12, base.getY() - 11);
						HelperMethods.restartSound(baseFire);
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					left = false;
					break;
				case KeyEvent.VK_RIGHT:
					right = false;
					break;
				}
			}
		});
	}

	// Helper to trigger events for each tick sent by timer
	// Each trigger of this method is a "frame"
	private void actionPreformed() {
		if (left)
			base.move(Direction.LEFT);
		if (right)
			base.move(Direction.RIGHT);
		missileFrame();
		tick++;
		moveFrame = tick % rate == 0;
		invadersFrame();
		// Check if we should change direction and rate
		rateChange();
		if (tick % 1500 == 0) {
			mystery = new Mystery();
		}
		repaint();
	}

	private void rateChange() {
		if (moveFrame && invaderMoves++ == maxHorizontalMoves) {
			invaderMoves = 0;
			// Decrease rate by 80
			rate = (int) (rate * 0.8);
			// Make sure we don't get / 0 error
			rate = rate == 0 ? 1 : rate;
			invadersRight = !invadersRight;
			if (maxHorizontalMoves == 15) {
				maxHorizontalMoves = 26;
			}
		}
	}

	// Choose a random invader to shoot
	private void invadersShoot() {
		if (!invaders.isEmpty() && rand.nextInt(20) < 8 && missiles.size() < 3) {
			List<Invader> row = invaders.get(invaders.size() - 1);
			Invader invader = row.get(rand.nextInt(row.size()));
			missiles.add(new Missile(invader.getX() + 16, invader.getY() + 5));
		}
	}

	// Move Invaders and check if base missile hit
	private void invadersFrame() {
		if (moveFrame) {
			invadersShoot();
		}
		// Move and hit check mystery
		if (mystery != null && mystery.isVisable()) {
			if (baseMissile != null && HelperMethods.hitCheck(mystery, baseMissile)) {
				hitShip(mystery);
				baseMissile = null;
			}
			if (tick % 10 == 0) {
				mystery.move();
			}
		}
		else {
			mystery = null;
		}
		// move and hitcheck all invaders
		for (List<Invader> row : invaders) {
			for (Invader invader : row) {
				if (moveFrame) {
					moveInvader(invader);
				}
				// on hit remove invader and missile and play destroy sound
				if (baseMissile != null && HelperMethods.hitCheck(invader, baseMissile)) {

					hitShip(invader);
					baseMissile = null;
				}
				if (invader.getY() + invader.getHeight() > base.getY() - 20) {
					invader.hit();
					base.hit();
					lose();
				}
			}
		}
	}

	// Make hit, trigger displaying the hit image, add points and clear base missile
	private void hitShip(Invader i) {
		i.hit();
		score += i.getPoints();
	}

	// Execute moving for all missiles
	// and hit check for base
	private void missileFrame() {
		// Move base missile or remove
		if (baseMissile != null && !baseMissile.moveMissile(Direction.UP)) {
			baseMissile = null;
		}
		// Move and Check if invader missiles hit the base
		for (int i = 0; i < missiles.size(); i++) {
			Missile m = missiles.get(i);
			// Move invader missiles every other pulse
			if (tick % 2 == 0) {
				m.moveMissile(Direction.DOWN);
				if (!m.isVisable()) {
					missiles.remove(m);
				}
			}
			// Lose if base is hit
			if (HelperMethods.hitCheck(base, m)) {
				base.hit();
				lose();
			}
		}
	}

	// Game Over
	private void lose() {
		pause(true);
		gameOver = true;
	}

	// Move invader
	private void moveInvader(Invader invader) {
		if (invaderMoves == maxHorizontalMoves) {
			invader.move(Direction.DOWN, 12);
		} else {
			invader.move(invadersRight ? Direction.LEFT : Direction.RIGHT, 5);
		}
	}
}
