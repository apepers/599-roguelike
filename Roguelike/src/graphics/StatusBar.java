package graphics;

import java.awt.Font;

import javax.swing.JTextArea;


public class StatusBar extends JTextArea{

	public StatusBar(){
		super.setEditable(false);
		super.setFocusable(false);
		super.setFont(new Font("default", Font.BOLD, super.getFont().getSize()));
	}
	
	public void setText(String text){
		super.setText(text);
	}
}
