package bank;

import bank.interfaces.BCustomer;


public class BankAccount{
	private boolean AccountOpen;
	private double balance;
	private double moneyOwed;
	private BCustomer owner;
	
	public BankAccount(BCustomer cust, double bal){
		balance = bal;
		owner = cust;
		moneyOwed = 0;
		AccountOpen = true;
	}
	
	public BCustomer getCustomer(){
		return owner;
	}
	public double getBalance(){
		return balance;
	}
	public void setBalance(double b){
		balance = b;
	}
	public void setLoanAmount(double m){
		moneyOwed = m;
	}
	public double getLoanAmount(){
		return moneyOwed;
	}
	public void setAccountStatus(boolean status){
		AccountOpen = status;
	}
	public boolean getStatus(){
		return AccountOpen;
	}
}
