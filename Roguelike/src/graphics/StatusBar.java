package graphics;

import javax.swing.JTextArea;


public class StatusBar extends JTextArea{

	public StatusBar(){
		super.setEditable(false);
	}
	
	public void setText(String text){
		super.setText(text);
	}
}
