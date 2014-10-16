package SimCity;
import javax.swing.*;

import amyRestaurant.AmyCustomerRole;
import amyRestaurant.AmyNormalWaiterRole;
import amyRestaurant.AmyWaiterRole;
import bank.BankAgent;
import bank.BankCustomer;
import bank.BankHost;
import bank.BankRobber;
import bank.gui.BCustomerGui;
import bank.gui.HostGui;
import bank.gui.TellerGui;
import busstop.BusAgent;
import busstop.BusPassengerRole;
import busstop.TicketMachineAgent;
import busstop.gui.BusInsideGui;
import busstop.gui.BusPassengerGui;
import busstop.interfaces.Bus;
import busstop.interfaces.BusPassenger;
import busstop.interfaces.TicketMachine;
import SimCity.PersonAgent.Coordinates;
import SimCity.interfaces.Person;
import restaurant.CashierAgent;
import restaurant.CookAgent;
import restaurant.HostAgent;
import market.MarketAgent;
import restaurant.WaiterAgent;
import restaurant.gui.CookGui;
import restaurant.gui.CustomerGui;
import restaurant.gui.FoodGui;
import restaurant.gui.WaiterGui;
import restaurant.interfaces.Customers;
import restaurant_andrew.AndrewCustomerRole;
import restaurant_andrew.AndrewNormalWaiterRole;
import restaurant_andrew.AndrewWaiterRole;
import restaurant_rancho.RanchoCustomerRole;
import restaurant_rancho.RanchoManager;
import restaurant_rancho.RanchoNormalWaiterRole;
import restaurant_rancho.RanchoWaiterRole;
import restaurant_sid.SidCustomerRole;
import restaurant_sid.SidNormalWaiterRole;
import restaurant_sid.SidWaiterRole;
import Role.CustomerRole;
import Role.Landlord;
import Role.NormalWaiterRole;
import Role.ResidentRole;
import Role.Role;
import Role.WaiterRole;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.Timer;
import java.util.TimerTask;

//citypanel creates the city


@SuppressWarnings("serial")
public class CityPanel extends JPanel implements MouseListener, ActionListener 
{
	int accidentx=-50, accidenty=-50;
	boolean accident = false;
	JLabel population = new JLabel();
	JLabel numCar = new JLabel();
	JLabel numHome = new JLabel();
	JLabel unemployed = new JLabel();
	SimCityTest c;
    Vector<Integer> hroad = new Vector<Integer>();
    Vector<Integer> vroad = new Vector<Integer>();
    public Map<Integer,String> restaurants = new HashMap<Integer,String>();
    public Map<Integer,String> homes = new HashMap<Integer,String>();
	//Restaurant variables
	private CarGui car;
	static int xsize=50,ysize=50;
    public CookAgent cook = new CookAgent("Cook");
    public CashierAgent cashier = new CashierAgent("Cashier");
	public HostAgent host;
    public WaiterAgent waiter;
    private WaiterGui waiterGui;
    private FoodGui foodGui;
    private CookGui cookGui;
    private Clock clock;
    public Vector<Customers> customers;
    public Vector<WaiterAgent> waiters;
   // public Vector<MarketAgent> markets;
    
    //Bank Agents and Guis
    public BankHost bHost;
    public HostGui bHostGui;
    public TellerGui bTellerGui;
    public BCustomerGui bCustomerGui;
    
    //Bank Vectors
    public Vector<BankCustomer> bankCustomers;
    public Vector<BankAgent> bankTellers;
    
    //Bus Stop Vectors
    public Vector<BusPassengerRole> busPassengers = new Vector<BusPassengerRole>();
    public Vector<BusAgent> buses = new Vector<BusAgent>();
    public Vector<TicketMachineAgent> ticketMachines = new Vector<TicketMachineAgent>();
    
	public Map<String,Coordinates> locations = new HashMap<String,Coordinates>();
	public Map<String,ParkingSpot> parking = new HashMap<String,ParkingSpot>();
	ArrayList<DeliveryTruckInCityGui> trucks = new ArrayList<DeliveryTruckInCityGui>();

	ArrayList<Building> buildings;
	Vector<PersonAgent> people = new Vector<PersonAgent>();
	PersonAgent person;
	PersonGui personGui;

	ImageIcon vertroad = new ImageIcon("Images/vertroadsection.png");
	ImageIcon horizroad = new ImageIcon("Images/horizroadsection.png");

	Image imgvroad = vertroad.getImage();
	Image imghroad = horizroad.getImage();
	

	int ranchoRestaurantRoleCount = 0;
	int landlordCount = 0;

	Vector<Gui> test = new Vector<Gui>();
	Vector<Rectangle> intersection = new Vector<Rectangle>();
	Rectangle r; 
	
	public static class ParkingCoordinates
	{
		boolean occupied;
		public ParkingCoordinates(int i, int j) 
		{
			x=i;
			y=j;
			occupied = false;
		}
		int x;  
		int y;
	}
	public class ParkingSpot
	{
		Vector<ParkingCoordinates> parkingspot = new Vector<ParkingCoordinates>();
	}

	public CityPanel(Clock cl) 
	{
		restaurants.put(0,"Restaurant");
		restaurants.put(1,"SidRestaurant");
        restaurants.put(2,"RanchoRestaurant");
        restaurants.put(3,"AndrewRestaurant");
        restaurants.put(4,"AmyRestaurant");
        
        homes.put(0,"Resident");
        homes.put(1,"Resident1");
        homes.put(2,"Resident2");
        homes.put(3,"Resident3");
        homes.put(4,"Apartment");
	        
		population.setText("Population: "+people.size());
		numCar.setText("Number of car owners: "+numCars());
		numHome.setText("Number of home owners: "+numHomes());
		unemployed.setText("Unemployed population: "+numUnemployed());
		setLayout(new FlowLayout());
		add(population);
		add(numCar);
		add(numHome);
		add(unemployed);
		
		vroad.add(10);
		for (int i=0;i<4;i++)
		{
			vroad.add(220+(160*i));
		}
		vroad.add(910);
		
		hroad.add(15);
		for (int i=0;i<3;i++)
		{
			hroad.add(165+(150*i));
		}
		hroad.add(645);
		
		for (int i=0;i<vroad.size();i++)
        {
        	for (int j=0;j<hroad.size();j++)
        	{
        		r = new Rectangle(vroad.get(i)-10,hroad.get(j)-15,50,50);
        		intersection.add(r);
        	}
        }
		
		CarGui c1 = new CarGui(360,200,this);
		c1.setDestination(790,200);
		test.add(c1);
		
		clock = cl;
        buildings = new ArrayList<Building>();
        Building b,b1 = null,b2 = null,b3 = null;
        for (int i=0;i<5;i++)
        {
        	if (i==0)
        	{
                b = new Building(110,70,xsize,ysize,"Restaurant");
                b1 = new Building(110,230,xsize,ysize,"BusStop");
                b2 = new Building(110,390,xsize,ysize,"Resident");
                b3 = new Building(110,550,xsize,ysize,"BusStop");
        	} 
        	else if (i==4)
        	{
        		b = new Building(150+160*i,70,xsize,ysize,"AmyRestaurant");
                b1 = new Building(150+160*i,230,xsize,ysize,"BusStop"+i);
                //b2 = new Building(150+160*i,390,xsize,ysize,"Resident"+i);
                b2 = new Building(150+160*i,390,xsize,ysize,"Apartment");

                b3 = new Building(150+160*i,550,xsize,ysize,"BusStop"+i);
        	}
        	else
        	{
        		if (i==1)
        		{
                    b = new Building(130+160*i,70,xsize,ysize,"SidRestaurant");
        		}
        		else if (i==2)
        		{
        			b = new Building(130+160*i,70,xsize,ysize,"RanchoRestaurant");
        		}
        		else
        		{
        			b = new Building(130+160*i,70,xsize,ysize,"AndrewRestaurant");
        		}
                b1 = new Building(130+160*i,230,xsize,ysize,"Bank"+i);
                b2 = new Building(130+160*i,390,xsize,ysize,"Resident"+i);
                b3 = new Building(130+160*i,550,xsize,ysize,"Market"+i);
    
        	}
            buildings.add(b);buildings.add(b1);buildings.add(b2);buildings.add(b3);
        }
		for (Building bd:buildings)
		{
			locations.put(bd.type, new Coordinates(bd.x,bd.y));
		}
		
		//Parking Spot
		
		//Restaurants
   		for (int i=0;i<5;i++)
   		{
   			ParkingSpot p = new ParkingSpot();
   			String s = restaurants.get(i);
   			Coordinates c = locations.get(s);
   	   		for (int j=0;j<3;j++)
   	   		{
   	   			ParkingCoordinates w = new ParkingCoordinates(c.x-30,c.y+10+(j*20));
   	   			ParkingCoordinates z = new ParkingCoordinates(c.x+50,c.y+10+(j*20));
   	   			p.parkingspot.add(w);
   	   			p.parkingspot.add(z);
   	   		}
   	   		parking.put(s,p);
   		}
   		
   		//Banks
   		for (int i=0;i<3;i++)
   		{
   			ParkingSpot p = new ParkingSpot();
   			String s = "Bank"+(i+1);
   			Coordinates c = locations.get(s);
   	   		for (int j=0;j<3;j++)
   	   		{
   	   			ParkingCoordinates w = new ParkingCoordinates(c.x-30,c.y+10+(j*20));
   	   			ParkingCoordinates z = new ParkingCoordinates(c.x+50,c.y+10+(j*20));
   	   			p.parkingspot.add(w);
   	   			p.parkingspot.add(z);
   	   		}
   	   		parking.put(s, p);
   		}
   		
   		//Residents and Apartments
   		for (int i=0;i<5;i++)
   		{
   			ParkingSpot p = new ParkingSpot();
   			String s = homes.get(i);
   			Coordinates c = locations.get(s);
   	   		for (int j=0;j<3;j++)
   	   		{
   	   			ParkingCoordinates w = new ParkingCoordinates(c.x-30,c.y+10+(j*20));
   	   			ParkingCoordinates z = new ParkingCoordinates(c.x+50,c.y+10+(j*20));
   	   			p.parkingspot.add(w);
   	   			p.parkingspot.add(z);
   	   		}
   	   		parking.put(s,p);
   		}
   		
   		//Testing validity
   		/*System.out.println(parking.get("Restaurant").parkingspot.size());
   		System.out.println(parking.get("SidRestaurant").parkingspot.size());
   		System.out.println(parking.get("RanchoRestaurant").parkingspot.size());
   		System.out.println(parking.get("AndrewRestaurant").parkingspot.size());
   		System.out.println(parking.get("AmyRestaurant").parkingspot.size());
		//Parking Spot
		/*for (int i=0;i<50;i++) //50 personAgents
		{
			if (i%2 == 0)
			{
				person = new PersonAgent(""+i, clock);
				person.addRole(new WaiterRole("Bach"));
				person.addRole(new RanchoWaiterRole());
				person.addRole(new AndrewWaiterRole("Andrew"));
				person.addRole(new AmyWaiterRole("Amy"));
				person.addRole(new SidWaiterRole("Sid",i));
			}
			else
			{
				person = new PersonAgent(""+i, clock);
			}
			personGui = new PersonGui(person,locations,i%5);
			person.setGui(personGui);
			person.startThread();
			people.add(person);
	
			car = new CarGui(person,locations,this);
			personGui.setCar(car);
			car.setXY(110+180*i,360);
			car.setHome(110+180*i,360);
			if (i==2)
			{
				car.setXY(90+180*i,360);
				car.setHome(110+180*i,360);
			}
			else if (i==3)
			{
				car.setXY(70+180*i,360);
				car.setHome(110+180*i,360);
			}
			else if (i==4)
			{
				car.setXY(70+180*i,360);
				car.setHome(110+180*i,360);
			}
		}*/
		
        addMouseListener(this);
        javax.swing.Timer timer = new javax.swing.Timer(20, this);
        timer.start();
        for (int i=0;i<people.size();i++)
        {
        	Coordinates c;
        	int choice = (int)(Math.random()*5);
        	if (choice==0)
        	{
        		//c = locations.get("Restaurant");
        		c = locations.get("Resident");
            	//people.get(i).gui.setDestination(c.x,c.y,"Restaurant"); //Fix concurrent animation
            	people.get(i).gui.setDestination(c.x,c.y,"Resident");
        	}
        	else if(choice == 4){
        		c = locations.get("Apartment");
        		people.get(i).gui.setDestination(c.x, c.y, "Apartment");
        	}
        	else
        	{
        		//c = locations.get("Restaurant"+choice);
        		c = locations.get("Resident"+choice);
            	//people.get(i).gui.setDestination(c.x,c.y,"Restaurant"+choice);
            	people.get(i).gui.setDestination(c.x,c.y,"Resident"+choice);
        	}
        }
	}
	public int numUnemployed()
	{
		int count=0;
		for (PersonAgent p:people)
		{
			if (p.gui.unemployed)
			{
				count++;
			}
		}
		return count;
	}
	public int numCars()
	{
		int count=0;
		for (PersonAgent p:people)
		{
			if (p.gui.ownsCar)
			{
				count++;
			}
		}
		return count;
	}
	public int numHomes()
	{
		int count=0;
		for (PersonAgent p:people)
		{
			if (p.gui.ownsHome)
			{
				count++;
			}
		}
		return count;
	}
	public void addPerson()
	{
		person = new PersonAgent(""+people.size(), clock);
		for (JCheckBox b:c.boxes)
		{
			if (b == c.bachwaiter && b.isSelected())
			{
				person.addRole(new WaiterRole("Bach"));
			}
			else if (b == c.sidwaiter && b.isSelected())
			{
				person.addRole(new SidWaiterRole("Sid",people.size()));
			}
			else if (b == c.ranchowaiter && b.isSelected())
			{
				person.addRole(new RanchoWaiterRole());
			}
			else if (b == c.andrewwaiter && b.isSelected())
			{
				person.addRole(new AndrewWaiterRole("Andrew"));
			}
			else if (b == c.amywaiter && b.isSelected())
			{
				person.addRole(new AmyWaiterRole("Amy"));
			}
			else if (b == c.bachnormalwaiter && b.isSelected())
			{
				person.addRole(new NormalWaiterRole("Bach"));
			}
			else if (b == c.sidnormalwaiter && b.isSelected())
			{
				person.addRole(new SidNormalWaiterRole("Sid",people.size()));
			}
			else if (b == c.ranchonormalwaiter && b.isSelected())
			{
				person.addRole(new RanchoNormalWaiterRole());
			}
			else if (b == c.andrewnormalwaiter && b.isSelected())
			{
				person.addRole(new AndrewNormalWaiterRole("Andrew"));
			}
			else if (b == c.amynormalwaiter && b.isSelected())
			{
				person.addRole(new AmyNormalWaiterRole("Amy"));
			} else if (b == c.bankcustomer && b.isSelected())
			{
				person.addRole(new BankCustomer("Sid"));
			} else if (b == c.bankteller && b.isSelected())
			{
				person.addRole(new BankAgent("Sid"));
			} else if (b == c.bankrobber)
			{
				Building bank = null;
				int bankNum = -1;
				for (Building build : buildings){
					if (build.type.substring(0,build.type.length()-1).equals("Bank")){
						bank = build;
						bankNum = Integer.parseInt(build.type.substring(build.type.length()-1));
						break;
					}
				}
				person.addRole(new BankRobber(bank.myBuildingPanel.bankHosts.get(0)));
			}
			/*else if (b == c.bachcook && b.isSelected())
			{
				person.addRole(new CookRole("Bach"));
			}
			else if (b == c.sidcook && b.isSelected())
			{
				person.addRole(new SidCookRole("Sid",people.size()));
			}
			else if (b == c.ranchocook && b.isSelected())
			{
				person.addRole(new RanchoCookRole());
			}
			else if (b == c.andrewcook && b.isSelected())
			{
				person.addRole(new AndrewCookRole("Andrew"));
			}
			else if (b == c.amycook && b.isSelected())
			{
				person.addRole(new AmyCookRole("Amy"));
			}
			else if (b == c.bachmanager && b.isSelected())
			{
				person.addRole(new ManagerRole("Bach"));
			}
			else if (b == c.sidmanager && b.isSelected())
			{
				person.addRole(new SidManagerRole("Sid",people.size()));
			}
			else if (b == c.ranchomanager && b.isSelected())
			{
				person.addRole(new RanchoManagerRole());
			}
			else if (b == c.andrewmanager && b.isSelected())
			{
				person.addRole(new AndrewManagerRole("Andrew"));
			}
			else if (b == c.amymanager && b.isSelected())
			{
				person.addRole(new AmyManagerRole("Amy"));
			}*/
		}
		Coordinates coord;
		String xs = c.personXPos.getText();
		String ys = c.personYPos.getText();
		if (!xs.equals("") && !ys.equals(""))
		{
			int x = Integer.parseInt(c.personXPos.getText());
			int y = Integer.parseInt(c.personYPos.getText());
			personGui = new PersonGui(person,locations,x,y);
		}
		else
		{
			personGui = new PersonGui(person,locations,450,350);
			//personGui = new PersonGui(person,locations,people.size()%5);
		}
		//c.car.setSelected(true);
		//c.home.setSelected(true);
		
		String dest = c.destination.getText();
		coord = locations.get(dest);
		person.setGui(personGui);
		person.startThread();

		//Owns Car
		car = new CarGui(person,locations,this);
		if (c.car.isSelected())
		{
			personGui.setCar(car);
			personGui.ownsCar = true;
		}
		car.setXY(110+180*(people.size()%5),360);
		car.setHome(110+180*(people.size()%5),360);
		//Owns Home
		if (c.home.isSelected())
		{
			personGui.ownsHome = true;
		}
		//Unemployed
		if (c.unemployed.isSelected())
		{
			personGui.unemployed = true;
		}
		
		personGui.cityPanel = this;
		
		String s = c.personSpeed.getText();
		if (!s.equals(""))
		{
			personGui.speed = Integer.parseInt(c.personSpeed.getText());
		}
		s = c.carSpeed.getText();
		if (!s.equals(""))
		{
			car.speed = Integer.parseInt(c.carSpeed.getText());
		}
		
		if (people.size()%5==2)
		{
			car.setXY(90+180*(people.size()%5),360);
			car.setHome(90+180*(people.size()%5),360);
		}
		else if (people.size()%5==3)
		{
			car.setXY(70+180*(people.size()%5),360);
			car.setHome(70+180*(people.size()%5),360);
		}
		else if (people.size()%5==4)
		{
			car.setXY(70+180*(people.size()%5),360);
			car.setHome(70+180*(people.size()%5),360);
		}
		
		/////
		if (!dest.equals(""))
		{
			personGui.setDestination(coord.x,coord.y,dest);
		}
		else
		{
	    	Coordinates c;
	    	int choice = (int)(Math.random()*5);
	    	if (personGui.ownsHome)
	    	{
				if (choice==0)
		    	{
		    		c = locations.get("Resident");
		        	personGui.setDestination(c.x,c.y,"Resident");
		    	}
		    	else if(choice == 4)
		    	{
		    		c = locations.get("Apartment");
		    		personGui.setDestination(c.x, c.y, "Apartment");
		    	}
		    	else
		    	{
		    		c = locations.get("Resident"+choice);
		        	personGui.setDestination(c.x,c.y,"Resident"+choice);
		    	}
	    	}
	    	else
	    	{
	    		c = locations.get("Restaurant");
	        	personGui.setDestination(c.x,c.y,"Restaurant");
	    	}
		}
		people.add(person);
		population.setText("Population: "+people.size());
		numCar.setText("Number of car owners: "+numCars());
		numHome.setText("Number of home owners: "+numHomes());
		unemployed.setText("Unemployed population: "+numUnemployed());
	}
	@SuppressWarnings("static-access")
	public boolean collisionBetween(Rectangle r, CarGui c)
	{
		Rectangle r1;
		r1 = new Rectangle(c.xPos,c.yPos,c.width,c.height);
		if (r != null && r1 != null)
		{
			if (r.intersects(r1))
			{
				return true;
			}
		}
		return false;
	}
	@SuppressWarnings("static-access")
	public boolean rightCollision(Rectangle r, CarGui c)
	{
		if (r.x > c.xPos)
		{
			if (Math.abs(c.xPos+c.width-r.x) <= 3)
			{
				return true;
			}
		}
		else if (r.x < c.xPos)
		{
			if (Math.abs(r.x+r.width-c.xPos) <= 3)
			{
				return false;
			}
		}
		return false;
	}
	@SuppressWarnings("static-access")
	public boolean downCollision(Rectangle r, CarGui c)
	{
		if (r.y > c.yPos)
		{
			if (Math.abs(c.yPos+c.height-r.y) <= 3)
			{
				return true;
			}
		}
		else if (r.y < c.yPos)
		{
			if (Math.abs(r.y+r.height-c.yPos) <= 3)
			{
				return false;
			}
		}
		return false;
	}
	public boolean collisionBetween(Gui g1,Gui g2)
	{
		Rectangle r1 = convertGuiRectClass(g1);
		Rectangle r2 = convertGuiRectClass(g2);
		if (r1 != null && r2 != null)
		{
			if (r1.intersects(r2))
			{
				return true;
			}
		}
		return false;
	}
	@SuppressWarnings("static-access")
	public boolean sideCollision(PersonGui g1, PersonGui g2)
	{
		if (g1.xPos < g2.xPos)
		{
			if (g1.xPos+g1.width == g2.xPos)
			{
				return true;
			}
		}
		else
		{
			if (g2.xPos+g2.width == g1.xPos)
			{
				return true;
			}
		}
		return false;
	}
	public boolean checkCarSideCollision(CarGui c1, CarGui c2, int s)
	{
		/*if (c1.isMoving() && !c2.isMoving())
		{
			c1.yPos += s;
			c1.setDestination(c1.xDestination, c1.yDestination+s, c1.location,0);
		}
		else if (!c1.isMoving() && c2.isMoving())
		{
			c2.yPos += s;
			c2.setDestination(c2.xDestination, c2.yDestination+s, c2.location,0);
		}*/
		if (c1.isMoving() && c2.isMoving() && c1.xPos <= 950 && c1.yPos <= 700)
		{
			//System.out.println("Car Crash");
			return true;
		}
		return false;
	}
	public boolean checkCarTopCollision(CarGui c1, CarGui c2, int s)
	{
		/*if (c1.isMoving() && !c2.isMoving())
		{
			c1.xPos += s;
			c1.setDestination(c1.xDestination+s, c1.yDestination, c1.location,0);
		}
		else if (!c1.isMoving() && c2.isMoving())
		{
			c2.xPos += s;
			c2.setDestination(c2.xDestination+s, c2.yDestination, c2.location,0);
		}*/
		if (c1.isMoving() && c2.isMoving() && c1.xPos <= 950 && c1.yPos <= 700)
		{
			//System.out.println("Car Crash");
			return true;
		}
		return false;
	}
	public void checkTopCollision(PersonGui p1, PersonGui p2, int s)
	{
		if (p1.xPos > p2.xPos)
		{
			if (p1.xDestination > p2.xDestination)
			{
				p1.xPos += s;
				p2.xPos -= s;
			}
			else
			{
				p1.xPos -= s;
				p2.xPos += s;
			}
		}
		else
		{
			if (p1.xDestination < p2.xDestination)
			{
				p1.xPos -= s;
				p2.xPos += s;
			}
			else
			{
				p1.xPos += s;
				p2.xPos -= s;
			}
		}
	}
	public void checkSideCollision(PersonGui p1, PersonGui p2, int s)
	{
		if (p1.yPos > p2.yPos)
		{
			p1.yPos += s;
			if (p2.xPos < p2.xDestination)
			{
				p2.xPos +=s;
			}
			else
			{
				p2.xPos -= s;
			}
		}
		else
		{
			p2.yPos += s;
			if (p1.xPos < p1.xDestination)
			{
				p1.xPos +=s;
			}
			else
			{
				p1.xPos -= s;
			}
		}
	}
	@SuppressWarnings("static-access")
	public boolean topCollision(PersonGui g1, PersonGui g2)
	{
		if (g1.yPos > g2.yPos)
		{
			if (Math.abs(g2.yPos+g2.height-g1.yPos) <= 3)
			{
				return true;
			}
		}
		else if (g1.yPos < g2.yPos)
		{
			if (Math.abs(g1.yPos+g1.height-g2.yPos) <= 3)
			{
				return true;
			}
		}
		return false;
	}
	@SuppressWarnings("static-access")
	public boolean topCollision(Rectangle g1, CarGui g2)
	{
		if (g1.y > g2.yPos)
		{
			if (Math.abs(g2.yPos+g2.height-g1.y) <= 3)
			{
				return true;
			}
		}
		else if (g1.y < g2.yPos)
		{
			if (Math.abs(g1.y+g1.height-g2.yPos) <= 3)
			{
				return true;
			}
		}
		return false;
	}
	@SuppressWarnings("static-access")
	public boolean topCollision(CarGui g1, CarGui g2)
	{
		if (g1.yPos > g2.yPos)
		{
			if (Math.abs(g2.yPos+g2.height-g1.yPos) <= 5)
			{
				return true;
			}
		}
		else if (g1.yPos < g2.yPos)
		{
			if (Math.abs(g1.yPos+g1.height-g2.yPos) <= 5)
			{
				return true;
			}
		}
		return false;
	}
	public Gui convertGuiClass(Gui g)
	{
		if (g instanceof PersonGui)
		{
			return (PersonGui)g;
		}
		else if (g instanceof CarGui)
		{
			return (CarGui)g;
		}
		else if (g instanceof BusOutsideGui)
		{
			return (BusOutsideGui)g;
		}
		else if (g instanceof DeliveryTruckInCityGui)
		{
			return (DeliveryTruckInCityGui)g;
		}
		return null;
	}
	@SuppressWarnings("static-access")
	public Rectangle convertGuiRectClass(Gui g)
	{
		if (g instanceof PersonGui)
		{
			PersonGui p = (PersonGui)g;
			if (!(p.xPos < 0 && p.yPos < 0))
			{
				return new Rectangle(p.xPos,p.yPos,p.width,p.height);
			}
		}
		else if (g instanceof CarGui)
		{
			CarGui c = (CarGui)g;
			if (!(c.xPos < 0 && c.yPos < 0))
			{
				return new Rectangle(c.xPos,c.yPos,c.width,c.height);
			}
		}
		else if (g instanceof BusOutsideGui)
		{
			BusOutsideGui b = (BusOutsideGui)g;
			if (!(b.xPos < 0 && b.yPos < 0))
			{
				return new Rectangle(b.xPos,b.yPos,b.width,b.height);
			}
		}
		else if (g instanceof DeliveryTruckInCityGui)
		{
			DeliveryTruckInCityGui d = (DeliveryTruckInCityGui)g;
			if (!(d.xPos < 0 && d.yPos < 0))
			{
				return new Rectangle(d.xPos,d.yPos,d.wSize,d.hSize);
			}
		}
		return null;
	}
	public void paintComponent(Graphics g) 
	{
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(getBackground());
        g2.fillRect(0, 0, 950, 700);
        
        g2.setColor(Color.black);
        for (int i=0; i<buildings.size(); i++) 
        {
            final Building b = buildings.get(i);
            g2.fill(b);
            g2.drawImage(b.i,b.x,b.y,b.width,b.height,this);
            
            if (!b.myBuildingPanel.isVisible())
            {
                b.myBuildingPanel.updateBuilding(b.generaltype);
            }
        }
        for (int i=0;i<6;i++)
        {
        	if (i!=0 && i!=5)
        	{
            	g2.drawImage(imgvroad,210+((i-1)*160),0,50,700,this);
        	}
        	else
        	{
        		g2.drawImage(imgvroad,i*180,0,50,700,this);
        	}
        }
        for (int i=0;i<5;i++)
        {
        	if (i!=0 && i!=4)
        	{
        		g2.drawImage(imghroad,0,i*150,950,50,this);
        	}
        	else
        	{
        		g2.drawImage(imghroad,0,i*157,950,50,this);
        	}
        }
        for (int i=0;i<people.size();i++)
        {
        	PersonGui pg = people.get(i).gui;
        	if (pg.ownsCar)
        	{
        		for (int j=0;j<intersection.size();j++)
        		{
        			if (collisionBetween(intersection.get(j),pg.car))
        			{
        				Rectangle r = intersection.get(j);
        				CarGui c = pg.car;
        				c.IntersectionWait(r);
        			}
        		}
        	}
        	for (int j=0;j<people.size();j++)
        	{
        		PersonGui p1 = people.get(i).gui;
        		PersonGui p2 = people.get(j).gui;
        		if (i!=j)
        		{
        			if (collisionBetween(p1,p2))
        			{
        				if (topCollision(p1,p2))
        				{
        					checkTopCollision(p1,p2,1);
        				}
        				else
        				{
        					checkSideCollision(p1,p2,1);
        				}
        			}
            		if (p1.ownsCar && p2.ownsCar)
            		{
                		CarGui c1 = p1.car;
                		CarGui c2 = p2.car;
            			if (collisionBetween(c1,c2) && c1.isMoving() && c2.isMoving() && !range(c1.xPos,c1.xhome,30) && !range(c2.xPos,c2.xhome,30))
            			{
            				accident = true;
            				accidentx = c1.xPos;
            				accidenty = c1.yPos;
            				resolveAccident(p1,p2);
            			}
            			/*if (collisionBetween(c1,c2) && c1.isMoving() && !c2.isMoving())
            			{
                    		for (int j1=0;j1<intersection.size();j1++)
                    		{
                    			if (collisionBetween(intersection.get(j1),c2))
                    			{
                    				Rectangle r = new Rectangle(c2.xPos,c2.yPos,c2.width,c2.height);
                    				c1.IntersectionWait2(r);
                    			}
                    		}
            			}
            			else if (collisionBetween(c1,c2) && !c1.isMoving() && c2.isMoving())
            			{
                    		for (int j1=0;j1<intersection.size();j1++)
                    		{
                    			if (collisionBetween(intersection.get(j1),c1))
                    			{
                    				Rectangle r = new Rectangle(c1.xPos,c1.yPos,c1.width,c1.height);
                    				c2.IntersectionWait2(r);
                    			}
                    		}
            			}
            			else if (collisionBetween(c1,c2) && !c1.isMoving() && !c2.isMoving())
            			{
                    		for (int j1=0;j1<intersection.size();j1++)
                    		{
                    			if (collisionBetween(intersection.get(j1),c1))
                    			{
                    				c1.collision = false;
                    				c2.collision = false;
                    			}
                    		}
            			}
            			/*if (collisionBetween(c1,c2) && !c1.isMoving() && c2.isMoving())
            			{
                    		for (int j1=0;j1<intersection.size();j1++)
                    		{
                    			if (collisionBetween(intersection.get(j1),c2))
                    			{
                    				Rectangle r = new Rectangle(c1.xPos,c1.yPos,c1.width,c1.height);
                    				c2.IntersectionWait(r);
                    			}
                    		}
            			}*/
            		}
        			if (collisionBetween(p1.car,p2) && p1.car.isMoving())
        			{
        				people.remove(p2.agent);
        				//P1 decreases money
        				/*boolean fatality = true;
        				if (fatality)
        				{
            	   			g2.setFont(new Font("TimesRoman", Font.PLAIN, 100)); 
            	   			g2.drawString("FATALITY", 475, 350);
        				}
        				Timer timer2 = new Timer();
        				timer2.schedule(new TimerTask() 
        				{
        					public void run() 
        					{
        						fatality = false;
        					}
        				},3000);*/
        			}
        			if (collisionBetween(p1,p2.car) && p2.car.isMoving())
        			{
        				people.remove(p1.agent);
        				//P2 decreases money
        			}
        		}
        	}
        }
        for (PersonAgent p:people)
        {
            p.gui.updatePosition();
            p.gui.draw(g2);
            if (p.gui.ownsCar)
            {
            	if (!accident)
            	{
            		p.gui.car.updatePosition();
            	}
                p.gui.car.draw(g2);
            }
        }
        for (Bus b : buses)
        {
            b.getOutsideGui().updatePosition();
            b.getOutsideGui().draw(g2);
        }
        for (int i=0; i<buildings.size(); i++) 
        {
            Building b = buildings.get(i);
            for (PersonAgent p:people)
            {
                if (b.contains(p.gui.xPos, p.gui.yPos) && p.gui.xDestination == b.x && p.gui.yDestination == b.y && b.generaltype.equals("Restaurant")) 
                {
                	if (checkExistRole(WaiterRole.class,p))
                	{
                		addRestaurantRoles(p,b,WaiterRole.class,CustomerRole.class);
                	}
                	else
                	{
                		addRestaurantRoles(p,b,NormalWaiterRole.class,CustomerRole.class);
                	}
                	p.gui.intoBuilding();
                }
                else if (b.contains(p.gui.xPos, p.gui.yPos) && p.gui.xDestination == b.x && p.gui.yDestination == b.y && b.generaltype.equals("SidRestaurant")) 
                {
                	if (checkExistRole(SidWaiterRole.class,p))
                	{
                		addRestaurantRoles(p,b,SidWaiterRole.class,SidCustomerRole.class);
                	}
                	else
                	{
                		addRestaurantRoles(p,b,SidNormalWaiterRole.class,SidCustomerRole.class);
                	}
                	p.gui.intoBuilding();
                }
                else if (b.contains(p.gui.xPos, p.gui.yPos) && p.gui.xDestination == b.x && p.gui.yDestination == b.y && b.generaltype.equals("AndrewRestaurant")) 
                {
                	if (checkExistRole(AndrewWaiterRole.class,p))
                	{
                		addRestaurantRoles(p,b,AndrewWaiterRole.class,AndrewCustomerRole.class);
                	}
                	else
                	{
                		addRestaurantRoles(p,b,AndrewNormalWaiterRole.class,AndrewCustomerRole.class);
                	}
                	p.gui.intoBuilding();
                }
                else if (b.contains(p.gui.xPos, p.gui.yPos) && p.gui.xDestination == b.x && p.gui.yDestination == b.y && b.generaltype.equals("RanchoRestaurant")) 
                {
                	if(ranchoRestaurantRoleCount == 0)
                	{
                		p.addRole(new RanchoManager("Manager"));
                		b.myBuildingPanel.rancho.addRanchoManager(b, p);
                	}
                	else
                	{
                      	if (checkExistRole(RanchoWaiterRole.class,p))
                    	{
                      		addRestaurantRoles(p,b,RanchoWaiterRole.class,RanchoCustomerRole.class);
                    	}
                    	else
                    	{
                    		addRestaurantRoles(p,b,RanchoNormalWaiterRole.class,RanchoCustomerRole.class);
                    	}
                	}
                	ranchoRestaurantRoleCount++;
                	p.gui.intoBuilding();
                }
                else if (b.contains(p.gui.xPos, p.gui.yPos) && p.gui.xDestination == b.x && p.gui.yDestination == b.y && b.generaltype.equals("AmyRestaurant")) 
                {
                 	if (checkExistRole(AmyWaiterRole.class,p))
                	{
                 		addRestaurantRoles(p,b,AmyWaiterRole.class,AmyCustomerRole.class);
                	}
                	else
                	{
                		addRestaurantRoles(p,b,AmyNormalWaiterRole.class,AmyCustomerRole.class);
                	}
                	p.gui.intoBuilding();
                }
                else if(b.contains(p.gui.xPos, p.gui.yPos) && p.gui.xDestination == b.x && p.gui.yDestination == b.y && b.generaltype.equals("Resident"))
                {
                	addRoles(p,ResidentRole.class,b);
                	p.gui.intoBuilding();
                	b.myBuildingPanel.addResident(b,p);
                }
                else if(b.contains(p.gui.xPos, p.gui.yPos) && p.gui.xDestination == b.x && p.gui.yDestination == b.y && b.generaltype.equals("Apartment"))
                {
                	if(landlordCount == 0){
                		addRoles(p,Landlord.class,b);
                		p.gui.intoBuilding();
                		b.myBuildingPanel.addLandlord(b, p);
                	}
                	else{
	                	addRoles(p,ResidentRole.class,b);
	                	p.gui.intoBuilding();
	                	b.myBuildingPanel.addResident(b,p);
                	}
                	landlordCount++;
                }
                else if(b.contains(p.gui.xPos, p.gui.yPos) && p.gui.xDestination == b.x && p.gui.yDestination == b.y && b.generaltype.equals("BusStop"))
                {
                	addRoles(p,BusPassengerRole.class,b);
                	p.gui.intoBuilding();
                	b.myBuildingPanel.addResident(b,p);
                	b.myBuildingPanel.addBusPassenger(b,p);
                }
                else if(b.contains(p.gui.xPos, p.gui.yPos) && p.gui.xDestination == b.x && p.gui.yDestination == b.y && b.generaltype.equals("Bank"))
                {
                	p.gui.intoBuilding();
                	if (checkExistRole(BankAgent.class,p)){
                		addRoles(p,BankAgent.class,b);
                		b.myBuildingPanel.addBankTeller(b, p);
                	} else if (checkExistRole(BankRobber.class,p)){
                		addRoles(p,BankRobber.class,b);
                		b.myBuildingPanel.addBankRobber(b, p);
                		p.stateChanged();
                	}else {
                		addRoles(p,BankCustomer.class,b);
                    	b.myBuildingPanel.addBankCustomer(b,p);
                	}
                }
                /*else if (b.contains(p.gui.xPos, p.gui.yPos) && b.generaltype.equals(p.gui.location))
                {
                	if (Integer.parseInt(p.name)%5 == 0 && b.type.equals(p.gui.location))
                	{
                    	Coordinates loc = randomRestaurant();
                    	
                    	if (computeDistance(p,loc) <= 400)
                    	{
                        	p.gui.intoBuilding();
                           	p.gui.setDestination(loc.x,loc.y,"Restaurant");
                    	}
                    	else
                    	{
                    		p.gui.useCar(loc.x,loc.y,"Restaurant");
                    	}
                	}
                	else if (b.type.equals(p.gui.location+Integer.parseInt(p.name)%5))
                	{
                    	Coordinates loc = randomRestaurant();
                    	if (computeDistance(p,loc) <= 400)
                    	{
                    		p.gui.intoBuilding();
                    		p.gui.setDestination(loc.x,loc.y,"Restaurant"+Integer.parseInt(p.name)%5);
                    	}
                    	else
                    	{
                    		p.gui.useCar(loc.x,loc.y,"Restaurant");
                    	}
                	}
                }*/
            }
        }  
   		for (int j=0; j<trucks.size(); j++)
   		{
        	trucks.get(j).updatePosition();
        	trucks.get(j).draw(g2);
        }
   		g2.setColor(Color.red);
   		//Parking spots for Restaurants
   		for (int i=0;i<5;i++)
   		{
   			Coordinates c = locations.get(restaurants.get(i));
   	   		for (int j=0;j<3;j++)
   	   		{
   	   			g2.drawLine(c.x-30,c.y+10+(j*20),c.x,c.y+10+(j*20));
	   			g2.drawLine(c.x+50,c.y+10+(j*20),c.x+80,c.y+10+(j*20));
   	   		}
   		}
   		//Parking spots for Banks
   		for (int i=0;i<3;i++)
   		{
   			Coordinates c = locations.get("Bank"+(i+1));
   	   		for (int j=0;j<3;j++)
   	   		{
   	   			g2.drawLine(c.x-30,c.y+10+(j*20),c.x,c.y+10+(j*20));
	   			g2.drawLine(c.x+50,c.y+10+(j*20),c.x+80,c.y+10+(j*20));
   	   		}
   		}
   		//Parking spots for residents and apartments
   		for (int i=0;i<5;i++)
   		{
   			Coordinates c = locations.get(homes.get(i));
   	   		for (int j=0;j<3;j++)
   	   		{
   	   			g2.drawLine(c.x-30,c.y+10+(j*20),c.x,c.y+10+(j*20));
	   			g2.drawLine(c.x+50,c.y+10+(j*20),c.x+80,c.y+10+(j*20));
   	   		}
   		}
   		if (accident)
   		{
   			g2.setFont(new Font("TimesRoman", Font.PLAIN, 100)); 
   			g2.drawString("ACCIDENT", 100, 100);
   			g2.setFont(new Font("TimesRoman", Font.PLAIN, 10)); 
   			g2.drawString("Accident location",accidentx,accidenty);
   		}
        /*g2.setColor(Color.red);
        for (int i=0;i<vroad.size();i++)
        {
        	for (int j=0;j<hroad.size();j++)
        	{
        		g2.fillRect(vroad.get(i)-10,hroad.get(j)-15,50,50);
        	}
        }*/
        /*//Testing
        for (Gui t:test)
        {
        	t.updatePosition();
            t.draw(g2);
        }
        //Testing
        for (int i=0;i<test.size();i++)
        {
        	for (int j=0;j<test.size();j++)
        	{
        		CarGui c1 = (CarGui) test.get(i);
        		CarGui c2 = (CarGui) test.get(j);
        		if (i!=j)
        		{
        			if (collisionBetween(c1,c2))
        			{
        				if (topCollision(c1,c2))
        				{
        					checkCarTopCollision(c1,c2,50);
        				}
        				else
        				{
        					checkCarSideCollision(c1,c2,20);
        				}
        			}
        		}
        	}
        }*/
	}
	public void resolveAccident(final PersonGui p1, final PersonGui p2)
	{
		final CarGui c1 = p1.car;
		final CarGui c2 = p2.car;

		p1.setDestination(c1.xPos, c1.yPos,475,350);
		p2.setDestination(c2.xPos, c2.yPos,475,350);
		
		//Arguing
		Timer timer2 = new Timer();
		timer2.schedule(new TimerTask() 
		{
			public void run() 
			{
				p1.setDestination(p1.car.xPos,p1.car.yPos);
				p2.setDestination(p2.car.xPos,p2.car.yPos);
				c1.xPos = c1.xhome;c1.yPos = c1.yhome;
				if (range(c1.xhome,c2.xhome))
				{
					if (c2.xhome+180 >= 950)
					{
						c2.setHome(c2.xhome-180,c2.yhome);
					}
					else
					{
						c2.setHome(c2.xhome+180,c2.yhome);
					}
				}
				c2.xPos = c2.xhome;c2.yPos = c2.yhome;
				//working resolveAccident function
				/*while(collisionBetween(c1,c2))
				{
					if (c1.xPos <= c2.xPos)
					{
						c1.xPos-=6;
						c1.yPos-=6;
						c2.xPos+=6;
						c2.yPos+=6;
					}
					else
					{
						c1.xPos+=6;
						c2.xPos-=6;
						c1.yPos+=6;
						c2.yPos-=6;
					}
				}*/
			}
		},3000);
		timer2.schedule(new TimerTask() 
		{
			public void run() 
			{
				accident = false;
				p1.resolving = false;
				p2.resolving = false;
				accidentx = -50;
				accidenty = -50;
			}
		},6000);
	}
	public boolean range(int pos, int value)
	{
		return (pos >= value-50 && pos <= value+50);
	}
	public boolean range(int pos, int value, int s)
	{
		return (pos >= value-s && pos <= value+s);
	}
	public void addRoles(PersonAgent p, Class<?> role, Building b)
	{
    	//Checking existence of roles here
    	try 
    	{
			if (!checkExistRole(p,role))
			{
				addOtherRoles(role,p,b);
			}
		} 
    	catch (ClassNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void addOtherRoles(Class<?> customer, PersonAgent p, Building b)
	{
		if (customer == ResidentRole.class)
		{
			p.addRole(new ResidentRole("Rancho"));
		}
		else if (customer == Landlord.class){
			p.addRole(new Landlord("Landlord"));
		}
		else if (customer == BusPassengerRole.class)
		{
			p.addRole(new BusPassengerRole("Andrew", 5));
		}
		else if (customer == BankCustomer.class)
		{
			p.addRole(new BankCustomer("Customer"+b.myBuildingPanel.bankCustomers.size()+1));
			
		} else if (customer == BankAgent.class){
			p.addRole(new BankAgent("Teller"+b.myBuildingPanel.bankTellers.size()+1));
		} else if (customer == BankRobber.class){
			int bankNum = Integer.parseInt(b.type.substring(b.type.length()-1));
			p.addRole(new BankRobber(b.myBuildingPanel.bankHosts.elementAt(bankNum-1)));
		}
	}
	public void addRestaurantRoles(PersonAgent p, Building b, Class<?> waiter, Class<?> customer)
	{
    	//Checking existence of roles here
    	try 
    	{
			if (!checkExistRole(p,waiter))
			{
				try 
				{
					if (!checkExistRole(p,customer))
					{
						addCustomerRole(customer,p);
			        	//p.addRole(new CustomerRole("Bach"));
						//b.myBuildingPanel.addCustomer(b,p);
					}
				} 
				catch (ClassNotFoundException e) 
				{
					e.printStackTrace();
				}
				checkInCustomer(customer,b,p);
			}
			else
			{
				addWaiterRole(waiter,b,p);
			}
		} 
    	catch (ClassNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void addCustomerRole(Class<?> customer, PersonAgent p)
	{
		if (customer == CustomerRole.class)
		{
        	p.addRole(new CustomerRole("Bach"));
		}
		else if (customer == SidCustomerRole.class)
		{
        	p.addRole(new SidCustomerRole("Sid"));
		}
		else if (customer == RanchoCustomerRole.class)
		{
        	p.addRole(new RanchoCustomerRole("Rancho"));
		}
		else if (customer == AndrewCustomerRole.class)
		{
        	p.addRole(new AndrewCustomerRole("Andrew"));
		}
		else if (customer == AmyCustomerRole.class)
		{
        	p.addRole(new AmyCustomerRole("Amy"));
		}
	}
	public void checkInCustomer(Class<?> customer, Building b, PersonAgent p)
	{
		if (customer == CustomerRole.class)
		{
			b.myBuildingPanel.addCustomer(b,p);
		}
		else if (customer == SidCustomerRole.class)
		{
			b.myBuildingPanel.sid.addSidCustomer(b,p);
		}
		else if (customer == RanchoCustomerRole.class)
		{
			b.myBuildingPanel.rancho.addRanchoCustomer(b,p);
		}
		else if (customer == AndrewCustomerRole.class)
		{
			b.myBuildingPanel.andrew.addAndrewCustomer(b,p);
		}
		else if (customer == AmyCustomerRole.class)
		{
			b.myBuildingPanel.amy.addAmyCustomer(b,p);
		}
	}
	public void addWaiterRole(Class<?> waiter, Building b, PersonAgent p)
	{
		if (waiter == WaiterRole.class)
		{
        	//p.addRole(new WaiterRole("Bach"));
			b.myBuildingPanel.addWaiter(b,p);
		}
		else if (waiter == SidWaiterRole.class)
		{
        	//p.addRole(new SidWaiterRole());
			b.myBuildingPanel.sid.addSidWaiter(b,p);
		}
		else if (waiter == RanchoWaiterRole.class)
		{
        	//p.addRole(new RanchoCustomerRole("Rancho"));
			b.myBuildingPanel.rancho.addRanchoWaiter(b,p);
		}
		else if (waiter == AndrewWaiterRole.class)
		{
        	//p.addRole(new AndrewWaiterRole("Andrew"));
			b.myBuildingPanel.andrew.addAndrewWaiter(b,p);
		}
		else if (waiter == AmyWaiterRole.class)
		{
        	//p.addRole(new AmyCustomerRole("Amy"));
			b.myBuildingPanel.amy.addAmyWaiter(b,p);
		}
		else if (waiter == NormalWaiterRole.class)
		{
			b.myBuildingPanel.addNormalWaiter(b,p);
		}
		else if (waiter == SidNormalWaiterRole.class)
		{
			b.myBuildingPanel.sid.addSidNormalWaiter(b,p);
		}
		else if (waiter == RanchoNormalWaiterRole.class)
		{
			b.myBuildingPanel.rancho.addRanchoNormalWaiter(b,p);
		}
		else if (waiter == AndrewNormalWaiterRole.class)
		{
			b.myBuildingPanel.andrew.addAndrewNormalWaiter(b,p);
		}
		else if (waiter == AmyNormalWaiterRole.class)
		{
			b.myBuildingPanel.amy.addAmyNormalWaiter(b,p);
		}
	}
	public Coordinates randomRestaurant()
	{
    	Coordinates c;
    	int choice = (int)(Math.random()*5);
    	c = locations.get(restaurants.get(choice));
    	return c;
	}
	public double computeDistance(PersonAgent p, Coordinates c)
	{
		int px = p.gui.xPos, py=p.gui.yPos;
		return Math.sqrt(Math.pow(px-c.x, 2)+Math.pow(py-c.y, 2));
	}
	public double computeDistance(CarGui p, Coordinates c)
	{
		int px = p.xPos, py=p.yPos;
		return Math.sqrt(Math.pow(px-c.x, 2)+Math.pow(py-c.y, 2));
	}
	public boolean checkExistRole (Class<?> c, PersonAgent p) 
	{
		for (Role r:p.roles)
		{
			if (c.isInstance(r))
			{
				return true;
			}
		}
		return false;
	}
	public boolean checkExistRole(PersonAgent p, Class<?> c) throws ClassNotFoundException
	{
		for (Role r:p.roles)
		{
			if (c.isInstance(r))
			{
				return true;
			}
		}
		return false;
	}
	public void mouseClicked(MouseEvent me) 
	{
        //Check to see which building was clicked
        for ( int i=0; i<buildings.size(); i++ ) 
        {
            Building b = buildings.get(i);
            if ( b.contains( me.getX(), me.getY() ) ) 
            {
            	b.displayBuilding();
            }
        }     
	}
	public void addBus(Building b, int num)
	{    	
		BusAgent bus = (BusAgent) buses.get(num);
		BusInsideGui g = bus.getInsideGui();
		if (g == null) {
			g = new BusInsideGui(bus, bus.boGui);
		}
		b.myBuildingPanel.busStopAdd(g);
	}
	public void addPassenger(Building b)
	{
		BusPassengerRole bp = (BusPassengerRole) person.roles.get(0);
		BusPassengerGui g = new BusPassengerGui(personGui, bp, true);
		b.myBuildingPanel.busStopAdd(g);
	}
	
	//This method doesn't seem to be getting implemented ever, why does it need to be here? 
	public void createBank(Building b){
		System.out.println("Created in city");
    	bankCustomers = new Vector<BankCustomer>();
    	bankTellers = new Vector<BankAgent>();
    	
    	//TODO: THIS IS A TEST IMPLEMENTATION. Need to revise it with roles, etc. 
    	bHost = new BankHost("bhost");
    	bHostGui = new HostGui(bHost);

    	b.myBuildingPanel.bankAdd(bHostGui);
    	bHost.setGui(bHostGui);
    	//bHost.startThread();
 
    	/*
    	bankTellers.add(new BankAgent("Will"));
    	bankTellers.get(0).startThread();
    	bHost.msgImFree(bankTellers.get(0));
    	
    	bankCustomers.add(new BankCustomer("Sid"));
    	bankCustomers.get(0).setMoneyVariables(100, 0);
    	bankCustomers.get(0).startThread();
    	*/
    	
    	
    }

	public void mouseEntered(MouseEvent arg0) 
	{
	}
	public void mouseExited(MouseEvent arg0) 
	{
	}
	public void mousePressed(MouseEvent arg0) 
	{
	}
	public void mouseReleased(MouseEvent arg0) 
	{
	}
	public void actionPerformed(ActionEvent e)
	{
		repaint();  //Will have paintComponent called
	}
	public ArrayList<Building> getBuildings() 
	{
        return buildings;
	}
	public Person getPerson()
	{
		return person;
	}
}