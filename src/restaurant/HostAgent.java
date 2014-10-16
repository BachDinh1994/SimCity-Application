package restaurant;

import agent.Agent;

import java.util.*;

import restaurant.gui.RestaurantGui;
import restaurant.interfaces.Customers;
import restaurant.interfaces.Waiters;

/**
 * Restaurant Host Agent
 */
public class HostAgent extends Agent //Complete implementation for host agent
{//Normative Scenarios handled. All diagrams and design docs done
	static final int NTABLES = 3;
	public List<Customers> waitingCustomers = Collections.synchronizedList(new ArrayList<Customers>());
    public Vector<Waiter> waiters = new Vector<Waiter>();
	public Collection<Table> tables;
	private RestaurantGui gui;
	private String name;
	private int nextWaiter;
    enum stopstate{none,stopping}
	stopstate stop = stopstate.none;
	enum waiterstate{offBreak,working,askedforBreak,onBreak,resting}
	public class Waiter
	{
		Waiters w;
		waiterstate state;
		public Waiter(Waiters w2, waiterstate state2)
		{
			w = w2;
			state = state2;
		}
	}
	
	public HostAgent(String name, RestaurantGui gui) 
	{
		super();
		this.gui = gui;
		this.name = name;
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) 
		{
			tables.add(new Table(ix));
		}
		nextWaiter=0;
	}
	public HostAgent(String name) 
	{
		super();
		this.name = name;
		tables = new ArrayList<Table>(NTABLES);
		for (int ix = 1; ix <= NTABLES; ix++) 
		{
			tables.add(new Table(ix));
		}
		nextWaiter=0;
	}
	
	// Messages
	public void msgOutOfHere(Customers c)
	{
		waitingCustomers.remove(c);
		stateChanged();
	}
	public void msgDoneBreak(Waiters w)
	{
		print ("Waiter done with break");
		for (Waiter waiter:waiters) 
		{
			if (waiter.w == w) 
			{
				waiter.state = waiterstate.offBreak;
			}
		}
		stateChanged();
	}
	public void msgWantBreak(Waiters w)
	{
		print ("Waiter wants a break");
		for (Waiter waiter:waiters) 
		{
			if (waiter.w == w) 
			{
				waiter.state = waiterstate.askedforBreak;
			}
		}
		stateChanged();
	}
	public void msgPleaseStop()
	{//To allow pause to occur, the agent should go to sleep. In this case set a state where the scheduler cannot assign actions.
		stop = stopstate.stopping;
		stateChanged();
	}
	public void msgIWantFood(Customers cust) 
	{
		System.out.print(waiters.size());
		int usedTables=0;
		waitingCustomers.add(cust);
		for (Table table : tables) 
		{
			if (table.isOccupied()) 
			{
				usedTables++;
			}
		}
		if (usedTables == 3)
		{
			cust.msgPleaseWaitFullRestaurant(cust);
		}
		stateChanged();
	}
	public void msgTableFree(int table, Waiters w)
	{
		for (Table t : tables) 
		{
			if (t.tableNumber == table) 
			{
				t.setUnoccupied();
				print(t+" is free!");
			}
		}
		for (Waiter waiter:waiters) 
		{
			if (waiter.w == w) 
			{
				if (waiter.state != waiterstate.resting)
				{
					waiter.state = waiterstate.offBreak;
				}
			}
		}
		stateChanged();
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
		for (Waiter waiter:waiters) 
		{
			if (waiter.state == waiterstate.askedforBreak) 
			{
				checkWaiter(waiter);
				return true;
			}
			else if (waiter.state == waiterstate.onBreak) 
			{
				goForBreak(waiter);
				return true;
			}
		}
		for (Table table : tables) 
		{
			if (!table.isOccupied()) 
			{
				synchronized(waitingCustomers)
				{
					if (!waitingCustomers.isEmpty() && !waiters.isEmpty()) 
					{
						while (waiters.get(nextWaiter).state != waiterstate.offBreak) //All v2 requirements handled. This while loop fixes previous v2 problem
						{ //Also fixes bugs
							nextWaiter++;
							if (nextWaiter > waiters.size()-1) //Keep getting the next waiter in increments of 1. If the next waiter exceeds the size, then 
							{								   //just go back to the first waiter of the list.
								nextWaiter = 0;
							}
						}
						informWaiter(waitingCustomers.get(0), table.tableNumber,waiters.get(nextWaiter));
						return true;
					}
				}
			}
		}
		return false;
	}

	// Actions
	public void checkWaiter(Waiter w)
	{
		int workingwaiters=0;
		if (waiters.size() == 1)
		{
			w.w.msgDeniedBreak();
			w.state = waiterstate.offBreak;
			//gui.setWaiterEnabled(w.w);
		}
		else
		{
			for (Waiter waiter:waiters)
			{
				if (waiter.state == waiterstate.working)
				{
					workingwaiters++;
				}
			}
			if (workingwaiters >= 1)
			{
				w.state = waiterstate.onBreak;
			}
			else
			{
				w.w.msgDeniedBreak();
				w.state = waiterstate.working;
				//gui.setWaiterEnabled(w.w);
			}
		}
	}
	public void goForBreak(Waiter w)
	{
		w.w.msgGoRest();
		w.state = waiterstate.resting;
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
    private void informWaiter(Customers c,int table,Waiter waiter)
    {
		System.out.println("Informing waiter");
    	waiter.w.msgPleaseSitCustomer(c, table);
    	waiter.state = waiterstate.working;
		for (Table t : tables) 
		{
			if (t.tableNumber == table)
			{
				t.setOccupant(c);
			}
		}
		waitingCustomers.remove(c);
    }

	// utilities
	public String getName() 
	{
		return name;
	}
	public void setWaiter(Waiters waiter) 
	{
		 waiters.add(new Waiter(waiter,waiterstate.offBreak));
	}
	public class Table 
	{
		Customers occupiedBy;
		public int tableNumber;

		Table(int tableNumber) 
		{
			this.tableNumber = tableNumber;
		}
		void setOccupant(Customers cust) 
		{
			occupiedBy = cust;
		}
		void setUnoccupied() 
		{
			occupiedBy = null;
		}
		Customers getOccupant() 
		{
			return occupiedBy;
		}
		boolean isOccupied() 
		{
			return occupiedBy != null;
		}
		public String toString() 
		{
			return "table " + tableNumber;
		}
	}
}