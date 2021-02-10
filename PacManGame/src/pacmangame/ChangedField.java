/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame;

import java.io.Serializable;

/**
 *
 * @author Roland
 */
public class ChangedField  implements Serializable
{
    private int x;
    private int y;
    
    public void setX(int x)
    {
        this.x = x;
    }
    
    public int getX()
    {
        return x;
    }
    
    public void setY(int y)
    {
        this.y = y;
    }
    
    public int getY()
    {
        return y;
    }
}
