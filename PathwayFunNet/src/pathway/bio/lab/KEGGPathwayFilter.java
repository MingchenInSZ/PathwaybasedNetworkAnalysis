package pathway.bio.lab;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Filter kegg in pathway size by setting min and max
 * 
 * @author mingchen
 * @date 2015Äê5ÔÂ3ÈÕ
 */
public class KEGGPathwayFilter {

	public static HashMap<String, HashSet<String>> keggFilter(
			HashMap<String, HashSet<String>> kegg, int min, int max) {
		HashMap<String, HashSet<String>> filteredKegg = new HashMap<String, HashSet<String>>();
		for (String key : kegg.keySet()) {
			if (kegg.get(key).size() > min && kegg.get(key).size() < max) {
				filteredKegg.put(key, kegg.get(key));
			}
		}
		return filteredKegg;
	}
}
