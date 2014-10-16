package bank.test.mock;

import java.util.ArrayList;
import java.util.List;

import bank.BankAccount;
import bank.test.mock.EventLog;
import bank.interfaces.BCustomer;
import bank.interfaces.BHost;
import bank.interfaces.Banker;
import bank.interfaces.Robber;

public class MockBankHost extends Mock implements BHost {
	public EventLog log;
	public BCustomer customer;
	public Banker banker;
	
	public List<Banker> bankerList;
	
	public MockBankHost(String name) {
		super(name);
		log = new EventLog();
		bankerList = new ArrayList<Banker>();
	}

	@Override
	public void msgJoiningLine(BCustomer c) {
		log.add(new LoggedEvent("Got a message to send a customer to a teller"));
		c.msgGoSeeTeller(banker);
	}

	@Override
	public void msgImFree(Banker t) {
		log.add(new LoggedEvent("Got a message that a teller is free"));

	}

	@Override
	public void msgImFree(Banker t, BankAccount a) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Updated account, and added teller"));
	}

	public void msgGiveMeBankers(Robber r) {
		// TODO Auto-generated method stub
		if (r.getWState().equals("Armed")){
			log.add(new LoggedEvent("Got request for bankers, sending it"));
			r.msgHereAreBankers(bankerList);
		} else{
			log.add(new LoggedEvent("Turned away unarmed robber"));
			r.msgGoAway();
		}
	}
	
	public void msgAccountDrained(BankAccount a){
		log.add(new LoggedEvent("Drained account of " + a.getBalance()));
	}

	public void msgResumeActivity() {
		log.add(new LoggedEvent("Done getting robbed"));
		for (Banker b : bankerList){
			b.msgResumeActivity();
		}
	}
	
	//For testing
	public void addBanker(Banker b){
		bankerList.add(b);
	}

}
