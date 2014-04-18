package graphics;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import entities.Player;


@SuppressWarnings("serial")
public class StatusBar extends JTextPane{
	private Player player;
	
	public StatusBar(Player p){
		super.setEditable(false);
		super.setFocusable(false);
		super.setFont(new Font("default", Font.BOLD, super.getFont().getSize()));
		player = p;
	}
	
	public void updateText() {
		Highlighter.HighlightPainter hpPainter = new DefaultHighlighter.DefaultHighlightPainter(player.hpColor());
		Highlighter.HighlightPainter nutritionPainter = new DefaultHighlighter.DefaultHighlightPainter(player.hungerColor());
		int hpStartMarker = 0;
		int hpEndMarker = 0;
		int nutritionStartMarker = 0;
		int nutritionEndMarker = 0;
		String text = "Player: ";
		text += "HP = ";
		hpStartMarker = text.length();
		text += player.getCurrentHP();
		hpEndMarker = text.length();
		text += ", Strength = " + player.getStrength();
		text += ", Dexterity = " + player.getDexterity();
		text += ", Armour: " + player.getACBonus();
		text += ", Nutrition = ";
		nutritionStartMarker = text.length();
		text += player.hungerText();
		nutritionEndMarker = text.length();
		text += ", XP = " + player.getXP();
		setText(text);
		try {
			this.getHighlighter().addHighlight(hpStartMarker, hpEndMarker, hpPainter);
			this.getHighlighter().addHighlight(nutritionStartMarker, nutritionEndMarker, nutritionPainter);
		} catch (BadLocationException e) {	
		}
	}
	
	public void setText(String text){
		super.setText(text);
	}
}
