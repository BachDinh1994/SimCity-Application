package SimCity;

import java.awt.*;
import java.util.HashMap;

import javax.swing.ImageIcon;

import busstop.BusAgent;
import SimCity.PersonAgent.Coordinates;

public class BusOutsideGui implements Gui
{
	ImageIcon imgleft,imgright;
	Image ileft,iright;
	CityPanel cityPanel;
	private BusAgent agent = null;
	private boolean isPresent = true;
	static final int width = 50, height = 20;
    public HashMap<String,Coordinates> stops = new HashMap<String, Coordinates>();
    String currentStop;
    Integer destination;

	int xPos;
	int yPos;
	private int xDestination, yDestination;
	private enum Command {noCommand, GoToStop};
	private Command command=Command.noCommand;

	public static final int yTable = 150;

	public BusOutsideGui(BusAgent b, CityPanel c, String stop)
	{ 
		currentStop = stop;
		cityPanel = c;
		imgleft = new ImageIcon("Images/helibusleft.png");
		imgright = new ImageIcon("Images/helibusright.png");
		ileft = imgleft.getImage();
		iright = imgright.getImage();
		stops.put("0", new Coordinates(110, 230));
		stops.put("1", new Coordinates(110, 550));
		stops.put("2", new Coordinates(150+160*4, 230));
		stops.put("3", new Coordinates(150+160*4, 550));
		xPos = stops.get(stop).x;
		yPos = stops.get(stop).y;
		this.agent = b;
		intoBuilding();
	}
	public void updatePosition() 
	{
		if (xPos < xDestination)
			xPos+=5;
		else if (xPos > xDestination)
			xPos-=5;

		if (yPos < yDestination)
			yPos+=5;
		else if (yPos > yDestination)
			yPos-=5;

		if (xPos == xDestination && yPos == yDestination) {
			if (command == Command.GoToStop) {
				currentStop = destination.toString();
				intoBuilding();
			}
			command=Command.noCommand;
		}
	}

	public void draw(Graphics2D g)
	{
		if (xDestination >= xPos)
		{
			g.drawImage(iright, xPos, yPos, width, height, cityPanel);
		}
		else
		{
			g.drawImage(ileft, xPos, yPos, width, height, cityPanel);
		}
	}
	public boolean isPresent() 
	{
		return isPresent;
	}
	public void DoGoToStop(String stop) {
		outOfBuilding();
		destination = Integer.parseInt(stop);
		xDestination = stops.get(stop).x;
		yDestination = stops.get(stop).y;
		command=Command.GoToStop;
	}
	public void outOfBuilding()
	{
		xPos = stops.get(currentStop).x;
		yPos = stops.get(currentStop).y;
		xDestination = stops.get(currentStop).x;
		yDestination = stops.get(currentStop).y;
	}
	public void intoBuilding()
	{
		agent.msgArrivedAtStop(currentStop);
		xPos = -50;
		yPos = -50;
		xDestination = -50;
		yDestination = -50;
	}
}
