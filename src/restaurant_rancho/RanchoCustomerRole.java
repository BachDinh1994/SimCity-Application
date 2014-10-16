package restaurant_rancho;

import restaurant_rancho.RanchoWaiterRole.Menu;
import restaurant_rancho.gui.CustomerGui;
import restaurant_rancho.interfaces.Cashier;
import restaurant_rancho.interfaces.Customer;
import restaurant_rancho.interfaces.Waiter;
import Role.Role;
import agent.Agent;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

/**
 * Restaurant customer agent.
 */
public class RanchoCustomerRole extends Role implements Customer{
	private String name;
	private int hungerLevel = 5;        // determines length of meal
	Timer timer = new Timer();
	private CustomerGui customerGui;
	private int tableNumber;
	private String choice;
	private Menu menu;
	private double check;
	private double cash;
	private Semaphore atWait=new Semaphore(0,true);
    
	// agent correspondents
	private RanchoHost host;
	private Cashier cashier;
	private Waiter myWaiter;
    
	//    private boolean isHungry = false; //hack for gui
	public enum AgentState
	{DoingNothing, WaitingInRestaurant, BeingSeated, Seated, WaitingWaiter,WaitingOrder,Eating, DoneEating, Paying,WaitingChange,Leaving};
	private AgentState state = AgentState.DoingNothing;//The start state
    
	public enum AgentEvent 
	{none, gotHungry, followWaiter, seated, tellOrder,eating,doneEating, paying,gettingChange,doneLeaving,tableFull};
	AgentEvent event = AgentEvent.none;
    
	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 * @param gui  reference to the customergui so the customer can send it messages
	 */
	public RanchoCustomerRole(String name){
		super();
		this.name = name;
		//this.cash=5;
		this.cash=Math.random()*30;//generate doubles from 0 to 30
		
	}
    

    
	
	
	// Messages
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgFollowMe(restaurant.WaiterAgent.Menu, restaurant.WaiterAgent, int)
	 */
	public void msgFollowMe(Menu menu,Waiter w,int table) {
		// TODO Auto-generated method stub
		event=AgentEvent.followWaiter;
		this.menu=menu;
		//print("received msgFollowMe");
		tableNumber=table;
		myWaiter=w;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgAnimationFinishedGoToSeat()
	 */
	public void msgAnimationFinishedGoToSeat() {
		//from animation
		event = AgentEvent.seated;
		state = AgentState.Seated;
		stateChanged();
	}
	
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgTellMeYourChoice(restaurant.WaiterAgent)
	 */
	public void msgTellMeYourChoice(Waiter w) {
		// TODO Auto-generated method stub
		event=AgentEvent.tellOrder;
		myWaiter=w;
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#msgHereIsYourOrder(java.lang.String)
	 */
	public void msgHereIsYourOrder(String foodToBeSent) {
		// TODO Auto-generated method stub
		event=AgentEvent.eating;
		stateChanged();
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#msgAnimationFinishedLeaveRestaurant()
	 */
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		event = AgentEvent.doneLeaving;
		stateChanged();
	}
	


	/* (non-Javadoc)
	 * @see restaurant.Customer#msgTellMeYourNewChoice(restaurant.WaiterAgent.Menu, restaurant.WaiterAgent)
	 */
	public void msgTellMeYourNewChoice(Menu menu2,
			Waiter waiterAgent) {
		// TODO Auto-generated method stub
		this.menu=menu2;
		state=AgentState.WaitingWaiter;
		stateChanged();
	}





	/* (non-Javadoc)
	 * @see restaurant.Customer#msgHereIsCheck(double)
	 */
	public void msgHereIsCheck(double check) {
		// TODO Auto-generated method stub
		this.check=check;
		event=AgentEvent.paying;
		stateChanged();
	}





	/* (non-Javadoc)
	 * @see restaurant.Customer#msgHereIsChange(double)
	 */
	public void msgHereIsChange(double change) {
		// TODO Auto-generated method stub
		cash+=change;
		event=AgentEvent.gettingChange;
		stateChanged();
	}





	/* (non-Javadoc)
	 * @see restaurant.Customer#msgTableIsFull()
	 */
	public void msgTableIsFull() {
		// TODO Auto-generated method stub
		int num=(int)Math.random()*2;
		if(num==0){
			//print("Never mind. I am still waiting.");
		}
		else{
			//print("Sorry I don't wanna wait. I am leaving.");
			event=AgentEvent.tableFull;
			stateChanged();
		}
	}

	
	
	
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		//	CustomerAgent is a finite state machine
        
		if (state == AgentState.DoingNothing && event == AgentEvent.gotHungry ){
			state = AgentState.WaitingInRestaurant;
			goToRestaurant();
			return true;
		}
		if (state == AgentState.WaitingInRestaurant && event == AgentEvent.followWaiter ){
			state = AgentState.BeingSeated;
			
			SitDown();
			return true;
		}
		
		if(state == AgentState.WaitingInRestaurant && event == AgentEvent.tableFull){
			state=AgentState.Leaving;
			tellWaiterImDone();
			host.msgImNotWaiting(this);
			return true;
		}
        if(state==AgentState.Seated && event==AgentEvent.seated){
        	state=AgentState.WaitingWaiter;
        	if(cash<menu.getMinCost()){
        		//print("I only have "+cash+" dollars.");
        		//print("Cannot afford any food!");
				state=AgentState.Leaving;
				tellWaiterImDone();
				return true;
			}
        	callWaiter();
        	return true;
        }
        if(state==AgentState.WaitingWaiter && event==AgentEvent.tellOrder){
        	state=AgentState.WaitingOrder;
        	if(cash<menu.getMinCost()){
        		//print("I only have "+cash+" dollars.");
        		//print("Cannot afford any available food!");
				state=AgentState.Leaving;
				tellWaiterImDone();
				return true;
			}
        	giveOrder();
        	return true;
        }
        if(state==AgentState.WaitingOrder && event==AgentEvent.eating){
        	state=AgentState.Eating;
        	eatFood();
        	return true;
        }
		if (state == AgentState.Eating && event == AgentEvent.doneEating){
			state = AgentState.Paying;
			//tellWaiterImDone();
			check();
			return true;
		}
		if(state==AgentState.Paying && event == AgentEvent.paying){
			state=AgentState.WaitingChange;
			payBill();
			return true;
		}
		if(state==AgentState.WaitingChange && event==AgentEvent.gettingChange){
			state=AgentState.Leaving;
			tellWaiterImDone();
			return true;
		}
		if (state == AgentState.Leaving && event == AgentEvent.doneLeaving){
			state = AgentState.DoingNothing;
			//no action
			return true;
		}
		return false;
	}
    
	
	
	
	
	
	// Actions
	
	private void payBill() {
		// TODO Auto-generated method stub
		//print("Paying bills");
		double temp=cash;
		cash=0;
		cashier.msgHereIsPayment(this,check,temp);
	}





	private void check() {
		// TODO Auto-generated method stub
		//print("Asking for check");
		myWaiter.msgReadyForCheck(this,choice);
	}





	/* (non-Javadoc)
	 * @see restaurant.Customer#gotHungry()
	 */
	public void gotHungry() {//from animation
		//print("I'm hungry");
		event = AgentEvent.gotHungry;
		stateChanged();
	}
	
	
	private void goToRestaurant() {
		//Do("Going to restaurant");
		customerGui.DoGoToWaiting();
		try{
			atWait.acquire();
		}catch(InterruptedException e){
			e.printStackTrace();
		}

		host.msgIWantToEat(this);//send our instance, so he can respond to us
	}
    
	private void SitDown() {
		//Do("Being seated. Going to table");
		customerGui.setTablePosition();
		customerGui.DoGoToSeat(tableNumber);//hack; only one table
		
	}
	
	private void callWaiter() {
		// TODO Auto-generated method stub
		myWaiter.msgReadyToOrder(this);
		DoCallWaiter();
	}

	private void DoCallWaiter() {
		// TODO Auto-generated method stub
		//Do("I am ready to order!");
		
	}

	private void giveOrder() {
		choice=menu.getChoice(cash);
		myWaiter.msgHereIsMyChoice(this,choice);
		DoGiveOrder();
	}

	private void DoGiveOrder() {
		// TODO Auto-generated method stub
		//Do("That's what I want to order.");
	}

	private void eatFood() {
		//Do("Eating Food");
		//This next complicated line creates and starts a timer thread.
		//We schedule a deadline of getHungerLevel()*1000 milliseconds.
		//When that time elapses, it will call back to the run routine
		//located in the anonymous class created right there inline:
		//TimerTask is an interface that we implement right there inline.
		//Since Java does not all us to pass functions, only objects.
		//So, we use Java syntactic mechanism to create an
		//anonymous inner class that has the public method run() in it.
		timer.schedule(new TimerTask() {
			Object cookie = 1;
			public void run() {
				//print("Done eating, cookie=" + cookie);
				event = AgentEvent.doneEating;
				stateChanged();
			}
		},
                       10000);//getHungerLevel() * 1000);//how long to wait before running task
	}
    
	private void tellWaiterImDone() {
		//Do("Leaving.");
		myWaiter.msgImDone(this);
		customerGui.DoExitRestaurant();
		event=AgentEvent.doneLeaving;
	}
    
	
	
	
	
	
	
	
	// Accessors, etc.
    
	/* (non-Javadoc)
	 * @see restaurant.Customer#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#getHungerLevel()
	 */
	public int getHungerLevel() {
		return hungerLevel;
	}
    
	/* (non-Javadoc)
	 * @see restaurant.Customer#setHungerLevel(int)
	 */
	public void setHungerLevel(int hungerLevel) {
		this.hungerLevel = hungerLevel;
		//could be a state change. Maybe you don't
		//need to eat until hunger lever is > 5?
	}
    
	/* (non-Javadoc)
	 * @see restaurant.Customer#toString()
	 */
	@Override
	public String toString() {
		return "customer " + getName();
	}
    
	/* (non-Javadoc)
	 * @see restaurant.Customer#setGui(restaurant.gui.CustomerGui)
	 */
	public void setGui(CustomerGui g) {
		customerGui = g;
	}
    
	/* (non-Javadoc)
	 * @see restaurant.Customer#getGui()
	 */
	public CustomerGui getGui() {
		return customerGui;
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#setHost(restaurant.HostAgent)
	 */
	public void setHost(RanchoHost host2) {
		// TODO Auto-generated method stub
		host=host2;
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#setCashier(restaurant.interfaces.Cashier)
	 */
	public void setCashier(Cashier cashier){
		this.cashier=cashier;
	}
	
	
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#getCustomerName()
	 */
	public String getCustomerName() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Customer#getTableNumber()
	 */
	public int getTableNumber() {
		return tableNumber;
	}

	/* (non-Javadoc)
	 * @see restaurant.Customer#setTableNumber(int)
	 */
	public void setTableNumber(int tableNumber) {
		this.tableNumber = tableNumber;
	}





	/* (non-Javadoc)
	 * @see restaurant.Customer#getChoice()
	 */
	public String getChoice() {
		return choice;
	}





	/* (non-Javadoc)
	 * @see restaurant.Customer#setChoice(java.lang.String)
	 */
	public void setChoice(String choice) {
		this.choice = choice;
	}





	@Override
	public void msgAtWait() {
		// TODO Auto-generated method stub
		atWait.release();
	}



}
