/*
 * Class to communicate with the player through the command line. It keeps track of what commands are accepted
 * and figure out what information it needs from the Controller, and then tells the Controller what needs to be 
 * done.
 */

package game;

import entities.*;
import graphics.PlayerLog;

import java.util.ArrayList;
import java.util.Scanner;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.security.InvalidKeyException;

import javax.swing.*;

public class Messenger {
	private Scanner reader;
	private Controller controller;
	private Player player;
	private Action quitAction;
	private Action pAction;
	private Action iAction;
	private PlayerLog log;
	
	public Messenger(Controller cont, Player p) {
		controller = cont;
		player = p;
		// Set up scanner to read from the console
		reader = new Scanner(System.in);
		
		quitAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				reader.close();
				log.println("Goodbye");	// Not usually seen since it closes too quickly
				controller.endGame();
			}
		};
		
		pAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				pickUpNew();
			}
		};
		
		iAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				viewInventory();
			}
		};
	}
	
	public void setPlayerLog(PlayerLog p) {
		log = p;
	}
	
	public Action getQuitAction() {
		return quitAction;
	}
	
	public Action getPAction() {
		return pAction;
	}
	
	public Action getIAction() {
		return iAction;
	}
	
	public void playerAction() {
		char c = reader.next().charAt(0);
		switch (c) {
			case 'p':
				pickUp();
				break;
			case 'q':
				reader.close();
				controller.endGame();
				break;
		}
	}
	
	// Display the inventory in a popup, or if the player isn't holding anything say so in the log
	private void viewInventory() {
		String[] weapons = player.getInventory().getWeaponTexts();
		String[] foods = player.getInventory().getFoodsTexts();
		String[] misc = player.getInventory().getMiscTexts();
		int inventoryLength = weapons.length + foods.length + misc.length;
		if (inventoryLength == 0) {
			log.println("You are not holding anything");
		} else {
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(0, 1));
			if (weapons.length > 0)
				panel.add(new JLabel("WEAPONS:"));
			for (String w : weapons)
				panel.add(new JLabel(w));
			if (foods.length > 0)
				panel.add(new JLabel("FOOD:"));
			for (String f : foods)
				panel.add(new JLabel(f));
			if (misc.length > 0)
				panel.add(new JLabel("MISC:"));
			for (String m : misc)
				panel.add(new JLabel(m));
			
			JOptionPane.showMessageDialog(null, panel, "Inventory", JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	// Open a dialog to find out which of the tile's contents the player wishes to pick up.
	// Note that this method will need alteration to deal with upper case IDs
	private void pickUpNew() {
		Tile playerLocation = player.getLocation();
		final String idsString = playerLocation.getItems().getIDString();
		String[] weapons = playerLocation.getItems().getWeaponTexts();
		String[] foods = playerLocation.getItems().getFoodsTexts();
		String[] misc = playerLocation.getItems().getMiscTexts();
		final JCheckBox[] checkBoxes = new JCheckBox[weapons.length + foods.length + misc.length];
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));
		
		// Set up actions for every ID to toggle the appropriate checkbox
		Action charAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(e.getActionCommand());
				int index = idsString.indexOf(e.getActionCommand());
				JCheckBox box = checkBoxes[index];
				box.setSelected(!box.isSelected());
			}
		};

		int itemCounter = 0;
		
		// Add labels when needed, and add a checkbox for every item
		// while also mapping IDs to their keystrokes
		if (weapons.length > 0)
			panel.add(new JLabel("WEAPONS:"));
		for (int i = 0; i < weapons.length; i++) {
			JCheckBox newBox = new JCheckBox(weapons[i]);
			newBox.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(idsString.substring(i, i+1).toUpperCase()), weapons[i]);
			newBox.getActionMap().put(weapons[i], charAction);
			checkBoxes[itemCounter] = newBox;
			panel.add(newBox);
			itemCounter++;
		}
		if (foods.length > 0)
			panel.add(new JLabel("FOODS:"));
		for (int i = 0; i < foods.length; i++) {
			JCheckBox newBox = new JCheckBox(foods[i]);
			newBox.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(idsString.substring(i, i+1).toUpperCase()), foods[i]);
			newBox.getActionMap().put(foods[i], charAction);
			checkBoxes[itemCounter] = newBox;
			panel.add(newBox);
			itemCounter++;
		}
		if (misc.length > 0)
			panel.add(new JLabel("MISC:"));
		for (int i = 0; i < misc.length; i++) {
			JCheckBox newBox = new JCheckBox(misc[i]);
			newBox.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(idsString.substring(i, i+1).toUpperCase()), misc[i]);
			newBox.getActionMap().put(misc[i], charAction);
			checkBoxes[itemCounter] = newBox;
			panel.add(newBox);
			itemCounter++;
		}		
		
		// Create an option dialog with the checkboxes
		JOptionPane.showMessageDialog(null, panel, "What would you like to pick up?", JOptionPane.PLAIN_MESSAGE);
		for (JCheckBox box : checkBoxes) {
			// Act on the selected checkboxes
			if (box.isSelected()) {
				char id = box.getText().charAt(0);
				// Pick up the item with the ID of the selected box
				try {
					Holdable newItem = playerLocation.removeItem((Character) id);
					if (newItem != null) {
						player.addItem(newItem);
						log.println("Picked up " + newItem.properName() + " off the floor.");
					} else {
						System.out.println("Didn't pick anything up.");
					}
				} catch (InvalidKeyException e) {
					log.println("The item you picked was invalid.");	// Shouldn't ever happen
				}
			}
		}
	}
	
	private void pickUp() {
		Tile playerLocation = player.getLocation();
		System.out.println("Pick up what? (type 'q' to quit)");
		playerLocation.displayItems();
		char input = reader.next().charAt(0);
		Holdable newItem = null;
		while (input != 'q') {
			try {
				Holdable item = playerLocation.getItem((Character) input);
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
								newItem = playerLocation.removeItem(input, count);
								break;
							}
						} else if (countInput.equalsIgnoreCase("all")) {
							newItem = playerLocation.removeItem(input, item.stackSize());
							break;
						}
						countInput = reader.next();
						if (countInput.charAt(0) == 'q')
							input = 'q';
					}
				} else {
					newItem = playerLocation.removeItem((Character)input);
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
	
	public void closeReader() {
		reader.close();
	}
}
