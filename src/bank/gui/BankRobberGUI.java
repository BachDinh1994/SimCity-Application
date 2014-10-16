
package bank.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import SimCity.Gui;
import bank.BankRobber;

public class BankRobberGUI implements Gui{
	private BankRobber robber = null;
	private boolean armed = false;
    static final int width = 20, height = 20;
	private int xPos = 0, yPos = 0;
	static final int deskX = 280;
	private int xDestination = 0, yDestination = 0;
	
	public BankRobberGUI(BankRobber r){
		robber = r;
		if (robber.getWState().equals("Armed"))
			armed = true;
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
        	robber.msgAtTeller();
        }

	}
	
	
	public void goSeeTeller(int position){
		xDestination = deskX;
		yDestination = 130 + (70 * position);
		//For testing
		if (robber.name.equals("test")){
			robber.msgAtTeller();
		}
	}
	
	public void leaveTeller(){
		xDestination = -20;
		yDestination = -20;
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.RED);
		g.fillRect(xPos,yPos,width,height);
		if (armed){
			g.setColor(Color.YELLOW);
			g.drawString("Armed!", xPos, yPos);
		}
	}

	public boolean isPresent() {
		return true;
	}

}

