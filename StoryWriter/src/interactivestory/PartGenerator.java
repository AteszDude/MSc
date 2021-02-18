package interactivestory;

import java.util.Arrays;
import java.util.List;

/**This class comes up with a random draft of Story Parts.
 * 
 */
public class PartGenerator {

	public static List<String> getAllParts() {
		return Arrays.asList(new String[] {"Introduction, Desire, Plan, Opponent, Battle, NewEquilibrium"});
	}
	
	StoryWorld sw;
	
	public PartGenerator(StoryWorld sw) {
		this.sw = sw;
	}
	
	
	static StoryPart[] generateParts (String[] parts) {
		StoryPart[] result = new StoryPart[parts.length];
		for(int i = 0; i < parts.length; i++) {
			result[i] = new StoryPart(parts[i], 10, new PredicateInst[] {}, i % 2 == 0 ? true : false);//TEST
		}
		return result;
	}
}

