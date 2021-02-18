/**
 * 
 */
package interactivestory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**A collection of all the data available in the Story.
 * @author Attila Torda
 *
 */
public class StoryWorld {
	//A collection of all the AVAILABLE objects
	private Set<ObjectInst> allCharacters = new HashSet<>();
	private Set<Action> allActions = new HashSet<>();
	private Set<Predicate> allPreds = new HashSet<>();
	
	private State start;
	private State finish;
	private List<State> allStates  = new ArrayList<>();
	private List<StoryPart> storyparts = new ArrayList<>();
	
	//Objects selected by the user
	private Set<ObjectInst> selCharacters;
	private Set<Action> selActions;
	private List<StoryPart> selParts;

	
	//The first generated world, which groups the selected objects into lists, such as heroes
	private Set<ObjectInst> heroes = new HashSet<>();
	private Set<ObjectInst> villains = new HashSet<>();
	private Set<ObjectInst> places = new HashSet<>();
	private Set<ObjectInst> treasures = new HashSet<>();
	
	//Other generated properties
	private ObjectInst mainHero;
	private ObjectInst mainVillain;
	private String arc = "";
	
	
	//The final story
	private String result = "";
	
	public StoryWorld() {		
	}

	public StoryWorld(Collection<ObjectInst> allCharacters, Collection<Predicate> allPreds, Collection<Action> allActions) {
	this.allActions.addAll(allActions);
	this.allCharacters.addAll(allCharacters);
	this.allPreds.addAll(allPreds);

	}
	
	public Set<ObjectInst> getCharacters() {return Collections.unmodifiableSet(allCharacters);}
	public Set<Action> getActions() {return Collections.unmodifiableSet(allActions);}
	public Set<Predicate> getPreds() {return Collections.unmodifiableSet(allPreds);}
	public List<State> getStates() {return Collections.unmodifiableList(allStates);}
	public State getStart() {return start;}
	public State getFinish() {return finish;}
	public void setStart(State start) {this.start = start;}
	public void setFinish(State finish) {this.finish = finish;}

	public void selectCharacters(Collection<ObjectInst> characters) {selCharacters = new HashSet<>(characters);}
	public void selectActions(Collection<Action> actions) {selActions = new HashSet<>(actions);}
	public void selectParts(List<StoryPart> parts) {selParts = parts;}
	
	
	public Set<ObjectInst> getSelCharacters() {return Collections.unmodifiableSet(selCharacters);}
	public Set<Action> getSelActions() {return Collections.unmodifiableSet(selActions);}
	public List<StoryPart> getStoryParts() {
		if(storyparts.size() == 0) createParts();
		return Collections.unmodifiableList(storyparts);}
	
	public void setResultText(String result) {this.result = result;}
	public String getResultText() {return result;}
	
	public Set<ObjectInst> getHeroes() {return Collections.unmodifiableSet(heroes);}
	public Set<ObjectInst> getVillains() {return Collections.unmodifiableSet(villains);}
	public Set<ObjectInst> getPlaces() {return Collections.unmodifiableSet(places);}
	public Set<ObjectInst> getTreasures() {return Collections.unmodifiableSet(treasures);}
	
	public void generateBasics() {

		//Generate heroes, villains, treasures and locations lists, based on the statements
		Set<PredicateInst> allpreds = new HashSet<>(start.getPredicates());
		
		for(PredicateInst statement : allpreds) {
			//Assume only one object is assigned
			ObjectInst actObj = statement.instances.get(0);
			if(!selCharacters.contains(actObj)) return;
			
			switch(statement.predicate.name.toLowerCase()) {
			case "hero":
				heroes.add(actObj);
				break;
			case "treasure":
				treasures.add(actObj);
				break;
			case "place":
				places.add(actObj);
				break;
			case "villain":
				villains.add(actObj);
				break;
			default:
				break;
			}
		}
		
		System.out.println(heroes);
		System.out.println(villains);
		System.out.println(treasures);
		
		
	}
	
	private void createParts() {
		storyparts.add(new StoryPart("Introduction", 10, null, true));
		storyparts.add(new StoryPart("Middle", 10, null, true));
		storyparts.add(new StoryPart("End", 10, null, true));
	}
	
	public void addAll(StoryWorld other) {
		allCharacters.addAll(other.getCharacters());
		allActions.addAll(other.getActions());
		allPreds.addAll(other.getPreds());
		allStates.addAll(other.getStates());
		
		
	}

	
	public void clearAll() {
		allCharacters.clear();
		allActions.clear();
		allPreds.clear();
		start = null;
		finish = null;
		allStates.clear();
		storyparts.clear();
		selActions.clear();
		selParts.clear();
		heroes.clear();
		villains.clear();
		places.clear();
		treasures.clear();
		mainHero = null;
		mainVillain = null;
		arc = "";
		result = "";
	}
}
