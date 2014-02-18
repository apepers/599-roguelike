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
	public static Food createFoodFromReader(String name, BufferedReader in) {
		Food food = new SimpleFood();
		try {
			food.name = name;
			food.cost = Integer.parseInt(in.readLine().substring("Cost:".length()).trim());
			food.weight = Integer.parseInt(in.readLine().substring("Weight:".length()).trim());
			String[] colours = in.readLine().substring("Colour:".length()).split(",");
			int red = Integer.parseInt(colours[0].trim());
			int blue = Integer.parseInt(colours[1].trim());
			int green = Integer.parseInt(colours[2].trim());
			food.colour = new Color(red, blue, green);
			food.nutrition = Integer.parseInt(in.readLine().substring("Nutrition:".length()).trim());
			food.turnsToEat = Integer.parseInt(in.readLine().substring("Turns To Eat:".length()).trim());
			food.eatMessage = in.readLine().substring("Eat Message: ".length()).trim();
			String[] specials = in.readLine().substring("Special:".length()).split(",");
			food = Food.applySpecialTraits(food, specials);
		} catch (IOException e) {
			System.out.println("Error reading food object");
			if (name != null)
				System.out.println(name + " is missing some parameter.");
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
