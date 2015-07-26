package inoutput.bio.lab;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;

import logging.bio.lab.LogUtils;
import serialization.bio.lab.Serialization;

import com.csvreader.CsvWriter;

/**
 * output the network to csv file
 * 
 * @author mingchen
 * @date 2015Äê5ÔÂ3ÈÕ
 */
public class NetworkOutput {

	private static final String wdir = "dataRepository/results";
	private static CsvWriter recorder;

	/**
	 * save the net to local file
	 * 
	 * @param net
	 * @param fileName
	 */
	public static void net2csv(HashMap<String, Double> net, String fileName) {
		if (!fileName.endsWith(".csv")) {
			throw new RuntimeException(
					"The file name should be a csv,but given" + fileName);
		}
		String realFile = wdir + File.separator + fileName;
		CsvWriter cw = new CsvWriter(realFile, ',', Charset.forName("UTF-8"));
		for(String key:net.keySet()){
			String [] genes = key.split("->");
			String[] record = { genes[0], genes[1], net.get(key).toString() };
			try {
				cw.writeRecord(record);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		cw.close();
		LogUtils.log("Output done [" + realFile + "]");
	}

	/**
	 * output the net specified by netFile
	 * 
	 * @param netFile
	 *            The file name of the network
	 * @param fileName
	 */
	@SuppressWarnings("unchecked")
	public static void net2csv(String netFile,String fileName){
		HashMap<String,Double> map = (HashMap<String, Double>) Serialization.load(netFile);
		net2csv(map, fileName);
	}

	/**
	 * Open the csvwriter
	 * 
	 * @param fileName
	 */
	public static void open(String fileName) {
		String realFile = wdir + File.separator + fileName;
		recorder = new CsvWriter(realFile, ',', Charset.forName("UTF-8"));
	}

	/**
	 * write record
	 * 
	 * @param record
	 */
	public static void writeRecord(String[] record) {
		try {
			recorder.writeRecord(record);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * close the csvwriter handle
	 */
	public static void close() {
		if (recorder != null) {
			recorder.close();
		}
	}

	@SuppressWarnings("unchecked")
	public static void dataset2csv(String dataset, String fileName) {
		HashMap<String, HashSet<String>> map = (HashMap<String, HashSet<String>>) Serialization.load(dataset);
		open(fileName);
		for (String pathway : map.keySet()) {
			String st = "";
			for (String s : map.get(pathway)) {
				st += s + ",";
			}
			String[] rec = { pathway, st.substring(0, st.length() - 1) };
			writeRecord(rec);
		}

	}
}
