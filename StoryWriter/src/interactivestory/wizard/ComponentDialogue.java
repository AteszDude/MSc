package interactivestory.wizard;

//Code Genarated by JGuiD
import interactivestory.Action;
import interactivestory.ObjectInst;
import interactivestory.StoryPart;
import interactivestory.StoryWorld;
import interactivestory.utils.CheckBoxList;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;

/**The User selects story parts and actions from a list.
 * 
 * @author Attila Torda
 *
 */
@SuppressWarnings("serial")
public class ComponentDialogue extends Dialogue {

		//Story Parts
		List<StoryPart> parts;
	    JList<JCheckBox> storyList;
	    JCheckBox[] storyModel;
	    
		//Actions
		List<Action> actions;
		JList<JCheckBox> actionList;
	    JCheckBox[] actionModel;
	    
	    //Characters
		List<ObjectInst> characters;
		JList<JCheckBox> characterList;
	    JCheckBox[] characterModel;

	     
	   public ComponentDialogue(StoryWorld sw, DialogueListener listener)
	   {
		 super(sw, listener);
	     setupGUI();
	     
	     parts = sw.getStoryParts();
	 	 JCheckBox[] checkBoxes = new JCheckBox[parts.size()];
	 	 for(int i = 0; i < parts.size(); i++)
	 		  checkBoxes[i] = new JCheckBox(parts.get(i).title, true);
	 	 storyModel = checkBoxes;
	 	 storyList.setListData(storyModel);

	 	 
	     actions = new ArrayList<>(sw.getActions());
	 	 JCheckBox[] checkBoxes2 = new JCheckBox[actions.size()];
		 for(int i = 0; i < actions.size(); i++)
		 		checkBoxes2[i] = new JCheckBox(actions.get(i).getName(), true);
		 actionModel = checkBoxes2;
		 actionList.setListData(actionModel);
		 
		 
		 characters = new ArrayList<>(sw.getCharacters());
	 	 JCheckBox[] checkBoxes3 = new JCheckBox[characters.size()];
		 for(int i = 0; i < characters.size(); i++)
			 checkBoxes3[i] = new JCheckBox(characters.get(i).name, true);
		 characterModel = checkBoxes3;
		 characterList.setListData(characterModel);		 
	   }
	   
	   
	   private void setupGUI()
	   {
		setBasicGUI();

		storyList = new CheckBoxList();
		storyList.setLocation(1,2);
		storyList.setSize(158,284);
		getContentPane().add(storyList);

		actionList = new CheckBoxList();
		actionList.setLocation(176,2);
		actionList.setSize(158,284);
		getContentPane().add(actionList);
		
		characterList = new CheckBoxList();
		characterList.setLocation(352,2);
		characterList.setSize(158,284);
		getContentPane().add(characterList);
		
		setTitle("Set Components");
		setSize(520, 402);
		setVisible(true);		
		
	   }

	@Override
	protected void updateStoryWorld(){	
		
		List<StoryPart> selParts = new ArrayList<>();
		for(int i = 0; i < storyModel.length; i++)
			if(storyModel[i].isSelected())
				selParts.add(parts.get(i));

		List<Action> selActions = new ArrayList<>();
		for(int i = 0; i < actionModel.length; i++)
			if(actionModel[i].isSelected())
				selActions.add(actions.get(i));

		List<ObjectInst> selChars = new ArrayList<>();
		for(int i = 0; i < characterModel.length; i++)
			if(characterModel[i].isSelected())
				selChars.add(characters.get(i));
		
		sw.selectActions(selActions);
		sw.selectParts(selParts);
		sw.selectCharacters(selChars);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		super.actionPerformed(e);
		
	}
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
