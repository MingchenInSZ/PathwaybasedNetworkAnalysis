package serialization.bio.lab;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * 
 * @author mingchen
 * @date May 1st, 2015
 */

public class Serialization {
	private static final String outDir = "dataRepository/serout";

	/**
	 * save the object to OutputStream
	 * 
	 * @param obj
	 *            Object
	 * @param out
	 *            OutputStream
	 * 
	 */
	public static void save(Object obj, OutputStream out) {
		if (obj.equals(null) || out.equals(null)) {
			throw new RuntimeException("The Object and Stream can not be null");
		}
		try {
			ObjectOutputStream oos = new ObjectOutputStream(out);
			oos.writeObject(obj);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * save the Object to file[fullPathName]
	 * 
	 * @param obj
	 *            Object
	 * @param fullPathName
	 *            file Name to store the Object
	 */
	public static void save(Object obj, String fileName) {
		String fullPathName = outDir + File.separator + fileName;
		File file = new File(fullPathName);
		try {
			OutputStream os = new FileOutputStream(file);
			save(obj, os);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * load the object from the file
	 * 
	 * @param fullPathName
	 *            It is the full Name of storing the obj
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("resource")
	public static Object load(String fileName) {
		String fullPathName = outDir + File.separator + fileName;
		File file = new File(fullPathName);
		try {
			InputStream ins = new FileInputStream(file);
			return load(ins);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * load Object from InputStream
	 * 
	 * @param ins
	 *            InputStream
	 * 
	 * @return Object
	 */
	public static Object load(InputStream ins) {
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(ins);
			return ois.readObject();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
		return null;
	}

}
