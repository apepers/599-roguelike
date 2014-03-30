package graphics;

import java.io.File;
import java.util.HashMap;

/**
 * Class that manages each of the tile sets per level.
 * For each set of textures, the folder name of that
 * that contains the indices will be used as the key
 * of that texture.
 * 
 * Singleton Class.
 * @author Kevin
 *
 */
public class ImageManager {

	private static final String RESOURCE_PATH = "\\res";
	private static ImageManager global;
	
	private static HashMap<String, ImageRegistry> tileSets = new HashMap<String, ImageRegistry>();
	
	public static ImageManager getInstance(){
		if (global == null){
			global = new ImageManager("D:\\Users\\Kevin\\GitHub\\599-roguelike\\Roguelike\\src"+ RESOURCE_PATH);
		}
		return global;
	}
	
	
	
	/**
	 * Creates an Image tile manager. Gets tilesets from the
	 * set of
	 * @param path
	 */
	private ImageManager(String path){
		
		
		File resFolder = new File(path);
		if((resFolder.exists()) && (resFolder.isDirectory())){
			//is folder and exists
			File[] fileList = resFolder.listFiles();
			for(int i=0; i< fileList.length; i++){
				if(fileList[i].isDirectory()){
					ImageRegistry reg = new ImageRegistry(fileList[i].getAbsolutePath());
					tileSets.put(fileList[i].getName(), reg);
				}
			}
		}
		else{
			//cannot proceed if there is an error in the Image manager.
			System.err.println("Error! Cannot find the resource folder. Cannot load tiles.");
		}
	}
	
	
	/**
	 * Gets a tileset to work with.
	 * @param key
	 * @return
	 */
	public ImageRegistry getTileSet(String key){
		return tileSets.get(key);
	}
	
	/**
	 * Returns the number of tilesets currently loaded
	 * into memory.
	 * @return
	 */
	public int getSize(){
		return tileSets.size();
	}
	
	public static void main(String[] args){
		
		System.out.println(ImageManager.getInstance().getSize());
		for (ImageRegistry s: tileSets.values()){
			System.out.println(s.getSize());
		}
	}
}
