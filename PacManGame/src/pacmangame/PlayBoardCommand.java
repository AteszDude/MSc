/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Roland
 */
public class PlayBoardCommand extends Command
{
    private Avatar[] avatars = new Avatar[3];
    private ChangedField[] changedFields = new ChangedField[10];
    private boolean gameWon = false;
    
    public void setAvatars(Avatar[] avatars)
    {
        this.avatars = avatars;
    }
    
    public Avatar[] getAvatars()
    {
        return avatars;
    }
    
    public void setChangedFields(ChangedField[] fields)
    {
        changedFields = fields;
    }
    
    public ChangedField[] getChangedFields()
    {
        return changedFields;
    }
    
    public PlayBoardCommand()
    {
        this.setType(CommandType.PLAYBOARD);
    }
    
    public void addAvatar(int index, Avatar avatar)
    {
        Avatar av = new Avatar(avatar.getX(), avatar.getY(), avatar.getOrientation(), avatar.getColor(), avatar.getAvatarName());
        av.setPoints(avatar.getPoints());
        avatars[index] = av;
    }
    
    public void setGameWon(boolean value)
    {
        gameWon = value;
    }
    
    public boolean getGameWon()
    {
        return gameWon;
    }
}
