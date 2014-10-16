package restaurant_sid.interfaces;

import restaurant_sid.SidCookAgent;

public interface Market {
	public abstract void msgInventoryLow(SidCookAgent c, String o);
	public abstract void msgHereIsCash(String s, double val);
	public abstract void msgNoCash(String s, double val);
}
