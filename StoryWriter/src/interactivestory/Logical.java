package interactivestory;

import java.util.Map;

/**An abstract class that represents a logical expression.
 * A Logical could be a Predicate (eg. NOT X), or an expression composed of predicates.
 * @author Attila Torda
 *
 */
public abstract class Logical {
	/**Is the logical condition true, when the following are true?
	 * 
	 * @param state A State which is composed of a list of predicates
	 * @return
	 */
	public abstract boolean isTrue (State state);
	
	/**Maps the template to the given Object Instances
	 * 
	 * @param other
	 * @return
	 */
	public abstract Logical substitute(ObjectInst[] other);
	
	/**Maps the Object Instances to Integers, which makes a template out of the class
	 * 
	 * @param map
	 * @return
	 */
	public abstract Logical map(Map<ObjectInst, Integer> map);
	protected abstract Logical deepCopy();
}
