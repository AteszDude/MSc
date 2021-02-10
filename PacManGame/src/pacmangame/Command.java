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
public class Command implements Serializable
{
    private CommandType type;
    
    public void setType(CommandType type)
    {
        this.type = type;
    }
    
    public CommandType getType()
    {
        return type;
    }
    
    public enum CommandType
    {
        ERROR,
        QUIT,
        WALL,
        READY,
        NAVIGATION,
        PLAYBOARD
    }
}
