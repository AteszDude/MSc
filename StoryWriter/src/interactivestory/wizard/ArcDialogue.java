/**
 * 
 */
package interactivestory.wizard;

import javax.swing.event.ListSelectionEvent;

import interactivestory.ObjectInst;
import interactivestory.StoryWorld;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

/**
 * @author Attila Torda
 *
 */
@SuppressWarnings("serial")
public class ArcDialogue extends Dialogue {
	
	//GUI elements
    JLabel heroLabel;
    Choice heroChoice;
    JLabel enemyLabel;
    Choice enemyChoice;
    JLabel arcLabel;
    Choice arcChoice;
    Choice treasureChoice;
    JLabel bstoryLabel;
    Choice bstoryChoice;
    JLabel timeLabel;
    JTextField timeField;
    
    //StoryWorld elements
    java.util.List<ObjectInst> heroes = new ArrayList<>();
    java.util.List<ObjectInst> villains = new ArrayList<>();
    java.util.List<ObjectInst> treasures = new ArrayList<>();
    
  void setupGUI()
  {
	setBasicGUI();

	heroLabel = new JLabel();
	heroLabel.setLocation(25,14);
	heroLabel.setSize(50, 25);
	heroLabel.setText("Hero");
	getContentPane().add(heroLabel);
	
	heroChoice = new Choice();
	heroChoice.setLocation(117,13);
	heroChoice.setSize(100,50);
	heroChoice.add("None");
	for(ObjectInst obj : heroes)
		heroChoice.add(obj.name);
	
	getContentPane().add(heroChoice);

	enemyLabel = new JLabel();
	enemyLabel.setLocation(25,68);
	enemyLabel.setSize(50, 25);
	enemyLabel.setText("Enemy");
	getContentPane().add(enemyLabel);

	enemyChoice = new Choice();
	enemyChoice.setLocation(117,66);
	enemyChoice.setSize(100,50);
	enemyChoice.add("None");
	for(ObjectInst obj : villains)
		enemyChoice.add(obj.name);
	
	getContentPane().add(enemyChoice);

	arcLabel = new JLabel();
	arcLabel.setLocation(25,180);
	arcLabel.setSize(50, 25);
	arcLabel.setText("Arc");
	getContentPane().add(arcLabel);

	arcChoice = new Choice();
	arcChoice.setLocation(121,181);
	arcChoice.setSize(100,50);
	arcChoice.add("None");
	arcChoice.add("Obtain");
	arcChoice.add("Lose");

	getContentPane().add(arcChoice);

	treasureChoice = new Choice();
	treasureChoice.setLocation(231,181);
	treasureChoice.setSize(100,50);
	treasureChoice.add("None");
	for(ObjectInst obj : treasures)
		treasureChoice.add(obj.name);
	
	getContentPane().add(treasureChoice);

	bstoryLabel = new JLabel();
	bstoryLabel.setLocation(24,245);
	bstoryLabel.setSize(50, 25);
	bstoryLabel.setText("B Story");
	getContentPane().add(bstoryLabel);

	bstoryChoice = new Choice();
	bstoryChoice.setLocation(119,244);
	bstoryChoice.setSize(100,50);
	bstoryChoice.add("None");
	getContentPane().add(bstoryChoice);

	timeLabel = new JLabel();
	timeLabel.setLocation(25,123);
	timeLabel.setSize(50, 25);
	timeLabel.setText("Time");
	getContentPane().add(timeLabel);

	timeField = new JTextField();
	timeField.setLocation(119,123);
	timeField.setSize(50,25);
	timeField.setText("40");
	getContentPane().add(timeField);
	
	setTitle("Set Arc");
	setSize(454,402);
	setVisible(true);
  	}
  
  
	public ArcDialogue(StoryWorld sw, DialogueListener listener) {
		 super(sw, listener);
		 
		 //Commit the previous dialogue's updated in the Story World
		 sw.generateBasics();
		 
		 heroes.addAll(sw.getHeroes());
		 villains.addAll(sw.getVillains());
		 treasures.addAll(sw.getTreasures());
		 
	     setupGUI();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		
	}

}
