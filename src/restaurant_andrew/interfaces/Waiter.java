package restaurant_andrew.interfaces;

import java.util.ArrayList;
import java.util.List;

import restaurant_andrew.AndrewCustomerRole;
import restaurant_andrew.AndrewWaiterRole.MyCustomer;

public interface Waiter 
{
	public abstract void msgHereIsBill(Customer c, double cost);

	public abstract String getName();

	public abstract void msgYesGoOnBreak();

	public abstract void msgNoBreakForYou();

	public abstract int numCustomers();

	public abstract void msgSeatCustomer(AndrewCustomerRole c, int num);

	public abstract void msgReadyToOrder(AndrewCustomerRole andrewCustomerRole);

	public abstract void msgHereIsChoice(AndrewCustomerRole andrewCustomerRole,
			String substring);

	public abstract void msgPaidAndLeaving(AndrewCustomerRole andrewCustomerRole);

	public abstract void msgOutOfFood(String choice, int table);

	public abstract void msgOrderDone(String choice, int table);

	public abstract void msgWantToGoOnBreak();

	public abstract String getCarrying();

	public abstract void msgAtDestination();

	public abstract void msgAtEntrance();
	public abstract void animateSupport();

}
