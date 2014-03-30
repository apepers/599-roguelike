import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

import graphics.PlayerLog;
import graphics.Frame;
import graphics.StatusBar;
import graphics.TileDisplay;
import mapGeneration.BSTMap;
import mapGeneration.MapGenerator;
import mapGeneration.MapTile;
import mapGeneration.SimpleMap;




/**
 * Starts the game.
 * @author Kevin
 *
 */
public class Driver {

	public static void main(String args[]){
		MapGenerator demoLevel = new BSTMap(125,125);
		
		
		TileDisplay tileDisplay = new TileDisplay(125,125);
		PlayerLog console = new PlayerLog();
		StatusBar status = new StatusBar();
		Frame frame = new Frame(tileDisplay, console, status);
		frame.setVisible(true);
		try {
			
			//grab some images
			System.out.println(System.getProperty("user.dir"));
			System.out.println(new File(System.getProperty("user.dir")+"\\res\\derp.png").exists());
			BufferedImage derp = ImageIO.read(new File(System.getProperty("user.dir")+"\\res\\derp.png"));
			BufferedImage wall = ImageIO.read(new File(System.getProperty("user.dir")+"\\res\\wall.png"));
			BufferedImage floor = ImageIO.read(new File(System.getProperty("user.dir")+"\\res\\floor.png"));
			BufferedImage space = ImageIO.read(new File(System.getProperty("user.dir")+"\\res\\space.png"));
			
			for (int i = 0; i< tileDisplay.getXCells(); i++){ 
				for (int j = 0; j< tileDisplay.getYCells(); j++){
					//interpet some walls/floor/space
					if(demoLevel.getTile(i, j) == MapTile.ROOM_FLOOR){
						tileDisplay.drawTile(floor, i,j);
					}
					else if  (demoLevel.getTile(i, j) == MapTile.CORRIDOR_FLOOR){
						tileDisplay.drawTile(derp, i, j);
					}
					else if ((demoLevel.getTile(i, j) == MapTile.WALL_H) || (demoLevel.getTile(i, j) == MapTile.WALL_V)){
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
