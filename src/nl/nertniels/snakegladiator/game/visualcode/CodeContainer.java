package nl.nertniels.snakegladiator.game.visualcode;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class CodeContainer {
	
	private ArrayList<Block> blocks;
	
	private Block selectedBlock = null;
	private int mouseDragLastX;
	private int mouseDragLastY;
	
	
	public CodeContainer() {
		blocks = new ArrayList<Block>();
		
		blocks.add(new PrintBlock(20, 20, 100, 50));
		blocks.add(new PrintBlock(20, 20, 100, 50));
		blocks.get(1).setSnappedTo(blocks.get(0));
	}
	
	public void update() {
		for(Block b : blocks ) {
			b.update(blocks);
		}
	}
	
	public void render(Graphics g) {
		for(Block b : blocks ) {
			b.render(g);
		}
	}
	
	public boolean handleMouseDown(MouseEvent e) {
		for(Block b : blocks) {
			if(b.contains(e.getX(), e.getY())) {
				selectedBlock = b;
				mouseDragLastX = e.getX();
				mouseDragLastY = e.getY();
				return true;
			}
		}
		return false;
	}
	
	public boolean handleMouseUp(MouseEvent e) {
		if(selectedBlock != null) {
			selectedBlock.isBeingDragged = false;
			selectedBlock.snap(blocks);
			selectedBlock = null;
			return true;
		}
		return false;
	}
	
	public boolean handleMouseDragged(MouseEvent e) {
		if(selectedBlock != null) {
			selectedBlock.setSnappedTo(null);
			selectedBlock.isBeingDragged = true;
			int x = e.getX();
			int y = e.getY();
			int dX = x - mouseDragLastX;
			int dY = y - mouseDragLastY;
			selectedBlock.x += dX;
			selectedBlock.y += dY;
			mouseDragLastX = x;
			mouseDragLastY = y;
			return true;
		}
		return false;
	}
	
}
