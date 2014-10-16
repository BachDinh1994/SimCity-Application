package restaurant_rancho.test.mock;

import restaurant_rancho.RanchoCashier;
import restaurant_rancho.RanchoCook;
import restaurant_rancho.interfaces.Market;

public class MockMarket extends Mock implements Market{

	public MockMarket(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgFulfillMe(RanchoCook cook, String choice, int i) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("received msgFulfillMe."));
	}

	@Override
	public void setCashier(RanchoCashier c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsPayment(double flow) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("received msgHereIsPayment and payment is "+flow+" dollars."));
	}

}
