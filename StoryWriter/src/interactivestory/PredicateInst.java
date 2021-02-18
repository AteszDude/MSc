package interactivestory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * An instantiation of a Predicate. Here instantiation means assigning objects to values,
 * for example HAS(X, Y) becomes HAS(DAGOBERT, CAKE) 
 * @author Attila Torda
 *
 */
public class PredicateInst extends Logical {
	public final Predicate predicate;
	public final List<ObjectInst> instances;
	private static Map<String, PredicateInst> pool = new WeakHashMap<>();
	
	//Hashcode is computed once, then stored
	private final int hashCode;
	
	/**Creates a new Predicate Instance, where the objects are assigned to the given predicate. Set to private, use factory method!
	 * 
	 * @param predicate
	 * @param objects
	 */
	private PredicateInst(Predicate predicate, ObjectInst[] objects) {
		this.predicate = predicate;
		instances = Collections.unmodifiableList(Arrays.asList(objects));
		hashCode = predicate.hashCode() + instances.hashCode();
	}
	
	public PredicateInst substitute(ObjectInst[] other) {
	//Iterate through all the objects in all the predicates and substitute
	ObjectInst[] newInst = new ObjectInst[instances.size()];
	
	for (int i = 0; i < newInst.length; i++)
		newInst[i] = ObjectInst.createInstance(other[Integer.parseInt(instances.get(i).name)].name);

	return new PredicateInst(predicate, newInst);
	}
	
	public int getLength() {return instances.size();}
	
	public boolean isTrue(State state) {
		return state.evaluateTrue(this);}

	@Override
	public int hashCode() {
	return hashCode;
	}

	@Override
	public boolean equals (Object other) {
		if(this == other) return true;
		if(!(other instanceof PredicateInst))
			return false;
		PredicateInst o = (PredicateInst)other;
		
		//Should be working
		return o.predicate.equals(predicate) && instances.equals(instances);
	}
	
	@Override
	public String toString() {
		//Can an array be converted into a string?
		return new String(predicate + " " + instances);
	}

	/**Factory method. Returns a reference to the predicate instance, or if not in the pool, creates a new one!
	 * 
	 * @param predicate
	 * @param objects
	 * @return
	 */
	public static PredicateInst createInstance(Predicate predicate, ObjectInst[] objects) {
		String key = predicate + " " + Arrays.deepToString(objects);
		PredicateInst instance = pool.get(key);
		if(instance == null)
		{
			instance = new PredicateInst(predicate, objects);
			pool.put(key, instance);
		}
		return instance;
	}
	
	
	@Override
	protected PredicateInst deepCopy() {
		return this;
	}

	@Override
	public PredicateInst map(Map<ObjectInst, Integer> map) {
		if(instances == null || instances.size() < 1) return this;
		
		
		ObjectInst[] newList = new ObjectInst[instances.size()];
		for(int i = 0; i < instances.size(); i++)
			newList[i] = ObjectInst.createInstance((map.get(instances.get(i))).toString());
		return createInstance(predicate, newList);
	}
	
}
