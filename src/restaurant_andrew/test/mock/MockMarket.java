package restaurant_andrew.test.mock;

import restaurant_andrew.AndrewCashierAgent;
import restaurant_andrew.interfaces.Market;

public class MockMarket extends Mock implements Market {

	public AndrewCashierAgent cashier;
	public EventLog log = new EventLog();
	
	public MockMarket(String name) {
		super(name);
	}

	@Override
	public void msgPayBill(double money) {
		log.add(new LoggedEvent("Received PayBill. Total = " + money));
	}

}
