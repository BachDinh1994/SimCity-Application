package restaurant_rancho;

import agent.Agent;

import java.util.*;

import restaurant_rancho.interfaces.Customer;
import restaurant_rancho.interfaces.Waiter;

/**
 * Restaurant Host Agent
 */

public class RanchoHost extends Agent {
	static final int NTABLES = 4;//a global for the number of tables.
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<Customer> waitingCustomers = Collections.synchronizedList(new ArrayList<Customer>());
	public Collection<Table> tables;
	public List<Waiter> waiters =Collections.synchronizedList(new ArrayList<Waiter>());
	private int waiterIndex=-1;
	private List<Waiter> waitersAskingBreak=Collections.synchronizedList(new ArrayList<Waiter>());
	private boolean waiterBreakReply=false;
	private String name;
	RanchoManager manager;
	
	
	public RanchoHost(String name) {
		super();
        this.name = name;
		// make some tables
		tables = Collections.synchronizedList(new ArrayList<Table>(NTABLES));
		//tables = new Table[NTABLES];
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
		
	}
    
	public String getMaitreDName() {
		return name;
	}
    
	public String getName() {
		return name;
	}
    
	public List getWaitingCustomers() {
		return waitingCustomers;
	}
    
	public Collection getTables() {
		return tables;
	}
	
	
	
	
	// Messages
    
	public void msgIWantToEat(Customer cust) {
		waitingCustomers.add(cust);
		stateChanged();	
	}
    
	public void msgTableIsClear(int tableNumber) {
		synchronized(tables){
			for(Table table:tables){
				if(table.tableNumber==tableNumber){
					table.setUnoccupied();
					stateChanged();
					return;
				}
			}
		}
	}
	
    
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		
		/* Think of this next rule as:
         Does there exist a table and customer,
         so that table is unoccupied and customer is waiting.
         If so seat him at the table.
		 */
		
		if(!waitersAskingBreak.isEmpty()){
			tellWaiterAboutBreak(waitersAskingBreak.get(0),waiterBreakReply);
			waitersAskingBreak.remove(0);
			waiterBreakReply=false;
			return true;
		}

		synchronized(tables){
			for (Table table : tables) {
				if (!table.isOccupied()) {
				
					if (!waitingCustomers.isEmpty()&&!waiters.isEmpty()) {
						tellWaiterSeatCustomer(waitingCustomers.get(0), table);//the action
						return true;//return true to the abstract agent to reinvoke the scheduler.
					}
				}
			}
		}
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}
    

	// Action
	//utilities
	

	private void tellCustomer(Customer c) {
		// TODO Auto-generated method stub
		print("Sorry "+c+". All the tables are full now.");
		c.msgTableIsFull();
		
	}

	
	private void tellWaiterAboutBreak(Waiter w,
			boolean reply) {
		// TODO Auto-generated method stub
		print(w.getName()+", your break reply is "+reply);
		w.msgBreakReply(reply);
	}

	private void tellWaiterSeatCustomer(
			Customer customerAgent, Table table) {
		// TODO Auto-generated method stub
		waiterIndex = (waiterIndex+1)%waiters.size();
		table.setOccupant(customerAgent);
		print(waiters.get(waiterIndex).getName()+", please seat "+customerAgent+" at "+table);
		waiters.get(waiterIndex).msgSitAtTable(customerAgent, table.tableNumber);
		//waiter.msgSitAtTable(customer,table.tableNumber);
		waitingCustomers.remove(customerAgent);
		
	}

	
	
	private class Table {
		Customer occupiedBy;
		int tableNumber;
        
		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}
        
		void setOccupant(Customer customerAgent) {
			occupiedBy = customerAgent;
		}
        
		void setUnoccupied() {
			occupiedBy = null;
		}
        
		public boolean isOccupied() {
			return occupiedBy != null;
		}
        
		public String toString() {
			return "table " + tableNumber;
		}
	}






	public void addWaiter(Waiter waiter) {
		// TODO Auto-generated method stub
		waiters.add(waiter);
		stateChanged();
	}

	public void msgGoOnBreakPlease(Waiter w) {
		// TODO Auto-generated method stub
		if(waiters.size()<=1){
			waiterBreakReply=false;
		}
		else{
			waiterBreakReply=true;
			waiters.remove(w);
			print("Remove "+w.getName()+" from the working list");
		}
		waitersAskingBreak.add(w);
		stateChanged();
		
	}


	public void msgIAmBack(Waiter w) {
		// TODO Auto-generated method stub
		waiters.add(w);
		print("add "+w.getName()+" to the working list");
		stateChanged();
	}

	public void msgImNotWaiting(Customer customerAgent) {
		// TODO Auto-generated method stub
		waitingCustomers.remove(customerAgent);
	}

	public void giveWaiterBreak(Waiter w) {
		// TODO Auto-generated method stub
		waiters.remove(w);
	}

	public void setManager(RanchoManager c) {
		// TODO Auto-generated method stub
		this.manager = c;
	}

	
}
