package restaurant_andrew.interfaces;

public abstract interface Cashier {
	
	public abstract void msgCalculateBill(Waiter w, Customer c, String choice);
	
	public abstract void msgPayingBill(Customer c, double cash);
	
	public abstract boolean pickAndExecuteAnAction();
	
	public abstract String getName();
}
