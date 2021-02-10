/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pacmangame;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import sun.font.Font2D;


/**
 *
 * @author attilatorda
 */
public class DisplayEngine extends JComponent implements KeyListener
{
  private static Color m_tRed = new Color(255, 0, 0, 150);
  private static Color m_tGreen = new Color(0, 255, 0, 150);
  private static Color m_tBlue = new Color(0, 0, 255, 150);
  public static final int cellsize = 20;//Don't change this! Might cause problems!
  
    private JFrame mainFrame;
  
    ClientGameEngine parent;
  
  public DisplayEngine(ClientGameEngine gameengine)
    {
      //Need to reference gameengine to know what to paint!
      parent = gameengine;
      
              //Create window
        mainFrame = new JFrame("Pac Man Game");
        mainFrame.getContentPane().add(this);
        mainFrame.pack();
        mainFrame.setVisible(true);
        mainFrame.addKeyListener(this);

    }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    //set background
    g.setColor(Color.GRAY);
    g.fillRect(0, 0, getWidth(), getHeight());

    if(parent.waitForServer)
    {
        g.setColor(Color.BLUE);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        g.drawString("Waiting for server", 1 * cellsize , 15 * cellsize);
    }
    else if(parent.getGameWon())
    {
        Avatar winningPlayer = null;
        int points = -1;
        for(int i = 0; i < parent.avatarList.size(); i++)
        {
            if(points < parent.avatarList.get(i).getPoints())
            {
                winningPlayer = parent.avatarList.get(i);
                points = winningPlayer.getPoints();
            }
        }
        
        g.setColor(Color.BLUE);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        
        g.drawString("Player " + winningPlayer.getAvatarName() + " won the round.", 1 * cellsize , 15 * cellsize);
        
    }
    else
    {
        //Paint wall
        for(int i = 0; i < 20; i++)
            for (int j = 0; j < 20; j++)
                    {
                    paintCell(g, i * cellsize, j * cellsize, parent.getWall().getField()[i][j]);
                    }

        //Paint pac men
        for(int i = 0; i < parent.avatarList.size(); i++)
        {
            Avatar av = parent.avatarList.get(i);
            int period = (parent.time % (parent.speedtimer * 10)) / parent.speedtimer;
            if(period > 5) period = 10 - period;
            paintAvatar(g, av.getX(), av.getY(), av.getOrientation() * 90, period, av.getColor());
        }
    }
    
    // Paint scoreboard
    g.setColor(Color.WHITE);
    g.setFont(new Font("Arial", Font.PLAIN, 16));
    int starty = 8 * cellsize;
    for(int i = 0; i < parent.avatarList.size(); i++)
    {
        g.drawString("Score: \t" + parent.avatarList.get(i).getPoints(), 8 * cellsize, starty + i * cellsize);
    }
  }

  public void paintCell(Graphics g, int x, int y, Field field)
    {
        int cell = field.getType();
        //draw wall or path
        if (cell == Wall.wall)
        {
                g.setColor(Color.blue);
        }
        else
        {
                g.setColor(Color.BLACK);
        }

        g.fillRect(x, y, cellsize, cellsize);
        
        if(field.getStone())
        {
            g.setColor(Color.WHITE);
            g.fillRect(x + cellsize/8*3, y + cellsize/8*3, cellsize/4, cellsize/4);    
        }
        
        //draw borderlines or return if there is no needed
        if (cell == Wall.wall || cell == Wall.cross)
        {
            return;
        }

        g.setColor(Color.black);
        if(cell % Wall.up != 0)
        {
            g.drawLine(x, y, x + cellsize, y);
        }
        if(cell % Wall.right != 0)
        {
            g.drawLine(x + cellsize - 1, y, x + cellsize - 1, y + cellsize);
        }
        if(cell % Wall.down != 0)
        {
            g.drawLine(x, y + cellsize - 1, x + cellsize, y + cellsize - 1);
        }
        if(cell % Wall.left != 0)
        {
            g.drawLine(x, y, x, y + cellsize);
        }
    }

  public void paintAvatar(Graphics g, int x, int y, int degrees, int period, Color color)
    {//Paint a pac man
      g.setColor(color);
      g.fillArc(x, y, cellsize, cellsize, degrees + period * 7, 360 - period * 14);
    }

  public Dimension getPreferredSize() {
    return new Dimension(400, 400);
  }

  public Dimension getMinimumSize() {
    return getPreferredSize();
  }

  public void keyTyped(KeyEvent e) {}
  public void keyPressed(KeyEvent e)
  {
      try
      {
        parent.checkUserInput(e);
      }
      catch(Exception exc)
      {
          exc.printStackTrace();
      }
  }
  public void keyReleased(KeyEvent e) {}
}
