package inoutput.bio.lab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

/**
 * 
 * @author mingchen
 * @date May 1st, 2015
 */
public class PPInteractionNet {

	private static final String wdir = "dataRepository";

	/**
	 * Read the PPI network and get all the distinct ids(swissprot ac)
	 *   content demo:
	 *   dataset swissprot1 swissprot2
	 *   BioGrid	P0CG48	A0AV96
	 * @param fileName
	 * @return HashSet<String>
	 */
	public static HashSet<String> getDifferentIDS(String fileName) {
		HashSet<String> unique = new HashSet<String>();
		String realFileName = wdir + File.separator + fileName;
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(realFileName)));
			String line = br.readLine();
			boolean isFirstLine = true;
			while (line != null) {
				if (isFirstLine) {
					isFirstLine = false;
				} else {
					String[] fs = line.trim().split("\t");
					unique.add(fs[1]);
					unique.add(fs[2]);
				}
				line = br.readLine();

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return unique;
	}

	/**
	 * get all the edges from the ppi network into hashset
	 * 
	 * 
	 * The demo is the same like that of in getDifferentIDS
	 * 
	 * @param fileName
	 * @param directed
	 *            to indict whether the network is directed or not
	 * @return
	 */
	public static HashSet<String> getAllEdges(String fileName, boolean directed) {
		HashSet<String> edges = new HashSet<String>();
		String realFileName = wdir + File.separator + fileName;
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(
					realFileName)));
			String line = br.readLine();
			boolean isFirstLine = true;
			while (line != null) {
				if (isFirstLine) {
					isFirstLine = false;
				} else {
					String[] fs = line.trim().split("\t");
					edges.add(fs[1] + "->" + fs[2]);
					if (!directed) {
						edges.add(fs[2] + "->" + fs[1]);
					}
				}
				line = br.readLine();

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return edges;
	}

}
