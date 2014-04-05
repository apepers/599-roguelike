package game;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import entities.Tile;

public class Map {

	private int width;
	private int height;
	
	private Tile[][] grid;
	
	private ArrayList<Rectangle> rooms = new ArrayList<Rectangle>();
	private Point spawn;
	
	
	/**
	 * Creates an empty map of width x height.
	 * @param width
	 * @param height
	 */
	public Map(int width, int height){
		this.width = width;
		this.height = height;
		grid = new Tile[height][width];
	}
	
	
	
	public void setTile(int x, int y, Tile tile){
		grid[x][y] = tile;
		tile.setRow(y);
		tile.setColumn(x);
	}

	public Tile getTile(int i, int j) {
		// TODO Auto-generated method stub
		return grid[i][j];
	}

	public void addRoom(Rectangle room){
		rooms.add(room);
	}
	
	/**
	 * Gets all the rooms currently represented by the map.
	 * All rectangles include the walls of the room.
	 * @return
	 */
	public Rectangle[] getRooms(){
		Rectangle[] all = new Rectangle[rooms.size()];
		rooms.toArray(all);
		return all;
	}



	public int getWidth() {
		return width;
	}



	public int getHeight() {
		return height;
	}



	public Point getSpawn() {
		return spawn;
	}

	public void setSpawn(Point spawn) {
		this.spawn = spawn;
	}
}
