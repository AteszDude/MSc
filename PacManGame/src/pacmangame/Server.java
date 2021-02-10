/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Roland
 */
public class Server {
    private GameEngine gameEngine;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int port;
        
        try
        {
            if(args.length != 1)
            {
                System.out.println("Missing start argument port number!");
            }
            else
            {
                // Read port number from program arguments
                port = Integer.parseInt(args[0]);
                
                // Start a new server
                Server server = new Server();
                server.run(port);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void run(int port) throws IOException
    {
        gameEngine = new GameEngine();
        gameEngine.start();
        
        boolean exit = false;
        ServerSocket server = new ServerSocket(port);
        
        System.out.println("Waiting for players.");
        while(!exit)
        {
            try
            {
                Socket socket = server.accept();
                gameEngine.addPlayer(socket);
            }
            catch(Exception e)
            {
                System.console().printf("Error getting connection to client");
                e.printStackTrace();
            }
        }
    }
}
