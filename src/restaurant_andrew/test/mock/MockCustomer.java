package restaurant_andrew.test.mock;

import restaurant_andrew.interfaces.Cashier;
import restaurant_andrew.interfaces.Customer;

public class MockCustomer extends Mock implements Customer {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	public Cashier cashier;
	public EventLog log = new EventLog();

	public MockCustomer(String name) {
		super(name);

	}

	@Override
	public void msgHereIsChange(double change) {
		log.add(new LoggedEvent("Received HereIsChange from cashier. Change = "+ change));
	}
}
