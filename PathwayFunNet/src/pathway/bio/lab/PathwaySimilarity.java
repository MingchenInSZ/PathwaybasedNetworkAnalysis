package pathway.bio.lab;

import inoutput.bio.lab.DifferentialGenes;
import inoutput.bio.lab.GeneInteractionNet;
import inoutput.bio.lab.NetworkOutput;

import java.util.HashMap;
import java.util.HashSet;

import logging.bio.lab.LogUtils;
import serialization.bio.lab.Serialization;

/**
 * Calculate the similarity of pathways
 * 
 * @author mingchen
 * @date 2015年5月3日
 */
public class PathwaySimilarity {

	private final HashMap<String, HashSet<String>> pathways;
	private final PathwayPermutation permute;
	private final int nperms = 1000;
	private final HashSet<String> geneSet; // the differential expressed genes

	@SuppressWarnings("unchecked")
	public PathwaySimilarity() {
		pathways = (HashMap<String, HashSet<String>>) Serialization.load("keggpathway.out");
		permute = new PathwayPermutation();
		geneSet = DifferentialGenes.getAllDifGenes("samr005.txt");
	}

	/**
	 * calculate the similarity of pathways entrance
	 * 
	 * @param type
	 *            This indicate the category of network[norm,tumor,semantic]
	 * @param fileName
	 *            The name of file to store the pathway similarity and pvals
	 */
	public void similarityCalculation(String type, String fileName,double threshold) {
		if(type.equalsIgnoreCase("norm")){
			HashMap<String,Double> normNet = GeneInteractionNet.geneNetFilter("normnet.out", threshold);
			simCalculation(normNet, fileName);
		}else if(type.equalsIgnoreCase("tumor")){
			HashMap<String,Double> tumorNet = GeneInteractionNet.geneNetFilter("tumnet.out", threshold);
			simCalculation(tumorNet, fileName);
		}else if(type.equalsIgnoreCase("semantic")){
			HashMap<String,Double> semNet = GeneInteractionNet.geneNetFilter("semnet.out", threshold);
			simCalculation(semNet, fileName);
		}else{
			LogUtils.log("Type error:[type should be  in [norm,tumor,semantic] ], but given:"+type);
		}

	}

	/**
	 * The actual calculation function
	 * 
	 * @param dataIn
	 * @param fileName
	 */
	public void simCalculation(HashMap<String, Double> dataIn, String fileName) {
		Object[] objs = pathways.keySet().toArray();
		int npaths = objs.length;

		NetworkOutput.open(fileName);

		for (int i = 0; i < npaths - 1; i++) {
			HashSet<String> source = pathwayOverlap(pathways.get(objs[i].toString()), geneSet);
			for (int j = i + 1; j < npaths; j++) {
				HashSet<String> target = pathwayOverlap(pathways.get(objs[j].toString()), geneSet);
				// 相似性计算
				double sim = geneSetSim(source, target, dataIn);
				// p值计算
				// double pval = permutePval(pathways.get(objs[i].toString())
				// .size(), pathways.get(objs[j].toString()).size(),
				// nperms, sim, dataIn);
				// pval calculation
				HashSet<String> union = unionGenes(
						pathways.get(objs[i].toString()),
						pathways.get(objs[j].toString()));
				double pval = pValuePermuteLabel(
						pathways.get(objs[i].toString()).size(),
						pathways.get(objs[j].toString()).size(),
						union,nperms, sim, dataIn);
				// 存储数据到csv文件中
				String[] contents = { objs[i].toString(), objs[j].toString(),
						String.valueOf(sim), String.valueOf(pval) };

				NetworkOutput.writeRecord(contents);
				LogUtils.log(objs[i].toString() + "[" + i + "]"
						+ objs[j].toString() + "[" + j + "] [" + npaths + "]["
						+ pval + "]");
			}
		}
		NetworkOutput.close();
	}

	public double permutePval(int sLength, int tLength, int nperms,
			double threshold, HashMap<String, Double> dataIn) {
		int count = 0;
		for (int i = 0; i < nperms; i++) {
			HashSet<String> sgenes = permute.getPermGenes(sLength);
			HashSet<String> tgenes = permute.getPermGenes(tLength);
			HashSet<String> source = pathwayOverlap(sgenes, geneSet);
			HashSet<String> target = pathwayOverlap(tgenes, geneSet);
			double sim = geneSetSim(source, target, dataIn);
			if (sim > threshold) {
				count++;
			}
		}
		return count * 1.0 / nperms;
	}

	/**
	 * calculation p value using the class label permutation
	 * 
	 * @param snum
	 * @param tnum
	 * @param source
	 * @param nperms
	 * @param threshold
	 * @param data
	 * @return double p value
	 */
	public double pvalCalculation(int snum, int tnum, HashSet<String> source,
			int nperms, double threshold, HashMap<String, Double> data) {
		double p = 0.0,fenzi = 1.0;
		for (int i = 0; i < nperms; i++) {
			HashSet<String> rsgenes = permute.getPermGenes(snum, source);
			HashSet<String> rtgenes = permute.getPermGenes(tnum, source);
			double sim = geneSetSim(pathwayOverlap(rsgenes, geneSet),
					pathwayOverlap(rtgenes, geneSet), data);
			if (sim > threshold) {
				fenzi += 1;
			}
		}
		p = fenzi / nperms;
		return p;
	}

	/**
	 * calculate the p value using the genes all in dif expressed genes
	 * 
	 * @param snum
	 * @param tnum
	 * @param source
	 * @param perms
	 * @param threshold
	 * @param data
	 * @return
	 */
	public double pvalCalculationAllInDif(int snum, int tnum,
			HashSet<String> source, int perms, double threshold,
			HashMap<String, Double> data) {
		double p = 0.0, fenzi = 1.0;
		for (int i = 0; i < nperms; i++) {
			HashSet<String> rsgenes = permute.getPermGenes(snum, source);
			HashSet<String> rtgenes = permute.getPermGenes(tnum, source);
			double sim = geneSetSim(rsgenes, rtgenes, data);
			if (sim > threshold) {
				fenzi += 1;
			}
		}
		p = fenzi / nperms;
		return p;
	}
	/**
	 * Calculate the p value using the permute label method
	 * 
	 * @param snum
	 * @param tnum
	 * @param union
	 * @param nperms
	 * @param thre
	 * @param data
	 * @return
	 */
	public double pValuePermuteLabel(int snum, int tnum, HashSet<String> union,
			int nperms, double thre, HashMap<String, Double> data) {
		double p = 0.0, fenzi = 1.0;
		for (int i = 0; i < nperms; i++) {
			HashMap<String,HashSet<String>> map = permute.getPermGenes(union, snum, tnum);
			double sim = geneSetSim(pathwayOverlap(map.get("source"),geneSet),pathwayOverlap(map.get("target"),geneSet),data);
			if (sim > thre){
				fenzi += 1.0;
			}
		}
		p = fenzi / nperms;
		return p;
	}
	/**
	 * pathway overlap
	 * 
	 * @param source
	 * @param target
	 * @return HashSet<String>
	 */
	public static HashSet<String> pathwayOverlap(HashSet<String> source,
			HashSet<String> target) {
		HashSet<String> inter = new HashSet<String>();
		for (String s : source) {
			inter.add(s);
		}
		inter.retainAll(target);
		return inter;
	}

	/**
	 * calculate the gene set similarity using the data given
	 * 
	 * @param source
	 * @param target
	 * @param dataIn
	 * @return
	 */
	public double geneSetSim(HashSet<String> source, HashSet<String> target,
			HashMap<String, Double> dataIn) {
		double d = 0.0;
		HashMap<String, Double> back = new HashMap<String, Double>();
		for (int i = 0; i < source.size(); i++) {
			Object[] obj = source.toArray();
			double tmp = 0.0;
			for (int j = 0; j < target.size(); j++) {
				Object objt = target.toArray()[j];
				String key = obj[i].toString() + "->" + objt.toString();
				if (dataIn.keySet().contains(key)) {
					tmp = Math.max(tmp, dataIn.get(key).doubleValue());
					// 存储当前j所在基因的最大相似值
					Double dobj = back.containsKey(objt.toString()) ? Double.valueOf(Math.max(back.get(objt.toString())
							.doubleValue(), dataIn.get(key)
							.doubleValue())) : dataIn.get(key);
					back.put(objt.toString(), dobj);
				}
			}
			d += tmp;
		}
		for (String s : back.keySet()) {
			d += back.get(s).doubleValue();
		}
		return d / (source.size() + target.size() + 0.0001);
	}

	/**
	 * Get the union of the two gene sets
	 * 
	 * @param sgenes
	 * @param tgenes
	 * @return HashSet<String>
	 */
	public HashSet<String> unionGenes(HashSet<String> sgenes,
			HashSet<String> tgenes) {
		HashSet<String> union = new HashSet<String>();
		union.addAll(sgenes);
		union.addAll(tgenes);
		return union;
	}


}
