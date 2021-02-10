/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package pacmangame;

import java.awt.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author attilatorda
 */
public class GameEngine extends  Thread{
    private ArrayList<Avatar> avatarList;
       
    public Wall wall;
    public int speedtimer;
    public int time;
    private ChangedField[] changedFields = new ChangedField[10];
    private int level = 0;
    private boolean won  = false;
    private boolean isRunning = false;
    private long changePlayerColor = 30000;
    private long lastChange = 0;

    DisplayEngine de;

    public GameEngine ()
    {
        avatarList = new ArrayList<Avatar>();

        wall = new WallLevel1();
        
        speedtimer = 20;
        time = 0;
        //Start thread
        //this.run();
    }

    private void move()
    {
        for(int i = 0; i < avatarList.size(); i++)
        {//Do it for all players
            Avatar av = avatarList.get(i);
            int x = av.getX();
            int y = av.getY();

            if(x % DisplayEngine.cellsize == 0 && y % DisplayEngine.cellsize == 0)
            {//The guy is standing in the center of a cell

                int cellx = x / DisplayEngine.cellsize;
                int celly = y / DisplayEngine.cellsize;
                int cell = wall.getField()[cellx][celly].getType();
                
                switch(av.getNewOrientation())
                {
                    case Avatar.up:
                        if(celly <= 0 || cell % Wall.up != 0 || wall.getField()[cellx][celly - 1].getType() % Wall.down != 0) 
                        {
                            break;
                        }
                        else
                        {
                            av.setY(av.getY() - 1);
                            av.setOrientation(av.getNewOrientation());
                            break;
                        }
                    case Avatar.left:
                        if(cellx <= 0 || cell % Wall.left != 0 || wall.getField()[cellx - 1][celly].getType() % Wall.right != 0) 
                        {
                            break;
                        }
                        else
                        {
                            av.setX(av.getX() - 1);
                            av.setOrientation(av.getNewOrientation());
                            break;
                        }
                    case Avatar.down:
                        if(celly >= 20 || cell % Wall.down != 0 || wall.getField()[cellx][celly + 1].getType() % Wall.up != 0)
                        {
                            break;
                        }
                        else
                        {
                            av.setY(av.getY() + 1);
                            av.setOrientation(av.getNewOrientation());
                            break;
                        }
                    case Avatar.right:
                        if(cellx >= 20 || cell % Wall.right != 0 || wall.getField()[cellx + 1][celly].getType() % Wall.left != 0) 
                        {
                            break;
                        }
                        else
                        {
                            av.setX(av.getX() + 1);
                            av.setOrientation(av.getNewOrientation());
                            break;
                        }
                    default: 
                    {
                        break;
                    }
                }//switch
            }//if
            else
            {//The gut is inbetweem two cells
            
                //Changing directions
                if((av.getOrientation() == Avatar.left || av.getOrientation() == Avatar.right) &&
                (av.getNewOrientation() == Avatar.left || av.getNewOrientation() == Avatar.right))
                {
                    av.setOrientation(av.getNewOrientation());
                }
                else if((av.getOrientation() == Avatar.up || av.getOrientation() == Avatar.down)
                     && (av.getNewOrientation() == Avatar.up || av.getNewOrientation() == Avatar.down))
                {
                    av.setOrientation(av.getNewOrientation());
                }
                
                switch(av.getOrientation())
                {
                    case Avatar.up:
                    {
                        av.setY(av.getY() - 1);
                        break;
                    }
                    case Avatar.left:
                    {
                        av.setX(av.getX()-1);
                        break;
                    }
                    case Avatar.down:
                    {
                        av.setY(av.getY() + 1);
                        break;
                    }
                    case Avatar.right:
                    {
                        av.setX(av.getX() + 1);
                        break;
                    }
                    default: break;
                }//switch
            }//else
        }//for
    }//move

    private void collisionCheck()
    {
        // Reset ChangedFields
        for(int i = 0; i < changedFields.length; i++)
        {
            changedFields[i] = null;
        }
        
        // Check for stones collisions       
        for(int i = 0; i < avatarList.size(); i++)
        {//Do it for all players
            Avatar av = avatarList.get(i);
            int x = av.getX();
            int y = av.getY();

            if(x % DisplayEngine.cellsize == 0 && y % DisplayEngine.cellsize == 0)
            {//The guy is standing in the center of a cell
                int cellx = x / DisplayEngine.cellsize;
                int celly = y / DisplayEngine.cellsize;
                if(wall.getField()[cellx][celly].getStone())
                {
                    av.setPoints(av.getPoints() + 1);
                    wall.getField()[cellx][celly].setStone(false);
                    for(int j = 0; j < changedFields.length; j++)
                    {
                        if(changedFields[j] == null)
                        {
                            ChangedField changed = new ChangedField();
                            changed.setX(cellx);
                            changed.setY(celly);
                            
                            changedFields[j] = changed;
                        }
                    }
                }
            }
        }
        
        // Check for avatar collisions
        for(int i = 0; i < avatarList.size(); i++)
        {//Do it for all players
            Avatar av = avatarList.get(i);
            int x = av.getX();
            int y = av.getY();
            
            if(x % DisplayEngine.cellsize == 0 && y % DisplayEngine.cellsize == 0)
            {
                for(int j = 0; j < avatarList.size(); j++)
                {
                    //Do it for all other players
                    if(i != j)
                    {
                        Avatar av2 = avatarList.get(j);
                        int x1 = av2.getX();
                        int y1 = av2.getY();
                        
                        if(x1 % DisplayEngine.cellsize == 0 && y1 % DisplayEngine.cellsize == 0)
                        {
                           // Both Players stands in the middle of the same field
                            if(x == x1 && y == y1)
                            {
                                boolean eats = false;
                                // Check only if the first player eats the second player
                                if(av.getColor().equals(Color.RED) && av2.getColor().equals(Color.BLUE))
                                {
                                    // The Red Player eats the blue player
                                    eats = true;
                                }
                                else if(av.getColor().equals(Color.BLUE) && av2.getColor().equals(Color.GREEN))
                                {
                                    // The Blue Player eats the green player
                                    eats = true;
                                }
                                else if(av.getColor().equals(Color.GREEN) && av2.getColor().equals(Color.RED))
                                {
                                    // The green Player eats the red player
                                    eats = true;
                                }
                                
                                if(eats)
                                {
                                    av.setPoints(av.getPoints() + av2.getPoints());
                                    av2.setPoints(0);
                                    won = true;
                                    changeLevel();
                                }
                            }
                        }            
                    }
                }
            }
        }
    }
    
    private void changeLevel()
    {
        for(Avatar avatar : avatarList)
        {
            avatar.setPoints(0);
        }
        
        // TODO change Levels
        this.wall = new WallLevel1();
        level = 0;
    }

    public void run ()
    {
        int round = 0;
        while(true)
        {
            round ++;
            time += speedtimer;
           
            //de.repaint();
//            if(hasChanged)
//            {
                if(isRunning && !won)
                {
                    if(changePlayerColor < System.currentTimeMillis() - lastChange)
                    {
                        Color color = avatarList.get(0).getColor();
                        avatarList.get(0).setColor(avatarList.get(1).getColor());
                        avatarList.get(1).setColor(avatarList.get(2).getColor());
                        avatarList.get(2).setColor(color);
                            
                        lastChange = System.currentTimeMillis();
                    }
                    
                    move();
                    collisionCheck();
            
                    PlayBoardCommand command = new PlayBoardCommand();
                    int i = 0;
                    command.setGameWon(won);
                    for(Avatar avatar : this.avatarList)
                    {
                        command.addAvatar(i, avatar);
                        i++;
                    }
                    
                    for(i = 0; i < changedFields.length; i++)
                    {
                        command.getChangedFields()[i] = changedFields[i];
                    }

                    for(Avatar avatar : this.avatarList)
                    {
                        if(avatar instanceof ServerAvatar)
                        {
                            ((ServerAvatar)avatar).sendNotification(command);
                        }
                    }        

                round = 0;
            }
            //}
            try {
		Thread.sleep(speedtimer);
	    } catch (InterruptedException e) {}

        }
    }
    
    public void addPlayer(Socket socket)
    {       
        ServerAvatar avatar = new ServerAvatar(socket, this);
        NotifyCommand notification = new NotifyCommand();
        
        if(avatarList.size() > 3)
        {
            // There are to many players
            notification.setMessage("There are too many players");
            notification.setType(Command.CommandType.QUIT);
            System.out.println("Client " + socket.getInetAddress().toString() + " connection refused");
        }
        else
        {
            Avatar levelAvatar = wall.playerAtLocation(avatarList.size());
            avatar.setX(levelAvatar.getX() * DisplayEngine.cellsize);
            avatar.setY(levelAvatar.getY() * DisplayEngine.cellsize);
            avatar.setOrientation(levelAvatar.getOrientation());
            avatar.setColor(levelAvatar.getColor());
            avatar.setAvatarName(levelAvatar.getAvatarName());
            avatarList.add(avatar);
            notification.setMessage("Connected.");
            notification.setType(Command.CommandType.READY);
            avatar.start();
            System.out.println("Client " + socket.getInetAddress().toString() + " connected");
        }
        avatar.sendNotification(notification);
    }
    
    public void playerCommand(Avatar avatar, Command command)
    {
        switch(command.getType())
        {
            case READY:
            {
                checkGameStart();
                break;
            }
        }
    }

    private void checkGameStart()
    {
        // Check if game can start
        for(Avatar avatar : this.avatarList)
        {
            if(avatar instanceof ServerAvatar)
            {
                ServerAvatar sa = (ServerAvatar)avatar;
                if(!sa.getReady())
                {
                    return;
                }
            }
        }
        
        for(int i = avatarList.size(); i < 3; i++)
        {
            Avatar av = new Avatar(wall.playerAtLocation(i).getX() * DisplayEngine.cellsize, wall.playerAtLocation(i).getY() * DisplayEngine.cellsize,
                    Avatar.fixed, wall.playerAtLocation(i).getColor(), wall.playerAtLocation(i).getAvatarName());
            avatarList.add(av);
        }
        
        for(int i = 0; i < avatarList.size(); i++)
        {
            Avatar levelAvatar = wall.playerAtLocation(i);
            avatarList.get(i).setX(levelAvatar.getX() * DisplayEngine.cellsize);
            avatarList.get(i).setY(levelAvatar.getY() * DisplayEngine.cellsize);
            avatarList.get(i).setOrientation(levelAvatar.getOrientation());
            avatarList.get(i).setColor(levelAvatar.getColor());
        }
        
        // Start game
        WallCommand command = new WallCommand();
        command.setWall(this.wall);
        for(Avatar avatar : this.avatarList)
        {
            if(avatar instanceof ServerAvatar)
            {
                System.out.println("Send Wall");
                ((ServerAvatar)avatar).sendNotification(command);
            }
        }
        
        isRunning = true;
        won = false;
        lastChange = System.currentTimeMillis();
    }
}
