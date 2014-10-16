package busstop.gui;

import java.awt.Color;
import java.awt.Graphics2D;

import SimCity.Gui;
import SimCity.PersonGui;
import busstop.interfaces.BusPassenger;

public class BusPassengerGui implements Gui{

	BusPassenger bp;
	PersonGui pg;
	
	private boolean isPresent = false;
	
	private int xPos, yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToMachine, GetInLine, GetOnBus, LeaveStation};
	private Command command=Command.noCommand;

	private static final int xOutside = 500;
	private static final int yOutside = 150;
	private static final int xEnterBus = 140;
	private static final int yEnterBus = 110;
	private static final int xLeaveBus = 140;
	private static final int yLeaveBus = 170;
	private static final int W = 20, H = 20;

	public BusPassengerGui(PersonGui pg, BusPassenger bp, boolean entering) {
		this.bp = bp;
		if (entering) {
			xPos = xOutside;
			yPos = yOutside;
		}
		else {
			xPos = xLeaveBus;
			yPos = yLeaveBus;
		}
		xDestination = xOutside;
		yDestination = yOutside;
	}
	
	@Override
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
			if (command == Command.GoToMachine) bp.msgAtMachine();
			else if (command == Command.GetInLine) bp.msgInLine();
			else if (command == Command.GetOnBus) bp.msgOnBus();
			else if (command == Command.LeaveStation) bp.msgLeftStop();
			command=Command.noCommand;
		}
	}

	@Override
	public void draw(Graphics2D g) {
        g.setColor(Color.GREEN);
        g.fillRect(xPos, yPos, W, H);
	}

	@Override
	public boolean isPresent() {
		return isPresent;
	}
	
	public void DoGoToMachine() {
		xDestination = 50;
		yDestination = 250;
		command = Command.GoToMachine;
	}
	public void DoGoToLine(int spot) {
		// for now, there will never be > 10, which is the max that will fit in 450 pixels
		int col = spot % 10;
		xDestination = 175 + (col * 25);
		yDestination = yEnterBus;
		command = Command.GetInLine;
	}
	public void DoGetOnBus() {
		xDestination = xEnterBus;
		yDestination = yEnterBus;
		command = Command.GetOnBus;
	}
	public void DoLeaveStop() {
		xDestination = xOutside;
		yDestination = yOutside;
		command = Command.LeaveStation;
	}

}
