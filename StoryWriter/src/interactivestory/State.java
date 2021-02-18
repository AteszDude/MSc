package interactivestory;

import interactivestory.utils.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * A State is composed of a list of predicate instances.
 * @author Attila Torda
 *
 */
public class State  {

	Set<PredicateInst> predicateinsts;
	//StoryVariables variables;
	
	//TODO pooling should be improved ore deleted!
	private static Map<String, State> pool = new WeakHashMap<>();
	
	//A variable, holding the asymmetric difference from another State
	private Pair<State, Set<PredicateInst>> diffCache;
	
	private final int hashCode;

	/**Creates a new state, which contains a list of the given predicates.
	 * 
	 * @param preds
	 */
	private State(Collection<PredicateInst> preds) {
		predicateinsts = Collections.unmodifiableSet(new HashSet<PredicateInst>(preds));
		hashCode = predicateinsts.hashCode();
	}
	
	public static State createInstance(Collection<PredicateInst> predinstances) {
		StringBuilder sb = new StringBuilder();
		for(PredicateInst pi : predinstances)
			sb.append(pi + " ");
		String key = sb.toString();
		State instance = pool.get(key);
		if(instance == null)
		{
			instance = new State(predinstances);
			pool.put(key, instance);
		}
		return instance;
	}
	
	public static State createInstance(PredicateInst[] predinstances) {
		return createInstance(new ArrayList<PredicateInst>(Arrays.asList(predinstances)));
	}
	
	public static State createInstance(State current, List<PredicateInst> add, List<PredicateInst> remove)
	{
		Set<PredicateInst> newSet = new HashSet<>(current.getPredicates());
		newSet.removeAll(remove);
		newSet.addAll(add);
					
		return createInstance(newSet);
	}
	
	public Set<PredicateInst> getPredicates() {
		return new HashSet<PredicateInst>(predicateinsts);
		
	}
	
	/**Adds together all the predicate instances in the two states
	 * 
	 * @param state1
	 * @param state2
	 * @return
	 */
	public static State mergeState(State state1, State state2) {
		Set<PredicateInst> allInst = new HashSet<>();
		allInst.addAll(state1.getPredicates());
		allInst.addAll(state2.getPredicates());

		return createInstance(allInst);
	}
	
	
	/**Gives back a list of missing PredInst from the other state.
	 * The diffCache variable caches the last value.
	 * 
	 * @param other
	 * @return the asymmetric set difference of PredicateInst
	 */
	public Set<PredicateInst> substituteFrom(State other) {
		//Check cache
		if(diffCache != null && diffCache.first == other)
			return diffCache.second;

		//Calculate result
		Set<PredicateInst> result = other.getPredicates();
		result.removeAll(getPredicates());
		
		//Write cache
		diffCache = new Pair(other, result);
		
		return result;
	}
	
	/**Gets the number of different statements from the other State.
	 * Uses caching.
	 * 
	 * @param other
	 * @return
	 */
	public int getDistancefrom(State other) {
		return substituteFrom(other).size();
	}
	
	
	
	public boolean contains(PredicateInst predicateinst) {
		return predicateinsts.contains(predicateinst);
	}
	

	public boolean evaluateTrue(Logical logical) {
		if(logical instanceof PredicateInst)
			return contains((PredicateInst) logical);
		else assert(false);
		return false;
	}
	
	
	/**
	 * Checks whether the state contains all the predicates
	 * @param conditions
	 * @return
	 */
	public boolean satisfies(List<PredicateInst> conditions) {
		for(PredicateInst p : conditions) {
			if(!predicateinsts.contains(p))
				return false;
		}
		
		return true;
	}
	
	public boolean satisfies(State other) {
		for(PredicateInst p : other.getPredicates()) {
			if(!predicateinsts.contains(p))
				return false;
		}
		
		return true;

	}
	
	@Override
	public int hashCode() {return hashCode;}
	
	@Override
	public boolean equals (Object other) {
		if(this == other) return true;
		if(!(other instanceof State))
			return false;
		State o = (State)other;

		//Set interface does a good job
		return predicateinsts.equals(o.getPredicates());
	}
	
	@Override
	public String toString() {
		return ("State : " + predicateinsts.toString());
	}


}