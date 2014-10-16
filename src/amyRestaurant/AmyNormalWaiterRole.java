package amyRestaurant;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Semaphore;

import agent.Agent;
import amyRestaurant.AmyWaiterRole.foodPrice;
import amyRestaurant.gui.AmyWaiterGui;
import amyRestaurant.interfaces.AmyCashier;
import amyRestaurant.interfaces.AmyCook;
import amyRestaurant.interfaces.AmyCustomer;
import amyRestaurant.interfaces.AmyWaiter;
import Role.Role;

/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class AmyNormalWaiterRole extends Role implements AmyWaiter {



	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	private Integer totalCustomer = 0;
	private String name;

	//semaphores help with no msgs being overwritten
	private Semaphore atTable = new Semaphore(0,true); 
	private Semaphore atCook = new Semaphore(0,true);
	private Semaphore atHome = new Semaphore(0,true);
	private Semaphore atOutside = new Semaphore(0, true);
	private Semaphore atCashier = new Semaphore(0, true);
	
	Timer breakTime = new Timer();
	private boolean busy = false; //for the host to know when this waiter can be called
	int tableLocation;


	private AmyHostAgent host;
	private AmyCashier amyCashier;
	private AmyCustomer currentCustomer; //the current customer that waiter is attending
	private AmyCook amyCook;
	public AmyWaiterGui amyWaiterGui;
	
	private List<foodPrice> Menu = new ArrayList<foodPrice>();

	public enum CustomerState
	{WaitingToSeat, RequestMenu,CallingToOrder, needToReOrder, GetCheck,Leaving, BeingAttended};
	public class myCustomer{
		AmyCustomer cust;
		int tableNum;
		CustomerState cState;
	}
	
	public enum checkState
	{New, Given};
	public class custCheck{
		AmyCustomer cust;
		String food;
		double money;
		int table;
		checkState cState = checkState.New;
		
		
		custCheck(AmyCustomer c, String f, int t, Double m){
			cust = c;
			food = f;
			table = t;
			money = m;
			cState = checkState.New;
		}
	}
	public List<custCheck> checksToGive = new ArrayList<custCheck>();
	
	private enum BreakState
	{None,Pending,Waiting,Granted,Rejected,Outside,ComeBack};

	private BreakState waiterState = BreakState.None;



	public List<myCustomer> MyCustomers = new ArrayList<myCustomer>();//this is unique to each waiter and should pop it after the order is sent to the cook
	public List<Order> Orders = new ArrayList<Order>();


	private Hashtable<String, Double> menuPrice = new Hashtable<String, Double>();
	{menuPrice.put("Chicken", 10.99 );
	menuPrice.put("Steak", 15.99);
	menuPrice.put("Salad", 5.99);
	menuPrice.put("Pizza",8.99);}
	
	public enum OrderState
	{None, CreatedOrder, AtCook,Done}
	public class Order{
		String choice = "nothing";
		OrderState oState= OrderState.None;
		AmyCustomer cust;
		int tableNum;
	}
	
	

	public AmyNormalWaiterRole(String name) {
		super();
		//set menu
		Menu.add(new foodPrice("Chicken", 10.99 ));
		Menu.add(new foodPrice("Steak", 15.99));
		Menu.add(new foodPrice("Salad", 5.99));
		Menu.add(new foodPrice("Pizza",8.99));
		this.name = name;

	}

	//messages

	public void msgSitCustomer(AmyCustomer cust, int tableNumber){
		
		setCustomer(cust);
		addNewCustomer(tableNumber);
		stateChanged();
	}

	public void msgWantToGoToBreak(){
		//from gui
		waiterState = BreakState.Pending;
		stateChanged();
	}
	
	public void msgWantToReturn(){
		waiterState = BreakState.ComeBack;
		stateChanged();
	}

	public void msgBreakRequestAnswer(boolean answer){
		if (answer){ //request a
			waiterState = BreakState.Granted;
			stateChanged();
		}
		else //request denied
		{
			waiterState = BreakState.Rejected;
			stateChanged();

		}
	}

    public void msgRestocked(String food){ //from cook
    	boolean update = true;
    	for(foodPrice menuItem: Menu){
    		if(menuItem.food.equals(food)){
    			update = false;
    		}
    	}
    	
    	if(update){
    		Menu.add(new foodPrice(food, menuPrice.get(food)));
    	}
    }

	public void msgReadyToOrder(AmyCustomer amyCustomer){
		for(myCustomer currC:MyCustomers)
		{
			if(currC.cust == amyCustomer) // should be the same customer that you just seated
			{
				currC.cState = CustomerState.CallingToOrder;
				stateChanged();
				break;
			}
		}


	}

	public void msgGiveOrder(AmyCustomer amyCustomer, String choice){
		//add new order to queue
		//set the information for the class order
		addNewOrder(amyCustomer,choice);
		//once its added into order list, customer can have new waiters serving food
		// so pop the customer from the MyCustomers list
		//perhaps

		stateChanged();

	}

	public void msgFoodIsReady(String foodReady, int tableNum)
	{
		for(Order request:Orders)
		{
			if(request.choice == foodReady && request.tableNum == tableNum)
			{
				request.oState = OrderState.Done;
				break;
			}
		}
		stateChanged();

	}
	
	public void msgAskForCheck(AmyCustomer cust){
		//from customer
		
		for (myCustomer checkCust: MyCustomers){
			if(checkCust.cust == cust){
				checkCust.cState = CustomerState.GetCheck;
				stateChanged();
			}
		}
	}

	public void msgHereIsCheck(AmyCustomer cust, String food, Double money, int table ){
		//go to cashier
		checksToGive.add(new custCheck(cust, food, table, money));
		stateChanged();
		
	}
	
	public void msgLeavingTable(AmyCustomer cust) {
		for (myCustomer leavingCust: MyCustomers)
		{
			if(cust.equals(leavingCust.cust)){
			leavingCust.cState = CustomerState.Leaving;}
		}
		stateChanged();

	}

	public void msgAtTable() {//from animation
		atTable.release();
		stateChanged();
	}

	public void msgAtCook(){
		atCook.release();
		stateChanged();
	}
	
	public void msgAtCashier(){
		atCashier.release();
		stateChanged();
	}
	
	public void msgAtOutside(){
		atOutside.release();
		stateChanged();
	}
	
	public void msgAtHome(){
		atHome.release();
		stateChanged();
	}


	public void msgOutOfFood(String choice, int tableNum) {
		// from cook
		//updateMenu
		for(foodPrice removeItem: Menu){
			if(removeItem.food.equals(choice)){
				Menu.remove(removeItem);
			}
		}
		
		
		for (myCustomer reOrder: MyCustomers){
			if (reOrder.tableNum == tableNum)
			{
				reOrder.cState = CustomerState.needToReOrder;
				stateChanged();
			}
		}

	}




	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public  boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		if (waiterState == BreakState.Pending){
			waiterState = BreakState.Waiting;
			requestBreak();
			return true;
		}

		if (waiterState == BreakState.Rejected){
			waiterState = BreakState.None;
			rejectBreak();
			return true;
		}
		
		if (waiterState == BreakState.ComeBack){
			waiterState = BreakState.None;
			returnWork();
			return true;
		}

		if (waiterState == BreakState.Granted){
			if(totalCustomer == MyCustomers.size()){
				System.out.println("go to break");
				goToBreak();
				return true;
			}
			if (MyCustomers.isEmpty()){
				System.out.println("no customers");
				goToBreak();
				return true;
			}
		}
	


		for(myCustomer customer: MyCustomers)
		{
			if(customer.cState == CustomerState.WaitingToSeat)
			{
				customer.cState = CustomerState.BeingAttended;
				seatCustomer(customer.cust,customer.tableNum);
				return true;
			}
		}


		for(myCustomer customer: MyCustomers)
		{
			if(customer.cState == CustomerState.CallingToOrder)
			{
				customer.cState = CustomerState.BeingAttended;
				getOrder(customer.cust);
				return true;
			}
		}
		for(myCustomer customer: MyCustomers)
		{
			if(customer.cState == CustomerState.needToReOrder)
			{
				customer.cState = CustomerState.BeingAttended;
				reOrder(customer.cust,customer.tableNum);
				return true;
			}
		}
		for(Order request:Orders)
		{
			if(request.oState == OrderState.CreatedOrder)
			{
				HereIsOrder(request.choice, request.tableNum);
				request.oState = OrderState.AtCook;
				return true;
			}
		}

		for(Order request:Orders)
		{
			if(request.oState == OrderState.Done)
			{
				request.oState = OrderState.None;
				PickUpFood(request);
				return true;
			}
		}
		
		for(myCustomer checkCust: MyCustomers)
		{
			if(checkCust.cState == CustomerState.GetCheck)
			{
				checkCust.cState = CustomerState.BeingAttended;
				for(Order request:Orders){
					if(request.cust == checkCust.cust){
						tellCashier(request.cust, request.choice, request.tableNum);
						return true;
					}
					
				}
				
			}
		}
		
		for(custCheck cCheck: checksToGive){
			if (cCheck.cState == checkState.New){
				cCheck.cState = checkState.Given;
				giveCheck(cCheck);
				return true;
			}
		}

		for(myCustomer leaving:MyCustomers)
		{
			if(leaving.cState == CustomerState.Leaving)
			{
				leaving.cState = CustomerState.BeingAttended;
				totalCustomer++;
				System.out.println("tellin host " + leaving.cust.getName() + " is leaving");
				tellHost(leaving.cust);
				return true;
			}
		}



		return false;

	}

	// Actions

	public void seatCustomer(AmyCustomer amyCustomer, int tableNumber) {

		amyCustomer.msgFollowMe(this);
		System.out.println("Seating " + amyCustomer + " at " + tableNumber);

		amyWaiterGui.DoPickCustomer((AmyCustomerRole) amyCustomer);
		try {
			atHome.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		amyWaiterGui.DoBringToTable((AmyCustomerRole)amyCustomer, tableNumber);

		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		amyCustomer.msgHereIsMenu(Menu);
		amyWaiterGui.DoLeaveCustomer();

	}



	private void goToBreak(){
		//tell gui to go to Break
		
       System.out.println("going to break");
		waiterState = BreakState.Outside;
		
		amyWaiterGui.doGoToBreak();
		try {
			atOutside.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
	
	private void returnWork(){
		System.out.println("come back");
		amyWaiterGui.doReturnWork();
		host.msgBackFromBreak(this);
		
	}

	private void getOrder(AmyCustomer amyCustomer)
	{
		//get order 
		//send message to customer to give order
		for(myCustomer currC:MyCustomers)
		{
			if (currC.cust == amyCustomer)
			{
				System.out.println("waiter " + this.getName() + " gonna take order for customer " + amyCustomer.getName());
				amyWaiterGui.goToTable(currC.tableNum); //go to table
				break;
			}
		}
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		amyCustomer.msgWhatIsOrder();

	}

	public void reOrder(AmyCustomer amyCustomer,Integer tableNumber){

		amyWaiterGui.goToTable(tableNumber);
		try {

			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Sorry,please reorder");
		amyCustomer.msgHereIsUpdatedMenu(Menu);
	}


	public void HereIsOrder(String orderChoice, int tableNumber)
	{
		System.out.println("going to cook with order");
		amyWaiterGui.DoGoToCook();
		try {

			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		amyCook.msgHereIsOrder(orderChoice, tableNumber, this);
		amyWaiterGui.DoLeaveCustomer();
		
		if(waiterState == BreakState.None){
			host.msgNotBusy(this);// tells host not busy any more
			}

	}

	private void PickUpFood(Order foodToDeliver)
	{

		amyWaiterGui.DoPickUpFood(foodToDeliver.choice);
		try {
			atCook.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(this.name + " waiter is picking up food for table " + foodToDeliver.choice);
		this.setCustomer(foodToDeliver.cust);
		giveFood(foodToDeliver);


	}

	private void giveFood(Order food)
	{
		amyWaiterGui.createCustomerFood(food.choice);
		amyWaiterGui.goToTable(food.tableNum);
		try {

			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		currentCustomer.msgHereIsFood();
		System.out.println("Here is your " +food.choice + ", enjoy.");
		
		amyWaiterGui.createCustomerFood("");
		amyWaiterGui.DoLeaveCustomer();
		
	}
	
	private void tellCashier(AmyCustomer c, String choice, int t ){
		//tell cashier to create check
		//go to cashier
		for (int done = 0 ; done <Orders.size(); done ++)
		{
			if(Orders.get(done).cust == c)
			{
				Orders.remove(done);
			}
		}//remove order
		
		amyWaiterGui.goToCashier();
		System.out.println("go to cashier");
		try {

			atCashier.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		amyCashier.msgCreateCheck(choice, t, this, c);
		amyWaiterGui.DoLeaveCustomer();
		System.out.println("leaving cashier");
		amyWaiterGui.createCustomerFood("Check");
		//return back to station
	}
	
	private void giveCheck(custCheck check){
		//go to customer
		amyWaiterGui.goToTable(check.table);
		try {

			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//tell customer 
		
		check.cust.msgHereIsCheck(check.money);
		System.out.println("here is your check " + check.cust.getName());
		amyWaiterGui.createCustomerFood("None");
		amyWaiterGui.DoLeaveCustomer();
	}
	
	private void tellHost(AmyCustomer cust){
		amyWaiterGui.DoLeaveCustomer();
		System.out.println("I'm in tell host");
		host.msgCustomerLeft((AmyCustomerRole) cust, this);
		
	}
	
	private void requestBreak(){
		host.msgAskHostBreak(this); //ask host
		this.getGui().breakStatus("Pending");
		
	}
	private void rejectBreak(){
		this.getGui().breakStatus("Rejected");
		
	}
	



	//utilities

	public boolean availability(){
		return busy;
	}

	public void setBusy(){
		busy = true;
	}
	
	public void setCashier(AmyCashierAgent cashier) {
		this.amyCashier = cashier;
	}

	public void setHost(AmyHostAgent host) {
		this.host = host;
	}

	public void setCook (AmyCookAgent cook){
		this.amyCook  = cook;
	}

	public void setCustomer(AmyCustomer amyCustomer){
		this.currentCustomer = amyCustomer;
	}

	public String getName() {
		return name;
	}



	public void addNewOrder(AmyCustomer cust, String choice)
	{
		Order newOrder = new Order();
		newOrder.cust = cust;
		newOrder.choice = choice;
		newOrder.oState = OrderState.CreatedOrder;
		for(myCustomer currC:MyCustomers)
		{
			if (currC.cust == cust)
			{
				newOrder.tableNum = currC.tableNum;
				break;
			}
		}

		Orders.add(newOrder);
	}

	public void addNewCustomer(int tableNumber)
	{

		myCustomer newCustomer = new myCustomer();
		newCustomer.cust = currentCustomer;
		newCustomer.tableNum = tableNumber;
		newCustomer.cState = CustomerState.WaitingToSeat;
		MyCustomers.add(newCustomer);
	}

	public void setGui(AmyWaiterGui gui) {
		amyWaiterGui = gui;
	}

	public AmyWaiterGui getGui() {
		return amyWaiterGui;
	}

	



	

}


