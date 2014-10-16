package restaurant_andrew;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import agent.Agent;

import java.util.Timer;
import java.util.TimerTask;

import restaurant_andrew.interfaces.Market;

public class AndrewMarketAgent extends Agent implements Market {
	
	double money = 0;
	
	AndrewCashierAgent ca;
	public List<Order> orders = Collections.synchronizedList(new ArrayList<Order>());
	public class Order {
		AndrewCookAgent c; String type; int num; int filled; int unfilled; OrderState s;
		public Order(AndrewCookAgent c, String type, int num) {
			this.c = c;
			this.type = type;
			this.num = num;
			this.filled = 0;
			this.unfilled = 0;
			this.s = OrderState.pending;
		}
	}
	public enum OrderState {pending, filling, filled};
	Timer timer = new Timer();
	int orderTime = 10000;
	Map<String, Integer> inventory;
	Map<String, Double> costs;
	String name;
	
	public AndrewMarketAgent(String name) {
		super();
		this.name = name;
		inventory = new HashMap<String, Integer>();
		inventory.put("Steak", 2);
		inventory.put("Chicken", 2);
		inventory.put("Pizza", 2);
		inventory.put("Salad", 2);
		costs = new HashMap<String, Double>();
		costs.put("Steak", 13.);
		costs.put("Chicken", 9.);
		costs.put("Pizza", 5.);
		costs.put("Salad", 2.);
	}

	@Override
	public String getName() {
		return name;
	}
	
	public void msgOrderFood(AndrewCookAgent c, String type, int num) {
		print("Recieved order from " + c.getName() + " for " + num + " " + type + "s");
		orders.add(new Order(c, type, num));
		stateChanged();
	}
	
	public void msgOrderFilled(Order o)
	{
		print("Finished filling order for " + (o.c).getName() + " for " + o.num + " " + o.type + "s (" + o.filled + " filled and " + o.unfilled + " unfilled)");
		o.s = OrderState.filled;
		stateChanged();
	}
	
	@Override
	public void msgPayBill(double money) {
		print("Recieved payment of " + money);
		this.money += money;
		stateChanged();
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		int i;
		synchronized(orders) {
			for (i = 0; i < orders.size(); i++) {
				Order o = orders.get(i);
				if (o.s == OrderState.pending) {
					tryToFillOrder(o);
					return true;
				}
			}
		}
		synchronized(orders) {
			for (i = 0; i < orders.size(); i++) {
				Order o = orders.get(i);
				if (o.s == OrderState.filled) {
					sendOrder(o);
					return true;
				}
			}
		}

		return false;
	}
	
	class OrderTask extends TimerTask {
		Order o;
		
		@Override
		public void run() {
			msgOrderFilled(o);
		}
		
		public OrderTask(Order o)
		{
			this.o = o;
		}
	}
	
	public void tryToFillOrder(Order o) {
		print("Trying to fill order from " + (o.c).getName() + " for " + o.num + " " + o.type + "s");
		o.s = OrderState.filling;
		int current = inventory.get(o.type);
		if (current == 0) { // if no inventory left
			o.unfilled = o.num;
			(o.c).msgOrderSummary(this, o.type, o.filled, o.unfilled);
		}
		else {
			if (current - o.num >= 0) { // if order completely fulfilled
				o.filled = o.num;
				(o.c).msgOrderSummary(this, o.type, o.filled, o.unfilled);
				inventory.put(o.type, current - o.num);
				timer.schedule(new OrderTask(o), orderTime);
			}
			else { // if order only partly fulfilled
				o.filled = current;
				o.unfilled = o.num - current;
				(o.c).msgOrderSummary(this, o.type, o.filled, o.unfilled);
				inventory.put(o.type, 0);
				timer.schedule(new OrderTask(o), orderTime);
			}
		}
	}
	
	public void sendOrder(Order o) {
		print("Sending order for " + o.num + " " + o.type + "s (" + o.filled + " filled and " + o.unfilled + " unfilled) to " + (o.c).getName());
		print("Sending bill to " + ca.getName() + " for " + costs.get(o.type) * o.filled);
		(o.c).msgHereIsFood(o.type, o.filled);
		ca.msgHereIsMBill(this, costs.get(o.type) * o.filled);
		orders.remove(o);
	}
	
	public void setCashier(AndrewCashierAgent ca) {
		this.ca = ca;
	}
}
