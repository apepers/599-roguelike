package entities;

import java.util.ArrayList;

public class Test {
	public static void main(String[] args) {
		ArrayList<Food> foods = new ArrayList<Food>();
		foods.add(new CreamPie());
		foods.add(new Ration());
		foods.add(new Lembas());
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
