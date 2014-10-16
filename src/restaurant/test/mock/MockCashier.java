package restaurant.test.mock;

import market.MarketAgent.dCheck;
import restaurant.CashierAgent.Check;
import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customers;
import restaurant.interfaces.Market;
import restaurant.interfaces.Waiters;

public class MockCashier extends Mock implements Cashier
{
	EventLog log = new EventLog();
	public MockCashier(String name) 
	{
		super(name);
	}
	public void msgPleaseStop()
	{
		log.add(new LoggedEvent("Stopping"));
	}
	public void msgCalculateCheck(Customers c, Waiters w, String choice)
	{
		log.add(new LoggedEvent("Calculating check for customer "+c.getName()+"for order "+choice));
	}
	public void msgPayment(Check check, int money)
	{
		log.add(new LoggedEvent("Payment from customer "+check.c.getName()+" with $"+money));
	}
	public void msgNotEnoughMoney(Check check)
	{
		log.add(new LoggedEvent("Customer "+check.c.getName()+" is unable to pay"));
	}
	public void msgPayingMarket(Market market, String choice, int amount) 
	{
		log.add(new LoggedEvent("Paying market "+market.getName()+" for "+amount+" "+choice));
	}
	@Override
	public void msgHereIsCheck(dCheck check) {
		// TODO Auto-generated method stub
		
	}
}
