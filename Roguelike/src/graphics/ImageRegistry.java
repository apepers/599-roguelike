package graphics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * The registry that caches image resources for tiles.
 * 
 * The index file is a simple key->value list of items
 * The first value is the key used in the game as a way to refer
 * to the specific tiles. The value corresponds to the location of the 
 * image mapped to the key. This pair is delimited by a comma, and is whitespace
 * safe.
 * 
 * Each line represents a single key mapping. Any invalid lines with missing files
 * or incorrect syntax is not consumed.
 * 
 * Name this file as INDEX_FILE below and place in the directory of the image set.
 * @author kta
 *
 */
public class ImageRegistry {

	private static final String INDEX_FILE = "tile_index";

	private static final String[] keywords = {"floor", 
		"topleftcorner", "bottomleftcorner", "toprightcorner", "bottomrightcorner", 
		"frontwall", "leftwall", "rightwall", 
		"frontdooropen", "leftdooropen", "rightdooropen","leftdoorclosed", "rightdoorclosed", "frontdoorclosed"};


	//for now, we're pretending to store images
	//Uses a string to refer to the ID of tile.
	private HashMap<String, ImageIcon> registry = new HashMap<String, ImageIcon>();

	private HashMap<String, Integer> keywordCount = new HashMap<String, Integer>();
	private String dir;
	/**
	 * Loads a themed texture from a folder
	 * expects an index file listing all the string mappings.
	 * @param textureDir
	 */
	public ImageRegistry(String textureDir){

		initialize();

		this.dir = textureDir;
		//open index file
		File indexFile = new File(textureDir + "\\" + INDEX_FILE);
		if (indexFile.exists() == false){
			System.err.println("Warning! Cannot find the index file for the texture folder: " + textureDir + ". Tile set not added.");
		}
		else{
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
						String key = splitted[0].trim();
						String image = splitted[1].trim();

						File imageFile = new File(textureDir + "\\" + image);
						if (imageFile.exists() == true){
							//key is at least length one, and image can be read
							registry.put(key, new ImageIcon(ImageIO.read(imageFile)));

							for(int i = 0; i < keywords.length; i++){

								int index = key.indexOf(keywords[i]);
								int nextChar = index + keywords[i].length();
								if((index>=0) && (nextChar < key.length()) && (Character.isDigit(key.charAt(nextChar)))){
									//is one of the keywords. break when done.
									Integer current = keywordCount.get(keywords[i]);
									current++;
									keywordCount.put(keywords[i], current);
									break;
								}
							}
						}
						else{
							System.err.println("Warning! Invalid key->image mapping detected in index file from " + textureDir + " that does not exist for file: " + nextLine);
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
	}

	/**
	 * Add all the keywords to the hashtable and intialize their integers.
	 */
	private void initialize(){
		for(int i =0; i < keywords.length; i++){
			keywordCount.put(keywords[i], 0);
		}
		
	}

	/**
	 * Gets a tile given by the key. Returns null if
	 * the key is not in the registry.
	 * @param key
	 * @return
	 */
	public ImageIcon getTile(String key){

		if (registry.containsKey(key) == false){
			System.err.println("Warning! Tile key: "+ key + " was not found in the registry: "+dir);
		}
		return registry.get(key);
	}

	/**
	 * Gets the keyword count of the specified keywords.
	 * Used to get the total number of keys that start with the
	 * keyword. Append integer at the end of keyword.
	 * This is then used to randomize tiles.
	 * @param keyword
	 * @return
	 */
	public int keyCount(String keyword){
		return keywordCount.get(keyword);
	}



	public int getSize(){
		return registry.size();
	}



}
