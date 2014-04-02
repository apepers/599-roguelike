package mapGeneration;

import java.awt.Point;
import java.awt.Rectangle;


public class SimpleMap extends MapGenerator {


	private static final int DEFAULT_ZONE_X_ROOMS = 3;
	private static final int DEFAULT_ZONE_Y_ROOMS = 3;

	private static final int MIN_ROOM_WIDTH = 4;				//minimum room width
	private static final int MIN_ROOM_HEIGHT = 4;				//minimum room height

	private Rectangle[][] roomLayouts;
	private int roomsX;
	private int roomsY;


	/**
	 * Creates a map based on the given cell width and height.
	 * The map created with the given size may not be created as specified
	 * if the map size is not a multiple of DEFAULT_ZONE constants.
	 * @param width
	 * @param height
	 */
	public SimpleMap(int width, int height) {
		this(width/DEFAULT_ZONE_X_ROOMS, height/DEFAULT_ZONE_Y_ROOMS, DEFAULT_ZONE_X_ROOMS, DEFAULT_ZONE_Y_ROOMS);
	}



	/**
	 * Recommended way to create a classic Rouge level.
	 * May extend the number of map zones.
	 * @param zoneWidth Width of each zone
	 * @param zoneHeight Height of each zone
	 * @param roomsX Number of zones along X axis
	 * @param roomsY Number of zones along Y axis
	 */
	public SimpleMap(int zoneWidth, int zoneHeight, int roomsX, int roomsY){
		super(zoneWidth * roomsX, zoneHeight * roomsY);

		this.roomsX = roomsX;
		this.roomsY = roomsY;

		roomLayouts = new Rectangle[roomsX][roomsY];


		for (int i =0; i < roomsX; i++){
			for ( int j =0; j< roomsY; j++){
				roomLayouts[i][j] = new Rectangle(i * zoneWidth, j * zoneHeight, zoneWidth, zoneHeight);
			}
		}
		generateMap();
	}


	@Override
	/**
	 * Generates the map so it is filled. Algorithm implemented based on details from:
	 * http://www.roguebasin.com/index.php?title=Simple_Rogue_levels
	 * 
	 * At the end of the generation, the player is spawned on a random point.
	 */
	protected void generateMap() {

		//This simple code is for the demonstration. Linear room creation. 

		//generation parameters

		Rectangle[][] rooms = new Rectangle[roomsX][roomsY];

		//create every room.
		for (int i =0; i < roomsX; i++){
			for ( int j =0; j< roomsY; j++){
				rooms[i][j] = MapRand.randRect(roomLayouts[i][j]);

				//any room less than the speicfied height or size is converted into a hidden room
				if((rooms[i][j].width < MIN_ROOM_WIDTH) || (rooms[i][j].height < MIN_ROOM_HEIGHT)){
					rooms[i][j].width = 1;
					rooms[i][j].height = 1;
				}
				else{
					//add room
					super.fillRoom(rooms[i][j]);
					super.addRoom(rooms[i][j]);
				}
			}
		}





		//create a link between every room
		Point[] corridorA = new Point[rooms.length-1];
		Point[] corridorB = new Point[rooms.length-1];
		Point[] corriMids = new Point[corridorA.length];			//midpoint of paths
		for (int i =0; i < roomsX-1; i++){
			for ( int j =0; j< roomsY-1; j++){
				corridorA[i] = MapRand.randPerimeter(MapRand.innerRectangle(rooms[i][j]));			
				corridorB[i] = MapRand.randPerimeter(MapRand.innerRectangle(rooms[i+1][j]));
				corriMids[i] = MapRand.randPoint(MapRand.rectFromPoints(corridorA[i], corridorB[i]));
			}
		}




		//draw the corridors
		for (int i =0; i < corridorA.length; i++){
			super.fillCorridor(corridorA[i], corriMids[i], corridorB[i], MapTile.CORRIDOR_FLOOR, true);
		}


		//determine door intersections.

		//set player spawn
		super.setPlayerSpawn(MapRand.randPoint(MapRand.innerRectangle(super.getRooms()[MapRand.randInt(super.getRoomCount()-1)])));

	}


}
