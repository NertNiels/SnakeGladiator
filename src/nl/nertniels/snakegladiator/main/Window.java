package nl.nertniels.snakegladiator.main;

import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

public class Window extends JFrame {
	
	private boolean fullscreen = false;

	public Window(Main main) {
		super();
		setTitle(Settings.TITLE);
		setSize(Settings.WINDOW_WIDTH, Settings.WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		
		setLocationRelativeTo(null);
		setVisible(true);
		
		add(main);
		pack();
		
	}
	
	public void toggleFullScreen() {
		fullscreen = !fullscreen;
		
		if(fullscreen) {
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(this);
		}
		else {
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(null);
			setVisible(true);
		}
	}
	
	
	
}
