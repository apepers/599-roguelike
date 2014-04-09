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
	private Action dAction;
	private Action enterAction;
	private Action questionAction;
	private Action LAction;
	private Action equipAction;
	
	//cursor events
	private Action upAction;
	private Action downAction;
	private Action rightAction;
	private Action leftAction;
	
	private Action waitAction;
	private Action invalidAction;
	
	//stair events
	private Action stairsUp;
	private Action stairsDown;
	
	private Action centerMap;
	
	//door closing events
	private Action openDoor;
	private Action closeDoor;
	
	//GUI elements
	private Frame frame;
	private PlayerLog log;
	private TileDisplay display;
	private StatusBar status;
	private boolean cursorMode;
	
	

	public Messenger(Controller cont, Player p, Frame frame, TileDisplay tileDisplay, PlayerLog logDisplay, StatusBar statusDisplay) {
		this.controller = cont;
		this.player = p;
		this.display = tileDisplay;
		this.log = logDisplay;
		this.status = statusDisplay;
		this.frame = frame;
		cursorMode = false;
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
				if(cursorMode) {
					log.println("Invalid key");
				} else {
					pickUpNew();
					controller.addPlayerEvent(20);
					controller.playTurn();
				}
			}
		};
		
		iAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if(cursorMode) {
					log.println("Invalid key");
				} else {
					viewInventory();
				}
			}
		};
		
		eAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if(cursorMode) {
					log.println("Invalid key");
				} else {
					int eatTime = eat();
					if (eatTime != -1) {
						controller.addPlayerEvent(eatTime);
						controller.playTurn();
					}
				}
			}
		};
		
		dAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (cursorMode) {
					log.println("Invalid key");
				} else {
					dropNew();
					controller.addPlayerEvent(10);
					controller.playTurn();
				}
			}
		};
		
		equipAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (cursorMode) {
					log.println("Invalid key");
				} else {
					equipWeapon();
					controller.addPlayerEvent(10);
					controller.playTurn();
				}
			}
		};
		
		upAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if(cursorMode) {
					controller.moveCursorUp();
				} else {
					controller.movePlayerUp();
					controller.addPlayerEvent(10);
					controller.playTurn();
				}
				display.repaint();
			}
		};
		
		downAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if(cursorMode) {
					controller.moveCursorDown();
				} else {
					controller.movePlayerDown();
					controller.addPlayerEvent(10);
					controller.playTurn();
				}
				display.repaint();
			}
		};
		
		leftAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if(cursorMode) {
					controller.moveCursorLeft();
				} else {
					controller.movePlayerLeft();
					controller.addPlayerEvent(10);
					controller.playTurn();
				}
				display.repaint();
			}
		};
		
		rightAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if(cursorMode) {
					controller.moveCursorRight();
				} else {
					controller.movePlayerRight();
					controller.addPlayerEvent(10);
					controller.playTurn();
				}
				display.repaint();
			}
		};
		
		questionAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if(cursorMode) {
					log.println("Invalid key");
				} else {
					log.println("Identify what? (Use arrow keys to move and enter to select)");
					identify();
					display.repaint();
				}
			}
		};
		
		LAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if (cursorMode) {
					log.println("Invalid key");
				} else {
					look();
				}
			}
		};
		
		enterAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				if(cursorMode) {
					select();
					display.repaint();
				} else {
					log.println("Invalid key");
				}
			}
		};
		
		waitAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				controller.addPlayerEvent(10);
				controller.playTurn();
			}
		};
		
		stairsUp = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				controller.addPlayerEvent(10);
				controller.stairsUp();
			}
		};
		
		stairsDown = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				controller.addPlayerEvent(10);
				controller.stairsDown();
			}
		};
		
		centerMap = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				controller.centerMapEvent();
			}
		};
		
		openDoor = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				controller.addPlayerEvent(10);
				controller.openDoorEvent();
			}
		};
		
		closeDoor = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				controller.addPlayerEvent(10);
				controller.closeDoorEvent();
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
	 * draws an image on top of the existing tile
	 * @param pt
	 */
	public void drawImage(ImageIcon img, Point pt){
		display.drawTile(img, pt.x, pt.y);
	}
	
	/**
	 * Updates a tile on the tile display
	 * @param pt
	 */
	public void updateTile(Point pt){
		display.refreshTile(pt.x, pt.y);
	}
	
	/**
	 * Updates a tile on the tile display
	 * @param x
	 * @param y
	 */
	public void updateTile(int x, int y){
		display.refreshTile(x, y);
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
	private int eat() {
		// Get all available food
		String[] tileFood = player.getLocation().getItems().getFoodsTexts();
		String[] playerFood = player.getInventory().getFoodsTexts();
		if (tileFood.length + playerFood.length == 0) {
			log.println("There is nothing to eat.");
			return -1;
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
						return food.getTurnsToEat() * 10;
					} catch (InvalidKeyException e) {
						log.println("The item you picked was invalid");
						return -1;
					}
				}
			}
		}
		return -1;
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
	
	private void look() {
		Tile location = player.getLocation();
		String[] weapons = location.getItems().getWeaponTexts();
		String[] foods = location.getItems().getFoodsTexts();
		String[] misc = location.getItems().getMiscTexts();
		int inventoryLength = weapons.length + foods.length + misc.length;
		if (inventoryLength == 0) 
			log.println("There is nothing here.");
		else {
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
			
			JOptionPane.showMessageDialog(null, panel, "Ground", JOptionPane.PLAIN_MESSAGE);
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
		updateTile(player.getLocation().getColumn(), player.getLocation().getRow());
	}
	
	public void dropNew() {
		// Get all available food
		String[] playerItems = player.getInventory().getItemTexts();
		if (playerItems.length == 0) {
			log.println("You have nothing to drop.");
		} else {
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(0, 1));
			final JCheckBox[] checkBoxes = new JCheckBox[playerItems.length];
			final String idsString = descriptionsToIDString(playerItems);
			ButtonGroup buttons = new ButtonGroup();
			// Set up actions for every ID to toggle the appropriate checkbox
			Action charAction = new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					int index = idsString.indexOf(e.getActionCommand());
					JCheckBox box = checkBoxes[index];
					box.setSelected(!box.isSelected());
				}
			};
			int itemCount = 0;
			panel.add(new JLabel("INVENTORY"));
			for (String f : playerItems) {
				JCheckBox newBox = new JCheckBox(f);
				newBox.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(idsString.substring(itemCount, itemCount+1).toUpperCase()), f);
				newBox.getActionMap().put(f, charAction);
				checkBoxes[itemCount] = newBox;
				panel.add(newBox);
				itemCount++;
			}
			JOptionPane.showMessageDialog(null, panel, "What would you like to drop?", JOptionPane.PLAIN_MESSAGE);
			for (JCheckBox box : checkBoxes) {
				if (box.isSelected()) {
					// Get the selected item
					Character id = box.getText().charAt(0);
					Holdable item;
					try {
						item = player.getInventory().getItem(id);
						if (item.isStackable())
							item = player.getInventory().removeStackedItem(id, 1);
						else
							item = player.getInventory().removeItem(id);
						player.getLocation().addItem(item);
					} catch (InvalidKeyException e) {
						log.println("The item you picked was invalid");
					}
				}
			}
		}
		updateTile(player.getLocation().getColumn(), player.getLocation().getRow());
	}
	
	public void equipWeapon() {
		// Get all available weapons
		String[] playerItems = player.getInventory().getWeaponTexts();
		if (playerItems.length == 0) {
			log.println("You have nothing to drop.");
		} else {
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(0, 1));
			final JRadioButton[] radioButtons = new JRadioButton[playerItems.length];
			final String idsString = descriptionsToIDString(playerItems);
			ButtonGroup buttons = new ButtonGroup();
			// Set up actions for every ID to toggle the appropriate checkbox
			Action charAction = new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					int index = idsString.indexOf(e.getActionCommand());
					JRadioButton button = radioButtons[index];
					button.setSelected(!button.isSelected());
				}
			};
			int itemCount = 0;
			panel.add(new JLabel("INVENTORY"));
			for (String f : playerItems) {
				JRadioButton newButton = new JRadioButton(f);
				newButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(idsString.substring(itemCount, itemCount+1).toUpperCase()), f);
				newButton.getActionMap().put(f, charAction);
				radioButtons[itemCount] = newButton;
				panel.add(newButton);
				itemCount++;
			}
			JOptionPane.showMessageDialog(null, panel, "What would you like to equip?", JOptionPane.PLAIN_MESSAGE);
			for (JRadioButton button : radioButtons) {
				if (button.isSelected()) {
					// Get the selected item
					Character id = button.getText().charAt(0);
					Weapon weapon;
					weapon = (Weapon) player.getInventory().getItem(id);
					player.setEquippedWeapon(weapon);
				}
			}
		}
	}
	
	public void identify() {
		cursorMode = true;
		controller.createCursor();
	}
	
	
	public void select() {
		Entity entity = controller.select();
		//log.println(controller.select().getName());
		controller.deleteCursor();
		cursorMode = false;
		
		if (entity == null) {
			log.println("There's nothing here.");
		} else {
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(0, 1, 0, 10));
			
			panel.add(new JLabel("Name: \n" + entity.getName()));
			panel.add(new JTextArea(entity.getDescription()));
			
			JOptionPane.showMessageDialog(null, panel, "Identifying...", JOptionPane.PLAIN_MESSAGE);
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
	
	public Action getDAction() {
		return dAction;
	}
	
	public Action getEquipAction() {
		return equipAction;
	}
	
	public Action getUpAction() {
		return upAction;
	}
	
	public Action getDownAction() {
		return downAction;
	}


	public Action getRightAction() {
		return rightAction;
	}


	public Action getLeftAction() {
		return leftAction;
	}
	
	public Action getQuestionAction() {
		return questionAction;
	}
	
	public Action getLAction() {
		return LAction;
	}
	
	public Action getEnterAction() {
		return enterAction;
	}
	
	public Action getWaitAction() {
		return waitAction;
	}


	public Action getStairsUp() {
		return stairsUp;
	}

	public Action getStairsDown() {
		return stairsDown;
	}


	public Action getCenterMap() {
		return centerMap;
	}


	public Action getOpenDoor() {
		return openDoor;
	}


	public Action getCloseDoor() {
		return closeDoor;
	}

	
	
}
