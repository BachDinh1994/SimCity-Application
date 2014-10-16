package restaurant_andrew;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import agent.Agent;

import java.util.Timer;
import java.util.TimerTask;

import restaurant_andrew.interfaces.Cashier;
import restaurant_andrew.interfaces.Customer;
import restaurant_andrew.interfaces.Market;
import restaurant_andrew.interfaces.Waiter;
import restaurant_andrew.test.mock.EventLog;

public class AndrewCashierAgent extends Agent implements Cashier {
	
	public EventLog log = new EventLog();
	
	double totalMoney = 100;
	
	int calcTime = 1000;
	public List<Bill> bills = Collections.synchronizedList(new ArrayList<Bill>());
	public class Bill {
		public Waiter w; public Customer c; public String choice; public double cost; public double paidMoney; public BillState s;
		public Bill(Waiter w, Customer c, String choice) {
			this.w = w;
			this.c = c;
			this.choice = choice;
			this.cost = 0;
			this.paidMoney = 0;
			this.s = BillState.pending;
		}
	}
	public List<Debtor> debtors = Collections.synchronizedList(new ArrayList<Debtor>());
	public class Debtor {
		public Customer c; public double debt;
		public Debtor(Customer c, double debt)
		{
			this.c = c;
			this.debt = debt;
		}
	}
	public enum BillState {pending, calculating, done, out, paying, complete};
	Timer timer = new Timer();
	Map<String, Double> costs = new HashMap<String, Double>();
	String name;
	
	public List<MBill> marketBills = Collections.synchronizedList(new ArrayList<MBill>());
	public class MBill {
		Market m; double cost;
		public MBill(Market m, double cost) {
			this.m = m;
			this.cost = cost;
		}
	}
	
	public AndrewCashierAgent(String name) {
		super();
		
		this.name = name;
		
		costs.put("Steak", 15.99);
		costs.put("Chicken", 10.99);
		costs.put("Salad", 5.99);
		costs.put("Pizza", 8.99);
	}

	public String getName() {
		return name;
	}


	public void msgCalculateBill(Waiter w, Customer c, String choice)
	{
		print("Recieved bill for " + choice + " from " + w.getName() + " for customer " + c.getName());
		bills.add(new Bill(w, c, choice));
		stateChanged();
	}
	
	public void msgBillDone(Bill b)
	{
		print("Finished calculating bill for " + b.choice + " from " + (b.w).getName() + " for customer " + (b.c).getName());
		b.s = BillState.done;
		stateChanged();
	}
	
	public void msgPayingBill(Customer c, double cash) {
		print("Recieved " + cash + " from " + c.getName());
		Bill b = findBill(c);
		b.s = BillState.paying;
		b.paidMoney = cash;
		stateChanged();
	}
	
	public void msgHereIsMBill(Market m, double money) {
		print("Recieved bill for " + money + " from " + m.getName());
		marketBills.add(new MBill(m, money));
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		synchronized(bills) {
			for (Bill b : bills) {
				if (b.s == BillState.pending) {
					calculateBill(b);
					return true;
				}
			}
		}
		synchronized(bills) {
			for (Bill b : bills) {
				if (b.s == BillState.done) {
					sendBill(b);
					return true;
				}
			}
		}
		synchronized(bills) {
			for (Bill b : bills) {
				if (b.s == BillState.paying) {
					getChange(b);
					return true;
				}
			}
		}
		synchronized(bills) {
			for (Bill b : bills) {
				if (b.s == BillState.complete) {
					bills.remove(b);
					return true;
				}
			}
		}
		synchronized(marketBills) {
			if (! marketBills.isEmpty()) {
				payBill(marketBills.get(0));
				return true;
			}
		}

		return false;
	}
	
	class BillTask extends TimerTask {
		Bill b;
		
		@Override
		public void run() {
			b.cost = costs.get(b.choice);
			msgBillDone(b);
		}
		
		public BillTask(Bill b)
		{
			this.b = b;
		}
	}
	private void calculateBill(Bill b) {
		print("Calculating bill for " + b.choice + " from " + (b.w).getName() + " for customer " + (b.c).getName());
		/*b.s = BillState.calculating; removed because unit testing doesn't do well with timers
		timer.schedule(new BillTask(b), calcTime);*/
		b.cost = costs.get(b.choice);
		print("Finished calculating bill for " + b.choice + " from " + (b.w).getName() + " for customer " + (b.c).getName());
		b.s = BillState.done;
	}
	private void sendBill(Bill b) {
		print("Sending bill for " + b.choice + " from " + (b.w).getName() + " for customer " + (b.c).getName());
		b.s = BillState.out;
		(b.w).msgHereIsBill(b.c, b.cost);
	}
	private void getChange(Bill b) {
		print("Getting change for " + (b.c).getName());
		double change = b.paidMoney - b.cost;
		Debtor d = findDebtor(b.c);
		if (change < 0) {
			if (d == null) {
				print((b.c).getName() + " is short " + -change + " on bill. Pay next time, or else!...");
				debtors.add(new Debtor(b.c, -change));
			}
			else d.debt += -change;
			totalMoney += b.paidMoney;
			(b.c).msgHereIsChange(0);
		}
		else {
			(b.c).msgHereIsChange(change);
			if (d != null) {
				debtors.remove(d);
			}
			totalMoney = totalMoney + b.cost;
		}
		b.s = BillState.complete;
	}
	private void payBill(MBill b) {
		marketBills.remove(b);
		if (b.cost <= totalMoney) {
			print("Paying bill of " + b.cost + " for " + (b.m).getName());
			(b.m).msgPayBill(b.cost);
			totalMoney -= b.cost;
		}
		else {
			print("Unable to pay bill of " + b.cost + " for " + (b.m).getName() + "; will try again later");
			marketBills.add(b); // adds back to end of list if unable to pay
		}
	}
	
	private Bill findBill(Customer c) {
		synchronized(bills) {
			for (Bill b : bills) {
				if (b.c == c) return b;
			}
			print("could not find b from c");
			return null;
		}
	}
	
	private Debtor findDebtor(Customer c) {
		synchronized(debtors) {
			for (Debtor d : debtors) {
				if (d.c == c) return d;
			}
			return null;
		}
	}
}
