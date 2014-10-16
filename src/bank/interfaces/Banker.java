package bank.interfaces;

import bank.BankAccount;


public interface Banker {
	public abstract void msgChangeBalance(double amount, BCustomer c);
	public abstract void msgOpenAccount(double amount, BCustomer c);
	public abstract void msgIWantALoan(double amount, BCustomer c);
	public abstract void msgHereIsAccount(BankAccount act);
	public abstract void msgGetRobbed(Robber r);
	public abstract void msgResumeActivity();
	public abstract int getPosition();
}
