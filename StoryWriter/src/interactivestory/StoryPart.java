package interactivestory;

/**
 * A part of the story, eg. "All is Lost" from the book "The Anatomy of Story"
 */
public class StoryPart {
	public final String title;
	public final int length;
	public final PredicateInst[] goals;
	public final boolean isPositive;
	
	
	public StoryPart(String title, int length, PredicateInst[] goals, boolean isPositive) {
		this.title = title;
		this.length = length;
		this.isPositive = isPositive;
		this.goals = goals;
		
	}
	
	@Override
	public String toString() {
		return title + ": " + length;
	}
}


/**The characters are introduced, but nothing special happens here
 * 
 * @author Attila Torda
 *
 */
class Introduction extends StoryPart {
	
	public Introduction(String title, int length, PredicateInst[] goals, boolean isPositive) {super( title,  length, goals,  isPositive);}
	
	public State generateState() {
		StoryVariables sv = new StoryVariables();
		sv.time = length;
		return null;
	}
}

class Desire extends StoryPart {
	
	public Desire(String title, int length, PredicateInst[] goals, boolean isPositive) {super( title,  length, goals,  isPositive);}
	
	public State generateState() {
		StoryVariables sv = new StoryVariables();
		sv.time = length;
		return null;
	}
}

class Plan extends StoryPart {
	
	public Plan(String title, int length, PredicateInst[] goals, boolean isPositive) {super( title,  length, goals,  isPositive);}
	
	public State generateState() {
		StoryVariables sv = new StoryVariables();
		sv.time = length;
		return null;
	}
}

class Opponent extends StoryPart {
	
	public Opponent(String title, int length, PredicateInst[] goals, boolean isPositive) {super( title,  length, goals,  isPositive);}
	
	public State generateState() {
		StoryVariables sv = new StoryVariables();
		sv.time = length;
		return null;
	}
}

class Battle extends StoryPart {
	
	public Battle(String title, int length, PredicateInst[] goals, boolean isPositive) {super( title,  length, goals,  isPositive);}
	
	public State generateState() {
		StoryVariables sv = new StoryVariables();
		sv.time = length;
		return null;
	}
}


class NewEquilibrium extends StoryPart {
	
	public NewEquilibrium(String title, int length, PredicateInst[] goals, boolean isPositive) {super( title,  length, goals,  isPositive);}
	
	public State generateState() {
		StoryVariables sv = new StoryVariables();
		sv.time = length;
		return null;
	}
}


