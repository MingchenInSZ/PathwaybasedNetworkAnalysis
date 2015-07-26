package semanticsimilarity.bio.lab;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import serialization.bio.lab.Serialization;


/**
 * calculate the semantic similarity from the dumped svals
 * 
 * @author mingchen
 * @date 2015年5月4日
 */
public class SemanticSimilarityFromDump {
	private static Map<String, LinkedList<relationship>> pmap = null;
	private static HashMap<String, HashMap<String, Float>> dpsv = null;
	private static Map<String, LinkedHashSet<String>> g2gmap = null;

	/**
	 * 
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * 
	 *             Initialize the pmap
	 */
	@SuppressWarnings("unchecked")
	public SemanticSimilarityFromDump() {
		pmap = (Map<String, LinkedList<relationship>>) Serialization.load("parents.out");
		dpsv = (HashMap<String, HashMap<String, Float>>) Serialization.load("dumpSVal.out");
		g2gmap = (HashMap<String, LinkedHashSet<String>>) Serialization.load("gene2go.out");
	}

	/**
	 * 
	 * @param goId
	 * @return map<String,Float>
	 * 
	 *         calculate the s-val of go term
	 */
	public HashMap<String, Float> semanticValue(String goId) {
		HashMap<String, Float> queue = new HashMap<String, Float>();
		LinkedList<String> extra = new LinkedList<String>();
		queue.put(goId, Float.valueOf(1.0f));
		extra.add(goId);
		while (!extra.isEmpty()) {
			String curGo = extra.poll();
			float curVal = queue.get(curGo).floatValue();
			// System.out.println(curGo + "  " + curVal);
			for (relationship rel : pmap.get(curGo)) {
				if (queue.containsKey(rel.getGoId())) {
					// if the term has calculated, then update with max s-val
					if (curVal * rel.getIntensity() > queue.get(curGo)
							.floatValue()) {
						queue.remove(rel.getGoId());
						queue.put(rel.getGoId(), curVal * rel.getIntensity());
					}
				} else {
					queue.put(rel.getGoId(), curVal * rel.getIntensity());
				}
				extra.add(rel.getGoId());// push the parent go to 队列
			}

		}
		return queue;
	}


	/**
	 * 
	 * @param go1
	 * @param go2
	 * @return float
	 * 
	 *         calculate the semsim value of go terms
	 */
	public float goSemSim(String go1, String go2) {
		float sim = 0.0f;
		HashMap<String,Float> gs1,gs2;
		if(!dpsv.containsKey(go1)){
			return sim;
		}else{
			gs1 = dpsv.get(go1);
		}
		if (!dpsv.containsKey(go2)){
			return sim;
		}else{
			gs2 = dpsv.get(go2);
		}
		Set<String> res = new LinkedHashSet<String>();
		for (String s : gs1.keySet()) {
			res.add(s);
		}
		res.retainAll(gs2.keySet());
		float sum = 0.0f, gv1 = 0.0f, gv2 = 0.0f;
		for (String s : gs1.keySet()) {
			gv1 += gs1.get(s).floatValue();
			if (res.contains(s)) {
				sum += gs1.get(s).floatValue();
			}
		}
		for (String s : gs2.keySet()) {
			gv2 += gs2.get(s).floatValue();
			if (res.contains(s)) {
				sum += gs2.get(s).floatValue();
			}

		}
		sim = sum / (gv1 + gv2 + 0.0001f);
		return sim;
	}

	/**
	 * 
	 * @param go
	 * @param goset
	 * @return float
	 * 
	 *         calculate go and goset sim
	 */
	public float goAndSetSemSim(String go, Set<String> goset) { // calculate the
		// max one
		float max = 0;
		for (String g : goset) {
			float sim = goSemSim(go, g);
			if (sim > max) {
				max = sim;
			}
		}
		return max;
	}

	/**
	 * 
	 * @param goset1
	 * @param goset2
	 * @return float
	 * 
	 *         calculate the sim of gosets
	 */
	public float goSetSemSim(Set<String> goset1, Set<String> goset2) {
		float gos1 = 0.0f, gos2 = 0.0f;
		Map<String, Float> map = new HashMap<String, Float>();
		for (String go : goset1) {
			float tmp = 0.0f;
			for (String g : goset2) {
				float sim = goSemSim(go, g);
				tmp = Math.max(tmp, sim);
				// dump the calculated sim
				// System.out.println(go + " " + g + " " + sim);
				map.put(g, Float.valueOf(Math.max(sim,
						map.get(g) == null ? 0.0f : map.get(g).floatValue())));
			}
			gos1 += tmp;
		}
		for (String go : map.keySet()) {
			gos2 += map.get(go).floatValue();
		}

		return (gos1 + gos2) / (goset1.size() + goset2.size() + 0.0001f);
	}

	/**
	 * 
	 * @param gene1
	 * @param gene2
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * 
	 *             Calculate the semsim of genes
	 */
	public float geneSemSim(String gene1, String gene2) {
		Map<String, LinkedHashSet<String>> map = g2gmap;
		Set<String> gs1, gs2;
		if (gene1 != "" && map.containsKey(gene1)) {
			gs1 = map.get(gene1);
		} else {
			return 0.0f;
		}
		if (gene2 != "" && map.containsKey(gene2)) {
			gs2 = map.get(gene2);
		} else {
			return 0.0f;
		}
		return goSetSemSim(gs1, gs2);
	}



}
