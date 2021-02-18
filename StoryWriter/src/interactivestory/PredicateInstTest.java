package interactivestory;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PredicateInstTest {

	@Before
	public void setUp() throws Exception {
		Predicate character = Predicate.createInstance("CHARACTER", 1);
		ObjectInst x = ObjectInst.createInstance("x");
		ObjectInst num0 = ObjectInst.createInstance("0");
		ObjectInst dago = ObjectInst.createInstance("Dagobert");
		
		PredicateInst pi = PredicateInst.createInstance(character, new ObjectInst[] {x});
		
	}

	@Test
	public void testSubstituteObjectInstArray() {
		Predicate p = Predicate.createInstance("TestPred", 1);
		PredicateInst pi = PredicateInst.createInstance(p, new ObjectInst[] {ObjectInst.createInstance("0")});
		System.out.println("Before substitution: " + pi);
		pi = pi.substitute(new ObjectInst[] {ObjectInst.createInstance("Test guy")});
		System.out.println("After substitution: " + pi);
		fail("Not yet implemented");
	}

	@Test
	public void testCreateInstance() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeepCopy() {
		fail("Not yet implemented");
	}

	@Test
	public void testMap() {
		fail("Not yet implemented");
	}

}
