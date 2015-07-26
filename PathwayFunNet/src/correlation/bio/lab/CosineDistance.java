package correlation.bio.lab;

import java.util.List;

/**
 * Cosine similarity
 * 
 * @author mingchen
 * @date May 2nd,2015
 * 
 */
public class CosineDistance {

	/**
	 * calculate the cosine similarity of the two vector denote by list
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static double cosine(List<Double> x, List<Double> y) {
		double cos = 0.0;
		if (x.isEmpty() || y.isEmpty() || x.size() != y.size()) {
			return 0.0;
		}
		double dividend = 0.0, xsq = 0.0, ysq = 0.0;
		for (int i = 0; i < x.size(); i++) {
			dividend += x.get(i).doubleValue() * y.get(i).doubleValue();
			xsq += x.get(i).doubleValue() * x.get(i).doubleValue();
			ysq += y.get(i).doubleValue() * y.get(i).doubleValue();
		}
		cos = dividend / (Math.sqrt(xsq * ysq) + 0.0001);
		return cos;
	}

}
