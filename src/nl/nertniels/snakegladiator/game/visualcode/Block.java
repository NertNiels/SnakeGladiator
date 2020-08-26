package nl.nertniels.snakegladiator.game.visualcode;

import java.awt.Graphics;
import java.util.ArrayList;

import nl.nertniels.snakegladiator.main.Settings;

public abstract class Block {
	
	public int x;
	public int y;
	public int width;
	public int height;;
	
	protected Block snappedTo = null;
	public boolean isBeingDragged = false;
	public boolean somethingSnap = false;
	
	public abstract void update(ArrayList<Block> blocks);
	
	public abstract void render(Graphics g);
	
	public int getCenterx() {
		return x + width/2;
	}
	
	public int getCenterY() {
		return y + height/2;
	}
	
	public boolean contains(int tX, int tY) {
		return (tX >= x && tY >= y && tX < x+width && tY < y+height);
	}
	
	public boolean isAbleToSnap(Block b) {
		somethingSnap = (b.x >= x && b.y >= y+height && b.x < x+width && b.y < y+height+Settings.MARGIN_BLOCK_SNAP);
		return somethingSnap;
	}
	
	public void setSnappedTo(Block snap) {
		snappedTo = snap;
	}
	
	public Block getSnappedTo() {
		return snappedTo;
	}
	
	public void snap(ArrayList<Block> blocks) {
		for(Block b : blocks) {
			if(b.isAbleToSnap(this)) {
				snappedTo = b;
				b.somethingSnap = false;
			}
		}
	}
}
