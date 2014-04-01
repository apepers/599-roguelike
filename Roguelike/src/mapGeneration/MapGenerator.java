package mapGeneration;


import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * The generalization of a map. Does not contain any tile
 * information. Each cell is filled with a placeholder for a
 * real tile.
 * 
 * The generated map contains a list of rectangles in which it is safe 
 * add monsters and objects. This typically excludes walls.
 * 
 * It is expected that when the rooms are drawn, any intersection with a wall 
 * corridor will result in the creation of a door.
 * @author Kevin
 *
 */
public abstract class MapGenerator implements Iterable<MapTile>{

	private int width = 0;
	private int height = 0;

	protected MapTile[][] grid; 

	private Point playerSpawn = null;

	private ArrayList<Rectangle> rooms = new ArrayList<Rectangle>();			//rectangles represetnting the rooms, includes walls


	public MapGenerator(int width, int height){
		this.width = width;
		this.height = height;

		grid = new MapTile[width][height];

		for(int i= 0; i< width; i++){
			for(int j =0; j < height; j++){
				grid[i][j] = MapTile.SPACE;
			}
		}
	}


	protected abstract void generateMap();




	//====================================================================================
	//Map filling methods.

	/**
	 * Completely fills a rectangle with the specified tile.
	 * @param r
	 * @param tile
	 */
	protected void fillRectangle(Rectangle r, MapTile tile){

		for (int h = r.x; h <= r.x + r.width ; h++){
			for (int v = r.y; v <= r.y +  r.height; v++){
				grid[h][v] = tile;
			}
		}

	}

	/**
	 * Fills a rectangle's border with a specified title.
	 * @param r
	 * @param tile
	 */
	protected void fillBorder(Rectangle r, MapTile tile){

		//horizonal lines
		for (int h = r.x; h <= r.x + r.width; h++){
			grid[h][r.y] = tile;
			grid[h][r.y + r.height-1] = tile;
		}

		//vertical lines
		for (int v = r.y;  v < r.y + r.height ; v++ ){
			grid[r.x][v] = tile;
			grid[r.x + r.width-1][v] = tile;
		}
	}

	/**
	 * Fills the specified rectangle's borders with the standard 
	 * filled with the correct corners.
	 * @param r
	 * @param tile
	 */
	protected void fillWallRoom(Rectangle r){
		//horizonal lines
		for (int h = r.x; h <= r.x + r.width-1; h++){
			grid[h][r.y] = MapTile.WALL_H;
			grid[h][r.y + r.height-1] = MapTile.WALL_H;
		}

		//vertical lines
		for (int v = r.y;  v < r.y + r.height-1; v++ ){
			grid[r.x][v] = MapTile.WALL_V;
			grid[r.x + r.width-1][v] = MapTile.WALL_V;
		}

		//set the corners with the appropriate titles
		grid[r.x][r.y] = MapTile.WALL_TL_CORNER;
		grid[r.x + r.width-1][r.y] = MapTile.WALL_TR_CORNER;
		grid[r.x][r.y + r.height-1] = MapTile.WALL_BL_CORNER;
		grid[r.x + r.width-1][r.y + r.height-1] = MapTile.WALL_BR_CORNER;


	}

	/**
	 * Fills a rectangle with a wall border and
	 * the floor tiles
	 * @param r
	 */
	protected void fillRoom(Rectangle r){
		fillRectangle(MapRand.innerRectangle(r), MapTile.ROOM_FLOOR);
		fillWallRoom(r);
	}

	/**
	 * Draws a corridor according the specific style.
	 * @param A The start point to draw
	 * @param mid The middle point to pass bend through
	 * @param B The end point to draw to
	 * @param tile The tile to fill the corridor with.
	 * @param horizontal Draws with two horizontal pieces or not.
	 * HORIZONTAL
	 * ===========
	 *           =
	 *           =
	 *           ===========B
	 *           
	 * HORIZONTAL
	 * 			 ===========B
	 *           =
	 *           =
	 * ===========
	 * 
	 * VERTICAL
	 * =
	 * =
	 * =
	 * =======
	 * 		 =
	 * 		 =
	 * 		 =
	 * 
	 * VERTICAL
	 * 		 =
	 *  	 =
	 *  	 =
	 * =======
	 * =
	 * =
	 * =
	 * 
	 * 
	 */
	protected void fillCorridor(Point A, Point mid, Point B, MapTile tile, boolean horizontal){

		if (horizontal == true){
			//ensure start is the furthest left of the two points
			Point start = (A.x < B.x) ? A : B;
			Point end = (A.x < B.x) ? B : A;
			Point midPoint = mid;	

			//determine step up or down
			int negation;
			if (start.y > end.y){
				negation = -1;					//point A is below point B on Y
			}
			else{
				negation = 1;
			}

			//horizontal draw
			//leftmost first
			for (int h = 0; h <= Math.abs(midPoint.x - start.x); h++){
				fillRoomCorridor(start.x+h, start.y, true, tile);
			}

			//rightmost next
			for (int h = 0; h <= Math.abs(midPoint.x - end.x); h++){
				fillRoomCorridor(end.x-h, end.y, false, tile);
			}

			//vertical draw
			for(int v = 0 ; v <= Math.abs(start.y - end.y); v++){
				fillRoomCorridor(midPoint.x, start.y + (v * negation), false, tile);
			}
		}
		else if (horizontal == false){
			//ensure start is the furthest left of the two points
			Point start = (A.x < B.x) ? A : B;
			Point end = (A.x < B.x) ? B : A;
			Point midPoint = mid;	

			//determine step up or down
			int negation;
			if (start.y > end.y){
				negation = -1;					//point A is below point B on Y
			}
			else{
				negation = 1;
			}


			//vertical draw
			//top first
			for (int v = 0; v <= Math.abs(midPoint.y - start.y); v++){
				fillRoomCorridor(start.x,start.y + (v* negation), false, tile);
			}

			//bottom next
			for (int v = 0; v <= Math.abs(midPoint.y - end.y); v++){
				fillRoomCorridor(end.x, end.y - (v * negation), false, tile);
			}

			//horizontal draw
			for(int h = start.x ; h <= end.x; h++){
				boolean leftRight = false;
				leftRight = (!leftRight) ? true : false;
				fillRoomCorridor(h, midPoint.y, leftRight, tile);
			}
		}

	}

	/**
	 * Fills tile of corridor iff the space is blank. If there is a wall, then
	 * the corridor is filled with a door, and if there's a room floor,
	 * then the corridor is not filled at all.
	 * @param x
	 * @param y
	 * @param vRight Tells if the drawing is done on the left or right wall. (vertical) Use false for default
	 * @param tile
	 */
	private void fillRoomCorridor(int x, int y, boolean vRight, MapTile tile){
		if(((x >= 0) && (x < width)) && ((y >=0) && (y < height))){
			if(grid[x][y] == MapTile.WALL_H){
				//drawing on a wall, make as door
				grid[x][y] = MapTile.DOOR_FRONT;
			}
			else if((grid[x][y] == MapTile.WALL_V) && (vRight == true)){
				grid[x][y] = MapTile.DOOR_RIGHT;
			}
			else if((grid[x][y] == MapTile.WALL_V) && (vRight == false)){
				grid[x][y] = MapTile.DOOR_LEFT;
			}
			else if(grid[x][y] != MapTile.ROOM_FLOOR){
				grid[x][y] = tile;
			}
		}
	}

	//==============================================================================================================
	//Getters and setters.


	/**
	 * Gets a maptile
	 * @param x
	 * @param y
	 * @return
	 */
	public MapTile getTile(int x, int y){
		return grid[x][y];
	}


	/**
	 * Width in cells
	 * @return
	 */
	public int getWidth() {
		return width;
	}


	/**
	 * Height in cells
	 * @return
	 */
	public int getHeight() {
		return height;
	}


	/**
	 * Gets the spawn point of the player as generated by the map
	 * @return
	 */
	public Point getPlayerSpawn() {
		return playerSpawn;
	}

	/**
	 * Sets the player's spawn point. No tile is drawn.
	 * @param spawn
	 */
	protected void setPlayerSpawn(Point spawn){
		this.playerSpawn = spawn;
	}
	

	/**
	 * Adds a room to the map generator.
	 * @param room
	 */
	protected void addRoom(Rectangle room){
		rooms.add(room);
	}

	public Rectangle[] getRooms(){
		Rectangle[] rects = new Rectangle[rooms.size()];
		rooms.toArray(rects);
		return rects;
	}

	protected void writeTile(int x, int y, MapTile tile){
		grid[x][y] = tile;
	}
	/**
	 * Returns the ASCII representation of the grid according to MapRand
	 * 
	 */
	public String toString(){
		String result = "";

		for (int i =0; i < width; i++){
			for (int j=0; j < height; j++){
				result = result + grid[i][j].toString();
			}
		}

		return result;
	}
	//===============================================================================================
	//iteratable implementation


	@Override
	/**
	 * Returns an interator that runs from left to right through
	 * the entire grid. Assumes a rectangular grid.
	 */
	public Iterator<MapTile> iterator() {
		return new GridRunner();
	}
	private class GridRunner implements Iterator<MapTile>{

		private int current = grid.length;

		@Override
		public boolean hasNext() {
			return current < grid.length;
		}

		@Override
		public MapTile next() {
			MapTile result = grid[current / width][current % height];
			current++;
			return result;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}













}
