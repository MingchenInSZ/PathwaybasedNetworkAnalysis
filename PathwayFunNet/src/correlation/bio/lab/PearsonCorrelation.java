package correlation.bio.lab;

import java.util.List;

/**
 * calculate the pearson correlation coefficient
 * 
 * @author mingchen
 * @date May 2nd, 2015
 * 
 */
public class PearsonCorrelation {

	/**
	 * calculate the pearson cor
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static double pearson(List<Double> x, List<Double> y) {
		double cor = 0.0;
		double[] avg = average(x, y);
		cor = numerator(x, y, avg) / (denominator(x, y, avg) + 0.0001);
		return cor;
	}

	/**
	 * Get the x and y average from their List numbers(Double)
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public static double[] average(List<Double> x, List<Double> y) {
		double[] result = new double[2];
		if(x.isEmpty() ||y.isEmpty() ||x.size()!=y.size()){
			return null;
		}
		for (int i = 0; i < x.size(); i++) {
			result[0] += x.get(i).doubleValue();
			result[1] += y.get(i).doubleValue();
		}
		result[0] /= x.size();
		result[1] /= y.size();
		return result;
	}

	/**
	 * calculate the numerator/dividend
	 * 
	 * @param x
	 * @param y
	 * @param avg
	 * @return
	 */
	public static double numerator(List<Double> x, List<Double> y, double[] avg) {
		double fenzi = 0.0;
		for (int i = 0; i < x.size(); i++) {
			fenzi += (x.get(i).doubleValue() - avg[0])
					* (y.get(i).doubleValue() - avg[1]);
		}
		return fenzi;
	}

	/**
	 * calculate the denominator/divider
	 * 
	 * @param x
	 * @param y
	 * @param avg
	 * @return
	 */
	public static double denominator(List<Double> x, List<Double> y,double [] avg) {
		double fenmu = 0.0, xstd = 0.0, ystd = 0.0;
		for (int i = 0; i < x.size(); i++) {
			xstd += (x.get(i).doubleValue()-avg[0])*(x.get(i).doubleValue()-avg[0]);
			ystd += (y.get(i).doubleValue()-avg[1])*(y.get(i).doubleValue()-avg[1]);
		}
		fenmu = Math.sqrt(xstd * ystd);
		return fenmu;
	}
}
