package restaurant_rancho.test.mock;

import restaurant_rancho.RanchoHost;
import restaurant_rancho.RanchoWaiterRole.Menu;
import restaurant_rancho.gui.CustomerGui;
import restaurant_rancho.interfaces.Cashier;
import restaurant_rancho.interfaces.Customer;
import restaurant_rancho.interfaces.Waiter;

/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockCustomer extends Mock implements Customer{
	public Cashier cashier;

	public MockCustomer(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgFollowMe(Menu menu, Waiter w, int table) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("received msgFollowMe from "+w+" to be seated at table "+table));
	}

	@Override
	public void msgAnimationFinishedGoToSeat() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("received msgAnimationFinishedGoToSeat and is on seat right now"));
	}

	@Override
	public void msgTellMeYourChoice(Waiter w) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("received msgTellMeYourChoice from "+w));
	}

	@Override
	public void msgHereIsYourOrder(String foodToBeSent) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("received msgHereIsYourOrder and the choice is "+foodToBeSent));
	}

	@Override
	public void msgAnimationFinishedLeaveRestaurant() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("received msgAnimationFinishedLeaveRestaurant"));
	}

	@Override
	public void msgTellMeYourNewChoice(Menu menu2, Waiter waiterAgent) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("received msgTellMeYourNewChoice from "+waiterAgent));
	}

	@Override
	public void msgHereIsCheck(double check) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("received msgHereIsCheck from the waiter and the check amount is "+check));
	}

	@Override
	public void msgHereIsChange(double change) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("received msgHereIsChange from cashier and change amount is "+change));
	}

	@Override
	public void msgTableIsFull() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("received msgTableIsFull"));
	}

	@Override
	public void gotHungry() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getHungerLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setHungerLevel(int hungerLevel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(CustomerGui g) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CustomerGui getGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setHost(RanchoHost host2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setCashier(Cashier cashier) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getCustomerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTableNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setTableNumber(int tableNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getChoice() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setChoice(String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtWait() {
		// TODO Auto-generated method stub
		
	}
	
}