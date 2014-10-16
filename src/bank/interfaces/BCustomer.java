package bank.interfaces;

public interface BCustomer {
	public abstract void msgGoSeeTeller(Banker ag);
	public abstract void msgAccountClosed();
	public abstract void msgDoYouWantALoan(double amountReceived);
	public abstract void msgHereIsCash(double amt);
	public abstract void msgWaitInLine(int pos);
}
