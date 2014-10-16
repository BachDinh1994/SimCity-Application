package restaurant_rancho.gui;

import restaurant_rancho.RanchoCashier;
import restaurant_rancho.RanchoCook;
import restaurant_rancho.RanchoCustomerRole;
import restaurant_rancho.RanchoHost;
import restaurant_rancho.RanchoManager;
import restaurant_rancho.RanchoMarket;
import restaurant_rancho.RanchoNormalWaiterRole;
import restaurant_rancho.RanchoWaiterRole;
import restaurant_rancho.gui.Gui;
import restaurant_rancho.interfaces.Waiter;

import javax.swing.*;

import Role.Role;
import SimCity.Building;
import SimCity.PersonAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RanchoRestaurantPanel extends JPanel {
	//The size of the window
	private final int WINDOWX = 500;
	private final int WINDOWY = 450;
    
	//Host, cook, waiters and customers
    private RanchoHost host;
    private RanchoCook cook;
    private RanchoCashier cashier;
    private RanchoManager manager;
    private Vector<RanchoCustomerRole> customers = new Vector<RanchoCustomerRole>();
    private Vector<Waiter> waiters = new Vector<Waiter>();
    public List<Gui> guis = new ArrayList<Gui>();
    //Constructor
    public RanchoRestaurantPanel() 
    {
        cook = new RanchoCook("Cook");
        host = new RanchoHost("Sarah");
        cashier = new RanchoCashier("Cashier");
        
        host.startThread();
        cook.startThread();
        cashier.startThread();
        RanchoMarket market1 = new RanchoMarket("Market1");
        market1.setCashier(cashier);
        RanchoMarket market2 = new RanchoMarket("Market2");
        market2.setCashier(cashier);
        RanchoMarket market3 = new RanchoMarket("Market3");
        market3.setCashier(cashier);
        market1.startThread();
        market2.startThread();
        market3.startThread();
        cook.addMarket(market1);
        cook.addMarket(market2);
        cook.addMarket(market3);
              
        CookGui c = new CookGui(cook);
        guis.add((Gui) c);
        cook.setGui(c);
    }
	public void addRanchoCustomer(Building b, PersonAgent person) 
	{
		RanchoCustomerRole c = null;
		for (Role r:person.roles)
		{
			if (r instanceof RanchoCustomerRole)
			{
				r.active = true;
				c = (RanchoCustomerRole) r;
				break;
			}
		}
		CustomerGui g=null;
		g = new CustomerGui(c,person.gui);
		guis.add(g);
		c.setHost(host);
		c.setCashier(cashier);
		c.setGui(g);
		customers.add(c);
		g.setHungry();
	}
	public void addRanchoWaiter(Building b, PersonAgent person) 
	{
		RanchoWaiterRole c = null;
		for (Role r:person.roles)
		{
			if (r instanceof RanchoWaiterRole)
			{
				r.active = true;
				c = (RanchoWaiterRole) r;
				break;
			}
		}
		WaiterGui g = new WaiterGui(c,waiters.size(),person.gui);
		guis.add(g);
		c.setHost(host);
		c.setCashier(cashier);
		c.setGui(g);
		c.setCook(cook);
		host.addWaiter(c);
		waiters.add(c);
		host.stateChanged();
		manager.addStaff(c);
		c.setManager(manager);
	}   
	public void addRanchoNormalWaiter(Building b, PersonAgent person) 
	{
		RanchoNormalWaiterRole c = null;
		for (Role r:person.roles)
		{
			if (r instanceof RanchoNormalWaiterRole)
			{
				r.active = true;
				c = (RanchoNormalWaiterRole) r;
				break;
			}
		}
		WaiterGui g = new WaiterGui(c,waiters.size(),person.gui);
		guis.add(g);
		c.setHost(host);
		c.setCashier(cashier);
		c.setGui(g);
		c.setCook(cook);
		host.addWaiter(c);
		waiters.add(c);
		host.stateChanged();
		manager.addStaff(c);
		c.setManager(manager);
	} 
	public void addRanchoManager(Building b, PersonAgent person){
		RanchoManager c = null;
		for(Role r:person.roles){
			if(r instanceof RanchoManager){
				r.active = true;
				c = (RanchoManager) r;
				break;
			}
		}
		ManagerGui g = new ManagerGui(c,person.gui);
		manager = c;
		guis.add(g);
		c.setHost(host);
		c.setCook(cook);
		c.setCashier(cashier);
		c.setGui(g);
		host.setManager(c);
		cook.setManager(c);
		cashier.setManager(c);
	}
}
