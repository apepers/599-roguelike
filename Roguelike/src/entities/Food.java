/* Class for food objects, which are objects that can be eaten for some given
 * nutrition in some number of turns.
 */

package entities;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;

public abstract class Food extends Holdable {
	public char ascii = '%';
	public Color colour = Color.white;
	int nutrition;
	int turnsToEat;
	String eatMessage;
	
	public abstract String eatMsg();
	
	// Messy file parser just for demonstration, should be replaced by a way to read
	// a comma-delimited list much more smoothly
	public static Food createFoodFromReader(String foodString) {
		String[] values = foodString.split(",");
		Food food = new SimpleFood();
		try {
			food.name = values[0];
			food.cost = Integer.parseInt(values[1]);
			food.weight = Integer.parseInt(values[2]);
			int red = Integer.parseInt(values[3]);
			int blue = Integer.parseInt(values[4]);
			int green = Integer.parseInt(values[5]);
			food.colour = new Color(red, blue, green);
			food.nutrition = Integer.parseInt(values[6]);
			food.turnsToEat = Integer.parseInt(values[7]);
			food.eatMessage = values[8];
			if (values.length == 10 && values[9] != "") {
				String[] specials = values[9].split(" ");
				food = Food.applySpecialTraits(food, specials);
			}
		} catch (Exception e) {
			System.out.println("Error reading food object");
			if (food.name != null)
				System.out.println(food.name + " has some incorrect parameter.");
			return null;
		}
		return food;
	}
	
	// Apply the decorators as matched with the strings
	public static Food applySpecialTraits(Food food, String[] traits) {
		for (String trait : traits) {
			if (trait.trim().equals("Splat"))
				food = new Splat(food);
		}
		return food;
	}
}

// The basic food object, with no extra functionality
class SimpleFood extends Food {
	public SimpleFood() { 
		
	}
	
	public String eatMsg() {
		return eatMessage;
	}
}
