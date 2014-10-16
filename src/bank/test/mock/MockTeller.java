package bank.test.mock;

import bank.BankAccount;
import bank.BankHost;
import bank.interfaces.BCustomer;
import bank.interfaces.BHost;
import bank.interfaces.Banker;
import bank.interfaces.Robber;

public class MockTeller extends Mock implements Banker {
	public EventLog log;
	public BHost host; 
	
	public MockTeller(String name) {
		super(name);
		log = new EventLog();
	}

	@Override
	public void msgChangeBalance(double amount, BCustomer c) {
		log.add(new LoggedEvent("Got request to change an account balance"));
		if (amount < 0){
			amount *= -1;
			if (this.name.toLowerCase().equals("lender")){
				log.add(new LoggedEvent("Giving a full loan"));
				c.msgDoYouWantALoan(0);
			} else if (this.name.toLowerCase().equals("filler")){
				log.add(new LoggedEvent("giving a half loan"));
				amount *= 0.5;
				c.msgDoYouWantALoan(amount);
			} else {
				c.msgHereIsCash(amount);
			}
		} else {
			c.msgHereIsCash(0);
		}
	}

	@Override
	public void msgOpenAccount(double amount, BCustomer c) {
		log.add(new LoggedEvent("Added new account with " + amount +" dollars"));
	}

	@Override
	public void msgIWantALoan(double amount, BCustomer c) {
		log.add(new LoggedEvent("Received request for loan of " + amount + " dollars"));
		c.msgHereIsCash(amount);
	}
	
	public void msgHereIsAccount(BankAccount act) {
		log.add(new LoggedEvent("Updated accounts with new information"));
	}
	
	public int getPosition(){
		return 0;
	}

	@Override
	public void msgGetRobbed(Robber r) {
		double amount = 50;
		if (this.name.equals("low")){
			amount = 10;
		} else if (this.name.equals("medium")){
			amount = 100;
		} else if (this.name.equals("high")){
			amount = 1000;
		}
		log.add(new LoggedEvent("Getting robbed for " +amount));
		if (!name.equals("slow"))
			r.msgHereIsCash(this, amount);
		
	}

	@Override
	public void msgResumeActivity() {
		log.add(new LoggedEvent("Done getting robbed"));		
	}


}
