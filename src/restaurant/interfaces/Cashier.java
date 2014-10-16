package restaurant.interfaces;

import market.MarketAgent.dCheck;
import restaurant.CashierAgent.Check;


public interface Cashier 
{
	public abstract void msgPleaseStop(); 
	//Test the market scenarios
	public abstract void msgCalculateCheck(Customers c, Waiters w, String choice);
	public abstract void msgPayment(Check check, int money);
	public abstract void msgNotEnoughMoney(Check check);
	public abstract void msgHereIsCheck(dCheck check);
	
	

	
}