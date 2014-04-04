package graphics;

import game.Constants;

import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollBar;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;



import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ActionListener;

/**
 * This is a window builder compatible class. Edit with
 * window builder
 * @author Kevin
 *
 */
public class Frame extends JFrame {

	private JPanel contentPane;

	
	private static final boolean RESIZEABLE = false;
	
	
	
	private TileDisplay tileDisplay;
	private PlayerLog console;
	private StatusBar statusBar;
	
	private JScrollBar mapScrHorizontal;
	private JScrollBar mapScrVertical;
	
	private JScrollPane scrollPaneMap;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame frame = new Frame(new TileDisplay(50,40), new PlayerLog(), new StatusBar());
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Frame(TileDisplay display, PlayerLog console, StatusBar status) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 725);
		setTitle(Constants.GAME_NAME);
		
		//========================================================================
		//menu bar items
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);
		
		JMenuItem mntmQuit = new JMenuItem("Quit");
		mntmQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		mnFile.add(mntmQuit);
		
		JMenu mnSettings = new JMenu("Settings");
		menuBar.add(mnSettings);
		
		JMenuItem mntmConfigureKeys = new JMenuItem("Configure keys...");
		mnSettings.add(mntmConfigureKeys);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmAbout = new JMenuItem("About...");
		mnHelp.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		setResizable(RESIZEABLE);
		contentPane.setLayout(null);
		
		//====================================================================================
		
		
		JScrollPane scrollPaneConsole = new JScrollPane();
		scrollPaneConsole.setBounds(0, 0, 894, 132);
		contentPane.add(scrollPaneConsole);
		
		this.console = console;
		scrollPaneConsole.setViewportView(console);
		
		JScrollPane scrollPaneStatusBar = new JScrollPane();
		scrollPaneStatusBar.setBounds(0, 632, 894, 46);
		contentPane.add(scrollPaneStatusBar);
		
		this.statusBar = status;
		scrollPaneStatusBar.setViewportView(statusBar);
		
		scrollPaneMap = new JScrollPane();
		scrollPaneMap.setBounds(0, 131, 894, 500);
		contentPane.add(scrollPaneMap);
		this.tileDisplay = display;
		this.mapScrHorizontal = scrollPaneMap.getHorizontalScrollBar();
		this.mapScrVertical = scrollPaneMap.getVerticalScrollBar();
		scrollPaneMap.setViewportView(tileDisplay);
		scrollPaneMap.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener(){
			@Override
			public void adjustmentValueChanged(AdjustmentEvent arg0) {
				//update the tile display for optimizations, give horizonal position of bar
				int value = mapScrHorizontal.getValue();
				tileDisplay.updateScrollHorizontal(value, value + mapScrHorizontal.getModel().getExtent() + (16*3));
			}
		});
		scrollPaneMap.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener(){

			@Override
			public void adjustmentValueChanged(AdjustmentEvent arg0) {
				//update the tile display for optimizations, give vertcial position of bar
				int value = mapScrVertical.getValue();
				tileDisplay.updateScrollVertical(value, value + mapScrVertical.getModel().getExtent() + (16*3));
			}
		});
		tileDisplay.repaint();
		centerMap(100,100);
	}
	
	/**
	 * Given the tile coordinates of the map
	 * centers the map to that coordinate point.
	 * @param x
	 * @param y
	 */
	public void centerMap(int x, int y){
		Point focus = tileDisplay.getTileAbsolute(x, y);
		mapScrHorizontal.setValue(focus.x-(mapScrHorizontal.getModel().getExtent()/2));
		mapScrVertical.setValue(focus.y-(mapScrVertical.getModel().getExtent()/2));
	}
}
