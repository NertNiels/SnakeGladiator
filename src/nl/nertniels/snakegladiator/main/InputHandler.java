package nl.nertniels.snakegladiator.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class InputHandler implements KeyListener, MouseListener, MouseMotionListener {
	
	public static boolean W;
	public static boolean A;
	public static boolean S;
	public static boolean D;
	public static boolean UP;
	public static boolean LEFT;
	public static boolean DOWN;
	public static boolean RIGHT;
	public static boolean ENTER;
	public static boolean SHIFT;
	
	@Override
	public void keyTyped(KeyEvent e) {
//		System.out.println(e.getKeyChar());
		
		if(Main.MAIN.chat != null) {
			if(Main.MAIN.chat.focus) {
				if(e.getKeyChar() == '\n') Main.MAIN.chat.send();
				else if((int)e.getKeyChar() == 8) Main.MAIN.chat.backspace();
				else Main.MAIN.chat.inputText += e.getKeyChar();
			}
		}
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == 87) W = true;
		if(e.getKeyCode() == 65) A = true;
		if(e.getKeyCode() == 83) S = true;
		if(e.getKeyCode() == 68) D = true;
		
		if(e.getKeyCode() == 38) UP = true;
		if(e.getKeyCode() == 37) LEFT = true;
		if(e.getKeyCode() == 40) DOWN = true;
		if(e.getKeyCode() == 39) RIGHT = true;
		
		if(e.getKeyCode() == 10) ENTER = true;
		if(e.getKeyCode() == 16) SHIFT = true;
		
//		System.out.println(e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == 87) W = false;
		if(e.getKeyCode() == 65) A = false;
		if(e.getKeyCode() == 83) S = false;
		if(e.getKeyCode() == 68) D = false;
		
		if(e.getKeyCode() == 38) UP = false;
		if(e.getKeyCode() == 37) LEFT = false;
		if(e.getKeyCode() == 40) DOWN = false;
		if(e.getKeyCode() == 39) RIGHT = false;
		
		if(e.getKeyCode() == 10) ENTER = false;
		if(e.getKeyCode() == 16) SHIFT = false;
		
		if(e.getKeyCode() == 122) Main.MAIN.toggleFullScreen();
		if(e.getKeyChar() == 27) System.exit(0);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			if(Main.MAIN.chat != null) {
				Main.MAIN.chat.focus = Main.MAIN.chat.input.contains(e.getX(), e.getY());
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		Main.MAIN.codeContainer.handleMouseDown(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Main.MAIN.codeContainer.handleMouseUp(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Main.MAIN.codeContainer.handleMouseDragged(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}
