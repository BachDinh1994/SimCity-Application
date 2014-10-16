package restaurant.interfaces;

import Role.WaiterRole.Customer;
import restaurant.CashierAgent.Check;

public interface Waiters
{
	public abstract void msgWaitingForCheck(Customers c);
	public abstract void msgDeniedBreak();
	public abstract void msgStartBreak();
	public abstract void msgGoRest();
	public abstract void msgOutOfFood(int table, String choice);
	public abstract void msgPleaseStop();
	public abstract void msgPleaseSitCustomer(Customers c,int table);
	public abstract void msgGiveCustomerTime(Customer c);
	public abstract void msgReadyToOrder(Customers c);
	public abstract void msgHereIsMyOrder(Customers c, String choice);
	public abstract void msgDoneCooking(String choice, int table);
	public abstract void msgCheckForWaiter(Check check);
	public abstract void msgLeavingTable(Customers cust);
	public abstract Object getName();
}