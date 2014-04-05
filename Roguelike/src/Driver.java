import entities.Player;
import game.Controller;
import game.Messenger;
import graphics.PlayerLog;
import graphics.Frame;
import graphics.StatusBar;
import graphics.TileDisplay;


/**
 * Starts the game.
 * @author Kevin
 *
 */
public class Driver {

	public static void main(String args[]){

		TileDisplay tileDisplay = new TileDisplay(300,300);
		PlayerLog console = new PlayerLog();
		StatusBar status = new StatusBar();
		
		
		Controller controller = Controller.getInstance();
		controller.setup(new Messenger(controller, tileDisplay, console, status), new Player());
		
		
		Frame frame = new Frame(tileDisplay, console, status, controller);
		frame.setVisible(true);
		
		
		
		controller.combatTest();
	}
}
