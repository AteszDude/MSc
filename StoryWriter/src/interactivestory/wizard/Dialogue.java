package interactivestory.wizard;
import interactivestory.StoryWorld;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

/**
 * @author Attila Torda
 *
 */
@SuppressWarnings("serial")
public abstract class Dialogue extends JFrame implements ActionListener, ListSelectionListener
{
	 //Buttons for next/back
     protected JButton backButton;
     protected JButton okButton;

     //The callback class
     protected DialogueListener listener;

     //All the data
     protected StoryWorld sw;
     
     //Window positions
     private static int windowX, windowY;
     
     public Dialogue(StoryWorld sw, DialogueListener listener)
     {this.listener = listener; this.sw = sw;}
     
     protected void setBasicGUI()
   		{
	   //Create a window with absolute positioning
	    getContentPane().setLayout(null);
		setResizable(false);
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
	    setLocation(windowX, windowY);
	   
	    //Next (OK) and back buttons
	    backButton = new JButton();
		backButton.setLocation(0,317);
		backButton.setSize(100,50);
		backButton.setText("<<Back");
		getContentPane().add(backButton);
		backButton.addActionListener(this);
		
		okButton = new JButton();
		okButton.setLocation(290,317);
		okButton.setSize(100,50);
		okButton.setText("OK>>");
		getContentPane().add(okButton);
		okButton.addActionListener(this);
		
   }

   /**
    * Position the window in the center area of the screen
    */
   public void center() {
	   Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	   this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
   }   
   
	@Override
	public void actionPerformed(ActionEvent e) {
		checkOkBack(e);
	}

   
   private void checkOkBack(ActionEvent event) {
	 	if( event.getSource() == okButton )
	 		next();
	 	else if( event.getSource() == backButton )
	 		back();
}
 
   protected void closeWindow() {
		windowX = getLocation().x;
		windowY = getLocation().y;
	    setVisible(false);
		dispose();
   }
   
   /**Update the StoryWorld to the User's preferences (MVC pattern)
    * 
    */
   protected void updateStoryWorld(){}
   
   protected void next() {
	   closeWindow();
	   updateStoryWorld();
	   listener.next();
	   }
   private void back() {
	   closeWindow();
	   listener.back();
	   }
}
