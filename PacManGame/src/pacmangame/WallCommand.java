/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame;

/**
 *
 * @author Roland
 */
public class WallCommand extends Command
{
    private Wall wall;
    
    public void setWall(Wall wall)
    {
        this.wall = wall;
    }
    
    public Wall getWall()
    {
        return wall;
    }
    
    public WallCommand()
    {
        this.setType(CommandType.WALL);
    }
}
