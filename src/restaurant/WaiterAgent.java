package restaurant;

import agent.Agent;

import java.util.*;

import restaurant.CashierAgent.Check;
import restaurant.gui.FoodGui;
import restaurant.gui.RestaurantGui;
import restaurant.gui.WaiterGui;
import restaurant.interfaces.Customers;
import restaurant.interfaces.Waiters;

import java.util.concurrent.Semaphore;

/**
 * Restaurant Waiter Agent
 */
public class WaiterAgent extends Agent implements Waiters//Complete implementation
{
	Check check;
	private RestaurantGui gui;
	private Menu newMenu;
	private Timer timer = new Timer();
	private CashierAgent cashier;
	private HostAgent host;
	private CookAgent cook;
	Semaphore WaitForOrder = new Semaphore(0,true); //wait until customer has finished his order
	private String name;
	public List<Customer> customers;
	public WaiterGui waiterGui = null;
	private FoodGui foodGui = null;
	enum waiterstate{working,asking,waiting,resting}
	enum customerstate{stop,waiting,seated,choosing,readyToOrder,askedToOrder,waitingforfood,waitingforwaiter,eating,waitingforcheck,paying,leaving,cancelorder};
    enum stopstate{none,stopping}
	stopstate stop = stopstate.none;
	waiterstate state = waiterstate.working;
	public class Customer
	{
		public Customers c;
		int table;
		String choice;
		customerstate s;
		public Customer(Customers c2, int table2, customerstate s2) 
		{
			c=c2;
			table=table2;
			s=s2;
		}
	}
	public WaiterAgent(String name) 
	{
		super();
		this.name = name;
		customers = new ArrayList<Customer>();
	}

	// Messages
	public void msgWaitingForCheck(Customers c) 
	{
		for (Customer cust : customers) 
		{
			if (c == cust.c) 
			{
				cust.s=customerstate.waitingforcheck; //use this state to deliver check to waiter later
			}
		}
		stateChanged();
	}
	public void msgDeniedBreak()
	{
	//	print ("Denied break from host");
		state = waiterstate.working;
		waiterGui.notReadyBreak();
		stateChanged();
	}
	public void msgStartBreak()
	{ 
		state = waiterstate.asking;
		stateChanged();
	}
	public void msgGoRest()
	{
		//print ("Accepted break from host");
		state = waiterstate.resting;
		stateChanged();
	}
	public void msgOutOfFood(int table, String choice)
	{
		newMenu = new Menu();
		newMenu.choices.remove(choice);
	//	print ("Waiter got message out of food");
		for (Customer cust : customers) 
		{
			if (cust.table == table) 
			{
				cust.s=customerstate.cancelorder;
			}
		}
		stateChanged();
	}
	public void msgPleaseStop()
	{
		stop = stopstate.stopping;
		stateChanged();
	}
	public void msgPleaseSitCustomer(Customers c,int table) 
	{
		customers.add(new Customer(c,table,customerstate.waiting));
		stateChanged();
	}
	public void msgGiveCustomerTime(Customer c)
	{
		//print ("Waiting for customer order");
		c.s = customerstate.choosing;
		stateChanged();
	}
	public void msgReadyToOrder(Customers c) 
	{
		for (Customer cust : customers) 
		{
			if (c == cust.c) 
			{
				cust.s=customerstate.readyToOrder;
			}
		}
		stateChanged();
	}
	public void msgHereIsMyOrder(Customers c, String choice) 
	{
		for (Customer cust : customers) 
		{
			if (c == cust.c) 
			{
				foodGui.setInfo(choice,cust.table);
				cust.s=customerstate.waitingforfood;
				cust.choice=choice;
				WaitForOrder.release(); //customer's done with his order
			}
		}
		stateChanged();
	}
	public void msgDoneCooking(String choice, int table)
	{
	//	print ("Received order from cook");
		for (Customer cust : customers) 
		{
			if (cust.table == table) 
			{
				cust.s = customerstate.waitingforwaiter;
			}
		}
		stateChanged();
	}
	public void msgCheckForWaiter(Check check)
	{
		//print ("Received check from Cashier");
		this.check = check;
		stateChanged();
	}
	public void msgLeavingTable(Customers cust) 
	{
		//System.out.println("Received leaving");
		for (Customer c:customers) 
		{
			if (c.c == cust) 
			{
				c.s = customerstate.leaving;
				stateChanged();
			}
		}
	}
	
	/**
	 * Scheduler. Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() 
	{
		if (stop == stopstate.stopping)
		{
			stop();
		}
		if (state == waiterstate.asking)
		{
			askHost();
			return true;
		}
		else if (state == waiterstate.resting && customers.size() == 0)
		{
			waiterResting(this);
			return true;
		}
		try
		{
			for (Customer c : customers) 
			{
				if (c.s == customerstate.cancelorder)
				{
					cancelOrder(c);
					return true;
				}
				else if (c.s == customerstate.waiting)
				{
					seatCustomer(c);
					return true;
				}
				else if (c.s == customerstate.choosing)
				{
					WaitUntilCalled(c);
					return true;
				}
				else if (c.s == customerstate.readyToOrder)
				{
					TakeOrder(c);
					return true;
				}
				else if (c.s == customerstate.waitingforfood)
				{
					//print ("Ask cook");
					SendOrderToCook(c);
					return true;
				}
				else if (c.s == customerstate.waitingforwaiter)
				{
					//print ("Delivering food to customer");
					DeliverFoodToCustomer(c);
					return true;
				}
				else if (c.s == customerstate.waitingforcheck)
				{
					//print ("Giving check to customer");
					giveCustomerCheck(c);
					return true;
				}
				else if (c.s == customerstate.leaving)
				{
					informHost(c);
					return true;
				}
			}
		}
		catch (ConcurrentModificationException e)
		{
			return false;
		}
		return false;
	}

	// Actions
	public void giveCustomerCheck(final Customer c)
	{
		waiterGui.GoBackToTable();
		c.s = customerstate.paying;
		timer.schedule(new TimerTask()
		{
			public void run()
			{
				c.c.msgCheckForCustomer(check);
			}
		},4000);
	}
	public void askHost()
	{
		host.msgWantBreak(this);
		state = waiterstate.waiting;
	}
	public void waiterResting(final WaiterAgent w)
	{
		print ("Going on break");
		waiterGui.GoForRest();
		state = waiterstate.working;
    	gui.setWaiterEnabled(this);
		waiterGui.notReadyBreak();
		/*timer.schedule(new TimerTask()
		{
			public void run()
			{
				host.msgDoneBreak(w);
				waiterGui.BackToWork();
			}
		},5000);*/
	}
	public void backToWork()
	{
		gui.onBreak.setText("Go On Break?");
    	gui.onBreak.setEnabled(true);
		host.msgDoneBreak(this);
		waiterGui.BackToWork();
	}
	public void stop()
	{
		stop = stopstate.none;
		try
		{
			getThread().pauseS.acquire();
			getThread().pause();
		}
		catch (Exception st) 
		{
			st.printStackTrace();
		}
	}
	private void cancelOrder(Customer c)
	{
		c.c.msgOutOfChoice(newMenu);
		c.s = customerstate.stop;
	}
	private void seatCustomer(Customer c) 
	{
		c.c.setTable(c.table);
		c.s = customerstate.seated;
		c.c.msgFollowMe(new Menu(),this); //pass menu pointer so that waiter knows what food is available
		DoSeatCustomer(c);
	}
	private void DoSeatCustomer(final Customer c)
	{
		print("Seating " + c.c + " at " + c.table);
		waiterGui.DoBringToTable(c.c,c.table);
		timer.schedule(new TimerTask() 
		{
		    public void run() 
		    {  
				msgGiveCustomerTime(c);    
		    }
		},5500+(c.table*500));
	}
	private void WaitUntilCalled(Customer c)
	{
		waiterGui.GoToCook(); //go back to kitchen until customer calls. In the meantime, go to sleep.
		c.s = customerstate.stop;
	}
	private void TakeOrder(final Customer c) 
	{
		c.s = customerstate.stop;
		waiterGui.GoBackToTable();
		timer.schedule(new TimerTask() 
		{
		    public void run() 
		    {  
				c.c.msgYourChoicePlease();
				c.s = customerstate.askedToOrder;
				try 
				{
					WaitForOrder.acquire(); //Customer is ordering, so stay there.
				} 
				catch (InterruptedException e) 
				{
					e.printStackTrace();
				} 
		    }
		},5000);
	}
	private void SendOrderToCook(Customer c)
	{
		foodGui.showChoice();
		waiterGui.GoToCook();
		cook.orders.insert(this,c.choice,c.table,foodGui);
		c.s = customerstate.stop;
		timer.schedule(new TimerTask()
		{
			public void run()
			{
				foodGui.MoveOut();
			}
		},3800);
	}
	private void DeliverFoodToCustomer(Customer c)
	{
		//print ("Delivered food");
		foodGui.MoveToTable();
		waiterGui.DoBringToTable(c.c, c.table);
		c.s = customerstate.stop;
		c.c.msgHereIsYourFood(c.choice);
		cashier.msgCalculateCheck(c.c,this,c.choice);
		timer.schedule(new TimerTask()
		{
			public void run()
			{
				waiterGui.LetCustomerEat();
			}
		},3000);
	}
	private void informHost(Customer c) 
	{
		customers.remove(c);
		foodGui.MoveOut(); //move food offscreen to show the customer's done 
		host.msgTableFree(c.table,this);
		waiterGui.MoveToHomePosition();
		c.s = customerstate.stop;
	}
	
	//Accessors and Mutators
	public String getName() 
	{
		return name;
	}
	public void setGui(WaiterGui gui) 
	{
		waiterGui = gui;
	}
	public void setGui(FoodGui gui) 
	{
		foodGui = gui;
	}
	public WaiterGui getGui() 
	{
		return waiterGui;
	}
	public void setHost(HostAgent host) 
	{
		this.host = host;
	}
	public void setCook(CookAgent cook) 
	{
		this.cook = cook;
	}
	public void setCashier(CashierAgent cashier)
	{
		this.cashier = cashier;
	}
	public void setGui(RestaurantGui gui)
	{
		this.gui = gui;
	}
}