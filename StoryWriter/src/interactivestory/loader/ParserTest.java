package interactivestory.loader;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import interactivestory.Action;
import interactivestory.LogicalExpr;
import interactivestory.ObjectInst;
import interactivestory.Operator;
import interactivestory.Predicate;
import interactivestory.PredicateInst;
import interactivestory.StoryVariables;

import org.junit.Before;
import org.junit.Test;

public class ParserTest {	
	
	
	   @Before 
	   public void setUp() {
		   //TODO
	   }
	
	@Test
	public void testProcessObj() {
		String text = " Dago1  , Dago2 ";
		ObjectInst[] result = Parser.processObj(text);
		assertTrue(result.length == 2);
		assertEquals("Dago1", result[0].name);
		assertEquals("Dago2", result[1].name);
	}

	@Test
	public void testProcessPred() {
		String text = "HAS 2,  DO 1 ";
		Predicate[] result = Parser.processPred(text);
		assertTrue(result.length == 2);
		assertEquals("HAS", result[0].name);
		assertEquals(2, result[0].objnum);
		assertEquals(1, result[1].objnum);
	}

	@Test
	public void testProcessPI() {
		String text = " HAS( Dagobert, Ball) , HAS( Glomgold, Ball )";
		Map<String, Predicate> predList = new HashMap<>();
		predList.put("HAS 2", Predicate.createInstance("HAS", 2));		
		Map<String, ObjectInst> objects = new HashMap<>();
		objects.put("Dagobert", ObjectInst.createInstance("Dagobert"));
		objects.put("Glomgold", ObjectInst.createInstance("Glomgold"));
		objects.put("Ball", ObjectInst.createInstance("Ball"));


		PredicateInst[] result = Parser.processPI(text, predList, objects);
		assertTrue(result.length == 2);
		assertEquals("Dagobert", result[0].instances.get(0).name);
		assertEquals("Ball", result[0].instances.get(1).name);
		assertTrue(result[0].predicate.objnum == 2);
		assertEquals("Glomgold", result[1].instances.get(0).name);
		assertEquals("HAS", result[1].predicate.name);
	}

	@Test
	public void testProcessAction() {
		String text = "NOD(x) @output: $x nods @precondition: CHARACTER(x) @end";
		
		ObjectInst zero = ObjectInst.createInstance("0");
		Predicate character = Predicate.createInstance("CHARACTER", 1);
		PredicateInst pi1 = PredicateInst.createInstance(character, new ObjectInst[] {zero});
		
		Action exp = new Action.ActionBuilder("NOD", "$0 nods").addPrecon(new LogicalExpr(Operator.NONE, pi1))
		.setParamNumber(1).build();
		
		Action[] result = Parser.processAction(text, null, null);
		
		assertTrue(result.length == 1);
		assertEquals(exp.toString(), result[0].toString());
	}

	@Test
	public void testProcessAction2() {
		String text = "NOD(x, y) @output: $x $y nods @precondition: CHARACTER(x) AND CHARACTER(y) \n @add:  CHARACTER(x) "
				+ " @remove:CHARACTER(y), CHARACTER(x) \n \t   @end";
		
		ObjectInst zero = ObjectInst.createInstance("0");
		ObjectInst one = ObjectInst.createInstance("1");
		Predicate character = Predicate.createInstance("CHARACTER", 1);
		PredicateInst charx = PredicateInst.createInstance(character, new ObjectInst[] {zero});
		PredicateInst chary = PredicateInst.createInstance(character, new ObjectInst[] {one});
		List<PredicateInst> predstoAdd = new ArrayList<>();
		predstoAdd.add(charx);
		List<PredicateInst> predstoRemove = new ArrayList<>();
		predstoRemove.add(chary);
		predstoRemove.add(charx);
		
		
		Action exp = new Action.ActionBuilder("NOD", "$0 $1 nods").addPrecon(new LogicalExpr(Operator.AND, charx, chary))
		.addEffectAdd(predstoAdd).addEffectRem(predstoRemove).setParamNumber(2).build();
		
		Action[] result = Parser.processAction(text, null, null);
		
		assertTrue(result.length == 1);
		assertEquals(exp.toString(), result[0].toString());
	}	
	
	@Test
	public void testParseBracket() {
		String[] result = Parser.parseBracket("GIVE ( x , y )");
		assertTrue(result.length == 3);
		assertEquals(result[0], "GIVE");
		assertEquals(result[1], "x");
		assertEquals(result[2], "y");
	}

	@Test
	public void testParseLogical() {
		String text = "HAS(x, y) AND CHARACTER(y) AND (NOT CHARACTER( x ))";
		Map<String, Predicate> preds = new HashMap<>();

		Predicate character = Predicate.createInstance("CHARACTER", 1);
		Predicate has = Predicate.createInstance("HAS", 2);
		ObjectInst x = ObjectInst.createInstance("x");
		ObjectInst y = ObjectInst.createInstance("y");
		
		
		preds.put("HAS 2", has);		
		preds.put("CHARACTER 1", character);		
		
		LogicalExpr result = Parser.parseLogical(text, preds);
		
		PredicateInst pi1 = PredicateInst.createInstance(character, new ObjectInst[] {x});
		
		LogicalExpr subexp2 = new LogicalExpr(Operator.NOT, pi1);
		LogicalExpr subexp = new LogicalExpr(Operator.AND,
				PredicateInst.createInstance(character, new ObjectInst[] {y}), subexp2);
		LogicalExpr exp = new LogicalExpr(Operator.AND, PredicateInst.createInstance(has, new ObjectInst[] {x, y}),
				subexp);
		
		assertEquals(result.toString(), exp.toString());
		//assertEquals(result, exp); This doesn't work...!

	}

	@Test
	public void testProcessChange() {
		String text = "TIME + 1, ACTION - 2, COMEDY + 100";
		StoryVariables result = Parser.processChange(text);
		
		StoryVariables expectation = new StoryVariables();
		expectation.time += 1;
		expectation.action -= 2;
		expectation.comedy += 100;
		
		assertEquals(result.toString(), expectation.toString());
	}
	
	@Test
	public void testMakeTokens() {
		String test = "HAS(x, z) AND CHARACTER(y) AND CHARACTER(x)";
		
		TokenTree tokens = Parser.makeTokens(test, 0, test.length() - 1);
		String exp = "Token 5: [(PredInst, HAS(x, z), (Operator, AND), (PredInst, CHARACTER(y), (Operator, AND), (PredInst, CHARACTER(x)]";
		assertEquals(exp, tokens.toString());
	}

}
