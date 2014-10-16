package bank.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import SimCity.Gui;
import bank.BankAgent;

public class TellerGui implements Gui{
	private BankAgent teller = null;
    static final int width = 20, height = 20;
    static final int deskX = 330;
    private int deskY = 145;
    private int xPos = 0, yPos = 0;
    private int xDestination, yDestination = 0;
    //BankGui gui;
    public TellerGui(BankAgent t, int deskPos){
    	teller = t;
    	deskY += (deskPos * 70);
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
	        	teller.msgAtDesk();
	        }
		}	
	}

	public void draw(Graphics2D g) {
    	g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, width, height);
	}
	
	public void moveToDesk(){
		xDestination = deskX;
		yDestination = deskY;
	}
	
	public void leaveBank(){
		xDestination = -20;
		yDestination = -20;
	}

	public boolean isPresent() {
		return true;
	}

}
