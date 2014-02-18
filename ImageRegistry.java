import java.awt.Image;
import java.util.HashMap;

/**
 * The registry that caches image resources for tiles.
 * @author kta
 *
 */
public class ImageRegistry {

	
	private static ImageRegistry master;
	private static final String PATH = "C:\tiles";
	
	
	//for now, we're pretending to store images
	//Uses a string to refer to the ID of tile.
	private static HashMap<String, Image> registry = new HashMap<String, Image>();
	 
	
	
	/**
	 * Singleton pattern
	 * @return
	 */
	public static ImageRegistry getInstance(){
		if (master == null){
			master = new ImageRegistry();
		}
		
		return master;
	}
	
	private ImageRegistry(){
		//nothing, override default constructor.
	}
	
	public void registerTile(String key, Image img){
		registry.put(key, img);
	}
	
	
	public Image getResource(String key){
		return registry.get(key);
	}
	
	
}
