package restaurant.test.mock;

import restaurant.interfaces.Customers;
import restaurant.interfaces.Host;
import restaurant.interfaces.Waiters;

public class MockHost extends Mock implements Host
{
	public MockHost(String name) 
	{
		super(name);
	}
	public void msgOutOfHere(Customers c)
	{
		
	}
	public void msgDoneBreak(Waiters w)
	{
		
	}
	public void msgWantBreak(Waiters w)
	{
		
	}
	public void msgPleaseStop()
	{
		
	}
	public void msgIWantFood(Customers cust)
	{
		
	}
	public void msgTableFree(int table, Waiters w)
	{
		
	}
}
