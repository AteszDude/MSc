package interactivestory.utils;

/**An IMMUTABLE implementation of the missing Triple class!
 * 
 * @author Attila Torda
 *
 * @param <F>
 * @param <S>
 * @param <T>
 */
public class Triple<F, S, T>{
	public final F first;
	public final S second;
	public final T third;

    public Triple(F first, S second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;

    }

    @Override
    public String toString() {
    	return "(" + first + ", " + second + ", " + third + ")";
    }

}
