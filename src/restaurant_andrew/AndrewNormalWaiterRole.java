package restaurant_andrew;

import Role.Role;
import restaurant_andrew.AndrewCookAgent.OrderTask;
import restaurant_andrew.gui.WaiterGui;
import restaurant_andrew.interfaces.Customer;
import restaurant_andrew.interfaces.Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Waiter Agent
 */
public class AndrewNormalWaiterRole extends Role implements Waiter {
	AndrewCashierAgent cashier;
	static final int NTABLES = 4;
	//Timer timer = new Timer();
	//int breakTime = 10000;
	public List<MyCustomer> customers = new ArrayList<MyCustomer>();
	class MyCustomer {
		AndrewCustomerRole c; int table; String choice; double billCost; cState s;
		public MyCustomer(AndrewCustomerRole c, int t) {
			this.c = c;
			this.table = t;
			this.choice = "";
			this.billCost = 0;
			this.s = cState.waiting;
		}
	}
	public enum cState {waiting, seated, ready, asked, ordered, eating, gettingBill, gotBill, sentBill, done};
	public List<Order> orders = new ArrayList<Order>();
	class Order {
		String choice; int table; oState s;
		public Order(String choice, int table) {
			this.choice = choice;
			this.table = table;
			this.s = oState.notTaken;
		}
	}
	public enum oState {notTaken, taken, sent, out, done};
	
	wState s = wState.working;
	public enum wState {working, wantToGoOnBreak, breakApproved, onBreak, back};
	
	private AndrewCookAgent cook;
	private AndrewHostAgent host;

	private String name;

	Semaphore waitingForOrder = new Semaphore(0,true);
	Semaphore atDestination = new Semaphore(0,true);
	Semaphore atEntrance = new Semaphore(0,true);

	private WaiterGui waiterGui;
	
	String carrying;

	public AndrewNormalWaiterRole(String name) {
		super();

		this.name = name;
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	// Messages

	public void msgSeatCustomer(AndrewCustomerRole c, int table) {
		customers.add(new MyCustomer(c, table));
		stateChanged();
	}
	public void msgReadyToOrder(AndrewCustomerRole c) {
		MyCustomer mc = findCustomer(c);
		mc.s = cState.ready;
		stateChanged();
	}
	public void msgHereIsChoice(AndrewCustomerRole c, String choice) {
		MyCustomer mc = findCustomer(c);
		mc.choice = choice;
		mc.s = cState.ordered;
		Order o = new Order(choice, mc.table);
		o.s = oState.taken;
		orders.add(o);
		waitingForOrder.release();
		stateChanged();
	}
	public void msgOutOfFood(String choice, int table) {
		Order o = findOrder(table);
		o.s = oState.out;
		stateChanged();
	}
	public void msgOrderDone(String choice, int table) {
		Order o = findOrder(table);
		o.s = oState.done;
		stateChanged();
	}
	
	public void msgHereIsBill(Customer c, double cost) {
		MyCustomer mc = findCustomer((AndrewCustomerRole) c);
		mc.billCost = cost;
		mc.s = cState.gotBill;
		stateChanged();
	}
	
	public void msgPaidAndLeaving(AndrewCustomerRole c) {
		MyCustomer mc = findCustomer(c);
		mc.s = cState.done;
		stateChanged();
	}

	public void msgAtDestination() {//from animation
		atDestination.release();
		stateChanged();
	}

	public void msgAtEntrance() {//from animation
		atEntrance.release();
		stateChanged();
	}
	
	public void msgWantToGoOnBreak() {
		
		s = wState.wantToGoOnBreak;
		stateChanged();
	}
	
	public void msgYesGoOnBreak() {
		s = wState.breakApproved;
		waiterGui.setWaiting(false);
		stateChanged();
	}
	
	public void msgNoBreakForYou() {
		s = wState.working;
		waiterGui.setWaiting(false);
		stateChanged();
	}
	
	public void msgBreakDone() {
		s = wState.back;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		try {
			if (s == wState.wantToGoOnBreak) {
				askForBreak();
			}
			int i;
			for (i = 0; i < customers.size(); i++) {
				MyCustomer mc = customers.get(i);
				if (mc.s == cState.ready) {
					takeOrder(mc);
					return true;
				}
			}
			for (i = 0; i < orders.size(); i++) {
				Order o = orders.get(i);
				if (o.s == oState.done) {
					deliverOrder(o);
					return true;
				}
			}
			for (i = 0; i < orders.size(); i++) {
				Order o = orders.get(i);
				if (o.s == oState.out) {
					outOf(o);
					return true;
				}
			}
			for (i = 0; i < customers.size(); i++) {
				MyCustomer mc = customers.get(i);
				if (mc.s == cState.waiting) {
					seatCustomer(mc);
					return true;
				}
			}
			for (i = 0; i < orders.size(); i++) {
				Order o = orders.get(i);
				if (o.s == oState.taken) {
					sendOrder(o);
					return true;
				}
			}
			for (i = 0; i < customers.size(); i++) {
				MyCustomer mc = customers.get(i);
				if (mc.s == cState.done) {
					clearTable(mc);
					return true;
				}
			}
			for (i = 0; i < customers.size(); i++) {
				MyCustomer mc = customers.get(i);
				if (mc.s == cState.eating) {
					getBill(mc);
					return true;
				}
			}
			for (i = 0; i < customers.size(); i++) {
				MyCustomer mc = customers.get(i);
				if (mc.s == cState.gotBill) {
					giveBill(mc);
					return true;
				}
			}
			if (s == wState.breakApproved && customers.isEmpty()) {
				goOnBreak();
			}
			if (s == wState.back) {
				backFromBreak();
			}
		}
		catch (ConcurrentModificationException cme) {
			return false;
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void seatCustomer(MyCustomer mc) {
		waiterGui.DoLeaveDestination();
		try {
			atEntrance.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		DoSeatCustomer(mc.c, mc.table); // animation
		(mc.c).msgFollowMe(this, new AndrewMenu());
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		mc.s = cState.seated;
		waiterGui.DoLeaveDestination();
	}
	private void DoSeatCustomer(AndrewCustomerRole customer, int tableNum) {
		waiterGui.DoBringToTable(customer, tableNum);
	}
	
	private void takeOrder(MyCustomer mc) {
		
		DoGoToTable(mc.table); // animation
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		(mc.c).msgWhatDoYouWant();
		mc.s = cState.asked;
		try {
			waitingForOrder.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//waiterGui.DoMarkOrder(mc.choice, mc.table, /*delivered =*/ false);
		waiterGui.DoLeaveDestination();
	}
	private void DoGoToTable(int table) {
		waiterGui.DoGoToTable(table);
	}

	private void clearTable(MyCustomer mc) {
		
		host.msgTableFree(mc.table);
		customers.remove(mc);
	}

	private void sendOrder(Order o) {
		carrying = o.choice + "?";
		DoSendOrder(); // animation
		
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		o.s = oState.sent;
		cook.msgHereIsOrder(this, o.choice, o.table);
		carrying = null;
		waiterGui.DoLeaveDestination();
	}
	private void outOf(Order o) {
		DoDeliverOrder(o.table, null); // animation
		
		MyCustomer mc = findCustomer(o.table);
		mc.s = cState.seated;
		(mc.c).msgOutOfFood(new AndrewMenu(o.choice /*remove parameter*/));
		orders.remove(o);
		//waiterGui.DoMarkOrder(mc.choice, mc.table, /*delivered =*/ true);
		waiterGui.DoLeaveDestination();
	}
	private void DoSendOrder() {
		waiterGui.DoGoSendCook();
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void deliverOrder(Order o) {
		DoDeliverOrder(o.table, o.choice); // animation
		
		MyCustomer mc = findCustomer(o.table);
		mc.s = cState.eating;
		(mc.c).msgHereIsFood();
		orders.remove(o);
		carrying = null;
		waiterGui.DoLeaveDestination();
	}
	private void DoDeliverOrder(int tableNum, String choice) {
		waiterGui.DoGetFromCook();
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		carrying = choice;
		cook.getGui().DoSend(choice);
		waiterGui.DoGoToTable(tableNum);
		try {
			atDestination.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void getBill(MyCustomer mc) {
		
		cashier.msgCalculateBill(this, mc.c, mc.choice);
		mc.s = cState.gettingBill;
	}
	
	private void giveBill(MyCustomer mc) {
		
		(mc.c).msgHereIsBill(cashier, mc.billCost);
		mc.s = cState.sentBill;
	}
	
	private void askForBreak() {
		
		host.msgCanIGoOnBreak(this);
		waiterGui.setWaiting(true);
	}
	
	private void goOnBreak() {
		
		waiterGui.goOnBreak();
		s = wState.onBreak;
		
		/*
		timer.schedule(new TimerTask() {
			public void run() {
				
				msgBreakDone();
			}
		},
		breakTime);
		*/
	}
	
	private void backFromBreak() {
		s = wState.working;
		
		host.msgBackFromBreak(this);
		waiterGui.setBreak(false);
	}

	public void setHost(AndrewHostAgent host) {
		this.host = host;
	}
	
	public void setCook(AndrewCookAgent cook) {
		this.cook = cook;
	}

	public void setCashier(AndrewCashierAgent cashier) {
		this.cashier = cashier;
	}
	
	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}

	public WaiterGui getGui() {
		return waiterGui;
	}
	
	private MyCustomer findCustomer(AndrewCustomerRole c) {
		for (MyCustomer mc : customers) {
			if (mc.c == c) return mc;
		}
		
		return null;
	}
	private MyCustomer findCustomer(int table) {
		for (MyCustomer mc : customers) {
			if (mc.table == table) return mc;
		}
		
		return null;
	}
	private Order findOrder(int table) {
		for (Order o : orders) {
			if (o.table == table) return o;
		}
		
		return null;
	}
	
	public int numCustomers() {
		return customers.size();
	}
	
	public String getCarrying() {
		return carrying;
	}
	public void animateSupport() 
	{
		if (!customers.isEmpty()) msgAtEntrance();
	}
}

