package pathway.bio.lab;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Time;
import java.util.Properties;

import logging.bio.lab.LogUtils;

/**
 * 
 * @author mingchen
 * @date May 1st, 2015
 * 
 */
public class KeggPathwayDownload {
	private final String wdir = "dataRepository/pathway/kegg";
	private final String sourceDir = "dataRepository";

	/**
	 * construction function if the filepath not exist then mkdirs
	 */
	public KeggPathwayDownload() {
		File dir = new File(wdir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	/**
	 * Download all the pathway in kegg[hsa]
	 * 
	 * @param fileName
	 * @param org
	 */
	public void pathwayDownload(String fileName, String org) {
		String sourceFile = sourceDir + File.separator + fileName;
		String realPath = wdir + File.separator + org;
		// check whether the file folder exists
		// if not mkdir
		File file = new File(realPath);
		if (!file.exists()) {
			file.mkdir();
		}
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(
					sourceFile)));
			String line;
			line = br.readLine();
			while (line != null) {
				String[] items = line.split("\t");
				String path = items[0].substring(items[0].length() - 5);
				if (items[1].indexOf("/") > 0) {
					items[1] = items[1].replace('/', '-');
				}
				saveKgml(path, org, realPath + File.separator + "hsa" + path
						+ " - " + items[1] + ".xml");
				LogUtils.log("Pathway: hsa" + path + " download!"
						+ new Time(System.currentTimeMillis()).toString());
				line = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * prepare the url string for the download function [getAndSavePage]
	 * 
	 * @param path
	 * @param org
	 * @param file
	 */
	public void saveKgml(String path, String org, String file) {
		String url = "http://www.kegg.jp/kegg-bin/download?entry=" + org + path
				+ "&format=kgml";
		getAndSavePage(url, file);
	}

	/**
	 * The real download function to retrieve all the web data
	 * 
	 * @param url
	 * @param fileName
	 */
	public void getAndSavePage(String url, String fileName) {

		try {
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
			conn.connect();
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
					fileName)));
			BufferedReader br = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "utf-8"));
			String line = br.readLine();
			int count = 1;
			while (line != null) {
				bw.write(line
						+ new Properties(System.getProperties())
				.getProperty("line.separator"));
				line = br.readLine();
			}
			bw.close();
			br.close();
		} catch (MalformedURLException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		KeggPathwayDownload kp = new KeggPathwayDownload();
		kp.getAndSavePage("http://rest.kegg.jp/list/pathway/hsa",
				"dataRepository/kegghsa.paths");
		kp.pathwayDownload("kegghsa.paths", "hsa");// then delete all null files

	}
}
