package inoutput.bio.lab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class DifferentialGenes {
	private static final String wdir = "dataRepository";

	/**
	 * get all the differential expressed genes from samr[8659]
	 * 
	 * example gene name per line
	 * 
	 * @param fileName
	 * @return
	 */
	public static HashSet<String> getAllDifGenes(String fileName) {
		String realFile = wdir + File.separator + fileName;
		HashSet<String> genes = new HashSet<String>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(
					realFile)));

			String line = br.readLine();
			while (line != null) {
				genes.add(line.trim());
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return genes;
	}
}
