package graphics;
import game.Map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;



import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


/**
 * The display class. A low level implementation of the
 * interface used to display graphical tiles of the
 * main game onto the screen.
 * Not necessarily the text display. 
 * 
 * The origin starts at the top left.
 * @author kta
 *
 */
@SuppressWarnings("serial")
public class TileDisplay extends JPanel{


	//dimensions of grid.

	private static final int X_CELLS_DEFAULT = 50;
	private static final int Y_CELLS_DEFAULT = 40;
	public static final int TILE_SIZE = 16; 					//in pixels, assume square tiles

	private static final boolean DOUBLE_BUFFERING = true;

	private static final Color BACKGROUND = Color.BLACK;
	private static ImageIcon BLANK_TILE_IMAGE = createBlank();

	private int xCells;
	private int yCells;

	private int width;						//in pixels
	private int height;						//in pixels


	private BufferedImage buffer;



	private Map currentMap;

	/**
	 * Default constructor for the display
	 */
	public TileDisplay(){
		this(X_CELLS_DEFAULT, Y_CELLS_DEFAULT);
	}



	/**
	 * Creates a display with the given width and height.
	 * Then the display is then given a partitioning of the
	 * screen space.
	 * @param width in pixels
	 * @param height in pixels
	 * @param xCells cells in
	 */
	public TileDisplay(int xCells, int yCells){
		super(DOUBLE_BUFFERING);			//enable double buffering

		super.setBackground(BACKGROUND);


		//Instantiate variables
		this.xCells = xCells;
		this.yCells = yCells;
		this.width = xCells * TILE_SIZE;
		this.height = yCells * TILE_SIZE;


		//drawing canvas
		buffer = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);


		//clear all cells.
		clearDisplay();

		this.setPreferredSize(new java.awt.Dimension(width,height));

		repaint();
	}


	/**
	 * Clears the display of all tiles
	 */
	public void clearDisplay(){
		Graphics pane = buffer.getGraphics();

		for (int i = 0; i< xCells; i++){
			for (int j = 0; j< yCells; j++){
				pane.drawImage(BLANK_TILE_IMAGE.getImage(), i*TILE_SIZE, j*TILE_SIZE, null);
			}
		}

		repaint();
	}



	/**
	 * Draws a tile onto the screen at the specified cell coordinate.
	 * Updated on next frame.
	 * No screen update is done. See super.repaint()
	 * @param x cell coordinate
	 * @param y cell coordinate
	 * @see super.repaint()
	 */
	public void drawTile(ImageIcon tile, int x, int y){
		Graphics pane = buffer.getGraphics();
		if (tile != null){
			pane.drawImage(tile.getImage(), x*TILE_SIZE, y*TILE_SIZE, null);
		}


	}


	/**
	 * Updates a map tile on the screen
	 * Display is refreshed.
	 * 
	 * The method assumes that the background tile covers the
	 * entire tile. 
	 * @param x
	 * @param y
	 */
	public void refreshTile(int x, int y){

		
		drawTile(currentMap.getTile(x, y).getBackground(), x, y);
		drawTile(currentMap.getTile(x, y).getTopItemImage(), x, y);
		drawTile(currentMap.getTile(x, y).getOccupantImage(), x, y);

		//update now
		repaintSuper();

	}

	/**
	 * Clears a tile to the background color
	 * @param x
	 * @param y
	 */
	public void clearTile(int x, int y){
		drawTile(BLANK_TILE_IMAGE,x,y);

	}


	/**
	 * This method calls the super's repaint method for a 
	 * later time.
	 */
	public void repaint(){
		//update later
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				repaintSuper();
			}

		});
	}
	private void repaintSuper(){
		super.repaint();
	}



	/**
	 * Main drawing method. Posts all tile information to screen
	 */
	@Override
	public void paintComponent(Graphics g){

		g.drawImage(buffer, 0, 0, null);

	}
	/**
	 * Loads an entire map into the tile display.
	 * @param map
	 */
	public void drawMap(Map map){
		clearDisplay();

		this.currentMap = map;

		//fill array
		for (int i = 0; i< map.getWidth(); i++){
			for (int j = 0; j< map.getHeight(); j++){
				drawTile(map.getTile(i, j).getBackground(), i, j);
				drawTile(map.getTile(i, j).getTopItemImage(), i, j);
				drawTile(map.getTile(i, j).getOccupantImage(), i, j);
			}
		}

		this.width = map.getWidth() * TILE_SIZE;
		this.height = map.getHeight() * TILE_SIZE;
		this.setPreferredSize(new java.awt.Dimension(width,height));


		repaintSuper();

	}


	//==============================================================================================
	// Getter and setters
	public int getXCells() {
		return xCells;
	}

	public int getYCells() {
		return yCells;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}


	public Point getTileAbsolute(int x, int y){
		return new Point(x*TILE_SIZE, y*TILE_SIZE);
	}


	/**
	 * Creates a blank image icon that is the default background color
	 * @return
	 */
	private static ImageIcon createBlank(){
		BufferedImage blank = new BufferedImage(TILE_SIZE, TILE_SIZE, BufferedImage.TYPE_INT_RGB);
		blank.getGraphics().fillRect(0, 0, TILE_SIZE, TILE_SIZE);

		return new ImageIcon(blank);
	}
}
