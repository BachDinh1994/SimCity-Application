package amyRestaurant.gui;


import javax.swing.*;

import agent.Agent;
import amyRestaurant.AmyBankAgent;
import amyRestaurant.AmyCashierAgent;
import amyRestaurant.AmyCookAgent;
import amyRestaurant.AmyCustomerRole;
import amyRestaurant.AmyHostAgent;
import amyRestaurant.AmyMarketAgent;
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
public class RestaurantPanel extends JPanel implements ActionListener  {

	private static final int pRow = 5, pCol =1, pSpace = 2 ,NWAITERS = 2; //panel row, column, spacing
	private List<Agent> allAgents = new ArrayList<Agent>(); //array of all agents

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
	private Vector<AmyWaiterRole> waiters = new Vector<AmyWaiterRole>();
	private List<AmyMarketAgent> markets = new ArrayList<AmyMarketAgent>();{
		markets.add(market1);
		markets.add(market2);
		markets.add(market3);
		markets.add(market4);

	}


	private JPanel restLabel = new JPanel();
	private JPanel myLabel = new JPanel();
	private ImageIcon myIcon = new ImageIcon("C:/Users/Amy/restaurant_lee779/fighton.jpg");
	private JButton pause = new JButton("pause");

	private ListPanel customerPanel = new ListPanel(this, "Customers");
	private JPanel group = new JPanel();


	private RestaurantGui gui; //reference to main gui

	public RestaurantPanel(RestaurantGui gui) {

		pause.addActionListener(this);

		
		
		this.gui = gui;
		host.setGui(new AmyHostGui(host));
		host.setCook(cook);
		host.startThread();
		cook.startThread();
		cook.setGui(new AmyCookGui(cook,gui));
		gui.animationPanel.addGui(cook.amyCookGui);// dw
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



		////////////////////////////////////////////////////////
		setLayout(new BorderLayout());
		group.setLayout(new BorderLayout());

		group.add(customerPanel);
		initRestLabel();

		add(restLabel,BorderLayout.EAST);
		add(myLabel,BorderLayout.NORTH);
		add(group, BorderLayout.CENTER);
		add(pause,BorderLayout.SOUTH);


	}

	/**
	 * Sets up the restaurant label that includes the menu,
	 * and host and cook information
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == pause) {
			// Chapter 2.19 describes showInputDialog()
			if(pause.getText() == "pause")
			{
				pause.setText("resume");
				for (Agent agents:allAgents){
					agents.setPause();
					gui.animationPanel.paused = true;
				}

			}
			else if (pause.getText()=="resume")
			{
				pause.setText("pause");
				for (Agent agents:allAgents){
					agents.setResume();
					gui.animationPanel.paused= false;
				}
			}
		}

	}

	private void initRestLabel() {

		JLabel label = new JLabel();

		// restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
		restLabel.setLayout(new BorderLayout());
		label.setText(
				"<html><h3><u>Tonight's Staff</u></h3><table><tr><td></td><td>"+ host.getName() + "<table><tr>Cook: </td><td>" + cook.getName() +
				"</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

		restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
		restLabel.add(label);

		restLabel.add(new JLabel("               "), BorderLayout.EAST);
		restLabel.add(new JLabel("               "), BorderLayout.WEST);


		//ADDED a new panel
		JLabel mlabel = new JLabel();
		mlabel = new JLabel("Restaurant v2.1", myIcon, JLabel.CENTER);
		myLabel.add(mlabel);

	}



	/**
	 * When a customer or waiter is clicked, this function calls
	 * updatedInfoPanel() from the main gui so that person's information
	 * will be shown
	 *
	 * @param type indicates whether the person is a customer or waiter
	 * @param name name of person
	 */
	public void showInfo(String type, String name, Boolean check) {

		if (type.equals("Customers")) {

			for (int i = 0; i < customers.size(); i++) {
				AmyCustomerRole temp = customers.get(i);

				if (temp.getName() == name){

					gui.updateInfoPanel(type, temp,check);}
			}
		}

		if (type.equals("Waiters")) {

			for (int i = 0; i < waiters.size(); i++) 
			{
				AmyWaiterRole temp = waiters.get(i);

				if (temp.getName().equals(name))
				{
					//gui.updateBreak("Rejected", temp);
					gui.updateInfoPanel(type, temp,check); 
				}
			}
		}

	}

	/**
	 * Adds a customer or waiter to the appropriate list
	 *
	 * @param type indicates whether the person is a customer or waiter (later)
	 * @param name name of person
	 */
	public void addPerson(String name) {


		AmyWaiterRole w = new AmyWaiterRole(name);	
		AmyWaiterGui g = new AmyWaiterGui(w,gui);

		gui.animationPanel.addGui(g);// dw
		w.setHost(host);
		w.setGui(g);
		w.setCook(cook);
		w.setCashier(cashier);
		
		wHome = wHome + 50;
		if(wHome > 350){
			g.setHome(-30, 2);
		}
		else{
		g.setHome(wHome, 2);}
		
		cook.setWaiter(w);
		cook.addWaiter(w);
		host.addWaiters(w);

		waiters.add(w);
		allAgents.add(w);
		w.startThread();

	}

	public void addCust(String name, String cash, Boolean thief)
	{
		AmyCustomerRole c = new AmyCustomerRole(name);	
		AmyCustomerGui g = new AmyCustomerGui(c, gui);

		gui.animationPanel.addGui(g);// dw
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
		c.setHost(host);
		c.setCashier(cashier);
		c.setGui(g);
		c.setCash(Integer.parseInt(cash));
		c.setThief(thief);
		
		customers.add(c);
		allAgents.add(c);



		c.startThread();

	}


	

}





