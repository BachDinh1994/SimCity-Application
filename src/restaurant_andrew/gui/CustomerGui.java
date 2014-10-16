package restaurant_andrew.gui;

import restaurant_andrew.AndrewCustomerRole;

import java.awt.*;
import java.util.concurrent.Semaphore;

import Role.CustomerRole;
import SimCity.PersonGui;

public class CustomerGui implements Gui{
	private PersonGui personGui;
	private AndrewCustomerRole agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	RestaurantGui gui;
	WaiterGui g;

	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToWaitSpot, GoToWaiter, GoToSeat, LeaveRestaurant};
	private Command command=Command.noCommand;

	public static final int xTable1 = 640/4;
	public static final int yTable = 2*400/3;
	private static final int CUSTSIZE = 20;
	private static final int xOutside = -20;
	private static final int yOutside = -20;

	public CustomerGui(AndrewCustomerRole c, RestaurantGui gui){ //HostAgent m) {
		agent = c;
		xPos = xOutside;
		yPos = yOutside;
		xDestination = xOutside;
		yDestination = yOutside;
		this.gui = gui;
	}
	public CustomerGui(AndrewCustomerRole c, PersonGui p){ //HostAgent m) {
		personGui = p;
		agent = c;
		xPos = 0;
		yPos = 0;
		xDestination = 10;
		yDestination = 10;
	}
	public void updatePosition() {
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;

		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToWaiter) g.cAtWaiter.release();
			else if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
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
			personGui.backToResident(AndrewCustomerRole.class);
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, CUSTSIZE, CUSTSIZE);
		String choice = agent.getChoice();
		if (choice != null) {
			g.drawString(choice, xPos, yPos);
		}
	}
	
	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() 
	{
		isHungry = true;
		agent.msgGotHungry();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}

	public void setPresent(boolean p) {
		isPresent = p;
	}

	public void DoGoToWaitSpot(int x, int y) {
		xDestination = x;
		yDestination = y;
		command = Command.GoToWaitSpot;
	}
	
	public void DoGoToWaiter(int x, int y, WaiterGui g) {
		this.g = g;
		xDestination = x;
		yDestination = y;
		command = Command.GoToWaiter;
	}
	
	public void DoGoToSeat(int x, int y) {
		xDestination = x; // seatPositions.get(seatnumber).getX();
		yDestination = y; // seatPositions.get(seatnumber).getY();
		command = Command.GoToSeat;
	}

	public void DoExitRestaurant() {
		xDestination = xOutside;
		yDestination = yOutside;
		command = Command.LeaveRestaurant;
	}
	
}
