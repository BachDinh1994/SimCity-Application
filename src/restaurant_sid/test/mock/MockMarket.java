package restaurant_sid.test.mock;

import restaurant_sid.SidCookAgent;
import restaurant_sid.interfaces.Market;

public class MockMarket extends Mock implements Market{
	public EventLog log;
	public MockMarket(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		log = new EventLog();
	}

	public void msgInventoryLow(SidCookAgent c, String o){
		log.add(new LoggedEvent("Received order"));
	}
	public void msgHereIsCash(String s, double val){
		log.add(new LoggedEvent("Received cash"));
	}
	public void msgNoCash(String s, double val){
		log.add(new LoggedEvent("Received no cash"));
	}
}
