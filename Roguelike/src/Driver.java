import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

import game.Map;
import graphics.ImageManager;
import game.Controller;
import graphics.PlayerLog;
import graphics.Frame;
import graphics.StatusBar;
import graphics.TileDisplay;
import mapGeneration.BSTMap;
import mapGeneration.MapGenerator;
import mapGeneration.MapInterpreter;
import mapGeneration.MapTile;
import mapGeneration.SimpleMap;

/**
 * Starts the game.
 * @author Kevin
 *
 */
public class Driver {

	public static void main(String args[]){

		TileDisplay tileDisplay = new TileDisplay(300,300);

		Controller controller = Controller.getInstance();
		controller.setup(tileDisplay);
		
		PlayerLog console = new PlayerLog();
		StatusBar status = new StatusBar();
		Frame frame = new Frame(tileDisplay, console, status, controller);
		frame.setVisible(true);
		
		//write some messages
		console.println("Welcome to Severed Space!");
		console.println("Testing console");
		
		for(int i = 0; i< 10 ; i++){
			console.println("T = " + (i+1));
		}
		
		status.setText(controller.playerStatus());
		
		controller.combatTest();
	}
}
