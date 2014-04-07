/* Class for food objects, which are objects that can be eaten for some given
 * nutrition in some number of turns.
 */

package entities;

import graphics.ImageManager;

@SuppressWarnings("serial")
public class Food extends Holdable {
	private int nutrition;
	private int turnsToEat;
	private String eatMessage;
	
	public static String[] csvHeaders() {
		String[] headers = {"Name", "Cost", "Weight", "Nutrition", "TurnsToEat", "EatMsg", "Special"};
		return headers;
	}
	
	// Messy file parser just for demonstration, should be replaced by a way to read
	// a comma-delimited list much more smoothly
	public static Food createFoodFromReader(String foodString) {
		String[] values = foodString.split(",");
		Food food = new Food();
		try {
			food.setName(values[0]);
			food.setCost(Integer.parseInt(values[1]));
			food.setWeight(Integer.parseInt(values[2]));
			food.setNutrition(Integer.parseInt(values[3]));
			food.setTurnsToEat(Integer.parseInt(values[4]));
			food.setEatMessage(values[5].replaceAll("\\[comma\\]", ","));
			food.setImage(ImageManager.getGlobalRegistry().getTile("item"));			//TEMPORARY
			if (values.length == 7 && values[6] != "") {
				String[] specials = values[6].split(" ");
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

	public int getNutrition() {
		return nutrition;
	}

	void setNutrition(int nutrition) {
		this.nutrition = nutrition;
	}

	public int getTurnsToEat() {
		return turnsToEat;
	}

	void setTurnsToEat(int turnsToEat) {
		this.turnsToEat = turnsToEat;
	}

	public String getEatMessage() {
		return eatMessage;
	}

	void setEatMessage(String eatMessage) {
		this.eatMessage = eatMessage;
	}
	
	@Override
	public String eatMsg() {
		return this.getEatMessage();
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