package interactivestory.wizard;

import interactivestory.State;
import interactivestory.StoryVariables;
import interactivestory.StoryWorld;
import interactivestory.planners.ExecutorCallback;
import interactivestory.planners.PlannerExecutor;
import interactivestory.utils.Pair;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;

/**
 * Displays a small window which allows the user to cancel the planning
 */
@SuppressWarnings("serial")
public class PlannerDialogue extends Dialogue implements ExecutorCallback {

	Thread thread;
	PlannerExecutor executor;
	
    JButton startButton;
    JTextField threadInput;
    JLabel labelField;
    JTextArea statusField;

	PlanState state = PlanState.IDLE;
    
    enum PlanState {IDLE, RUNNING, FINISHED};
    
	public PlannerDialogue(StoryWorld sw, DialogueListener listener) {
		super(sw, listener);
		setupGUI();
	}
	
	private void setupGUI()
	{
		setBasicGUI();
		
		startButton = new JButton();
		startButton.setLocation(158,137);
		startButton.setSize(100,50);
		startButton.setText("Start");
		getContentPane().add(startButton);
		startButton.addActionListener(this);

		
		threadInput = new JTextField();
		threadInput.setLocation(158,61);
		threadInput.setSize(100,20);
		threadInput.setText("4");
		threadInput.setColumns(10);
		getContentPane().add(threadInput);

		labelField = new JLabel();
		labelField.setLocation(30,61);
		labelField.setSize(100,20);
		labelField.setText("No. of threads:");
		getContentPane().add(labelField);

		statusField = new JTextArea();
		statusField.setLocation(94,225);
		statusField.setSize(215,52);
		statusField.setText("Status: Idle");
		statusField.setRows(5);
		statusField.setColumns(5);
		getContentPane().add(statusField);
		
		
		setTitle("Planning");
	 	setSize(400,400);
		setVisible(true);
	}
	
	private void initPlanner() {
		List<Pair<interactivestory.State, StoryVariables>> states = new ArrayList<>();
		states.add(new Pair<State, StoryVariables>(sw.getStart(), new StoryVariables()));
		states.add(new Pair<State, StoryVariables>(sw.getFinish(), new StoryVariables()));
		
		//Try to convert the number of threads into an integer
		int threadNo = 1;
		try{threadNo = Integer.parseInt(threadInput.getText());}
		catch(Exception e) {threadNo = 1;}
		
		executor = new PlannerExecutor(this, threadNo, states, sw.getActions(), sw.getCharacters(), sw.getPreds());

	}
	
	private void start() {
		statusField.setText("Status: Planning!");
		thread = new Thread(executor);
		thread.start();
		state = PlanState.RUNNING;
	}
	
	private void stop() {

	}
	
	
	private void startPressed() {
		initPlanner();
		start();
	}
	
	public void planFinished(boolean succes, String resultText) {
		sw.setResultText(resultText);
		state = PlanState.FINISHED;
		statusField.setText("Status: Finished!");
		
	}

	
	@Override
	public void actionPerformed(ActionEvent event) {
		super.actionPerformed(event);
		
		if(event.getSource() == startButton) {
			startPressed();
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
