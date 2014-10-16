package restaurant_rancho.gui;

import restaurant_rancho.RanchoCashier;
import restaurant_rancho.RanchoCook;
import restaurant_rancho.RanchoCustomerRole;
import restaurant_rancho.RanchoHost;
import restaurant_rancho.RanchoMarket;
import restaurant_rancho.RanchoWaiterRole;
import restaurant_rancho.interfaces.Customer;
import restaurant_rancho.interfaces.Waiter;

import javax.swing.*;

import java.awt.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {
	//The size of the window
	private final int WINDOWX = 500;
	private final int WINDOWY = 450;
    
	//Host, cook, waiters and customers
    private RanchoHost host;
    private RanchoCook cook;
    private RanchoCashier cashier;
    private Vector<RanchoCustomerRole> customers = new Vector<RanchoCustomerRole>();
    private Vector<RanchoWaiterRole> waiters = new Vector<RanchoWaiterRole>();
    
    //Constructor
    public RestaurantPanel() 
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
        
        
        CookGui c = new CookGui(cook,gui);
        gui.animationPanel.addGui(c);
        cook.setGui(c);
    }    
}
