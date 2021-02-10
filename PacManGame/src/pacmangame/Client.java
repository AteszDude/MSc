/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Roland
 */
public class Client 
{
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean isRunning;
    private ClientGameEngine engine;
    
     public static void main(String[] args) 
     {
         if(args.length != 1)
         {
             System.out.println("Wrong input parameters");
         }
         else
         {
             Client client = new Client();
             client.start(args[0]);
         }
     }
     
     public void start(String address)
     {
        String host = address.split(":")[0];
        int port = Integer.parseInt(address.split(":")[1]);
        
        try 
        {
            System.out.println("Connect to host");
            Socket socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            
            engine = new ClientGameEngine(out);
            engine.start();
            
            isRunning = true;
            while(isRunning)
            {
                Object obj = in.readObject();
                if(obj instanceof Command)
                {
                    Command notification = (Command)obj;
                    switch(notification.getType())
                    {
                        case QUIT:
                        {
                            isRunning = false;
                            System.out.println("Client closing");
                            break;
                        }
                        case WALL:
                        {
                            System.out.println("Get Wall");
                            WallCommand command = (WallCommand)notification;
                            engine.setWall(command.getWall());
                            engine.waitForServer = false;
                            engine.refresh();
                            break;
                        }
                        case PLAYBOARD:
                        {
                            PlayBoardCommand command = (PlayBoardCommand)notification;
                            engine.setGameWon(command.getGameWon());
                            engine.avatarList.clear();
                            for(int i = 0; i < command.getAvatars().length; i++)
                            {
                                if(command.getAvatars()[i] != null)
                                {
                                    engine.avatarList.add(command.getAvatars()[i]);
                                }
                            }
                            
                            for(int i = 0; i < command.getChangedFields().length; i++)
                            {
                                if(command.getChangedFields()[i] != null)
                                {
                                    engine.getWall().getField()[command.getChangedFields()[i].getX()][command.getChangedFields()[i].getY()].setStone(false);
                                }
                            }
                            engine.refresh();
                            break;
                        }
                    }
                }
            }
            
            out.close();
            in.close();
            socket.close();
        } 
        catch (Exception ex) 
        {
           ex.printStackTrace();
        } 
     }
}
