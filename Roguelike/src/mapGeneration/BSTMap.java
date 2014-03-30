package mapGeneration;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import mapGeneration.MapRand.RectangleSide;

/** 
 * Uses the algorithm taken from:
 * http://www.roguebasin.com/index.php?title=Basic_BSP_Dungeon_generation
 * 
 * To generate simple dungeons with straight corridors and
 * a heirarchial structure.
 * 
 * Minimum room size is at least 25*depth of the map
 * @author Kevin
 *
 */
public class BSTMap extends MapGenerator {


	private static final double ROOM_SPLIT_FACTOR = 0.4;		//modifies the split boundary between successive subdivisions for rooms (0.5 is the max value)
	private static final int AREA_PADDING = 3;					//modifies the minmum number of empty spaces between each room.
	private static final int MIN_ROOM_WIDTH = 4;				//minimum room width
	private static final int MIN_ROOM_HEIGHT = 4;				//minimum room height

	private static final double HIDDEN_ROOM_PROB = 0.0;


	private static final int DEPTH_FACTOR = 25;					//modifies the  minimum room size for a certain depth. Change if errors occur.

	private int width;
	private int height;

	private int depth = 5;
	private ArrayList<Rectangle> areas;


	/**
	 * Creates a default BST map using the recommended
	 * depth setting
	 * @param width
	 * @param height
	 */
	public BSTMap(int width, int height) {
		super(width, height);


		this.width = width;
		this.height = height;

		depth = Math.min(height, width) /DEPTH_FACTOR;

		this.areas = new ArrayList<Rectangle>(2^depth);
		generateMap();

	}

	public BSTMap(int width, int height, int depth){
		super(width, height);


		this.depth = depth;
		this.width = width;
		this.height = height;

		this.areas = new ArrayList<Rectangle>(2^depth);

		generateMap();
	}

	@Override
	/**
	 * Creates a BST structured level.
	 */
	protected void generateMap() {

		BST head = new BST(null, null, new Rectangle(width-1, height-1));

		subDivide(head, depth);

		//picks a random point to be the spawn point of the player.
		Rectangle[] allRooms = super.getRooms();
		Point spawn = MapRand.randPoint(MapRand.innerRectangle(allRooms[MapRand.randInt(allRooms.length-1)]));
		super.setPlayerSpawn(spawn);
		super.writeTile(spawn.x, spawn.y, MapTile.PLAYER_SPAWN);

	}

	/**
	 * Extends a BST tree by subdividing the rectangles randomly.
	 * Expects a node with both null children, but with a specified rectangle
	 * @param node
	 * @return Null if the room is hidden (does not exist due to size or randomness). 
	 * Returns a room that has been filled on the map otherwise.
	 */
	private Rectangle subDivide(BST node, int depth){

		if (depth == 0){
			//leaf nodes, randomly generate rectangles in this area
			areas.add(node.area);


			/*
			Point roomLocation = MapRand.randPoint(new Rectangle(node.area.x + AREA_PADDING, node.area.y + AREA_PADDING, Math.max(node.area.width/6,1), Math.max(node.area.height /6, 1)));		//pick point from first sixth
			int width = MapRand.randInt((int) (Math.abs(roomLocation.x - (node.area.x + node.area.width)) * FILL_SCALE), Math.abs(roomLocation.x - (node.area.x + node.area.width)) - AREA_PADDING);
			int height = MapRand.randInt((int) (Math.abs(roomLocation.y - (node.area.y + node.area.height)) * FILL_SCALE), Math.abs(roomLocation.y - (node.area.y + node.area.height)) - AREA_PADDING);
			Rectangle room = new Rectangle(roomLocation.x, roomLocation.y, Math.max(width, MIN_ROOM_WIDTH), Math.max(height, MIN_ROOM_HEIGHT));
			 */

			//randomly determine if the room is hidden or not.
			if(MapRand.randBool(HIDDEN_ROOM_PROB) == false){
				//create room within this area by the given padding
				Rectangle room = node.area;
				for(int i = 0; i < AREA_PADDING ; i++){
					room = MapRand.innerRectangle(room);
				}

				//create the room if its in specifications, otherwise its a hidden room.
				if((room.width >= MIN_ROOM_WIDTH) && (room.height >= MIN_ROOM_HEIGHT)){
					super.addRoom(room);
					//fill each room on top of corridors
					super.fillRoom(room);
					return room;
				}
			}
			
			
			//hidden room
			Point intersect = MapRand.randPoint(MapRand.innerRectangle(node.area));
			return new Rectangle(intersect.x,intersect.y,1,1);		//returns the node area as the hidden room, this room is a single point as to connect empty rooms
		}
		else{
			Rectangle original = node.area;				//the rectangle of this partition
			Rectangle r1;
			Rectangle r2;

			boolean horizontal = MapRand.randBool((original.width < original.height) ? 0.75: 0.25);
			if (horizontal){
				//horizontal split chosen
				int fraction = (int) (original.height * ROOM_SPLIT_FACTOR);
				int upperHeight = MapRand.randInt(fraction, original.height - fraction);
				int lowerHeight = original.height - upperHeight;

				r1 = new Rectangle(original.x, original.y, original.width, upperHeight);					//top
				r2 = new Rectangle(original.x, original.y + upperHeight, original.width, lowerHeight);		//bottom
			}
			else{
				//vertical split chosen
				int fraction = (int) (original.width * ROOM_SPLIT_FACTOR);
				int leftWidth = MapRand.randInt(fraction, original.width - fraction);
				int rightWidth = original.width - leftWidth;

				r1 = new Rectangle (original.x, original.y, leftWidth, original.height);					//left
				r2 = new Rectangle (original.x + leftWidth, original.y, rightWidth, original.height);		//right
			}

			node.left = new BST(null, null, r1);
			node.right = new BST(null, null, r2);

			//Continue to subdivide.
			Rectangle roomLeft = subDivide(node.left, depth-1);
			Rectangle roomRight = subDivide(node.right, depth-1);

			//Connect each room to its sibling

			if (horizontal){
				//horizontal split chosen, r1 is top, r2 is bottom
				Rectangle top = MapRand.innerRectangle(roomLeft);
				Rectangle bottom = MapRand.innerRectangle(roomRight);

				Point start = MapRand.randRectEdge(top, RectangleSide.BOTTOM);
				Point end =  MapRand.randRectEdge(bottom, RectangleSide.TOP);
				Point mid = new Point(MapRand.randInt(r2.x, r2.x + r2.width-1), r2.y);


				super.fillCorridor(start, mid, end, MapTile.CORRIDOR_FLOOR, false);


			}
			else{
				//vertical split, r1 is left, r2 is bottom
				Rectangle left = MapRand.innerRectangle(roomLeft);
				Rectangle right = MapRand.innerRectangle(roomRight);


				Point start = MapRand.randRectEdge(left, RectangleSide.RIGHT);
				Point end =  MapRand.randRectEdge(right, RectangleSide.LEFT);
				Point mid = new Point(r2.x, MapRand.randInt(r2.y, r2.y + r2.height-1));


				super.fillCorridor(start, mid, end, MapTile.CORRIDOR_FLOOR, true);
			}

			return (MapRand.randBool() == true) ? roomLeft : roomRight;

		}

	}


	private class BST{
		private BST left;
		private BST right;
		private Rectangle area;

		/**
		 * Intializes the tree. Children may be null.
		 * Must contain a rectangle.
		 * @param left
		 * @param right
		 */
		private BST(BST left, BST right, Rectangle r){
			this.area = r;
			this.left = left;
			this.right = right;
		}

	}
}
