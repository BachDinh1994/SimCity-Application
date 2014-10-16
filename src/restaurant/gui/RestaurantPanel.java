package restaurant.gui;

import restaurant.CashierAgent;
import restaurant.CustomerAgent;
import restaurant.HostAgent;
import market.MarketAgent;
import restaurant.WaiterAgent;
import restaurant.CookAgent;

import javax.swing.*;

import java.awt.*;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
@SuppressWarnings("serial")
public class RestaurantPanel extends JPanel 
{
    //Host, cook, waiters and customers
	static final int rows=1,cols=2,hgap=10,vgap=10,hgap2=5,vgap2=0;
    public CookAgent cook = new CookAgent("Cook");
    public CashierAgent cashier = new CashierAgent("Cashier");
	public HostAgent host;
    public WaiterAgent waiter;
    private WaiterGui waiterGui;
    private FoodGui foodGui;
    private CookGui cookGui;

    public Vector<CustomerAgent> customers = new Vector<CustomerAgent>();
    public Vector<WaiterAgent> waiters = new Vector<WaiterAgent>();
    public Vector<MarketAgent> markets = new Vector<MarketAgent>();

    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private ListPanel waiterPanel = new ListPanel(this, "Waiters");
    private JPanel group = new JPanel();
    JTextField input = new JTextField(10);	
    JPanel field = new JPanel();
    JCheckBox checking = new JCheckBox("Initial hungry status Checkbox");

    RestaurantGui gui; //reference to main gui

    public RestaurantPanel(RestaurantGui gui) 
    {
        this.gui = gui;
        markets.add(new MarketAgent("Market 1"));
        markets.add(new MarketAgent("Market 2"));
        markets.add(new MarketAgent("Market 3"));
        host = new HostAgent("Host",gui);
        cook.setMarkets(markets);
        host.startThread();
        cook.startThread();
        cashier.startThread();
        cookGui = new CookGui(cook,gui);
        cook.setGui(cookGui);
        gui.animationPanel.addGui(cookGui);
        for (int i=0;i<markets.size();i++)
        {
        	
        	markets.get(i).startThread();
        }
        
        setLayout(new GridLayout(rows,cols,hgap,vgap));
        group.setLayout(new GridLayout(rows,cols,hgap,vgap));

        group.add(customerPanel);
        group.add(waiterPanel);

        initRestLabel();
        add(restLabel);
        add(group);
        field.setLayout(new BorderLayout(hgap2, vgap2));
        field.add(new JLabel("Enter a name"), BorderLayout.WEST);
        field.add(input, BorderLayout.CENTER);
        field.add(checking, BorderLayout.EAST);
        //this.gui.add(field);
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() 
    {
        JLabel label = new JLabel();
        //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>Host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Pork</td><td>$9.99</td></tr><tr><td>Chicken</td><td>$19.99</td></tr><tr><td>Steak</td><td>$10.99</td></tr><tr><td>Fogra</td><td>$29.99</td></tr><tr><td>Fried Rice</td><td>$33.00</td></tr><tr><td>Beef Tenderloin</td><td>$39.99</td></tr></table><br></html>");
        label.setFont(new Font("Serif", Font.ITALIC, 27));
        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        restLabel.add(new JLabel("               "), BorderLayout.EAST);
        restLabel.add(new JLabel("                      "), BorderLayout.WEST);
    }

    /**
     * When a customer or waiter is clicked, this function calls
     * updatedInfoPanel() from the main gui so that person's information
     * will be shown
     *
     * @param type indicates whether the person is a customer or waiter
     * @param name name of person
     */
    public void showInfo(String type, String name) 
    {
        if (type.equals("Customers")) 
        {
            for (int i = 0; i < customers.size(); i++) 
            {
                CustomerAgent temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
        else if (type.equals("Waiters")) 
        {
            for (int i = 0; i < waiters.size(); i++) 
            {
                WaiterAgent temp = waiters.get(i);
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
    public void addPerson(String type, String name) 
    {
    	if (type.equals("Customers") && customers.size() < 13) 
    	{
    		CustomerAgent c = new CustomerAgent(name);	
    		CustomerGui g = new CustomerGui(c, gui, customers.size()+1);
            if (!checking.isSelected()) //if the customer's added without setting the initialized hungry checkbox, there's no point in using it anymore.
            {
            	c.incInit(); //This incrementation disables any operations put forth by the initialized checkbox
            }
    		gui.animationPanel.addGui(g);// dw
    		c.setRestGui(gui);
    		c.setHost(host);
    		c.setGui(g);
    		c.setCashier(cashier);
    		customers.add(c);
    		c.startThread();
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
    	else if (type.equals("Waiters") && waiters.size() < 16) 
    	{
    		waiter = new WaiterAgent(name);
    		waiterGui = new WaiterGui(waiter,gui,waiters.size()+1);
    		foodGui = new FoodGui();
            waiter.setGui(waiterGui);
            waiter.setGui(foodGui);
            waiter.setGui(gui);
            waiter.setHost(host);
            waiter.setCook(cook);
            waiter.setCashier(cashier);
            waiters.add(waiter);
            host.setWaiter(waiter);
            
            gui.animationPanel.addGui(waiterGui);
            gui.animationPanel.addGui(foodGui);
            waiter.startThread();
    	}
    }
}