package amyRestaurant.test.mock;


import java.util.Hashtable;
import java.util.List;

import amyRestaurant.interfaces.AmyCashier;
import amyRestaurant.interfaces.AmyMarket;


//import restaurant.interfaces.Cashier;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockMarket extends Mock implements AmyMarket {

	/**
	 * Reference to the Cashier under test that can be set by the unit test.
	 */
	

	public MockMarket(String name) {
		super(name);

	}

	
	public EventLog log = new EventLog();{
		log.clear();
	}
	private Hashtable<AmyCashier, Double> pastRestDebt = new Hashtable<AmyCashier, Double>();

	public AmyCashier amyCashier;

	@Override
	//from cook , dont need when testing cashier cases
	public void msgOutOf(String food, Integer amt) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgOutOf from cook. Food: " + food + " amt: " + amt));
		
	}
	
	

	@Override
	//from cashier
	public void msgHereIsPayment(Double amt, AmyCashier amyCashier) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgHereIsPayment from cashier. Bill = " + amt));
		amyCashier.msgThankYou(this);
		
	}

	@Override
	public void msgCantPay(Double paidAmt, String food, AmyCashier amyCashier) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Received msgCantPay from cashier. paid amount = " + paidAmt));
		
		amyCashier.msgYouHaveToPay(158.99 + pastRestDebt.get(amyCashier), this);
		pastRestDebt.remove(amyCashier);
		
	}



	@Override
	public Hashtable<AmyCashier, Double> getPastDebt() {
		// TODO Auto-generated method stub
		return pastRestDebt;
	}



	@Override
	public void setPastDebt(AmyCashier c, double debt) {
		// TODO Auto-generated method stub
		pastRestDebt.put(c, debt);
		
	}





}
