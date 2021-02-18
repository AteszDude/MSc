package interactivestory;

/**
 * 
 * @author Attila Torda
 *
 */
public class StoryVariables {
	/**How much time elapsed
	 * 
	 */
	public int time;
	
	/**How much action happened
	 * 
	 */
	public int action;
	
	/**How much drama happened
	 * 
	 */
	public int drama;
	
	/**How much comedy happened
	 * 
	 */
	public int comedy;
	
	/**How much romance happened
	 * 
	 */
	public int romance;
	
	/**Positive, if good things happened, or negative when bad things happened!
	 * 
	 */
	public int outcome;
	
	/**The characters got closer to their goals!
	 * 
	 */
	public int achieve;
	
	public StoryVariables() {super();}
	
	/**Copy constructor
	 * 
	 * @param other
	 */
	public StoryVariables(StoryVariables other) {
		if(other == null) return;
		this.time = other.time;
		this.action = other.action;
		this.drama = other.drama;
		this.comedy = other.comedy;
		this.romance = other.romance;
		this.outcome = other.outcome;
		this.achieve = other.achieve;
	}
	
	public void incrementBy (StoryVariables other) {
		time += other.time;
		action += other.action;
		drama += other.drama;
		comedy += other.comedy;
		romance += other.romance;
		outcome += other.outcome;
		achieve += other.achieve;
	}
	
	@Override
	public String toString() {
		return "Vars ( " + (time == 0 ? "" : "Time " + time + " ") + (action == 0 ? "" : "Action " + action + " ") +
				(drama == 0 ? "" : "Drama " + drama + " ") + (comedy == 0 ? "" : "Comedy " + comedy + " ") +
				(romance == 0 ? "" : "Romance " + romance + " ") + (outcome == 0 ? "" : "Outcome " + outcome + " ") +
				(achieve == 0 ? "" : "Acieve " + achieve + " ") + ")";
	}
	
	@Override
	public boolean equals(Object other) {
		if(this == other) return true;
		if(!(other instanceof StoryVariables))
			return false;
		StoryVariables o = (StoryVariables)other;

		return o.action == action && o.achieve == achieve && o.comedy == comedy && o.drama == drama && o.outcome == outcome && o.romance == romance &&
				o.time == time;
	}
	
	@Override
	public int hashCode() {
		//Some random mumbo jumbo
        int hash = 31;
        hash = hash * 17 + time;
        hash = hash * 7 + action;
        hash = hash * 13 + drama;

        hash = hash * 17 + comedy;
        hash = hash * 7 + romance;
        hash = hash * 13 + outcome;
        
        hash += achieve;
        return hash;
	}
	
	public double getDistance(StoryVariables other) {
		return Math.sqrt(Math.pow(1.0 - (float) other.time / (float) (time == 0 ? 0.1 : time), 2));
	}
	
//	public double getDistance(StoryVariables other) {
//		return Math.sqrt(Math.pow(1.0f - (float) other.time / (float) time, 2) + Math.pow(1.0f - (float) other.action / (float) action, 2) + 
//				Math.pow(1.0f - (float) other.drama / (float) drama, 2) + Math.pow(1.0f - (float) other.comedy / (float) comedy, 2) + 
//				Math.pow(1.0f - (float) other.romance / (float) romance, 2) + Math.pow(1.0f - (float) other.outcome / (float) outcome, 2) +
//				Math.pow(1.0f - (float) other.achieve / (float) achieve, 2));
//		
//	}
}
