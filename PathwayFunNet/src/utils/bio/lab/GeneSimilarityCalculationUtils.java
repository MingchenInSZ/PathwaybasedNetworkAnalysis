package utils.bio.lab;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import logging.bio.lab.LogUtils;
import semanticsimilarity.bio.lab.SemanticSimilarityFromDump;
import serialization.bio.lab.Serialization;
import statistics.bio.lab.Normalization;

/**
 * 
 * @author mingchen
 * @date 2015Äê5ÔÂ4ÈÕ
 */
public class GeneSimilarityCalculationUtils {

	/**
	 * calculate the semantic similarity of the gene net
	 */
	@SuppressWarnings("unchecked")
	public void geneSimNetCalculation() {
		HashSet<String> edges = (HashSet<String>) Serialization.load("filteredGeneNet.out");
		HashMap<String, Double> semNet = new HashMap<String, Double>();
		SemanticSimilarityFromDump ssfp = new SemanticSimilarityFromDump();
		int count = 1;
		for (String edge : edges) {
			String[] genes = edge.split("->");
			float sim = ssfp.geneSemSim(genes[0], genes[1]);
			semNet.put(edge, Double.valueOf(sim));
			LogUtils.log("Genes:" + genes[0] + "," + genes[1] + "[" + sim + "]["+count++ +"]");
		}
		semNet = normalizeGeneNet(semNet);
		Serialization.save(semNet, "semnet.out");
	}

	/**
	 * Normalize the gene net using minmax normalizatin method
	 * 
	 * @param netWithWeight
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Double> normalizeGeneNet(
			HashMap<String, Double> netWithWeight) {
		LinkedList<Double> list = new LinkedList<Double>();
		for (String key : netWithWeight.keySet()) {
			list.add(netWithWeight.get(key));
		}
		List<Double> nlist = (List<Double>) Normalization.minmax((List) list);
		int i = 0;
		for (String key : netWithWeight.keySet()) {
			netWithWeight.put(key, nlist.get(i++));
		}
		return netWithWeight;
	}

	public static void main(String[] args) {
		GeneSimilarityCalculationUtils gscu = new GeneSimilarityCalculationUtils();
		gscu.geneSimNetCalculation();
	}
}
