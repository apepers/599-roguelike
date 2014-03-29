/*
 * Class to communicate with the player through the command line. It keeps track of what commands are accepted
 * and figure out what information it needs from the Controller, and then tells the Controller what needs to be 
 * done.
 */

package game;

public class Messenger {
	private Scanner reader;
	private Controller controller;
	private Player player;
	
	public Messenger(Controller cont, Player p) {
		controller = cont;
		player = p;
		// Set up scanner to read from the console
		reader = new Scanner(System.in);
	}
	
	public void playerAction() {
		char c = reader.next().charAt(0);
		switch c {
			case 'p':
				pickUp();
				break;
			case 'q':
				reader.close();
				controller.endGame();
				break;
		}
	}
	
	private void pickUp() {
		System.out.println("Pick up what? (type 'q' to quit)");
		player.location.displayItems();
		char input = reader.next().charAt(0);
		Holdable newItem = null;
		while (input != 'q') {
			try {
				Holdable item = player.location.getItem((Character) input);
				if (item.isStackable()) {
					System.out.println("How many do you want to pick up? (#, all, or q to quit)");
					item.display();
					String countInput = reader.next();
					while (countInput.charAt(0) != 'q') {
						if (countInput.matches("\\d*")) {
							int count = Integer.parseInt(countInput);
							if (count > item.stackSize())
								System.out.println("There aren't that many " + item.getName() + " here.");
							else if (count < 1) 
								System.out.println("You have to pick up at least 1.");
							else {
								newItem = player.location.removeItem(input, count);
								break;
							}
						} else if (countInput.equalsIgnoreCase("all")) {
							newItem = player.location.removeItem(input, item.stackSize());
							break;
						}
						countInput = reader.next();
						if (countInput.charAt(0) == 'q')
							input = 'q';
					}
				} else {
					newItem = player.location.removeItem((Character)input);
				}
				if (newItem != null) {
					player.addItem(newItem);
					System.out.println("Picked up " + newItem.properName() + " off the floor.");
					break;
				} else {
					System.out.println("Didn't pick anything up.");
				}
			} catch (InvalidKeyException e) {
				input = reader.next().charAt(0);
				continue;
			}
		}
	}
	
	public char getInput() {
		char c = reader.next().charAt(0);
		while (c != 'q') {
			reactToChar(c);
			c = reader.next().charAt(0);
		}
	}
	
	public void closeReader() {
		reader.close();
	}
}
