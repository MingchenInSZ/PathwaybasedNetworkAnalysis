package utils.bio.lab;

import inoutput.bio.lab.NetworkOutput;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;

import pathway.bio.lab.PathwaySimilarity;

import com.csvreader.CsvReader;

public class PathwaySimilarityUtils {
	private static final String wdir = "dataRepository";

	public static void removeRepeate(String fileName, String outName) {
		String realFile = "dataRepository/results/" + fileName;
		HashSet<String> backup = new HashSet<String>();
		NetworkOutput.open(outName);
		try {
			CsvReader cr = new CsvReader(realFile);
			while (cr.readRecord()) {
				String[] values = cr.getValues();
				String key1 = values[0]+"_"+values[1];
				String key2 = values[1]+"_"+values[0];
				if (!backup.contains(key1)) {
					if (!values[0].equals(values[1])) {
						NetworkOutput.writeRecord(values);
					}
					backup.add(key1);
					backup.add(key2);
				}
			}
			NetworkOutput.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String [] args){
		PathwaySimilarity ps = new PathwaySimilarity();
		// ps.similarityCalculation("norm", "pathway_norm_08.csv", 0.8);
		// ps.similarityCalculation("norm", "pathway_norm_075.csv", 0.75);
		// ps.similarityCalculation("norm", "pathway_norm_07.csv", 0.7);
		// ps.similarityCalculation("tumor", "pathway_tum_08.csv", 0.8);
		// ps.similarityCalculation("tumor", "pathway_tum_075.csv", 0.75);
		// ps.similarityCalculation("tumor", "pathway_tum_07.csv", 0.7);
		ps.similarityCalculation("semantic", "pathway_sem_06.csv", 0.6);
		ps.similarityCalculation("semantic", "pathway_sem_08.csv", 0.8);
		// removeRepeate("pathway_norm_00.csv", "pathway_norm_nr_00.csv");
		// removeRepeate("pathway_norm_08.csv", "pathway_norm_nr_08.csv");
		// removeRepeate("pathway_norm_075.csv", "pathway_norm_nr_075.csv");
		// removeRepeate("pathway_norm_07.csv", "pathway_norm_nr_07.csv");
		// removeRepeate("pathway_tum_00.csv", "pathway_tum_nr_00.csv");
		// removeRepeate("pathway_tum_08.csv", "pathway_tum_nr_08.csv");
		// removeRepeate("pathway_tum_075.csv", "pathway_tum_nr_075.csv");
		// removeRepeate("pathway_tum_07.csv", "pathway_tum_nr_07.csv");
		// removeRepeate("pathway_sem_06.csv", "pathway_sem_nr_06.csv");

	}

}
