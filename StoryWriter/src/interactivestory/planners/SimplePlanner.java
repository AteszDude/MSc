package interactivestory.planners;

import interactivestory.Action;
import interactivestory.LogicalExpr;
import interactivestory.ObjectInst;
import interactivestory.Operator;
import interactivestory.Predicate;
import interactivestory.PredicateInst;
import interactivestory.State;
import interactivestory.StoryVariables;
import interactivestory.utils.Triple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;


/**A planner implementation, a very simple one that supports multi threading.
 * 
 */
public class SimplePlanner implements Planner {

	//The world
	List<Action> actions;
	List<ObjectInst> objects;
	List<Predicate> predicates;
	
	//The start and finish
	List<Triple<interactivestory.State, String, StoryVariables>> searchList;
	interactivestory.State start;
	interactivestory.State current;
	interactivestory.State finish;
	StoryVariables targetV;
	
	//Potential next state to choose from
	List<Triple<interactivestory.State, String, StoryVariables>> nextStates;
	
	//The current number of runs
	int currentRun = 0;
	
	//Callback
	PlannerCallback callback;
	
	//Constants for planner
	public static final int MAXRUN = 10000;
	
	//Random number generator
	Random rand = new Random();
	
	public SimplePlanner() {
		actions = new ArrayList<>();
		objects = new ArrayList<>();
		predicates = new ArrayList<>();
		searchList = new ArrayList<>();
		nextStates = new ArrayList<>();
		
	}

	public void initPlanner(PlannerCallback callback, Collection<Action> actions,
			Collection<ObjectInst> objects, Collection<Predicate> predicates,
			interactivestory.State start, interactivestory.State finish, StoryVariables target) {
		clear();
		
		this.actions.addAll(actions);
		this.objects.addAll(objects);
		this.predicates.addAll(predicates);
		this.callback = callback;
		this.start = start;
		this.finish = finish;
		this.targetV = new StoryVariables(target);
	}

	
	private void clear() {
		actions.clear();
		objects.clear();
		predicates.clear();
		searchList.clear();
		nextStates.clear();
	}
	
	public void setGoals(interactivestory.State start, interactivestory.State finish) {
		this.start = start;
		this.finish = finish;
	}
		
	public interactivestory.State getCurrentState() {
		return 	searchList.get(searchList.size() - 1).first;
	}
	
	@Override
	public void run() {
		searchList.add(new Triple<interactivestory.State, String, StoryVariables>(start, "", new StoryVariables()));
		
		while(true)
			if(execute()) break;
		
		callback.planFinished(getCurrentState().satisfies(finish), getResult(), getCurrentState(), searchList.get(searchList.size() - 1).third);
	}
	
	private boolean execute() {
		currentRun++;
		if(currentRun > MAXRUN) {System.out.println("Max running reached!"); return true;}
		//Get the last state
		Triple<interactivestory.State, String, StoryVariables> currentData = searchList.get(searchList.size() - 1);
		current = currentData.first;

		//Print current state
		//System.out.println(searchList.size() + " " + current);
		
		//Check whether the current the state is the final state
		if(current.satisfies(finish)) {System.out.println("Condition reached!"); return true;}
				
		//Shuffle actions for randomness
		Collections.shuffle(actions);
		
		//Itearate through all the possible actions with all the objects
		for(Action action : actions) {
			
			//Get the remaining statements
			Set<PredicateInst> remaining = current.substituteFrom(finish);
			ObjectInst[] subResult = attemptSubstitute(action, remaining);
			
			//The final substitution formula
			ObjectInst[] substitution = null;
			
			//Count the nulls in the substitution
			int nulls = 0;
			for(int i = 0; i < subResult.length; i++)
				if(subResult[i] == null) nulls++;
			
			if(nulls == 0)	
				substitution = subResult;
			
			//Attempt to create a valid action
			else {
				List<ObjectInst> availableObj = new ArrayList<ObjectInst>(objects);
				availableObj.removeAll(Arrays.asList(subResult));

				for(int i = 0, max = nulls * 5; i < max; i++) {
					ObjectInst[] subResult2 = subResult;

					//Traverse the array and substitute a random object to each null
					for(int j = 0; j < subResult.length; j++)
						if(subResult2[j] == null)
							subResult2[j] = availableObj.get(rand.nextInt(availableObj.size()));
					if(action.createCondition(subResult2).isTrue(current)) {
						substitution = subResult2;
						break;
					}
				}
			}
			
			//TODO clear up randomization
			final int length = action.getNumPar();

			//Rendomize the objects that are to be substituted
			Collections.shuffle(objects);

			//Get a new random subtitution pattern
			if(substitution == null) substitution = objects.subList(0, length).toArray(new ObjectInst[length]);
			//System.out.println("!!Substitution pattern: " + Arrays.toString(substitution));
			
			//If the precondition is satisfied
			if(action.createCondition(substitution).isTrue(current))
			{
				//Insert a new State into the search list, with the current updates
				Triple<String, List<PredicateInst>, List<PredicateInst>> result = action.getEffects(substitution);
				interactivestory.State newState = interactivestory.State.createInstance(current, result.second, result.third);
				StoryVariables newVars = new StoryVariables(currentData.third);
				newVars.incrementBy(action.getChanges());
				nextStates.add(new Triple<interactivestory.State, String, StoryVariables>(newState, result.first, newVars));
			}

		}
		selectNextState();
		nextStates.clear();
		return false;
	}

	/**The current planner thread selects the next state from the nextStates list and inserts it into the searchList.
	 * It might make a step back, in which case it deletes the last state.
	 * 
	 */
	private void selectNextState() {
		Triple<interactivestory.State, String, StoryVariables> selectedState = null;
		int minDistance = current.getDistancefrom(finish);
		
		for(Triple<interactivestory.State, String, StoryVariables> t : nextStates) {
			interactivestory.State state = t.first;
			if(state.getDistancefrom(finish) < minDistance) {
				selectedState = t;
				minDistance = state.getDistancefrom(finish);
			}
		}

		if(selectedState != null)
			searchList.add(selectedState);
		else if(!nextStates.isEmpty()) {
			if(searchList.size() > 1 && rand.nextInt(4) == 0)
				searchList.remove(searchList.size() - 1);
			else
				searchList.add(nextStates.get(rand.nextInt(nextStates.size())));
		}
		else if(searchList.size() > 1)
			searchList.remove(searchList.size() - 1);
	}
	
	/**Attempts to substitute objects into an action, in a way that the final state is satisfied.
	 * 
	 * @return the substituted array, could contain null values
	 */
	private ObjectInst[] attemptSubstitute(Action act, Set<PredicateInst> target) {

		//The result substitution. Could contain null values
		ObjectInst[] result = new ObjectInst[act.getNumPar()];
		
		//Make a predicate -> objectinst hashmap from the set
		Map<Predicate, List<ObjectInst>> targets = new HashMap<>();
		for(PredicateInst pi : target) {
			targets.put(pi.predicate, pi.instances);
		}
				
		//The PredicateInst added by the Action
		List<PredicateInst> effect = act.getEffectAdd();
		
		//Go through each predicate instance
		for(PredicateInst pi : effect) {
			

			//Skip if not needed in the targets
			if(!targets.containsKey(pi.predicate)) continue;
			
			//See if the substitution is possible
			boolean hasPlace = true;
			List<ObjectInst> subNumbers = pi.instances;
			for(ObjectInst number : subNumbers) {
				int num = Integer.parseInt(number.toString());
				//If something is already substituted in that place
				if(result[num] != null) hasPlace = false;
			}

			if(hasPlace == false) continue;

			List<ObjectInst> currentTarget = targets.get(pi.predicate);

			//Substitute in a way that our goal is achieved
			int i = 0;
			for(ObjectInst number : subNumbers) {
				int num = Integer.parseInt(number.toString());
				result[num] = currentTarget.get(i);
				i++;
			}

		}

		return result;
	}


	@Override
	public String getResult() {
		StringBuilder sb = new StringBuilder();
		for(Triple<interactivestory.State, String, StoryVariables> t : searchList)
			sb.append(t.second + "\n");
		
		return sb.toString();
	}

}
