package amyRestaurant.interfaces;


/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public interface AmyCashier{


	public abstract void msgCreateCheck(String food, int table, AmyWaiter amyWaiter, AmyCustomer cust);

	public abstract void msgCantPay(AmyCustomer cust, double debt);

	public abstract void hereIsMoney(AmyCustomer cust, double cash);
	
	public abstract void msgHereIsBill(Double bill, String food,AmyMarket amyMarket);
	
	public abstract void msgWarning(AmyMarket amyMarket);
	
	public abstract String getName();

	public abstract void msgThankYou(AmyMarket amyMarket);

	public abstract void msgYouHaveToPay(Double cost, AmyMarket amyMarket);

	public abstract void msgHereIsBankMoney(Double money);
	
}

	
