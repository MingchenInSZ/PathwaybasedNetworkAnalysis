package pathway.bio.lab;

import java.util.Collection;
import java.util.HashSet;

/**
 * Get random numbers random range from min to max including the boundary
 * 
 * @author mingchen
 * @date 2015Äê5ÔÂ3ÈÕ
 */
public class RandomGenerator {

	private final int max;
	private final int min;
	private int num = 10;
	private static HashSet<Integer> hash = null;

	public RandomGenerator() {
		this(0,0,0);
	}
	public RandomGenerator(int max,int min){
		this(max, min, 0);
	}

	public RandomGenerator(int max, int min, int num) {
		if (max <= min) {
			System.err.println("Max should be greater than min");
		}
		this.max = max;
		this.min = min;
		this.num = num;
	}

	/**
	 * 
	 * @param max
	 * @param min
	 * @param num
	 * @return HashSet
	 */
	public static HashSet<Integer> randomRange(int max, int min, int num) {
		hash = new HashSet<Integer>();
		while (hash.size() < num) {
			hash.add(Integer.valueOf((int) (Math.random() * (max - min)) + min));
		}
		return hash;
	}

	/**
	 * 
	 * @return HashSet
	 */
	public HashSet<Integer> getRandom() {
		hash = new HashSet<Integer>();
		while (hash.size() < num) {
			hash.add(Integer.valueOf((int) (Math.random() * (max - min+1)) + min));
		}
		return hash;
	}

	/**
	 * @return the num
	 */
	public int getNum() {
		return num;
	}

	/**
	 * @param num
	 *            the num to set
	 */
	public void setNum(int num) {
		this.num = num;
	}

	/**
	 * @return the max
	 */
	public int getMax() {
		return max;
	}

	/**
	 * @return the min
	 */
	public int getMin() {
		return min;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String pre = "RandomGenerator [max=" + max + ", min=" + min + ", num=" + num+ "]{";
		for(Integer t:hash){
			pre += t.toString() + ",";
		}
		return pre.substring(0, pre.length() - 1) + "}";
	}

	/**
	 * 
	 * @param c
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <E> E randomGet(Collection<E> c) {
		if (c == null) {
			return null;
		}
		Object[] objs = c.toArray();
		int index = (int) (Math.random() * objs.length);
		return (E) objs[index];
	}

	public static void main(String[] args) {
		RandomGenerator rg = new RandomGenerator(10, 1, 5);
		rg.getRandom();
		System.out.println(rg.toString());
	}
}
