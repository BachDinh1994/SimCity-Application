package SimCity;
import javax.swing.*;

import market.gui.DeliveryTruckGui;
import market.gui.MarketCustomerGui;
import market.gui.MarketEmployeeGui;
import market.gui.MarketGui;
import market.interfaces.Market;
import amyRestaurant.gui.AmyRestaurantPanel;
import bank.BankAgent;
import bank.BankCustomer;
import bank.BankHost;
import bank.BankRobber;
import bank.gui.BCustomerGui;
import bank.gui.BankRobberGUI;
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
import restaurant.CashierAgent;
import restaurant.CookAgent;
import restaurant.HostAgent;
import market.DeliveryTruckAgent;
import market.MarketAgent;
import market.MarketCustomerAgent;
import market.MarketEmployeeAgent;
import restaurant.WaiterAgent;
import restaurant.gui.AnimationPanel;
import restaurant.gui.CookGui;
import restaurant.gui.CustomerGui;
import restaurant.gui.FoodGui;
import restaurant.gui.WaiterGui;
import restaurant.interfaces.Customers;
import restaurant.interfaces.Waiters;
import restaurant_andrew.gui.AndrewRestaurantPanel;
import restaurant_andrew.gui.Coord;
import restaurant_rancho.gui.RanchoRestaurantPanel;
import restaurant_sid.gui.SidRestaurantPanel;
import Role.CustomerRole;
import Role.Landlord;
import Role.NormalWaiterRole;
import Role.ResidentRole;
import Role.Role;
import Role.WaiterRole;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

@SuppressWarnings("serial")
public class BuildingPanel extends JPanel implements ActionListener 
{
	Graphics2D graphic;
	//Bach's animation and agents
	private static final int WINDOWX = 700;
    private static final int WINDOWY = 700;
    private static final int TABLEH = 50;
    private static final int TABLEW = 50;
    private static final int ORIGIN = 0;
    HashMap<Integer, Coord> tablePositions = new HashMap<Integer, Coord>();
    
    //Sid's animation and agents
    SidRestaurantPanel sid;
    
    //Andrew's animation and agents
    AndrewRestaurantPanel andrew;
    
    //Rancho's animation and agents
    RanchoRestaurantPanel rancho;
    private final int c1=50,c2=150,c3=250,c4=350;
    private final int r1=50,r2=150,r3=250,r4=350;
    private final int[] tablesX={c2,c3,c2,c3};
    private final int[] tablesY={r2,r2,r3,r3};
    private final int side=50;
    //Amy's animation and agents
    AmyRestaurantPanel amy;
    public static Hashtable<Integer, Integer> hashTable = new Hashtable<Integer, Integer>();
	{hashTable.put(1,150);
	hashTable.put(2, 270);
	hashTable.put(3, 390);}
	private final int frameInterval = 7;
	private final int WINDOW_W = 450;
    private final int WINDOW_H = 400;
    private final int WINDOW_X = 0;
    private final int WINDOW_Y = 0;
    private final int TABLE_Y = 250;
    private final int TABLE_H = 75;
    private final int TABLE_W =50;
    
	//Restaurant Agents and Guis
    public CookAgent cook = new CookAgent("Cook");
    public CashierAgent cashier = new CashierAgent("Cashier");
	public HostAgent host;
    public WaiterAgent waiter;
    private WaiterGui waiterGui;
    private FoodGui foodGui;
    private CookGui cookGui;

    //Restaurant Vectors
    
    public Vector<Customers> customers = new Vector<Customers>();
    public Vector<Waiters> waiters = new Vector<Waiters>();
    public MarketAgent market;
    public MarketEmployeeAgent marketEmployee;
    public DeliveryTruckAgent deliveryTruck;
    public MarketCustomerAgent mCustomer;

    
    //Bank Agents and Guis
    //public BankHost bHost;
    public HostGui bHostGui;
    public TellerGui bTellerGui;
    public BCustomerGui bCustomerGui;
    public BankRobberGUI robberGui;
    
    //Bank Vectors

    public Vector<BankHost> bankHosts = new Vector<BankHost>();
    public Vector<BankCustomer> bankCustomers = new Vector<BankCustomer>();
    public Vector<BankAgent> bankTellers = new Vector<BankAgent>();
    public Vector<BankRobber> bankRobbers = new Vector<BankRobber>();
    
    //Bus Stop Vectors
    public Vector<BusPassengerRole> busPassengers = new Vector<BusPassengerRole>();
    public Vector<BusAgent> buses = new Vector<BusAgent>();
    public Vector<TicketMachineAgent> busStops = new Vector<TicketMachineAgent>();
    public CityPanel c; // to get the TMs
    
    static final int x = 100;static final int x2 = 200;static final int x3 = 300;static final int y = 150;
    static final int width = 50;static final int height = 50;
    private List<Gui> restaurantguis = new ArrayList<Gui>();
    private List<Gui> bankguis = new ArrayList<Gui>();
    private List<Gui> residentguis = new ArrayList<Gui>();
    private List<Gui> marketguis = new ArrayList<Gui>();
    private List<Gui> busstopguis = new ArrayList<Gui>();
    
    private Landlord landlord;
	
    Building myBuilding;
    String myName;
    SimCity myCity;
    AnimationPanel anim;
    public BuildingPanel(Building r, int i, SimCity sc) 
    {
        tablePositions.put(1, new Coord(2*WINDOWX/15, 10*WINDOWY/12));
        tablePositions.put(2, new Coord(3*WINDOWX/15, 9*WINDOWY/12));
        tablePositions.put(3, new Coord(4*WINDOWX/15, 8*WINDOWY/12));
        tablePositions.put(4, new Coord(5*WINDOWX/15, 7*WINDOWY/12));
        tablePositions.put(5, new Coord(200, 20)); // cook top row (orders in)
        tablePositions.put(6, new Coord(200, 70)); // cook top row (food out)
        
        myBuilding = r;
        myName = "" + i;
        myCity = sc;
        anim = new AnimationPanel();
        
        setBackground(Color.WHITE);
        Timer timer = new Timer(20,this);
        timer.start();
    }
    public void runRestaurant(Graphics g)
    {
        Graphics2D g2 = (Graphics2D)g;
        //Clear the screen by painting a rectangle the size of the frame
        graphic = (Graphics2D)g;
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );
        
        g2.setColor(Color.RED);
        g2.drawString("Restaurant",200,100);
        
        //Here are the GUI components
        g2.setColor(Color.GRAY);
        g2.fillRect(380, 270, 30, 60);
        g2.setColor(Color.RED);
        g2.drawString("Fridge",373,265);
        g2.setColor(Color.ORANGE);
        g2.fillRect(x, y, width, height);
        g2.setColor(Color.ORANGE);
        g2.fillRect(x, y, width, height);
        g2.fillRect(x2, y, width, height);
        g2.fillRect(x3, y, width, height);
        g2.setColor(Color.RED);
        g2.drawString("Waiter Home Position",150,35);
        g2.drawLine(30, 20, 450, 20);
        g2.setColor(Color.BLUE);
        g2.drawString("Waiting Area",35,343);
        g2.drawLine(30, 20, 30, 350);
        g2.setColor(Color.PINK);
        g2.fillRect(100, 250, 250, 30);
        g2.setColor(Color.RED);
        g2.drawString("Plating Area",190,268);
        g2.setColor(Color.RED);
        g2.fillRect(100, 300, 250, 30);
        g2.setColor(Color.darkGray);
        g2.fillRect(100, 300, 30, 30);
        g2.fillRect(200, 300, 30, 30);
        g2.fillRect(300, 300, 30, 30);
        g2.setColor(Color.WHITE);
        g2.drawString("Cooking Area",185,320);  

	    updateAnimation(restaurantguis,g2);
    }
    public void restaurantAdd(Gui g)
    {
        restaurantguis.add(g);
    }
    public boolean checkExist(CustomerRole c)
    {
    	boolean exist = false;
        for(Gui gui : restaurantguis) 
        {
            if (((CustomerGui) gui).getAgent() == c) 
            {
                exist = true;
                break;
            }
        }
        return exist;
    }
    
    public void runMarket(Graphics g, String type)
    {
        Graphics2D g2 = (Graphics2D)g;
        //Clear the screen by painting a rectangle the size of the frame
        g.setColor(getBackground());
        g.fillRect(0, 0, WINDOWX, WINDOWY );
       
        g2.setColor(Color.RED);
        g2.drawString(type,450/2,350/2);
       
        
        //add the static objects
        
        //table where market agent is
        g2.setColor(Color.black);
        g2.fillRect(120, 100, 40, 90);
        
        //boundaries
        g2.setColor(Color.RED);
        g2.drawLine(30, 10, 440, 10);//top
        g2.drawLine(200, 10, 200, 240); //middle
        g2.drawLine(200, 290, 200, 350);
        g2.drawLine(30, 350, 380, 350);//bottom
        g2.drawLine(430, 350, 440, 350);
        g2.drawLine(440, 10, 440, 350);
        
        //line
        g2.setColor(Color.gray);
        g2.drawLine(45, 130, 105, 130);
        g2.drawLine(85, 160, 105, 160);
        g2.drawLine(45, 130, 45, 280);
        g2.drawLine(85, 160, 85, 280);
        
        //storage
       
        if(type.equals("Market")){
        g2.setColor(Color.green);
        g2.fillRect(280, 40, 160, 20);
        g2.setColor(Color.PINK);
        g2.fillRect(280, 100, 160, 20);
        g2.setColor(Color.ORANGE);
        g2.fillRect(280, 160, 160, 20);
        }
        else if(type.equals("Market1")){
        	 g2.setColor(Color.darkGray);
             g2.fillRect(280, 40, 160, 20);
             g2.setColor(Color.YELLOW);
             g2.fillRect(280, 100, 160, 20);
             g2.setColor(Color.MAGENTA);
             g2.fillRect(280, 160, 160, 20);
        }
        else{
        	 g2.setColor(Color.blue);
             g2.fillRect(280, 40, 160, 20);
             g2.setColor(Color.RED);
             g2.fillRect(280, 100, 160, 20);
             g2.setColor(Color.LIGHT_GRAY);
             g2.fillRect(280, 160, 160, 20);
        }
      
        
        
        updateAnimation(marketguis,g2);
    }
    public void runBank(Graphics g)
    {
        Graphics2D g2 = (Graphics2D)g;

        
        //Clear the screen by painting a rectangle the size of the frame
        g.setColor(getBackground());
        g.fillRect(0, 0, WINDOWX, WINDOWY );
        
        g2.setColor(Color.RED);
        g2.drawString("Bank",200,100);
        
        //GUI components
        Color brown = new Color(163,105,67);
        g2.setColor(brown);
        g2.drawRect(300,120,100,210);//Teller Desk Outline
        g2.fillRect(300, 120, 30, 210);//Teller Desk
        g2.drawRect(40, 120, 30, 30);//Host Desk Outline
        g2.fillRect(40, 120, 30, 5);//Host Desk
        
        g2.setColor(Color.CYAN);
        g2.fillRect(300, 120, 2, 210);//Glass of teller desk
        
        g2.setColor(Color.BLACK); //Outlines of the bank and teller dividers
        g2.drawLine(30, 70, 30, 400);
        g2.drawLine(30,400,400,400);
        g2.drawLine(400, 400, 400, 70);
        g2.drawLine(300, 190, 330, 190);
        g2.drawLine(300, 260, 330, 260);
        g2.drawLine(300, 330, 330, 330);
        
        g2.rotate(Math.toRadians(90));
        g2.drawString("Teller Area", 200, -390);
        g2.rotate(Math.toRadians(-90));
       
        updateAnimation(bankguis,g2);
    }
    
    public void runApartment(Graphics g)
    {
    	Graphics2D g2 = (Graphics2D)g;
        //Clear the screen by painting a rectangle the size of the frame
        g.setColor(getBackground());
        g.fillRect(0, 0, WINDOWX, WINDOWY );
        g2.setColor(Color.RED);
        g2.drawString("Apartment",200,450);
        for(int i=0;i<4;i++){
        	int a = i%2;
        	int b = i/2;
	        
	        g2.setColor(Color.BLACK);
	        g2.drawLine(0+a*200, 0+b*200, 200*a*200, 0+b*200);
	        g2.drawLine(25+a*200, 25+b*200, 25+a*200, 200+b*200);
	        g2.drawLine(25+a*200,125+b*200,200+a*200,125+b*200);
	        g2.drawLine(100+a*200, 0+b*200, 100+a*200, 125+b*200);
	        g2.drawLine(100+a*200, 50+b*200, 200+a*200, 50+b*200);
	        g2.drawLine(25+a*200, 200+b*200, 200+a*200, 200+b*200);
	        g2.drawLine(200+a*200, 0+b*200, 200+a*200, 200+b*200);
	        g2.setColor(Color.BLUE);
	        g2.drawString("Kitchen", 100+a*200, 140+b*200);
	        g2.drawString("Bed", 150+a*200, 10+b*200);
	        g2.drawString("Home",35+a*200,50+b*200);
	        g2.drawString("Fridge", 28+a*200, 132+b*200);
	        g2.drawString("Dining Table", 130+a*200, 65+b*200);
	        g2.drawString("Stove", 170+a*200, 132+b*200);
	        g2.setColor(Color.ORANGE);
	        g2.fillRect(140+a*200, 18+b*200, 40, 20);
	        g2.fillRect(135+a*200, 75+b*200, 35, 35);
	        g2.fillRect(30+a*200, 138+b*200, 25, 60);
	        g2.fillRect(170+a*200,138+b*200,25,60);
        }

        
        updateAnimation(residentguis,g2);
    }
    public void runResident(Graphics g)
    {
        Graphics2D g2 = (Graphics2D)g;
        //Clear the screen by painting a rectangle the size of the frame
        g.setColor(getBackground());
        g.fillRect(0, 0, WINDOWX, WINDOWY );
        
        g2.setColor(Color.RED);

        g2.drawString("Home",0,100);
        
        g2.setColor(Color.BLACK);
        g2.drawLine(50, 50, 50, 400);
        g2.drawLine(50,250,400,250);
        g2.drawLine(200, 0, 200, 250);
        g2.drawLine(200, 100, 400, 100);
        g2.drawLine(50, 400, 400, 400);
        g2.drawLine(400, 0, 400, 400);
        g2.setColor(Color.BLUE);
        g2.drawString("Kitchen", 200, 280);
        g2.drawString("Bed", 300, 20);
        g2.drawString("Home Position",70,100);
        g2.drawString("refrigerator", 56, 264);
        g2.drawString("Dining Table", 260, 130);
        g2.drawString("Stove", 340, 264);
        g2.setColor(Color.ORANGE);
        g2.fillRect(280, 36, 80, 40);
        g2.fillRect(270, 150, 70, 70);
        g2.fillRect(60, 276, 50, 120);
        g2.fillRect(340,276,50,120);
        

        
        updateAnimation(residentguis,g2);
    }
    public void runMarket(Graphics g)
    {
        Graphics2D g2 = (Graphics2D)g;
        //Clear the screen by painting a rectangle the size of the frame
        g.setColor(getBackground());
        g.fillRect(0, 0, WINDOWX, WINDOWY );
        
        g2.setColor(Color.RED);
        g2.drawString("Market",200,100);
        
        updateAnimation(marketguis,g2);
    }
    public void runBusStop(Graphics g) {
    	
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g.setColor(getBackground());
        g.fillRect(0, 0, WINDOWX, WINDOWY);

        //Bus area
        g2.setColor(Color.GRAY);
        g2.fillRect(0, 0, 150, WINDOWY);
        //Ticket stand
        g2.setColor(Color.RED);
        g2.fillRect(200, 0, 120, 50);
        
        updateAnimation(busstopguis, g2);
        
        for(Gui gui : busstopguis) 
        {
            if (gui.isPresent()) 
            {
                gui.draw(g2);
            }
        }
    }
    public void updateAnimation(List<Gui> guis, Graphics2D g)
    {	
    	
        for(Gui gui : guis) 
        {
            if (gui.isPresent()) 
            {
                gui.updatePosition();
                gui.draw(g);
            }
        }
    }
    public void updateAnimation(List<Gui> guis)
    {
        for(Gui gui : guis) 
        {
            if (gui.isPresent()) 
            {
                gui.updatePosition();
            }
        }
    }
    public void updateBuilding(String type)
    {
    	if (type.equals("SidRestaurant"))
    	{
            for(restaurant_sid.gui.Gui gui : sid.guis) 
            {
                if (gui.isPresent()) 
                {
                    gui.updatePosition();
                }
            }
    	}
    	else if (type.equals("RanchoRestaurant"))
    	{
            for(restaurant_rancho.gui.Gui gui : rancho.guis) 
            {
                if (gui.isPresent()) 
                {
                    gui.updatePosition();
                }
            }
    	}
    	else if (type.equals("AndrewRestaurant"))
    	{
            for(restaurant_andrew.gui.Gui gui : andrew.guis) 
            {
                if (gui.isPresent()) 
                {
                    gui.updatePosition();
                }
            }
    	}
    	else if (type.equals("AmyRestaurant"))
    	{
            for(amyRestaurant.gui.Gui gui : amy.guis) 
            {
                if (gui.isPresent()) 
                {
                    gui.updatePosition();
                }
            }
    	}
    	if (type.equals("Restaurant"))
    	{
    		updateAnimation(restaurantguis);
    	}
    	else if (type.equals("Bank"))
    	{
    		updateAnimation(bankguis);
    	}
    	else if (type.equals("Resident"))
    	{
    		updateAnimation(residentguis);
    	}
    	else if (type.equals("Market"))
    	{
    		updateAnimation(marketguis);
    	}
    	else if (type.equals("BusStop"))
    	{
    		updateAnimation(busstopguis);
    	}
    }
    public String getName() 
    {
        return myName;
    }
    public void displayBuildingPanel() 
    {
        myCity.displayBuildingPanel(this);       
    }
    public void bankAdd(Gui g)
    {
    	bankguis.add(g);
    }
    public void residentAdd(Gui g)
    {
    	residentguis.add(g);
    }
    public void marketAdd(Gui g)
    {
    	marketguis.add(g);
    }
    public void busStopAdd(Gui g)
    {
    	busstopguis.add(g);
    }
	public void actionPerformed(ActionEvent e) 
	{
		repaint();  //Will have paintComponent called
	}
    public void paintComponent(Graphics g) 
    {
    	if (myBuilding.generaltype.equals("Restaurant"))
    	{
    		runRestaurant(g);
    	}
    	else if (myBuilding.generaltype.equals("SidRestaurant"))
    	{
    		runSidRestaurant(g);
    	}
    	else if (myBuilding.generaltype.equals("RanchoRestaurant"))
    	{
    		runRanchoRestaurant(g);
    	}
    	else if (myBuilding.generaltype.equals("AndrewRestaurant"))
    	{
    		runAndrewRestaurant(g);
    	}
    	else if (myBuilding.generaltype.equals("AmyRestaurant"))
    	{
    		runAmyRestaurant(g);
    	}
    	else if (myBuilding.generaltype.equals("Bank"))
    	{
    		runBank(g);
    	}
    	else if (myBuilding.generaltype.equals("Resident"))
    	{
    		runResident(g);
    	}
    	else if(myBuilding.generaltype.equals("Apartment"))
    	{
    		runApartment(g);
    	}
    	else if (myBuilding.generaltype.equals("Market"))
    	{
    		runMarket(g, myBuilding.type);
    	}
    	else if (myBuilding.generaltype.equals("BusStop"))
    	{
    		runBusStop(g);
    	}
    }
    public void createBank(Building b){
    	bankHosts.add(new BankHost("Host " + bankHosts.size()+1));
    	bHostGui = new HostGui(bankHosts.lastElement());
    	

    	bankHosts.lastElement().setGui(bHostGui);

    	b.myBuildingPanel.bankAdd(bHostGui);
    	bankHosts.lastElement().startThread();	
    	
    }
    public void createMarket(Building b, int i) {
		
		// TODO Auto-generated method stub
		market = new MarketAgent("Market "+i);
		
		//mCustomer.getMyInfo().myType = "Customer";
        marketEmployee = new MarketEmployeeAgent("Employee "+i);

        deliveryTruck = new DeliveryTruckAgent("DeliveryTruck " + i);
        DeliveryTruckGui dG = new DeliveryTruckGui(deliveryTruck);
        MarketEmployeeGui eG = new MarketEmployeeGui(marketEmployee);
        MarketGui mG = new MarketGui(market);
        
        b.myBuildingPanel.marketAdd(eG);
        b.myBuildingPanel.marketAdd(dG);
        b.myBuildingPanel.marketAdd(mG);
        
        if(i == 1){
        mCustomer = new MarketCustomerAgent("Customer");
		mCustomer.setMyInfo(new myContact(new Address(0,0), "Customer", mCustomer, mCustomer));
		MarketCustomerGui cG = new MarketCustomerGui(mCustomer);
		cG.setPosition(-20, -20);
        b.myBuildingPanel.marketAdd(cG);
        mCustomer.setGui(cG);
		mCustomer.startThread();}

        
        marketEmployee.setGui(eG);
		deliveryTruck.setGui(dG);
	  
		market.setGui(mG);
		market.setMyDeliveryTruck(deliveryTruck);
		market.setMyEmployee(marketEmployee);
		
		deliveryTruck.setMyBoss(market);
		marketEmployee.setMyManager(market);
    	market.startThread();
    	deliveryTruck.startThread();
    	marketEmployee.startThread();
	}
    public void createBusStop(Building b, int num){
    	
        TicketMachineAgent tm = new TicketMachineAgent(b, num);
        busStops.add(tm);
        
        c.ticketMachines.add(tm);
    }
	public void addBus(Building b, BusAgent bus, int num)
	{
		BusInsideGui g = bus.getInsideGui();
		if (g == null) {
			g = new BusInsideGui(bus, bus.boGui);
		}
		busStopAdd(g);
	}
	public void createRestaurant(Building b , int j)
	{
  	
		cook = new CookAgent("Cook "+j);
		 host = new HostAgent("Host " + j);
	        myContact cookInfo  = new myContact (new Address(90,370), "Cook", cook, cashier);
	        cook.setMyInfo(cookInfo);
	        
	        if(j == 1){
	        	cook.createFood(2);
	        }
	        else{
	        	cook.createFood(20);
	        }
	        host.startThread();
	        cook.startThread();
	        cashier.startThread();
	        cookGui = new CookGui(cook);
	        cook.setGui(cookGui);
	        b.myBuildingPanel.restaurantAdd(cookGui);
	}
	public void createRanchoRestaurant()
	{
		rancho = new RanchoRestaurantPanel();
	}
	public void runRanchoRestaurant(Graphics g)
	{
        Graphics2D g2 = (Graphics2D)g;
        
        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0,700,700);

        for(int i=0;i<3;i++){
        	g2.setColor(Color.ORANGE);
        	g2.fillRect(tablesX[i], tablesY[i], side, side);//200 and 250 need to be table params
        }
        for(restaurant_rancho.gui.Gui gui : rancho.guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }
        
        for(restaurant_rancho.gui.Gui gui: rancho.guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
	}
	public void createAmyRestaurant()
	{
		amy = new AmyRestaurantPanel();
	}
	public void runAmyRestaurant(Graphics g)
	{
        Graphics2D g2 = (Graphics2D)g;
        
        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(WINDOW_X, WINDOW_Y, this.getWidth(), this.getHeight() );

        //draw boundary
        g2.setColor(Color.BLACK);
        g2.drawRect(WINDOW_X, WINDOW_Y, 635, this.getHeight());
        
        //draw patio for waiter
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(1,this.getHeight()-140, 75 , 140);
        g2.setColor(Color.BLACK);
        g2.drawRect(1,this.getHeight()-140, 75 , 140);
        
       
        //draw cook station
        g2.setColor(Color.cyan);
        g2.fillRect(this.getWidth()-250,this.getHeight()-100, 230 , 100);
        
        //plating station
        g2.setColor(Color.GREEN);
        g2.fillRect(this.getWidth()-250,this.getHeight()-100, 150 , 35);
        g2.setColor(Color.BLACK);
        g2.drawRect(this.getWidth()-250,this.getHeight()-100, 150 , 35);
       
        //cooking station
        g2.setColor(Color.BLUE);
        g2.fillRect(this.getWidth()- 70,this.getHeight()-60, 50 , 60);
        g2.setColor(Color.WHITE);
        g2.drawOval(this.getWidth()- 60,this.getHeight()-55, 22, 22);
        g2.drawOval(this.getWidth()- 57,this.getHeight()-52, 15, 15);
        g2.drawOval(this.getWidth()- 60,this.getHeight()-25, 22, 22);
        g2.drawOval(this.getWidth()- 57,this.getHeight()-22, 15, 15);
     	g2.setColor(Color.BLACK);
     	g2.setFont(new Font(null, Font.PLAIN, 15));
     	g2.drawString("St", 410,this.getHeight()-90 );
     	g2.drawString("Ch", 440,this.getHeight()-90 );
     	g2.drawString("Sa", 475,this.getHeight()-90 );
     	g2.drawString("Pi", 510,this.getHeight()-90 );
		
	    //cashier station
     	g2.setColor(Color.CYAN);
        g2.fillRect(520,2, 70,100);
       
        //draw waiting station for customer
        g2.setColor(Color.black);
        g2.fillRect(1, 1, 80, 220);
        g2.setColor(Color.LIGHT_GRAY);
        
        //draw waiter home station
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(100,2, 300 , 70);
        g2.setColor(Color.BLACK);
        g2.drawRect(100,2, 300 , 70);
        
        for (int i = 0; i < 6; i ++){
        	 g2.setColor(Color.BLACK);
             g2.drawRect(100 + i*50,2, 35 , 35);
        }
        //draw cashier station
        //Here is the table
        g2.setColor(Color.YELLOW);
        
        g2.fillRect(hashTable.get(1),TABLE_Y, TABLE_W,TABLE_H);//200 and 250 need to be table parameter
        g2.fillRect(hashTable.get(2), TABLE_Y, TABLE_W,TABLE_H);
        g2.fillRect(hashTable.get(3), TABLE_Y,  TABLE_W	, TABLE_H);
        
        for(amyRestaurant.gui.Gui gui : amy.guis) 
        {
            if (gui.isPresent()) 
            {
                gui.updatePosition();
            }
        }
        
        for(amyRestaurant.gui.Gui gui : amy.guis) 
        {
            if (gui.isPresent()) 
            {
                gui.draw(g2);
            }
        }
	}
	public void createAndrewRestaurant()
	{
		andrew = new AndrewRestaurantPanel();
	}
	public void runAndrewRestaurant(Graphics g)
	{
        Graphics2D t1 = (Graphics2D)g;
        Graphics2D t2 = (Graphics2D)g;
        Graphics2D t3 = (Graphics2D)g;
        Graphics2D t4 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        t1.setColor(getBackground());
        t1.fillRect(ORIGIN, ORIGIN, WINDOWX, WINDOWY);

        //Tables
        t1.setColor(Color.ORANGE);
        t1.fillRect(tablePositions.get(1).getX(), tablePositions.get(1).getY(), TABLEH, TABLEW);
        t2.setColor(Color.ORANGE);
        t2.fillRect(tablePositions.get(2).getX(), tablePositions.get(2).getY(), TABLEH, TABLEW);
        t3.setColor(Color.ORANGE);
        t3.fillRect(tablePositions.get(3).getX(), tablePositions.get(3).getY(), TABLEH, TABLEW);
        t4.setColor(Color.ORANGE);
        t4.fillRect(tablePositions.get(4).getX(), tablePositions.get(4).getY(), TABLEH, TABLEW);
        
        for(restaurant_andrew.gui.Gui gui : andrew.guis) 
        {
            if (gui.isPresent()) 
            {
                gui.updatePosition();
            }
        }

        for(restaurant_andrew.gui.Gui gui : andrew.guis) 
        {
            if (gui.isPresent()) 
            {
                gui.draw(t1);
                gui.draw(t2);
                gui.draw(t3);
                gui.draw(t4);
            }
        }

	}
	public void createSidRestaurant()
	{
		sid = new SidRestaurantPanel();
	}
	public void runSidRestaurant(Graphics g)
	{
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, 700,700 );

        //Here is the table
        //g2.setColor(Color.ORANGE);
        //g2.fillRect(TABLEX, TABLEY, TABLESIZE, TABLESIZE);//200 and 250 need to be table params


        for(restaurant_sid.gui.Gui gui : sid.guis) 
        {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }

        for(restaurant_sid.gui.Gui gui : sid.guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
            }
        }
	}
	public void addResident(Building b, PersonAgent person){
		String name = "Resident";
		
		ResidentRole rr = null;
		for(Role r:person.roles){
			if(r instanceof ResidentRole){
				
				rr = (ResidentRole)r;
				r.active = true;
				break;
			}
		}
		
		LiveGui g =null;
		if(b.generaltype.equals("Resident")){
			
			if (!checkExistResidentGui(person))
			{
				g = new ResidentGui(person.gui,rr);
				rr.setGui(g);
				b.myBuildingPanel.residentAdd((ResidentGui)g);
					
			}
		}
		
		else if(b.generaltype=="Apartment"){
			
			
			if (!checkExistAResidentGui234(person))
			{
				int random = (int)(Math.random()*3);
				if(random == 0){
					g = new AResidentGui2(person.gui,rr);
					b.myBuildingPanel.residentAdd((AResidentGui2)g);
				}
				else if(random == 1){
					g = new AResidentGui3(person.gui,rr);
					b.myBuildingPanel.residentAdd((AResidentGui3)g);
				}
				else if(random == 2){
					g = new AResidentGui4(person.gui,rr);
					b.myBuildingPanel.residentAdd((AResidentGui4)g);
				}
				
				rr.setGui(g);
				landlord.addResident(rr,1);
				rr.setLord(landlord);
				
			}
		}
		
		//rr.backToHomePosition();
		//rr.msgGotHungry();
		//rr.msgGoToSleep();
		//rr.eatOut();
		
	}
	public void addBankTeller(Building b, PersonAgent p){
		int bankNum = Integer.parseInt(b.type.substring(b.type.length()-1)); 
		BankAgent ba = null;
		for (Role r : p.roles){
			if (r instanceof BankAgent){
				r.active = true;
				ba = (BankAgent) r;
				break;
			}
		}
		TellerGui g = null;
		System.out.println(bankHosts.size());
		BankHost appropriateHost = bankHosts.elementAt(0);
		
		g = new TellerGui(ba,bankTellers.size()%3);
		ba.setGui(g);
		ba.setHost(appropriateHost);
		bankTellers.add(ba);
		
		b.myBuildingPanel.bankAdd(g);

		ba.stateChanged();
	}

	public void addBankCustomer(Building b, PersonAgent person)
	{
		int bankNum = Integer.parseInt(b.type.substring(b.type.length()-1));
		BankCustomer c = null;
		for (Role r:person.roles)
		{
			if (r instanceof BankCustomer)
			{
				r.active = true;
				c = (BankCustomer) r;
				break;
			}
		}
		c.setHost(bankHosts.get(bankNum-1));
		bCustomerGui = new BCustomerGui(c);
		c.setGui(bCustomerGui);
		b.myBuildingPanel.bankAdd(bCustomerGui);
		bankCustomers.add(c);
		
    	c.stateChanged();
	}
	public void addBankRobber(Building b, PersonAgent person){
		int bankNum = Integer.parseInt(b.type.substring(b.type.length()-1));
		BankRobber r = null;
		for (Role c:person.roles){
			if (c instanceof BankRobber){
				c.active = true;
				r = (BankRobber) c;
				break;
			}
		}
		robberGui = new BankRobberGUI(r);
		r.setGUI(robberGui);
		b.myBuildingPanel.bankAdd(robberGui);
		//TODO: HACK - SETTING ARMED STATE
		r.setArmed(true);
		bankRobbers.add(r);
    	
	}
	public void addCustomer(Building b, PersonAgent person)
	{
		String name = "Customer";
		CustomerRole c = null;
		//CustomerAgent c = new CustomerAgent(name);
		for (Role r:person.roles)
		{
			if (r instanceof CustomerRole)
			{
				r.active = true;
				c = (CustomerRole) r;
				break;
			}
		}
		CustomerGui g=null;
		if (!checkExistCustomerGui(person))
		{
			g = new CustomerGui(c,person.gui,customers.size()+1);
			b.myBuildingPanel.restaurantAdd(g);
			c.setHost(host);
			c.setGui(g);
			c.setCashier(cashier);
			customers.add(c);
		}
		c.gotHungry();	
		if (name.equals("Flake")) //Non-Normative Scenarios handled
		{
			c.setCash(0);
		}
		else if (name.equals("Poor"))
		{
			c.setCash(9);
		}
		else if (name.equals("Rich"))
		{
			c.setCash(50);
		}
		else if (name.equals("Upperclass"))
		{
			c.setCash(30);
		}
		else if (name.equals("Lowerclass"))
		{
			c.setCash(10);
		}
		else if (name.equals("Check"))
		{
	        cook.checkInventory();
	        c.setCash(10);
		}
		else
		{
			c.setCash(11);
		}
	}
	public boolean checkExistCustomerGui(PersonAgent p)
	{
		for (Gui g:p.guis)
		{
			if (g instanceof CustomerGui)
			{
				return true;
			}
		}
		return false;
	}
	public boolean checkExistResidentGui(PersonAgent p)
	{
		for (Gui g:p.guis){
			if (g instanceof ResidentGui && !(g instanceof AResidentGui1)){
				return true;
			}
		}
		return false;
	}
	public boolean checkExistAResidentGui1(PersonAgent p)
	{
		for (Gui g:p.guis){
			if (g instanceof AResidentGui1){
				return true;
			}
		}
		return false;
	}
	public boolean checkExistAResidentGui234(PersonAgent p){
		for (Gui g:p.guis){
			if (g instanceof AResidentGui2 || g instanceof AResidentGui3 || g instanceof AResidentGui4){
				return true;
			}
		}
		return false;
	}
	public CustomerGui getExistGui(PersonAgent p)
	{
		for (Gui g:p.guis)
		{
			if (g instanceof CustomerGui)
			{
				return (CustomerGui) g;
			}
		}
		return null;
	}
	public boolean checkExistWaiterGui(PersonAgent p)
	{
		for (Gui g:p.guis)
		{
			if (g instanceof WaiterGui)
			{
				return true;
			}
		}
		return false;
	}
	public void addNormalWaiter(Building b, PersonAgent person)
	{
		NormalWaiterRole c = null;
		for (Role r:person.roles)
		{
			if (r instanceof NormalWaiterRole)
			{
				r.active = true;
				c = (NormalWaiterRole) r;
				break;
			}
		}
		WaiterGui g=null;
		if (!checkExistWaiterGui(person))
		{
			g = new WaiterGui(c,person.gui,waiters.size()+1);
			foodGui = new FoodGui();
	        c.setGui(g);
	        c.setGui(foodGui);
	        c.setHost(host);
	        c.setCook(cook);
	        c.setCashier(cashier);
	        waiters.add(c);
	        host.setWaiter(c);
	        
	        b.myBuildingPanel.restaurantAdd(g);
	        b.myBuildingPanel.restaurantAdd(foodGui);
		}
		host.stateChanged();
	}
	public void addWaiter(Building b, PersonAgent person)
	{
		WaiterRole c = null;
		for (Role r:person.roles)
		{
			if (r instanceof WaiterRole)
			{
				r.active = true;
				c = (WaiterRole) r;
				break;
			}
		}
		WaiterGui g=null;
		if (!checkExistWaiterGui(person))
		{
			g = new WaiterGui(c,person.gui,waiters.size()+1);
			foodGui = new FoodGui();
	        c.setGui(g);
	        c.setGui(foodGui);
	        c.setHost(host);
	        c.setCook(cook);
	        c.setCashier(cashier);
	        waiters.add(c);
	        host.setWaiter(c);
	        
	        b.myBuildingPanel.restaurantAdd(g);
	        b.myBuildingPanel.restaurantAdd(foodGui);
		}
		host.stateChanged();
	}
	
	public CookAgent getCreatedCook(){
		return cook;
	}
	
	public MarketAgent getCreatedMarket(){
		return market;
	}
	
	
	public void addBankHost(Building b, BankHost newHost){
		bHostGui = new HostGui(newHost);
    	newHost.setGui(bHostGui);
    	
		bankHosts.add(newHost);
    	b.myBuildingPanel.bankAdd(bHostGui);

	}

	public void addBusPassenger(Building b, PersonAgent person){
		BusPassenger bp = new BusPassengerRole(person.name, 4);
		for(Role r:person.roles){
			if(r instanceof BusPassenger){
				bp = (BusPassenger)r;
				break;
			}
		}
		BusPassengerGui g;
		if (!checkExistBusPassengerGui(person))
		{
			g = new BusPassengerGui(person.gui, bp, true);
			bp.setGui(g);
			b.myBuildingPanel.busStopAdd(g);
			
		}
		bp.msgEnteredStop();
	}
	public boolean checkExistsTellerGui(PersonAgent p){
		for (Gui g:p.guis){
			if (g instanceof TellerGui){
				return true;
			}
		}
		return false;
	}
	public boolean checkExistBusPassengerGui(PersonAgent p)
	{
		for (Gui g:p.guis){
			if (g instanceof BusPassengerGui){
				return true;
			}
		}
		return false;
	}
	
	public void setCityPanel(CityPanel c) {
		this.c = c;
	}
	
	
	
	public void addLandlord(Building b, PersonAgent person) {
		// TODO Auto-generated method stub
		Landlord rr = null;
		for(Role r:person.roles){
			if(r instanceof Landlord){
				r.active = true;
				rr = (Landlord)r;
				landlord = rr;
				break;
			}
		}
		
		LiveGui g =null;
	
		if (!checkExistAResidentGui1(person))
		{
			
			g = new AResidentGui1(person.gui,rr);
			b.myBuildingPanel.residentAdd((AResidentGui1)g);
			
			rr.setGui(g);
				
				
		}
		
		
	}
	
	
	
}