package restaurant.gui;

import restaurant.interfaces.Customers;

import java.awt.*;

import Role.CustomerRole;
import SimCity.Gui;
import SimCity.PersonGui;

public class CustomerGui implements SimCity.Gui 
{
	private PersonGui personGui;
	private Customers agent = null;
	private boolean isPresent = true;
	private boolean isHungry = false;
	static final int width = 20, height = 20;
	private int homeposition;
    int[] table = new int[3]; //store table xpositions inside an array for easy access
	RestaurantGui gui;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;

	public static final int yTable = 150;
	public Customers getAgent()
	{
		return agent;
	}
	public CustomerGui(Customers c, RestaurantGui gui, int waitingposition)
	{ 
		this.agent = c;
		xPos = 0;
		yPos = 0;
		xDestination = 5;
		yDestination = 20*waitingposition+(5*(waitingposition-1));
		homeposition = waitingposition;
		this.gui = gui;
        for (int i=0;i<3;i++)
        {
        	table[i] =(i+1)*100; 
        }
	}
	public CustomerGui(Customers c, int waitingposition)
	{ 
		this.agent = c;
		xPos = 0;
		yPos = 0;
		xDestination = 5;
		yDestination = 20*waitingposition+(5*(waitingposition-1));
		homeposition = waitingposition;
        for (int i=0;i<3;i++)
        {
        	table[i] =(i+1)*100; 
        }
	}
	public CustomerGui(Customers c, PersonGui p, int waitingposition)
	{ 
		agent = c;
		personGui = p;
		xPos = 0;
		yPos = 0;
		xDestination = 5;
		yDestination = 20*waitingposition+(5*(waitingposition-1));
		homeposition = waitingposition;
        for (int i=0;i<3;i++)
        {
        	table[i] =(i+1)*100; 
        }
	}
    public void MoveToHomePosition()
    {
    	xDestination = 5;
    	yDestination = 20*homeposition+(5*(homeposition-1));
		isHungry = false;
		//gui.setCustomerEnabled(agent);
		command=Command.noCommand;
    }
    public void MoveOut()
    {
    	xDestination = -20;
    	yDestination = -20;
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

		if (xPos == xDestination && yPos == yDestination) 
		{
			if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
			else if (command==Command.LeaveRestaurant) 
			{
				//agent.msgAnimationFinishedLeaveRestaurant();
				//System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
				//gui.setCustomerEnabled(agent);
			}
			command=Command.noCommand;
		}
		if (xPos == -20 && yPos == -20)
		{
			xPos = -25;
			yPos = -25;
	    	xDestination = -25;
	    	yDestination = -25;
			personGui.backToResident(CustomerRole.class);
		}
	}

	public void draw(Graphics2D g) 
	{
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, width, height);
	}
	public boolean isPresent() 
	{
		return isPresent;
	}
	public void setHungry() 
	{
		isHungry = true;
		agent.gotHungry();
		//setPresent(true);
	}
	public boolean isHungry() 
	{
		return isHungry;
	}
	public void setPresent(boolean p) 
	{
		isPresent = p;
	}
	public void DoGoToSeat(int seatnumber) 
	{
	    xDestination = table[seatnumber-1];
		yDestination = yTable;
		command = Command.GoToSeat;
	}
	public void DoExitRestaurant() 
	{
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}
	public void setFullStomach()
	{
		isHungry = false;
	}
}
