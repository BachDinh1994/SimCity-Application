package amyRestaurant.gui;
import javax.swing.*;

import amyRestaurant.AmyCustomerRole;
import amyRestaurant.AmyWaiterRole;
import amyRestaurant.gui.AmyCustomerGui;
import amyRestaurant.gui.AmyWaiterGui;
import amyRestaurant.interfaces.AmyWaiter;
import Role.Role;
import SimCity.Building;
import SimCity.PersonAgent;
import agent.Agent;
import amyRestaurant.AmyBankAgent;
import amyRestaurant.AmyCashierAgent;
import amyRestaurant.AmyCookAgent;
import amyRestaurant.AmyCustomerRole;
import amyRestaurant.AmyHostAgent;
import amyRestaurant.AmyMarketAgent;
import amyRestaurant.AmyNormalWaiterRole;
import amyRestaurant.AmyWaiterRole;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class AmyRestaurantPanel extends JPanel {

	private static final int pRow = 5, pCol =1, pSpace = 2 ,NWAITERS = 2; //panel row, column, spacing
	private List<Agent> allAgents = new ArrayList<Agent>(); //array of all agents
    public List<Gui> guis = new ArrayList<Gui>();
	int loc = 0;
	int xloc = 5;
	
	int wHome =50;
	boolean toggle = false;

	//Host, cook, waiters and customers
	private AmyHostAgent host = new AmyHostAgent("Host h");

	private AmyMarketAgent market1 = new AmyMarketAgent("Meat1");
	{
		market1.setInventory("Steak", 30);
		market1.setInventory("Chicken", 5);
	}
	private AmyMarketAgent market2 = new AmyMarketAgent("Meat2");{
		market2.setInventory("Steak", 10);
		market2.setInventory("Chicken", 30);
	}
	private AmyMarketAgent market3 = new AmyMarketAgent("Italian1");{
		market3.setInventory("Pizza", 30);
		market3.setInventory("Salad", 20);
	}
	private AmyMarketAgent market4 = new AmyMarketAgent("Italian2");{
		market4.setInventory("Pizza", 10);
		market4.setInventory("Salad", 1);
	}
	private AmyBankAgent bank = new AmyBankAgent("Bank b");
	private AmyCookAgent cook = new AmyCookAgent("cook c");
	private AmyCashierAgent cashier = new AmyCashierAgent("Cashier c");

	private Vector<AmyCustomerRole> customers = new Vector<AmyCustomerRole>();
	private Vector<AmyWaiter> waiters = new Vector<AmyWaiter>();
	private List<AmyMarketAgent> markets = new ArrayList<AmyMarketAgent>();
	{
		markets.add(market1);
		markets.add(market2);
		markets.add(market3);
		markets.add(market4);

	}
	private ImageIcon myIcon = new ImageIcon("C:/Users/Amy/restaurant_lee779/fighton.jpg");

	private RestaurantGui gui; //reference to main gui

	public AmyRestaurantPanel() 
	{
		host.setGui(new AmyHostGui(host));
		host.setCook(cook);
		host.startThread();
		cook.startThread();
		cook.setGui(new AmyCookGui(cook,gui));
		guis.add(cook.amyCookGui);// dw
		cashier.setBank(bank);
		bank.setAccount(cashier, 1000.0);
		bank.startThread();
		cashier.startThread();

		allAgents.add(host);
		allAgents.add(cook);
		allAgents.add(cashier);
		allAgents.add(bank);

		for (int i = 0; i < markets.size(); i++){
			allAgents.add(markets.get(i));
			markets.get(i).setCook(cook);
			markets.get(i).setCashier(cashier);
			markets.get(i).startThread();
		}
		cook.setMarket(markets);
	}
	public void addAmyCustomer(Building b, PersonAgent person) 
	{
		AmyCustomerRole c = null;
		for (Role r:person.roles)
		{
			if (r instanceof AmyCustomerRole)
			{
				r.active = true;
				c = (AmyCustomerRole) r;
				break;
			}
		}
		c.setCash(1000);
		AmyCustomerGui g=null;
		g = new AmyCustomerGui(c,person.gui);
		g.setDestination(xloc, 180 - loc);
		loc = loc + 50;
		if(loc > 180){
			loc = 0;
			if(!toggle){
				xloc = xloc + 30;
				toggle = true;
			}
			else{
				xloc = 5;
			}
			
		}
		guis.add(g);
		c.setHost(host);
		c.setGui(g);
		c.setCashier(cashier);
		customers.add(c);
		g.setHungry();
		//
	}
	public void addAmyWaiter(Building b, PersonAgent person) 
	{
		AmyWaiterRole c = null;
		for (Role r:person.roles)
		{
			if (r instanceof AmyWaiterRole)
			{
				r.active = true;
				c = (AmyWaiterRole) r;
				break;
			}
		}
		AmyWaiterGui g = new AmyWaiterGui(c);
		wHome = wHome + 50;
		if(wHome > 350){
			g.setHome(-30, 2);
		}
		else{
		g.setHome(wHome, 2);}
		guis.add(g);
		c.setHost(host);
		c.setCashier(cashier);
		c.setGui(g);
		c.setCook(cook);
		cook.setWaiter(c);
		cook.addWaiter(c);
		host.addWaiters(c);
		waiters.add(c);
		host.stateChanged();
	}  
	public void addAmyNormalWaiter(Building b, PersonAgent person) 
	{
		AmyNormalWaiterRole c = null;
		for (Role r:person.roles)
		{
			if (r instanceof AmyNormalWaiterRole)
			{
				r.active = true;
				c = (AmyNormalWaiterRole) r;
				break;
			}
		}
		AmyWaiterGui g = new AmyWaiterGui(c);
		wHome = wHome + 50;
		if(wHome > 350){
			g.setHome(-30, 2);
		}
		else{
		g.setHome(wHome, 2);}
		guis.add(g);
		c.setHost(host);
		c.setCashier(cashier);
		c.setGui(g);
		c.setCook(cook);
		cook.setWaiter(c);
		cook.addWaiter(c);
		host.addWaiters(c);
		waiters.add(c);
		host.stateChanged();
	}  
}