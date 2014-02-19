package entities;

import java.util.ArrayList;
import java.io.*;
import java.lang.Integer;
import java.awt.Color;

public class Test {
	public static void main(String[] args) {
		ArrayList<Food> foods = new ArrayList<Food>();
		try {
			BufferedReader in = new BufferedReader(new FileReader("itemdata.txt"));
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
		} catch (IOException e) {
			System.out.println("Error reading the item file");
			System.exit(0);
		}

		for (Food food : foods) {
			System.out.println("Eat the food");
			System.out.println(food.eatMsg());
			System.out.println("Nutrition increased by " + food.nutrition);
			System.out.println("Throw the food");
			System.out.println(food.throwMsg());
			System.out.println("\n");
		}
	}
	
	private static boolean headersMatch(String input) {
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
}
