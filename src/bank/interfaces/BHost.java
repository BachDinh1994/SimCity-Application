package bank.interfaces;

import bank.BankAccount;

public interface BHost {
	public abstract void msgJoiningLine(BCustomer c);
	public abstract void msgImFree(Banker t);
	public abstract void msgImFree(Banker t, BankAccount a);
	public abstract void msgGiveMeBankers(Robber r);
	public abstract void msgAccountDrained(BankAccount a);
	public abstract void msgResumeActivity();
}
