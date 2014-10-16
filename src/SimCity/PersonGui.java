package SimCity;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import Role.ResidentRole;
import Role.Role;
import SimCity.CityPanel.ParkingCoordinates;
import SimCity.CityPanel.ParkingSpot;
import SimCity.PersonAgent.Coordinates;

public class PersonGui implements Gui
{
	boolean resolving = false,wait = false;
	CityPanel cityPanel;
	String location;
	CarGui car;
	PersonAgent agent = null;
	private int buildingx,buildingy;
	private boolean isPresent = true;
	private boolean isHungry = false;
	static final int width = 20, height = 20;
    int[] table = new int[3]; //store table xpositions inside an array for easy access
    public Map<String,Coordinates> locations;
    public Map<Integer,String> restaurants = new HashMap<Integer,String>();
    private int xcarDest,ycarDest;
    String carStr;
	public int xPos, yPos;
	int xDestination;
	int yDestination;
	int speed=1;
	boolean ownsCar = false,ownsHome = false,unemployed = false;

	public static final int yTable = 150;

	public PersonGui(PersonAgent p, Map<String,Coordinates> locations, int num)
	{ 
		this.locations = locations;
		this.agent = p;
		xPos = 200*num;
		yPos = 100;
		xDestination = 200*num;
		yDestination = 100;
        for (int i=0;i<3;i++)
        {
        	table[i] =(i+1)*100; 
        }
        restaurants.put(0,"Restaurant");
        restaurants.put(1,"SidRestaurant");
        restaurants.put(2,"RanchoRestaurant");
        restaurants.put(3,"AndrewRestaurant");
        restaurants.put(4,"AmyRestaurant");
	}
	public PersonGui(PersonAgent p, Map<String,Coordinates> locations, int x, int y)
	{ 
		this.locations = locations;
		this.agent = p;
		xPos = x;
		yPos = y;
		xDestination = x;
		yDestination = y;
        for (int i=0;i<3;i++)
        {
        	table[i] =(i+1)*100; 
        }
        restaurants.put(0, "Restaurant");
        restaurants.put(1,"SidRestaurant");
        restaurants.put(2,"RanchoRestaurant");
        restaurants.put(3,"AndrewRestaurant");
        restaurants.put(4,"AmyRestaurant");
	}
	public PersonGui()
	{ 
		xPos = 100;
		yPos = 100;
		xDestination = 100;
		yDestination = 100;
        for (int i=0;i<3;i++)
        {
        	table[i] =(i+1)*100; 
        }
        restaurants.put(1,"SidRestaurant");
        restaurants.put(2,"RanchoRestaurant");
        restaurants.put(3,"AndrewRestaurant");
        restaurants.put(4,"AmyRestaurant");
	}
	public void updatePosition() 
	{
		if (xPos < xDestination)
			xPos +=speed;
		else if (xPos > xDestination)
			xPos -=speed;

		if (yPos < yDestination)
			yPos +=speed;
		else if (yPos > yDestination)
			yPos -=speed;
		
		if (wait)
		{
			if (xPos == xDestination && yPos == yDestination)
			{
				intoCar();
				//car.xPos = car.xhome;
				//car.yPos = car.yhome;
				wait = false;
			}
		}
		if (ownsCar && !cityPanel.accident)
		{
			if (range(xDestination,car.xPos) && range(yDestination,car.yPos))
			{
				if (!car.parking)
				{
					ParkingSpot p = cityPanel.parking.get(carStr);
					if (!NoParkingLeft(p))
					{
						for (ParkingCoordinates pc:p.parkingspot)
						{
							if (!pc.occupied)
							{
								car.setDestination(pc.x, pc.y-10, carStr,pc);
								pc.occupied = true;
								intoCar();
								break;
							}
						}
					}
					else
					{
						backToResident();
						car.setDestination(xcarDest-30, ycarDest, carStr);
					}
				}
			}
			if (range(xDestination,car.xPos) && range(yDestination,car.yPos))
			{
				car.setDestination(xcarDest-30, ycarDest, carStr);
				intoCar();
			}
		}
	}
	public boolean NoParkingLeft(ParkingSpot p)
	{
		int count=0;
		for (ParkingCoordinates pc:p.parkingspot)
		{
			if (pc.occupied)
			{
				count++;
			}
		}
		if (count == 6)
		{
			return true;
		}
		return false;
	}
	public void outOfCar()
	{
		xPos = car.xPos;
		yPos = car.yPos;
		xDestination = xcarDest;
		yDestination = ycarDest;
		car.parking = true;
		//System.out.println(xPos+" "+yPos+" "+xDestination+" "+yDestination);
	}
	public void intoCar()
	{
		xPos = -50;
		yPos = -50;
		xDestination = -50;
		yDestination = -50;
	}
	public void draw(Graphics2D g) 
	{
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, width, height);
	}
	public void setPos(int pos)
	{
		xDestination = pos;
	}
	public int getPos()
	{
		return xPos;
	}
	public void setDestination(int x,int y,String location)
	{
		if (buildingx != 0 && buildingy != 0)
		{
			xPos = buildingx;
			yPos = buildingy;
		}
		xDestination = x;
		yDestination = y;
		buildingx = x;
		buildingy = y;
		this.location = location;
	}
	public void setDestination(int x,int y, int xd, int yd)
	{
		if (cityPanel.accident && !resolving)
		{
			resolving = true;
			xPos = x;
			yPos = y;
			xDestination = xd;
			yDestination = yd;
		}
	}
	public void setDestination(int x,int y)
	{
		xDestination = x;
		yDestination = y;
		wait = true;
	}
	public void goToRestaurant() 
	{
		residentRole().active = false;
		location = "Restaurant";
		xPos = buildingx;
		yPos = buildingy;
		Coordinates loc; 
		int i = Integer.parseInt(agent.name)%5;
		loc = locations.get(restaurants.get(i));
		buildingx = loc.x;
		buildingy = loc.y;

		if (ownsCar)
		{
			if (car.xPos == car.xhome && car.yPos == car.yhome)
			{
				useCar(loc.x,loc.y,restaurants.get(i));
			}
			else
			{
				xDestination = loc.x;
				yDestination = loc.y;
			}
		}
		else
		{
			xDestination = loc.x;
			yDestination = loc.y;
		}

	}
	public Role residentRole()
	{
		for (Role r:agent.roles)
		{
			if (r instanceof ResidentRole)
			{
				return r;
			}
		}
		return null;
	}
	public void backToResident()
	{
		if (ownsHome)
		{
			location = "Resident";
			xPos = buildingx;
			yPos = buildingy;
			Coordinates loc; 
			if (Integer.parseInt(agent.name)%5 == 0)
			{
				loc = locations.get("Resident");
				buildingx = locations.get("Resident").x;
				buildingy = locations.get("Resident").y;
			}
			else if(Integer.parseInt(agent.name)%5 == 4)
			{
				loc = locations.get("Apartment");
				buildingx = locations.get("Apartment").x;
				buildingy = locations.get("Apartment").y;
			}
			else
			{
				loc = locations.get("Resident"+Integer.parseInt(agent.name)%5);
				buildingx = locations.get("Resident"+Integer.parseInt(agent.name)%5).x;
				buildingy = locations.get("Resident"+Integer.parseInt(agent.name)%5).y;
			}
			if (ownsCar)
			{
				if (car.xPos != car.xhome && car.yPos != car.yhome)
				{
					useCar(loc.x,loc.y,"Resident");
				}
				else
				{
					xDestination = loc.x;
					yDestination = loc.y;
				}
			}
		}
	}
	public void backToResident(Class<?> c)
	{
		customerRole(c).active = false;
		if (ownsHome)
		{
			location = "Resident";
			xPos = buildingx;
			yPos = buildingy;
			Coordinates loc; 
			if (Integer.parseInt(agent.name)%5 == 0)
			{
				loc = locations.get("Resident");
				buildingx = locations.get("Resident").x;
				buildingy = locations.get("Resident").y;
			}
			else if(Integer.parseInt(agent.name)%5 == 4)
			{
				loc = locations.get("Apartment");
				buildingx = locations.get("Apartment").x;
				buildingy = locations.get("Apartment").y;
			}
			else
			{
				loc = locations.get("Resident"+Integer.parseInt(agent.name)%5);
				buildingx = locations.get("Resident"+Integer.parseInt(agent.name)%5).x;
				buildingy = locations.get("Resident"+Integer.parseInt(agent.name)%5).y;
			}
			if (ownsCar)
			{
				if (car.xPos != car.xhome && car.yPos != car.yhome)
				{
					useCar(loc.x,loc.y,"Resident");
				}
				else
				{
					xDestination = loc.x;
					yDestination = loc.y;
				}
			}
		}
	}
	public boolean range(int pos, int value)
	{
		return (pos >= value-speed && pos <= value+speed);
	}
	public Role customerRole(Class<?> c)
	{
		for (Role r:agent.roles)
		{
			if (c.isInstance(r))
			{
				return r;
			}
		}
		return null;
	}
	public void useCar(int x, int y, String s)
	{
		xcarDest = x;
		ycarDest = y;
		carStr = s; //compute car distance to determine appropriateness
		xDestination = car.xPos;
		yDestination = car.yPos;
		car.parking = false;
		if (car.pc != null)
		{
			car.pc.occupied = false;
		}
	}
	public void setCar(CarGui g)
	{
		car = g;
	}
	public boolean isPresent() 
	{
		return isPresent;
	}
	public boolean isHungry() 
	{
		return isHungry;
	}
	public void setPresent(boolean p) 
	{
		isPresent = p;
	}
	public void setFullStomach()
	{
		isHungry = false;
	}
	public void intoBuilding()
	{
		xPos = -50;
		yPos = -50;
		xDestination = -50;
		yDestination = -50;
	}
	
	public PersonAgent getAgent(){
		return agent;
	}
	
}
