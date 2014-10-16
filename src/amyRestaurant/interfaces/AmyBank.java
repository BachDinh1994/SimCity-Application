package amyRestaurant.interfaces;

import java.util.Hashtable;


public interface AmyBank {

	
	
	public abstract Hashtable<AmyCashier, Double> getBankAccount();

	public abstract void setAccount(AmyCashier c, double money);

	
	public abstract String getName();

	public abstract void msgNeedMoney(AmyCashier amyCashier, Double bill);


}