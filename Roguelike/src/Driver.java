import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

import graphics.PlayerLog;
import graphics.Frame;
import graphics.StatusBar;
import graphics.TileDisplay;

import mapGeneration.Map;
import mapGeneration.MapTile;




/**
 * Starts the game.
 * @author Kevin
 *
 */
public class Driver {

	public static void main(String args[]){
		Map demoLevel = new Map(50,40);
		
		
		TileDisplay tileDisplay = new TileDisplay(50,40);
		PlayerLog console = new PlayerLog();
		StatusBar status = new StatusBar();
		Frame frame = new Frame(tileDisplay, console, status);
		frame.setVisible(true);
		try {
			
			//grab some images
			System.out.println(System.getProperty("user.dir"));
			System.out.println(new File(System.getProperty("user.dir")+"\\res\\derp.png").exists());
			BufferedImage wall = ImageIO.read(new File(System.getProperty("user.dir")+"\\res\\wall.png"));
			BufferedImage floor = ImageIO.read(new File(System.getProperty("user.dir")+"\\res\\floor.png"));
			BufferedImage space = ImageIO.read(new File(System.getProperty("user.dir")+"\\res\\space.png"));
			
			for (int i = 0; i< tileDisplay.getxCells(); i++){ 
				for (int j = 0; j< tileDisplay.getyCells(); j++){
					//interpet some walls/floor/space
					if(demoLevel.getTile(i, j) == MapTile.FLOOR){
						tileDisplay.drawTile(floor, i,j);
					}
					else if (demoLevel.getTile(i, j) == MapTile.WALL){
						tileDisplay.drawTile(wall, i,j);
					}
					else {
						tileDisplay.drawTile(space, i,j);
					}
					
					
				}
			}
			tileDisplay.repaint();
			
			
			//write some messages
			console.println("Welcome to Severed Space!");
			console.println("Testing console");
			
			for(int i = 0; i< 10 ; i++){
				console.println("T = " + (i+1));
			}
			
			status.setText("Player: ");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
