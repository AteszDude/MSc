package interactivestory;

import java.util.Map;

/**
 * A Logical expression composed of several predicates and operations. It is a tree, composed of 1 or nodes and 1 operator
 * @author Attila Torda
 *
 */
public class LogicalExpr extends Logical {
	protected final Operator operator;
	protected Logical left;
	protected Logical right;
	
	/**Creates a unary expression like NOT X, or NONE Y
	 * 
	 * @param operator
	 * @param left
	 */
	public LogicalExpr(Operator operator, Logical left) {
		this.operator = operator;
		this.left = left;
	}
	
	public LogicalExpr (Operator operator, Logical left, Logical right) {
		this.operator = operator;
		this.left = left;
		this.right = right;
		
		//Safety check
		if(right == null && operator.isBinary())
			throw new AssertionError();
	}
	
	//Getter/setter
	public Logical getLeft() {return left;}
	public void setLeft(Logical left) {this.left = left;}
	public Logical getRight() {return right;}
	public void setRight(Logical right) {this.right = right;}	
	
	public boolean isUnary() {
		return operator.isUnary();
	}
	
	@Override
	public LogicalExpr substitute(ObjectInst[] newobjects) {
		left = left.substitute(newobjects);
		if(right != null) right = right.substitute(newobjects);
		
		return this;
	}
	

	@Override
	public boolean isTrue(State state) {
		if(isUnary()) return operator.apply(left.isTrue(state), false);
		else return operator.apply(left.isTrue(state), right.isTrue(state));
	}
	
	@Override
	public String toString() {
		if(isUnary()) return new String("(" + operator + " " + left + ")");
		else return new String("(" + left + " " + operator + " " + right + ")");
	}
	
	//TODO: not tested!
	@Override
	public boolean equals (Object other) {
		if(this == other) return true;
		if(!(other instanceof PredicateInst))
			return false;
		LogicalExpr o = (LogicalExpr)other;

		return operator == o.operator && left.equals(o.left) && (isUnary() ? right.equals(o.right) : true);
	}

	@Override
	public LogicalExpr deepCopy(){
		if(isUnary())
		 return new LogicalExpr(operator, left.deepCopy());
		else return new LogicalExpr(operator, left.deepCopy(), right.deepCopy());
	}

	@Override
	public Logical map(Map<ObjectInst, Integer> map) {
		left = left.map(map);
		if(right != null) right = right.map(map);
		
		return this;
	}
	
}