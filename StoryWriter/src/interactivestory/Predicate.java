package interactivestory;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * A template for instances like EQUALS X Y, it's pair is Predicate Instance.
 * @author Attila Torda
 *
 */
public class Predicate {
	public final String name;
	public final int objnum;
	private static Map<String, Predicate> pool = new WeakHashMap<>();

	/**Creates a new predicate. Use the factory method instead!
	 * 
	 * @param name
	 * @param objnum
	 */
	private Predicate(String name, int objnum) {
		this.name = name;
		this.objnum = objnum;
	}
	
	@Override
	public String toString() {
		return name + " " + objnum;
	}

	@Override
	public int hashCode() {
		return name.hashCode() + objnum;
	}

	@Override
	public boolean equals(Object other) {
		if(this == other) return true;
		if(!(other instanceof Predicate))
			return false;
		Predicate o = (Predicate)other;
		return name.equals(o.name) && objnum == o.objnum;
	}
	
	/**Creates a new predicate in the pool. Predicates are immutable and they are all stored in a common pool.
	 * 
	 * @param name
	 * @param num
	 * @return
	 */
	public static Predicate createInstance(String name, int num) {
		String key = name + " " + num;
		Predicate instance = pool.get(key);
		if(instance == null)
		{
			instance = new Predicate(name, num);
			pool.put(key, instance);
		}
		return instance;
	}
	
}