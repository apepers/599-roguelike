package graphics;
import game.Map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;


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

	//optimization controls.
	private int xScrMin;
	private int xScrMax;
	private int yScrMin;
	private int yScrMax;

	private int xCells;
	private int yCells;

	private int width;						//in pixels
	private int height;						//in pixels


	private Image[][] buffer;


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

		this.xScrMin =0;
		this.xScrMax = width;
		this.yScrMin =0;
		this.yScrMax = height;

		//drawing canvas
		buffer = new Image[xCells][yCells];


		//clear all cells.
		clearDisplay();

		this.setPreferredSize(new java.awt.Dimension(width,height));

		repaint();
	}


	/**
	 * Clears the display of all tiles
	 */
	public void clearDisplay(){
		for (int i = 0; i< xCells; i++){
			for (int j = 0; j< yCells; j++){
				buffer[i][j] = null;
			}
		}
	}



	/**
	 * Draws a tile onto the screen at the specified cell coordinate.
	 * Updated on next frame.
	 * No screen update is done. See super.repaint()
	 * @param x cell coordinate
	 * @param y cell coordinate
	 * @see super.repaint()
	 */
	public void drawTile(Image tile, int x, int y){
		buffer[x][y] = tile;

	}


	/**
	 * Clears a tile to the background color
	 * @param x
	 * @param y
	 */
	public void clearTile(int x, int y){
		buffer[x][y] = null;
	}

	/**
	 * Gets the location on the game where to draw the image.
	 * @param x
	 * @param y
	 * @return Top left corner of the cell is returned.
	 */
	private Point getCellLocation(int x, int y){
		int deltaX = TILE_SIZE;
		int deltaY = TILE_SIZE;

		return new Point(x*deltaX, y*deltaY);
	}


	/**
	 * Updates the horizontal viewable area of the tile display as to not
	 * waste time repainting unseen tiles.
	 * 
	 * Parameters expected to the clipping area of the tile display
	 * @param min
	 * @param max
	 */
	protected void updateScrollHorizontal(int min, int max){
		xScrMin = min;
		xScrMax = max;

		//update later
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				repaint();
			}

		});
	}

	/**
	 * Updates the vertical viewable area of the tile display as to not
	 * waste time repainting unseen tiles.
	 * 
	 * Parameters expected to be the clipping area of the tile display.
	 * @param min
	 * @param max
	 */
	protected void updateScrollVertical(int min, int max){
		yScrMin = min;
		yScrMax = max;

		//update later
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				repaint();
			}

		});
	}

	public void repaint(){
		super.repaint();
	}



	/**
	 * Main drawing method. Posts all tile information to screen
	 */
	@Override
	public void paintComponent(Graphics g){

		//redraw only the tiles that have been updated.
		for (int i = xScrMin/TILE_SIZE; i< Math.min(xScrMax/TILE_SIZE, xCells); i++){
			for (int j = yScrMin/TILE_SIZE; j< Math.min(yScrMax/TILE_SIZE, yCells); j++){
				Point location = getCellLocation(i, j);
				g.drawImage(buffer[i][j], location.x, location.y, BACKGROUND, null);
			}
		}
	}
	/**
	 * Loads an entire map into the tile display.
	 * @param map
	 */
	public void drawMap(Map map){
		clearDisplay();

		//fill array
		for (int i = 0; i< map.getWidth(); i++){
			for (int j = 0; j< map.getHeight(); j++){
				buffer[i][j] = map.getTile(i, j).getBackground();
			}
		}

		this.width = map.getWidth() * TILE_SIZE;
		this.height = map.getHeight() * TILE_SIZE;
		this.setPreferredSize(new java.awt.Dimension(width,height));

		//update later
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				repaint();
			}

		});
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

}
