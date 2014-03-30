package mapGeneration;

import game.Map;
import graphics.ImageRegistry;
import entities.TileFactory;
import entities.Tile;

/**
 * Interprets map created by the Map generator. 
 * Converts a map into a state
 * @author Kevin
 *
 */
public class MapInterpreter {

	public static Map interpretMap(MapGenerator map, ImageRegistry[] registries){

		Map newMap = new Map(map.getWidth(), map.getHeight());
		
		//for each tile in the map, convert to an entity tile.
		for(int i= 0; i< map.getWidth(); i++){
			for(int j =0; j < map.getHeight(); j++){
				MapTile tile = map.getTile(i, j);
				Tile newTile = null;
				if (tile == MapTile.BLANK){
					newTile = TileFactory.makeBlank();
				}
				else if(tile == MapTile.SPACE){
					newTile = TileFactory.makeWall();
				}
				else if((tile == MapTile.CORRIDOR_FLOOR) ||
						(tile == MapTile.ROOM_FLOOR) ||
						(tile == MapTile.PLAYER_SPAWN)){
					//any type of floor
					newTile = TileFactory.makeFloor();
					
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
				else if(tile == MapTile.DOOR){}
				
				
				newMap.setTile(i, j, newTile);
			}
		}
		for (MapTile tile : map){
			if (tile == MapTile.BLANK){
				
			}
			else if(tile == MapTile.SPACE){}
			else if(tile == MapTile.CORRIDOR_FLOOR){}
			else if(tile == MapTile.WALL_H){}
			else if(tile == MapTile.WALL_V){} 									//horizonal/vertical walls.
			else if(tile == MapTile.WALL_TL_CORNER){} 
			else if(tile == MapTile.WALL_TR_CORNER){} 
			else if(tile == MapTile.WALL_BL_CORNER){} 
			else if(tile == MapTile.WALL_BR_CORNER){}			//wall corners
			else if(tile == MapTile.ROOM_FLOOR){} 
			else if(tile == MapTile.PLAYER_SPAWN){} 
			else if(tile == MapTile.DOOR){}
		}
		return null;
	}
}
