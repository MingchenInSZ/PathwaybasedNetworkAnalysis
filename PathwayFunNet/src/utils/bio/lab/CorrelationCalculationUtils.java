package utils.bio.lab;

import java.sql.Time;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import logging.bio.lab.LogUtils;
import serialization.bio.lab.Serialization;
import statistics.bio.lab.Normalization;
import correlation.bio.lab.PearsonCorrelation;

public class CorrelationCalculationUtils {

	@SuppressWarnings("unchecked")
	public HashMap<String, Double> correlationCalcuOfGeneNet(String expData,
			String netFile) {
		HashMap<String, Double> netWithWeight = new HashMap<String, Double>();

		HashMap<String, List<Double>> exp = (HashMap<String, List<Double>>) Serialization
				.load(expData);

		HashSet<String> net = (HashSet<String>) Serialization.load(netFile);
		HashSet<String> omit = new HashSet<String>();
		int count = 1;
		for (String edge : net) {
			String[] genes = edge.split("->");
			if (exp.containsKey(genes[0]) && exp.containsKey(genes[1])) {
				double cor = PearsonCorrelation.pearson(exp.get(genes[0]),
						exp.get(genes[1]));
				netWithWeight.put(edge, Double.valueOf(cor));
				LogUtils.log("Gene :" + genes[0] + "," + genes[1] + "[" + cor
						+ "],[" + count++ + "],["
						+ new Time(System.currentTimeMillis()) + "]");
			} else {
				omit.add(edge);
			}
		}
		LogUtils.info("Total calculation:" + count + " and omit:" + omit.size());
		netWithWeight = normalizeCorrelationNet(netWithWeight);
		return netWithWeight;
	}

	/**
	 * normalize the net
	 * 
	 * @param netWithWeight
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Double> normalizeCorrelationNet(
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
	/**
	 * dump all the correlation gene net
	 * 
	 * @param expressData
	 * @param netFile
	 * @param dumpFile
	 */
	public void dumpCorrelationOfGeneNet(String expressData,String netFile,String dumpFile){
		HashMap<String, Double> cors = correlationCalcuOfGeneNet(expressData, netFile);
		Serialization.save(cors, dumpFile);
		LogUtils.log(expressData + " dump done " + dumpFile + "["
				+ new Time(System.currentTimeMillis()) + "]");
	}

	public void display(HashMap<String, Double> map) {
		for (String key : map.keySet()) {
			System.out.println(key + ":" + map.get(key).doubleValue());
		}
	}
	public static void main(String[] args) {
		CorrelationCalculationUtils ccu = new CorrelationCalculationUtils();
		// ccu.dumpCorrelationOfGeneNet("normexp.out", "filteredGeneNet.out",
		// "normnet.out");
		// ccu.dumpCorrelationOfGeneNet("tumexp.out", "filteredGeneNet.out",
		// "tumnet.out");
		// ccu.display((HashMap<String, Double>)
		// Serialization.load("tumnet.out"));
	}
}
