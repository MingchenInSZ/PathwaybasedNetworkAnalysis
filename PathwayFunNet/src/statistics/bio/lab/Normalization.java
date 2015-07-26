package statistics.bio.lab;

import java.util.LinkedList;
import java.util.List;

/**
 * Implements the normalizatin method including minmax z-score
 * 
 * @author mingchen
 * @date May 3td,2015
 */
public class Normalization {
	/**
	 * normalization using minmax method
	 * 
	 * @param list
	 * @return
	 */
	public static List<? extends Number> minmax(List<Number> list) {
		List<Double> transed = new LinkedList<Double>();
		Double[] minmax = getMinAndMax(list);
		double range = minmax[1].doubleValue() - minmax[0].doubleValue();
		for (Number n : list) {
			transed.add(Double.valueOf((n.doubleValue()-minmax[0].doubleValue())/range));
		}
		return transed;
	}

	/**
	 * get the max and min value of the arraylist
	 * 
	 * @param list
	 * @return
	 */
	public static Double[] getMinAndMax(List<Number> list) {
		Double[] minmax = new Double[2];
		double min = Double.MAX_VALUE, max = Double.MIN_VALUE;
		for (Number n : list) {
			if (n.doubleValue() > max) {
				max = n.doubleValue();
			}
			if (n.doubleValue() < min) {
				min = n.doubleValue();
			}
		}
		minmax[0] = Double.valueOf(min);
		minmax[1] = Double.valueOf(max);
		return minmax;
	}

	/**
	 * get the average of the list
	 * 
	 * @param list
	 * @return
	 */
	public static double average(List<Number> list) {
		if (list.size() == 0) {
			return 0.0;
		}
		double avg = 0.0;
		for (Number n : list) {
			avg += n.doubleValue();
		}
		return avg / list.size();
	}

	/**
	 * get the standard variance of the list
	 * 
	 * @param list
	 * @return
	 */
	public static double std(List<Number> list) {
		double avg = average(list);
		double std = 0.0;
		for (Number n : list) {
			std += (n.doubleValue() - avg) * (n.doubleValue() - avg);
		}
		std = Math.sqrt(std / (list.size() - 1 + 0.0001));
		return std;
	}

	/**
	 * get the standard variance of the list given avg value
	 * 
	 * @param list
	 * @return
	 */
	public static double std(List<Number> list,double avg) {
		double std = 0.0;
		for (Number n : list) {
			std += (n.doubleValue() - avg) * (n.doubleValue() - avg);
		}
		std = Math.sqrt(std / (list.size() - 1 + 0.0001));
		return std;
	}

	/**
	 * normalization in z-score method
	 * 
	 * @param list
	 * @return
	 */
	public static List<? extends Number> zscore(List<Number> list){
		double avg = average(list);
		double std = std(list, avg);
		List<Double> rs = new LinkedList<Double>();
		for(Number n:list){
			rs.add(Double.valueOf((n.doubleValue() - avg) / (std + 0.0001)));
		}
		return rs;
	}
}
