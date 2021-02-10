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
public class Field implements Serializable
{
    private int type;
    private boolean stone;
    
    public void setType(int value)
    {
        type = value;
    }
    
    public int getType()
    {
        return type;
    }
    
    public void setStone(boolean value)
    {
        stone = value;
    }
    
    public boolean getStone()
    {
        return stone;
    }
}
