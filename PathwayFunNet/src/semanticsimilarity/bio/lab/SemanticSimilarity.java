package semanticsimilarity.bio.lab;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import serialization.bio.lab.Serialization;

/**
 * 
 * @author mingchen
 * @date 2015年5月4日
 */
public class SemanticSimilarity {

	private static Map<String, LinkedList<relationship>> pmap = null;

	// this is the parent relation

	/**
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * 
	 *             constructor and initialize the pmap
	 */
	@SuppressWarnings("unchecked")
	public SemanticSimilarity() {
		pmap = (HashMap<String, LinkedList<relationship>>) Serialization.load("parents.out");
	}

	/**
	 * 
	 * @param goId
	 * @return map<String,Float>
	 * 
	 *         calculate the s-val of go term
	 */
	public Map<String, Float> semanticValue(String goId) {
		Map<String, Float> queue = new HashMap<String, Float>();
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
	 *         calculate the go term sim
	 */
	public float goSemSim(String go1, String go2) {
		Map<String, Float> goSVal1 = semanticValue(go1);
		Map<String, Float> goSVal2 = semanticValue(go2);
		Set<String> res = new LinkedHashSet<String>();
		for (String s : goSVal1.keySet()) {
			res.add(s);
		}
		res.retainAll(goSVal2.keySet());
		float sgo1 = 0.0f, sgo2 = 0.0f, sum = 0.0f;
		for (String s : goSVal1.keySet()) {
			sgo1 += goSVal1.get(s);
			if(res.contains(s)) {
				sum += goSVal1.get(s);
			}
		}
		for (String s : goSVal2.keySet()) {
			sgo2 += goSVal2.get(s);
			if (res.contains(s)) {
				sum += goSVal2.get(s);
			}
		}

		return sum / (sgo1 + sgo2 + 0.0001f);
	}

	/**
	 * 
	 * @param go
	 * @param goset
	 * @return float
	 * 
	 *         calculate go and goset sim
	 */
	public float goAndSetSemSim(String go, Set<String> goset) { // calculate the max one
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


	@SuppressWarnings("unchecked")
	public float geneSemSim(String gene1, String gene2) throws IOException,
	ClassNotFoundException {
		Map<String, LinkedHashSet<String>> map = (HashMap<String, LinkedHashSet<String>>) Serialization.load("gene2go.out");
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
