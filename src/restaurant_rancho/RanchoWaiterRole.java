package restaurant_rancho;

import Role.Role;
import agent.Agent;
import restaurant_rancho.gui.WaiterGui;
import restaurant_rancho.interfaces.Cashier;
import restaurant_rancho.interfaces.Customer;
import restaurant_rancho.interfaces.Waiter;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Waiter Agent
 */

public class RanchoWaiterRole extends Role implements Waiter {
	
	private boolean timeToLeave;
	public List<MyCustomer> customers = new ArrayList<MyCustomer>();
	public WaiterGui waiterGui = null;
	private Semaphore atTable = new Semaphore(0,true);
	private Semaphore atCook = new Semaphore(0,true);
	private Semaphore atDoor = new Semaphore(0,true);
	private Semaphore atCashier = new Semaphore(0,true);
	private Semaphore atManager = new Semaphore(0,true);
	private String foodToBeSent=null;
	private String foodToOrder=null;
	private String deletedChoice=null;
	private String name;
	private boolean onBreak=false;
	private String choiceForCheck;
	private double currentCheck;
	
	private RanchoManager manager;
	private RanchoHost host;
	private RanchoCook cook;
	private Cashier cashier;
	public enum AgentState{waiting,readyToOrder,givingChoice,beingToldNoFood,beingServed,askingCheck,receivingCheck,leaving,noAction};
	
	public RanchoWaiterRole(RanchoHost host,RanchoCook cook,Cashier cashier,String name) {
		super();
		this.name=name;
		this.host=host;
		this.cook=cook;
		this.cashier=cashier;
	}
    
	
	
	// Messages
	
	public RanchoWaiterRole() 
	{
		super();
	}
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgBreakReply(boolean)
	 */
	@Override
	public void msgBreakReply(boolean reply) {
		// TODO Auto-generated method stub
		if(reply){
			onBreak=true;
			stateChanged();
			return;
		}
		else{
			onBreak=false;
			waiterGui.setOnBreak(false);
			waiterGui.setApplying(false);
		}
	}
	

	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgRunOutOfFood(java.lang.String, int)
	 */
	@Override
	public void msgRunOutOfFood(String choice, int table) {
		// TODO Auto-generated method stub
		try{
		for(MyCustomer cust:customers){
			if(cust.tableNumber==table){
				//change
				deletedChoice=choice;
				cust.state = AgentState.beingToldNoFood;
				stateChanged();
				return;
			}
		}
		}catch(ConcurrentModificationException e){
			System.out.println("Concurrent Modification Error.");
		}
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgAtDoor()
	 */
	@Override
	public void msgAtDoor(){
		atDoor.release();
	}
	
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgSitAtTable(restaurant.interfaces.Customer, int)
	 */
	@Override
	public void msgSitAtTable(Customer customerAgent, int t) {
		// TODO Auto-generated method stub
		customers.add(new MyCustomer(customerAgent,t));
		
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgAtTable()
	 */
	@Override
	public void msgAtTable() {//from animation
		
		atTable.release();// = true;
	}
	
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgReadyToOrder(restaurant.interfaces.Customer)
	 */
	@Override
	public void msgReadyToOrder(Customer customerAgent){
		try{
		for(MyCustomer cust:customers){
			if(cust.customer==customerAgent ){
				cust.state=AgentState.readyToOrder;
				//stateChanged();
				stateChanged();
				
				//return;
			}
		}
		}catch(ConcurrentModificationException e){
			System.out.println("Concurrent Modification Error.");
		}
	}
	

	
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgHereIsMyChoice(restaurant.interfaces.Customer, java.lang.String)
	 */
	@Override
	public void msgHereIsMyChoice(Customer customerAgent, String choice){
		try{
		for(MyCustomer cust:customers){
			if(cust.customer==customerAgent){
				cust.state=AgentState.givingChoice;
				foodToOrder=choice;
				stateChanged();
				return;
			}
		}
		}catch(ConcurrentModificationException e){
			System.out.println("Concurrent Modification Error.");
		}
		
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgAtCook()
	 */
	@Override
	public void msgAtCook() {
		// TODO Auto-generated method stub
		atCook.release();
	}
	
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgOrderIsReady(java.lang.String, int)
	 */
	@Override
	public void msgOrderIsReady(String choice, int table) {
		try{
			for(MyCustomer cust:customers){
				if(cust.tableNumber==table){
					
					foodToBeSent = choice;
					cust.state = AgentState.beingServed;
					stateChanged();
					return;
				}
			}
		}catch(ConcurrentModificationException e){
			System.out.println("Concurrent Modification Error.");
		}
	}

	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgImDone(restaurant.interfaces.Customer)
	 */
	@Override
	public void msgImDone(Customer customerAgent){
		try{
			for(MyCustomer cust: customers){
				if(cust.customer==customerAgent){
					//print(customerAgent + " leaving Table " + cust.tableNumber);
					cust.state=AgentState.leaving;
					stateChanged();
					return;
				}
			}
		}catch(ConcurrentModificationException e){
			System.out.println("Concurrent Modification Error.");
			return;
		}
	}
	
	



	
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		if(onBreak){
			onBreak=false;
			goBreak();
			return true;
		}
		
		if(timeToLeave && customers.size()==0){
			timeToLeave = false;
			leaveWork();
			return true;
		}
		
		try{
			for(MyCustomer cust:customers){
				
				if(cust.state==AgentState.waiting){
					cust.state=AgentState.noAction;
					seatCustomer(cust);
					return true;
				}
				else if(cust.state==AgentState.givingChoice){
					cust.state=AgentState.noAction;
					giveOrderToCook(cust);
					return true;
				}
				else if(cust.state==AgentState.readyToOrder){
					cust.state=AgentState.noAction;
					takeOrder(cust);
					return true;
				}
				else if(cust.state==AgentState.beingToldNoFood){
					cust.state=AgentState.noAction;
					takeOrderAgain(cust.customer,cust.tableNumber);
					return true;
				}
				else if(cust.state==AgentState.beingServed){
					cust.state=AgentState.noAction;
					sendFoodToCustomer(cust);
					return true;
				}
				else if (cust.state==AgentState.askingCheck){
					cust.state=AgentState.noAction;
					informCashier(cust);
					return true;
				}
				else if (cust.state == AgentState.receivingCheck){
					cust.state=AgentState.noAction;
					giveCustomerCheck(cust);
					return true;
				}
				else if(cust.state==AgentState.leaving){
					
					cust.state=AgentState.noAction;
					customers.remove(cust);
					tellHostTableClear(cust.tableNumber);
					return true;
				}
			}
		}catch(ConcurrentModificationException e){
			System.out.println("We come across concurrent Modification Error in the scheduler.");
			return false;
		}
		return false;
	}
    
	
	
	
	
	//Actions
	
	private void giveCustomerCheck(MyCustomer c) {
		// TODO Auto-generated method stub
		//print("Giving "+c.customer+" the check");
		
		waiterGui.setTablePosition(c.tableNumber);
		waiterGui.DoGoToTable();
		try{
			atTable.acquire();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		c.customer.msgHereIsCheck(currentCheck);
		waiterGui.DoGoToStand();
	}



	private void informCashier(MyCustomer cust) {
		// TODO Auto-generated method stub
		//print("Tell cashier to compute bill");
		waiterGui.DoGoToCashier();
		try{
			atCashier.acquire();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		cashier.msgComputeBill(this,choiceForCheck,cust.tableNumber);
	}







	private void goBreak() {
		// TODO Auto-generated method stub
		//host.msgOffBreak(this);
		waiterGui.setOnBreak(true);
		waiterGui.setApplying(false);
	}



	private void takeOrderAgain(Customer customer,int t) {
		// TODO Auto-generated method stub
		DoTakeOrderAgain(t);
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		waiterGui.DoGoToStand();
		customer.msgTellMeYourNewChoice(new Menu(deletedChoice),this);
		deletedChoice=null;
		
	}



	private void DoTakeOrderAgain(int t) {
		// TODO Auto-generated method stub
		//print("Sorry! "+deletedChoice+" has run out!");
		waiterGui.setTablePosition(t);
		waiterGui.DoGoToTable();
	}



	private void seatCustomer(MyCustomer c) {
		DoPickUpCustomer(c.customer);
		c.customer.msgFollowMe(new Menu(),this,c.tableNumber);
		DoSeatCustomer(c.customer, c.tableNumber);
		waiterGui.DoGoToStand();
	}
	
	private void DoPickUpCustomer(Customer customer) {
		// TODO Auto-generated method stub
		//print("Going to the door to pick up "+customer);
		waiterGui.DoGoToDoor();
		//print("The permits of atDoor is "+atDoor.availablePermits());
		try {
			atDoor.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	// The animation DoXYZ() routines
	private void DoSeatCustomer(Customer customer, int table) {
			//Notice how we print "customer" directly. It's toString method will do it.
			//Same with "table"
			//print("Seating " + customer + " at Table " + table);
			waiterGui.setTablePosition(table);
			waiterGui.DoGoToTable(); 
			//print("The permits of atTable is "+atTable.availablePermits());
			try {
				atTable.acquire();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	
	private void takeOrder(MyCustomer c) {
		c.customer.msgTellMeYourChoice(this);
		
		DoTakeOrder(c.customer,c.tableNumber);
		//print("The permits of atTable is "+atTable.availablePermits());
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

    
	private void DoTakeOrder(Customer customer,int t) {
		// TODO Auto-generated method stub
		//print("coming to table "+t+" again");
		waiterGui.setTablePosition(t);
		waiterGui.DoGoToTable();
	}
	
	
	private void giveOrderToCook(MyCustomer cust) {
		// TODO Auto-generated method stub
		//print("Going to cook");
		waiterGui.DoGoToCook();
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cook.orders.insert(this,foodToOrder,cust.tableNumber);
		//cook.msgHereIsAnOrder(this,foodToOrder,cust.tableNumber);
		DoFinishGiveOrderToCook();
	}

	
	

	private void DoFinishGiveOrderToCook() {
		// TODO Auto-generated method stub
		//print("Finish giving order to cook");
		waiterGui.DoGoToStand();
	}
	
	private void sendFoodToCustomer(MyCustomer c) {
		// TODO Auto-generated method stub
		
		waiterGui.DoGoToCook();
		//print("The permits of atCook is "+atCook.availablePermits());
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cook.msgPickUp(this);
		
		DoSendFoodToCustomer(c.customer,c.tableNumber);
		//print("The permits of atTable is "+atTable.availablePermits());
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		c.customer.msgHereIsYourOrder(foodToBeSent);
		waiterGui.doneServing();
		waiterGui.DoGoToStand();
		
	}



	private void DoSendFoodToCustomer(Customer customer,int t) {
		// TODO Auto-generated method stub
		//print("Sending food to cunstomer at Table "+t);
		waiterGui.sendGood(foodToBeSent);
		waiterGui.setTablePosition(t);
		waiterGui.DoGoToTable();
	}
	
	
	private void tellHostTableClear(int table) {
		// TODO Auto-generated method stub
		host.msgTableIsClear(table);
		//print("Table "+table+" is clear");
	}


    
	
	
	//utilities
	/* (non-Javadoc)
	 * @see restaurant.Waiter#setGui(restaurant.gui.WaiterGui)
	 */
	@Override
	public void setGui(WaiterGui gui) {
		waiterGui = gui;
	}
    
	/* (non-Javadoc)
	 * @see restaurant.Waiter#getGui()
	 */
	@Override
	public WaiterGui getGui() {
		return waiterGui;
	}
    
	private class MyCustomer {
		private Customer customer;
		private int tableNumber;
		
		private AgentState state;
		
		MyCustomer(Customer customerAgent, int num){
			customer=customerAgent;
			tableNumber=num;
			state=AgentState.waiting;
		}
				
		
	}


	
	static public class Menu{
		private List<Price> choices=new ArrayList<Price>();
		
		Menu(){
			choices.add(new Price("Steak",15.99));
			choices.add(new Price("Chicken",10.99));
			choices.add(new Price("Salad",5.99));
			choices.add(new Price("Pizza",8.99));
		}
		
		Menu(String deletedChoice){
			choices.add(new Price("Steak",15.99));
			choices.add(new Price("Chicken",10.99));
			choices.add(new Price("Salad",5.99));
			choices.add(new Price("Pizza",8.99));
			
			for(Price p:choices){
				if(p.choice.equals(deletedChoice)){
					choices.remove(p);
					return;
				}
			}
			
		}
		
		
		public String getChoice(double cash){
			int number;
			while(true){
				number=(int)(Math.random()*choices.size());
				
				if(choices.get(number).cost<=cash){
					break;
				}
			}
			return choices.get(number).choice;
		}
		
		public double getMinCost(){
			double temp=15.99;
			for(Price p:choices){
				if(p.cost<temp){
					temp=p.cost;
				}
			}
			return temp;	
		}
		
		
		
		class Price{
			private String choice;
			private double cost;
			
			Price(String choice,double cost){
				this.choice=choice;
				this.cost=cost;
			}
		}
		
		
		

	}




	/* (non-Javadoc)
	 * @see restaurant.Waiter#wantToGoOnBreak()
	 */
	@Override
	public void wantToGoOnBreak(){
		host.msgGoOnBreakPlease(this);
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Waiter#getName()
	 */
	@Override
	public String getName(){
		return name;
	}

	public String toString(){
		return name;
	}

	/* (non-Javadoc)
	 * @see restaurant.Waiter#comeBackToWork()
	 */
	@Override
	public void comeBackToWork() {
		// TODO Auto-generated method stub
		host.msgIAmBack(this);
	}



	



	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgReadyForCheck(restaurant.interfaces.Customer, java.lang.String)
	 */
	@Override
	public void msgReadyForCheck(Customer customerAgent,String choice) {
		try{
		for(MyCustomer c:customers){
			if(c.customer==customerAgent){
				c.state=AgentState.askingCheck;
				choiceForCheck=choice;
				stateChanged();
				return;
			}
		}
		}catch(ConcurrentModificationException e){
			System.out.println("Concurrent Modification Error.");
		}
	}



	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgCheckReady(double, int)
	 */
	@Override
	public void msgCheckReady(double check, int table) {
		try{
		for(MyCustomer c:customers){
			if(c.tableNumber==table){
				c.state=AgentState.receivingCheck;
				currentCheck=check;
				stateChanged();
				return;
			}
		}
		}catch(ConcurrentModificationException e){
			System.out.println("Concurrent Modification Error.");
		}
	}



	/* (non-Javadoc)
	 * @see restaurant.Waiter#msgAtCashier()
	 */
	@Override
	public void msgAtCashier() {
		// TODO Auto-generated method stub
		atCashier.release();
	}



	public void setHost(RanchoHost host2) {
		host=host2;
		
	}
	public void setCook(RanchoCook cook2) {
		cook=cook2;
	}
	public void setCashier(RanchoCashier cashier2) {
		cashier=cashier2;
	}

	
	
	
	
	
	
	
	
	
	// interact with manager

	public void msgYouCanLeave() {
		// TODO Auto-generated method stub
		timeToLeave = true;
		System.out.println("4");
		stateChanged();
	}

	private void leaveWork(){
		waiterGui.DoGoToManager();
		try {
			atManager.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		manager.msgImLeaving(this);
		waiterGui.exitRestaurant();
	}

	public void msgAtManager(){
		atManager.release();
	}



	public void msgHereIsSalary(double amount) {
		// TODO Auto-generated method stub
		myPerson.getWealth().setCash(myPerson.getWealth().getCash()+amount);
		System.out.println(this+" received salary "+amount+" dollars.");
	}



	public void setManager(RanchoManager manager) {
		// TODO Auto-generated method stub
		this.manager = manager;
	}
	
	
}

