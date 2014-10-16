package Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import restaurant.CashierAgent;
import restaurant.HostAgent;
import restaurant.Menu;
import restaurant.CashierAgent.Check;
import restaurant.gui.CustomerGui;
import restaurant.gui.RestaurantGui;
import restaurant.interfaces.Customers;
import restaurant.interfaces.Waiters;

public class CustomerRole extends Role implements Customers
{
	private RestaurantGui gui;
	private CashierAgent cashier;
	private Timer timer = new Timer();
	private Timer waitingtimer = new Timer();
	private boolean waitingtimerDone = true;
	private int cash;
	private String name;	
	private Menu menu;
	private int hungerLevel = 5;  // determines length of meal
	private CustomerGui customerGui;
	private int tablenum;
	private int init=0; //allows initial hungry checkbox access to be only once
	Check check;
	// agent correspondents
	private HostAgent host;
	private Waiters waiter;

	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, ChoosingOrder, DoneChoosing, WaitingForFood, Eating, WaitingForCheck, Leaving};
	private AgentState state = AgentState.DoingNothing;//The start state
	public enum AgentEvent 
	{none, gotHungry, followWaiter, seated, chosenOrder, ordering, gotOrder, doneEating, gotCheck, doneLeaving,stopping,leaving,outofhere};
	List<AgentEvent> events = new ArrayList<AgentEvent>(); //prevent clobbering of events in messages

	public CustomerRole(String name)
	{
		super();
		this.name = name;
	}
	//establish connection to host and waiter agents
	public void setHost(HostAgent host) 
	{
		this.host = host;
	}
	public String getCustomerName() 
	{
		return name;
	}
	
	// Messages
	public void msgOutOfChoice(final Menu newMenu)
	{
		if (cash < CashierAgent.foodprices.get(newMenu.choices.get(0)))
		{
			events.add(AgentEvent.leaving); //Can't afford anything else
		}
		else
		{
			state = AgentState.DoneChoosing;
			events.add(AgentEvent.ordering);
			menu = newMenu;
		}
		stateChanged();
	}
	public void msgPleaseStop()
	{
		events.add(AgentEvent.stopping);
		stateChanged();
	}
	public void gotHungry() 
	{
		
		events.add(AgentEvent.gotHungry);
		
		stateChanged();
	}
	public void msgFollowMe(Menu menu,Waiters waiter)
	{
		this.waiter = waiter;
		this.menu = menu;
		events.add(AgentEvent.followWaiter);
		if (!waitingtimerDone)
		{
			waitingtimer.cancel();
		}
		stateChanged();
	}
	public void msgHasChosenOrder()
	{
		events.add(AgentEvent.chosenOrder);
		stateChanged();
	}
	public void msgYourChoicePlease()
	{
		events.add(AgentEvent.ordering);
		stateChanged();
	}
	public void msgHereIsYourFood(String choice)
	{
		events.add(AgentEvent.gotOrder);
		stateChanged();
	}
	//messages from animation
	public void msgAnimationFinishedGoToSeat() 
	{
		events.add(AgentEvent.seated);
		stateChanged();
	}
	public void msgCheckForCustomer(Check check)
	{
		this.check = check;
		events.add(AgentEvent.gotCheck);
		stateChanged();
	}
	public void msgThanksYouMayLeave()
	{
		events.add(AgentEvent.doneLeaving);
		stateChanged();
	}
	public void msgPleasePayNextTime()
	{
		cash = 100;
		events.add(AgentEvent.doneLeaving);
		stateChanged();
	}
	public void msgPleaseWaitFullRestaurant(final Customers c)
	{
		int wait = (int)(Math.random()*2);
		customerGui.MoveOut();
		if (wait == 0)
		{
	    	events.add(AgentEvent.outofhere);
	    	//gui.setCustomerEnabled(c);
	    	state = AgentState.DoingNothing;
	    	customerGui.setFullStomach();
	    	customerGui.DoExitRestaurant();
	    	stateChanged();
		}
		else
		{
			waitingtimerDone = false;
			waitingtimer.schedule(new TimerTask() 
			{
			    public void run() 
			    {  
			    	events.add(AgentEvent.outofhere);
			    	waitingtimerDone = true;
			    	//gui.setCustomerEnabled(c);
			    	state = AgentState.DoingNothing;
			    	customerGui.setFullStomach();
			    	customerGui.DoExitRestaurant();
			    	stateChanged();
			    }
			},5000);
		}

	}
	/*public void msgAnimationFinishedLeaveRestaurant() 
	{
		events.add(AgentEvent.doneLeaving);
		stateChanged();
	}*/
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() 
	{
		if (events.isEmpty()) //prevent crash if there is no event
		{
			return false;	
		}
		
		AgentEvent event = events.remove(0); //take the first event and remove it so other events can occur
		if (event == AgentEvent.leaving) //Leave if restaurant's out of choice
		{
			state = AgentState.Leaving;
			//gui.setCustomerEnabled(this);
			state = AgentState.DoingNothing;
			leaveTable();
			return true;
		}
		else if (event == AgentEvent.outofhere)
		{
			host.msgOutOfHere(this);
		}
		else if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry )
		{
			
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		else if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followWaiter )
		{
			state = AgentState.BeingSeated;
			SitDown();
			return true;
		}
		else if (state == AgentState.BeingSeated && event == AgentEvent.seated)
		{
			if (cash < 10 && !name.equals("Flake")) //Can't afford anything on the menu
			{
				CannotAffordAnything();
			}
			else
			{
				state = AgentState.ChoosingOrder;
				makingChoice();
			}
			return true;
		}
		else if (state == AgentState.ChoosingOrder && event == AgentEvent.chosenOrder)
		{
			state = AgentState.DoneChoosing;
			ReadyToOrder();
			return true;
		}
		else if (state == AgentState.DoneChoosing && event == AgentEvent.ordering)
		{
			state = AgentState.WaitingForFood;
			orderFood();
			return true;
		}
		else if (state == AgentState.WaitingForFood && event == AgentEvent.gotOrder)
		{
			state = AgentState.Eating;
			EatFood();
			return true;
		}
		else if (state == AgentState.Eating && event == AgentEvent.doneEating)
		{
			state = AgentState.WaitingForCheck;
			waitingForCheck();
			return true;
		}
		else if (state == AgentState.WaitingForCheck && event == AgentEvent.gotCheck)
		{
			if (cash < check.price)
			{
				cashier.msgNotEnoughMoney(check);
			}
			else
			{
				payCheck();
			}
			state = AgentState.Leaving;
			return true;
		}
		else if (state == AgentState.Leaving && event == AgentEvent.doneLeaving)
		{
			state = AgentState.DoingNothing;
			leaveTable();
			return true;
		}
		return false;
	}
	
	// Actions
	public void CannotAffordAnything()
	{
		events.add(AgentEvent.leaving);
		waiter.msgLeavingTable(this);
		state = AgentState.DoingNothing;
	}
	public void waitingForCheck()
	{
		waiter.msgWaitingForCheck(this);
	}
	public void payCheck()
	{
		cashier.msgPayment(check,check.price);
		cash -= check.price;
	}
	private void goToRestaurant() 
	{
		//Do("Going to restaurant");
		host.msgIWantFood(this);
	}
	private void SitDown() 
	{
		//Do("Being seated. Going to table");
		customerGui.DoGoToSeat(tablenum);
	}
	private void makingChoice()
	{
		//print("Choosing order(5 seconds)");
		timer.schedule(new TimerTask() 
		{
		    public void run() 
		    {  
		    	msgHasChosenOrder();	    
		    }
		},5000);
	 }
    private void ReadyToOrder()
    {
		waiter.msgReadyToOrder(this);
    }
    private void orderFood()
    {
		//String choice = menu.choice[(int)(Math.random()*6)];
    	String choice="";
    	if (name.equals("poorcashier"))
    	{
    		cashier.cash = 100;
    		choice = menu.choices.get((int)(Math.random()*menu.choices.size()));
    	}
    	else if (name.equals("Flake"))
    	{
    		choice = menu.choices.get((int)(Math.random()*menu.choices.size()));
    	}
    	else if (cash < 40)
    	{
    		for (int i=0;i<menu.choices.size();i++)
    		{
    			if (cash >= CashierAgent.foodprices.get(menu.choices.get(i)))
    			{
    				choice = menu.choices.get(i);
    				continue;
    			}
    		}
    	}
    	else 
    	{
    		choice = menu.choices.get((int)(Math.random()*menu.choices.size()));
    	}
		waiter.msgHereIsMyOrder(this, choice);
    }
	private void EatFood() 
	{
		timer.schedule(new TimerTask() 
		{
			public void run() 
			{
				events.add(AgentEvent.doneEating);
				stateChanged();
			}
		},5000);
	}
	private void leaveTable() 
	{
		//Do("Leaving.");
		waiter.msgLeavingTable(this);
		customerGui.MoveOut();
		//customerGui.MoveToHomePosition();
	}
	// Accessors and mutators
	public String getName() 
	{
		return name;
	}
	public String toString() 
	{
		return "customer " + getName();
	}
	public int getHungerLevel() 
	{
		return hungerLevel;
	}
	public void setHungerLevel(int hungerLevel) 
	{
		this.hungerLevel = hungerLevel;
	}
	public CustomerGui getGui() 
	{
		return customerGui;
	}
	public void setGui(CustomerGui g) 
	{
		customerGui = g;
	}
	public void setTable(int table)
	{
		tablenum = table;
	}
	public int getInit()
	{
		return init;
	}
	public void incInit()
	{
		init++;
	}
	public void setCashier(CashierAgent cashier)
	{
		this.cashier = cashier;
	}
	public void setRestGui(RestaurantGui gui)
	{
		this.gui = gui;
	}
	public void setCash(int cash)
	{
		this.cash = cash;
	}
}