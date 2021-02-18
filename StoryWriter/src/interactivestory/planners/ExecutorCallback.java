package interactivestory.planners;

import interactivestory.State;


public interface ExecutorCallback {
	public void planFinished(boolean succes, String resultText);

}
