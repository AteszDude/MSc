package interactivestory.wizard;

import interactivestory.StoryWorld;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

@SuppressWarnings("serial")
public class MilestoneDialogue extends Dialogue {
	
    JList<String> partList;
    JList<String> statementList;
    JTable varTable;

	public MilestoneDialogue(StoryWorld sw, DialogueListener listener) {
		super(sw, listener);

		setupGUI();


	}

	   private void setupGUI()
	   {
		setBasicGUI();
		
		partList = new JList<>(new String[] {"Introduction", "Middle", "End"});
		partList.setLocation(1,2);
		partList.setSize(158,284);
		partList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getContentPane().add(partList);

		statementList = new JList<>(new String[] {"HAS(Glomgold, Cake)"});
		statementList.setLocation(176,2);
		statementList.setSize(158,284);
		statementList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getContentPane().add(statementList);

		String[] headers = new String[] {"Name", "Value"};
		Object[][] testData = new Object[][] { {"Time", 10}, {"Action", 0}, {"Action", 0}, {"Drama", 0}, {"Comedy", 0}, {"Romance", 0},
		{"Outcome", 0}, {"Achieve", 0}};
		
		varTable = new JTable(testData, headers);
		varTable.setLocation(352,2);
		varTable.setSize(160,128);
		varTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getContentPane().add(varTable);

		
		setTitle("Check Milestones");
		setSize(520, 402);
		setVisible(true);
	   }
	   
	   
	@Override
	public void valueChanged(ListSelectionEvent e) {

	}

}
