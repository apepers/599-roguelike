package graphics;

import game.*;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;


import javax.swing.KeyStroke;

import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

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
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Controller controller = new Controller();
				// Setup the game, only continue if it succeeded
				if (!controller.setup()) {
					System.err.println("Setup did not complete successfully. Exiting now.");
					System.exit(0);
				}
				try {
					Frame frame = new Frame(new TileDisplay(50,40), new PlayerLog(), new StatusBar(), controller);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				//controller.run();
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Frame(TileDisplay display, PlayerLog console, StatusBar status, Controller controller) {
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
		controller.getMessenger().setPlayerLog(console); // Set up Messenger to be able to write to console
		// Connect key stroke commands to Messenger actions
		console.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), "quit");
		console.getActionMap().put("quit", controller.getMessenger().getQuitAction());
		console.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "pickup");
		console.getActionMap().put("pickup", controller.getMessenger().getPAction());
		console.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_I, 0), "inventory");
		console.getActionMap().put("inventory", controller.getMessenger().getIAction());
		console.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), "eat");
		console.getActionMap().put("eat", controller.getMessenger().getEAction());
		
		JScrollPane scrollPaneStatusBar = new JScrollPane();
		scrollPaneStatusBar.setBounds(0, 632, 894, 46);
		contentPane.add(scrollPaneStatusBar);
		
		this.statusBar = status;
		scrollPaneStatusBar.setViewportView(statusBar);
		
		JScrollPane scrollPaneMap = new JScrollPane();
		scrollPaneMap.setBounds(0, 131, 894, 500);
		contentPane.add(scrollPaneMap);
		this.tileDisplay = display;
		this.mapScrHorizontal = scrollPaneMap.getHorizontalScrollBar();
		this.mapScrVertical = scrollPaneMap.getVerticalScrollBar();
		scrollPaneMap.setViewportView(tileDisplay);
		scrollPaneMap.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener(){
			@Override
			public void adjustmentValueChanged(AdjustmentEvent arg0) {
				tileDisplay.updateScrollHorizontal(mapScrHorizontal.getValue(), mapScrHorizontal.getModel().getExtent());
			}
		});
		scrollPaneMap.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener(){

			@Override
			public void adjustmentValueChanged(AdjustmentEvent arg0) {
				tileDisplay.updateScrollVertical(mapScrVertical.getValue(), mapScrVertical.getModel().getExtent());
			}
		});
		tileDisplay.repaint();
		
	}
	
}
