/*
 * Class to communicate with the player through the command line. It keeps track of what commands are accepted
 * and figure out what information it needs from the Controller, and then tells the Controller what needs to be 
 * done.
 */

package game;

import entities.*;
import graphics.Frame;
import graphics.PlayerLog;
import graphics.StatusBar;
import graphics.TileDisplay;

import java.util.ArrayList;
import java.util.Scanner;
import java.awt.GridLayout;
import java.awt.Point;
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
	private Action eAction;
	private Action upAction;
	private Action downAction;
	
	
	//GUI elements
	private Frame frame;
	private PlayerLog log;
	private TileDisplay display;
	private StatusBar status;

	
	
	public Messenger(Controller cont, Frame frame, TileDisplay tileDisplay, PlayerLog logDisplay, StatusBar statusDisplay) {
		this.controller = cont;
		this.display = tileDisplay;
		this.log = logDisplay;
		this.status = statusDisplay;
		this.frame = frame;
		
		
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
		
		eAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				eat();
			}
		};
		
		upAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				log.println("Moving up");
				controller.movePlayerUp();
				display.repaint();
			}
		};
		
		downAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				log.println("Moving down");
				controller.movePlayerDown();
				display.repaint();
			}
		};
	}
	
	
	/**
	 * Loads an entire map onto the tile display.
	 * @param m
	 */
	public void drawMap(Map m){
		display.drawMap(m);
	}
	
	/**
	 * Centers the display onto a specified point.
	 * @param pt
	 */
	public void centerMap(Point pt){
		frame.centerMap(pt.x, pt.y);
	}
	
	/**
	 * Centers the display onto a specified point.
	 * @param x
	 * @param y
	 */
	public void centerMap(int x, int y){
		frame.centerMap(x,y);
	}
	
	/**
	 * Asks the tileDisplay to update its screen
	 */
	public void repaintDisplay(){
		display.repaint();
	}
	
	/**
	 * Updates the status bar for the player stats
	 * @param text
	 */
	public void updateStatus(String text){
		status.setText(text);
	}
	
	/**
	 * Writes a line to the player's console
	 * @param text
	 */
	public void println(String text){
		log.println(text);
	}
	

	
	// Eat a food item from the current tile or inventory
	// If the item is stackable, just eats one. Only one food item can be selected.
	private void eat() {
		// Get all available food
		String[] tileFood = player.getLocation().getItems().getFoodsTexts();
		String[] playerFood = player.getInventory().getFoodsTexts();
		if (tileFood.length + playerFood.length == 0) {
			log.println("There is nothing to eat.");
		} else {
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(0, 1));
			final JRadioButton[] radioButtons = new JRadioButton[tileFood.length + playerFood.length];
			String tileIDs = descriptionsToIDString(tileFood);
			String playerIDs = descriptionsToIDString(playerFood);
			final String idsString = tileIDs + playerIDs;
			ButtonGroup buttons = new ButtonGroup();
			// Select the button based on the id of the item
			Action charAction = new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					int index = idsString.indexOf(e.getActionCommand());
					JRadioButton button = radioButtons[index];
					button.setSelected(true);
				}
			};
			int foodCount = 0;
			if (tileFood.length > 0) {
				panel.add(new JLabel("ON GROUND:"));
				for (String f : tileFood) {
					JRadioButton newButton = new JRadioButton(f);
					panel.add(newButton);
					radioButtons[foodCount] = newButton;
					buttons.add(newButton);
					newButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(idsString.substring(foodCount, foodCount+1).toUpperCase()),f);
					newButton.getActionMap().put(f, charAction);
					foodCount++;
				}
			}
			if (playerFood.length > 0) {
				panel.add(new JLabel("INVENTORY"));
				for (String f : playerFood) {
					JRadioButton newButton = new JRadioButton(f);
					panel.add(newButton);
					radioButtons[foodCount] = newButton;
					buttons.add(newButton);
					newButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(idsString.substring(foodCount, foodCount+1).toUpperCase()),f);
					newButton.getActionMap().put(f, charAction);
					foodCount++;
				}
			}
			JOptionPane.showMessageDialog(null, panel, "What would you like to eat?", JOptionPane.PLAIN_MESSAGE);
			for (JRadioButton radio : radioButtons) {
				if (radio.isSelected()) {
					// Get the selected food
					Character id = radio.getText().charAt(0);
					// Determine if it's from the tile or inventory
					Food food;
					try {
						if (tileIDs.indexOf(id) >= 0) {
							food = (Food) player.getLocation().getItem(id);
							if (food.isStackable())
								food = (Food) player.getLocation().removeItem(id, 1);
							else
								food = (Food) player.getLocation().removeItem(id);
						} else {
							food = (Food) player.getInventory().getItem(id);
							if (food.isStackable())
								food = (Food) player.getInventory().removeStackedItem(id, 1);
							else
								food = (Food) player.getInventory().removeItem(id);
						}
						log.println(controller.playerEat(food));
					} catch (InvalidKeyException e) {
						log.println("The item you picked was invalid");
					}
				}
			}
		}
	}
	
	private String descriptionsToIDString(String[] descriptions) {
		String idString = "";
		for (String s : descriptions) {
			idString += s.charAt(0);
		}
		return idString;
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


	public void closeReader() {
		reader.close();
	}
	
	//========================================================================================
	//getters and setters
	
	public void setPlayerLog(PlayerLog p) {
		log = p;
	}
	
	public void setTileDisplay(TileDisplay d) {
		display = d;
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
	
	public Action getEAction() {
		return eAction;
	}
	
	public Action getUpAction() {
		return upAction;
	}
	
	public Action getDownAction() {
		return downAction;
	}
}
