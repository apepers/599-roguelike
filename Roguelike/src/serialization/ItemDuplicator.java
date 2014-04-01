package serialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ItemDuplicator {

	public Object duplicate(Object object) {
		try
	      {
			
			File f = new File("copy.ser");
			
			FileOutputStream fileOut = new FileOutputStream("copy.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(object);
			out.close();
			fileOut.close();
			 
			FileInputStream fileIn = new FileInputStream("copy.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			Object newObject = in.readObject();
			in.close();
			fileIn.close();
			
			System.out.println(f.delete());
 
 return newObject;
	      }catch(Exception e)
	      {
	          e.printStackTrace();
	          return null;
	      }
		
	}
}
