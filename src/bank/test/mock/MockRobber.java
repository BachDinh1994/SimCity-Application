package bank.test.mock;

import java.util.List;

import bank.interfaces.BCustomer;
import bank.interfaces.Banker;
import bank.interfaces.Robber;

public class MockRobber extends Mock implements Robber {
	public EventLog log;
	
	public MockRobber(String name) {
		super(name);
		log = new EventLog();
	}

	@Override
	public void msgHereIsCash(Banker b, double money) {
		log.add(new LoggedEvent("Got "+ money + "from teller"));

	}

	@Override
	public void msgHereIsCash(BCustomer c, double money) {
		//This hasn't been implemented yet.
	}

	@Override
	public void msgHereAreBankers(List<Banker> l) {
		log.add(new LoggedEvent("Got list with " + l.size() + " bankers"));
	}

	@Override
	public void msgGoAway() {
		log.add(new LoggedEvent("Leaving bank"));
	}

	@Override
	public String getWState() {
		if (this.name.contains("armed"))
			return "Armed";
		else
			return "Unarmed";
	}

}
