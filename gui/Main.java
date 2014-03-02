package gui;

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
public class Main extends JFrame {

	private JPanel contentPane;

	
	private static final boolean RESIZEABLE = false;

	
	
	private TileDisplay tileDisplay;
	private Console console;
	private StatusBar statusBar;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
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
	public Main() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 806, 864);
		
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
		
		
		tileDisplay = new TileDisplay();
		tileDisplay.setBounds(0, 132,TileDisplay.WIDTH, TileDisplay.HEIGHT);
		contentPane.add(tileDisplay);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 800, 132);
		contentPane.add(scrollPane);
		
		console = new Console();
		scrollPane.setViewportView(console);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(0, 772, 800, 46);
		contentPane.add(scrollPane_1);
		
		statusBar = new StatusBar();
		scrollPane_1.setViewportView(statusBar);
		try {
			System.out.println(new File("D:\\Users\\Kevin\\GitHub\\599-roguelike\\res\\derp.png").exists());
			BufferedImage derp = ImageIO.read(new File("D:\\Users\\Kevin\\GitHub\\599-roguelike\\res\\derp.png"));
			for (int i = 0; i< 50; i++){ 
				for (int j = 0; j< 40; j++){
					tileDisplay.drawTile(derp, i,j);
					
				}
				console.println("1111111111111111111111111111111112222222222222222222222222222222222222222222222333333333333333333333333333" + i);
			}
			
			tileDisplay.repaint();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
