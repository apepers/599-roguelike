/* Class for food objects, which are objects that can be eaten for some given
 * nutrition in some number of turns.
 */

package entities;

import java.awt.Color;

@SuppressWarnings("serial")
public class Food extends Holdable {
	private char ascii = '%';
	private Color colour = Color.white;
	private int nutrition;
	private int turnsToEat;
	private String eatMessage;
	
	// Messy file parser just for demonstration, should be replaced by a way to read
	// a comma-delimited list much more smoothly
	public static Food createFoodFromReader(String foodString) {
		String[] values = foodString.split(",");
		Food food = new Food();
		try {
			food.setName(values[0]);
			food.setCost(Integer.parseInt(values[1]));
			food.setWeight(Integer.parseInt(values[2]));
			int red = Integer.parseInt(values[3]);
			int blue = Integer.parseInt(values[4]);
			int green = Integer.parseInt(values[5]);
			food.setColour(new Color(red, blue, green));
			food.setNutrition(Integer.parseInt(values[6]));
			food.setTurnsToEat(Integer.parseInt(values[7]));
			food.setEatMessage(values[8]);
			if (values.length == 10 && values[9] != "") {
				String[] specials = values[9].split(" ");
				food = Food.applySpecialTraits(food, specials);
			}
		} catch (Exception e) {
			System.out.println("Error reading food object");
			if (food.getName() != null)
				System.out.println(food.getName() + " has some incorrect parameter.");
			return null;
		}
		return food;
	}
	
	// Apply the decorators as matched with the strings
	public static Food applySpecialTraits(Food food, String[] traits) {
		for (String trait : traits) {
			if (trait.trim().equals("Splat"))
				food = new Splat(food);
			if (trait.trim().equals("Stackable"))
				food = new Stackable(food);
		}
		return food;
	}

	public char getAscii() {
		return ascii;
	}

	public void setAscii(char ascii) {
		this.ascii = ascii;
	}

	public Color getColour() {
		return colour;
	}

	public void setColour(Color colour) {
		this.colour = colour;
	}

	int getNutrition() {
		return nutrition;
	}

	void setNutrition(int nutrition) {
		this.nutrition = nutrition;
	}

	int getTurnsToEat() {
		return turnsToEat;
	}

	void setTurnsToEat(int turnsToEat) {
		this.turnsToEat = turnsToEat;
	}

	String getEatMessage() {
		return eatMessage;
	}

	void setEatMessage(String eatMessage) {
		this.eatMessage = eatMessage;
	}
}

/*
// The basic food object, with no extra functionality
class SimpleFood extends Food {
	public SimpleFood() { 
		
	}
	
	public String eatMsg() {
		return getEatMessage();
	}
}
*/