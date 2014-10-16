package restaurant_rancho.gui;

import restaurant.interfaces.Customers;
import restaurant_rancho.RanchoCustomerRole;
import restaurant_rancho.interfaces.Customer;

import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import Role.CustomerRole;
import SimCity.PersonGui;

public class CustomerGui implements Gui{
    
	private Customer agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;
    
	//private HostAgent host;
	RestaurantGui gui;
    
	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToSeat, LeaveRestaurant, Waiting};
	private Command command=Command.noCommand;
    
	public static int xTable;
	public static int yTable;
	private int xWait=10;
	private int yWait=50;
	private PersonGui personGui;
	private String xiFilename="/imgres-2.jpg";
	private Image xiImage;
	
	public CustomerGui(Customer c, RestaurantGui gui){ //HostAgent m) {
		agent = c;
		
		xPos =-80;
		yPos= -80;
		xDestination = -80;
		yDestination = -80;
		//maitreD = m;
		this.gui = gui;
		
		//lab3
		xiImage=new ImageIcon(getClass().getResource(xiFilename)).getImage();
	}
	public CustomerGui(Customer c, PersonGui p)
	{ 
		agent = c;
		personGui = p;
		xPos = 0;
		yPos = 0;
		xDestination = 5;
		xiImage=new ImageIcon(getClass().getResource(xiFilename)).getImage();
	}
	public void updatePosition() {
		if (xPos < xDestination)
			xPos=xPos+1;
		else if (xPos > xDestination)
			xPos=xPos-1;
        
		if (yPos < yDestination)
			yPos=yPos+1;
		else if (yPos > yDestination)
			yPos=yPos-1;
        
		if (xPos == xDestination && yPos == yDestination) {
			if (command==Command.GoToSeat) agent.msgAnimationFinishedGoToSeat();
			else if (command==Command.LeaveRestaurant) {
				agent.msgAnimationFinishedLeaveRestaurant();
				//System.out.println("about to call gui.setCustomerEnabled(agent);");
				isHungry = false;
				//gui.setCustomerEnabled(agent);
			}
			else if(command==Command.Waiting){
				agent.msgAtWait();
				System.out.println("The customer has arrived at the waiting area.");
			}
			command=Command.noCommand;
		}
		if (xPos == -40 && yPos == -40)
		{
			xPos = -45;
			yPos = -45;
	    	xDestination = -45;
	    	yDestination = -45;
			personGui.backToResident(RanchoCustomerRole.class);
		}
	} 
    
	public void draw(Graphics2D g) {
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, 20, 20);
		
		g.drawImage(xiImage, xPos, yPos,new JPanel());
	}
    
	public boolean isPresent() {
		return isPresent;
	}
	public void setHungry() {
		isHungry = true;
		agent.gotHungry();
		setPresent(true);
	}
	public boolean isHungry() {
		return isHungry;
	}
    
	public void setPresent(boolean p) {
		isPresent = p;
	}
    
	public void DoGoToSeat(int seatnumber) {//later you will map seatnumber to table coordinates.
		xDestination = xTable;
		yDestination = yTable;
		command = Command.GoToSeat;
	}
    
	public void DoExitRestaurant() {
		xDestination = -40;
		yDestination = -40;
		command = Command.LeaveRestaurant;
	}

	public void setTablePosition() {
		xTable=(agent.getTableNumber()-1)%2*100+150;
		yTable=(int)((agent.getTableNumber()-1)/2)*100+150;
	}

	public void DoGoToWaiting() {
		// TODO Auto-generated method stub
		xDestination=xWait;
		yDestination=yWait;
		command = Command.Waiting;
	}
}
