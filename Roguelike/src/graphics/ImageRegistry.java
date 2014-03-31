package graphics;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * The registry that caches image resources for tiles.
 * Uses singleton desgin pattern
 * @author kta
 *
 */
public class ImageRegistry {

	private static final String INDEX_FILE = "tile_index";

	private static final String[] keywords = {"floor", 
		"topleftcorner", "bottomleftcorner", "toprightcorner", "bottomrightcorner", 
		"frontwall", "leftwall", "rightwall"};
	
	
	//for now, we're pretending to store images
	//Uses a string to refer to the ID of tile.
	private HashMap<String, Image> registry = new HashMap<String, Image>();
	 
	private int[] keywordCount = new int[keywords.length];
	private String dir;
	/**
	 * Loads a themed texture from a folder
	 * expects an index file listing all the string mappings.
	 * @param textureDir
	 */
	public ImageRegistry(String textureDir){

		this.dir = textureDir;
		//open index file
		File indexFile = new File(textureDir + "\\" + INDEX_FILE);
		if (indexFile.exists() == false){
			throw new IllegalArgumentException("Error! Cannot find the index file for the texture folder: " + textureDir);
		}
		
		//read the index file for all mappings
		FileReader fs;
		BufferedReader br;
		try {
			fs = new FileReader(indexFile);
			br = new BufferedReader(fs);
			
			
			String nextLine = br.readLine();
			while(nextLine != null){
				//expects a mapping then the image file name, delimited by comma
				int delim = nextLine.indexOf(",");
				
				//process line as long as there is at least one character for the mapping
				if(delim >=1){
					String[] splitted = nextLine.split(",");
					String key =splitted[0].trim();
					String image = splitted[1].trim();
					
					File imageFile = new File(textureDir + "\\" + image);
					if (imageFile.exists() == true){
						//key is at least length one, and image can be read
						registry.put(key, ImageIO.read(imageFile));
						
						for(int i = 0; i < keywords.length; i++){
							if(key.indexOf(keywords[i]) >=0){
								//is one of the keywords. break when done.
								
								keywordCount[i]++;
								break;
							}
						}
					}
					
					
					
				}
				//skip line if not conforming.
				nextLine = br.readLine();
			}
			
			fs.close();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public Image getTile(String key){
		
		if (registry.containsKey(key) == false){
			System.err.println("Warning! Tile key: "+ key + " was not found in the registry: "+dir);
		}
		return registry.get(key);
	}
	
	public int getSize(){
		return registry.size();
	}
	
	
	
}
