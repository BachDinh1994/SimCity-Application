package amyRestaurant.interfaces;

import java.util.Hashtable;

;


/*Market interface*/

public interface AmyMarket{

	
	public abstract void msgOutOf(String food, Integer amt );
	
	public abstract void msgHereIsPayment(Double amt, AmyCashier amyCashier);
	
	public abstract void msgCantPay(Double paidAmt, String food,AmyCashier amyCashier);
	
	public abstract String getName();
	
	public abstract Hashtable<AmyCashier, Double> getPastDebt();
	
	public abstract void setPastDebt(AmyCashier c, double debt);
	

	
}