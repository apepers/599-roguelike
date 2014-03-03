package graphics;

import game.Constants;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.ScrollPane;

import javax.swing.JScrollPane;
import javax.swing.AbstractAction;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JTextArea;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
		setBounds(100, 100, 806, 864);
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
		scrollPaneConsole.setBounds(0, 0, 800, 132);
		contentPane.add(scrollPaneConsole);
		
		this.console = console;
		scrollPaneConsole.setViewportView(console);
		
		JScrollPane scrollPaneStatusBar = new JScrollPane();
		scrollPaneStatusBar.setBounds(0, 772, 800, 46);
		contentPane.add(scrollPaneStatusBar);
		
		this.statusBar = status;
		scrollPaneStatusBar.setViewportView(statusBar);
		
		JScrollPane scrollPaneMap = new JScrollPane();
		scrollPaneMap.setBounds(0, 131, 800, 640);
		contentPane.add(scrollPaneMap);
		
		
		
		
		
		this.tileDisplay = display;
		scrollPaneMap.setViewportView(tileDisplay);
		
		tileDisplay.repaint();
		
	}
	
}
