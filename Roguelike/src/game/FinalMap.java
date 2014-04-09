package game;

import entities.StairTile;
import graphics.ImageRegistry;

import java.awt.Point;
import java.awt.Rectangle;

import mapGeneration.MapGenerator;
import mapGeneration.MapInterpreter;
import mapGeneration.MapRand;


/**
 * 
 * @author Kevin
 *
 */
public final class FinalMap extends MapGenerator {

	private static final int HEIGHT = 40;
	private static final int WIDTH = 60;
	
	private static final int ROOM_PADDING = 7;
	
	private static final Point STAIR_POINT = new Point(ROOM_PADDING+3, ROOM_PADDING+3);
	
	private ImageRegistry skin;
	
	
	private Map map;
	private StairTile link;
	
	
	public FinalMap(ImageRegistry skin) {
		super(WIDTH, HEIGHT);
		this.skin = skin;
		generateMap();
		
	}

	@Override
	protected void generateMap() {
		Rectangle room = new Rectangle(ROOM_PADDING, ROOM_PADDING, WIDTH-ROOM_PADDING, HEIGHT-ROOM_PADDING);
		
		super.fillWallRoom(room);
		super.addRoom(room);
		
		
	}
	
	/**
	 * Converts the map into a map state
	 * @return
	 */
	public Map getMap(){
		if (map == null){
			map = createMap();
		}
		return map;
	}

	private Map createMap(){
		
		ImageRegistry[] skinWrap = {skin};
		Map newMap = MapInterpreter.interpretMap(this, skinWrap, 9001);
		
		
		
		return newMap;
	}
	
	
	/**
	 * Will automatically link the provided room with a stair tile
	 * and complete the link.
	 * @param linkMap
	 */
	public void linkRoom(Map linkMap){
		
		if (link.getpB() == null){
			//complete the link
			Point stair1 = STAIR_POINT;
			Point stair2;


			Rectangle[] rooms2 = linkMap.getRooms();
			stair2 = MapRand.randPoint(MapRand.innerRectangle(rooms2[MapRand.randInt(rooms2.length -1)]));

			//ensure staircase gets a free space
			while ((linkMap.getTile(stair2.x, stair2.y).getItemCount() > 0) || (linkMap.getTile(stair2.x, stair2.y).isOccupied() == true)){
				stair2 = MapRand.randPoint(MapRand.innerRectangle(rooms2[MapRand.randInt(rooms2.length -1)]));
			}


			//create the special staircase link
			this.map.setTile(stair1.x, stair1.y, new StairTile(this.map, linkMap, stair1, stair2));
			linkMap.setTile(stair2.x, stair2.y, new StairTile(linkMap, this.map, stair2, stair1));
		}
		else{
			System.err.println("Warning! Attempted to link final level to more than one existing map");
		}
	}
	

}
