package restaurant_andrew.test.mock;

import restaurant_andrew.AndrewCashierAgent;
import restaurant_andrew.interfaces.Customer;
import restaurant_andrew.interfaces.Waiter;

public class MockWaiter extends Mock implements Waiter {

	public AndrewCashierAgent cashier;
	public EventLog log = new EventLog();
	
	public MockWaiter(String name) {
		super(name);
	}

	@Override
	public void msgHereIsBill(Customer c, double cost) {
		log.add(new LoggedEvent("Received HereIsBill for " + c.getName() + ". Total = " + cost));
	}

}
