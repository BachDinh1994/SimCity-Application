package restaurant_andrew;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import agent.Agent;

import java.util.Timer;
import java.util.TimerTask;

import restaurant_andrew.AndrewWaiterRole.Order;
import restaurant_andrew.gui.CookGui;
import restaurant_andrew.interfaces.Market;
import restaurant_andrew.interfaces.Waiter;

public class AndrewCookAgent extends Agent {
	
	private CookGui g;
	AndrewWaiterCookMonitor orders = new AndrewWaiterCookMonitor(this);
	public List<AndrewMarketAgent> markets = new ArrayList<AndrewMarketAgent> ();
	int currentMarket = 0;
	public List<MOrder> marketOrders = Collections.synchronizedList(new ArrayList<MOrder>());
	private class MOrder {
		int marketNum; String type; int num; MOrderState s;
		public MOrder(int marketNum, String type, int num) {
			this.marketNum = marketNum;
			this.type = type;
			this.num = num;
			this.s = MOrderState.waiting;
		}
	}
	public enum MOrderState {pending, waiting, received, fulfilled};
	public List<Order> Orders = Collections.synchronizedList(new ArrayList<Order>());
	static public class Order {
		Waiter w; String choice; int table; OrderState s;
		public Order(Waiter w, String choice, int table) {
			this.w = w;
			this.choice = choice;
			this.table = table;
			this.s = OrderState.pending;
		}
	}
	public enum OrderState {pending, cooking, done, out};
	Timer timer = new Timer();
	Map<String, Food> foods;
	String name;
	private class Food {
		String name; int cookingTime; int inventory; int capacity; int low; //fState orderState;
		public Food(String n, int cT, int i, int c, int l) {
			this.name = n;
			this.cookingTime = cT;
			this.inventory = i;
			this.capacity = c;
			this.low = l;
		}
		public boolean isLow() {
			return inventory <= low;
		}
	}
	//public enum fState {};
	
	public AndrewCookAgent(String name) {
		super();
		this.name = name;
		Food steak = new Food("Steak", 3000, 2, 2, 1);
		Food chicken = new Food("Chicken", 4000, 2, 2, 1);
		Food pizza = new Food("Pizza", 5000, 2, 2, 1);
		Food salad = new Food("Salad", 2000, 2, 2, 1);
		foods = new HashMap<String, Food>();
		foods.put("Steak", steak);
		foods.put("Chicken", chicken);
		foods.put("Pizza", pizza);
		foods.put("Salad", salad);
	}

	public String getName() {
		return name;
	}


	public void msgHereIsOrder(Waiter w, String choice, int table)
	{
		System.out.println("Received order of " + choice + " from " + w.getName() + " for table " + table);
		Orders.add(new Order(w, choice, table));
		stateChanged();
	}
	
	public void msgFoodDone(Order o)
	{
		print("Finished cooking order of " + o.choice + " from " + (o.w).getName() + " for table " + o.table);
		o.s = OrderState.done;
		stateChanged();
	}
	
	public void msgOrderSummary(Market m, String type, int filled, int unfilled) {
		print("Recieved order summary from " + m.getName() + " for " + type + " (" + filled + " filled and " + unfilled + " unfilled)");
		MOrder mo = getMOrder(type);
		if (unfilled > 0) {
			mo.num = unfilled;
			mo.s = MOrderState.pending;
		}
		else mo.s = MOrderState.fulfilled;
		stateChanged();
	}
	public void msgHereIsFood(String type, int num) {
		print("Recieved " + num + " of " + type + " from markets");
		foods.get(type).inventory += num;
		stateChanged();
	}
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		int i;
		//Normal
		synchronized(orders) {
			for (Order o : Orders) {
				if (o.s == OrderState.done) {
					plateAndSend(o);
					return true;
				}
			}
		}
		synchronized(Orders) {
			for (Order o : Orders) {
				if (o.s == OrderState.pending) {
					TryToCookIt(o);
					return true;
				}
			}
		}
		synchronized(Orders) {
			for (Order o : Orders) {
				if (o.s == OrderState.out) {
					Orders.remove(o);
					return true;
				}
			}
		}
		
		//Producer consumer
		synchronized(orders) {
			for (Order o : orders.orders) {
				if (o.s == OrderState.done) {
					plateAndSend(o);
					return true;
				}
			}
		}
		synchronized(orders.orders) {
			for (Order o : orders.orders) {
				if (o.s == OrderState.pending) {
					TryToCookIt(o);
					return true;
				}
			}
		}
		synchronized(orders.orders) {
			for (Order o : orders.orders) {
				if (o.s == OrderState.out) {
					orders.remove(o);
					return true;
				}
			}
		}
		synchronized(marketOrders) {
			for (MOrder mo : marketOrders) {
				if (mo.s == MOrderState.pending) {
					orderFoodFromMarket(mo);
					return true;
				}
			}
		}
		synchronized(marketOrders) {
			for (MOrder mo : marketOrders) {
				if (mo.s == MOrderState.fulfilled) {
					marketOrders.remove(mo);
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
			msgFoodDone(o);
		}
		
		public OrderTask(Order o)
		{
			this.o = o;
		}
	}
	private void TryToCookIt(Order o) {
		Food food = foods.get(o.choice);
		if (food.inventory > 0) {
			print("Cooking order of " + o.choice + " from " + (o.w).getName() + " for table " + o.table + " for " + food.cookingTime/1000 + " seconds");
			food.inventory--;
			print("Inventory left for " + o.choice + ": " + food.inventory);
			if (food.isLow()) {
				if (getMOrder(o.choice) == null) orderFoodFromMarket(o.choice);
			}
			DoPutOnGrill(o.choice);
			o.s = OrderState.cooking;
			timer.schedule(new OrderTask(o), food.cookingTime);
		}
		else
		{
			print("Out of inventory of " + o.choice + " for " + (o.w).getName() + " for table " + o.table);
			(o.w).msgOutOfFood(o.choice, o.table);
			orders.remove(o);
			Orders.remove(o);
		}
	}
	private void DoPutOnGrill(String choice) {
		g.DoPutOnGrill(choice);
	}
	
	public void orderFoodFromMarket(String type) {
		AndrewMarketAgent m = markets.get(currentMarket);
		currentMarket++; if (currentMarket == markets.size()) currentMarket = 0;
		Food food = foods.get(type);
		print("Low on " + type + ", ordering " + (food.capacity - food.inventory) + " more from " + m.getName());
		marketOrders.add(new MOrder(currentMarket, type, food.capacity - food.inventory));
		m.msgOrderFood(this, type, food.capacity - food.inventory); // supposed to be list, but only one will be reordered at a time
	}
	public void orderFoodFromMarket(MOrder mo) {
		mo.marketNum++; if (mo.marketNum == markets.size()) mo.marketNum = 0;
		AndrewMarketAgent m = markets.get(mo.marketNum);
		print("Re-ordering " + mo.num + " of " + mo.type + " from " + m.getName());
		m.msgOrderFood(this, mo.type, mo.num);
	}
	
	private void plateAndSend(Order o) {
		print("Plating and sending order of " + o.choice + " to " + (o.w).getName() + " for table " + o.table);
		DoPlate(o.choice);
		(o.w).msgOrderDone(o.choice, o.table);
		o.s = OrderState.out;
	}
	private void DoPlate(String choice) {
		g.DoPlate(choice);
	}
	
	private MOrder getMOrder(String type) {
		synchronized(marketOrders) {
			for (MOrder mo : marketOrders) {
				if (mo.type.equals(type)) return mo;
			}
			print("could not find mo from m and type");
			return null;
		}
	}
	
	public void addMarket(AndrewMarketAgent m) {
		markets.add(m);
		m.startThread();
	}
	
	public void setGui(CookGui g) {
		this.g = g;
	}
	public CookGui getGui() {
		return g;
	}
}
