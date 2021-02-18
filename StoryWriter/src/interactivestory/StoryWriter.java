package interactivestory;
import interactivestory.loader.Parser;
import interactivestory.wizard.ComponentDialogue;
import interactivestory.wizard.ArcDialogue;
import interactivestory.wizard.Dialogue;
import interactivestory.wizard.DialogueListener;
import interactivestory.wizard.FileDialogue;
import interactivestory.wizard.MilestoneDialogue;
import interactivestory.wizard.PlannerDialogue;
import interactivestory.wizard.ResultDialogue;

/**
 * The main workflow.
 */

class StoryWizard extends Workflow implements DialogueListener{
	
	StoryWorld world;
	Dialogue act;
	int step;
	
	public StoryWizard() {
		//Create and set up the window.
		world = new Parser("src\\ducktales.txt").getResult();
		step = 1;
		showDialogue();
		act.center();
	}

	
	private void showDialogue() {
		switch(step) {
		case 1:
			act = new FileDialogue(world, this);
			break;
		case 2:
			act = new ComponentDialogue(world, this);
			break;
		case 3:
			act = new ArcDialogue(world, this);
			break;
		case 4:
			act = new MilestoneDialogue(world, this);
			break;
		case 5:
			act = new PlannerDialogue(world, this);
			break;
		case 6:
			act = new ResultDialogue(world, this);
			break;
		case 7:
			break;
		default:
			assert(false);
			break;
		}
	}
	
	@Override
	public void next() {
		step++;
		showDialogue();
	}

	@Override
	public void back() {
		step--;
		showDialogue();
	}
	
}


abstract class Workflow {}


public class StoryWriter {

	public static void main(String[] args) {
		new StoryWizard();
	}

}
