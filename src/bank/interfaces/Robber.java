package bank.interfaces;

import java.util.List;


public interface Robber {
	public abstract void msgHereIsCash(Banker b, double money);
	public abstract void msgHereIsCash(BCustomer c, double money);
	public abstract void msgHereAreBankers(List<Banker> l);
	public abstract void msgGoAway();
	public abstract String getWState();
}
