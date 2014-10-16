package restaurant_sid.interfaces;

import restaurant_sid.SidCustomerRole;
import restaurant_sid.SidHostAgent;
import restaurant_sid.interfaces.Customer;

public interface Waiter {
	
	public abstract void msgHereIsTheBill(double value, int tNum);
	public abstract void msgHereIsCash(Customer c, double value);
	public abstract void msgCantPay(Customer c, double value);
	public abstract void msgOutOfChoice(String meal);
	public abstract void msgOrderReady(String meal, int table);
	public abstract void setHost(SidHostAgent sidHostAgent);
	public abstract void msgOrderReceived(SidCustomerRole sidCustomerRole,
			String choice);
	public abstract void msgSitAtTable(SidCustomerRole sidCustomerRole,
			int number);
	public abstract void msgRemoveCustomer(SidCustomerRole c);
	public abstract void msgGoOnBreak();
	public abstract void msgReadytoOrder(SidCustomerRole sidCustomerRole);
	public abstract void msgIWantCheck(SidCustomerRole sidCustomerRole);
	public abstract void msgLeavingTable(SidCustomerRole sidCustomerRole);
	public abstract void WantToBreak();
	public abstract void msgAtTable();
	public abstract void msgAtCook();
	public abstract int getWNum();
	
}
