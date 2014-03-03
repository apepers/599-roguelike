/* Class using the decorator design pattern to add functionality to foods, which is
 * set via keywords in the database for items.
 */

package entities;

import java.awt.Color;

/* Use the Decorator design pattern to add functionality to food items
 * individually.
 */
public abstract class FoodDecorator extends Food {
	protected final Food decoratedFood;
	
	public FoodDecorator(Food decoratedFood) {
		this.decoratedFood = decoratedFood;
		this.setName(decoratedFood.getName());
		this.cost = decoratedFood.cost;
		this.colour = decoratedFood.colour;
		this.weight = decoratedFood.weight;
		this.nutrition = decoratedFood.nutrition;
		this.turnsToEat = decoratedFood.turnsToEat;
		this.eatMessage = decoratedFood.eatMessage;
		this.stackable = decoratedFood.stackable;
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
		return "The " + decoratedFood.getName() + " hits the floor with a splat!";
	}
}

// Decorator for food which can be stacked
class Stackable extends FoodDecorator {
	public int count = 1;
	
	public Stackable(Food decoratedFood) {
		super(decoratedFood);
		this.stackable = true;
	}
	
	@Override
	public void combineStack(Stackable item) {
		count += item.count;
	}
	
	@Override
	public Stackable reduceStack(int c) {
		count -= c;
		Stackable newStack = new Stackable(this);
		newStack.count = c;
		return newStack;
	}
	
	@Override
	public int stackSize() {
		return count;
	}
	
	@Override
	public String properName() {
		return this.getName() + " (" + count + ")";
	}
}