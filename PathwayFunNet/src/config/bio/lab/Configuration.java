package config.bio.lab;

/**
 * @author mingchen
 * @date May 1st, 2015
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class Configuration {

	private final Properties properties;

	public Configuration(){

		properties = new Properties();
	}

	/**
	 * Load properties content
	 * 
	 * @param fileName
	 */
	public Configuration(String fileName) {
		properties = new Properties();
		try {
			FileInputStream fis = new FileInputStream(fileName);
			properties.load(fis);
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * get the value according to the key
	 * 
	 * @param key
	 * @return
	 */
	public String getValues(String key) {
		String value = "";
		if (properties.containsKey(key)) {
			value = properties.getProperty(key);
		} else {
			return null;
		}
		return value;
	}

	/**
	 * set key:value and save to properties file
	 * 
	 * @param key
	 * @param value
	 */
	public void setValue(String key, String value) {
		try {
			FileOutputStream fos = new FileOutputStream("project.properties");
			properties.setProperty(key, value);
			properties.store(fos, "Set Key:" + key + ",Value:" + value);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * set key:value by dictionary
	 * 
	 * @param dict
	 * @param comment
	 */
	public void setValues(HashMap<String, String> dict, String comment) {
		if (!dict.isEmpty()) {
			try {
				FileOutputStream fos = new FileOutputStream("project.properties",true);
				for (String key : dict.keySet()) {
					properties.setProperty(key, dict.get(key));
				}
				properties.store(fos, comment);
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * save key:value by properties
	 * 
	 * @param prs
	 * @param comments
	 */
	public void setValues(Properties prs, String comments) {
		if(!prs.isEmpty()){
			try {
				FileOutputStream fos = new FileOutputStream("project.properties",true);
				for (Object key : prs.keySet()) {
					properties.setProperty(key.toString(), prs.getProperty(key.toString()));
				}
				properties.store(fos, comments);
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * set key:value to fileName(properties)
	 * 
	 * @param fileName
	 * @param key
	 * @param value
	 */
	public void setValue(String fileName, String key, String value) {
		Properties property = new Properties();
		try {
			FileOutputStream fos = new FileOutputStream(fileName);
			property.setProperty(key, value);
			property.store(fos, "set Key:" + key + ",Value:" + value);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	/**
	 * Clear cache in properties
	 */
	public void clear() {
		properties.clear();
	}

	public static void main(String[] args) {
		Configuration conf = new Configuration("project.properties");
		Properties pros = new Properties();
		pros.setProperty("DBDriver", "com.mysql.jdbc.Driver");
		pros.setProperty("DBHost", "localhost");
		pros.setProperty("DBName", "geneonto");
		pros.setProperty("DBPort", "3306");
		pros.setProperty("User", "root");
		pros.setProperty("Passwd", "aaa");
		conf.setValues(pros, "DB connection Information");

	}
}
