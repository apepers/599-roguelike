package mapGeneration;

import graphics.ImageRegistry;

import java.util.Map;

/**
 * Interprets map created by the Map generator. 
 * Converts a map into a state
 * @author Kevin
 *
 */
public class MapInterpreter {

	public static Map interpretMap(MapGenerator map){

		for (MapTile tile : map){
			if (tile == MapTile.BLANK){

			}
			else if(tile == MapTile.CORRIDOR_FLOOR){}
			else if(tile == MapTile.WALL_H){}
			else if(tile == MapTile.WALL_V){} 									//horizonal/vertical walls.
			else if(tile == MapTile.WALL_TL_CORNER){} 
			else if(tile == MapTile.WALL_TR_CORNER){} 
			else if(tile == MapTile.WALL_BL_CORNER){} 
			else if(tile == MapTile.WALL_BR_CORNER){}			//wall corners
			else if(tile == MapTile.ROOM_FLOOR){} 
			else if(tile == MapTile.CORRIDOR_FLOOR){}
			else if(tile == MapTile.PLAYER_SPAWN){} 
			else if(tile == MapTile.DOOR){}
		}
		return null;
	}
}
