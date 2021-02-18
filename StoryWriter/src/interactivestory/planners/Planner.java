/**
 * 
 */
package interactivestory.planners;

import interactivestory.Action;
import interactivestory.ObjectInst;
import interactivestory.Predicate;
import interactivestory.State;
import interactivestory.StoryVariables;

import java.util.Collection;

/**
 * @author Attila Torda
 *
 */
public interface Planner extends Runnable {
	public void initPlanner(PlannerCallback callback, Collection<Action> actions, Collection<ObjectInst> objects,
			Collection<Predicate> predicates, State start, State finish, StoryVariables target);
	public String getResult();
}
