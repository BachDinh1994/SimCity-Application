package market.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import market.DeliveryTruckAgent;
import market.MarketAgent.spot;
import market.gui.DeliveryTruckGui.placeType;
import market.MarketCustomerAgent;
import SimCity.DeliveryTruckInCityGui;
import SimCity.Gui;

public class MarketCustomerGui implements Gui 
{


	private MarketCustomerAgent agent = null;

	public MarketCustomerGui(MarketCustomerAgent a){
		agent = a;
	}
	private boolean createPackage = false;
	private boolean packageFollow = false;
	private int xPos = 360, yPos = 355;
	private int xDestination = 360, yDestination = 355;
    private int xBox =160 , yBox =170;

	private int wSize = 20, hSize = 20, fSize = 10;
	public enum placeType
	{atFrontLine, none};
	placeType whereAmI = placeType.none;
	private int xBoxDest , yBoxDest;

	private class Location{
		public int x;
		public int y;
		Location(int xloc, int yloc){
			x = xloc;
			y = yloc;
		}	
	}


	public void getItem() {
		// TODO Auto-generated method stub
		packageFollow = true;
	
		xBoxDest = xBox- 20;
		yBoxDest = yBox;
	}

	public void updatePosition() {
		// TODO Auto-generated method stub
		if(packageFollow){
			if (xBox < xDestination)
				xBox++;
			else if (xBox > xDestination)
				xBox--;

			if (yPos < yDestination)
				yPos++;
			else if (yPos > yDestination)
				yPos--;
			if (xBox == xDestination && yPos == yDestination){
				
			}
			
		}
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
			if(whereAmI == placeType.atFrontLine){
				whereAmI = placeType.none;
				agent.msgFrontOfLine();

			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		// TODO Auto-generated method stub
		g.setColor(Color.magenta);
		g.fillRect(xPos, yPos, wSize, hSize);
		g.setColor(Color.black);
		g.drawString("Cust", xPos+5, yPos +10);

		
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return true;
	}

	public void setPosition(int x, int y){
		xPos = x;
		yPos = y;
		xDestination =x;
		yDestination =y;
	}

	public void getInLine(spot s) {
		xDestination = s.x;
		yDestination = s.y;
		whereAmI = placeType.atFrontLine;

	}

	public void goFront(spot s) {
		// TODO Auto-generated method stub

	}

	public void goHome() {
		xDestination = -20;
		yDestination = 0;
		// TODO Auto-generated method stub

	}




}