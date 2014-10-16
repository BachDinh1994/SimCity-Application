package restaurant.test.mock;

import restaurant.CashierAgent;
import restaurant.Menu;
import restaurant.CashierAgent.Check;
import restaurant.interfaces.Customers;
import restaurant.interfaces.Waiters;

public class MockCustomer extends Mock implements Customers 
{
	CashierAgent cashier;
	EventLog log = new EventLog();
	public MockCustomer(String name) 
	{
		super(name);
	}
	public void msgOutOfChoice(final Menu newMenu)
	{
		log.add(new LoggedEvent("Received menu to reorder"));
	}
	public void msgPleaseStop()
	{
		log.add(new LoggedEvent("Stopping"));
	}
	public void gotHungry()
	{
		log.add(new LoggedEvent("Customer getting hungry"));
	}
	public void msgFollowMe(Menu menu,Waiters waiter)
	{
		log.add(new LoggedEvent("Following waiter to table"));
	}
	public void msgHasChosenOrder()
	{
		log.add(new LoggedEvent("Customer has chosen an order"));
	}
	public void msgYourChoicePlease()
	{
		log.add(new LoggedEvent("Customer ordering"));
	}
	public void msgHereIsYourFood(String choice)
	{
		log.add(new LoggedEvent("Received "+choice));
	}
	public void msgAnimationFinishedGoToSeat() 
	{
		log.add(new LoggedEvent("Informed by animation that customer has gone to seat"));
	}
	public void msgCheckForCustomer(Check check)
	{
		log.add(new LoggedEvent("Received check from waiter. Total amount is: "+check.price+" for "+check.choice));
		if (getName() == "flake")
		{
			cashier.msgNotEnoughMoney(check);
		}
		else if (getName() == "rich")
		{
			cashier.msgPayment(check, 40);
		}
		else if (getName() == "ordinary")
		{
			cashier.msgPayment(check, 20);
		}
		else
		{
			cashier.msgPayment(check, 11);
		}
	}
	public void msgThanksYouMayLeave()
	{
		log.add(new LoggedEvent("Customer may leave now"));
	}
	public void msgPleasePayNextTime()
	{
		log.add(new LoggedEvent("Customer will pay next time"));
	}
	public void msgPleaseWaitFullRestaurant(final Customers c)
	{
		log.add(new LoggedEvent("Customer "+c.getName()+" is requested to wait"));
	}
	public void setTable (int n)
	{
		log.add(new LoggedEvent("Setting table "+n));
	}
	public void setCashier(CashierAgent cashier)
	{
		this.cashier = cashier;
	}
}
