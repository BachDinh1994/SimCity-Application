package bank.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import SimCity.Gui;
import bank.BankHost;

public class HostGui implements Gui{
	private BankHost host = null;
    static final int width = 20, height = 20;
    static final int deskX = 45, deskY = 125;
    private int xPos = 0, yPos = 0;
    private int xDestination, yDestination = 0;
    
    
    public HostGui(BankHost h){
    	host = h;
    }
	public void updatePosition() {
		if (xPos != xDestination || yPos != yDestination){ //Check to make sure GUI is arriving at destination, not just standing there
			if (xPos < xDestination)
	            xPos++;
	        else if (xPos > xDestination)
	            xPos--;
	
	        if (yPos < yDestination)
	            yPos++;
	        else if (yPos > yDestination)
	            yPos--;
	        if (xPos == deskX && yPos == deskY){ // so that the GUI can inform the agent it's okay to start working
	        	host.msgAtDesk();
	        }
		}
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.ORANGE);
		g.fillRect(xPos, yPos, width, height);
	}
	
	public boolean isPresent() {
		return true;
	}
	
	public void moveToDesk(){
		xDestination = deskX;
		yDestination = deskY;
	}
	
	public void LeaveBank(){
		xDestination = -20;
		yDestination = -20;
	}

}
