package restaurant_sid;

import Role.Role;
import agent.Agent;
import restaurant_sid.SidCustomerRole.AgentEvent;
import restaurant_sid.SidWaiterRole.Menu;
import restaurant_sid.gui.WaiterGUI;
import restaurant_sid.interfaces.Customer;
import restaurant_sid.interfaces.Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Waiter Agent
 */
//A Waiter is responsible for seating customers, taking their orders, and 
//bringing them their desired meal from the cook. 
public class SidNormalWaiterRole extends Role implements Waiter{
	

	private String name;
	private boolean breakable = false;
	private boolean onBreak = false;
	public enum CustState {Waiting,Seated, Ordering, WaitingForFood, Delivering, WaitingForBill, WaitingForCashier, Paying, Paid, NoPay, Done,Leaving, OrderAgain, None};
	
	private int tableNum = 0;
	private int waiterNum = 0;
	private double money = 0;
	private ArrayList<myCustomer> customers = new ArrayList<myCustomer>();
	private Menu todaysMenu;
	private Timer breakTimer = new Timer();
	
	private Semaphore atTable = new Semaphore(0,true);
	
//Other Agents
	private SidHostAgent host;
	private myCustomer customer;
	private SidCookAgent cook;
	private SidCashierAgent cashier;

	public WaiterGUI hostGui = null;

	public SidNormalWaiterRole(String name, int number) {
		super();

		this.name = name;
		todaysMenu = new Menu();
		waiterNum = number;
		
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}
	
	public int getWNum(){
		return waiterNum;
	}

	// Messages

	public void msgSitAtTable(SidCustomerRole cust, int tNum){
		customers.add(new myCustomer(cust,tNum));
		if (customers.size() == 1)
				customer = customers.get(0);
		stateChanged();
		
	}
	
	public void msgRemoveCustomer(SidCustomerRole c){
		try{
			for (myCustomer m : customers){
				if (m.getCustomer() == c)
					customers.remove(m);
			}
		} catch (ConcurrentModificationException e){
			e.printStackTrace();
		}
	}
	
	public void msgReadytoOrder(SidCustomerRole cust){
		//print"Going to " + cust + " to get their order");
		stateChanged();
	}
	
	public void msgOrderReceived(SidCustomerRole cust, String choice){
		//Do"Okay, I'll bring your " + choice);
		customer.setState(CustState.Ordering);
		customer.makeOrder(choice, customer.getTable());
		stateChanged();
	}
	
	public void msgOutOfChoice(String i){
		todaysMenu.removeChoice(i);
		try{
			for (myCustomer m:customers){
				if (m.getOrder().getMeal().equals(i)){
					//print (m.getCustomer() + " needs to order again");
					m.setState(CustState.OrderAgain);
				}
			}
		} catch (ConcurrentModificationException e){
			e.printStackTrace();
		}
		stateChanged();
	}
	
	public void msgOrderReady(String choice, int tNum){
		if (tNum == customer.getTable()){
			customer.getOrder().setReady();
			customer.setState(CustState.Delivering);
		} else {
			try{
				for (myCustomer m :customers){
					if (m.getTable() == tNum){
						m.getOrder().setReady();
						m.setState(CustState.Delivering);
					}
				}
			} catch (ConcurrentModificationException e){
				e.printStackTrace();
			}
		}
		stateChanged();
	}
	
	public void msgIWantCheck(SidCustomerRole c){
		try {
			for (myCustomer m: customers){
				if (m.getCustomer() == c){
					m.setState(CustState.WaitingForBill);
					stateChanged();
				}
			}
		} catch (ConcurrentModificationException e){
			e.printStackTrace();
		}
	}
	
	public void msgHereIsTheBill(double value, int tNum){
		//print"Got bill for table " + tNum);
		if (tNum == customer.getTable()){
			customer.chargeBill(value);
			customer.setState(CustState.Paying);
			stateChanged();
		} else { 
			try{
				for (myCustomer m: customers){
					if (m.getTable() == tNum){
						m.chargeBill(value);
						m.setState(CustState.Paying);
						stateChanged();
					}
				}
			} catch (ConcurrentModificationException e){
				e.printStackTrace();
			}
		}
	}
	
	public void msgHereIsCash(Customer c, double value){
		//print"Got " + value + " from " + c);
		money += value;
		try{
			for (myCustomer m: customers){
				if (m.getCustomer() == c){
					m.payBill(value);
					m.setState(CustState.Paid);
					stateChanged();
				}
			}
		} catch (ConcurrentModificationException e){
			e.printStackTrace();
		}
	}
	
	public void msgCantPay(Customer c, double value){
		//print"Okay, pay next time");
		try {
			for (myCustomer m : customers){
				if (m.getCustomer() == c){
					m.setState(CustState.NoPay);
					stateChanged();
				}
			}
		} catch (ConcurrentModificationException e){
			e.printStackTrace();
		}
	}
	
	public void msgImGood(){
		customer.setState(CustState.Done); //Implemented later, in case we need to return to the customer
		stateChanged();
	}
	

	public void msgLeavingTable(SidCustomerRole cust) {
		if (customer.getCustomer() == cust){
			//printcust + " leaving table " + cust.getTableNum());
			customer.setState(CustState.Leaving);
		} else {
			try {
				for (myCustomer m: customers){
					if (m.getCustomer() == cust){
						m.setState(CustState.Leaving);
					}
				}
			} catch (ConcurrentModificationException e){
				e.printStackTrace();
			}
		}
		stateChanged();
	}
	
	public void msgGoOnBreak(){
		breakable = true;
		stateChanged();
	}
	
	public void WantToBreak(){//from GUI
		//print"Can I take a break?");
		host.msgIWantABreak(this);
	}

	public void msgAtTable() {//from animation
		atTable.release();// = true;
		stateChanged();
	}
	
	public void msgAtCook(){//from animation
		atTable.release();
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		try {
			for (myCustomer m:customers){
				if (onBreak)
					return true;
				if (customer.getState() == CustState.Waiting){
					seatCustomer(customer.getCustomer(),customer.getTable());
					return true;
				} else if (customer.getState() == CustState.Seated || customer.getState() == CustState.OrderAgain){
					getOrder();
					return true;
				} else if (customer.getState() == CustState.Ordering){
					takeOrderToCook();
					return true;
				} else if (customer.getState() == CustState.Delivering){
					deliverFood();
					return true;
				} else if (customer.getState() == CustState.WaitingForBill){
					getBill();
					return true;
				} else if (customer.getState() == CustState.Paying){
					takeBill();
					return true;
				} else if (customer.getState() == CustState.Paid){
					customerPaid();
				} else if (customer.getState() == CustState.NoPay){
					customerNoPay();
				} else if (customer.getState() == CustState.Done){
					LeaveTable();
				} else if (customer.getState() == CustState.Leaving){
					LeaveTable();
					customers.remove(customer);
					if (customers.isEmpty())
						customer = null;
					if (breakable){
						goOnBreak();
						return true;
					}
				}
				customer = m;
			}
		} catch (ConcurrentModificationException e){
			return false;
		}
		LeaveTable();
		return false;
	}

	// Actions

	private void seatCustomer(SidCustomerRole cust, int tNum) {
		if (hostGui.atStart()){
			cust.setTableNum(tNum);
			cust.msgSitAtTable(todaysMenu);
			DoSeatCustomer(cust, tNum);
			try {
				atTable.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			customer.setState(CustState.Seated);
		} else {
			LeaveTable();
		}
	}
	
	private void getOrder(){
		hostGui.DoBringToTable(customer.getCustomer());
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (customer.getState() == CustState.OrderAgain){
			customer.getCustomer().msgPickAgain();
		}
		customer.getCustomer().msgWhatIsYourOrder(todaysMenu);
	}
	
	private void takeOrderToCook(){
		//print"Taking order to cook");
		hostGui.DoGoToCook();
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//cook.orders.insert(this,customer.getOrder().getMeal(), customer.getOrder().getTable());
		cook.msgHereIsAnOrder(this,customer.getOrder().getMeal(), customer.getOrder().getTable());
		customer.setState(CustState.WaitingForFood);
		hostGui.DoLeaveCustomer();
	}
	
	private void deliverFood(){
			hostGui.DoGoToCook();
			try {
				atTable.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			hostGui.setPlate(customer.getOrder().getMeal());
			hostGui.DoBringToTable(customer.getCustomer());
			try {
				atTable.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			hostGui.setPlate("");
			customer.setState(CustState.None);
			customer.getCustomer().msgOrderReceived();
			stateChanged();
	}
	
	private void getBill(){
		if (!hostGui.atStart()){
			hostGui.DoLeaveCustomer();
		} else{
			cashier.msgHereIsBill(this, customer.getCustomer(), customer.getOrder().getMeal(), customer.getTable());
			customer.setState(CustState.WaitingForCashier);
			stateChanged();
		}
	}
	
	private void takeBill(){
		customer.setState(CustState.WaitingForCashier);
		hostGui.DoBringToTable(customer.getCustomer());
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		customer.getCustomer().msgHereIsCheck(customer.getMoneyOwed());
		stateChanged();
	}
	
	private void customerPaid(){
		customer.getCustomer().msgHereIsChange(0.01);
		cashier.msgCustomerPaid(customer.getCustomer(),money);
		customer.setState(CustState.Done);
		stateChanged();
	}
	
	private void customerNoPay(){
		customer.getCustomer().msgjustLeave();
		customer.setState(CustState.Done);
		stateChanged();
	}
	
	private void LeaveTable(){
		try{
			for(myCustomer c : customers){
				if (c.getState() == CustState.Leaving){
					host.msgTableCleared(this,c.getTable(),customers.size());
				}
			}
		} catch (ConcurrentModificationException e){
			e.printStackTrace();
		}
		if (!hostGui.atStart())
			hostGui.DoLeaveCustomer();
	}
	
	private void goOnBreak(){
		hostGui.DoLeaveCustomer();
		onBreak = true;
		//print"I'm on break");
		breakTimer.schedule(new TimerTask() {
			public void run() {
				//print"I'm done with my break");
				onBreak = false;
				breakable  = false;
				stateChanged();
				host.msgImBack(SidNormalWaiterRole.this);
			}
		},
		10000);
	}

	// The animation DoXYZ() routines
	private void DoSeatCustomer(SidCustomerRole cust, int tNum) {
		//print"Seating " + cust + " at table " + tNum);
		hostGui.DoBringToTable(cust);

	}

	//utilities

	public void setGui(WaiterGUI gui) {
		hostGui = gui;
	}

	public WaiterGUI getGui() {
		return hostGui;
	}
	
	public int getTableNum(){
		return tableNum;
	}
	
	public void setCook(SidCookAgent c){

		cook = c;
	}
	
	public void setHost(SidHostAgent h){
		host = h;
	}
	
	public void setCashier(SidCashierAgent c){
		
		cashier = c;
	}
	
	public int getNumTotalTables(){
		return host.getNumTables();
	}
	
	private class myCustomer{
		SidCustomerRole c;
		Order o;
		private double moneyOwed;
		int t;
		private CustState s = CustState.Waiting;
		public myCustomer(SidCustomerRole cust, int table){
			c = cust;
			t = table;
			moneyOwed = 0;
		}
		public SidCustomerRole getCustomer(){
			return c;
		}
		public int getTable(){
			return t;
		}
		public Order getOrder(){
			return o;
		}
		public CustState getState(){
			return s;
		}
		public void setTable(int nt){
			t = nt;
		}
		public void setState(CustState ns){
			s = ns;
		}
		public void makeOrder(String c, int t){
			o = new Order(c,t);
		}
		public void chargeBill(double v){
			moneyOwed += v;
		}
		public void payBill(double v){
			moneyOwed -= v;
		}
		public double getMoneyOwed(){
			return moneyOwed;
		}
	}
	
	private class Order{

		private int tNum;
		private String meal;
		private boolean ready;
		
		public Order(String m, int t){
			meal = m;
			tNum = t;
			ready = false;
		}
		
		public String getMeal(){
			return meal;
		}
		
		public int getTable(){
			return tNum;
		}
		
		public void setMeal(String c){
			meal = c;
		}
		
		public void setTable(int t){
			tNum = t;
		}
		public boolean getReadiness(){
			return ready;
		}
		public void setReady(){
			ready = true;
		}
		public void resetReady(){
			ready = false;
		}
	}
}
	
