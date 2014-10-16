package restaurant_rancho.interfaces;

import restaurant_rancho.RanchoMarket;

public interface Cashier {

	public abstract void msgComputeBill(Waiter w, String choice, int table);

	public abstract void msgHereIsPayment(Customer customerAgent, double check,
			double cash);

	public abstract String getName();

	public abstract void msgPayMarket(Market market, String choice, int fulfill);

}