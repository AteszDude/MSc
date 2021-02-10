/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame;

/**
 *
 * @author Roland
 */
public class WallLevel1 extends Wall
{
    public WallLevel1()
    {
        basicInit();
    }
    
    private  void basicInit()
    {
        //corners
        field[0][0].setType(down * right);
        field[0][0].setStone(true);
        field[19][0].setType(down * left);
        field[19][0].setStone(true);
        field[0][19].setType(up * right);
        field[0][19].setStone(true);
        field[19][19].setType(up * left);
        field[19][19].setStone(true);
        for(int i = 1; i < 19; i++)
        {
            //4 edges
            field[i][0].setType(left * right);
            field[i][0].setStone(true);
            field[i][19].setType(left * right);
            field[i][19].setStone(true);
            field[0][i].setType(up * down);
            field[0][i].setStone(true);
            field[19][i].setType(up * down);
            field[19][i].setStone(true);
        }

        //add some other elements
        field[9][0].setType(left * right * down);
        field[9][0].setStone(true);
        field[11][0].setType(left * right * down);
        field[11][0].setStone(true);
        field[9][1].setType(up * down);
        field[9][1].setStone(true);
        field[11][1].setType(up * down);
        field[11][1].setStone(true);
        field[9][2].setType(up * right);
        field[9][2].setStone(true);
        field[11][2].setType(up * left);
        field[11][2].setStone(true);
        field[10][2].setType(right * left);
        field[10][2].setStone(true);
    }    
}
