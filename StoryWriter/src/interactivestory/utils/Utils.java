package interactivestory.utils;

import java.util.Arrays;

public class Utils {

	/**Merge 2 arrays
	 * 
	 * @param first
	 * @param second
	 * @return the merged array
	 * @author http://stackoverflow.com/questions/80476/how-to-concatenate-two-arrays-in-java
	 */
	public static <T> T[] concat(T[] first, T[] second) {
		  T[] result = Arrays.copyOf(first, first.length + second.length);
		  System.arraycopy(second, 0, result, first.length, second.length);
		  return result;
		}
}
