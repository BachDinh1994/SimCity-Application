package restaurant_sid.gui;

import restaurant_sid.SidCustomerRole;
import restaurant_sid.SidHostAgent;
import restaurant_sid.SidWaiterRole;
import restaurant_sid.SidCookAgent;
import restaurant_sid.SidMarketAgent;
import restaurant_sid.SidCashierAgent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {

    //Host, cook, waiters and customers
    private SidHostAgent host = new SidHostAgent("Kyle");
    private SidCookAgent cook = new SidCookAgent("Mark");
    private SidMarketAgent mark = new SidMarketAgent("Costco");
    private SidMarketAgent mark2 =  new SidMarketAgent("Sysco");
    private SidMarketAgent mark3 = new SidMarketAgent("Albertsons");
    private SidCashierAgent cash = new SidCashierAgent("Debra");
    private HostGui hostGui = new HostGui(host);
    private CookGui cookGui = new CookGui(cook);
    private boolean running = true;
    

    private Vector<SidCustomerRole> customers = new Vector<SidCustomerRole>();
    private Vector<SidWaiterRole> waiters = new Vector<SidWaiterRole>();

    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private WaiterPanel waiterPanel = new WaiterPanel(this, "Waiters");
    private JPanel group = new JPanel();

    private RestaurantGui gui; //reference to main gui

    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;
        host.setGui(hostGui);
        cook.setGui(cookGui);
        cook.addMarket(mark);
        cook.addMarket(mark2);
        cook.addMarket(mark3);
        
        mark.addCashier(cash);
        mark2.addCashier(cash);
        mark3.addCashier(cash);
        
        gui.animationPanel.addGui(cookGui);
        gui.animationPanel.addGui(hostGui);
        host.startThread();
        cook.startThread();
        mark.startThread();
        mark2.startThread();
        mark3.startThread();
        cash.startThread();

        setLayout(new GridLayout(1, 2, 20, 20));
        group.setLayout(new GridLayout(1, 2, 10, 10));

        group.add(customerPanel);
        group.add(waiterPanel);

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
            	SidCustomerRole temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        } else if (type.equals("Waiters")){
        	for (int i = 0; i < waiters.size(); i++){
        		SidWaiterRole temp = waiters.get(i);
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
    public void addPerson(String type, String name) {

    	if (type.equals("Customers")) {
    		SidCustomerRole c = new SidCustomerRole(name);	
    		CustomerGui g = new CustomerGui(c, gui);

    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setGui(g);
    		customers.add(c);
    		//c.startThread();
    	} else if (type.equals("Waiters")){
    		SidWaiterRole w = new SidWaiterRole(name,waiters.size());
    		WaiterGUI g = new WaiterGUI(w);
    		
    		gui.animationPanel.addGui(g);
    		w.setHost(host);
    		w.setCashier(cash);
    		w.setGui(g);
    		w.setCook(cook);
    		host.addWaiter(w);
    		waiters.add(w);
    		//w.startThread();
    	}
    }
    public void makeHungry(String name){
    	for (SidCustomerRole c:customers){
    		if (c.getCustomerName().equals(name)){
    			c.getGui().setHungry();
    		}
    	
    	}
    }
    /*
    public void pauseRestaurant(){
    	if (running){
	    	for (CustomerAgent c:customers){
	    		c.pauseAgent();
	    	}
	    	for (WaiterAgent w:waiters){
	    		w.pauseAgent();
	    	}
	    	cook.pauseAgent();
	    	host.pauseAgent();
	    	running = false;
    	}
    }
    public void resumeRestaurant(){
    	if (!running){
    		for (CustomerAgent c:customers){
    			c.resumeAgent();
    		}
    		for (WaiterAgent w:waiters){
    			w.resumeAgent();
    		}
	    	cook.resumeAgent();
	    	host.resumeAgent();
	    	running = true;
    	}
    }
    */
    public boolean getRunning(){
    	return running;
    }

}
