package restaurant_rancho.test.mock;

import restaurant_rancho.gui.WaiterGui;
import restaurant_rancho.interfaces.Customer;
import restaurant_rancho.interfaces.Waiter;

public class MockWaiter extends Mock implements Waiter {
	
	public MockWaiter(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgBreakReply(boolean reply) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgRunOutOfFood(String choice, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtDoor() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSitAtTable(Customer customerAgent, int t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtTable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadyToOrder(Customer customerAgent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsMyChoice(Customer customerAgent, String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtCook() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderIsReady(String choice, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgImDone(Customer customerAgent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(WaiterGui gui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public WaiterGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void wantToGoOnBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void comeBackToWork() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadyForCheck(Customer customerAgent, String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgCheckReady(double check, int table) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("received msgCheckReady. check is "+check+" and table is "+table));
	}

	@Override
	public void msgAtCashier() {
		// TODO Auto-generated method stub
		
	}

}
