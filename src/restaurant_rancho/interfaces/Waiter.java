package restaurant_rancho.interfaces;

import restaurant_rancho.gui.WaiterGui;
import restaurant_rancho.test.mock.EventLog;

public interface Waiter {
	public EventLog log= new EventLog();
	public abstract void msgBreakReply(boolean reply);

	public abstract void msgRunOutOfFood(String choice, int table);

	public abstract void msgAtDoor();

	public abstract void msgSitAtTable(Customer customerAgent, int t);

	public abstract void msgAtTable();
	public abstract void msgAtManager();

	public abstract void msgReadyToOrder(Customer customerAgent);

	public abstract void msgHereIsMyChoice(Customer customerAgent, String choice);

	public abstract void msgAtCook();

	public abstract void msgOrderIsReady(String choice, int table);

	public abstract void msgImDone(Customer customerAgent);

	//utilities
	public abstract void setGui(WaiterGui gui);

	public abstract WaiterGui getGui();

	public abstract void wantToGoOnBreak();

	public abstract String getName();

	public abstract void comeBackToWork();

	public abstract void msgReadyForCheck(Customer customerAgent, String choice);

	public abstract void msgCheckReady(double check, int table);

	public abstract void msgAtCashier();
}
