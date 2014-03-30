package game;

import entities.Tile;

public class Map {

	private int width;
	private int height;
	
	private Tile[][] grid;
	
	/**
	 * Creates an empty map of width x height.
	 * @param width
	 * @param height
	 */
	public Map(int width, int height){
		this.width = width;
		this.height = height;
		grid = new Tile[width][height];
	}
	
	public void setTile(int x, int y, Tile tile){
		grid[x][y] = tile;
	}
}
