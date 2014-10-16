package restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import market.MarketAgent;
import market.MarketAgent.dCheck;
import restaurant.gui.CookGui;
import restaurant.gui.FoodGui;
import restaurant.interfaces.Waiters;
import SimCity.Address;
import SimCity.myContact;
import agent.Agent;

/**
 * Restaurant Cook Agent
 */
public class CookAgent extends Agent //Complete implementation
{
	String choices[] = new String[] {"Pork","Chicken","Fogra","Fried Rice","Steak","Beef Tenderloin"};
	CookGui cookGui;
	String name;
	Timer timer = new Timer();
	MarketAgent market;
	int nextMarket=0;
	myContact cookContact;
	CashierAgent myManager;
	
	//all the markets//
	public Vector<MarketAgent> markets;
	
	//producer-consumer monitor//
    public WaiterCookMonitor orders = new WaiterCookMonitor(this);
    
    //normal orders//
    public List<Order> Orders = new ArrayList<Order>();
    
    //check to give to cashier
  	public List<dCheck> checksForCashier = new ArrayList<dCheck>();
  	
  	///mapping the food to a Food class s
  	public Map<String,Food> foods = new HashMap<String,Food>();
    
  	//partial delivery
  	public List<PartialDelivery> partialdeliveries = Collections.synchronizedList(new ArrayList<PartialDelivery>());
   
  	enum deliverystate{none,fulfilling,waitingDelivery,fulfilled};
	enum state{none,pending,cooking,done};
    enum stopstate{none,stopping};
	
    stopstate stop = stopstate.none;
    
    public class PartialDelivery
	{
		MarketAgent market;
		String choice;
		int amountleft;
		int otherMarket;
		deliverystate s = deliverystate.fulfilling;
		public PartialDelivery(MarketAgent market2, String choice2, int amount2)
		{
			market = market2;
			choice = choice2;
			amountleft = amount2;
		}
	}
	
    static public class Order
	{
		Waiters w;
		String choice;
		int table;
		FoodGui foodGui;
		state s;
		public Order(Waiters w2, String choice2, int table2, FoodGui foodGui2) 
		{
			w=w2;
			choice=choice2;
			table=table2;
			foodGui = foodGui2;
			s = state.pending;
		}
	}
	public class Food
	{
		String type;
		int time = 3;
		int amount= 3; //how much it has right now
		
		int threshold = 3; //low threshold when it should order
		int maxAmount = 10; // amount of a fully stocked inventory
		int waitingAmount = 0;// amount waiting from delivery
		int amtOrdered = 0; //amount ordered from market
		
		/*added deliverystate to food as well. because we don't want the cook to 
		reorder food that is being delivered soon*/
		deliverystate foodDeliveredState = deliverystate.none;
		public Food(String type2, int time2, int amount2) 
		{
			type = type2;
			time = time2;
			amount = amount2;
			waitingAmount = 0;
			maxAmount =10; 
			amtOrdered = 0;
			foodDeliveredState = deliverystate.none;
		}
	}
	public CookAgent(String name) 
	{
		super();
		this.name = name;
		foods.put("Pork", new Food("Pork",4500,2)); 
		foods.put("Chicken", new Food("Chicken",4500,2));
		foods.put("Fogra", new Food("Fogra",4500,2));
		foods.put("Fried Rice", new Food("Fried Rice",4500,2));
		foods.put("Steak", new Food("Steak",5000,2));
		foods.put("Beef Tenderloin", new Food("Beef Tenderloin",5500,2));
	}
	
	public void createFood( int j){
		foods.put("Pork", new Food("Pork",4500,j)); 
		foods.put("Chicken", new Food("Chicken",4500,j));
		foods.put("Fogra", new Food("Fogra",4500,j));
		foods.put("Fried Rice", new Food("Fried Rice",4500,j));
		foods.put("Steak", new Food("Steak",5000,j));
		foods.put("Beef Tenderloin", new Food("Beef Tenderloin",5500,j));
	}
	public void msgHereIsOrder(Waiters w,String choice,int table,FoodGui foodGui) 
	{
		System.out.println("Cook Received message for an order from waiter");
		Orders.add(new Order(w,choice,table,foodGui));
		stateChanged();
	}
    public void msgHereIsQuote(Map<String, Integer> order, String choice, boolean complete){
		
		print ("Received quote from market");
		Food f = foods.get(choice);
		f.foodDeliveredState = deliverystate.waitingDelivery;
		f.waitingAmount = order.get(choice); //the order amount we are waiting for
		print("Now waiting for delivery for " + f.waitingAmount+ " " + choice);
		
		if(!complete){
			print ("Cannot fulfill all orders. Remaining orders left for next market: ");
			partialdeliveries.add(new PartialDelivery(market,choice, f.amtOrdered - f.waitingAmount ));
			for (int i=0;i<markets.size();i++)
			{
				if (markets.get(i) == market)
				{
					partialdeliveries.get(i).otherMarket = (i+1)%3;
					print ("Other market is: "+partialdeliveries.get(i).otherMarket);
					break;
				}
			}
		}
	}

	public void msgOutOfStock(String choice)
	{
		print ("Cannot fulfill " + choice + " from current market. Next Market");
		nextMarket = (nextMarket+1)%3;
		//cook will re order from the next market when it realizes the item is still low
		Food f = foods.get(choice);
		f.foodDeliveredState = deliverystate.none;
		f.amtOrdered = 0;
		
		stateChanged();
	}
	public void msgHereIsDelivery(dCheck dCheck)
	{
		print ("Received "+dCheck.choice);
		Food f = foods.get(dCheck.choice);
		f.waitingAmount  = f.waitingAmount - dCheck.order.get(dCheck.choice);
		f.amtOrdered = f.amtOrdered - dCheck.order.get(dCheck.choice);
		if(f.waitingAmount == 0){
			f.foodDeliveredState = deliverystate.fulfilled;
		}
		foods.get(dCheck.choice).amount += dCheck.order.get(dCheck.choice);
		
		checksForCashier.add(dCheck);
    	stateChanged();
	}
	public void msgPleaseStop()
	{
		stop = stopstate.stopping;
		stateChanged();
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
		
		if(!checksForCashier.isEmpty()){
			giveCheck(checksForCashier.get(0));
			checksForCashier.remove(0);
			return true;
			
		}
		
		synchronized(partialdeliveries)
		{
			for (PartialDelivery p:partialdeliveries)
			{
				if (p.s == deliverystate.fulfilling)
				{
					callOtherMarket(p);
				}
				else if (p.s == deliverystate.fulfilled)
				{
					partialdeliveries.remove(p);
					return true;
				}
			}
		}
		//Normal orders
		for (Order o : Orders) 
		{
			if (Orders.get(0) == o)
			{
				if (o.s == state.pending) 
				{
					Cook(o);
					return true;
				}
				else if (o.s == state.done)
				{
					SendOrderBackToWaiter(o);
					return true;
				}
			}
		}
		
		//Producer-Consumer
		for (Order o : orders.orders) 
		{
			if (orders.orders.get(0) == o)
			{
				if (o.s == state.pending) 
				{
					Cook(o);
					return true;
				}
				else if (o.s == state.done)
				{
					SendOrderBackToWaiter(o);
					return true;
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
	public void callOtherMarket(PartialDelivery p)
	{
		Map<String,Integer> supply = new HashMap<String,Integer>();
		for (int i=0;i<choices.length;i++)
		{
			if (choices[i].equals(p.choice))
			{
				supply.put(p.choice, p.amountleft);
			}
			else
			{
				supply.put(choices[i], 0);
			}
		}
		market = markets.get(p.otherMarket);
		market.msgCallOrder(supply,p.choice, cookContact);
		p.s = deliverystate.fulfilled;
	}
	private void Cook(final Order o) 
	{
		Food f = foods.get(o.choice);
		if (f.amount == 0)
		{
			o.w.msgOutOfFood(o.table, o.choice);
			if (!Orders.isEmpty())
			{
				Orders.remove(o);
			}
			if (!orders.orders.isEmpty())
			{
				orders.remove(o);
			}
			return;
		}
		else
		{
			f.amount--;
			System.out.println ("Amount of "+o.choice+": "+f.amount);
			cookGui.MoveToFridge();
			o.s = state.none;
			timer.schedule(new TimerTask()
			{
				public void run()
				{
					cookGui.MoveToTablePos(o.table);
					o.foodGui.FridgeToPosition();
					o.foodGui.setSpeed(2);
				}
			},4000);
			timer.schedule(new TimerTask()
			{
				public void run()
				{
					o.foodGui.MoveToPlatingArea();
				}
			},8000-(2000*(o.table-1)));
			timer.schedule(new TimerTask()
			{
				public void run()
				{
					DoCooking(o);
					o.s = state.cooking;
				}
			},5000-(2500*(o.table-1)));
		}
		//checking the inventory amount and its threshold low 
				if (f.amount <= f.threshold && (f.foodDeliveredState == deliverystate.none || 
						f.foodDeliveredState == deliverystate.fulfilled)) 
					//Scenario 2: The cook may run out of food. 
					//also only check inventory if food hasn't been delivered yet
				{
					print ("Food Low, so going to Market for "+f.type);
					Map<String,Integer> supply = new HashMap<String,Integer>();
					for (int i=0;i<choices.length;i++) //Roy believes putting every food inside the supply map is a better idea than adding just 1 
					{
						if (choices[i].equals(f.type))
						{
							f.amtOrdered = f.maxAmount - f.amount;
							supply.put(f.type, f.amtOrdered);
						}
						else
						{
							supply.put(choices[i], 0);
						}
					}
					
					f.foodDeliveredState = deliverystate.fulfilling;
					market = markets.get(nextMarket);
					
					//calling market for order, cook orders food one at a time
				
					market.msgCallOrder(supply,f.type,cookContact);
				}
	}
	
	public void checkInventory()
	{
		for (int j=0;j<6;j++)
		{
			Food f = foods.get(choices[j]);
			if (f.amount <= f.threshold) //fix this so that it won't be called 24/7 by the scheduler. Put every order inside the supply map. 
			{
				print ("Food Low, so going to Market for "+f.type);
				Map<String,Integer> supply = new HashMap<String,Integer>();
				for (int i=0;i<choices.length;i++) //Roy believes putting every food inside the supply map is a better idea than adding just 1 
				{
					if (choices[i].equals(f.type))
					{
						supply.put(f.type, 5); //ordering 5 items 
					}
					else
					{
						supply.put(choices[i], 0);
					}
				}
				market = markets.get(nextMarket);
				market.msgCallOrder(supply,f.type,cookContact);
			}
		}
	}
	
	private void DoCooking(final Order o)
	{
		print ("cooking");
		timer.schedule(new TimerTask()  //schedule time to cook dependent on choice. Set the state to done so that food can be delivered
		{
			public void run() 
			{
				o.s = state.done;
				o.foodGui.setSpeed(1);
				stateChanged();
			}
		},foods.get(o.choice).time+1000);
	}
	private void SendOrderBackToWaiter(Order o) 
	{
		o.w.msgDoneCooking(o.choice,o.table);
		FoodDone(o);
		cookGui.MoveToHomePosition();
	}
	
	private void giveCheck(dCheck check) {
		// TODO Auto-generated method stub
		Do("sending check to cashier");
		myManager = (CashierAgent) cookContact.myBoss;		
		myManager.msgHereIsCheck(check);
		
	}

	private  void FoodDone(Order o) 
	{
		if (!Orders.isEmpty())
		{
			Orders.remove(o);
		}
		if (!orders.orders.isEmpty())
		{
			orders.remove(o);
		}
	}
	public String getName() 
	{
		return name;
	}
	public void setMarkets(Vector<MarketAgent> markets)
	{
		this.markets = markets;
	}
	public void setGui(CookGui cookGui)
	{
		this.cookGui = cookGui;
	}
	public void setMyInfo(myContact information){
		this.cookContact = information; 
	}
	public void setAddress(Address s){
		cookContact.myAddress = s;
	}
	public myContact getMyInfo(){
		return cookContact;
	}
	
}
