package utils.bio.lab;

import pathway.bio.lab.SignificancePathway;

/**
 * significance pathway extraction uitls
 * 
 * @author mingchen
 * @date 2015��5��14��
 */
public class SignificancePathwayUtils {

	public static void main(String[] args) {
		SignificancePathway sp = new SignificancePathway();
		sp.getSignificantPathwayCommonHyperGenom(0.01, "fdr",
				"sigpathwaygenom.csv");
	}
}
