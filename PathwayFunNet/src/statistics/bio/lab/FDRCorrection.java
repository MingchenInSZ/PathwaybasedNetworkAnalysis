package statistics.bio.lab;

import java.util.Comparator;

import jsc.util.Rank;

/**
 * FDR correction for p values
 * 
 * @author mingchen
 * @date 2015年5月14日
 */
public class FDRCorrection {
	/**
	 * fdr method alias BH
	 * 
	 * @param pvals
	 */
	public static void fdr(double[] pvals) {
		double[] ranks = new Rank(pvals, 0.0).getRanks();
		double[] arr = new double[pvals.length];
		int i = 0;
		for (double d:pvals){
			arr[i++] = d;
		}
		for (i = 0; i < pvals.length; i++) {
			pvals[i] = pvals[i] * pvals.length / ranks[i];

		}
	}
}

/**
 * Implement the Comparator class for compare
 * 
 * @author mingchen
 * @date 2015年5月14日
 */
class SortComparator implements Comparator<Object> {

	@Override
	public int compare(Object arg0, Object arg1) {
		if (((Double) arg0).doubleValue() > ((Double) arg1).doubleValue()) {
			return 1;
		} else if (((Double) arg0).doubleValue() < ((Double) arg1)
				.doubleValue()) {
			return -1;
		}else{
			return 0;
		}
	}

}
