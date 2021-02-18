package interactivestory;

import interactivestory.utils.Triple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Equivalent to the Action in Planner languages. It consists of a precondition, an effect and a list of parameters.
 * @author Attila Torda
 *
 */
public class Action {
	
	protected LogicalExpr precondition;
	protected final List<PredicateInst> effectAdd;
	protected final List<PredicateInst> effectRem;
	protected final StoryVariables sv;
	protected final String name;
	protected final String templateText;
	protected final int numParameters;
	
	/**Builder pattern
	 * 
	 * @param builder
	 */
	private Action(ActionBuilder builder) {
		this.precondition = builder.precondition;
		this.name = builder.name;
		this.templateText = builder.templateText;
		this.effectAdd = builder.effectAdd;
		this.effectRem = builder.effectRem;
		this.numParameters = builder.noOfParameters;
		this.sv = builder.sv;
	}
	
	/**How many inputs does this action take
	 * 
	 * @return number of input paramaters
	 */
	public int getNumPar() {return numParameters;}
	
	public LogicalExpr createCondition(ObjectInst[] objects) {
		if(precondition == null || objects.length < numParameters) return null;
		return precondition.deepCopy().substitute(objects);
	}
	
	public List<PredicateInst> getEffectAdd() {return Collections.unmodifiableList(effectAdd);}
	
	public Triple<String, List<PredicateInst>, List<PredicateInst>> getEffects(ObjectInst[] objects) {
		if(objects.length < numParameters) return null;
		
		StringBuilder sb = new StringBuilder();
		int lastPos = 0;
		
		
		for(int i = 0; i < templateText.length(); i++)
			if(templateText.charAt(i) == '$') {
				int from = i;
				while(i < templateText.length() && templateText.charAt(i) != ' ') i++;
				if(from - 1 > lastPos)sb.append(templateText.subSequence(lastPos, from - 1) + " ");
				sb.append(objects[Integer.parseInt(templateText.substring(from + 1, i))]);
				lastPos = i;
			}
		sb.append(templateText.substring(lastPos, templateText.length()));
		
		//Effect (Add): make a list of predicates to be ADDED
		List<PredicateInst> newAdditions = new ArrayList<>(effectAdd.size());
		for(PredicateInst pi : effectAdd) {
			newAdditions.add(pi.substitute(objects));
		}

		//Effect (Remove): make an array of predicates be REMOVED		
		List<PredicateInst> newRemoves = new ArrayList<>(effectRem.size());
		for(PredicateInst pi : effectRem) {
			newRemoves.add(pi.substitute(objects));
		}

		return new Triple<String, List<PredicateInst>, List<PredicateInst>>(sb.toString(), newAdditions, newRemoves);
	}	
	
	public StoryVariables getChanges() {
		return sv;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode() + numParameters;
	}
	
	public String getName() {return name;}
	
	@Override
	public String toString() {
		return "Action " + name + " " + numParameters + " (precondition:" + precondition + "\n add:"
	+ Arrays.toString(effectAdd.toArray()) + "\n remove:" + Arrays.toString(effectRem.toArray())
	+ "\n text:" + eventText() + "\n changes:" + sv + ")";
	}
	
	public String eventText() {
		return templateText;
	}
	
	public static class ActionBuilder {
		protected LogicalExpr precondition;
		protected List<PredicateInst> effectAdd = new ArrayList<>();
		protected List<PredicateInst> effectRem = new ArrayList<>();
		protected StoryVariables sv = new StoryVariables();
		protected final String name;
		protected String templateText;
		protected String[] substitution;
		private int noOfParameters = -1;

		/**Builder class for Action.
		 * 
		 * @param name the action's name
		 * @param templateText the resulting text to display
		 */
		public ActionBuilder(String name, String templateText) {
			this.name = name;
			this.templateText = templateText;
		}
		
		/** Which predicates should the Action add after execution
		 * 
		 * @param predsToAdd the predicates to add
		 * @return
		 */
		public ActionBuilder addEffectAdd(Collection<PredicateInst> predsToAdd) {
			effectAdd.addAll(predsToAdd);
			return this;
		}
		

		/**A work around for Java's initialisation mechanism
		 * 
		 * @param predsToAdd
		 * @return
		 */
		public ActionBuilder addEffectAdd(PredicateInst[] predsToAdd) {
			effectAdd.addAll(Arrays.asList(predsToAdd));
			return this;
		}

		/**Which ones to remove
		 * 
		 * @param predsToAdd the predicates to remove
		 * @return
		 */		
		public ActionBuilder addEffectRem(Collection<PredicateInst> predsToRem) {
			effectRem.addAll(predsToRem);
			return this;
		}

		public ActionBuilder addEffectRem(PredicateInst[] predsToRem) {
			effectRem.addAll(Arrays.asList(predsToRem));
			return this;
		}
		
		public ActionBuilder addPrecon(LogicalExpr precondition) {
			this.precondition = precondition;
			return this;
		}
		
		public ActionBuilder addSubstitutionPattern(String[] substitution) {
			this.substitution = substitution;
			noOfParameters = substitution.length;
			return this;
		}
		
		public ActionBuilder setParamNumber(int num) {
			noOfParameters = num;
			return this;
		}
		
		public ActionBuilder setChanges(StoryVariables sv) {
			this.sv = sv;
			return this;
		}
		
		private void processParameters() {
			if(substitution == null) return;
			noOfParameters = substitution.length;
						
			//TODO: refactor ObjectInst into String
			Map<ObjectInst, Integer> mapping = new HashMap<>();
			Map<String, Integer> mapping2 = new HashMap<>();
			for(int i = 0; i < substitution.length; i++) {
				mapping.put(ObjectInst.createInstance(substitution[i]), i);
				mapping2.put(substitution[i], i);
			}
			
			for(int i = 0; i < effectAdd.size(); i++)
				effectAdd.set(i, effectAdd.get(i).map(mapping));
			
			for(int i = 0; i < effectRem.size(); i++)
				effectRem.set(i, effectRem.get(i).map(mapping));

			
			if(precondition != null) precondition.map(mapping);
						
			//Build template text
			StringBuilder sb = new StringBuilder();
			int lastPos = 0;
			
			for(int i = 0; i < templateText.length(); i++)
				if(templateText.charAt(i) == '$') {
					int from = i;
					while(i < templateText.length() && templateText.charAt(i) != ' ')
						i++;

					if(from - 1 > lastPos)sb.append(templateText.subSequence(lastPos, from - 1) + " ");
					sb.append("$" + mapping2.get(templateText.substring(from + 1, i)).toString() + " ");
					lastPos = i + 1;
				}
			if(lastPos < templateText.length()) sb.append(templateText.substring(lastPos, templateText.length()));
			templateText = sb.toString();
		}
		
		public Action build() {
			processParameters();
			if(noOfParameters < 0) return null;
			return new Action(this);
		}
	}
}
