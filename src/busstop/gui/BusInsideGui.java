package busstop.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import SimCity.BusOutsideGui;
import SimCity.Gui;
import busstop.BusAgent;

public class BusInsideGui implements Gui{

	BusAgent b;
	BusOutsideGui boGui;
	
	private boolean isPresent = false;
	
	public int xPos, yPos, xDestination, yDestination;
	private enum Command {noCommand, GoToCurb, LeaveStop};
	private Command command=Command.noCommand;

	private static final int xOutside = 100;
	private static final int yOutside = 500;
	private static final int W = 40, H = 100;

	public BusInsideGui(BusAgent b, BusOutsideGui boGui){
		this.b = b;
		this.boGui = boGui;
		xPos = xOutside;
		yPos = yOutside;
		xDestination = xOutside;
		yDestination = yOutside;
	}
	
	@Override
	public void updatePosition() {
		if (xPos < xDestination)
			xPos+=5;
		else if (xPos > xDestination)
			xPos-=5;

		if (yPos < yDestination)
			yPos+=5;
		else if (yPos > yDestination)
			yPos-=5;
		
		if (xPos == xDestination && yPos == yDestination) {
			if (command == Command.GoToCurb) b.msgAtCurb();
			else if (command == Command.LeaveStop) b.msgOutOfStop();
			command=Command.noCommand;
			isPresent = false;
		}
	}

	@Override
	public void draw(Graphics2D g) {
        g.setColor(Color.ORANGE);
        g.fillRect(xPos, yPos, W, H);
	}

	@Override
	public boolean isPresent() {
		return isPresent;
	}
	
	public void DoGoToCurb() {
		isPresent = true;
		xPos = xOutside;
		yPos = yOutside;
		xDestination = 100;
		yDestination = 100;
		command = Command.GoToCurb;
	}
	public void DoLeaveStop() {
		isPresent = true;
		xDestination = 100;
		yDestination = -150;
		command = Command.LeaveStop;
	}
}
