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

public class Main extends JFrame {

	private JPanel contentPane;

	
	private static final boolean RESIZEABLE = false;
	private static final int FRAME_WIDTH = 600;
	private static final int FRAME_HEIGHT = 400;
	
	
	private TileDisplay tileDisplay;
	
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
		setBounds(100, 100, FRAME_WIDTH, FRAME_HEIGHT);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setResizable(RESIZEABLE);
		
		tileDisplay = new TileDisplay();
		contentPane.add(tileDisplay);
		try {
			System.out.println(new File("D:\\Users\\Kevin\\GitHub\\599-roguelike\\res\\derp.png").exists());
			BufferedImage derp = ImageIO.read(new File("D:\\Users\\Kevin\\GitHub\\599-roguelike\\res\\derp.png"));
			for (int i = 0; i< 40; i++){
				for (int j = 0; j< 40; j++){
					tileDisplay.drawTile(derp, i,j);
				}
			}
			
			tileDisplay.repaint();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
