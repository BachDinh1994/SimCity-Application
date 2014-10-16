package restaurant.test.mock;

import market.MarketAgent.MOrder;
import restaurant.interfaces.Cook;
import restaurant.interfaces.Waiters;

public class MockCook extends Mock implements Cook
{
	public MockCook(String name) 
	{
		super(name);
	}
	public void msgMarketNoDelivery()
	{
		
	}
	public void msgHereIsDelivery(MOrder o)
	{
		
	}
	public void msgPleaseStop()
	{
		
	}
	public void msgHereIsOrder(Waiters w,String choice,int table)
	{
		
	}
}
