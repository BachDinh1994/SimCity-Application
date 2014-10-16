package restaurant_andrew.gui;

import restaurant_andrew.AndrewCustomerRole;
import restaurant_andrew.AndrewWaiterRole;
import restaurant_andrew.interfaces.Waiter;

import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.Semaphore;


public class WaiterGui implements Gui 
{
	private static final int WINDOWX = 640;
    private static final int WINDOWY = 400;
	Semaphore cAtWaiter = new Semaphore(0, true);
	
    private Waiter agent = null;

    private int xHome, yHome;//default waiter position
    private int xDestination, yDestination;//default start position
    private int xPos, yPos;

    private static final int WAITERSIZE = 20;
    
    HashMap<Integer, Coord> seatPositions = new HashMap<Integer,Coord>();
    
    boolean onBreak = false;
    boolean waiting = false;

    public WaiterGui(Waiter agent) {
        this.agent = agent;
        // populate map
        seatPositions.put(1, new Coord(2*WINDOWX/6, 10*WINDOWY/12));
        seatPositions.put(2, new Coord(3*WINDOWX/6, 9*WINDOWY/12));
        seatPositions.put(3, new Coord(4*WINDOWX/6, 8*WINDOWY/12));
        seatPositions.put(4, new Coord(5*WINDOWX/6, 7*WINDOWY/12));
        seatPositions.put(5, new Coord(300, 20)); // cook top row (orders in)
        seatPositions.put(6, new Coord(300, 70)); // cook top row (food out)
    }

    @SuppressWarnings("static-access")
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
        	if (xDestination != xHome & yDestination != yHome) {
        		agent.msgAtDestination();
        	}
        	else {
        		agent.animateSupport();
        	}
        }
    }

    public void draw(Graphics2D g) {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, WAITERSIZE, WAITERSIZE);
        String carrying = agent.getCarrying();
		if (carrying != null) {
			g.drawString(carrying, xPos, yPos);
		}
    }

    public boolean isPresent() {
        return true;
    }

    public void DoBringToTable(AndrewCustomerRole customer, int tableNum) {
        customer.getGui().DoGoToWaiter(xHome-20, yHome-20, this);
        try {
			cAtWaiter.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        xDestination = seatPositions.get(tableNum).getX();
        yDestination = seatPositions.get(tableNum).getY() - WAITERSIZE;
        customer.getGui().DoGoToSeat(xDestination, yDestination + WAITERSIZE);
    }

    public void DoGoToTable(int tableNum) {
        xDestination = seatPositions.get(tableNum).getX();
        yDestination = seatPositions.get(tableNum).getY() - WAITERSIZE;
    }

    public void DoGoSendCook() {
    	// cook in is #5 in map
        xDestination = seatPositions.get(5).getX();
        yDestination = seatPositions.get(5).getY();
    }
    public void DoGetFromCook() {
    	// cook out is #6 in map
        xDestination = seatPositions.get(6).getX();
        yDestination = seatPositions.get(6).getY();
    }

    public void DoLeaveDestination() {
        xDestination = xHome;
        yDestination = yHome;
    }

    public int getXPos() {
        return xPos;
    }

    public int getYPos() {
        return yPos;
    }
    
    public void setMap(HashMap<Integer,Coord> tablePositions) {
    	this.seatPositions = tablePositions;
    }
    
    public boolean onBreak() {
    	return onBreak;
    }
    public boolean waitingForBreakApproval() {
    	return waiting;
    }
    public void wantBreak() {
    	waiting = true;
    	agent.msgWantToGoOnBreak();
    }
    public void goOnBreak() {
    	waiting = false;
    	onBreak = true;
    }
    public void setBreak(boolean onBreak) {
    	this.onBreak = onBreak;
    }
    public void setWaiting(boolean waiting) {
    	this.waiting = waiting;
    }
    
    public void setHome(int x, int y) {
    	xHome = x;
    	yHome = y;
    	xDestination = x;
    	yDestination = y;
    	xPos = x;
    	yPos = y;
    }
}
