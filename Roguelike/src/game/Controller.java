/*
 * Master controller for the system, the central class which processes requests and asks the appropriate
 * parts of the game for the information needed and sends it back where it has to go.
 */

package game;

import java.util.ArrayList;

import entities.*;
import graphics.ImageManager;
import graphics.ImageRegistry;
import serialization.ItemDuplicator;
import mapGeneration.BSTMap;
import mapGeneration.MapGenerator;
import mapGeneration.MapInterpreter;
import mapGeneration.MapRand;
import mapGeneration.SimpleMap;

import java.awt.Point;
import java.io.*;

public class Controller {
	private Player player;
	private Cursor cursor;
	ArrayList<Food> foods;
	ArrayList<Monster> monsters;
	private ItemDuplicator duplicator;
	private Map map;								//the current map loaded
	private Messenger messenger;
	boolean gameRunning;
	private TimeQueue timeQueue;

	private static Controller global;

	private Controller() { 

		//load the food and monster CSV files.
		foods = new ArrayList<Food>();
		monsters = new ArrayList<Monster>();
		try {
			loadFoods();
			loadMonsters();
		} catch (IOException e) {
			System.err.println("Error reading the data CSV files.");
			e.printStackTrace();
		}

		//prepare duplicator and player
		duplicator = new ItemDuplicator();
		timeQueue = new TimeQueue();
	};

	public static Controller getInstance(){
		if (global == null){
			global = new Controller();
		}
		return global;
	}

	public void setup(Messenger messenger, Player p){
		
		this.messenger = messenger;
		this.player = p;
		
		//create the map.
		createMap();
		for (int row = 0; row < map.getHeight(); row++) {
			for (int column = 0; column < map.getWidth(); column++) {
				if (!player.getLocation().equals(map.getTile(column, row)) && map.getTile(column, row).isOccupied()) {
					Sentient occupant = map.getTile(column, row).getOccupant();
					timeQueue.addEventToQueue(occupant, ((Monster) occupant).getActionCost());
				}
			}
		}
		this.addPlayerEvent(10);
		this.playTurn();
	}
	
	/**
	 * Creates the map for the entire game. Does all linking
	 * between maps and sets the player's spawn when starting.
	 */
	private void createMap(){

		ImageRegistry[] allTiles = ImageManager.getInstance().getAllTileSets("map");


		//create level 1
		MapGenerator map1 = new SimpleMap(20,15,3,3);
		int[] level1Tiles = {0};
		Map m1 = MapInterpreter.interpretMap(map1, registrySubset(allTiles, level1Tiles));

		this.map = m1;


		//create level 2
		MapGenerator map2 = new SimpleMap(20,15,3,3);
		int[] level2Tiles = {1};
		Map m2 = MapInterpreter.interpretMap(map2, registrySubset(allTiles, level2Tiles));

		MapInterpreter.linkMaps(m1, m2);

		//create level 3
		MapGenerator map3 = new SimpleMap(20,15,4,4);
		int[] level3Tiles = {2};
		Map m3 = MapInterpreter.interpretMap(map3, registrySubset(allTiles, level3Tiles));

		MapInterpreter.linkMaps(m2, m3);

		//create level 4
		MapGenerator map4 = new SimpleMap(20,15,4,4);
		int[] level4Tiles = {3};
		Map m4 = MapInterpreter.interpretMap(map4, registrySubset(allTiles, level4Tiles));

		MapInterpreter.linkMaps(m2, m4);

		//create level 5
		MapGenerator map5 = new BSTMap(75,75);
		int[] level5Tiles = {4};
		Map m5 = MapInterpreter.interpretMap(map5, registrySubset(allTiles, level5Tiles));

		MapInterpreter.linkMaps(m3, m5);
		MapInterpreter.linkMaps(m4, m5);

		
		//=================================================================
		//create level 6
		MapGenerator map6 = new BSTMap(75,75);
		int[] level6Tiles = {5};
		Map m6 = MapInterpreter.interpretMap(map6, registrySubset(allTiles, level6Tiles));

		MapInterpreter.linkMaps(m5, m6);
		
		//create level 7
		MapGenerator map7 = new BSTMap(75,75);
		int[] level7Tiles = {6};
		Map m7 = MapInterpreter.interpretMap(map7, registrySubset(allTiles, level7Tiles));

		MapInterpreter.linkMaps(m5, m7);
		
		//create level 8
		MapGenerator map8 = new BSTMap(75,75);
		int[] level8Tiles = {7};
		Map m8 = MapInterpreter.interpretMap(map8, registrySubset(allTiles, level8Tiles));

		MapInterpreter.linkMaps(m5, m8);

		MapInterpreter.linkMaps(m6, m7);
		MapInterpreter.linkMaps(m7, m8);
		
		//===================================================================
		//create level 9
		MapGenerator map9 = new BSTMap(75,75);
		int[] level9Tiles = {8};
		Map m9 = MapInterpreter.interpretMap(map9, registrySubset(allTiles, level9Tiles));

		MapInterpreter.linkMaps(m6, m9);
		MapInterpreter.linkMaps(m7, m9);
		MapInterpreter.linkMaps(m8, m9);
		
		//create level 10
		MapGenerator map10 = new BSTMap(75,75);
		int[] level10Tiles = {9};
		Map m10 = MapInterpreter.interpretMap(map8, registrySubset(allTiles, level10Tiles));

		MapInterpreter.linkMaps(m9, m10);
		
		
		
		//=====================================================
		// Place player on the first map
		Point spawn = m1.getPlayerSpawn();
		m1.getTile(spawn.x, spawn.y).setOccupant(player);

		//setup the display
		messenger.drawMap(m1);
		messenger.updateStatus(playerStatus());
		messenger.centerMap(spawn);



	}

	
	/**
	 * Gets the subset of texture tiles given by the index.
	 * @param superSet
	 * @param start
	 * @param end
	 * @return
	 */
	private ImageRegistry[] registrySubset(ImageRegistry[] superSet, int[] indices){
		ImageRegistry[] subset = new ImageRegistry[indices.length];
		for (int i = 0; i < indices.length; i++){
			subset[i] = superSet[indices[i]];
		}
		
		return subset;
	}
	
	private void loadFoods() throws IOException {
		BufferedReader in = null;
		in = new BufferedReader(new FileReader("src\\itemdata.txt"));
		String line = in.readLine();
		if (!headersMatch(Food.csvHeaders(), line)) {
			System.out.println("Error: Food section is improperly defined in the headers");
			System.exit(0);
		}
		// Check headers
		String food = in.readLine();
		while (food != null) {
			Food newFood = Food.createFoodFromReader(food);
			if (newFood != null)
				foods.add(newFood);
			food = in.readLine();
		}

		in.close();
	}

	private void loadMonsters() throws IOException {
		BufferedReader in = null;
		in = new BufferedReader(new FileReader("src\\monsterdata.txt"));
		String line = in.readLine();
		if (!headersMatch(Monster.csvHeaders(), line)) {
			System.out.println("Error: Monster CSV file is improperly defined in the headers");
			System.exit(0);
		}
		String monster = in.readLine();
		while (monster != null) {
			Monster newMonster = Monster.createMonsterFromReader(monster);
			if (newMonster != null) {
				monsters.add(newMonster);
			}
			monster = in.readLine();
		}
		in.close();
	}

	public void combatTest() {
		Monster testMonster = getRandMapMonster(0);
		System.out.println("A wild " + testMonster.getName() +" appears!");
		while (!testMonster.isDead()) {
			System.out.println("The monster attacks!");
			if (this.monsterAttack(testMonster))
				System.out.println("The monster " + testMonster.getBaseMeleeDescription() + " you!");
			else
				System.out.println("The monster misses!");
			System.out.println(playerStatus());
			System.out.println("The player attacks!");
			if (playerAttack(testMonster)) 
				System.out.println("You hit!");
			else
				System.out.println("You miss!");
		}
		System.out.println("The " + testMonster.getName() + " is slain!");
	}

	// Make sure that the headers of the food section match what we expect
	private boolean headersMatch(String[] headers, String input) {
		String[] inHeaders = input.split(",");
		if (inHeaders.length != headers.length) 
			return false;
		for (int i = 0; i < headers.length; i++) {
			if (!inHeaders[i].equals(headers[i]))
				return false;
		}
		return true;
	}

	public void endGame() {
		gameRunning = false;
		// Handle any serialization or other game ending logic
		System.exit(0);	// Could this be done more smoothly? Not sure
	}

	public Messenger getMessenger() {
		return messenger;
	}

	public String playerEat(Food food) {
		player.reduceHunger(food.getNutrition());
		return food.eatMsg();
	}

	public void movePlayerUp() {
		if (player.getLocation().getRow() > 0) {
			moveSentient(player, 0, -1);
		}
	}

	public void movePlayerDown() {
		if (player.getLocation().getRow() < map.getHeight() - 1) {
			moveSentient(player, 0, 1);
		}
	}

	public void movePlayerRight(){
		if (player.getLocation().getColumn() < map.getWidth() - 1) {
			moveSentient(player, 1, 0);
		}
	}
	
	public void movePlayerLeft(){
		if (player.getLocation().getColumn() > 0) {
			moveSentient(player, -1, 0);
		}
	}
	
	/**
	 * Moves a sentient object in any of the specified directions
	 * Sentient position is then updated on screen and in game state.
	 * @param deltaX
	 * @param deltaY
	 */
	private void moveSentient(Sentient s, int deltaX, int deltaY) {
		Point oldPt = new Point(s.getLocation().getColumn(), s.getLocation().getRow());
		Point newPt = new Point(oldPt.x + deltaX, oldPt.y + deltaY);
		
		Tile nextTile = map.getTile(newPt.x, newPt.y);
		if (nextTile.isPassable() && !nextTile.isOccupied()){
			s.setLocation(nextTile);
			map.getTile(oldPt.x, oldPt.y).removeOccupant();
			map.getTile(newPt.x, newPt.y).setOccupant(s);
			
			//update the tile
			messenger.updateTile(oldPt);
			messenger.updateTile(newPt);
			messenger.centerMap(newPt);
		}
	}
	
	public void createCursor() {
		cursor = new Cursor(player.getLocation());
		Point point = new Point(cursor.getLocation().getColumn(), cursor.getLocation().getRow());
		messenger.drawImage(cursor.getImg(), point);
	}
	
	
	public String select() {
		return cursor.getTopEntity();
	}
	
	
	public void deleteCursor() {
		messenger.updateTile(cursor.getLocation().getColumn(), cursor.getLocation().getRow());
		cursor = null;
	}
	
	
	public void moveCursorUp() {
		if (cursor.getLocation().getRow() > 0) {
			moveCursor(0,-1);
		}
	}
	
	public void moveCursorDown() {
		if (cursor.getLocation().getRow() < map.getHeight() - 1) {
			moveCursor(0,1);
		}
	}
	
	public void moveCursorLeft() {
		if (cursor.getLocation().getColumn() > 0) {
			moveCursor(-1,0);
		}
	}

	
	public void moveCursorRight() {
		if (cursor.getLocation().getColumn() < map.getWidth() - 1) {
			moveCursor(1,0);
		}
	}
	
	
	/**
	 * Moves the cursor in any of the specified directions
	 * Cursor position is then updated on screen and in game state.
	 * @param deltaX
	 * @param deltaY
	 */
	private void moveCursor(int deltaX, int deltaY){
		Point oldPt = new Point(cursor.getLocation().getColumn(), cursor.getLocation().getRow());
		Point newPt = new Point(oldPt.x + deltaX, oldPt.y + deltaY);
		
		Tile nextTile = map.getTile(newPt.x, newPt.y);
		cursor.setLocation(nextTile);
		
		messenger.updateTile(oldPt);
		messenger.drawImage(cursor.getImg(), newPt);
	}
	
	
	public boolean monsterAttack(Monster monster) {
		int attackRoll = MapRand.randInt(20) + monster.getAttack();
		if (attackRoll >= player.getAC()) {
			player.takeDamage(monster.getMeleeDamage());
			return true;
		} else {
			return false;
		}
	}

	public boolean playerAttack(Monster monster) {
		int attackRoll = MapRand.randInt(20) + player.getAttack();
		if (attackRoll >= monster.getAC()) {
			monster.takeDamage(player.getMeleeDamage());
			return true;
		} else {
			return false;
		}
	}

	public String playerStatus() {
		return "Player: HP = " + player.getCurrentHP() + ", Strength = " + player.getStrength() + ", Dexterity = " + player.getDexterity();
	}

	// Return a random item for the map, given the current depth in the station
	// Currently just returns one of our foods randomly.
	public Holdable getRandMapItem(int mapIndex) {
		int randomIndex = MapRand.randInt(foods.size() - 1);
		return (Holdable)duplicator.duplicate(foods.get(randomIndex));
	}

	public Monster getRandMapMonster(int mapIndex) {
		int randomIndex = MapRand.randInt(monsters.size() - 1);
		return (Monster)duplicator.duplicate(monsters.get(randomIndex));
	}
	
	public void addPlayerEvent(int actionCost) {
		timeQueue.addEventToQueue(player, actionCost / player.getSpeed());
	}
	
	public void playTurn() {
		Sentient topEventSentient = timeQueue.getNextEvent();
		while (!topEventSentient.equals(player)) {
			System.out.println(topEventSentient.getName() + " takes an action");
			timeQueue.addEventToQueue(topEventSentient, ((Monster) topEventSentient).getActionCost());
			topEventSentient = timeQueue.getNextEvent();
		}
	}
}
