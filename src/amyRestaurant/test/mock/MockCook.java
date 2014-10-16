package amyRestaurant.test.mock;



import amyRestaurant.interfaces.AmyCashier;
import amyRestaurant.interfaces.AmyCook;
import amyRestaurant.interfaces.AmyMarket;
import amyRestaurant.interfaces.AmyWaiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockCook extends Mock implements AmyCook {

	public MockCook(String name) {
		super(name);

	}

	public EventLog log;
	public AmyCashier amyCashier;
	@Override
	public void msgHereIsOrder(String foodToCook, int forLocation,
			AmyWaiter myWaiter) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void msgWeHave(String food , int amt, boolean complete) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void msgWeAreOut(AmyMarket amyMarket, String food) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void msgHereIsDelivery(String food, Integer amt) {
		// TODO Auto-generated method stub
		
	}

}
