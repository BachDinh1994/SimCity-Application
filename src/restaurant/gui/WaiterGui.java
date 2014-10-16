package restaurant.gui;

//import restaurant.CustomerAgent;
import restaurant.WaiterAgent;
import restaurant.interfaces.Customers;
import restaurant.interfaces.Waiters;

import java.awt.*;

import Role.WaiterRole;
import SimCity.Gui;
import SimCity.PersonGui;

public class WaiterGui implements Gui 
{
	private Waiters agent = null;
	private PersonGui g;
    static final int width = 20, height = 20;
    private int xPos = 0, yPos = 0;
    private int xDestination, yDestination = 0;
    int[] table = new int[3];
    public static final int yTable = 150;
    private int homeposition;
    private boolean onBreak = false;
    private boolean willBeOnBreak = false;
    RestaurantGui gui;
    
    public WaiterGui(Waiters agent, RestaurantGui gui, int waitingposition) 
    {
    	xDestination = 30*waitingposition-(5*(waitingposition-1));
		homeposition = waitingposition;
        for (int i=0;i<3;i++)
        {
        	table[i] =(i+1)*100; 
        }
        this.agent = agent;
        this.gui = gui;
    }
    public WaiterGui(WaiterAgent agent, int waitingposition) 
    {
    	xDestination = 30*waitingposition-(5*(waitingposition-1));
		homeposition = waitingposition;
        for (int i=0;i<3;i++)
        {
        	table[i] =(i+1)*100; 
        }
        this.agent = agent;
    }
    public WaiterGui(Waiters c, PersonGui gui2, int waitingposition) 
    {
    	agent = c;
    	g = gui2;
    	xDestination = 30*waitingposition-(5*(waitingposition-1));
		homeposition = waitingposition;
        for (int i=0;i<3;i++)
        {
        	table[i] =(i+1)*100; 
        }
	}
	public void MoveToHomePosition()
    {
    	xDestination = 30*homeposition-(5*(homeposition-1));
    	yDestination = 0;
    }
    public void updatePosition() 
    {
        if (xPos < xDestination)
            xPos++;
        else if (xPos > xDestination)
            xPos--;

        if (yPos < yDestination)
            yPos++;
        else if (yPos > yDestination)
            yPos--;
    }
    public void draw(Graphics2D g) 
    {
    	g.setColor(Color.BLUE);
        g.fillRect(xPos, yPos, width, height);
    }
    public boolean isPresent() 
    {
        return true;
    }
    public void DoBringToTable(Customers c, int tableno) 
    {
        xDestination = table[tableno-1] + 20;
        yDestination = yTable - 20;
    }
    public void DoLeaveCustomer() 
    {
        xDestination = -20;
        yDestination = -20;
    }
    public void BackToWork()
    {
    	xDestination = 30*homeposition-(5*(homeposition-1));
    	yDestination = 0;
        onBreak = false;
        willBeOnBreak = false;
        //gui.setWaiterEnabled(agent);
    }
    public void GoBackToTable() 
    {
        yDestination = yTable - 20;
    }
    public void GoForRest()
    {
    	yDestination = 400;
    	onBreak = true;
    }
    public void LetCustomerEat()
    {
    	yDestination = yTable - 80;
    }
    public void setBreak()
    {
    	agent.msgStartBreak();
    }
    public void setReadyBreak()
    {
    	willBeOnBreak = true;
    }
    public void notReadyBreak()
    {
    	willBeOnBreak = false;
    }
    public boolean isOnBreak()
    {
    	return onBreak;
    }
    public boolean willBeOnBreak()
    {
    	return willBeOnBreak;
    }
    public void GoToCook()
    {
        yDestination = yTable + 100;
    }
}