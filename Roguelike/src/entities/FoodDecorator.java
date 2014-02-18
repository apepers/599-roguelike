package entities;

import java.awt.Color;

/* Use the Decorator design pattern to add functionality to food items
 * individually.
 */
public abstract class FoodDecorator extends Food {
	protected final Food decoratedFood;
	
	public FoodDecorator(Food decoratedFood) {
		this.decoratedFood = decoratedFood;
		this.nutrition = decoratedFood.nutrition;
		this.turnsToEat = decoratedFood.turnsToEat;
		this.eatMessage = decoratedFood.eatMessage;
	}
	
	public String eatMsg() {
		return decoratedFood.eatMsg();
	}
}

// Decorator for food which splats when thrown
class Splat extends FoodDecorator {
	public Splat(Food decoratedFood) {
		super(decoratedFood);
	}
	
	@Override
	public String throwMsg() {
		return "The " + decoratedFood.name + " hits the floor with a splat!";
	}
}
