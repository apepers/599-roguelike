import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

import game.Map;
import graphics.ImageManager;
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
		//MapGenerator demoLevel = new BSTMap(125,125);
		MapGenerator demoLevel = new SimpleMap(15,15,20,20);
		
		TileDisplay tileDisplay = new TileDisplay(300,300);
		PlayerLog console = new PlayerLog();
		StatusBar status = new StatusBar();
		Frame frame = new Frame(tileDisplay, console, status);
		frame.setVisible(true);
		//grab some images
		System.out.println(System.getProperty("user.dir"));
		System.out.println(new File(System.getProperty("user.dir")+"\\res\\derp.png").exists());

		
		
		Map map = MapInterpreter.interpretMap(demoLevel, ImageManager.getInstance().getAllTileSets("map"));
		tileDisplay.drawMap(map);
		tileDisplay.repaint();
		frame.centerMap(map.getSpawn().x, map.getSpawn().y);
		
		//write some messages
		console.println("Welcome to Severed Space!");
		console.println("Testing console");
		
		for(int i = 0; i< 10 ; i++){
			console.println("T = " + (i+1));
		}
		
		status.setText("Player: ");
		
		
	}
}
