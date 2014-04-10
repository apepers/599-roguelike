package entities;

public class HoldableDecorator extends Holdable {
	protected final Holdable decoratedHoldable;
	
	public HoldableDecorator(Holdable decoratedHoldable) {
		this.decoratedHoldable = decoratedHoldable;
		this.setID(decoratedHoldable.getID());
		this.setName(decoratedHoldable.getName());
		this.setCost(decoratedHoldable.getCost());
		this.setWeight(decoratedHoldable.getWeight());
	}
}
