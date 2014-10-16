package restaurant_rancho.interfaces;

import restaurant_rancho.RanchoCashier;
import restaurant_rancho.RanchoCook;
import restaurant_rancho.test.mock.EventLog;

public interface Market {

	public EventLog log = new EventLog();
	
	public abstract void msgFulfillMe(RanchoCook cook, String choice, int i);

	public abstract String getName();

	public abstract void setCashier(RanchoCashier c);

	public abstract void msgHereIsPayment(double flow);

	public abstract String toString();

}