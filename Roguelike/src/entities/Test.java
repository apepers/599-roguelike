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
			if (!line.equals("Foods")) {
				System.out.println("Error: File is missing Foods section");
				System.exit(0);
			}
			String name = in.readLine().substring("Name:".length()).trim();
			while (name != null) {
				foods.add(Food.createFoodFromReader(name, in));
				name = in.readLine();
				if (name == null)
					break;
				if (name.equals(""))
					name = in.readLine();
				if (!name.contains("Name:")) {
					name = null;
				} else {
					name = name.substring("Name:".length()).trim();
				}
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
}
