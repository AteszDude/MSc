/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame;

/**
 *
 * @author Roland
 */
public class NavigationCommand extends Command
{
    private int direction;
    
    public void setDirection(int direction)
    {
        this.direction = direction;
    }
    
    public int getDirection()
    {
        return direction;
    }
    
    public NavigationCommand()
    {
        this.setType(CommandType.NAVIGATION);
    }
}
