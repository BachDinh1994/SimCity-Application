package restaurant_andrew;

import agent.Agent;
// import restaurant.gui.HostGui;
import java.util.*;

import restaurant_andrew.AndrewWaiterRole.MyCustomer;
import restaurant_andrew.gui.HostGui;
import restaurant_andrew.interfaces.Waiter;


public class AndrewHostAgent extends Agent {
	public final int NTABLES = 4;
	
	public List<MyCustomer> waitingCustomers = Collections.synchronizedList(new ArrayList<MyCustomer>());
	public List<MyWaiter> waiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	public Collection<Table> tables;
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented

	private String name;
	
	boolean waiterWantsBreak;
	
	private class MyCustomer {
		AndrewCustomerRole c; cState s;
		MyCustomer(AndrewCustomerRole c) {
			this.c = c;
			s = cState.waiting;
		}
	}
	public enum cState {waiting, warned, seated};
	
	private class MyWaiter {
		Waiter waiter; wState s;
		MyWaiter(Waiter w) {
			this.waiter = w;
			s = wState.working;
		}
	}
	public enum wState {working, wantToGoOnBreak, breakApproved, back};
	
	public HostGui hostGui = new HostGui(this);

	public AndrewHostAgent(String name) {
		super();

		this.name = name;
		// make some tables
		tables = new Vector<Table>(NTABLES);
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

	public void msgWantFood(AndrewCustomerRole cust) {
		print("Adding customer " + cust.getName());
		waitingCustomers.add(new MyCustomer(cust));
		stateChanged();
	}

	public void msgTableFree(int t) {
		print("Setting table " + t + " as unoccupied");
		for (Table table : tables) {
			if (table.getNum() == t) {
				table.setUnoccupied();
				stateChanged();
			}
		}
	}
	
	public void msgLeaving(AndrewCustomerRole c) {
		print("Marking " + c.getName() + " as leaving");
		synchronized(waitingCustomers) {
			for (MyCustomer cust : waitingCustomers) {
				if (c == cust.c) waitingCustomers.remove(cust);
				stateChanged();
			}
		}
	}
	
	public void msgCanIGoOnBreak(Waiter w) {
		MyWaiter mw = findWaiter(w);
		mw.s = wState.wantToGoOnBreak;
		stateChanged();
	}
	
	public void msgBackFromBreak(Waiter w) {
		MyWaiter mw = findWaiter(w);
		mw.s = wState.working;
		stateChanged();
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
		synchronized(waiters) {
			for (MyWaiter w : waiters) {
				if (w.s == wState.wantToGoOnBreak) {
					setBreak(w);
				}
			}
		}
		synchronized(waitingCustomers) {
			synchronized(waiters) {
				for (Table table : tables) {
					if (!table.isOccupied()) {
						if (! waitingCustomers.isEmpty()) {
							if (! waiters.isEmpty()) {
								Waiter waiter = null;
								for (MyWaiter w : waiters) {
									if (w.s == wState.working && w.waiter.numCustomers() < 1) {
										waiter = w.waiter;
									}
								}
								if (waiter != null) {
									assignCustomer(waitingCustomers.get(0), waiter, table);//the action
								}
								else {
									int waitList = 0;
									for (MyCustomer mc : waitingCustomers) if (mc.s == cState.waiting) waitList++;
									if (waitList > 0) restaurantFull();
								}
								return true;//return true to the abstract agent to reinvoke the scheduler.
							}
						}
					}
				}
			}
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void assignCustomer(MyCustomer customer, Waiter w, Table table) {
		customer.s = cState.seated;
		print("Telling " + w.getName() + " to seat " + customer.c.getName() + " at table " + table.getNum());
		table.occupiedBy = customer.c;
		waitingCustomers.remove(customer);
		w.msgSeatCustomer(customer.c, table.getNum());
	}

	private void restaurantFull() {
		print("Telling waiting customers that restaurant is full");
		for (MyCustomer c : waitingCustomers) {
			if (c.s == cState.waiting) {
				c.c.msgRestaurantFull();
				c.s = cState.warned;
				hostGui.DoTellCustToWait(c.c, waitingCustomers.size() - 1);
			}
		}
	}
	
	private void setBreak(MyWaiter w) {
		print("Deciding whether waiter " + w.waiter.getName() + " can go on break");
		int workingWaiters = 0;
		synchronized(waiters) {
			for (MyWaiter mw : waiters) {
				if (mw.s != wState.breakApproved) workingWaiters++;
			}
			if (workingWaiters > 1) {
				print("Waiter " + w.waiter.getName() + "'s break approved");
				w.waiter.msgYesGoOnBreak();
				w.s = wState.breakApproved;
			}
			else {
				print("Waiter " + w.waiter.getName() + "'s break declined");
				w.waiter.msgNoBreakForYou();
				w.s = wState.working;
			}
		}
	}

	/*utilities

	public void setGui(HostGui gui) {
		hostGui = gui;
	}

	public HostGui getGui() {
		return hostGui;
	}

	*/

	private class Table {
		AndrewCustomerRole occupiedBy;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(AndrewCustomerRole cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		AndrewCustomerRole getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
		
		int getNum() {
			return this.tableNumber;
		}
	}
	
	public void addWaiter(Waiter w) {
		waiters.add(new MyWaiter(w));
		stateChanged();
	}
	
	private MyWaiter findWaiter(Waiter w) {
		synchronized(waiters) {
			for (MyWaiter mw : waiters) {
				if (mw.waiter == w) return mw;
			}
			print("could not find mw from w");
			return null;
		}
	}
}

