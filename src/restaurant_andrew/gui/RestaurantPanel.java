package restaurant_andrew.gui;

import restaurant_andrew.AndrewCashierAgent;
import restaurant_andrew.AndrewCookAgent;
import restaurant_andrew.AndrewCustomerRole;
import restaurant_andrew.AndrewHostAgent;
import restaurant_andrew.AndrewMarketAgent;
import restaurant_andrew.AndrewWaiterRole;

import javax.swing.*;

import java.awt.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {

    //Host, cook, waiters and customers
    private AndrewHostAgent host = new AndrewHostAgent("Host");

    private Vector<AndrewCustomerRole> customers = new Vector<AndrewCustomerRole>();

    private Vector<AndrewWaiterRole> waiters = new Vector<AndrewWaiterRole>();

    private AndrewCookAgent cook = new AndrewCookAgent("Cook");
    private CookGui cookGui = new CookGui(cook);

    private AndrewCashierAgent cashier = new AndrewCashierAgent("Cashier");
    
    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private ListPanel waiterPanel = new ListPanel(this, "Waiters");
    private JPanel group = new JPanel();

    private RestaurantGui gui; //reference to main gui

    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;

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
        gui.animationPanel.addGui(cookGui);
        
        setLayout(new GridLayout(/*rows*/ 1, /*cols*/ 2, /*hgap*/ 20, /*vgap*/ 20));
        group.setLayout(new GridLayout(/*rows*/ 1, /*cols*/ 2, /*hgap*/ 10, /*vgap*/ 20));

        group.add(waiterPanel);
        group.add(customerPanel);

        initRestLabel();
        add(restLabel);
        add(group);
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        restLabel.add(new JLabel("               "), BorderLayout.EAST);
        restLabel.add(new JLabel("               "), BorderLayout.WEST);
    }

    /**
     * When a customer or waiter is clicked, this function calls
     * updatedInfoPanel() from the main gui so that person's information
     * will be shown
     *
     * @param type indicates whether the person is a customer or waiter
     * @param name name of person
     */
    public void showInfo(String type, String name) {

        if (type.equals("Customers")) {

            for (int i = 0; i < customers.size(); i++) {
                AndrewCustomerRole temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
        if (type.equals("Waiters")) {

            for (int i = 0; i < waiters.size(); i++) {
                AndrewWaiterRole temp = waiters.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name, boolean isHungry) {

    	if (type.equals("Customers")) {
    		AndrewCustomerRole c = new AndrewCustomerRole(name);	
    		CustomerGui g = new CustomerGui(c, gui);
    		if (isHungry) g.setHungry();

    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setGui(g);
    		customers.add(c);
    	}
    	if (type.equals("Waiters")) {
    		AndrewWaiterRole w = new AndrewWaiterRole(name);	
    		WaiterGui g = new WaiterGui(w);

    		gui.animationPanel.addGui(g);// dw
    		host.addWaiter(w);
    		g.setHome(50, 50 + 30 * waiters.size());
    		w.setHost(host);
    		w.setCook(cook);
    		w.setCashier(cashier);
    		w.setGui(g);
    		waiters.add(w);
    	}
    }
    
}
