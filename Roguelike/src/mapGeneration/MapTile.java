package mapGeneration;


/**
 * Describes the language used throughout the map generation.
 * Do not use outside mapGeneration package
 * @author Kevin
 *
 */
public enum MapTile {

	BLANK, WALL, FLOOR, PLAYER, DOOR;



	public String toString(){
		switch (this){
		case BLANK:
			return " ";
		case FLOOR:
			return ".";
		case WALL:
			return "#";
		case PLAYER:
			return "@";
		case DOOR:
			return "+";
		default:
			return "ERROR";
		}

	}

}
