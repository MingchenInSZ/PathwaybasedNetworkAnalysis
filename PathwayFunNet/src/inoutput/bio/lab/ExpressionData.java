package inoutput.bio.lab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Time;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import logging.bio.lab.LogUtils;

public class ExpressionData {
	private final String wdir = "dataRepository";

	/**
	 * read expression data from file
	 * 
	 * @param fileName
	 *            The file name of the expression data
	 * @return HashMap<String,List<Double>>
	 */
	public HashMap<String, List<Double>> readExpressionData(String fileName) {
		String realFile = wdir + File.separator + fileName;
		HashMap<String, List<Double>> data = new HashMap<String, List<Double>>();
		File file = new File(realFile);
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			int count = 1;
			while (line != null) {
				String[] fs = line.split("\t");
				String gene = fs[0];
				List<Double> inner = new LinkedList<Double>();
				for (int i = 1; i < fs.length; i++) {
					inner.add(Double.valueOf(fs[i]));
				}
				data.put(gene, inner);
				LogUtils.log(fileName + " Gene:" + gene + "[" + count++ + "]");
				line = br.readLine();
			}
			LogUtils.info(fileName + " expression data read done!"
					+ new Time(System.currentTimeMillis()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return data;
	}
}
