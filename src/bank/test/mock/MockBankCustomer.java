package bank.test.mock;

import bank.BankAccount;
import bank.interfaces.BCustomer;
import bank.interfaces.Banker;

public class MockBankCustomer extends Mock implements BCustomer {
	public EventLog log;
	private Banker banker;
	
	public MockBankCustomer(String name){
		super(name);
		log = new EventLog();
	}
	
	public void msgGoSeeTeller(Banker ag) {
		banker = ag;
		log.add(new LoggedEvent("Got message to go see banker"));
		if (name.toLowerCase().equals("deposit")){
			banker.msgChangeBalance(100, this);
		} else if (name.toLowerCase().equals("withdraw")){
			banker.msgChangeBalance(-100,this);
		} else if (name.toLowerCase().equals("poor")){
			banker.msgChangeBalance(-150, this);
		}

	}

	public void msgAccountClosed() {
		log.add(new LoggedEvent("Informed that account is closed"));
	}

	public void msgDoYouWantALoan(double amountReceived) {
		if (name.toLowerCase().contains("poor")){
			double lend = 150 - amountReceived;
			log.add(new LoggedEvent("Taking out loan for " + lend + " dollars"));
			banker.msgIWantALoan(100, this);
		} else {
			log.add(new LoggedEvent("Don't want loan, leaving bank"));
		}

	}

	public void msgHereIsCash(double amt) {
		log.add(new LoggedEvent("Got " + amt + " dollars, leaving bank."));
	}
	
	public void msgWaitInLine(int pos){
		log.add(new LoggedEvent("Told to wait in line at position " + pos));
	}

}
