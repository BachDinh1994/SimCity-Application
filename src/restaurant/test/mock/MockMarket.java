package restaurant.test.mock;

import java.util.Map;

import restaurant.CashierAgent;
import restaurant.interfaces.Market;

public class MockMarket extends Mock implements Market
{
	CashierAgent cashier;
	EventLog log = new EventLog();
	public MockMarket(String name) 
	{
		super(name);
	}
	public void msgHereIsMoney(int i) 
	{
		log.add(new LoggedEvent("Received money from cashier: "+i));
	}
	public void msgNeedSupplies(Map<String,Integer> supply, String choice)
	{
		log.add(new LoggedEvent("Requested supplies from cook for: "+supply.get(choice)+" "+choice));
	}
	public void msgPleaseStop()
	{
		log.add(new LoggedEvent("Stopping"));
	}
	public void setCashier(CashierAgent cashier) 
	{
		this.cashier = cashier;		
	}
	public void msgCashierCannotPay(int i) 
	{
		log.add(new LoggedEvent("Cashier owes the following amount: "+i));
	}
}