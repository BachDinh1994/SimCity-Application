package restaurant.interfaces;

public interface Host
{
	public abstract void msgOutOfHere(Customers c);
	public abstract void msgDoneBreak(Waiters w);
	public abstract void msgWantBreak(Waiters w);
	public abstract void msgPleaseStop();
	public abstract void msgIWantFood(Customers cust);
	public abstract void msgTableFree(int table, Waiters w);
}