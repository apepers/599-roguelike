package mapGeneration;


/**
 * Describes the language used throughout the map generation.
 * Do not use outside mapGeneration package
 * @author Kevin
 *
 */
public enum MapTile {

	BLANK, 																	//null tile.
	SPACE,																	//Typically background tiles.
	WALL_H, WALL_V, 														//horizonal/vertical walls.
	WALL_TL_CORNER, WALL_TR_CORNER, WALL_BL_CORNER, WALL_BR_CORNER,			//wall corners
	ROOM_FLOOR, CORRIDOR_FLOOR,
	PLAYER_SPAWN, 
	DOOR_FRONT, DOOR_RIGHT, DOOR_LEFT,
	ITEM, MONSTER;



	public String toString(){
		switch (this){
		case BLANK:
			return " ";
		case SPACE:
			return " ";
		case ROOM_FLOOR:
			return ".";
		case CORRIDOR_FLOOR:
			return "_";
		case WALL_H:
			return "=";
		case WALL_V:
			return "|";
		case WALL_TL_CORNER:
			return "/";
		case WALL_TR_CORNER:
			return "\\";
		case WALL_BL_CORNER:
			return "\\";
		case WALL_BR_CORNER:
			return "/";
		case PLAYER_SPAWN:
			return "@";
		case DOOR_FRONT:
			return "+";
		case DOOR_LEFT:
			return "+";
		case DOOR_RIGHT:
			return "+";
		case ITEM:
			return "$";
		case MONSTER:
			return "M";
		default:
			return "ERROR";
		}

	}

}
