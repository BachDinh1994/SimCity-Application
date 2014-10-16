package SimCity;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

import busstop.BusAgent;
import busstop.TicketMachineAgent;
import busstop.gui.BusInsideGui;
import restaurant.CookAgent;
import market.DeliveryTruckAgent;
import market.MarketAgent;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("serial")
public class SimCity extends JFrame 
{
    CityPanel cityPanel;
    JPanel buildingPanels;
    CardLayout cardLayout;
	final ArrayList<Building> buildings;
	
	//need to set the cook to market connection after creating them
	   public Vector<MarketAgent> markets = new Vector<MarketAgent>();
	   public Vector<CookAgent> cooks = new Vector<CookAgent>();
	   ////
    public SimCity(Clock c) 
    {
        setLayout(new BorderLayout());
        cityPanel = new CityPanel(c);
        cityPanel.setPreferredSize(new Dimension(950, 700));

        cardLayout = new CardLayout()
        {
        	public void show(Container parent, String name)
        	{
        		super.show(parent,name);
        		
        	}
        };
        
        //zoomed in view of the buildingss are created in buildingPanels
        buildingPanels = new JPanel();
        buildingPanels.setLayout(cardLayout);
        buildingPanels.setPreferredSize(new Dimension(450, 450));
        buildingPanels.setBackground(Color.gray);
        
        //Create the BuildingPanel for each Building object
        buildings = cityPanel.getBuildings();
        for (int i=0; i<buildings.size(); i++) 
        {
            Building b = buildings.get(i);
            BuildingPanel bp = new BuildingPanel(b, i, this);
            b.setBuildingPanel(bp);
            buildingPanels.add(bp, "" + i);
            bp.setCityPanel(cityPanel);
        }
        int countR = 1;
        int countM = 1;
		int busStopNum = 0;
        
    	for (Building b:buildings)
    	{

    		if (b.generaltype.equals("Restaurant")) //use general type for everything
    		{
    			b.myBuildingPanel.createRestaurant(b, countR); //prevent access of waiters or any other agents in other restaurants
    			countR ++;
    			cooks.add(b.myBuildingPanel.getCreatedCook() );
    		}
    		else if (b.generaltype.equals("SidRestaurant"))
    		{
    			b.myBuildingPanel.createSidRestaurant(); //prevent access of waiters or any other agents in other restaurants
    		}
    		else if (b.generaltype.equals("RanchoRestaurant"))
    		{
    			b.myBuildingPanel.createRanchoRestaurant(); //prevent access of waiters or any other agents in other restaurants
    		}
    		else if (b.generaltype.equals("AndrewRestaurant"))
    		{
    			b.myBuildingPanel.createAndrewRestaurant(); //prevent access of waiters or any other agents in other restaurants
    		}
    		else if (b.generaltype.equals("AmyRestaurant"))
    		{
    			b.myBuildingPanel.createAmyRestaurant(); //prevent access of waiters or any other agents in other restaurants
    		}
    		if (b.generaltype.equals("Bank")){

    			b.myBuildingPanel.createBank(b);
    		}
    		if (b.generaltype.equals("Market")){
    			b.myBuildingPanel.createMarket(b, countM);
    			markets.add(b.myBuildingPanel.getCreatedMarket());

    			countM ++;
    		}
    		if (b.generaltype.equals("BusStop")) {
    			b.myBuildingPanel.createBusStop(b, busStopNum);
    			if (busStopNum < 10) {
    				BusAgent bus = new BusAgent(busStopNum);
        			BusOutsideGui busGui = new BusOutsideGui(bus, cityPanel, ((Integer)busStopNum).toString());
    				bus.setOutsideGui(busGui);
    				bus.setInsideGui(new BusInsideGui(bus, busGui));
    				cityPanel.buses.add(bus);
    				b.myBuildingPanel.buses.add(bus);
    			}
    			busStopNum++;
    		}
            
		for (TicketMachineAgent tm : cityPanel.ticketMachines) {
			tm.startThread();
		}
    	for (BusAgent ba : cityPanel.buses) {
    		for (TicketMachineAgent tm : cityPanel.ticketMachines) {
    			ba.addStop(tm);
    		}
    		ba.startThread();
    	}
    	}
    	
    	//creating a trucks list in cityPanel of all the deliveryTruck in city world
    	for (Building bd: buildings){
    		//setting correct address for the deliverytruck To use
    		if(bd.generaltype.equals("Restaurant")){
    			CookAgent cook = bd.myBuildingPanel.getCreatedCook();
    			cook.setAddress(new Address(bd.x+50, bd.y+10));
    		}
    		else if(bd.generaltype.equals("Market")){
    			
    			//System.out.println("truck created created");
    			bd.myBuildingPanel.deliveryTruck.setMyAddress(new Address(bd.x+50, bd.y+10));
    			
    			DeliveryTruckAgent truck = bd.myBuildingPanel.deliveryTruck;
        		DeliveryTruckInCityGui d = new DeliveryTruckInCityGui(truck);
        		d.setHome(truck.getMyAddress());
            	truck.getGui().setInCityGui(d);
            	truck.setCityGui(d);
				cityPanel.trucks.add(d);//}
    		}
    	}
    	
    	for (Building bd: buildings){
    		if(bd.type.equals("Market")){
			bd.myBuildingPanel.mCustomer.msgEnteredMarket(bd.myBuildingPanel.market);
    		}
    	}
    	setUpCookMarketConnection();
        add(cityPanel,BorderLayout.WEST);
        add(buildingPanels,BorderLayout.EAST);

    } 
    private void setUpCookMarketConnection() {
		// TODO Auto-generated method stub
		for (int i =0; i<cooks.size(); i++){
			cooks.get(i).setMarkets(markets);
		}
	
	}
	public void show(String name)
	{
        for (int i=0; i<buildings.size(); i++) 
        {
            Building b = buildings.get(i);
            if (b.myBuildingPanel.getName().equals(name))
            {
            	b.myBuildingPanel.setVisible(true);
            }
            else
            {
            	b.myBuildingPanel.setVisible(false);
            }
        }
		//super.show(parent,name);
		//cityPanel.repaint();
	}
    public void displayBuildingPanel(BuildingPanel bp) 
    {
        //System.out.println(bp.getName());
        cardLayout.show(buildingPanels, bp.getName());
        /*ArrayList<Building> buildings = cityPanel.getBuildings();
        for (int i=0; i<buildings.size(); i++) 
        {
            Building b = buildings.get(i);
            if (!b.myBuildingPanel.isVisible())
            {
            	//b.myBuildingPanel.repaint();
                System.out.println(b.myBuildingPanel.getName());
            }
        }
        System.out.println();*/
    }
	public static void main( String[] args ) 
    {	
		Clock clock = new Clock();

    	SimCity sc = new SimCity(clock);
    	sc.setVisible(true);
        sc.setSize(1400, 700);
        sc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    	JFrame clockFrame = new JFrame();
    	clockFrame.add(new JLabel("SimCity Clock"));
    	clockFrame.setVisible(true);
    	clockFrame.setSize(200,250);
    	clockFrame.add(clock);
    	clockFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	SimCityTest t = new SimCityTest(sc);
    	t.setVisible(true);
    	t.setSize(300,600);
    	t.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	
    	try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File("Images/Skyrim Theme.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        }
        
        catch(UnsupportedAudioFileException uae) {
            System.out.println(uae);
        }
        catch(IOException ioe) {
            System.out.println(ioe);
        }
        catch(LineUnavailableException lua) {
            System.out.println(lua);
        }
    }
}