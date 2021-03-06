package nl.nertniels.snakegladiator.game;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import nl.nertniels.snakegladiator.net.visualcode.Runner;

public class Snake {
	
	public int playerId;
	public int color;
	public String playerName;
	
	private ArrayList<Point> location;
	public Point head;
	public static Point[] DIRECTIONS = new Point[] {new Point(0, -1), new Point(-1, 0), new Point(0, 1), new Point(1, 0)};
	public int direction; //0 up, 1 left, 2 down, 3 right
	
	private boolean hasGrown = false;
	private boolean grow = false;
	public boolean dead = false;
	
	public Runner runner;

	public Snake(int playerId, int color, String playerName) {
		this.playerId = playerId;
		this.color = color;
		this.playerName = playerName;
		location = new ArrayList<Point>();
		location.add(new Point(5, 5));
		head = location.get(0);
		
		runner = new Runner(this);
	}
	
	public void update(int[][] tiles) {
		if(dead) return;
		
		addTile(DIRECTIONS[direction], tiles);
		head = location.get(location.size()-1);
		hasGrown = grow;
		if(!grow) removeTile(tiles);
		grow = false;
	}
	
	private void addTile(Point dir, int[][] tiles) {
		int x = head.x + dir.x;
		int y = head.y + dir.y;
		
		location.add(new Point(x, y)); // up
		if(tiles != null) tiles[x][y] = playerId;
	}
	
	private void removeTile(int[][] tiles) {
		Point p = location.get(0);
		location.remove(0);
		if(tiles != null) tiles[p.x][p.y] = -1;
	}
	
	public void render(int[] pixels, int w, int h) {
		for(int i = 0; i < location.size()-1; i++) {
			int y = location.get(i).y;
			int x = location.get(i).x;
			
			pixels[location.get(i).y*w+location.get(i).x] = color;
		}
		pixels[head.y*w+head.x] = new Color(color).brighter().getRGB();
	}
	
	public void grow() {
		grow = true;
	}
	
	public void grow(boolean grow) {
		this.grow = grow;
	}
	
	public void die(boolean die) {
		dead = die;
		if(die) color = Color.DARK_GRAY.getRGB();
	}
	
	public String getPacketString() {
		return "*"+playerId+"*"+direction+"*"+hasGrown+"*"+dead;
	}
	
	public boolean testCollide(Snake snake) {
		if(snake.getPlayerId() == playerId) {
			for(int i = 0; i < location.size()-1; i++) {
				if(head.x == location.get(i).x && head.y == location.get(i).y) return true;
			}
		} else {
			return snake.location.contains(head);
		}
		return false;
	}

	public void setLocation(int x, int y) {
		location.clear();
		location.add(new Point(x, y));
		head = location.get(0);
	}
	
	public int getPlayerId() {
		return playerId;
	}
	
	@Override
	public String toString() {
		return "Snake [playerId=" + playerId + ", color=" + color + ", playerName=" + playerName + "]";
	}
	
}
