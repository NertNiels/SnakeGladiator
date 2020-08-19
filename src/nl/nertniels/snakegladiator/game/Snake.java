package nl.nertniels.snakegladiator.game;

import java.awt.Point;
import java.util.ArrayList;

import nl.nertniels.snakegladiator.main.InputHandler;

public class Snake {
	
	public int playerId;
	public int color;
	public String playerName;
	
	private ArrayList<Point> location;
	public Point head;
	public int direction; //0 up, 1 left, 2 down, 3 right
	
	private boolean grow = false;

	public Snake(int playerId, int color, String playerName) {
		this.playerId = playerId;
		this.color = color;
		this.playerName = playerName;
		location = new ArrayList<Point>();
		location.add(new Point(5, 5));
		head = location.get(0);
	}
	
	public String update() {
		String out = "*"+playerId+"*"+direction+"*"+grow;
		
		if(direction == 0) addTile(new Point( 0, -1)); // up
		if(direction == 1) addTile(new Point(-1,  0)); // left
		if(direction == 2) addTile(new Point( 0,  1)); // down
		if(direction == 3) addTile(new Point( 1,  0)); // right
		head = location.get(location.size()-1);
		if(!grow) removeTile();
		grow = false;
		
		return out;
	}
	
	private void addTile(Point dir) {
		int x = head.x + dir.x;
		int y = head.y + dir.y;
		
		location.add(new Point(x, y)); // up
	}
	private void removeTile() {
		Point p = location.get(0);
		location.remove(0);
		
	}
	
	public void render(int[] pixels, int w, int h) {
		for(int i = 0; i < location.size(); i++) {
			int y = location.get(i).y;
			int x = location.get(i).x;
			
			pixels[location.get(i).y*w+location.get(i).x] = color;
		}
	}
	
	public void grow() {
		grow = true;
	}
	
	public void grow(boolean grow) {
		this.grow = grow;
	}
	
	public boolean testCollide(Snake snake) {
		return snake.location.contains(head);
	}

	public void setLocation(int x, int y) {
		location.clear();
		location.add(new Point(x, y));
		head = location.get(0);
	}
	
	@Override
	public String toString() {
		return "Snake [playerId=" + playerId + ", color=" + color + ", playerName=" + playerName + "]";
	}
	
	
	
}
