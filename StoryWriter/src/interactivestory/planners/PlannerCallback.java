/**
 * 
 */
package interactivestory.planners;

import interactivestory.State;
import interactivestory.StoryVariables;

/**
 * @author Attila Torda
 *
 */
public interface PlannerCallback {

	public void planFinished(boolean succes, String resultText, State result, StoryVariables vars);
}
