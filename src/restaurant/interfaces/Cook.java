package restaurant.interfaces;

import market.MarketAgent.MOrder;

public interface Cook
{
	public abstract void msgMarketNoDelivery();
	public abstract void msgHereIsDelivery(MOrder o);
	public abstract void msgPleaseStop();
	public abstract void msgHereIsOrder(Waiters w,String choice,int table);
}