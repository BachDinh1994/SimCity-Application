package amyRestaurant.test.mock;


import amyRestaurant.interfaces.AmyCashier;
import amyRestaurant.interfaces.AmyCustomer;
import amyRestaurant.interfaces.AmyWaiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockWaiter extends Mock implements AmyWaiter {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	//public Cashier cashier;

	public MockWaiter(String name) {
		super(name);

	}
	public String getName() {
		return name;
	}

	public EventLog log = new EventLog();{
		log.clear();
	}
	public AmyCashier amyCashier;
	public AmyCustomer amyCustomer;

	@Override
	public void msgHereIsCheck(AmyCustomer cust, String food, Double money, int table) {
		
		log.add(new LoggedEvent("Received msgHereIsCheck from cashier. Cust = " + cust.getClass().getName() + 
				", food: " + food + ", bill: $" + money.toString() + ", for table #: "  + table)) ;
		// TODO Auto-generated method stub
		
		cust.msgHereIsCheck(money);
	}
	@Override
	public void msgOutOfFood(String choice, int tableNum) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void msgRestocked(String item) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void msgFoodIsReady(String choice, int tableNum) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void msgReadyToOrder(AmyCustomer customerAgent) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void msgGiveOrder(AmyCustomer customerAgent, String myFood) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void msgAskForCheck(AmyCustomer customerAgent) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void msgLeavingTable(AmyCustomer customerAgent) {
		// TODO Auto-generated method stub
		
	}

}
