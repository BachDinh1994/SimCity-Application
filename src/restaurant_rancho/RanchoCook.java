package restaurant_rancho;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import restaurant_rancho.gui.CookGui;
import restaurant_rancho.interfaces.Waiter;
import agent.Agent;

public class RanchoCook extends Agent{
	public List<Order> Orders =Collections.synchronizedList(new ArrayList<Order>());
	RanchoWaiterCookMonitor orders = new RanchoWaiterCookMonitor(this);
	public List<RanchoMarket> markets=Collections.synchronizedList(new ArrayList<RanchoMarket>());
	Timer timer=new Timer();
	public enum OrderStatus{pending, cooking, cooked, finished};
	Map<String,Food> foods=new HashMap<String,Food>();
	
	private String name;
	private Semaphore doneFulfill=new Semaphore(0,true);
	
	private Semaphore atFrige=new Semaphore(0,true);
	private Semaphore atCookingArea = new Semaphore(0,true);
	private Semaphore atPlatingArea = new Semaphore(0,true);
	
	private CookGui cookGui;
	private RanchoManager manager;
	public RanchoCook(String name){
		super();
		this.name=name;
		foods.put("Steak",new Food("Steak",6000,1,2));
		foods.put("Chicken", new Food("Chicken",4000,1,2));
		foods.put("Salad", new Food("Salad",3000,1,2));
		foods.put("Pizza", new Food("Pizza",1500,1,2));
		
	}
	
	
	//messages
	
	public void msgHereIsAnOrder(Waiter w, String c, int t){
		System.out.println("Received order to cook for: "+c);
		Orders.add(new Order(w,c,t));
		stateChanged();
	}
	
	public void msgDone(Order o){
		o.status=OrderStatus.cooked;
		stateChanged();
	}
	
	
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	@Override
	protected boolean pickAndExecuteAnAction() 
	{
		//Normal
		synchronized (Orders){
			for(Order order:Orders){
				if(order.status==OrderStatus.pending){
					if(checkFood(order)){
						cookOrder(order);
					}
					return true;
				}
				if(order.status==OrderStatus.cooked){
					finishOrder(order);
					return true;
				}
			}
		}
		//Producer consumer
		synchronized (orders){
			for(Order order:orders.orders){
				if(order.status==OrderStatus.pending){
					if(checkFood(order)){
						cookOrder(order);
					}
					return true;
				}
				if(order.status==OrderStatus.cooked){
					finishOrder(order);
					return true;
				}
			}
		}
		return false;
	}
	//actions
	private void cookOrder(final Order order) {
		// TODO Auto-generated method stub
		order.status=OrderStatus.cooking;
		DoCooking();
		int cookingtime=foods.get(order.choice).cookingTime;
		
		timer.schedule(new TimerTask(){

			@Override
			public void run() {
				msgDone(order);
			}
			
		},cookingtime);
	}


	private void DoCooking() {
		// TODO Auto-generated method stub
		print("cooking right now");
		cookGui.moveToFrige();
		try{
			atFrige.acquire();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		cookGui.moveToCookingArea();
		try{
			atCookingArea.acquire();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		cookGui.moveToHome();
	}


	
	private void finishOrder(Order order) {
		order.status=OrderStatus.finished;
		DoFinishOrder(order.choice,order.waiter);
		if (!Orders.isEmpty())
		{
			Orders.remove(order);
		}
		if (!orders.orders.isEmpty())
		{
			orders.remove(order);
		}
		order.waiter.msgOrderIsReady(order.choice,order.table);	
	}

	private void DoFinishOrder(String choice,Waiter waiter) {
		print("Order finished");
		cookGui.moveToCookingArea();
		try{
			atCookingArea.acquire();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		cookGui.moveToPlatingArea(choice,waiter);
		try{
			atPlatingArea.acquire();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		cookGui.moveToHome();
		
	}


	
	private boolean checkFood(Order order){
		Food f=foods.get(order.choice);
		if(f.amount==0){
			//msg to Waiter
			order.waiter.msgRunOutOfFood(order.choice,order.table);
			if (!Orders.isEmpty())
			{
				Orders.remove(order);
			}
			if (!orders.orders.isEmpty())
			{
				orders.remove(order);
			}
			//send message to the market agent
			int i=0;
			while(f.amount!=f.capacity&&i<4){
				print("Market, please fulfill me with "+(f.capacity-f.amount)+" "+f.choice);
				markets.get(i).msgFulfillMe(this,f.choice,f.capacity-f.amount);
				try{
					doneFulfill.acquire();
				}
				catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				i++;
				if(i==4&&f.amount!=f.capacity){
					this.msgNotFill(f.choice, f.capacity-f.amount);
				}
			}
			
			//remove order from Orderlist
			
			return false;
		}
		f.amount--;
		
		return true;
	}
	
	
	//inner class
	public static class Order{
		private Waiter waiter;
		private String choice;
		private int table;
		
		private OrderStatus status;
		
		public Order(Waiter w, String c, int t){
			waiter=w;
			choice=c;
			table=t;
			status=OrderStatus.pending;
		}
	}



	private static class Food{
		private String choice;
		private int cookingTime;
		private int amount;
		private int capacity;
		
		public Food(String choice, int cookingTime,int amount,int capacity){
			this.choice=choice;
			this.cookingTime=cookingTime;
			this.amount=amount;
			this.capacity=capacity;
		}
		
	}


	
	
	public String getName(){
		return name;
	}

	public String toString(){
		return name;
	}

	public void msgHereIsFulfillment(String choice, int number) {
		// TODO Auto-generated method stub
		foods.get(choice).amount+=number;
		print("Receive "+number+" "+choice);
		doneFulfill.release();
		stateChanged();
	}


	public void addMarket(RanchoMarket market) {
		// TODO Auto-generated method stub
		markets.add(market);
	}


	public void msgNotFill(String choice, int n) {
		// TODO Auto-generated method stub
		print("Receive the message not fully fulfilled");
	}


	public void setGui(CookGui c) {
		// TODO Auto-generated method stub
		this.cookGui=c;
	}


	public void msgAtFrige() {
		// TODO Auto-generated method stub
		atFrige.release();
	}


	public void msgAtCookingArea() {
		// TODO Auto-generated method stub
		atCookingArea.release();
	}


	public void msgAtPlatingArea() {
		// TODO Auto-generated method stub
		atPlatingArea.release();
	}


	public void msgPickUp(Waiter w) {
		// TODO Auto-generated method stub
		cookGui.beingPickedUp(w);
	}


	public void setManager(RanchoManager c) {
		// TODO Auto-generated method stub
		this.manager = c;
	}





	
	
	
}

