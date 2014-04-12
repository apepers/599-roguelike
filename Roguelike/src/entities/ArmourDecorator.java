package entities;

import game.Controller;

public class ArmourDecorator extends Armour {
	protected final Armour decoratedArmour;
	
	public ArmourDecorator(Armour decoratedArmour) {
		this.decoratedArmour = decoratedArmour;
		this.setID(decoratedArmour.getID());
		this.setName(decoratedArmour.getName());
		this.setCost(decoratedArmour.getCost());
		this.setWeight(decoratedArmour.getWeight());
		this.setAC(decoratedArmour.getAC());
	}
}


@SuppressWarnings("serial")
class Teleport extends ArmourDecorator {
	public Teleport(Armour decoratedArmour) {
		super(decoratedArmour);
	}
	
	@Override
	public void equipEffect() {
		Controller.getInstance().winGame();
	}
}
