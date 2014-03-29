/*
 * Master controller for the system, the central class which processes requests and asks the appropriate
 * parts of the game for the information needed and sends it back where it has to go.
 */

package game;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;


public class Controller {
	private Player player;
	ArrayList<Food> foods;
	private ItemDuplicator duplicator;
	ArrayList<Tile> map;	// Placeholder until the proper map is brought in
	private Messenger messenger;
	boolean gameRunning;
	
	public Controller() { };
	
	public boolean setup() {
		foods = new ArrayList<Food>();
		try {
			loadFoods();
		} catch (IOException e) {
			System.err.println("Error reading foods CSV file.");
			return false;
		}
		duplicator = new ItemDuplicator();
		player = new Player();
		Tile tile = new Tile();
		map.add(tile);	// Would call function to create the random map
		// Would randomly add items to the map
		for (Food f : foods) {
			tile.addItem((Holdable)duplicator.duplicate(f));
			tile.addItem((Holdable)duplicator.duplicate(f));
		}
		// Place player on the map
		tile.setOccupant(player);
		messenger = new Messenger(this, player);
	}
	
	public static void main(String[] args) {
		Controller controller = new Controller();
		// Setup the game, only continue if it succeeded
		if (!controller.setup()) {
			System.err.println("Setup did not complete successfully. Exiting now.");
			System.exit(0);
		}
		gameRunning = true;
		while (gameRunning) {
			messenger.playerAction();
		}
	}
	
	private void loadFoods() throws IOException {
		BufferedReader in = null;
		in = new BufferedReader(new FileReader("itemdata.txt"));
		String line = in.readLine();
		if (!headersMatch(line)) {
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
	
	// Make sure that the headers of the food section match what we expect
	private boolean headersMatch(String input) {
		String[] inHeaders = input.split(",");
		String[] headers = {"Name", "Cost", "Weight", "Red", "Blue", "Green", 
				"Nutrition", "TurnsToEat", "EatMsg", "Special"};
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
	}
}