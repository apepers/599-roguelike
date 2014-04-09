package entities;

import java.awt.Image;

import javax.swing.ImageIcon;

public class TileFactory {

	private static final Tile BLANK = new Tile();
	
	/**
	 * Makes an impassable wall tile
	 * @param tileImg
	 * @return
	 */
	public static Tile makeWall(){
		return new Tile(false);
	}
	
	/**
	 * Makes an passable standard walkable floor.
	 * @param tileImg
	 * @return
	 */
	public static Tile makeFloor(){
		return new Tile(true);
	}
	
	/**
	 * Creates a door tile that is intitally closed.
	 * @param closed
	 * @param opened
	 * @return
	 */
	public static DoorTile makeDoor(ImageIcon closed, ImageIcon opened){
		return new DoorTile(closed, opened);
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
