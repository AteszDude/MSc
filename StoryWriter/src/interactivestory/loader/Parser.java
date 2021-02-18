package interactivestory.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import interactivestory.Action;
import interactivestory.Logical;
import interactivestory.LogicalExpr;
import interactivestory.ObjectInst;
import interactivestory.Operator;
import interactivestory.Predicate;
import interactivestory.PredicateInst;
import interactivestory.State;
import interactivestory.StoryVariables;
import interactivestory.StoryWorld;
import interactivestory.utils.Pair;

public class Parser {

	public static final String[] operatorList = {"NOT", "AND", "OR", "XOR"};
	private static final int HEAD = 0;//This should be refactored into a Pair
	
	//Available predicates
	Map<String, Predicate> predicates = new HashMap<>();
	Map<String, ObjectInst> objInsts = new HashMap<>();
	List<Action> actions = new ArrayList<>();
	List<PredicateInst> start = new ArrayList<>();
	List<PredicateInst> finish  = new ArrayList<>();
	
	/**The previously loaded dependencies
	 */
	static List<String> dependencies = new ArrayList<String>();
	static Set<String> processedDeps = new HashSet<String>();
	
	/**Reads a file at a given path. The result can be obtained by calling the getResult method.
	 * 
	 * @param filePath
	 */
	public Parser(String filePath) {
		//Resolve dependencies
		readDependencies(filePath);
		
		//Load dependencies and put them in front of the file
		StringBuilder finalText = new StringBuilder();
		
		for(int i = dependencies.size() - 1; i >= 0; i--) {
			String actFile = dependencies.get(i);
			if(processedDeps.contains(actFile)) continue;
			
			//Read file text into buffer and add filename into the set
			String text = readFile(actFile);
			finalText.append(text);//Add contents
			processedDeps.add(actFile);//Add filename to remove duplicates
		}
		
		//After reading dependencies, add the original file's content		
		finalText.append(readFile(filePath));
		String[] splitText = finalText.toString().split(";");
				
		//Iterate through every one and decide which method to call
		for(String command : splitText) {
			command = command.trim();
			if(command.toLowerCase().startsWith("objects:")) {
				ObjectInst[] objects = processObj(command.substring(8).trim());
				for(ObjectInst obj : objects)
					objInsts.put(obj.name, obj);
			}
			else if(command.toLowerCase().startsWith("predicates:")) {
				Predicate[] newPreds = processPred(command.substring(11).trim());
				//Add the new predicates to our list
				for(Predicate p : newPreds)
					predicates.put(p.name + " " + p.objnum, p);
			}
			else if(command.toLowerCase().startsWith("start:")) {
				start.addAll(Arrays.asList(processPI(command.substring(6).trim(), predicates, objInsts)));
			}
			else if(command.toLowerCase().startsWith("finish:")) {
				finish.addAll(Arrays.asList(processPI(command.substring(7).trim(), predicates, objInsts)));
			}
			else if(command.toLowerCase().startsWith("dependencies:")) {
				//Do nothing, already processed
			}
			else if(command.toLowerCase().startsWith("actions:"))
				actions.addAll(Arrays.asList(
				processAction(command.substring(8).trim(), null, null)));//TODO: don't assign null!

		}
		
	}
	
	/**Add the dependencies in a list form the dependencies list.
	 * 
	 * @param fileName
	 */
	private static void readDependencies(String fileName) {
		
		try{
			File namefile = new File(fileName);
			FileReader namereader = new FileReader(namefile);
			BufferedReader in = new BufferedReader(namereader);
		
			String firstLine = in.readLine().split(";")[0];
			String[] deps = new String[] {};
		
			if(firstLine.toLowerCase().contains("dependencies:"))
				deps = firstLine.split(":")[1].split(",");

			for(String s : deps) {
				String cached = s.trim();
				dependencies.add(cached);
				readDependencies(cached);
			}
		
		}
		catch(Exception e){
			System.out.println(e);
		}
		
	}
	
	/**Returns the obtained data from the given file as a StoryWorld.
	 * 
	 * @return
	 */
	public StoryWorld getResult() {
		StoryWorld result = new StoryWorld(objInsts.values(), predicates.values(), actions);
		result.setStart(State.createInstance(start));
		result.setFinish(State.createInstance(finish));
		return result;
	}
	
	/**Processes a list of Objects, eg. Dagobert, Glomgold
	 * 
	 * @param text
	 * @return
	 */
	public static ObjectInst[] processObj(String text) {
		ObjectInst[] result;
		String[] objs = text.split(",");
		result = new ObjectInst[objs.length];
		
		for(int i = 0; i < objs.length; i++)
			result[i] = ObjectInst.createInstance(objs[i].trim());
		
		return result;
	}

	/**Processes a list of predicates, eg. HAS 2, FULL 1
	 * 
	 * @param text
	 * @return
	 */
	public static Predicate[] processPred(String text) {
		Predicate[] result;
		String[] objs = text.split(",");
		result = new Predicate[objs.length];
		
		for(int i = 0; i < objs.length; i++)
		{
			String[] nameValue = objs[i].trim().split(" ");
			result[i] = Predicate.createInstance(nameValue[0].trim(), Integer.parseInt(nameValue[1]));
		}

		return result;
	}
	
	/**Processes StoryVariables change in an action
	 * 
	 * @param text
	 * @return
	 */
	public static StoryVariables processChange(String text) {
		String[] changes = text.split(",");
		StoryVariables result = new StoryVariables();
				
		for(String expression : changes) {
			String[] expParts = expression.trim().split(" ");

			int value = Integer.parseInt(expParts[2]);
			if(expParts[1].trim().equals("-")) value *= -1;

			switch(expParts[0].trim().toLowerCase()) {
			case("time"):
				result.time += value;
				break;
			case("action"):
				result.action += value;
				break;
			case("drama"):
				result.drama += value;
				break;
			case("comedy"):
				result.comedy += value;
				break;
			case("romance"):
				result.romance += value;
				break;
			case("outcome"):
				result.outcome += value;
				break;
			case("achieve"):
				result.achieve += value;
				break;		
			default: break;
			}
			
		}
		
		return result;
	}
	
	/**Processes a list of predicate instances, eg. HAS(DAGOBERT, BALL), FULL(COUSINS)
	 * 
	 * @param text
	 * @param predList
	 * @param objects
	 * @return
	 */
	public static PredicateInst[] processPI(String text, Map<String, Predicate> predList,
			Map<String, ObjectInst> objects) {

		char[] textChar = text.toCharArray();
		for(int i = 0, level = 0; i < textChar.length; i++) {
			if(textChar[i] == '(') level++;
			else if (textChar[i] == ')') level--;
			else if (textChar[i] == ',' && level == 0) textChar[i] = ';';
		}
		String[] objs = new String(textChar).split(";");

		PredicateInst[] result;
		result = new PredicateInst[objs.length];
		
		for(int i = 0; i < objs.length; i++)
		{
			String[] act = parseBracket(objs[i]);
			Predicate pred = predList == null ? Predicate.createInstance(act[HEAD], act.length - 1)
					: predList.get(act[HEAD] + " " + (act.length - 1));
			ObjectInst[] objectInsts = new ObjectInst[act.length - 1];
			
			if(pred == null)
				assert(false);
			for(int actStr = 1; actStr < act.length; actStr++) {
				if(objects == null) objectInsts[actStr - 1] = ObjectInst.createInstance(act[actStr].trim());
				else objectInsts[actStr - 1] = objects.get(act[actStr].trim());
				if(objectInsts[actStr - 1] == null){
					System.out.println(Arrays.deepToString(objs));
					assert(false);
				}
			}
			result[i] = PredicateInst.createInstance(pred, objectInsts);
		}

		return result;
	}
	
	/**Processes a list of actions, eg. "NOD(x) @output: $x nods @precondition: CHARACTER(x) @end"
	 * 
	 * @param text
	 * @param preds
	 * @param objects
	 * @return
	 */
	public static Action[] processAction(String text, Map<String, Predicate> preds,
			Map<String, ObjectInst> objects) {
		String[] splittext = text.split("@end");
		Action[] result = new Action[splittext.length];
		
		//For each action
		for(int i = 0; i < splittext.length; i++) {
			String[] properties = splittext[i].split("@");
			LogicalExpr precondition = null;
			List<PredicateInst> effectAdd = new ArrayList<>();
			List<PredicateInst> effectRem = new ArrayList<>();
			String templateText = "";//The text that an action outputs
			StoryVariables changes = new StoryVariables();
			
			//Action name and parameters
			String[] header = parseBracket(properties[0]);
			String name = header[0];
			String[] vars = Arrays.copyOfRange(header, 1, header.length);
			
			for(int j = 1; j < properties.length; j++) {
				String[] actProp = properties[j].split(":");
				String propName = actProp[0].trim();
				String propData = actProp[1].trim();
					switch(propName.toLowerCase()) {
					case("precondition"):
						precondition = parseLogical(propData, preds);
						break;
					case("add"):
						effectAdd.addAll(Arrays.asList(processPI(propData, preds, objects)));
						break;
					case("remove"):
						effectRem.addAll(Arrays.asList(processPI(propData, preds, objects)));
						break;
					case("change"):
						changes = processChange(propData);
						break;
					case("output"):
						templateText = propData;
						break;
					default: assert(false);
					}
			}
			result[i] = new Action.ActionBuilder(name, templateText).addEffectAdd(effectAdd).addEffectRem(effectRem)
					.addPrecon(precondition).addSubstitutionPattern(vars).setChanges(changes).build();
		}//for each Action
		
		return result;
	}
	
	/**Takes a bracketed expression as an input and returns a split representation of it.
	 * "GIVE(x, y)" returns ["GIVE", "x", "y"]
	 * 
	 */
	public static String[] parseBracket(String text) {
		String[] result;
		text = text.replace('(', ',').replace(')', ' ');
		result = text.split(",");
		
		for(int i = 0; i < result.length; i++)
			result[i] = result[i].trim();

		return result;
	}
	
	/**Reads a file into String, uses the AutoCloseable (Java 7) feature
	 * 
	 * @param filename
	 * @return
	 */
	private static String readFile(String filename) {
		 try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
		        StringBuilder sb = new StringBuilder();
		        String line = br.readLine();

		        while (line != null) {
		            sb.append(line);
		            sb.append(System.lineSeparator());
		            line = br.readLine();
		        }
		        return sb.toString();
		    }
		 catch (Exception e) {
			 System.out.println("Error while reading file: " + e);
		 }
		 return "";
	}
	
	/**Parses a logical expression into a tree, eg. HAS(x, y) AND CHARACTER(y) AND (NOT CHARACTER( x ))
	 * 
	 * @param text
	 * @param preds
	 * @return
	 */
	public static LogicalExpr parseLogical(String text, Map<String, Predicate> preds) {
		
		TokenTree root = makeTokens(text, 0, text.length() - 1);
		Logical result = convertTree(root, preds);
		
		if(result instanceof PredicateInst){
			return new LogicalExpr(Operator.NONE, result);
		}
		
		
		return (LogicalExpr) convertTree(root, preds);
	}
	
	/**Recursively builds a logical expression from tokens
	 * 
	 * @param tree
	 * @param preds
	 * @return
	 */
	static Logical convertTree(TokenTree tree, Map<String, Predicate> preds) {
		//A single element, which could be an expression or a predicate instance
		if(tree.contents.size() == 1) {
			Type act = tree.contents.get(0).first;
			switch(act) {
			case Expression:
				return convertTree((TokenTree)tree.contents.get(0).second, preds);
			case Operator:
				assert(false);
				return null;
			case PredInst:
				PredicateInst[] pi = processPI((String) tree.contents.get(0).second, preds, null);
				assert(pi.length == 1);
				return pi[0];
			default:
				assert(false);
				break;
			}
		}
		//Unary expression (NOT)
		else if(tree.contents.get(0).first == Type.Operator) {
			return new LogicalExpr(Operator.NOT, convertTree(tree.tail(1), preds));
		}
		
		//Binary expression
		else if(tree.contents.size() >= 3 && tree.contents.get(1).first == Type.Operator) {
			return new LogicalExpr((Operator) tree.contents.get(1).second, convertTree(tree.head(0), preds),
					convertTree(tree.tail(2), preds));
		}
		
		else assert(false);
		
		return null;
	}
	
	/**Makes tokens from texts
	 * 
	 * @param text
	 * @param start
	 * @param end
	 * @return
	 * @see interactivestory.loader.TokenTree
	 */
	static TokenTree makeTokens(String text, int start, int end) {
		//Remove leading and trailing whitespace
		while(text.charAt(start) == ' ') start++;
		while(text.charAt(end) == ' ') end--;

		TokenTree result = new TokenTree();
		
		//The current character to be processed, always goes left to right
		for(int pos = start; pos <= end; pos++) {

		//Check for operator
		int opMatch = matchOp(text, pos);
		if(opMatch  > -1) {
			result.insert(Type.Operator, Operator.valueOf(operatorList[opMatch]));
			pos += operatorList[opMatch].length() - 1;
		}
		
		//Check for expressions in brackets
		else if(text.charAt(pos) == '(') {
			//Skip until closing brackets
			int substart = pos + 1;

			for(int level = 1; level > 0;) {
				pos++;
				if(text.charAt(pos) == '(') level++;
				else if(text.charAt(pos) == ')') level--;
			}
			int subend = pos - 1;

			result.insert(Type.Expression, makeTokens(text, substart, subend));
		}

		//Check for predinst
		else if(text.charAt(pos) != ' ') {
			int substart = pos;
			while(text.charAt(pos) != ')')
				pos++;
			
			result.insert(Type.PredInst, text.substring(substart, pos));
		}
		
		}//for
		
		return result;
	}//parse
	
	static int matchOp(String text, int pos) {
		for(int i = 0; i < operatorList.length; i++)
			if(text.regionMatches(pos, operatorList[i], 0, operatorList[i].length()))
				return i;
		return -1;
	}
}

enum Type {Expression, Operator, PredInst};

/**A class for parsing logical expressions.
 * 
 * @author Attila Torda
 *
 */
class TokenTree {
	
	//Package public
	List<Pair<Type, Object>> contents;

	public TokenTree() {
		contents = new ArrayList<>();
	}
	
	public TokenTree(List<Pair<Type, Object>> contents) {
		this.contents = contents;
	}
	
	public void insert(Type type, Object value) {
		contents.add(new Pair<Type, Object>(type, value));
	}
	
	@Override
	public String toString() {
		return "Token " + contents.size() + ": " + contents.toString();
	}
	
	public TokenTree tail(int fromIndex) {
		return new TokenTree(contents.subList(fromIndex, contents.size()));
	}
	
	public TokenTree head(int toIndex) {
		return new TokenTree(contents.subList(0, toIndex + 1));
	}
	
}
