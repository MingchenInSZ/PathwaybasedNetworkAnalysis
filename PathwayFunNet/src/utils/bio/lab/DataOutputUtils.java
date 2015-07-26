package utils.bio.lab;

import java.util.HashMap;
import java.util.HashSet;

import serialization.bio.lab.Serialization;


public class DataOutputUtils {

	public static void main(String[] args) {
		// NetworkOutput.net2csv("normnet.out", "norm_N_100.csv");
		// NetworkOutput.net2csv("tumnet.out", "tum_N_100.csv");
		// NetworkOutput.net2csv("semnet.out", "sem_N_100.csv");
		// HashMap<String, HashSet<String>> map = (HashMap<String,
		// HashSet<String>>) Serialization
		// .load("keggpathway.out");
		// System.out.println(map.size());
		HashMap<String, HashSet<String>> kegg = (HashMap<String, HashSet<String>>) Serialization
				.load("keggpathway.out");
		int count = 1;
		for (String key : kegg.keySet()) {
			String rs = "";
			for (String value : kegg.get(key)) {
				rs += value + " ";
			}
			System.out.println("[" + count++ + "]" + key + "-->" + rs);
		}
	}

}
