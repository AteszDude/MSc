/**
 * 
 */
package interactivestory.wizard;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;

import interactivestory.StoryWorld;

/**
 * @author Attila Torda
 *
 */
@SuppressWarnings("serial")
public class FileDialogue extends Dialogue {

    JLabel textLabel;
    JTextArea locationField;

	
	public FileDialogue(StoryWorld sw, DialogueListener listener) {
		super(sw, listener);
	    setupGUI();
	}

	
	void setupGUI() {

		setBasicGUI();
		
		textLabel = new JLabel();
		textLabel.setLocation(43,123);
		textLabel.setSize(78,25);
		textLabel.setText("Location:");
		getContentPane().add(textLabel);

		locationField = new JTextArea();
		locationField.setLocation(133,124);
		locationField.setSize(217,25);
		locationField.setText("src\\ducktales.txt");
		locationField.setRows(5);
		locationField.setColumns(5);
		getContentPane().add(locationField);

		setTitle("Choose File");
		setSize(454,402);
		setVisible(true);

	}
	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
