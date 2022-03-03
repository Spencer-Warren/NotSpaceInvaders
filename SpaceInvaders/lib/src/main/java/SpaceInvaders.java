
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

@SuppressWarnings("serial")
public class SpaceInvaders extends JFrame {
	public static final int WIDTH = 500;
	public static final int HEIGHT = 450;
	private Panel panel;
	private JMenuItem pause;
	private JMenuItem resume;

	public SpaceInvaders() {
		super("Space Invaders");

		// Helper method for readability and organization
		menuInit();
		panel = new Panel();
		add(panel);

		// Setting the color does not work without get content pane
		// why? i don't know
		getContentPane().setBackground(Color.BLACK);
		setSize(WIDTH, HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
	}

	private void menuInit() {
		var bar = new JMenuBar();
		setJMenuBar(bar);

		var game = new JMenu("Game");
		bar.add(game);

		JMenuItem newGame = game.add("New Game");

		game.addSeparator();

		pause = game.add("Pause");
		pause.setEnabled(false);

		resume = game.add("Resume");
		resume.setEnabled(false);

		resume.addActionListener(e -> pause(false));

		pause.addActionListener(e -> pause(true));

		newGame.addActionListener(e -> {
			if (confirmDialog("Start a new game?")) {
				panel.start();
				pause(false);
			}
		});

		game.addSeparator();

		JMenuItem quit = game.add("Quit");
		quit.addActionListener(e -> close());

		var help = new JMenu("Help");
		bar.add(help);

		JMenuItem about = help.add("About...");
		about.addActionListener(e -> {
			pause(true);
			JOptionPane.showMessageDialog(SpaceInvaders.this,
					new JLabel("<html><hr><i>Space Invaders</i><br>Version 1.0<br>by Spencer A. Warren</hr></html>"),
					"About", JOptionPane.INFORMATION_MESSAGE);
			pause(false);
		});
	}

	private boolean confirmDialog(String message) {
		pause(true);
		int result = JOptionPane.showConfirmDialog(SpaceInvaders.this, message, "Confirm", JOptionPane.YES_NO_OPTION);
		pause(false);
		return result == JOptionPane.YES_OPTION;
	}

	private void close() {
		if (confirmDialog("Dare to quit?")) {
			dispose();
			pause(true);
		}
	}

	private void pause(boolean p) {
		panel.pause(p);
		resume.setEnabled(p);
		pause.setEnabled(!p);
	}

	public static void main(String[] args) {
		JFrame f = new SpaceInvaders();
		f.setVisible(true);
	}

}
