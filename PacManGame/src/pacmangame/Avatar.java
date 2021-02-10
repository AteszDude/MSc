/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pacmangame;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author attilatorda
 */
public class Avatar extends Thread implements Serializable
{
     //The numbers are according to rotation by 90 degrees
    public static final int up = 1, down = 3, left = 2, right = 0, fixed = -1;
    
    private int x, y, orientation, newOrientation;
    private Color color;
    private String name;
    private int points;
    
    public void setOrientation(int value)
    {
        orientation = value;
    }
    
    public int getOrientation()
    {
        return orientation;
    }
    
    public void setNewOrientation(int value)
    {
        newOrientation = value;
    }
    
    public int getNewOrientation()
    {
        return newOrientation;
    }
    
    public int getX()
    {
        return x;
    }
    
    public void setX(int value)
    {
        x = value;
    }
    
    public int getY()
    {
        return y;
    }
    
    public void setY(int value)
    {
        y = value;
    }
    
    public void setColor(Color color)
    {
        this.color = color;
    }
    
    public Color getColor()
    {
        return color;
    }
    
    public void setAvatarName(String name)
    {
        this.name = name;
    }
    
    public String getAvatarName()
    {
        return name;
    }
    
    public int getPoints()
    {
        return points;
    }
    
    public void setPoints(int points)
    {
        this.points = points;
    }
    
    public Avatar(int ax, int ay, int aorientation, Color acolor, String aname)
    {
        x = ax;
        y = ay;
        orientation = aorientation;
        newOrientation = aorientation;
        color = acolor;
        name = aname;
    }
    
    public Avatar()
    {
        color = Color.CYAN;
    }
}
