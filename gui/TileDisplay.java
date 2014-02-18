package gui;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.JPanel;


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
public class TileDisplay extends JPanel{
	
	
	//dimensions of grid.
	
	
	
	private static final int X_CELLS_DEFAULT = 40;
	private static final int Y_CELLS_DEFAULT = 40;
	private static final int WIDTH_DEFAULT = 800;
	private static final int HEIGHT_DEFAULT = 600;
	
	private static final boolean DOUBLE_BUFFERING = true;
	
	private static final Color BACKGROUND = Color.BLACK;
	
	
	private int xCells;
	private int yCells;
	private int width;
	private int height;
	
	
	private Image[][] buffer;

	
	/**
	 * Default constructor for the display
	 */
	public TileDisplay(){
		this(WIDTH_DEFAULT, HEIGHT_DEFAULT, X_CELLS_DEFAULT, Y_CELLS_DEFAULT);
		
	}

	

	/**
	 * Creates a display with the given width and height.
	 * Then the display is then given a partitioning of the
	 * screen space.
	 * @param width in pixels
	 * @param height in pixels
	 * @param xCells cells in
	 */
	public TileDisplay(int width, int height, int xCells, int yCells){
		super(DOUBLE_BUFFERING);			//enable double buffering
	
		
		
		
		//intantiate variables
		this.xCells = xCells;
		this.yCells = yCells;
		this.width = width;
		this.height = height;
	
		//drawing canvas
		buffer = new Image[xCells][yCells];
		
		
		//clear all cells.
		for (int i = 0; i< xCells; i++){
			for (int j = 0; j< yCells; j++){
				
			}
		}
		
		repaint();
	}
	
	
	/**
	 * Draws a tile onto the screen at the specified cell coordinate.
	 * Updated on next frame.
	 * No screen update is done. See TileDisplay.update()
	 * @param x cell coordinate
	 * @param y cell coordinate
	 * @see TileDisplay.update()
	 */
	public void drawTile(Image tile, int x, int y){
		buffer[x][y] = tile;
		
	}
	
	
	
	public void clearTile(int x, int y){
		
	}
	
	/**
	 * Gets the location on the game where to draw the image.
	 * @param x
	 * @param y
	 * @return Top left corner of the cell is returned.
	 */
	private Point getCellLocation(int x, int y){
		int deltaX = height/xCells;
		int deltaY = width/yCells;
		
		return new Point(x*deltaX, y*deltaY);
	}
	
	
	
	/**
	 * Main drawing method. Posts all tile information to screen
	 */
	@Override
	public void paint(Graphics g){
		super.paintComponent(g);
		
		//redraw only the tiles that have been updated.
		for (int i = 0; i< xCells; i++){
			for (int j = 0; j< yCells; j++){
				Point location = getCellLocation(i, j);
				g.drawImage(buffer[i][j], location.x, location.y, BACKGROUND, null);
			}
		}
	}

}
