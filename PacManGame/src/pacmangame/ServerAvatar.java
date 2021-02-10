/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Roland
 */
public class ServerAvatar extends Avatar
{
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private boolean isRunning = false;
    private GameEngine engine;
    private boolean ready = false;
            
    public void setReady(boolean value)
    {
        ready = value;
    }
    
    public boolean getReady()
    {
        return ready;
    }
    
    public ServerAvatar(Socket socket, GameEngine engine)
    {
        this.socket = socket;
        this.engine = engine;
        try
        {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            
            this.setAvatarName(socket.getInetAddress().toString());
            isRunning = true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void run()
    {
        while(isRunning)
        {
            Object obj;
            try 
            {
                obj = in.readObject();
                if(obj instanceof Command)
                {
                    Command notification = (Command)obj;
                    switch(notification.getType())
                    {
                        case READY:
                        {
                            System.out.println("Client " + this.getAvatarName() + " ready.");
                            this.setReady(true);
                            engine.playerCommand(this, notification);
                            break;
                        }
                        case NAVIGATION:
                        {
                            NavigationCommand command = (NavigationCommand)notification;
                            this.setNewOrientation(command.getDirection());
                            break;
                        }
                    }
                }
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(Avatar.class.getName()).log(Level.SEVERE, null, ex);
            } 
            catch (ClassNotFoundException ex) 
            {
                Logger.getLogger(Avatar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public synchronized void sendNotification(Command notification)
    {
        try 
        {
            out.writeObject(notification);
            out.flush();
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }
    }
    
    public void close()
    {
        String client = "";
        isRunning = false;
        
        try
        {
            if(in != null)
            {
                in.close();;
                in = null;
            }
            if(out != null)
            {
                out.close();
                out = null;
            }
            if(socket != null)
            {
                client = socket.getInetAddress().toString();
                socket.close();
                socket = null;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();;
        }
        System.out.println("Client " + client + " disconnected");
    }
}
