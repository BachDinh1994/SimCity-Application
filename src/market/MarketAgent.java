package market;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import market.gui.MarketGui;
import market.interfaces.Market;
import restaurant.CashierAgent;
import restaurant.CookAgent;
import SimCity.myContact;
import agent.Agent;
/**
 * Restaurant Market Agent
 */
public class MarketAgent extends Agent implements Market//Complete implementation
{
	String name;
	CashierAgent cashier;
	CookAgent cook;
	MarketCustomerAgent customer;
	//ResidentAgent resident;
	DeliveryTruckAgent myDeliveryTruck;
	MarketEmployeeAgent myEmployee;
	MarketGui myGui;
	Timer timer = new Timer();
	int cash = 1000;

	//all the past customers with debt
	private Map<Object, Double> pastCustDebt = new HashMap<Object, Double>();

	public List<MOrder> orders = Collections.synchronizedList(new ArrayList<MOrder>());
	public  Map<String,Integer> inv = new HashMap<String,Integer>();
	public Map<String, Double> cost = new HashMap<String, Double>();
	{
		cost.put("Pork", 13.99); 
		cost.put("Chicken", 10.99);
		cost.put("Fogra", 9.99);
		cost.put("Fried Rice", 8.99);
		cost.put("Steak", 20.99);
		cost.put("Beef Tenderloin", 32.99);

	}
	public List<dCheck> Bill = new ArrayList<dCheck>();

	
	public class spot{
		public int x;
		public int y;
		public boolean available =true;
		public spot(int xloc, int yloc){
			x = xloc;
			y = yloc;
			available = true;
		}
	}
	public List<spot> Line = new ArrayList<spot>();

	private void createLine(){
		Line.add(new spot(100, 135));
		Line.add(new spot(50, 135));
		Line.add(new spot(50, 165));
		Line.add(new spot(50, 195));
		Line.add(new spot(50, 225));
		Line.add(new spot(50, 255));
		Line.add(new spot(50, 285));
	}
	
	enum checkState {none,newCheck, paid, hasDebt, done};    
	enum stopstate{none,stopping}
	enum orderState{neworder,processing, processed,readyForDelivery, delivered,done,marketempty}

	stopstate stop = stopstate.none;

	public class dCheck{
		public Map<String, Integer> order;
		public String choice;
		public Double amtDue;
		public DeliveryTruckAgent deliverer;
		public myContact custContact;
	}

	public class MOrder 
	{
		public Map<String, Integer> order;
		public String choice;
		myContact custContact;

		public orderState oState;
		public checkState cState;

		//check that is created for the customer
		public dCheck custCheck;

		public MOrder(Map<String,Integer> order2,String choice2, myContact info)
		{
			oState = orderState.neworder;
			cState = checkState.none;
			order = order2;
			choice = choice2;
			custContact = info;
		}
	}

    
	public void createInventory(int j){
		inv.put("Pork", j); 
		inv.put("Chicken", j);
		inv.put("Fogra", j);
		inv.put("Fried Rice", j);
		inv.put("Steak", j);
		inv.put("Beef Tenderloin", j);
	}



	public MarketAgent(String name) 
	{
		super();
		this.name = name;
		createInventory(20);
		createLine();
	}

	// Messages

	public void msgCallOrder(Map<String,Integer> supply, String choice, myContact orderInfo)
	{
		print ("Received order from: " + orderInfo.myType);
		orders.add(new MOrder(supply,choice, orderInfo));
		stateChanged();
	}

	public void msgHereIsBill(Double amtPaid, dCheck custCheck){
		//received transaction from the delivery
		MOrder currentO = null;
		for(MOrder o: orders){
		
			if(o.custCheck.equals(custCheck)){
				currentO = o;
				//same check
			}
		}
		Do("   check: " + custCheck.choice);
		if (amtPaid<custCheck.amtDue){
			Do("they owe us money");
			//the amount paid is less than the amt Due
			//they owe us money!!!
			if(pastCustDebt.containsKey(currentO.custContact.myIdentity)){
				//if this is a past customer who had a debt before add interest 10%
				Double debt = pastCustDebt.get(currentO.custContact.myIdentity) + 
						(custCheck.amtDue - amtPaid);
				pastCustDebt.put(currentO.custContact.myIdentity, debt*1.1 );
				currentO.cState = checkState.hasDebt;
			}
			else
			{
				//add into the list with no interest penualty
				pastCustDebt.put(currentO.custContact.myIdentity, custCheck.amtDue - amtPaid);
			}
		}
		else{
			Do("payment received. Updating the log");
			///they paid, update the log
			if(pastCustDebt.containsKey(currentO.custContact.myIdentity)){
				//if existed before 
				pastCustDebt.remove(currentO.custContact.myIdentity);
			}
			currentO.oState = orderState.done;
			currentO.cState = checkState.done;
		}
	}


	public void msgPleaseStop(){
		stop = stopstate.stopping;
		stateChanged();
	}

	public void msgRetrievalComplete(Object cust) {
		// TODO Auto-generated method stub
		Do("ready for delivery");
		myDeliveryTruck.myGui.msgCreatePackage();
		for (MOrder o: orders)
		{
			if(o.custContact.myIdentity.equals(cust)){
				o.oState = orderState.readyForDelivery;
				stateChanged();
			}
		}
	}
	/**
	 * Scheduler. Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() 
	{
		if (stop == stopstate.stopping)
		{
			stop();
		}

		synchronized(orders)
		{
			for (MOrder o : orders) 
			{
				if (o.oState == orderState.neworder) 
				{
					o.oState = orderState.processed;
					processOrder(o);
					return true;
				}

				if (o.oState == orderState.readyForDelivery)
				{
					o.oState = orderState.processing;
					sendDelivery(o);
					return true;
				}
				if (o.oState == orderState.done)
				{
					if(o.cState == checkState.done){
						Do("order complete");
						orders.remove(o);
						return true;
					}
					else if(o.cState == checkState.hasDebt){
						warnBoss(o.custContact);
						orders.remove(o);
						return true;
					}
				}
			}
		}
		return false;
	}

	// Actions
	public void stop()
	{
		stop = stopstate.none;
		try
		{
			getThread().pauseS.acquire();
			getThread().pause();
		}
		catch (Exception st) 
		{
			st.printStackTrace();
		}
	}
	private void warnBoss(myContact custContact)
	{
		print ("call Cashier that interest is added");
		if(custContact.myBoss instanceof CashierAgent)
		{
			cashier = (CashierAgent) custContact.myBoss;
			cashier.msgPayASAP();
		}
		//else if(custContact.myBoss instanceof ResidentAgent)
		else{
			customer = (MarketCustomerAgent) custContact.myIdentity;
			customer.msgPayASAP();
		}
	}

	private void processOrder(MOrder o)
	{

		boolean fulfilled; //tells the customer that the order was fully fulfilled or not
		int remaining = 0; //remaining in the inventory
		//amount it couldn't fulfill
		String customerType = o.custContact.myType;
		if(o.custContact.myIdentity instanceof CookAgent)
		{
			cook = (CookAgent) o.custContact.myIdentity;
		}
		else if(o.custContact.myIdentity instanceof MarketCustomerAgent){
			customer = (MarketCustomerAgent) o.custContact.myIdentity;
		}
		else
		{
			//resident = (ResidentAgent) o.custContact.myIdentity;
		}

		//first check if they have it
		System.out.println(" choice:           " + o.choice );
		if (inv.get(o.choice) == 0)
		{
			//don't have it, don't expect a delivery

			if(customerType.equals("Cook")){
				cook.msgOutOfStock(o.choice);
			}
			else if(customerType.equals("Customer")){
				customer.msgOutOfStock(o.choice);
			}
			else{
				//resident
			}


			orders.remove(o);

		}
		else 
		{
			if (o.order.get(o.choice) <= inv.get(o.choice))
			{
				//can be fullfilled
				fulfilled = true;
				print ("MANAGED TO FULFILL EVERYTHING");
				remaining  = inv.get(o.choice) - o.order.get(o.choice);
			}
			else //has some in the inventory but can't fulfill it
			{
				fulfilled = false;
				remaining = 0;
				o.order.put(o.choice, inv.get(o.choice)); //change the order what it can fulfill
			}
		
		o.oState = orderState.processed;
		inv.put(o.choice, remaining);
		
		if(customerType.equals("Cook")){	
			cook.msgHereIsQuote(o.order, o.choice,fulfilled);
		}
		else if(customerType.equals("Customer")){
			customer.msgHereIsQuote(o.order, o.choice,fulfilled);
		}
		else{
				//resident
		}

		print("tell my employee to get these items");
		myEmployee.msgGetThis(o.order, o.choice, o.custContact.myIdentity);
		}

	}

	private void sendDelivery(final MOrder o)
	{
		//create the bill
		createBill(o);
		if(o.custContact.myIdentity instanceof MarketCustomerAgent){
			//no need for delivery then
			Do("Here is your item");
			myGui.msgGiveItem(o.choice);
			customer.msgHereIsOrder(o.custCheck);
			o.oState = orderState.done;
			
		}
		else{
			Do("sending delivery");
			//delivery
			myGui.msgWrapIntoDelivery();
			myDeliveryTruck.msgDeliverThis(o.custCheck);
			o.oState = orderState.delivered;
		}

	}

	private void createBill(MOrder o){
		dCheck custCheck = new dCheck();
		o.cState = checkState.newCheck;
		custCheck.choice = o.choice;
		custCheck.order = o.order;
		custCheck.custContact = o.custContact;
		String choice = custCheck.choice;
		custCheck.amtDue = cost.get(choice);

		custCheck.deliverer =  myDeliveryTruck;
		if(pastCustDebt.containsKey(o.custContact.myIdentity)){
			//if past customer with a debt
			custCheck.amtDue = custCheck.amtDue + pastCustDebt.get(o.custContact.myIdentity);
		}
		o.custCheck = custCheck;
	}

	public String getName() 
	{
		return name;
	}

	public void setGui(MarketGui g){
		myGui = g;
	}
	public MarketGui getGui(){
		return myGui;
	}
	public void setMyEmployee(MarketEmployeeAgent employee){
		myEmployee = employee;
	}
	public void setMyDeliveryTruck(DeliveryTruckAgent truck){
		myDeliveryTruck = truck;
	}












}