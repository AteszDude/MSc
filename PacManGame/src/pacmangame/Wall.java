/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pacmangame;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;

/**
 *
 * @author attilatorda
 */
public class Wall implements Serializable
{
    protected Avatar[] avatars = new Avatar[3];
    protected Field [][] field;
    /*0 means wall, otherwise it's composed as follows:
    every direction is represented by a prime number
    get all the allowed directions and multiply them together
    to check whether if a directions is allowed the division must have 0 remainder
    e.g. a cross allowes all directions so it is 2*3*5*7 = 210
    a vertical way allows up and down so it is 2*3=6 and it gives
     a remainder if it is divided by right (7) or left (5)
    */
    public static final int up = 2, down = 3, left = 5, right = 7;
    //Some examples
    public static final int wall = 0, updown = 10, leftright = 21, cross = 210;
    
    public void setField(Field[][] field)
    {
        this.field = field;
    }
    
    public Field[][] getField()
    {
        return field;
    }

    public Wall()
    {
        //Initialize the wall
        field = new Field[20][20];
        for(int i = 0; i < 20; i++)
        {
           for (int j = 0; j < 20; j++)
           {
            field[i][j] = new Field();
            field[i][j].setType(wall);
           }
        }
        
        avatars[0] = new Avatar(0, 0, Avatar.right, Color.RED, "P1");
        avatars[1] = new Avatar(19, 0, Avatar.down, Color.BLUE, "P2");
        avatars[2] = new Avatar(19, 19, Avatar.left, Color.GREEN, "P3");
    }
    
    public Avatar playerAtLocation(int i)
    {
        return avatars[i];
    }
    
    public int getAvatarCount()
    {
        return avatars.length;
    }
}
