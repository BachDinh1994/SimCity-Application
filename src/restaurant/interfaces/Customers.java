package restaurant.interfaces;

import restaurant.CashierAgent.Check;
import restaurant.Menu;

public interface Customers
{
	public abstract void msgOutOfChoice(final Menu newMenu);
	public abstract void msgPleaseStop();
	public abstract void gotHungry();
	public abstract void msgFollowMe(Menu menu,Waiters waiter);
	public abstract void msgHasChosenOrder();
	public abstract void msgYourChoicePlease();
	public abstract void msgHereIsYourFood(String choice);
	public abstract void msgAnimationFinishedGoToSeat() ;
	public abstract void msgCheckForCustomer(Check check);
	public abstract void msgThanksYouMayLeave();
	public abstract void msgPleasePayNextTime();
	public abstract void msgPleaseWaitFullRestaurant(final Customers c);
	public abstract void setTable(int n);
	public abstract String getName();
}