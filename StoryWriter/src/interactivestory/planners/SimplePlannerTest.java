package interactivestory.planners;

import static org.junit.Assert.*;
import interactivestory.Action;
import interactivestory.LogicalExpr;
import interactivestory.ObjectInst;
import interactivestory.Operator;
import interactivestory.Predicate;
import interactivestory.PredicateInst;
import interactivestory.State;
import interactivestory.utils.Triple;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class SimplePlannerTest {

	List<Action> actions;
	List<ObjectInst> objects;
	List<Predicate> predicates;
	List<State> states;
	State start;
	State finish;
	
	@Before
	public void setUp() throws Exception {
		objects.add(ObjectInst.createInstance("Dagobert"));//0
		predicates.add(Predicate.createInstance("CHARACTER", 1));//0

		objects.add(ObjectInst.createInstance("A Ball"));//1
		predicates.add(Predicate.createInstance("FREE", 1));//1
		
		predicates.add(Predicate.createInstance("HAS", 2));//2

		objects.add(ObjectInst.createInstance("Cake"));//2
		predicates.add(Predicate.createInstance("EDIBLE", 1));//3
		predicates.add(Predicate.createInstance("EATEN", 1));//4

		predicates.add(Predicate.createInstance("FULL", 1));//5
		
		
		ObjectInst x = ObjectInst.createInstance("0");//X
		ObjectInst y = ObjectInst.createInstance("1");//Y
		
		PredicateInst Dchar = PredicateInst.createInstance(predicates.get(0), new ObjectInst[] {objects.get(0)});
		PredicateInst Bfree = PredicateInst.createInstance(predicates.get(1), new ObjectInst[] {objects.get(1)});
		PredicateInst Cfree = PredicateInst.createInstance(predicates.get(1), new ObjectInst[] {objects.get(2)});
		PredicateInst Cedi = PredicateInst.createInstance(predicates.get(3), new ObjectInst[] {objects.get(2)});

		//First action: TAKE
		//Precondition: CHARACTER(X) AND FREE(Y)
		LogicalExpr log = new LogicalExpr(Operator.AND,
				PredicateInst.createInstance(predicates.get(0), new ObjectInst[] {x}),
				PredicateInst.createInstance(predicates.get(1), new ObjectInst[] {y}));
		//Effect: HAS(X, Y) AND -FREE(Y)
		actions.add(new Action.ActionBuilder("TAKE", "").addPrecon(log).addEffectAdd(new PredicateInst[] {PredicateInst.createInstance(predicates.get(2), new ObjectInst[]{x, y})})
		.addEffectRem(new PredicateInst[] {PredicateInst.createInstance(predicates.get(1), new ObjectInst[] {y})}).setParamNumber(2).build());
		System.out.println("Action: " + actions.get(0));
		
		//Second action: EAT
		//Precondition: CHARACTER(X) AND EDIBLE(Y) AND HAS(X, Y)
		LogicalExpr log2 = new LogicalExpr(Operator.AND, (new LogicalExpr(Operator.AND,
				PredicateInst.createInstance(predicates.get(0), new ObjectInst[] {x}),
				PredicateInst.createInstance(predicates.get(3), new ObjectInst[] {y}))),
				PredicateInst.createInstance(predicates.get(2), new ObjectInst[] {x, y}));
		//Effect: EATEN (Y) AND FULL(X)
		actions.add(new Action.ActionBuilder("EAT", "").addPrecon(log2).addEffectAdd(new PredicateInst[] {PredicateInst.createInstance(predicates.get(5), new ObjectInst[]{x}),
				PredicateInst.createInstance(predicates.get(4), new ObjectInst[]{y})}).setParamNumber(2).build());
		System.out.println("Action: " + actions.get(1));

		//Third action: DROP
		//Precondition: CHARACTER(X) AND HAS(X, Y)
		LogicalExpr log3 = new LogicalExpr(Operator.AND,
				PredicateInst.createInstance(predicates.get(0), new ObjectInst[] {x}),
				PredicateInst.createInstance(predicates.get(2), new ObjectInst[] {x, y}));
		//Effect: FREE(Y) AND -HAS(X, Y)
		actions.add(new Action.ActionBuilder("DROP", "").addPrecon(log3).addEffectAdd(new PredicateInst[] {PredicateInst.createInstance(predicates.get(1), new ObjectInst[]{y})})
		.addEffectRem(new PredicateInst[] {PredicateInst.createInstance(predicates.get(2), new ObjectInst[]{x, y})}).setParamNumber(2).build());


		
		//The starting and finish states
		start = State.createInstance(new PredicateInst[] {Dchar, Bfree, Cfree, Cedi});
		finish = State.createInstance(new PredicateInst[] {
				PredicateInst.createInstance(predicates.get(5), new ObjectInst[] {objects.get(0)})});
		

	}

	@Test
	public void testStart() {
		fail("Not yet implemented");
	}

}
