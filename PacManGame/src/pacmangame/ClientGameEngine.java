/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Roland
 */
public class ClientGameEngine extends Thread
{
    private DisplayEngine engine;
    private ObjectOutputStream out;
    private Wall wall;
    private long lastTime = System.currentTimeMillis();
    private boolean gameWon = false;
    
    public int speedtimer;
    public int time;
    public List<Avatar> avatarList;
    public boolean waitForServer = true;
    
    public void setGameWon(boolean value)
    {
        gameWon = value;
    }
    
    public boolean getGameWon()
    {
        return gameWon;
    }
    
    public void setWall(Wall wall)
    {
        this.wall = wall;
    }
    
    public Wall getWall()
    {
        return wall;
    }
    
    public ClientGameEngine(ObjectOutputStream out)
    {
        this.out = out;
        wall = new Wall();
        engine = new DisplayEngine(this);
        avatarList = new ArrayList<Avatar>();
        
        speedtimer = 20;
        time = 0;
        
        avatarList.add(new Avatar(0, 0, Avatar.right, Color.ORANGE, "P1"));
        avatarList.add(new Avatar(19 * engine.cellsize, 0,Avatar.down, Color.cyan, "P2"));
        avatarList.add(new Avatar(19 * engine.cellsize, 19 * engine.cellsize, Avatar.left, Color.green, "P3"));
    }
    
    public void run ()
    {
//        while(true)
//        {
//            time += speedtimer;
//
//            
//            engine.repaint();
//            try {
//		Thread.sleep(speedtimer);
//	    } catch (InterruptedException e) {}
//
//        }
    }
    
    public void refresh()
    {
        time += System.currentTimeMillis() - lastTime;
        engine.repaint();
        lastTime = System.currentTimeMillis();
    }
    
    public void checkUserInput(KeyEvent e) throws IOException
    {
        Avatar av = avatarList.get(0);
        switch (e.getKeyCode())
        {//P1
         case KeyEvent.VK_ENTER:
         {
            if(waitForServer || gameWon)
            {
                Command command = new Command();
                command.setType(Command.CommandType.READY);
                out.writeObject(command);
            }
            break;
         }
          
         case KeyEvent.VK_UP:
         {
             NavigationCommand command = new NavigationCommand();
             command.setDirection(Avatar.up);
             out.writeObject(command);
             break;
         }
         case KeyEvent.VK_LEFT:
         {
             NavigationCommand command = new NavigationCommand();
             command.setDirection(Avatar.left);
             out.writeObject(command);
             break;
         }
         case KeyEvent.VK_DOWN:
         {
             NavigationCommand command = new NavigationCommand();
             command.setDirection(Avatar.down);
             out.writeObject(command);
             break;
         }
         case KeyEvent.VK_RIGHT:
         {
             NavigationCommand command = new NavigationCommand();
             command.setDirection(Avatar.right);
             out.writeObject(command);
             break;
         }
         default:
         {
            break;
         }
     }
   
    }
}
