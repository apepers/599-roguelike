package entities;

import game.Map;
import graphics.ImageManager;
import graphics.ImageRegistry;

import java.awt.Point;

import javax.swing.ImageIcon;

public class StairTile extends Tile {

	private Map mapA;
	private Map mapB;
	
	private Point pA;
	private Point pB;
	
	private boolean imgUp = false;
	
	/**
	 * Creates a tile with the specified link point between maps.
	 * Note that this does not imply a bidirectional link.
	 * Parameters are passed in order.
	 * @param map1
	 * @param map2
	 * @param p1
	 * @param p2
	 */
	public StairTile(Map mapA, Map mapB, Point pA, Point pB){
		super(true);
		this.mapA = mapA;
		this.mapB = mapB;
		
		this.pA = pA;
		this.pB = pB;
		
		
	}
	/**
	 * Creates a tile with the specified link point between maps.
	 * Note that this does not imply a bidirectional link.
	 * Parameters are passed in order.
	 * @param map1
	 * @param map2
	 * @param p1
	 * @param p2
	 * @param imgUp True if the stairs will show stairs going up, false otherwise.
	 */
	public StairTile(Map mapA, Map mapB, Point pA, Point pB, boolean imgUp){
		super(true);
		this.mapA = mapA;
		this.mapB = mapB;
		
		this.pA = pA;
		this.pB = pB;
		
		this.imgUp = imgUp;
	}


	/**
	 * Gets the current map this stair tile is living on.
	 * @return
	 */
	public Map getMapA() {
		return mapA;
	}


	/**
	 * Gets the sibling map that the tile is linked to.
	 * @return
	 */
	public Map getMapB() {
		return mapB;
	}


	/**
	 * Gets the current position the stair tile is living on.
	 * @return
	 */
	public Point getpA() {
		return pA;
	}


	/**
	 * Gets the sibling point that the stair tile is linked to.
	 * @return
	 */
	public Point getpB() {
		return pB;
	}

	/**
	 * Sets the display image of either stair up or stair down.
	 * @param b
	 */
	public void setImgUp(boolean b){
		imgUp = b;
	}
	
	/**
	 * Gets whether it is an up staircase or not.
	 * @return
	 */
	public boolean getUp() {
		return imgUp;
	}
	
	@Override
	public ImageIcon getBackground(){
		if(this.isDiscovered()) {
			if (imgUp == true){
				return ImageManager.getGlobalRegistry().getTile("stairs_up");
			}
			else{
				return ImageManager.getGlobalRegistry().getTile("stairs_down");
			}
		} else {
			return getUndiscoveredImage();
		}
		
	}
}
