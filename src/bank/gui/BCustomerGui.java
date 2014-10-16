package bank.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import SimCity.Gui;
import bank.BankCustomer;

public class BCustomerGui implements Gui{
	private BankCustomer customer = null;
    static final int width = 20, height = 20;
	private int xPos = 0, yPos = 0;
	static final int deskX = 280;
	private int xDestination = 0, yDestination = 0;
	
	public BCustomerGui(BankCustomer c){
		customer = c;
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
        
        if (xPos == deskX && yPos == yDestination){
        	customer.msgAtTeller();
        }

	}
	
	public void waitInLine(int position){
		xDestination = 40;
		yDestination = 100 - (position * 20);
	}
	
	public void goSeeTeller(int position){
		xDestination = deskX;
		yDestination = 130 + (70 * position);
		//For testing
		if (customer.name.equals("test")){
			customer.msgAtTeller();
		}
	}
	
	public void leaveTeller(){
		xDestination = -20;
		yDestination = -20;
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillRect(xPos,yPos,width,height);
	}

	public boolean isPresent() {
		return true;
	}

}
