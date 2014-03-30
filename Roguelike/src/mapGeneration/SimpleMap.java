package mapGeneration;

import java.awt.Point;
import java.awt.Rectangle;


public class SimpleMap extends MapGenerator {

	
	
	
	
	private Rectangle[][] roomLayouts = {{new Rectangle(5,5,10, 15), new Rectangle(25,11, 10, 6), new Rectangle(4, 36, 5,6)}};
	
	
	
	
	public SimpleMap(int width, int height) {
		super(width, height);

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



		Rectangle[] rooms = roomLayouts[0];
		

		//create a link between every room
		Point[] corridorA = new Point[rooms.length-1];
		Point[] corridorB = new Point[rooms.length-1];
		Point[] corriMids = new Point[corridorA.length];			//midpoint of paths
		for (int i =0; i < rooms.length-1; i++){
			corridorA[i] = MapRand.randPerimeter(MapRand.innerRectangle(rooms[i]));			
			corridorB[i] = MapRand.randPerimeter(MapRand.innerRectangle(rooms[i+1]));
			corriMids[i] = MapRand.randPoint(MapRand.rectFromPoints(corridorA[i], corridorB[i]));
		}




		//draw the rooms

		//draw inner rectangle
		for (int i = 0; i < rooms.length ; i++){
			Rectangle inner = MapRand.innerRectangle(rooms[i]);

			this.fillRectangle(inner, MapTile.ROOM_FLOOR);
		}

		//draw border with walls
		for(int i=0; i < rooms.length; i++){
			this.fillWallRoom(rooms[i]);
		}


		//draw the corridors
		for (int i =0; i < corridorA.length ; i++){
			this.fillCorridor(corridorA[i], corriMids[i], corridorB[i], MapTile.CORRIDOR_FLOOR, MapRand.randBool());

		}


		//determine door intersections.

		//set player spawn
		super.setPlayerSpawn(MapRand.randPoint(MapRand.innerRectangle(rooms[MapRand.randInt(rooms.length-1)])));

	}
	

}
