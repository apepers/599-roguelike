package entities;

public class WeaponDecorator extends Weapon {
	protected final Weapon decoratedWeapon;
	
	public WeaponDecorator(Weapon w) {
		this.decoratedWeapon = w;
		this.setCost(w.getCost());
		this.setDamageMsg(w.getDamageMsg());
		this.setID(w.getID());
		this.setMaxDamage(w.getMaxDamage());
		this.setMinDamage(w.getMinDamage());
		this.setName(w.getName());
		this.setStackable(w.isStackable());
		this.setWeight(w.getWeight());
	}
}

@SuppressWarnings("serial")
class PoweredWeapon extends WeaponDecorator {
	public PoweredWeapon(Weapon decoratedWeapon) {
		super(decoratedWeapon);
		this.setPowered(true);
	}
}