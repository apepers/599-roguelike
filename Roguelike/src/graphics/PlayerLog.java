package graphics;

import java.util.LinkedList;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


/**
 * The console class that outputs text.
 * 
 * @author Kevin
 *
 */
public class PlayerLog extends JScrollPane {


	public static final int MAX_LINES = 200;			//inclusive

	private LinkedList<Integer> messages;

	private int totalLines = 0;
	
	private JTextArea textArea;
	private JScrollBar scroller;
	
	
	public PlayerLog(){

		//create the text area
		this.textArea = new JTextArea();
		this.textArea.setEditable(false);
		this.setViewportView(textArea);
		this.scroller = super.getVerticalScrollBar();
		
		this.totalLines = 0;
		this.messages = new LinkedList<Integer>();

	}


	/**
	 * Prints the text and terminates with a new line.
	 */
	public void println(String text){


		if (totalLines < MAX_LINES){
			//can still add easily.
			messages.add(text.length());


			textArea.setText(textArea.getText() + text + "\n");
			totalLines++;
		}
		else{
			//remove the least recent message and write new line
			String oldText = textArea.getText();
			int startPos = messages.remove() +1;


			textArea.setText(oldText.substring(startPos, oldText.length()) + text + "\n");
			messages.add(text.length());
		}
		

		//scroll to bottom after text print
		scroller.setValue(scroller.getModel().getMaximum());

	}


	/**
	 * Scrolls the log to the very bottom manually.
	 */
	public void scrollBottom(){
		scroller.setValue(scroller.getModel().getMaximum());
	}

}
