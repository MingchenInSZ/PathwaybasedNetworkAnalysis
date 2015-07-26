package inoutput.bio.lab;

import java.util.HashMap;

import serialization.bio.lab.Serialization;

public class GeneInteractionNet {
	/**
	 * filter the gene net by setting the threshold
	 * 
	 * @param origin
	 *            The origin network
	 * @param threshold
	 *            The cut off used to filter network
	 * @return
	 */
	public static HashMap<String,Double> geneNetFilter(HashMap<String,Double> origin,double threshold){
		HashMap<String,Double> newer = new HashMap<String,Double>();
		for(String key:origin.keySet()){
			if(origin.get(key).doubleValue()>=threshold){
				newer.put(key, origin.get(key));
			}
		}
		return newer;
	}

	/**
	 * Given the file name and filter the network
	 * 
	 * @param netFile
	 * @param threshold
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static HashMap<String,Double> geneNetFilter(String netFile,double threshold){
		HashMap<String,Double> origin = (HashMap<String, Double>) Serialization.load(netFile);
		return geneNetFilter(origin, threshold);
	}
}
