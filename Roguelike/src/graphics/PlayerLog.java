package graphics;

import java.util.LinkedList;

import javax.swing.JTextArea;


/**
 * The console class that outputs text.
 * 
 * @author Kevin
 *
 */
public class PlayerLog extends JTextArea {

	
	public static final int MAX_LINES = 200;			//inclusive
	
	private LinkedList<Integer> messages;
			
	private int totalLines = 0;
	
	public PlayerLog(){
		super.setEditable(false);
		
		totalLines = 0;
		messages = new LinkedList<Integer>();
	}
	
	
	/**
	 * Prints the text and terminates with a new line.
	 */
	public void println(String text){
		
		
		if (totalLines < MAX_LINES){
			//can still add easily.
			messages.add(text.length());
			
			
			super.setText(super.getText() + text + "\n");
			totalLines++;
		}
		else{
			//remove the least recent message and write new line
			String oldText = super.getText();
			int startPos = messages.remove() +1;
			
			
			super.setText(oldText.substring(startPos, oldText.length()) + text + "\n");
			messages.add(text.length());
		}
		
		
		
	}
	
}
