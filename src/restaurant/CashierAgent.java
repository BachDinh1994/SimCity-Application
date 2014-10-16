package restaurant;

import agent.Agent;

import java.util.*;

import market.DeliveryTruckAgent;
import market.MarketAgent;
import market.MarketAgent.dCheck;
import market.interfaces.Market;

import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customers;
import restaurant.interfaces.Waiters;
import restaurant.test.mock.EventLog;
import restaurant.test.mock.LoggedEvent;

/**
 * Restaurant Cashier Agent
 */
public class CashierAgent extends Agent implements Cashier//Complete implementation
{
	public EventLog log = new EventLog();
	String name;
	public double cash=1000.0;
	Timer timer = new Timer();
	//keeps track of its own debt
	public Map <DeliveryTruckAgent, Double> restaurantDebt = new HashMap<DeliveryTruckAgent, Double>();
	
	public List<Check> checks = Collections.synchronizedList(new ArrayList<Check>());
    
	public static  Map<String,Integer> foodprices = new HashMap<String,Integer>();
    
	public List<Blackbook> blackbook = Collections.synchronizedList(new ArrayList<Blackbook>()); //stores customers that must pay the second time
    
	public List<MarketBill> marketbills = Collections.synchronizedList(new ArrayList<MarketBill>());
	
	enum state{newcheck,checktoWaiter,cannotpay,paid};
	public enum cashierstate{none,payingMarket,paidMarket};
    enum stopstate{none,stopping}
	stopstate stop = stopstate.none;
	
	public class MarketBill
	{
		public DeliveryTruckAgent payTo;
		public String choice;
		public Double amount;
		public cashierstate s = cashierstate.payingMarket;
		public MarketBill(DeliveryTruckAgent person,String food, Double amt)
		{
			payTo = person;
			choice = food;
			amount = amt;
			s = cashierstate.payingMarket;
		}
	}
	public class Blackbook
	{
		Customers c;
		int debt;
		public Blackbook(Customers c2, int debt2)
		{
			c = c2;
			debt = debt2;
		}
	}
	static public class Check 
	{
	    Waiters w;
	    public Customers c;
	    public String choice;
	    public int price;
	    state s = state.newcheck;
	    public Check(Customers c2, Waiters w2, String choice2,int debt)
	    {
	    	c = c2;
	    	w = w2;
	    	choice = choice2;
	    	price = foodprices.get(choice)+debt; //Test whether Integer object can be assigned to int primitive data type
	    }
	}
	public CashierAgent(String name) 
	{
		super();
		this.name = name;
		foodprices.put("Pork", 10); 
		foodprices.put("Chicken",20);
		foodprices.put("Fogra",30);
		foodprices.put("Fried Rice",33);
		foodprices.put("Steak",11);
		foodprices.put("Beef Tenderloin",40);
	}

	// Messages
	
	public void msgHereIsCheck(dCheck check) { //from cook to cashier to pay the deliveryTruck
		// TODO Auto-generated method stub
		marketbills.add(new MarketBill(check.deliverer, check.choice, check.amtDue));
		stateChanged();
		

	}
	public void msgPleaseStop()
	{
		stop = stopstate.stopping;
		stateChanged();
	}
	public void msgPayASAP()
	{
		print ("WARNED BY MARKET: PAY ASAP! Must go to the bank at the end of the day");		stateChanged();
	}
	
	public void msgCalculateCheck(Customers c, Waiters w, String choice)
	{
		print ("Calculating check...");
		int debt=0;
		if (!blackbook.isEmpty())
		{
			synchronized(blackbook)
			{
				for (Blackbook b:blackbook)
				{
					if (b.c == c)
					{
						print ("It seems you have a debt of "+b.debt+" ,so we'll add this debt to your check");
						debt = b.debt;
					}
				}
			}
		}
		checks.add(new Check(c,w,choice,debt));
		stateChanged();
	}
	public void msgPayment(Check check, int money)
	{
		print ("Received payment from customer");
		if (!blackbook.isEmpty())
		{
			synchronized(blackbook)
			{
				for (Blackbook b:blackbook)
				{
					if (b.c == check.c)
					{
						print (check.c.getName()+" is removed from blackbook");
						blackbook.remove(b);
						break;
					}
				}
			}
		}
		cash += money;
		synchronized(checks)
		{
			for (Check c:checks)
			{
				if (c == check) //or just c == check?
				{
					c.s = state.paid;
				}
			}
		}
		stateChanged();
	}
	public void msgNotEnoughMoney(Check check)
	{
		blackbook.add(new Blackbook(check.c,check.price));
		synchronized(checks)
		{
			for (Check c:checks)
			{
				if (c == check) //or just c == check?
				{
					c.s = state.cannotpay;
				}
			}
		}
		stateChanged();
	}
	/**
	 * Scheduler. Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() 
	{
		if (stop == stopstate.stopping)
		{
			stop();
		}
		synchronized (marketbills)
		{
			for (MarketBill m:marketbills)
			{
				if (m.s == cashierstate.payingMarket)
				{
					payMarket(m);
					return true;
				}
				else if (m.s == cashierstate.paidMarket)
				{
					marketbills.remove(m);
					return true;
				}
			}
		}
		synchronized(checks)
		{
			for (Check c:checks) 
			{
				if (c.s == state.newcheck) 
				{
					print ("Giving check to waiter");
					giveCheckToWaiter(c);
					return true;
				}
				else if (c.s == state.cannotpay)
				{
					print ("I see. Please pay next time.");
					c.c.msgPleasePayNextTime();
					checks.remove(c);
					return true;
				}
				else if (c.s == state.paid)
				{
					print ("Thank you for your time");
					c.c.msgThanksYouMayLeave();
					checks.remove(c);
					return true;
				}
			}
		}
		return false;
	}

	// Actions
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
	
	private void payMarket(final MarketBill m)
	{
		
		log.add(new LoggedEvent("Paying market to Deliverer"));
		print ("Paying deliverer...");
		print ("Current cash: "+cash);
		double paying = 0.0;
		
		if (cash - m.amount < 0)
		{
			print ("Not enough money. We will be in debt");
			paying = cash;
			cash = 0;
			//add to our log
			restaurantDebt.put(m.payTo, m.amount - paying);
			return;
		}
		else
		{
			print ("Paid full");
			paying = m.amount;
		}
	
		m.payTo.msgHereIsPayment(paying, this);
		m.s = cashierstate.paidMarket;
	}
	public void giveCheckToWaiter(Check check)
	{
		print ("Total check price is "+check.price);
		check.w.msgCheckForWaiter(check);
		check.s = state.checktoWaiter;
	}
	public String getName() 
	{
		return name;
	}

	

	

	
	
}