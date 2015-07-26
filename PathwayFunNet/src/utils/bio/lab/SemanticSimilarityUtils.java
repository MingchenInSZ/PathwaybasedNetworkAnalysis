package utils.bio.lab;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import semanticsimilarity.bio.lab.SemanticSimilarity;
import semanticsimilarity.bio.lab.relationship;
import serialization.bio.lab.Serialization;

public class SemanticSimilarityUtils {
	@SuppressWarnings("unchecked")
	public void dumpSemanticValues() {
		Map<String, LinkedList<relationship>> pmap = (Map<String, LinkedList<relationship>>) Serialization.load("parents.out");
		HashMap<String, HashMap<String, Float>> dpsv = new HashMap<String, HashMap<String, Float>>();
		SemanticSimilarity ss = new SemanticSimilarity();
		for (String go : pmap.keySet()) {
			HashMap<String, Float> inner = (HashMap<String, Float>) ss.semanticValue(go);
			dpsv.put(go, inner);
		}
		Serialization.save(dpsv, "dumpSVal.out");
	}

	public static void main(String[] args) {
		SemanticSimilarityUtils ssu = new SemanticSimilarityUtils();
		ssu.dumpSemanticValues();
	}
}
