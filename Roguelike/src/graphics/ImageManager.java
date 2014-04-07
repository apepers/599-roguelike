package graphics;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

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
	
	//global tileset accessable regardless of tilesets.
	public static final String GLOBAL_KEY = "global";
	private static ImageRegistry globalReg;
	
	
	public static ImageManager getInstance(){
		if (global == null){
			global = new ImageManager(System.getProperty("user.dir") + RESOURCE_PATH);
		}
		return global;
	}
	
	public static void initInstance(){
		if (global == null){
			global = new ImageManager(System.getProperty("user.dir") + RESOURCE_PATH);
		}
	}
	
	/**
	 * Creates an Image tile manager. Gets tilesets from the
	 * set of
	 * @param path
	 */
	private ImageManager(String path){
		
		
		File resFolder = new File(path);
		//go through each folder on the top layer.
		if((resFolder.exists()) && (resFolder.isDirectory())){
			//is folder and exists
			File[] fileList = resFolder.listFiles();
			for(int i=0; i< fileList.length; i++){
				if(fileList[i].isDirectory()){
					ImageRegistry reg = new ImageRegistry(fileList[i].getAbsolutePath());
					tileSets.put(fileList[i].getName(), reg);
				}
			}
			
			//finally create the global tile set for tiles in the res folder.
			globalReg = new ImageRegistry(path);
		}
		else{
			//cannot proceed if there is an error in the Image manager.
			System.err.println("Error! Cannot find the resource folder. Cannot load tiles.");
		}
	}
	
	
	
	
	public static ImageRegistry getGlobalRegistry(){
		return globalReg;
	}
	
	/**
	 * Gets a tileset to work with.
	 * @param key
	 * @return
	 */
	public ImageRegistry getTileSet(String key){
		return tileSets.get(key);
	}
	
	public ImageRegistry[] getAllTileSets(){
		ImageRegistry[] all = new ImageRegistry[tileSets.size()];
		tileSets.values().toArray(all);
		return all;
	}
	
	/**
	 * Gets all tile sets that have the substring provided.
	 * Key comparison not case senstive.
	 * @param simularKey
	 * @return
	 */
	public ImageRegistry[] getAllTileSets(String simularKey){
		LinkedList<ImageRegistry> results = new LinkedList<ImageRegistry>();
		
		//search all keys for simularity
		for (String key : tileSets.keySet()){
			if (key.toLowerCase().indexOf(simularKey.toLowerCase()) >=0){
				results.add(tileSets.get(key));
			}
		}
		
		//return result
		ImageRegistry[] all = new ImageRegistry[results.size()];
		results.toArray(all);
		return all;
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
