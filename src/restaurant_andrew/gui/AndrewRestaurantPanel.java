package restaurant_andrew.gui;

import restaurant_andrew.AndrewCashierAgent;
import restaurant_andrew.AndrewCookAgent;
import restaurant_andrew.AndrewCustomerRole;
import restaurant_andrew.AndrewHostAgent;
import restaurant_andrew.AndrewMarketAgent;
import restaurant_andrew.AndrewNormalWaiterRole;
import restaurant_andrew.AndrewWaiterRole;
import restaurant_andrew.AndrewCustomerRole;
import restaurant_andrew.AndrewWaiterRole;
import restaurant_andrew.gui.CustomerGui;
import restaurant_andrew.gui.WaiterGui;
import restaurant_andrew.interfaces.Waiter;

import javax.swing.*;

import Role.Role;
import SimCity.Building;
import SimCity.PersonAgent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class AndrewRestaurantPanel extends JPanel {

    //Host, cook, waiters and customers
    private AndrewHostAgent host = new AndrewHostAgent("Host");

    private Vector<AndrewCustomerRole> customers = new Vector<AndrewCustomerRole>();

    private Vector<Waiter> waiters = new Vector<Waiter>();

    private AndrewCookAgent cook = new AndrewCookAgent("Cook");
    private CookGui cookGui = new CookGui(cook);

    private AndrewCashierAgent cashier = new AndrewCashierAgent("Cashier");
    public List<Gui> guis = new ArrayList<Gui>();
    public AndrewRestaurantPanel()
    {
        host.startThread();
        cook.startThread();
        cashier.startThread();
        
        cook.addMarket(new AndrewMarketAgent("m1"));
        cook.addMarket(new AndrewMarketAgent("m2"));
        cook.addMarket(new AndrewMarketAgent("m3"));
        
        for (AndrewMarketAgent m : cook.markets) {
        	m.setCashier(cashier);
        }
        cook.setGui(cookGui);
        guis.add(cookGui);
    }
	public void addAndrewCustomer(Building b, PersonAgent person) 
	{
		AndrewCustomerRole c = null;
		for (Role r:person.roles)
		{
			if (r instanceof AndrewCustomerRole)
			{
				r.active = true;
				c = (AndrewCustomerRole) r;
				break;
			}
		}
		CustomerGui g=null;
		g = new CustomerGui(c,person.gui);
		guis.add(g);
		c.setHost(host);
		c.setGui(g);
		customers.add(c);
		g.setHungry();
	}
	public void addAndrewWaiter(Building b, PersonAgent person) 
	{
		AndrewWaiterRole c = null;
		for (Role r:person.roles)
		{
			if (r instanceof AndrewWaiterRole)
			{
				r.active = true;
				c = (AndrewWaiterRole) r;
				break;
			}
		}
		WaiterGui g = new WaiterGui(c);
		g.setHome(50, 50 + 30 * waiters.size());
		guis.add(g);
		c.setHost(host);
		c.setCashier(cashier);
		c.setGui(g);
		c.setCook(cook);
		host.addWaiter(c);
		waiters.add(c);
		host.stateChanged();
	}   
	public void addAndrewNormalWaiter(Building b, PersonAgent person) 
	{
		AndrewNormalWaiterRole c = null;
		for (Role r:person.roles)
		{
			if (r instanceof AndrewNormalWaiterRole)
			{
				r.active = true;
				c = (AndrewNormalWaiterRole) r;
				break;
			}
		}
		WaiterGui g = new WaiterGui(c);
		g.setHome(50, 50 + 30 * waiters.size());
		guis.add(g);
		c.setHost(host);
		c.setCashier(cashier);
		c.setGui(g);
		c.setCook(cook);
		host.addWaiter(c);
		waiters.add(c);
		host.stateChanged();
	}   
}
