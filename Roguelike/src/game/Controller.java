/*
 * Master controller for the system, the central class which processes requests and asks the appropriate
 * parts of the game for the information needed and sends it back where it has to go.
 */

package game;

import java.util.ArrayList;
import java.util.HashMap;

import entities.*;
import graphics.ImageManager;
import graphics.ImageRegistry;
import serialization.ItemDuplicator;
import mapGeneration.BSTMap;
import mapGeneration.MapGenerator;
import mapGeneration.MapInterpreter;
import mapGeneration.MapRand;
import mapGeneration.SimpleMap;

import java.awt.GridLayout;
import java.awt.Point;
import java.io.*;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Controller {
	private Player player;
	private Cursor cursor;
	ArrayList<Food> foods;
	ArrayList<Monster> monsters;
	ArrayList<Weapon> weapons;
	ArrayList<Armour> armours;
	private ItemDuplicator duplicator;

	private Map map;								//the current map loaded

	private Messenger messenger;
	boolean gameRunning;
	private TimeQueue timeQueue;

	private static Controller global;

	private Controller() { 

		//load the food and monster CSV files.
		foods = new ArrayList<Food>();
		weapons = new ArrayList<Weapon>();
		monsters = new ArrayList<Monster>();
		armours = new ArrayList<Armour>();
		try {
			loadFoods();
			addFoodDescriptions();
			loadWeapons();
			addWeaponDescriptions();
			loadArmours();
			addArmourDescriptions();
			loadMonsters();
			addMonsterDescriptions();
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
		for (Food f : foods) {
			if (f.getName().equals("spice"))
				p.addItem((Holdable)duplicator.duplicate(f));
		}

		//create the map.
		createMap();
		resetTimeQueue();
		fieldOfView(true);
	}

	
	/**
	 * Creates the map for the entire game. Does all linking
	 * between maps and sets the player's spawn when starting.
	 */
	private void createMap(){

		ImageRegistry[] allTiles = ImageManager.getInstance().getAllTileSets("map");

		//create space icons
		ImageIcon[] lavas = { 
				ImageManager.getGlobalRegistry().getTile("lava1"),
				ImageManager.getGlobalRegistry().getTile("space5"),
				ImageManager.getGlobalRegistry().getTile("lava2")};
		ImageIcon[] ices = {
				ImageManager.getGlobalRegistry().getTile("ice1"), 
				ImageManager.getGlobalRegistry().getTile("space5"),
				ImageManager.getGlobalRegistry().getTile("ice2")};

		//create level 1
		MapGenerator map1 = new SimpleMap(20,15,3,3);
		int[] level1Tiles = {1};
		Map m1 = MapInterpreter.interpretMap(map1, registrySubset(level1Tiles), 1);

		this.map = m1;


		//create level 2
		MapGenerator map2 = new SimpleMap(20,15,3,3);
		int[] level2Tiles = {8};
		Map m2 = MapInterpreter.interpretMap(map2, registrySubset(level2Tiles), 1);
		m2.setTag("Chapter 1");
		
		MapInterpreter.linkMaps(m1, m2);

		//create level 3
		MapGenerator map3 = new SimpleMap(20,15,4,4);
		int[] level3Tiles = {17};
		Map m3 = MapInterpreter.interpretMap(map3, registrySubset(level3Tiles), 1);
		
		MapInterpreter.linkMaps(m2, m3);

		//create level 4
		MapGenerator map4 = new SimpleMap(20,15,4,4);
		int[] level4Tiles = {4};
		Map m4 = MapInterpreter.interpretMap(map4, registrySubset(level4Tiles), 1);

		MapInterpreter.linkMaps(m2, m4);

		//create level 5
		MapGenerator map5 = new BSTMap(75,75,4);
		int[] level5Tiles = {12};
		Map m5 = MapInterpreter.interpretMap(map5, registrySubset(level5Tiles), 2);
		m5.setTag("Chapter 2");
		
		MapInterpreter.linkMaps(m3, m5);
		MapInterpreter.linkMaps(m4, m5);


		//=================================================================
		//create level 6
		MapGenerator map6 = new BSTMap(75,75,4);
		int[] level6Tiles = {6};
		Map m6 = MapInterpreter.interpretMap(map6, registrySubset(level6Tiles), lavas, false, 2);
		m6.setTag("Chapter 3a");
		
		MapInterpreter.linkMaps(m5, m6);

		//create level 7
		MapGenerator map7 = new BSTMap(75,75,4);
		int[] level7Tiles = {21, 23};
		Map m7 = MapInterpreter.interpretMap(map7, registrySubset(level7Tiles), 2);
		
		MapInterpreter.linkMaps(m5, m7);

		//create level 8
		MapGenerator map8 = new BSTMap(75,75,4);
		int[] level8Tiles = {9};
		Map m8 = MapInterpreter.interpretMap(map8, registrySubset(level8Tiles), ices, false, 2);
		m8.setTag("Chapter 3b");
		
		MapInterpreter.linkMaps(m5, m8);

		MapInterpreter.linkMaps(m6, m7);
		MapInterpreter.linkMaps(m7, m8);

		//===================================================================
		//create level 9
		MapGenerator map9 = new BSTMap(90,90,4);
		int[] level9Tiles = {21, 23, 7};
		Map m9 = MapInterpreter.interpretMap(map9, registrySubset(level9Tiles), 3);

		MapInterpreter.linkMaps(m6, m9);
		MapInterpreter.linkMaps(m7, m9);
		MapInterpreter.linkMaps(m8, m9);

		//create level 10
		int[] level10Tiles = {13};
		FinalMap finalMap = new FinalMap(registrySubset(level10Tiles)[0]);
		Map m10 = finalMap.initMap();
		finalMap.linkRoom(m9);
		m10.setTag("Final Chapter");



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
	private ImageRegistry[] registrySubset(int[] indices){
		ImageRegistry[] subset = new ImageRegistry[indices.length];
		for (int i = 0; i < indices.length; i++){
			subset[i] = ImageManager.getInstance().getTileSet("map" + indices[i]);
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
	
	private void loadWeapons() throws IOException {
		BufferedReader in = null;
		in = new BufferedReader(new FileReader("src\\weapondata.txt"));
		String line = in.readLine();
		if (!headersMatch(Weapon.csvHeaders(), line)) {
			System.out.println("Error: Weapon section is improperly defined in the headers");
			System.exit(0);
		}
		// Check headers
		String weapon = in.readLine();
		while (weapon != null) {
			Weapon newWeapon = Weapon.createWeaponFromReader(weapon);
			if (newWeapon != null)
				weapons.add(newWeapon);
			weapon = in.readLine();
		}

		in.close();
	}
	
	private void loadArmours() throws IOException {
		BufferedReader in = null;
		in = new BufferedReader(new FileReader("src\\armourdata.txt"));
		String line = in.readLine();
		if (!headersMatch(Armour.csvHeaders(), line)) {
			System.out.println("Error: Armour section is improperly defined in the headers");
			System.exit(0);
		}
		// Check headers
		String armour = in.readLine();
		while (armour != null) {
			Armour newArmour = Armour.createArmourFromReader(armour);
			if (newArmour != null)
				armours.add(newArmour);
			armour = in.readLine();
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


	/*
	 * Add descriptions and quotes to already existing food
	 */
	private void addFoodDescriptions() throws IOException {
		HashMap<String, String> descMap = new HashMap<String, String>();
		descMap = parseDescriptionFile("src\\itemquotes.txt");

		for(Food food : foods) {
			String name = food.getName().toLowerCase();
			if(descMap.containsKey(name)) {
				food.setDescription(descMap.get(name));
			}
		}
	}

	/*
	 * Add descriptions and quotes to already existing weapons
	 */
	private void addWeaponDescriptions() throws IOException {
		HashMap<String, String> descMap = new HashMap<String, String>();
		descMap = parseDescriptionFile("src\\weaponquotes.txt");

		for(Weapon weapon : weapons) {
			String name = weapon.getName().toLowerCase();
			if(descMap.containsKey(name)) {
				weapon.setDescription(descMap.get(name));
			}
		}
	}
	
	/*
	 * Add descriptions and quotes to already existing weapons
	 */
	private void addArmourDescriptions() throws IOException {
		HashMap<String, String> descMap = new HashMap<String, String>();
		descMap = parseDescriptionFile("src\\armourquotes.txt");

		for(Armour armour : armours) {
			String name = armour.getName().toLowerCase();
			if(descMap.containsKey(name)) {
				armour.setDescription(descMap.get(name));
			}
		}
	}
	
	/*
	 * Add descriptions and quotes to already existing monsters
	 */
	private void addMonsterDescriptions() throws IOException {
		HashMap<String, String> descMap = new HashMap<String, String>();
		descMap = parseDescriptionFile("src\\monsterquotes.txt");

		for(Monster monster : monsters) {
			String name = monster.getName().toLowerCase();
			if(descMap.containsKey(name)) {
				monster.setDescription(descMap.get(name));
			}
		}
	}


	/*
	 * Parse through a given description/quotes .txt file and
	 * convert it into a HashMap with the Entity name as the key
	 */
	private HashMap<String, String> parseDescriptionFile(String filename) throws IOException {
		HashMap<String, String> descMap = new HashMap<String, String>();
		BufferedReader in = null;
		in = new BufferedReader(new FileReader(filename));
		String key = "";
		String value = "";

		String line = in.readLine();
		while (line != null) {
			//This line should always be the monster name.
			key = line.substring(2).trim().toLowerCase();
			value = "";
			line = in.readLine();
			while((line != null) && (!line.startsWith("##"))) {
				if(line.startsWith("^^")) {
					value += line.substring(2).trim();
				} else {
					value += line + "\n";
				}
				line = in.readLine();
			}
			descMap.put(key, value);
		}
		in.close();
		return descMap;
	}

	public void resetTimeQueue() {
		timeQueue.clear();
		Monster[] monsters = map.getMonsters();
		for (int i = 0; i < monsters.length; i++) {
			timeQueue.addEventToQueue(monsters[i], monsters[i].getActionCost());
		}
		this.addPlayerEvent(10);
		this.playTurn();
	}

	
	
	public void combatTest() {
		Monster testMonster = getRandMapMonster(0);
		System.out.println("A wild " + testMonster.getName() +" appears!");
		while (!testMonster.isDead()) {
			System.out.println("The monster attacks!");
			if (sentientAttack(testMonster, player))
				System.out.println("The monster " + testMonster.getBaseMeleeDescription() + " you!");
			else
				System.out.println("The monster misses!");
			System.out.println(playerStatus());
			System.out.println("The player attacks!");
			if (sentientAttack(player, testMonster))
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

	/**
	 * Begins the game
	 */
	public void startGame(){
		messenger.showTextDialog(GameText.getText("intro"), "Welcome to Severed Space!");
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
		player.increaseNutrition(food.getNutrition());
		food.eatEffects(player);
		updatePlayerStatus();
		return "You eat the " + food.properName()+ ".\n" + food.eatMsg();
	}

	public void movePlayerUp() {
		if (!player.isDrunk()) {
			if (player.getLocation().getRow() > 0) {
				moveSentient(player, 0, -1);
			}
		} else {
			moveRandomly(player);
		}
	}

	public void movePlayerDown() {
		if (!player.isDrunk()) {
			if (player.getLocation().getRow() < map.getHeight() - 1) {
				moveSentient(player, 0, 1);
			}
		} else {
			moveRandomly(player);
		}
	}

	public void movePlayerRight(){
		if (!player.isDrunk()) {
			if (player.getLocation().getColumn() < map.getWidth() - 1) {
				moveSentient(player, 1, 0);
			}
		} else {
			moveRandomly(player);
		}
	}

	public void movePlayerLeft(){
		if (!player.isDrunk()) {
			if (player.getLocation().getColumn() > 0) {
				moveSentient(player, -1, 0);
			}
		} else {
			moveRandomly(player);
		}
	}
	
	
	public boolean lineOfSight(Sentient source, Tile destination) {
		int x = source.getLocation().getColumn();
		int y = source.getLocation().getRow();
		int deltaX = destination.getColumn() - x;
		int deltaY = destination.getRow() - y;
		int absX = Math.abs(deltaX) * 2;
		int absY = Math.abs(deltaY) * 2;
		int signX = (deltaX < 0) ? -1 : ((deltaX > 0) ? 1 : 0);
		int signY = (deltaY < 0) ? -1 : ((deltaY > 0) ? 1 : 0);
		int t;

		if ((absX + absY) > (source.getSightRange() * 2))
			return false;
		
		// If the line between the two locations is x dominant
		if(absX >= absY) {
			t = absY - (absX / 2);
			// Each loop iteration steps one closer to the desination
			do {
				if((t >= 0) && ((t != 0) || (signX > 0))) {
					y += signY;
					t -= absX;
				}
				x += signX;
				t += absY;
				
				if( (x == destination.getColumn()) && (y == destination.getRow())) {
					//System.out.println(source.getName() + " sees you!");
					return true;
				}
			} while(map.getTile(x, y).isPassable());
			
			return false;
		
		// Else the line between the two locations is Y dominant
		} else {
			t = absX - (absY / 2);
			// Each loop iteration steps one closer to the desination
			do {
				if((t >= 0) && ((t != 0) || (signX > 0))) {
					x += signX;
					t -= absY;
				}
				y += signY;
				t += absX;
				
				if( (x == destination.getColumn()) && (y == destination.getRow())) {
					//System.out.println(source.getName() + " sees you!");
					return true;
				}
			} while(map.getTile(x, y).isPassable());
			
			return false;
		}
	}
	
	
	public void fieldOfView(boolean visible) {
		Point start = new Point(player.getLocation().getColumn() - player.getSightRange(), player.getLocation().getRow() - player.getSightRange());
		for(int i = 0; i < player.getSightRange() * 2; i++) {
			for(int j = 0; j < player.getSightRange() * 2; j++) {
				Point target = new Point(start.x + i, start.y + j);
				if(map.boundaryCheck(target)) {
					if(lineOfSight(player, map.getTile(target))) {
						Tile tile = map.getTile(target);
						tile.setDiscovered(true);
						tile.setVisible(visible);
						if(tile.getOccupant() != null)
							tile.getOccupant().setInSight(visible);
						messenger.updateTile(target);
						//messenger.drawImage(ImageManager.getGlobalRegistry().getTile("fog"), target);
					}
				}
			}
		}
	}
	

	public void moveRandomly(Sentient s) {
		ArrayList<Point> directions = new ArrayList<Point>(4);
		Tile location = s.getLocation();
		
		if (location.getRow() > 0)
			directions.add(new Point(0, -1));
		if (location.getRow() < map.getHeight() - 1)
			directions.add(new Point(0, 1));
		if (location.getColumn() < map.getWidth() - 1)
			directions.add(new Point(1, 0));
		if (location.getColumn() > 0)
			directions.add(new Point(-1, 0));
		int random = MapRand.randInt(directions.size() - 1);
		moveSentient(s, directions.get(random).x, directions.get(random).y);
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
			if (s.equals(player))
				messenger.centerMap(newPt);
		} else if (nextTile.isOccupied()) {
			Sentient occupant = nextTile.getOccupant();
			if (sentientAttack(s, occupant)) {
				if (s.equals(player)) {
					if (occupant.isDead()) {
						messenger.println(occupant.getPronoun() + " is slain!");
						player.giveXP(((Monster)occupant).getDifficulty() * 100);
						map.removeMonster((Monster)occupant);
						timeQueue.removeSentient(occupant);
						messenger.updateTile(newPt);
					}
					player.incrementStrength();
				} else {
					if (occupant.isDead() && !occupant.equals(player)) {
						if(occupant.isInSight())
							messenger.println(occupant.getPronoun() + " is slain!");
						map.removeMonster((Monster)occupant);
						timeQueue.removeSentient(occupant);
						messenger.updateTile(newPt);
					}
				}
				updatePlayerStatus();
			} else {
				if (occupant.equals(player)) {
					player.incrementDexterity();
				}
			}
			updatePlayerStatus();
		}
	}

	public void createCursor() {
		cursor = new Cursor(player.getLocation());
		Point point = new Point(cursor.getLocation().getColumn(), cursor.getLocation().getRow());
		messenger.drawImage(cursor.getImg(), point);
	}


	public Entity select() {
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

	public void stairsUp(){
		Point stairLoc = new Point(player.getLocation().getColumn(), player.getLocation().getRow());
		Tile currentTile = map.getTile(stairLoc.x, stairLoc.y);
		if(currentTile instanceof StairTile){
			StairTile stairs = (StairTile) currentTile;

			//switch maps
			switchMap(stairs);
		}
		else{
			messenger.println("There are no stairs to go up here.");
		}
	}

	public void stairsDown(){
		Point stairLoc = new Point(player.getLocation().getColumn(), player.getLocation().getRow());
		Tile currentTile = map.getTile(stairLoc.x, stairLoc.y);
		if(currentTile instanceof StairTile){
			StairTile stairs = (StairTile) currentTile;

			//switch maps
			switchMap(stairs);
		}
		else{
			messenger.println("There are no stairs to go down here.");
		}
	}

	/**
	 * Switches maps appropriately, moves the player to the linked stair
	 * and switches all AI.
	 * @param nextMap
	 * @param nextPoint
	 */
	private void switchMap(StairTile stairs){

		Point oldPt = stairs.getpA();
		Point nextPt = stairs.getpB();
		Map nextMap = stairs.getMapB();

		//set player location
		Tile nextLocation = nextMap.getTile(nextPt.x, nextPt.y);
		player.setLocation(nextLocation);

		stairs.getMapA().getTile(oldPt.x, oldPt.y).removeOccupant();
		stairs.getMapB().getTile(nextPt.x, nextPt.y).setOccupant(player);

		//set the current map
		this.map = nextMap;

		//update the tile
		messenger.drawMap(nextMap);
		messenger.updateTile(nextPt);
		messenger.centerMap(nextPt);
		resetTimeQueue();

		//show the chapter text.
		if (nextMap.getTag() != null){
			//has text on level entry
			messenger.showTextDialog(GameText.getText(nextMap.getTag()), nextMap.getTag());
			player.setTextCollected(player.getTextCollected() + 1);
			nextMap.setTag(null);			// delete tag to not repeat.
			
		}
	
	}

	public boolean sentientAttack(Sentient attacker, Sentient attackee) {
		int attackRoll = MapRand.randInt(20) + attacker.getAttack();
		String attackerUppercase = attacker.getPronoun().substring(0, 1).toUpperCase() + attacker.getPronoun().substring(1);
		if (attackRoll >= attackee.getAC()) {
			int damage = attacker.getMeleeDamage();
			attackee.takeDamage(damage, attacker);
			if (attacker.equals(player) && player.getEquippedWeapon() != null) {
				Weapon w = player.getEquippedWeapon();
				messenger.println("Your " + w.properName() + " " + w.getDamageMsg() + " " + attackee.getPronoun() + " for " + damage + " damage!");
			} else {
				if(attacker.isInSight() || attacker.equals(player))
					messenger.println(attackerUppercase + " " + attacker.getBaseMeleeDescription() + " " + attackee.getPronoun() + " for " + damage + " damage!");
			}
			return true;
		} else {
			if (attackerUppercase.contains("The")) {
				if(attacker.isInSight())
					messenger.println(attackerUppercase + " misses " + attackee.getPronoun());
			} else {
				messenger.println(attackerUppercase + " miss " + attackee.getPronoun());
			}
					
			return false;
		}
	}

	public void updatePlayerStatus() {
		messenger.updateStatus(playerStatus());
	}

	public String playerStatus() {
		return "Player: HP = " + player.getCurrentHP() + ", Strength = " + player.getStrength() + ", Dexterity = " + player.getDexterity() + 
				", Armour: " + player.getACBonus() + ", Nutrition = " + player.hungerText() + ", XP = " + player.getXP();
	}

	// Return a random item for the map, given the current depth in the station
	// Currently just returns one of our foods randomly.
	public Holdable getRandMapItem(int mapIndex) {
		Holdable item;
		do {
			int rand = MapRand.randInt(7);
			if (rand == 0) {
				// 1/8 chance of spawning a weapon
				int randomIndex = MapRand.randInt(weapons.size() - 1);
				item = (Holdable)duplicator.duplicate(weapons.get(randomIndex));
			} else if (rand == 1) {
				int randomIndex = MapRand.randInt(armours.size() - 1);
				item = (Holdable)duplicator.duplicate(armours.get(randomIndex));
			} else {
				int randomIndex = MapRand.randInt(foods.size() - 1);
				item = (Holdable)duplicator.duplicate(foods.get(randomIndex));
			}
		} while (item.getCost() > tierToMaxCost(mapIndex) || item.getCost() < tierToMinCost(mapIndex));
		return item;
	}
		
	private int tierToMaxCost(int tier) {
		switch (tier) {
		case 1:
			return 49;
		case 2:
			return 99;
		case 3:
			return 499;
		case 4:
			return 1000;
		case 5:
			return 5000;
		}
		return 0;
	}
	
	private int tierToMinCost(int tier) {
		switch (tier) {
		case 1:
			return 0;
		case 2:
			return 50;
		case 3:
			return 100;
		case 4:
			return 500;
		case 5:
			return 1000;
		}
		return 0;
	}

	public Monster getRandMapMonster(int mapIndex) {
		int randomIndex = MapRand.randInt(monsters.size() - 1);
		Monster monster = (Monster)duplicator.duplicate(monsters.get(randomIndex));
		while (monster.getDifficulty() != mapIndex) {
			randomIndex = MapRand.randInt(monsters.size() - 1);
			monster = (Monster)duplicator.duplicate(monsters.get(randomIndex));
		}
		return monster;
	}

	public void addPlayerEvent(int actionCost) {
		
		timeQueue.addEventToQueue(player, actionCost / player.getSpeed());
		player.increaseHunger(actionCost/2);
		messenger.updateStatus(playerStatus());
		messenger.updateTile(player.getLocation().getColumn(), player.getLocation().getRow());
		
	}

	public void playTurn() {
		Sentient topEventSentient = timeQueue.getNextEvent();
		while (!topEventSentient.equals(player)) {
			monsterAction((Monster)topEventSentient);
			//moveRandomly(topEventSentient);
			timeQueue.addEventToQueue(topEventSentient, ((Monster) topEventSentient).getActionCost());
			topEventSentient = timeQueue.getNextEvent();
		}
		checkGameOver();
		player.increaseCurrentHP(1);
		player.checkCounters();
	}
	
	public void monsterAction(Monster monster) {

		if (lineOfSight(monster, player.getLocation())) {
			// chase player
			ArrayList<Point> directions = new ArrayList<Point>(2);
			Tile location = monster.getLocation();
			Tile playerTile = player.getLocation();
			
			// Down
			if (location.getRow() > playerTile.getRow())
				directions.add(new Point(0, -1));
			// Up
			if (location.getRow() < playerTile.getRow())
				directions.add(new Point(0, 1));
			// Right
			if (location.getColumn() < playerTile.getColumn())
				directions.add(new Point(1, 0));
			// Left
			if (location.getColumn() > playerTile.getColumn())
				directions.add(new Point(-1, 0));
			int random = MapRand.randInt(directions.size() - 1);
			moveSentient(monster, directions.get(random).x, directions.get(random).y);
		} else {
			moveRandomly(monster);
		}
	}

	public void checkGameOver() {
		if (player.isDead()) {
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(0, 1, 0, 10));

			panel.add(new JLabel("You have died!"));
			panel.add(new JLabel(player.causeOfDeath()));
			panel.add(new JLabel("Your score was: " + player.getScore()));

			JOptionPane.showMessageDialog(null, panel, "Game Over", JOptionPane.PLAIN_MESSAGE);
			endGame();
		}
	}

	public void centerMapEvent(){
		messenger.centerMap(player.getLocation().getColumn(), player.getLocation().getRow());
	}

	public void openDoorEvent(){
		doorEvent(true);
	}
	public void closeDoorEvent(){
		doorEvent(false);
	}

	/**
	 * Does the door open and close if a door exists for player.
	 * @param open True to open the door, false to close it.
	 */
	private void doorEvent(boolean open){
		Point doorLoc = new Point(player.getLocation().getColumn(), player.getLocation().getRow());

		Point north = new Point(doorLoc.x, doorLoc.y-1);
		Point south = new Point(doorLoc.x, doorLoc.y+1);
		Point east = new Point(doorLoc.x+1, doorLoc.y);
		Point west = new Point(doorLoc.x-1, doorLoc.y);

		DoorTile activate = null;
		if(map.getTile(north) instanceof DoorTile){
			doorLoc = north;
			activate = (DoorTile) map.getTile(north);
			if(open == true){
				//open door
				activate.openDoor();
			}
			else{
				if (activate.tileFree() == false){
					//cannot close door if monster in the way.
					messenger.println("The door seems to be stuck! There's a " + activate.getOccupant().getName() + " in the way!");
				}
				else if(activate.getItemCount() > 0){
					//cannot close door if items in there.
					messenger.println("The door seems to be stuck! Maybe there are items blocking the way.");
				}
				else{
					activate.closeDoor();
				}
			}
			messenger.updateTile(doorLoc);
		}
		
		if(map.getTile(south) instanceof DoorTile){
			doorLoc = south;
			activate = (DoorTile) map.getTile(south);
			if(open == true){
				//open door
				activate.openDoor();
			}
			else{
				if (activate.tileFree() == false){
					//cannot close door if monster in the way.
					messenger.println("The door seems to be stuck! There's a " + activate.getOccupant().getName() + " in the way!");
				}
				else if(activate.getItemCount() > 0){
					//cannot close door if items in there.
					messenger.println("The door seems to be stuck! Maybe there are items blocking the way.");
				}
				else{
					activate.closeDoor();
				}
			}
			messenger.updateTile(doorLoc);
		}
		if(map.getTile(east) instanceof DoorTile){
			doorLoc = east;
			activate = (DoorTile) map.getTile(east);
			if(open == true){
				//open door
				activate.openDoor();
			}
			else{
				if (activate.tileFree() == false){
					//cannot close door if monster in the way.
					messenger.println("The door seems to be stuck! There's a " + activate.getOccupant().getName() + " in the way!");
				}
				else if(activate.getItemCount() > 0){
					//cannot close door if items in there.
					messenger.println("The door seems to be stuck! Maybe there are items blocking the way.");
				}
				else{
					activate.closeDoor();
				}
			}
			messenger.updateTile(doorLoc);
		}
		if(map.getTile(west) instanceof DoorTile){
			doorLoc = west;
			activate = (DoorTile) map.getTile(west);
			if(open == true){
				//open door
				activate.openDoor();
			}
			else{
				if (activate.tileFree() == false){
					//cannot close door if monster in the way.
					messenger.println("The door seems to be stuck! There's a " + activate.getOccupant().getName() + " in the way!");
				}
				else if(activate.getItemCount() > 0){
					//cannot close door if items in there.
					messenger.println("The door seems to be stuck! Maybe there are items blocking the way.");
				}
				else{
					activate.closeDoor();
				}
			}
			messenger.updateTile(doorLoc);
		}
		
		
		if (activate == null){
			messenger.println("There are no doors around you to " + (open ? "open" : "close") + ".");
		}

		messenger.updateTile(doorLoc);
	}
}
