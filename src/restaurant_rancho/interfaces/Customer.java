package restaurant_rancho.interfaces;

import restaurant_rancho.RanchoHost;
import restaurant_rancho.RanchoWaiterRole.Menu;
import restaurant_rancho.gui.CustomerGui;
import restaurant_rancho.test.mock.EventLog;

public interface Customer {
	public EventLog log = new EventLog();
	
	public abstract void msgFollowMe(Menu menu, Waiter w, int table);

	public abstract void msgAnimationFinishedGoToSeat();

	public abstract void msgTellMeYourChoice(Waiter w);

	public abstract void msgHereIsYourOrder(String foodToBeSent);

	public abstract void msgAnimationFinishedLeaveRestaurant();

	public abstract void msgTellMeYourNewChoice(Menu menu2,
			Waiter waiterAgent);

	public abstract void msgHereIsCheck(double check);

	public abstract void msgHereIsChange(double change);

	public abstract void msgTableIsFull();

	public abstract void gotHungry();

	public abstract String getName();

	public abstract int getHungerLevel();

	public abstract void setHungerLevel(int hungerLevel);

	public abstract String toString();

	public abstract void setGui(CustomerGui g);

	public abstract CustomerGui getGui();

	public abstract void setHost(RanchoHost host2);

	public abstract void setCashier(Cashier cashier);

	public abstract String getCustomerName();

	public abstract int getTableNumber();

	public abstract void setTableNumber(int tableNumber);

	public abstract String getChoice();

	public abstract void setChoice(String choice);

	public abstract void msgAtWait();

}