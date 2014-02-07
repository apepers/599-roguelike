import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


/**
 * The display class. A low level implementation of the
 * interface used to display graphical tiles of the
 * main game onto the screen.
 * Not necessarily the text display. 
 * @author kta
 *
 */
public class Display {
	
	
	//dimensions of grid.
	
	private BufferedImage buffer;
	
	private int xCells;
	private int yCells;
	
	
	public Display(){
		Display(Constants.X_CELLS, Constants.Y_CELLS);
		Graphics2D derp = new Graphics2D();
	}
	
	/**
	 * Creates a display with the given width and height.
	 * Then the display is then given a partitioning of the
	 * screen space.
	 * @param width in pixels
	 * @param height in pixels
	 */
	public Display(int width, int height, int xCells, int yCells){
		buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		this.xCells = xCells;
		this.yCells = yCells;
		
	
	}
	
	
	/**
	 * Draws a tile onto the screen at the specified coordinate
	 * @param x
	 * @param y
	 */
	public void drawTile(int tile, int x, int y){
		
		
	}
	
	/**
	 * Redraws the screen 
	 */
	public void update(){
	}

}
