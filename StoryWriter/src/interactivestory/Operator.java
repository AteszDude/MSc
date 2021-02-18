package interactivestory;

/**Refers to the logical expressions, respectively AND, OR, NOR, XOR.
 * NONE is a hack, so that a Numerical Expression is able to hold a single predicate.
 * 
 * @author Attila Torda
 *
 */
public enum Operator {
	AND, OR, NOT, XOR, NONE;
	
	public boolean isUnary() {if(this == NOT || this == NONE) return true; return false;}
	public boolean isBinary() {
		if(this == AND || this == NOT || this == XOR) return true; return false;}
	
	/**Apply the given operator
	 * 
	 * @param x left side
	 * @param y right side
	 * @return
	 */
	public boolean apply(boolean x, boolean y) {
		switch(this) {
		case AND: return x && y;
		case OR: return x || y;
		case NOT: return !x;
		case XOR: return x ^ y;
		case NONE: return x;
		}
		throw new AssertionError("Unknown operation: " + this);
	}
}