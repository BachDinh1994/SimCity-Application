package restaurant.test.mock;

import restaurant.CashierAgent;
import restaurant.CashierAgent.Check;
import restaurant.WaiterAgent.Customer;
import restaurant.interfaces.Customers;
import restaurant.interfaces.Waiters;

public class MockWaiter extends Mock implements Waiters
{
	EventLog log = new EventLog();
	CashierAgent cashier;
	public MockWaiter(String name) 
	{
		super(name);
	}
	public void msgWaitingForCheck(Customers c)
	{
		log.add(new LoggedEvent("Customer "+c.getName()+" is waiting for check"));
	}
	public void msgDeniedBreak()
	{
		log.add(new LoggedEvent("Denied Break from Host"));
	}
	public void msgStartBreak()
	{
		log.add(new LoggedEvent("Trying to start break"));
	}
	public void msgGoRest()
	{
		log.add(new LoggedEvent("Going for rest"));
	}
	public void msgOutOfFood(int table, String choice)
	{
		log.add(new LoggedEvent("Out of "+choice+" for table number "+table));
	}
	public void msgPleaseStop()
	{
		log.add(new LoggedEvent("Stopping"));
	}
	public void msgPleaseSitCustomer(Customers c,int table)
	{
		log.add(new LoggedEvent("Sitting customer "+c.getName()+" at table "+table));
	}
	public void msgGiveCustomerTime(Customer c)
	{
		log.add(new LoggedEvent("Giving customer "+c.c.getName()+" time to order"));
	}
	public void msgReadyToOrder(Customers c)
	{
		log.add(new LoggedEvent("Customer "+c.getName()+" is ready to order"));
	}
	public void msgHereIsMyOrder(Customers c, String choice)
	{
		log.add(new LoggedEvent("Customer "+c.getName()+"'s order is "+choice));
	}
	public void msgDoneCooking(String choice, int table)
	{
		log.add(new LoggedEvent("The following food has been cooked for table "+table+": "+choice));
	}
	public void msgCheckForWaiter(Check check)
	{
		log.add(new LoggedEvent("Received check from cashier for order "+check.choice+" with total "+check.price));
	}
	public void msgLeavingTable(Customers cust)
	{
		log.add(new LoggedEvent("Customer "+cust.getName()+" is leaving table"));
	}
	public void setCashier(CashierAgent cashier)
	{
		this.cashier = cashier;
	}
}
