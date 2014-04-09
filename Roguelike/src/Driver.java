import entities.Player;
import game.Controller;
import game.GameText;
import game.Messenger;
import graphics.ImageManager;
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

		//load image resources
		ImageManager.initInstance();
		GameText.initInstance();
		//intialize GUI elements
		TileDisplay tileDisplay = new TileDisplay(300,300);
		PlayerLog console = new PlayerLog();
		StatusBar status = new StatusBar();
		Frame frame = new Frame(tileDisplay, console, status);
		
		Player player = new Player();
		
		Controller controller = Controller.getInstance();
		controller.setup(new Messenger(controller, player, frame, tileDisplay, console, status), player);
		frame.registerController(controller);
		
		//show game
		frame.setVisible(true);
		
		frame.centerMap(player.getLocation().getColumn(), player.getLocation().getRow());
		
		
		controller.startGame();
	}
}
