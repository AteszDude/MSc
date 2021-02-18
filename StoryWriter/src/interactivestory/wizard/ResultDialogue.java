package interactivestory.wizard;

import interactivestory.StoryWorld;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;

/**The GUI displays the finished story in text format.
 * 
 * @author Attila Torda
 *
 */
@SuppressWarnings("serial")
public class ResultDialogue extends Dialogue {

    JTextArea resultText;
    JButton saveButton;

	
	public ResultDialogue(StoryWorld sw, DialogueListener listener) {
		 super(sw, listener);
	     setupGUI();
	}
	
	private void setupGUI()
	{
		setBasicGUI();
	
		resultText = new JTextArea();
		resultText.setLocation(14,4);
		resultText.setSize(408,306);
		resultText.setText("Output: \n" + sw.getResultText());
		resultText.setRows(5);
		resultText.setColumns(5);
		getContentPane().add(resultText);

		saveButton = new JButton();
		saveButton.setLocation(173,319);
		saveButton.setSize(100,50);
		saveButton.setText("Save");
		getContentPane().add(saveButton);

		setTitle("Results");
		setSize(454,402);
		setVisible(true);
		setResizable(true);
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
