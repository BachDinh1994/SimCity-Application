package restaurant.interfaces;

import java.util.*;

public interface Market
{
	public abstract void msgNeedSupplies(Map<String,Integer> supply, String choice);
	public abstract void msgPleaseStop();
	public abstract void msgHereIsMoney(int i);
	public abstract String getName();
	public abstract void msgCashierCannotPay(int i);
}