package mapGeneration;


import java.awt.Point;
import java.awt.Rectangle;


/**
 * The generalization of a map. Does not contain any tile
 * information. Each cell is filled with a placeholder for a
 * real tile.
 * @author Kevin
 *
 */
public class Map {

	private int width = 0;
	private int height = 0;

	private MapTile[][] grid; 

	private Point playerSpawn = null;

	public Map(int width, int height){
		this.width = width;
		this.height = height;

		grid = new MapTile[width][height];

		
		generateMap();
		
		
	}

	public MapTile getTile(int x, int y){
		return grid[x][y];
	}


	/**
	 * Generates the map so it is filled. Algorithm implemented based on details from:
	 * http://www.roguebasin.com/index.php?title=Simple_Rogue_levels
	 * 
	 * At the end of the generation, the player is spawned on a random point.
	 */
	private void generateMap() {

		//This simple code is for the demonstration. Linear room creation. 

		//generation parameters
		int MAX_ROOMS = 7;
		int MIN_WIDTH = 3;
		int MIN_HEIGHT = 3;
		int MAX_WIDTH = 11;
		int MAX_HEIGHT = 8;


		//create every room
		Rectangle[] rooms = new Rectangle[MAX_ROOMS];

		for (int i =0; i < MAX_ROOMS; i++){
			rooms[i] = new Rectangle(MapRand.randInt(width - MAX_WIDTH-1), MapRand.randInt(height - MAX_HEIGHT-1), MapRand.randInt(MIN_WIDTH, MAX_WIDTH), MapRand.randInt(MIN_HEIGHT, MAX_HEIGHT));

		}

		//create a link between every room
		Point[] corridorA = new Point[MAX_ROOMS-1];
		Point[] corridorB = new Point[MAX_ROOMS-1];
		Point[] corriMids = new Point[corridorA.length];			//midpoint of paths
		for (int i =0; i < MAX_ROOMS-1; i++){
			corridorA[i] = MapRand.randRect(MapRand.innerRectangle(rooms[i]));			
			corridorB[i] = MapRand.randRect(MapRand.innerRectangle(rooms[i+1]));
			corriMids[i] = MapRand.randRect(MapRand.rectFromPoints(corridorA[i], corridorB[i]));
		}




		//draw the rooms

		//draw inner rectangle
		for (int i = 0; i < rooms.length ; i++){
			Rectangle inner = MapRand.innerRectangle(rooms[i]);

			for (int h = inner.x; h <= inner.x + inner.width ; h++){
				for (int v = inner.y; v <= inner.y +  inner.height; v++){
					grid[h][v] = MapTile.FLOOR;
				}
			}
		}

		//draw border with walls
		for (int i = 0; i < rooms.length ; i++){
			Rectangle room = rooms[i];

			//horizonal lines
			for (int h = room.x; h <= room.x + room.width; h++){
				grid[h][room.y] = MapTile.WALL;
				grid[h][room.y + room.height] = MapTile.WALL;
			}

			//vertical lines
			for (int v = room.y;  v < room.y + room.height ; v++ ){
				grid[room.x][v] = MapTile.WALL;
				grid[room.x + room.width][v] = MapTile.WALL;
			}
		}




		//draw every link according to midpoint and end points defined.
		if (MapRand.randBool() == true){
			//draw vertical first then horizontal

			for (int i =0; i < corridorA.length ; i++){

				
				//ensure start is the furthest left of the two points
				Point start = (corridorA[i].x < corridorB[i].x) ? corridorA[i] : corridorB[i];
				Point end = (corridorA[i].x < corridorB[i].x) ? corridorB[i] : corridorA[i];
				Point midPoint = corriMids[i];	

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
					grid[start.x][start.y + (v* negation)] = MapTile.FLOOR;
				}

				//bottom next
				for (int v = 0; v <= Math.abs(midPoint.y - end.y); v++){
					grid[end.x][end.y - (v * negation)] = MapTile.FLOOR;
				}

				//horizontal draw
				for(int h = start.x ; h <= end.x; h++){
					grid[h][midPoint.y] = MapTile.FLOOR;
				}
			}



		}
		else{
			//draw horizontal first, then vertical
			for (int i =0; i < corridorA.length ; i++){
				
				//ensure start is the furthest left of the two points
				Point start = (corridorA[i].x < corridorB[i].x) ? corridorA[i] : corridorB[i];
				Point end = (corridorA[i].x < corridorB[i].x) ? corridorB[i] : corridorA[i];
				Point midPoint = corriMids[i];	

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
					grid[start.x+h][start.y] = MapTile.FLOOR;
				}

				//rightmost next
				for (int h = 0; h <= Math.abs(midPoint.x - end.x); h++){
					grid[end.x-h][end.y] = MapTile.FLOOR;
				}

				//vertical draw
				for(int v = 0 ; v <= Math.abs(start.y - end.y); v++){
					grid[midPoint.x][start.y + (v * negation)] = MapTile.FLOOR;
				}
			}
		}

		//determine door intersections.

		//set player spawn
		playerSpawn = MapRand.randRect(MapRand.innerRectangle(rooms[MapRand.randInt(rooms.length-1)]));

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


}
