package pathway.bio.lab;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import serialization.bio.lab.Serialization;

/**
 * Get permutation pathway genes
 * 
 * @author mingchen
 * @date 2015Äê5ÔÂ3ÈÕ
 */
public class PathwayPermutation {
	private HashMap<String, String> allGenes = null;// contains all the unique
	// genes


	@SuppressWarnings("unchecked")
	public PathwayPermutation() {
		allGenes = (HashMap<String, String>)Serialization.load("allUniqueGenes.out");

	}

	/**
	 * 
	 * @param num
	 * @return HashSet<String>
	 */
	public HashSet<String> getPermGenes(int num) {
		HashSet<Integer> random = RandomGenerator.randomRange(allGenes.size(),1, num);
		HashSet<String> randGenes = new HashSet<String>();
		for (Integer t : random) {
			randGenes.add(allGenes.get(t.toString()));
		}
		return randGenes;
	}

	/**
	 * From the source get the number counts of random elements
	 * 
	 * @param num
	 * @param source
	 * @return HashSet<String>
	 */
	public HashSet<String> getPermGenes(int num, HashSet<String> source) {
		HashSet<Integer> randomIndex = RandomGenerator.randomRange(source.size(), 1, num);
		HashSet<String> randomGenes = new HashSet<String>();
		Object[] objs = source.toArray();
		for(Integer t : randomIndex){
			randomGenes.add((String) objs[t.intValue() - 1]);
		}
		return randomGenes;
	}

	/**
	 * From the permutation set get two hashset which seperately same to the
	 * original size
	 * 
	 * @param union
	 * @param slen
	 * @param tlen
	 * @return
	 */
	public HashMap<String, HashSet<String>> getPermGenes(
			HashSet<String> union,int slen,int tlen) {
		HashMap<String, HashSet<String>> map = new HashMap<String, HashSet<String>>();
		List<String> list = new LinkedList<String>(union);
		Collections.shuffle(list);
		HashSet<String> s = new HashSet<String>(), t = new HashSet<String>();
		for (int i = 0; i < list.size(); i++) {
			if (i < slen) {
				s.add(list.get(i));
			}
			if (i >= list.size() - tlen) {
				t.add(list.get(i));
			}

		}
		map.put("source", s);
		map.put("target", t);
		return map;
	}

	public static void main(String[] args) {
		PathwayPermutation pp = new PathwayPermutation();
		HashSet<String> source = new HashSet<String>();
		for (int i = 1; i < 1001; i++) {
			source.add(String.valueOf(i));
		}
		HashMap<String, HashSet<String>> map = pp
				.getPermGenes(source, 600, 700);
		System.out.println(map.get("source").size() +":"+map.get("target").size());
	}
}
