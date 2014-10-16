package restaurant_sid.gui;

import restaurant.gui.FoodGui;
import restaurant.gui.WaiterGui;
import restaurant_sid.gui.CustomerGui;
import restaurant_sid.interfaces.Waiter;
import restaurant_sid.SidCustomerRole;
import restaurant_sid.SidHostAgent;
import restaurant_sid.SidNormalWaiterRole;
import restaurant_sid.SidWaiterRole;
import restaurant_sid.SidCookAgent;
import restaurant_sid.SidMarketAgent;
import restaurant_sid.SidCashierAgent;

import javax.swing.*;

import Role.CustomerRole;
import Role.Role;
import Role.WaiterRole;
import SimCity.Building;
import SimCity.PersonAgent;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
@SuppressWarnings("serial")
public class SidRestaurantPanel extends JPanel {

    //Host, cook, waiters and customers
    private SidHostAgent host = new SidHostAgent("Kyle");
    private SidCookAgent cook = new SidCookAgent("Mark");
    private SidMarketAgent mark = new SidMarketAgent("Costco");
    private SidMarketAgent mark2 =  new SidMarketAgent("Sysco");
    private SidMarketAgent mark3 = new SidMarketAgent("Albertsons");
    private SidCashierAgent cash = new SidCashierAgent("Debra");
    private HostGui hostGui = new HostGui(host);
    private CookGui cookGui = new CookGui(cook);
    public List<Gui> guis = new ArrayList<Gui>();

    private Vector<SidCustomerRole> customers = new Vector<SidCustomerRole>();
    public Vector<Waiter> waiters = new Vector<Waiter>();
    
    public SidRestaurantPanel() 
    {
        host.setGui(hostGui);
        cook.setGui(cookGui);
        cook.addMarket(mark);
        cook.addMarket(mark2);
        cook.addMarket(mark3);
        
        mark.addCashier(cash);
        mark2.addCashier(cash);
        mark3.addCashier(cash);
        
        guis.add(cookGui);
        guis.add(hostGui);
        host.startThread();
        cook.startThread();
        mark.startThread();
        mark2.startThread();
        mark3.startThread();
        cash.startThread();
    }
	public void addSidCustomer(Building b, PersonAgent person)
	{
		SidCustomerRole c = null;
		for (Role r:person.roles)
		{
			if (r instanceof SidCustomerRole)
			{
				r.active = true;
				c = (SidCustomerRole) r;
				break;
			}
		}
		CustomerGui g=null;
		g = new CustomerGui(c,person.gui,customers.size()+1);
		guis.add(g);
		c.setHost(host);
		c.setGui(g);
		customers.add(c);
		c.gotHungry();	
	}
	public void addSidWaiter(Building b, PersonAgent person)
	{
		String name = "Waiter";
		SidWaiterRole c = null;
		for (Role r:person.roles)
		{
			if (r instanceof SidWaiterRole)
			{
				r.active = true;
				c = (SidWaiterRole) r;
				break;
			}
		}
		WaiterGUI g = new WaiterGUI(c);
		guis.add(g);
		c.setHost(host);
		c.setCashier(cash);
		c.setGui(g);
		c.setCook(cook);
		host.addWaiter(c);
		waiters.add(c);
		host.stateChanged();
	}
	public void addSidNormalWaiter(Building b, PersonAgent person)
	{
		String name = "Waiter";
		SidNormalWaiterRole c = null;
		for (Role r:person.roles)
		{
			if (r instanceof SidNormalWaiterRole)
			{
				r.active = true;
				c = (SidNormalWaiterRole) r;
				break;
			}
		}
		WaiterGUI g = new WaiterGUI(c);
		guis.add(g);
		c.setHost(host);
		c.setCashier(cash);
		c.setGui(g);
		c.setCook(cook);
		host.addWaiter(c);
		waiters.add(c);
		host.stateChanged();
	}
}
