package nl.nertniels.snakegladiator.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import nl.nertniels.snakegladiator.game.Snake;

public class InputHandler implements KeyListener {
	
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
	}
	
}
