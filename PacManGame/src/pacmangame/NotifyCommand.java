/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmangame;

/**
 *
 * @author Roland
 */
public class NotifyCommand extends Command
{
        private String message;
        
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    public String getMessage()
    {
        return message;
    }
}
