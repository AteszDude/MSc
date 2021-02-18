package interactivestory;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * An object, which consist only of a name.
 * @author Attila Torda
 *
 */
public class ObjectInst{
	public final String name;
	private static Map<String, ObjectInst> pool = new WeakHashMap<>();
	
	private ObjectInst(String name) {this.name = name;}
	
	@Override
	public boolean equals (Object other) {
		if(this == other) return true;
		if(!(other instanceof ObjectInst))
			return false;
		ObjectInst o = (ObjectInst)other;
		
		return o.name.equals(name);
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return name;
	}

	public static ObjectInst createInstance(String name) {
		ObjectInst instance = pool.get(name);
		if(instance == null)
		{
			instance = new ObjectInst(name);
			pool.put(name, instance);
		}
		return instance;

	}
	
}