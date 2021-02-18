/**
 * 
 */
package interactivestory.planners;

import java.util.Collection;
import java.util.List;

import interactivestory.Action;
import interactivestory.ObjectInst;
import interactivestory.Predicate;
import interactivestory.State;
import interactivestory.StoryVariables;
import interactivestory.utils.Pair;
import interactivestory.utils.Triple;

/**
 * @author Attila Torda
 *
 */
public class PlannerExecutor extends Thread implements PlannerCallback {

	/**Called when it has the results
	 * 
	 */
	ExecutorCallback callback;
	
	/**The number of planner threads
	 * 
	 */
	public final int plannerNum;
	Planner[] planners;
	List<Pair<interactivestory.State, StoryVariables>> states;
	
	Collection<Action> actions;
	Collection<ObjectInst> objects;
	Collection<Predicate> predicates;
		
	StringBuilder resultText = new StringBuilder();
	
	Triple<interactivestory.State, String, StoryVariables>[]  planResults;
	int finishedPlanners;
	
	/**The actual, starting state
	 * 
	 */
	int actState = 0;

	/**
	 * 
	 * @param plannerNum number of planner threads
	 * @param states the states to achieve
	 */
	public PlannerExecutor(ExecutorCallback callback, int plannerNum, List<Pair<interactivestory.State, StoryVariables>> states, Collection<Action> actions,
			Collection<ObjectInst> objects, Collection<Predicate> predicates) {
		this.callback = callback;
		this.plannerNum = plannerNum;
		this.actions = actions;
		this.objects = objects;
		this.predicates = predicates;
		this.states = states;
		
		//Init planners
		planners = new Planner[plannerNum];
		
		for(int i = 0; i < plannerNum; i++) {
			planners[i] = new SimplePlanner();
		}
		
		planNextState();
	}
	
	/**Start the planners OR return to the callback if finished
	 * 
	 */
	private void planNextState() {
		if(actState == states.size() - 1) {
			finished();
			return;
		}
			
		//Set null the previous finished states
		planResults = new Triple[plannerNum];//Can't make it typed!
		
		//Go through all the planners and set the states
		finishedPlanners = 0;
		
		for(int i = 0; i < plannerNum; i++) {
			planners[i] = new SimplePlanner();
		}
		
		for(Planner p : planners)	{
			p.initPlanner(this, actions, objects, predicates, states.get(actState).first,
					states.get(actState + 1).first, states.get(actState + 1).second);
			Thread thread = new Thread(p);
			thread.start();
		}
		
	}
	
	/**Get the best result from the planners
	 * 
	 */
	private void collectResults() {
		//Go through all state results and pick the best match
		Triple<interactivestory.State, String, StoryVariables> bestResult = null;
		StoryVariables targetVariables = states.get(actState + 1).second;
		
		for(Triple<interactivestory.State, String, StoryVariables> result : planResults) {
			if(result == null) continue;
			if(bestResult == null)
				bestResult = result;
			else if(targetVariables.getDistance(result.third) < targetVariables.getDistance(bestResult.third))
				bestResult = result;
			System.out.println(result.third + " - " + targetVariables.getDistance(result.third));
		}
		
		if(bestResult == null) finished();
		
		//Replace the required state with the found state
		states.set(actState + 1, new Pair<interactivestory.State, StoryVariables>(bestResult.first, bestResult.third));
		resultText.append(bestResult.second);
		
		actState++;
		//Carry on planning the next state
		planNextState();
	}

	@Override
	public synchronized void planFinished(boolean success, String resultText, interactivestory.State result, StoryVariables vars) {
		if(success) planResults[finishedPlanners] = new Triple<interactivestory.State, String, StoryVariables>(result, resultText, vars);
		finishedPlanners++;
		if(finishedPlanners == plannerNum) collectResults();
	}
	
	/**Give back the result to the calling class
	 * 
	 */
	private void finished() {
		resultText.append(states.get(states.size() - 1).second);
		callback.planFinished(true, resultText.toString());
	}
}
