package utils.bio.lab;

import inoutput.bio.lab.DifferentialGenes;
import inoutput.bio.lab.PPInteractionNet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import logging.bio.lab.LogUtils;
import serialization.bio.lab.Serialization;
import conversion.bio.lab.IDConversion;

/**
 * 
 * @author mingchen
 * @date May 2nd,2015
 * 
 */

public class PPInteractionUtils {


	/**
	 * map all the ids in ppi network to hgnc approved symbol
	 * 
	 * @param fileName
	 *            the file name of the ppi network
	 */
	public void PPINetworkDictionary(String fileName) {
		HashMap<String, String> dictionary = new HashMap<String, String>();
		HashSet<String> idsFromPPI = PPInteractionNet.getDifferentIDS(fileName);
		IDConversion idc = new IDConversion();
		LogUtils.log("Start converting...");
		LinkedList<String> omit = new LinkedList<String>();
		for (String swissprot : idsFromPPI) {
			String approved_symbol = idc.swiss2ApprovedSymbol(swissprot);
			if (approved_symbol == "") {
				omit.add(swissprot);
			} else {
				dictionary.put(swissprot, approved_symbol);
				LogUtils.log("ID:" + swissprot + " convert to:" + approved_symbol);
			}
		}
		LinkedList<String> crossout = new LinkedList<String>();
		if (omit.size() != 0) {
			for(String swissprot:omit){
				String unigeneCluster = idc.swiss2Unigene(swissprot);
				String unigene = idc.unigeneCluster2unigene(unigeneCluster);
				String symbol = idc.unigene2ApprovedSymbol(unigene);
				if (symbol != "") {
					if (symbol.endsWith("~withdrawn")) {
						symbol = symbol.substring(0, symbol.indexOf("~"));
					}
					dictionary.put(swissprot, symbol);
					LogUtils.log("Omit ID:" + swissprot + " convert to:"
							+ symbol);
				} else {
					crossout.add(swissprot);
				}
			}
		}
		Serialization.save(dictionary, "ppidict.out");
		LogUtils.log("Conversion Done!");
		LogUtils.log("Total omit:[" + omit.size() + "],crossout:["
				+ crossout.size() + "]");
	}

	/**
	 * using the dictionary to transfer all the PPI network to PPI gene network
	 * 
	 * @param fileName
	 *            the ppi network file name
	 * @return HashSet<String> all the gene interaction edges
	 */
	@SuppressWarnings("unchecked")
	public HashSet<String> PPINetworkTransformation(String fileName) {
		HashSet<String> edges = PPInteractionNet.getAllEdges(fileName, false);
		HashMap<String,String> dict = (HashMap<String, String>) Serialization.load("ppidict.out");
		HashSet<String> transedNet = new HashSet<String>();
		for (String edge : edges) {
			String[] nodes = edge.split("->");
			String tfnode = "", tanode = "";
			if (dict.containsKey(nodes[0])) {
				tfnode = dict.get(nodes[0]);
			}
			if (dict.containsKey(nodes[1])) {
				tanode = dict.get(nodes[1]);
			}
			if (tfnode != "" && tanode != "") {
				transedNet.add(tfnode + "->" + tanode);
			}
		}
		return transedNet;
	}

	/**
	 * Filter the derived gene network using the differential expressed genes
	 * 
	 * @param difGeneFileName
	 *            The file name of differential expressed genes
	 * @param ppiNetFileName
	 *            The file name of PPI Network
	 */
	public void PPIGeneNetworkFilter(String difGeneFileName,String ppiNetFileName){
		HashSet<String> geneNet = PPINetworkTransformation(ppiNetFileName);
		HashSet<String> difGenes = DifferentialGenes.getAllDifGenes(difGeneFileName);
		HashSet<String> filteredNet = new HashSet<String>();
		int count = 1;
		for (String edge : geneNet) {
			String[] genes = edge.split("->");
			if (difGenes.contains(genes[0]) && difGenes.contains(genes[1])) {
				filteredNet.add(edge);
				LogUtils.log("Get:" + edge + "[" + count++ + "]");
			}
		}
		Serialization.save(filteredNet, "filteredGeneNet.out");
		LogUtils.log("Filtered Gene Network Saved to filteredGeneNet.out");
	}

	@SuppressWarnings("unchecked")
	public void differentailExpressedGenesFilteredByPPI() {
		HashSet<String> filtered = (HashSet<String>) Serialization.load("filteredGeneNet.out");
		HashSet<String> geneset = new HashSet<String>();
		for (String s : filtered) {
			String[] genes = s.split("->");
			geneset.add(genes[0]);
			geneset.add(genes[1]);
		}
		LogUtils.log(geneset.size());
		Serialization.save(geneset, "difgenesfileteredbyppi.out");
	}
	public static void main(String[] args) {
		PPInteractionUtils ppu = new PPInteractionUtils();
		// ppu.PPIGeneNetworkFilter("samr005.txt", "i2d.2_3.Public.HUMAN.tab");
		// ppu.differentailExpressedGenesFilteredByPPI();
		HashSet<String> net = (HashSet<String>) Serialization.load("filteredGeneNet.out");
		HashSet<String> dif = (HashSet<String>) Serialization.load("difgenesfileteredbyppi.out");
		System.out.println(net.size() + "  " + dif.size());
	}
}
