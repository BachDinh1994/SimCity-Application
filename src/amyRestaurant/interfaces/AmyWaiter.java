package amyRestaurant.interfaces;

import amyRestaurant.AmyCustomerRole;





public interface AmyWaiter {




	

	public abstract void msgHereIsCheck(AmyCustomer cust, String food, Double money, int table );
	public abstract String getName();
	public abstract void msgOutOfFood(String choice, int tableNum);
	public abstract void msgRestocked(String item);
	public abstract void msgFoodIsReady(String choice, int tableNum);
	public abstract void msgReadyToOrder(AmyCustomer customerAgent);
	public abstract void msgGiveOrder(AmyCustomer customerAgent, String myFood);
	public abstract void msgAskForCheck(AmyCustomer customerAgent);
	public abstract void msgLeavingTable(AmyCustomer customerAgent);
	public abstract void msgSitCustomer(AmyCustomer amyCustomerRole,
			int tableNumber);
	public abstract void msgBreakRequestAnswer(boolean answer);
	public abstract void msgWantToReturn();
	public abstract void msgWantToGoToBreak();
	public abstract void msgAtTable();
	public abstract void msgAtCook();
	public abstract void msgAtOutside();
	public abstract void msgAtHome();
	public abstract void msgAtCashier();
	

}


