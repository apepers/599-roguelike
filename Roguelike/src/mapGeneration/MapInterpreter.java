package mapGeneration;

import java.awt.Point;
import java.awt.Rectangle;

import game.Controller;
import game.Map;
import graphics.ImageManager;
import graphics.ImageRegistry;
import entities.StairTile;
import entities.Tile;
import entities.TileFactory;

/**
 * Interprets map created by the Map generator. 
 * Converts a map into a state
 * @author Kevin
 *
 */
public class MapInterpreter {

	private static final int RETRY_COUNT = 100;				//If the retry count exceeds this value, the object being placed is not placed.


	public static Map interpretMap(MapGenerator map, ImageRegistry[] registries){

		if(registries.length < 1){
			throw new IllegalArgumentException("Cannot interpret map with "+ registries.length + " registries.");
		}


		Map newMap = new Map(map.getWidth(), map.getHeight());

		newMap.setPlayerSpawn(map.getPlayerSpawn());

		//for each tile in the map, convert to an entity tile. Images not yet added.
		for(int i= 0; i< map.getWidth(); i++){
			for(int j =0; j < map.getHeight(); j++){

				MapTile tile = map.getTile(i, j);
				Tile newTile = null;

				//go through each case
				if (tile == MapTile.BLANK){
					newTile = TileFactory.makeBlank();
				}
				else if(tile == MapTile.SPACE){
					newTile = TileFactory.makeWall();
				}
				else if((tile == MapTile.CORRIDOR_FLOOR) ||
						(tile == MapTile.ROOM_FLOOR)){

					//any type of floor
					newTile = TileFactory.makeFloor();
				}


				else if (tile == MapTile.PLAYER_SPAWN){
					newTile = TileFactory.makeFloor();
					newMap.setPlayerSpawn(new Point(i, j));
				}
				else if((tile == MapTile.WALL_H) || 
						(tile == MapTile.WALL_V) || 
						(tile == MapTile.WALL_TL_CORNER) || 
						(tile == MapTile.WALL_TR_CORNER) ||
						(tile == MapTile.WALL_BL_CORNER) ||
						(tile == MapTile.WALL_BR_CORNER)){
					//any type of wall
					newTile = TileFactory.makeWall();
				}
				else if((tile == MapTile.DOOR_FRONT) || (tile == MapTile.DOOR_LEFT)|| (tile == MapTile.DOOR_RIGHT)){
					//TODO Doors are passable for now.
					newTile = TileFactory.makeFloor();
				}


				newMap.setTile(i, j, newTile);
			}
		}


		//for each room, decorate with the styles
		for (Rectangle room : map.getRooms()){
			ImageRegistry skin = registries[MapRand.randInt(registries.length-1)];

			//paint each room with the appropriate tile
			for(int i= room.x; i< room.x + room.width; i++){
				for(int j =room.y; j < room.y + room.height; j++){
					MapTile tile = map.getTile(i, j);
					Tile stateTile = newMap.getTile(i, j);
					if (tile == MapTile.BLANK){}
					else if(tile == MapTile.SPACE){
						stateTile.setBackground(skin.getTile("space"));
					}
					else if(tile == MapTile.CORRIDOR_FLOOR){
						stateTile.setBackground(skin.getTile("floor"));
					}
					else if(tile == MapTile.WALL_H){
						stateTile.setBackground(skin.getTile("frontwall" + MapRand.randInt(1, skin.keyCount("frontwall"))));
					}
					else if(tile == MapTile.WALL_V){
						//check if left wall
						if(i == room.x){
							//left wall
							stateTile.setBackground(skin.getTile("leftwall" + MapRand.randInt(1, skin.keyCount("leftwall"))));
						}
						else{
							//right wall
							stateTile.setBackground(skin.getTile("rightwall" + MapRand.randInt(1, skin.keyCount("rightwall"))));
						}
					} 	
					else if(tile == MapTile.WALL_TL_CORNER){
						stateTile.setBackground(skin.getTile("topleftcorner"));
					} 
					else if(tile == MapTile.WALL_TR_CORNER){
						stateTile.setBackground(skin.getTile("toprightcorner"));
					} 
					else if(tile == MapTile.WALL_BL_CORNER){
						stateTile.setBackground(skin.getTile("bottomleftcorner"));
					} 
					else if(tile == MapTile.WALL_BR_CORNER){
						stateTile.setBackground(skin.getTile("bottomrightcorner"));
					}			
					else if(tile == MapTile.ROOM_FLOOR){
						stateTile.setBackground(skin.getTile("floor"));
					} 
					else if(tile == MapTile.DOOR_FRONT){
						stateTile.setBackground(skin.getTile("frontdoorclosed" + MapRand.randInt(1, skin.keyCount("frontdoorclosed"))));
					}
					else if(tile == MapTile.DOOR_LEFT){
						stateTile.setBackground(skin.getTile("leftdoorclosed" + MapRand.randInt(1, skin.keyCount("leftdoorclosed"))));
					}
					else if(tile == MapTile.DOOR_RIGHT){
						stateTile.setBackground(skin.getTile("rightdoorclosed" + MapRand.randInt(1, skin.keyCount("rightdoorclosed"))));
					}
				}
			}

			decorateRoom(map, newMap, registries, room);

		}

		//fill the rest of the map with tiles. These are non room tiles supposedly

		ImageRegistry skin = registries[MapRand.randInt(registries.length-1)];

		//paint each room with the appropriate tile
		for(int i= 0; i< map.getWidth(); i++){
			for(int j =0; j < map.getHeight(); j++){
				MapTile tile = map.getTile(i, j);
				Tile stateTile = newMap.getTile(i, j);
				if (tile == MapTile.BLANK){
					stateTile.setBackground(ImageManager.getGlobalRegistry().getTile("blank"));
				}
				else if(tile == MapTile.SPACE){
					stateTile.setBackground(ImageManager.getGlobalRegistry().getTile("space"));
				}
				else if(tile == MapTile.CORRIDOR_FLOOR){
					stateTile.setBackground(skin.getTile("floor"));
				}		
				else if(tile == MapTile.PLAYER_SPAWN){
					stateTile.setBackground(skin.getTile("floor"));
				}
			}
		}

		return newMap;
	}


	/**
	 * This method decorates a room with a number of items
	 * monsters, and other tile artifacts.
	 * Do not add keys for doors here.
	 * @param room An entire room including the walls
	 */
	private static void decorateRoom(MapGenerator map, Map newMap, ImageRegistry[] registries, Rectangle room) {

		double[] probs = {0.1, 0.2, 0.10, 
				0.05, 0.01, 0.3, 
				0.1, 0.04};
		int style = MapRand.randArray(probs);

		if (style == 0){
			//nothing placed in room.
		}
		else if(style == 1){
			//single low tier treasure
			addItemsRoom(map, newMap, room, 1);
		}
		else if(style == 1){
			//single low tier treasure with one monster
			addItemsRoom(map, newMap, room, 1);
			addMonstersRoom(map, newMap, room, 1);
		}
		else if(style == 2){
			//single low tier treasure with two monsters
			addItemsRoom(map, newMap, room, 1);
			addMonstersRoom(map, newMap, room, 2);
		}
		else if(style == 3){
			//single medium tier treasure with two to three monsters
			addItemsRoom(map, newMap, room, 1);
			addMonstersRoom(map, newMap, room, MapRand.randInt(2, 3));
		}
		else if(style == 4){
			//single high tier treasure with three monsters
			addItemsRoom(map, newMap, room, 1);
			addMonstersRoom(map, newMap, room, MapRand.randInt(3,4));
		}
		else if(style == 5){
			//two low tier treasures
			addItemsRoom(map, newMap, room, 1);
			addItemsRoom(map, newMap, room, 1);
		}
		else if(style == 6){
			//single monster
			addMonstersRoom(map, newMap, room, 1);
		}
		else if(style == 7){
			//1-2 monsters
			addMonstersRoom(map, newMap, room, MapRand.randInt(1, 2));
		}
		else if(style == 8){
			//2-4 monsters
			addMonstersRoom(map, newMap, room, MapRand.randInt(2, 4));
		}
		else if(style == 9){
			//four monsters
			addMonstersRoom(map, newMap, room, 4);

		}
		else if(style == 10){


		}
		else if(style == 11){

		}
	}


	/**
	 * Adds a single item to a location. May overlap
	 * other items, but this is okay since they stack.
	 * @param room Entire room including walls
	 * @param tier Item tier
	 */
	private static void addItemsRoom(MapGenerator map, Map newMap, Rectangle room, int tier){

		Rectangle placement = MapRand.innerRectangle(room);

		Point tempPt = MapRand.randPoint(placement);
		int j = 0;

		//get new point if there's already a monster on the tile.
		while ((map.getTile(tempPt.x, tempPt.y) == MapTile.ROOM_FLOOR) && (j < RETRY_COUNT)){
			tempPt = MapRand.randPoint(placement);
			j++;
		}

		//create item and add to map.
		Tile selected = newMap.getTile(tempPt.x, tempPt.y);
		selected.addItem(Controller.getInstance().getRandMapItem(tier));
		selected.setBackground(ImageManager.getGlobalRegistry().getTile("item"));
	}

	/**
	 * Adds monsters into a room randomly without overlap
	 * @param room Entire room including walls
	 * @param count
	 */
	private static void addMonstersRoom(MapGenerator map, Map newMap, Rectangle room, int count){

		Rectangle placement = MapRand.innerRectangle(room);
		for (int i = 0; i < count; i++){
			Point tempPt = MapRand.randPoint(placement);

			int j= 0;
			j = 0;
			//get new point if there's already a monster on the tile.
			while ((map.getTile(tempPt.x, tempPt.y) != MapTile.ROOM_FLOOR) && (j < RETRY_COUNT)){
				tempPt = MapRand.randPoint(placement);
				j++;
			}

			//create monster and add to map.
			Tile selected = newMap.getTile(tempPt.x, tempPt.y);
			selected.setOccupant(Controller.getInstance().getRandMapMonster(0));
			selected.setBackground(ImageManager.getGlobalRegistry().getTile("monster"));
		}
	}

	/**
	 * Links both maps given by a random room staircase.
	 * May fail if no space in the chosen room.
	 * This should only be used
	 * @param map1
	 * @param map2
	 */
	public static void linkMaps(Map map1, Map map2){
		Point stair1;
		Point stair2;

		Rectangle[] rooms1 = map1.getRooms();
		stair1 = MapRand.randPoint(MapRand.innerRectangle(rooms1[MapRand.randInt(rooms1.length -1)]));

		//ensure staircase gets a free space
		while ((map1.getTile(stair1.x, stair1.y).getItemCount() > 0) || (map1.getTile(stair1.x, stair1.y).isOccupied() == true)){
			stair1 = MapRand.randPoint(MapRand.innerRectangle(rooms1[MapRand.randInt(rooms1.length -1)]));
		}



		Rectangle[] rooms2 = map2.getRooms();
		stair2 = MapRand.randPoint(MapRand.innerRectangle(rooms2[MapRand.randInt(rooms2.length -1)]));

		//ensure staircase gets a free space
		while ((map2.getTile(stair2.x, stair2.y).getItemCount() > 0) || (map2.getTile(stair2.x, stair2.y).isOccupied() == true)){
			stair2 = MapRand.randPoint(MapRand.innerRectangle(rooms2[MapRand.randInt(rooms2.length -1)]));
		}


		//create the special staircase link
		map1.setTile(stair1.x, stair1.y, new StairTile(map1, map2, stair1, stair2));
		map2.setTile(stair2.x, stair2.y, new StairTile(map2, map1, stair2, stair1));
	}
	
	
}


/* Template for many functions here. update with new MapTile members please.

  for(int i= 0; i< map.getWidth(); i++){
			for(int j =0; j < map.getHeight(); j++){
				MapTile tile = map.getTile(i, j);
				if (tile == MapTile.BLANK){}
				else if(tile == MapTile.SPACE){}
				else if(tile == MapTile.CORRIDOR_FLOOR){}
				else if(tile == MapTile.WALL_H){}
				else if(tile == MapTile.WALL_V){} 	
				else if(tile == MapTile.WALL_TL_CORNER){} 
				else if(tile == MapTile.WALL_TR_CORNER){} 
				else if(tile == MapTile.WALL_BL_CORNER){} 
				else if(tile == MapTile.WALL_BR_CORNER){}			
				else if(tile == MapTile.ROOM_FLOOR){} 
				else if(tile == MapTile.PLAYER_SPAWN){} 
				else if(tile == MapTile.DOOR_FRONT){}
				else if(tile == MapTile.DOOR_LEFT){}
				else if(tile == MapTile.DOOR_RIGHT){}
			}
		}
 */
