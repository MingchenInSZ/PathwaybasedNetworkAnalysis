package hypergenom.bio.lab;

import java.math.BigDecimal;

/**
 * 
 * @author mingchen
 * @date 2015.5.3
 * 
 */
public class HyperGenomFast {
	private static BigDecimal exp = new BigDecimal(Math.E);
	/**
	 * Change the continuous multiplication into addition
	 * 
	 * @param n
	 * @param m
	 * @return double
	 */
	public static double lnchoice(int n, int m) {
		// choose to calculate the small part
		if (m > n / 2.0) {
			m = n - m;
		}
		double d = 0.0;
		for (int i = m + 1; i <= n; i++) {
			d += Math.log(i);
		}
		for (int i = 2; i <= n - m; i++) {
			d -= Math.log(i);
		}
		return d;
	}

	public static double choice(int n, int m) {
		return Math.exp(lnchoice(n, m));
	}
	/**
	 * Calculate HyperGenom using lnchoice above
	 * 
	 * @param N
	 * @param M
	 * @param n
	 * @param k
	 * @return double
	 */
	public static double lnHyperGenom(int m, int n, int t, int k) {
		return lnchoice(t, k) + lnchoice(m - t, n - k) - lnchoice(m, n);
	}

	/**
	 * Actual hypergenom value
	 * 
	 * @param N
	 * @param M
	 * @param n
	 * @param k
	 * @return double
	 */
	public static double hyperGenom(int N, int M, int n, int k) {
		return Math.exp(lnHyperGenom(N, M, n, k));
	}

	/**
	 * Calculate lnhyperGenom using BigDecimal
	 * 
	 * @param N
	 * @param M
	 * @param n
	 * @param k
	 * @return BigDecimal
	 */
	public static BigDecimal hyperGenomBig(int N, int M, int n, int k) {
		return BigDecimalOper.exp(new BigDecimal(lnHyperGenom(N, M, n, k)));
	}
	/**
	 * 
	 * @param N
	 * @param M
	 * @param n
	 * @param k
	 * @return double
	 */
	public static double pHyperGenom(int N, int M, int n, int k) {
		int c = 0;
		double d = 0.0;
		while (c < k) {
			d += hyperGenom(N, M, n, c++);
		}
		return d;
	}

	/**
	 * 
	 * @param N
	 * @param M
	 * @param n
	 * @param k
	 * @return double
	 */
	public static double pHyerGenomBig(int N, int M, int n, int k) {
		BigDecimal base = new BigDecimal(1);
		int c = 0;
		while (c < k) {
			base = base.subtract(hyperGenomBig(N, M, n, c++));
		}
		return base.doubleValue();
	}


}
