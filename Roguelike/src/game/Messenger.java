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
import java.awt.event.ActionEvent;
import java.security.InvalidKeyException;

import javax.swing.*;

public class Messenger {
	private Scanner reader;
	private Controller controller;
	private Player player;
	private Action quitAction;
	private Action pAction;
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
	
	// Open a dialog to find out which of the tile's contents the player wishes to pick up.
	// Note that this method will need alteration to deal with upper case IDs
	private void pickUpNew() {
		Tile playerLocation = player.getLocation();
		final String idsString = playerLocation.getItems().getIDString();
		String[] descriptions = playerLocation.getItems().getItemTexts();
		final JCheckBox[] checkBoxes = new JCheckBox[descriptions.length];
		JComponent[] inputs = new JComponent[descriptions.length];
		
		// Set up actions for every ID to toggle the appropriate checkbox
		Action charAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(e.getActionCommand());
				int index = idsString.indexOf(e.getActionCommand());
				JCheckBox box = checkBoxes[index];
				box.setSelected(!box.isSelected());
			}
		};

		// Create a checkbox for every item, map the ID to the action above, add to the two lists
		for (int i = 0; i < descriptions.length; i++) {
			JCheckBox newBox = new JCheckBox(descriptions[i]);
			newBox.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(idsString.substring(i, i+1).toUpperCase()), descriptions[i]);
			newBox.getActionMap().put(descriptions[i], charAction);
			checkBoxes[i] = newBox;
			inputs[i] = newBox;
		}
		
		// Create an option dialog with the checkboxes
		JOptionPane.showMessageDialog(null, inputs, "What would you like to pick up?", JOptionPane.PLAIN_MESSAGE);
		for (JCheckBox box : checkBoxes) {
			// Act on the selected checkboxes
			if (box.isSelected()) 
				System.out.println(box.getText());
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
