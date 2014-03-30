package entities;

import java.awt.Image;

public class TileFactory {

	private static final Tile BLANK = new Tile();
	
	/**
	 * Makes an impassable wall tile
	 * @param tileImg
	 * @return
	 */
	public static Tile makeWall(){
		return new Tile(true);
	}
	
	/**
	 * Makes an passable standard walkable floor.
	 * @param tileImg
	 * @return
	 */
	public static Tile makeFloor(){
		return new Tile(false);
	}
	
	
	
	/**
	 * Returns a blank tile. Use only if this tile is unreachable.
	 * 
	 * @return
	 */
	public static Tile makeBlank(){
		return BLANK;
	}
}
