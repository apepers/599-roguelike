package game;

import entities.Monster;
import entities.StairTile;
import entities.Tile;
import graphics.ImageRegistry;

import java.awt.Point;
import java.awt.Rectangle;

import mapGeneration.MapGenerator;
import mapGeneration.MapInterpreter;
import mapGeneration.MapRand;
import mapGeneration.MapTile;


/**
 * 
 * @author Kevin
 *
 */
public final class FinalMap extends MapGenerator {

	private static final int HEIGHT = 32;
	private static final int WIDTH = 56;

	private static final int ROOM_PADDING = 8;
	private static final int RETRY_COUNT = 1000;
	private static final int MONSTER_COUNT = 30;
	
	
	//private static final int MONSTER_TIER = 9001;
	private static final int MONSTER_TIER = 3;
	
	
	private static final Rectangle ROOM = new Rectangle(ROOM_PADDING, ROOM_PADDING, WIDTH-(ROOM_PADDING*2), HEIGHT-(ROOM_PADDING*2));
	private static final Rectangle MONSTER_RECT = new Rectangle (ROOM_PADDING+5, ROOM_PADDING+5, ROOM.width-5, ROOM.height-5);
	
	private static final Point STAIR_POINT = new Point(ROOM_PADDING+3, ROOM_PADDING+3);
	private static final Point SPAWN_POINT = new Point(ROOM_PADDING+2,ROOM_PADDING+2);
	private static final Point TELEPORT_ITEM_POINT = new Point(ROOM.x + ROOM.width -5, ROOM.x + ROOM.height -5);
	

	private ImageRegistry skin;

	
	private Map map;
	private Rectangle room;


	public FinalMap(ImageRegistry skin) {
		super(WIDTH, HEIGHT);
		this.skin = skin;
		generateMap();

	}

	@Override
	protected void generateMap() {
		this.room = ROOM;

		super.fillRoom(room);
		super.addRoom(room);


	}

	/**
	 * Converts the map into a map state
	 * @return
	 */
	public Map initMap(){
		if (map == null){
			map = createMap();
		}
		return map;
	}

	public Map getMap(){
		return map;
	}
	
	private Map createMap(){

		ImageRegistry[] skinWrap = {skin};
		super.setPlayerSpawn(SPAWN_POINT);
		Map newMap = MapInterpreter.interpretMap(this, skinWrap, MONSTER_TIER);
		newMap.setPlayerSpawn(SPAWN_POINT);
		
		//fill the room with monsters
		Rectangle placement = MONSTER_RECT;
		for (int i = 0; i < MONSTER_COUNT; i++){
			Point tempPt = MapRand.randPoint(placement);

			int j= 0;
			j = 0;
			//get new point if there's already a monster on the tile.
			while (( (this.getTile(tempPt.x, tempPt.y) != MapTile.ROOM_FLOOR) || (newMap.getTile(tempPt.x, tempPt.y).isOccupied()) ) && (j < RETRY_COUNT) ) {
				tempPt = MapRand.randPoint(placement);
				j++;
			}

			
			
			//create monster and add to map.
			Tile selected = newMap.getTile(tempPt.x, tempPt.y);
			if(selected.isOccupied() == false){
				Monster babyMonster = Controller.getInstance().getRandMapMonster(MONSTER_TIER);
				selected.setOccupant(babyMonster);
				newMap.addMonster(babyMonster);
			
			}
				
			
		}
		
		Point tempPt = TELEPORT_ITEM_POINT;
		newMap.getTile(tempPt.x, tempPt.y).addItem(Controller.getInstance().creator.createArmour("personal teleport"));	
		
		return newMap;
	}


	/**
	 * Will automatically link the provided room with a stair tile
	 * and complete the link.
	 * @param linkMap
	 */
	public void linkRoom(Map linkMap){


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


}
