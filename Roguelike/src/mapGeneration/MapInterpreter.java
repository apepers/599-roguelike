package mapGeneration;

import java.awt.Rectangle;
import java.rmi.registry.Registry;

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

		if(registries.length < 1){
			throw new IllegalArgumentException("Cannot interpret map with "+ registries.length + " registries.");
		}
		
		
		Map newMap = new Map(map.getWidth(), map.getHeight());

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
				else if(tile == MapTile.DOOR){
					//TODO Doors are passable for now.
					newTile = TileFactory.makeFloor();
				}


				newMap.setTile(i, j, newTile);
			}
		}


		//for each room, decorate with the styles
		for (Rectangle room : map.getRooms()){
			ImageRegistry skin = registries[0];
			
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
					else if(tile == MapTile.WALL_H){}
					else if(tile == MapTile.WALL_V){} 	
					else if(tile == MapTile.WALL_TL_CORNER){} 
					else if(tile == MapTile.WALL_TR_CORNER){} 
					else if(tile == MapTile.WALL_BL_CORNER){} 
					else if(tile == MapTile.WALL_BR_CORNER){}			
					else if(tile == MapTile.ROOM_FLOOR){} 
					else if(tile == MapTile.PLAYER_SPAWN){} 
					else if(tile == MapTile.DOOR){}
				}
			}
			
			
			
		}
		


		return null;
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
				else if(tile == MapTile.DOOR){}
			}
		}
 */
