package SimCity;

import java.awt.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.ImageIcon;

import restaurant.CustomerAgent.AgentEvent;
import SimCity.CityPanel.ParkingCoordinates;
import SimCity.CityPanel.ParkingSpot;
import SimCity.PersonAgent.Coordinates;

public class CarGui implements Gui
{
	ImageIcon imgleft,imgright,imgup,imgdown;
	Image ileft,iright,iup,idown;
	String location;
	CityPanel cityPanel;
	ParkingCoordinates pc;
	private PersonAgent agent = null;
	private int buildingx,buildingy;
	private boolean isPresent = true;
	static final int width = 50, height = 20, width2 = 20,height2 = 50;
    public Map<String,Coordinates> locations;
    Vector<Integer> hroad = new Vector<Integer>();
    Vector<Integer> vroad = new Vector<Integer>();
    boolean roadTravel = false,secondroute=false;
    int speed = 2;
    int xhome,yhome;
    private int finalx,finaly;
	int roadx;
	int roady;
	private int tempx;
	private int tempy;
	public int xPos, yPos;
	int xDestination,yDestination;
	boolean parking = false,stop = false,left,right,up,down,collision = false;
	boolean[] bools = new boolean[4];
	Rectangle collided,collided2;
	Timer timer = new Timer();
	
	public CarGui(PersonAgent p, Map<String,Coordinates> locations, CityPanel c)
	{ 
		bools[0] = left;
		bools[1] = right;
		bools[2] = up;
		bools[3] = down;
		Arrays.fill(bools,Boolean.TRUE);
		
		vroad.add(5);
		for (int i=0;i<4;i++)
		{
			vroad.add(212+(160*i));
		}
		vroad.add(902);
		
		hroad.add(15);
		for (int i=0;i<3;i++)
		{
			hroad.add(165+(150*i));
		}
		hroad.add(645);

		cityPanel = c;
		imgleft = new ImageIcon("Images/carleft.png");
		imgright = new ImageIcon("Images/carright.png");
		imgup = new ImageIcon("Images/cartopup.png");
		imgdown = new ImageIcon("Images/cartopdown.png");

		ileft = imgleft.getImage();
		iright = imgright.getImage();
		iup = imgup.getImage();
		idown = imgdown.getImage();
		
		this.locations = locations;
		this.agent = p;
		xPos = 110;
		yPos = 360;
		xDestination = 110;
		yDestination = 360;
	}
	public CarGui(int x, int y, CityPanel c)
	{ 
		vroad.add(5);
		for (int i=0;i<4;i++)
		{
			vroad.add(215+(160*i));
		}
		vroad.add(905);
		
		hroad.add(15);
		for (int i=0;i<3;i++)
		{
			hroad.add(165+(150*i));
		}
		hroad.add(645);
		
		xPos = x;
		xDestination = x;
		yPos = y;
		yDestination = y;
		cityPanel = c;
		imgleft = new ImageIcon("Images/carleft.png");
		imgright = new ImageIcon("Images/carright.png");
		ileft = imgleft.getImage();
		iright = imgright.getImage();
	}
	public void IntersectionWait2(Rectangle r)
	{
		if (collided2 != r)
		{
			if (cityPanel.topCollision(r,this))
			{
				if (cityPanel.downCollision(r,this))
				{
					left = false;
					right = false;
					up = false;
					down = true;
				}
				else
				{
					left = false;
					right = false;
					up = true;
					down = false;
				}
			}
			else
			{
				if (cityPanel.rightCollision(r,this))
				{
					left = false;
					right = true;
					up = false;
					down = false;
				}
				else
				{
					left = true;
					right = false;
					up = false;
					down = false;
				}
			}
			collision = true;
			collided2 = r;
			tempx = xDestination;
			tempy = yDestination;
			xDestination = xPos;
			yDestination = yPos;
			stop = true;
			timer.schedule(new TimerTask() 
			{
				public void run() 
				{
					xDestination = tempx;
					yDestination = tempy;
					stop = false;
					collision = false;
				}
			},3000);
		}
	}
	public void IntersectionWait(Rectangle r)
	{
		if (collided != r)
		{
			if (cityPanel.topCollision(r,this))
			{
				if (cityPanel.downCollision(r,this))
				{
					left = false;
					right = false;
					up = false;
					down = true;
				}
				else
				{
					left = false;
					right = false;
					up = true;
					down = false;
				}
			}
			else
			{
				if (cityPanel.rightCollision(r,this))
				{
					left = false;
					right = true;
					up = false;
					down = false;
				}
				else
				{
					left = true;
					right = false;
					up = false;
					down = false;
				}
			}
			collision = true;
			collided = r;
			tempx = xDestination;
			tempy = yDestination;
			xDestination = xPos;
			yDestination = yPos;
			stop = true;
			timer.schedule(new TimerTask() 
			{
				public void run() 
				{
					xDestination = tempx;
					yDestination = tempy;
					stop = false;
					collision = false;
				}
			},3000);
		}
	}
	public void setBool(boolean b)
	{
		for (int i=0;i<bools.length;i++)
		{
			if (bools[i] == b)
			{
				bools[i] = true;
			}
			else
			{
				bools[i] = false;
			}
		}
	}
	public void updatePosition() 
	{
		if (xPos < xDestination)
			xPos+=speed;
		else if (xPos > xDestination)
			xPos-=speed;

		if (yPos < yDestination)
			yPos+=speed;
		else if (yPos > yDestination)
			yPos-=speed;

		if (roadTravel && !stop)
		{
			if (range(xPos,xDestination) && range(yPos,yDestination) && !secondroute)
			{
				roadx = minX(finalx);
				roady = minY(finaly);
				if (!chooseVerticleRoute())
				{
					xDestination = minX(xPos);
					yDestination = roady;
				}
				else
				{
					xDestination = roadx;
					yDestination = minY(yPos);
				}
				secondroute = true;
			}
			if (range(xPos,xDestination) && range(yPos,yDestination) && secondroute)
			{
				xDestination = finalx;
				yDestination = finaly;
				secondroute = false;
				roadTravel = false;
			}
		}
		else if (!roadTravel)
		{
			if (range(xPos,xDestination) && range(yPos,yDestination) && xPos != xhome && yPos != yhome && !parking)
			{
				agent.gui.outOfCar();
			}
		}
	}

	public void draw(Graphics2D g)
	{
		if (collision)
		{
			if (up)
			{
				g.drawImage(iup, xPos, yPos, width2, height2, cityPanel);
			}
			else if (down)
			{
				g.drawImage(idown, xPos, yPos, width2, height2, cityPanel);
			}
			else if (right)
			{
				g.drawImage(iright, xPos, yPos, width, height, cityPanel);
			}
			else if (left)
			{
				g.drawImage(ileft, xPos, yPos, width, height, cityPanel);
			}
		}
		else
		{
			if (yDestination == roady)
			{
				if (yDestination <= yPos)
				{
					if (xDestination <= xPos)
					{
						g.drawImage(iup, xPos, yPos, width2, height2, cityPanel);
					}
					else
					{
						g.drawImage(iup, xPos, yPos, width2, height2, cityPanel);
					}
				}
				else
				{
					g.drawImage(idown, xPos, yPos, width2, height2, cityPanel);
				}
			}
			else 
			{
				if (xDestination >= xPos)
				{
					g.drawImage(iright, xPos, yPos, width, height, cityPanel);
				}
				else if (xDestination <= xPos)
				{
					g.drawImage(ileft, xPos, yPos, width, height, cityPanel);
				}
			}
		}
	}
	public void setPos(int x, int y)
	{
		xPos = x;
		xDestination = x;
		yPos = y;
		yDestination = y;
	}
	public boolean range(int pos, int value)
	{
		return (pos >= value-speed && pos <= value+speed);
	}
	public void setDestination (int x, int y)
	{
		finalx = x;
		finaly = y;
		roadx = minX(x);
		roady = minY(y);
		if (chooseVerticleRoute())
		{
			xDestination = minX(xPos);
			yDestination = roady;
		}
		else
		{
			xDestination = roadx;
			yDestination = minY(yPos);
		}
		roadTravel = true;
	}
	public void setDestination (int x)
	{
		xPos = x;
		yPos = x;
		xDestination = x;
		yDestination = x;
	}
	public boolean chooseVerticleRoute()
	{
		if (Math.abs(yPos-finaly) < Math.abs(xPos-finalx))
		{
			return true;
		}
		return false;
	}
	public int minX(int x)
	{
		int min = 999,road=0;
		for (int i=0;i<vroad.size();i++)
		{
			//System.out.println(Math.abs(vroad.get(i)-x));
			if (Math.abs(vroad.get(i)-x) < min)
			{
				min = Math.abs(vroad.get(i)-x);
				road = vroad.get(i);
			}
		}
		return road;
	}
	
	public int minY (int y)
	{
		int min = 999,road=0;
		for (int i=0;i<hroad.size();i++)
		{
			//System.out.println(Math.abs(vroad.get(i)-y));
			if (Math.abs(hroad.get(i)-y) < min)
			{
				min = Math.abs(hroad.get(i)-y);
				road = hroad.get(i);
			}
		}
		return road;
	}
	public int getPos()
	{
		return xPos;
	}
	public void setXY(int x, int y)
	{
		xPos = x;
		yPos = y;
		xDestination = x;
		yDestination = y;
	}
	public void setHome(int x, int y)
	{
		xhome = x;
		yhome = y;
	}
	public void setDestination(int x,int y,String location, int i)
	{
        xDestination = x;
        yDestination = y;
        buildingx = x;
        buildingy = y;
        this.location = location;
	}
	public void setDestination(int x,int y,String location,ParkingCoordinates park)
	{
		pc = park;
		buildingx = x;
		buildingy = y;
		this.location = location;
		
		finalx = x;
		finaly = y;
		roadx = minX(x);
		roady = minY(y);
		if (chooseVerticleRoute())
		{
			xDestination = minX(xPos);
			yDestination = roady;
		}
		else
		{
			xDestination = roadx;
			yDestination = minY(yPos);
		}
		roadTravel = true;
	}
	public void setDestination(int x,int y,String location)
	{
		buildingx = x;
		buildingy = y;
		this.location = location;
		
		finalx = x;
		finaly = y;
		roadx = minX(x);
		roady = minY(y);
		if (chooseVerticleRoute())
		{
			xDestination = minX(xPos);
			yDestination = roady;
		}
		else
		{
			xDestination = roadx;
			yDestination = minY(yPos);
		}
		roadTravel = true;
	}
	public void backToResident()
	{
		location = "Resident";
		xPos = buildingx;
		yPos = buildingy;
		if (Integer.parseInt(agent.name)%5 == 0)
		{
			buildingx = locations.get("Resident").x;
			buildingy = locations.get("Resident").y;
		}
		else
		{
			buildingx = locations.get("Resident"+Integer.parseInt(agent.name)%5).x;
			buildingy = locations.get("Resident"+Integer.parseInt(agent.name)%5).y;
		}
		Coordinates loc; 
		if (Integer.parseInt(agent.name)%5 == 0)
		{
			loc = locations.get("Resident");
		}
		else
		{
			loc = locations.get("Resident"+Integer.parseInt(agent.name)%5);
		}
		xDestination = loc.x;
		yDestination = loc.y;
	}
	public boolean isMoving()
	{
		if (xPos != xDestination || yPos != yDestination)
		{
			return true;
		}
		return  false;
	}
	public boolean isPresent() 
	{
		return isPresent;
	}
	public void setPresent(boolean p) 
	{
		isPresent = p;
	}
	public void intoBuilding()
	{
		xPos = -50;
		yPos = -50;
		xDestination = -50;
		yDestination = -50;
	}
}