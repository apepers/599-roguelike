package game;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;



/**
 * Gets game text.
 * @author Kevin
 *
 */
public class GameText {
	
	
	private static final String TEXT_FOLDER = System.getProperty("user.dir") + File.separator +"text";
	
	private static HashMap<String, String> textMapping = new HashMap<String, String>();

	public static GameText global;
	
	public static GameText getInstance(){
		if (global == null){
			global = new GameText();
		}
		return global;
	}
	
	public static void initInstance(){
		if (global == null){
			global = new GameText();
		}
	}
	
	private GameText(){

		//open the folder
		File textFolder = new File(TEXT_FOLDER);
		
		if((textFolder.exists()) && (textFolder.isDirectory())){
			//is folder and exists
			File[] fileList = textFolder.listFiles();
			for(int i=0; i< fileList.length; i++){
				if(fileList[i].isFile()){
					//is file, add as text file
					FileReader fs;
					BufferedReader br;
					try {
						fs = new FileReader(fileList[i]);
						br = new BufferedReader(fs);

						String complete = "";
						String nextLine = br.readLine();
						while(nextLine != null){
							//expects a mapping then the image file name, delimited by comma
							complete = complete + nextLine + "\n";
							//skip line if not conforming.
							nextLine = br.readLine();
						}

						//done, add text to the keys.
						textMapping.put(fileList[i].getName(), complete);
						
						fs.close();
						br.close();
					} catch (IOException e) {
						System.out.println("Warning! Failed to add a text file.");
						e.printStackTrace();
					}
					
				}
			}
			
		}
	}
	
	
	public static String getText(String key){
		String text = textMapping.get(key);
		return ((text == null) ? "TEXT NOT FOUND!" : text);
	}
	
	public int getCount(){
		return textMapping.size();
	}
}
