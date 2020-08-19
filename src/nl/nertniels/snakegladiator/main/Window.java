package nl.nertniels.snakegladiator.main;

import javax.swing.JFrame;

public class Window extends JFrame {

	public Window(Main main) {
		super();
		setTitle(Settings.TITLE);
		setSize(Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		add(main);
		pack();
		
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
}
