package graphics;

import game.*;

import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;


import javax.swing.KeyStroke;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;


/**
 * This is a window builder compatible class. Edit with
 * window builder
 * @author Kevin
 *
 */
@SuppressWarnings("serial")
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


		JScrollPane scrollPaneConsole = console;
		scrollPaneConsole.setBounds(0, 0, 894, 132);
		contentPane.add(scrollPaneConsole);

		this.console = console;

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
		tileDisplay.repaint();
	}

	public void registerController(Controller controller){
		// Connect key stroke commands to Messenger actions
		console.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), "quit");
		console.getActionMap().put("quit", controller.getMessenger().getQuitAction());
		console.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "pickup");
		console.getActionMap().put("pickup", controller.getMessenger().getPAction());
		console.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_I, 0), "inventory");
		console.getActionMap().put("inventory", controller.getMessenger().getIAction());
		console.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), "eat");
		console.getActionMap().put("eat", controller.getMessenger().getEAction());
		console.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up");
		console.getActionMap().put("up", controller.getMessenger().getUpAction());
		console.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down");
		console.getActionMap().put("down", controller.getMessenger().getDownAction());
		console.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
		console.getActionMap().put("left", controller.getMessenger().getLeftAction());
		console.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
		console.getActionMap().put("right", controller.getMessenger().getRightAction());
	}

	/**
	 * Given the tile coordinates of the map
	 * centers the map to that coordinate point.
	 * @param x
	 * @param y
	 */
	public void centerMap(int x, int y){
		Point focus = new Point(TileDisplay.TILE_SIZE*x, TileDisplay.TILE_SIZE *y);
		mapScrHorizontal.setValue(focus.x- (scrollPaneMap.getWidth()/2)); 
		mapScrVertical.setValue(focus.y - (scrollPaneMap.getHeight()/2));
	}
}
