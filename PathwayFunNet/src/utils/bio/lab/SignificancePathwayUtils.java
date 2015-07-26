package utils.bio.lab;

import pathway.bio.lab.SignificancePathway;

/**
 * significance pathway extraction uitls
 * 
 * @author mingchen
 * @date 2015Äê5ÔÂ14ÈÕ
 */
public class SignificancePathwayUtils {

	public static void main(String[] args) {
		SignificancePathway sp = new SignificancePathway();
		sp.getSignificantPathwayCommonHyperGenom(0.01, "fdr",
				"sigpathwaygenom.csv");
	}
}
