package entities;

import game.Map;

import java.awt.Point;

public class StairTile extends Tile {

	private Map mapA;
	private Map mapB;
	
	private Point pA;
	private Point pB;
	
	
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
		this.mapA = mapA;
		this.mapB = mapB;
		
		this.pA = pA;
		this.pB = pB;
	}


	public Map getMapA() {
		return mapA;
	}


	public Map getMapB() {
		return mapB;
	}


	public Point getpA() {
		return pA;
	}


	public Point getpB() {
		return pB;
	}


	
}
