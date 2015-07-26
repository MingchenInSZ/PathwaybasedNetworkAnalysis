package pathway.bio.lab;

import hypergenom.bio.lab.HyperGenomFast;
import inoutput.bio.lab.NetworkOutput;

import java.util.HashMap;
import java.util.HashSet;

import logging.bio.lab.LogUtils;
import serialization.bio.lab.Serialization;
import statistics.bio.lab.FDRCorrection;

/**
 * Get all the significant pathways using the method of common used
 * hypergenometric method , And the FDR correction carried
 * 
 * @author mingchen
 * @date 2015Äê5ÔÂ14ÈÕ
 */
public class SignificancePathway {

	private HashMap<String, HashSet<String>> pathways = null;
	private HashSet<String> genes = null;

	@SuppressWarnings("unchecked")
	public SignificancePathway(){
		pathways = (HashMap<String, HashSet<String>>) Serialization.load("keggpathway.out");
		// genes = DifferentialGenes.getAllDifGenes("difgenesamr0.05.txt");
		genes = (HashSet<String>) Serialization.load("difgenesfileteredbyppi.out");
	}

	/**
	 * 
	 * @param p
	 * @param method
	 * @param fileName
	 * @return
	 */
	public void getSignificantPathwayCommonHyperGenom(
			double p, String method, String fileName) {
		double[] pvals = new double[pathways.size()];
		String[] names = new String[pathways.size()];
		int i = 0;
		int [] olaps = new int[pathways.size()];
		for (String pathway : pathways.keySet()) {
			HashSet<String> overlap = PathwaySimilarity.pathwayOverlap(pathways.get(pathway), genes);
			olaps[i] = overlap.size();
			double pval = HyperGenomFast.pHyerGenomBig(39284, 5664, pathways
					.get(pathway).size(), overlap.size());
			pvals[i] = pval;
			names[i++] = pathway;
			LogUtils.log("pathway:" + pathway + "[" + olaps[i - 1] + "]");
		}
		// FDR correction
		if (method.equals("fdr") || method.equalsIgnoreCase("bh")) {
			FDRCorrection.fdr(pvals);
		}
		// extract significant pathways
		NetworkOutput.open(fileName);
		String[] title = { "PathwayName", "PathwaySize", "DifGenes", "FDRPval" };
		NetworkOutput.writeRecord(title);
		for (i = 0; i < pathways.size(); i++) {
			if (pvals[i] <= p) {
				String[] records = { names[i],
						String.valueOf(pathways.get(names[i]).size()),
						String.valueOf(olaps[i]),
						String.valueOf(pvals[i]) };
				NetworkOutput.writeRecord(records);
				LogUtils.log("pathway:" + names[i] + "["
						+ pathways.get(names[i]).size() + "][" + olaps[i]
								+ "]["
								+ pvals[i] + "]");
			}
		}
		NetworkOutput.close();
	}
}
