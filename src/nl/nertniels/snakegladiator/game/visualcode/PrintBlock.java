package nl.nertniels.snakegladiator.game.visualcode;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import nl.nertniels.snakegladiator.main.Settings;

public class PrintBlock extends Block {
	
	public PrintBlock(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public void update(ArrayList<Block> blocks) {
		if(isBeingDragged) {
			for(Block b : blocks) {
				b.isAbleToSnap(this);
			}
		}
	}

	public void render(Graphics g) {
		if(snappedTo != null) {
			x = snappedTo.x;
			y = snappedTo.y + snappedTo.height;
		}
		
		height = g.getFontMetrics().getHeight()+2*Settings.MARGIN_BLOCK_TEXT;
		
		g.setColor(Settings.COLOR_BLOCK_PRINT);
		g.fillRect(x, y, width, height);
		
		g.setColor(Color.black);
		g.drawString("print", x+10, getCenterY()+g.getFontMetrics().getHeight()/4);
		
		if(somethingSnap) {
			g.setColor(Color.white);
			g.fillRect(x, y+height, width, Settings.MARGIN_BLOCK_SNAP);
		}
	}

	
	
}
