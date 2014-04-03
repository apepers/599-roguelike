package mapGeneration;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import mapGeneration.MapRand.RectangleSide;


public class SimpleMap extends MapGenerator {


	private static final int DEFAULT_ZONE_X_ROOMS = 3;
	private static final int DEFAULT_ZONE_Y_ROOMS = 3;

	private static final int MIN_ROOM_WIDTH = 4;				//minimum room width
	private static final int MIN_ROOM_HEIGHT = 4;				//minimum room height

	private static final int AREA_PADDING = 1;					//minimum padding between zones
	private static final double FILL_SCALE = 0.4;				//the filling scale to fill the entire zone. 
	private static final double HIDDEN_PROB = 0.2;				//the chance of creating a junction room



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

		//intialize each zone
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
				if (MapRand.randBool(HIDDEN_PROB)){
					//create hidden room within the padding limits
					Rectangle subZone = roomLayouts[i][j];
					for (int k= 0; k < AREA_PADDING; k++){
						subZone = MapRand.innerRectangle(subZone);
					}
					Point location = MapRand.randPoint(subZone);
					rooms[i][j] = new Rectangle(location.x, location.y, 1,1);
				}
				else{
					//create legitimate room.
					Point roomLocation = MapRand.randPoint(new Rectangle(roomLayouts[i][j].x + AREA_PADDING, roomLayouts[i][j].y + AREA_PADDING, Math.max(roomLayouts[i][j].width/6,1), Math.max(roomLayouts[i][j].height /6, 1)));		//pick point from first sixth
					int width = MapRand.randInt((int) (Math.abs(roomLocation.x - (roomLayouts[i][j].x + roomLayouts[i][j].width)) * FILL_SCALE), Math.abs(roomLocation.x - (roomLayouts[i][j].x + roomLayouts[i][j].width)) - AREA_PADDING);
					int height = MapRand.randInt((int) (Math.abs(roomLocation.y - (roomLayouts[i][j].y + roomLayouts[i][j].height)) * FILL_SCALE), Math.abs(roomLocation.y - (roomLayouts[i][j].y + roomLayouts[i][j].height)) - AREA_PADDING);
					rooms[i][j] = new Rectangle(roomLocation.x, roomLocation.y, Math.max(width, MIN_ROOM_WIDTH), Math.max(height, MIN_ROOM_HEIGHT));
				}


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
		linkCorridors(rooms);





		//determine door intersections.

		//set player spawn
		super.setPlayerSpawn(MapRand.randPoint(MapRand.innerRectangle(super.getRooms()[MapRand.randInt(super.getRoomCount()-1)])));

	}


	/**
	 * Link rooms according to a set of defined patterns
	 * @param rooms
	 */
	private void linkCorridors(Rectangle[][] rooms){
		double[] prob = {0.50,0.50};
		int style = MapRand.randArray(prob);

		if (style ==0){
			//total linkage between all points

			//all horizontal links
			Point[] corridorA = new Point[(roomsX) * (roomsY-1)];
			Point[] corridorB = new Point[(roomsX) * (roomsY-1)];
			Point[] corriMids = new Point[corridorA.length];			//midpoint of paths
			for (int i =0; i < roomsX; i++){
				for ( int j =0; j< roomsY-1; j++){
					corridorA[(i*(roomsY-1)) + j] = MapRand.randRectEdge(MapRand.innerRectangle(rooms[j][i]), RectangleSide.RIGHT);			
					corridorB[(i*(roomsY-1)) + j] = MapRand.randRectEdge(MapRand.innerRectangle(rooms[j+1][i]), RectangleSide.LEFT);
					corriMids[(i*(roomsY-1)) + j] = MapRand.randPoint(MapRand.innerRectangle(MapRand.innerRectangle(MapRand.innerRectangle(MapRand.rectFromPoints(corridorA[j], corridorB[j])))));
				}
			}

			//draw the corridors
			for (int i =0; i < corridorA.length; i++){
				if(corridorA[i] != null){
					super.fillCorridor(corridorA[i], corriMids[i], corridorB[i], MapTile.CORRIDOR_FLOOR, true);
				}
			}

			//all vertical links
			corridorA = new Point[(roomsX-1) * (roomsY)];
			corridorB = new Point[(roomsX-1) * (roomsY)];
			corriMids = new Point[corridorA.length];			//midpoint of paths
			for (int i =0; i < roomsY; i++){
				for ( int j =0; j< roomsX-1; j++){
					corridorA[(i*(roomsY-1)) + j] = MapRand.randRectEdge(MapRand.innerRectangle(rooms[i][j]), RectangleSide.BOTTOM);			
					corridorB[(i*(roomsY-1)) + j] = MapRand.randRectEdge(MapRand.innerRectangle(rooms[i][j+1]), RectangleSide.TOP);
					corriMids[(i*(roomsY-1)) + j] = MapRand.randPoint(MapRand.innerRectangle(MapRand.innerRectangle(MapRand.innerRectangle(MapRand.rectFromPoints(corridorA[j], corridorB[j])))));
				}
			}

			//draw the corridors
			for (int i =0; i < corridorA.length; i++){
				if(corridorA[i] != null){
					super.fillCorridor(corridorA[i], corriMids[i], corridorB[i], MapTile.CORRIDOR_FLOOR, false);
				}
			}
		}
		else if (style == 1){
			//partial linkage, ladder like, all horizontals, but only edge verticals
			//all horizontal links
			Point[] corridorA = new Point[(roomsX) * (roomsY-1)];
			Point[] corridorB = new Point[(roomsX) * (roomsY-1)];
			Point[] corriMids = new Point[corridorA.length];			//midpoint of paths
			for (int i =0; i < roomsX; i++){
				for ( int j =0; j< roomsY-1; j++){
					corridorA[(i*(roomsY-1)) + j] = MapRand.randRectEdge(MapRand.innerRectangle(rooms[j][i]), RectangleSide.RIGHT);			
					corridorB[(i*(roomsY-1)) + j] = MapRand.randRectEdge(MapRand.innerRectangle(rooms[j+1][i]), RectangleSide.LEFT);
					corriMids[(i*(roomsY-1)) + j] = MapRand.randPoint(MapRand.innerRectangle(MapRand.innerRectangle(MapRand.innerRectangle(MapRand.rectFromPoints(corridorA[j], corridorB[j])))));
				}
			}

			//draw the corridors
			for (int i =0; i < corridorA.length; i++){
				if(corridorA[i] != null){
					super.fillCorridor(corridorA[i], corriMids[i], corridorB[i], MapTile.CORRIDOR_FLOOR, true);
				}
			}

			//all left most vertical links
			corridorA = new Point[(roomsX-1)];
			corridorB = new Point[(roomsX-1)];
			corriMids = new Point[corridorA.length];			//midpoint of paths

			for ( int i =0; i< roomsX-1; i++){
				corridorA[i] = MapRand.randRectEdge(MapRand.innerRectangle(rooms[0][i]), RectangleSide.BOTTOM);			
				corridorB[i] = MapRand.randRectEdge(MapRand.innerRectangle(rooms[0][i+1]), RectangleSide.TOP);
				corriMids[i] = MapRand.randPoint(MapRand.innerRectangle(MapRand.innerRectangle(MapRand.innerRectangle(MapRand.rectFromPoints(corridorA[i], corridorB[i])))));
			}


			//draw the corridors
			for (int i =0; i < corridorA.length; i++){
				if(corridorA[i] != null){
					super.fillCorridor(corridorA[i], corriMids[i], corridorB[i], MapTile.CORRIDOR_FLOOR, false);
				}
			}

			//all right most vertical links
			corridorA = new Point[(roomsX-1)];
			corridorB = new Point[(roomsX-1)];
			corriMids = new Point[corridorA.length];			//midpoint of paths

			for ( int i =0; i< roomsX-1; i++){
				corridorA[i] = MapRand.randRectEdge(MapRand.innerRectangle(rooms[roomsX-1][i]), RectangleSide.BOTTOM);			
				corridorB[i] = MapRand.randRectEdge(MapRand.innerRectangle(rooms[roomsX-1][i+1]), RectangleSide.TOP);
				corriMids[i] = MapRand.randPoint(MapRand.innerRectangle(MapRand.innerRectangle(MapRand.innerRectangle(MapRand.rectFromPoints(corridorA[i], corridorB[i])))));
			}


			//draw the corridors
			for (int i =0; i < corridorA.length; i++){
				if(corridorA[i] != null){
					super.fillCorridor(corridorA[i], corriMids[i], corridorB[i], MapTile.CORRIDOR_FLOOR, false);
				}
			}
		}

	}
}
