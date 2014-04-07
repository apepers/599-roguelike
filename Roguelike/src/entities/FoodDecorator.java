/* Class using the decorator design pattern to add functionality to foods, which is
 * set via keywords in the database for items.
 */

package entities;

/* Use the Decorator design pattern to add functionality to food items
 * individually.
 */
@SuppressWarnings("serial")
public abstract class FoodDecorator extends Food {
	protected final Food decoratedFood;
	
	public FoodDecorator(Food decoratedFood) {
		this.decoratedFood = decoratedFood;
		this.setID(decoratedFood.getID());
		this.setName(decoratedFood.getName());
		this.setCost(decoratedFood.getCost());
		this.setWeight(decoratedFood.getWeight());
		this.setNutrition(decoratedFood.getNutrition());
		this.setTurnsToEat(decoratedFood.getTurnsToEat());
		this.setEatMessage(decoratedFood.getEatMessage());
		this.setStackable(decoratedFood.isStackable());
	}
}

// Decorator for food which splats when thrown
@SuppressWarnings("serial")
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
@SuppressWarnings("serial")
class Stackable extends FoodDecorator {
	public int count = 1;
	
	public Stackable(Food decoratedFood) {
		super(decoratedFood);
		this.setStackable(true);
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