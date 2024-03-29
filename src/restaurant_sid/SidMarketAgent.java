package restaurant_sid;

import agent.Agent;

import java.util.*;

import restaurant_sid.SidCookAgent.Inventory;
import restaurant_sid.interfaces.Market;

/**
 * Restaurant Market Agent
 */
//A Market is the agent who provides food to the cook in a restaurant
public class SidMarketAgent extends Agent implements Market{
	private String name;
	private Inventory myStock;
	private double money;
	
	private ArrayList<String> delivery;
	private ArrayList<Double> moneyOwed;
	Timer timer = new Timer();
	
	private SidCookAgent cook;
	private SidCashierAgent cashier;

	public SidMarketAgent(String name) {
		super();

		this.name = name;
		int a = (int)(Math.random()*10) + 1;
		myStock = new Inventory(a,a,a,a);
		delivery = new ArrayList<String>();
		moneyOwed = new ArrayList<Double>();
		print(a + " of all items available");
	}

	public String getName() {
		return name;
	}

	// Messages
	public void msgInventoryLow(SidCookAgent c, String o){
		cook = c;
		print("Received order to restock " + o);
		delivery.add(o);
		stateChanged();
	}
	
	public void msgHereIsCash(String s, double val){
		money += val;
		moneyOwed.remove(val);
		print("Got money for the shipment of " + s + " now have $" + money);
	}
	
	public void msgNoCash(String s, double val){
		if (!moneyOwed.contains(val)){
			moneyOwed.add(val);
			print("Refusing to send further orders until payment of $" + val + " for " + s + " is made");
			stateChanged();
		}
	}
	
  
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		if (!delivery.isEmpty() && moneyOwed.isEmpty()){
			sendFood();
			return true;
		}
		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions
	private void sendFood(){
			final String tempO = delivery.get(0);
			delivery.remove(0);
			timer.schedule(new TimerTask() {
				public void run() {
					int count = 0;
					 while (myStock.useStock(tempO) && count < 5){
						count++;
					}
					if (count > 0){
						cook.msgFoodDelivered(tempO, count);
						cashier.msgFoodDelivered(SidMarketAgent.this, tempO, count);
					}else{ 
						cook.msgNoDelivery();
					}
				}	
			},2000);
	}
	//utilities
	public void addCashier(SidCashierAgent c){
		cashier = c;
	}
	public ArrayList<Double> getMoneyOwed(){
		return moneyOwed;
	}
	private class Inventory{
		private Map <String,Integer> stock;
		
		public Inventory(Integer s, Integer c, Integer sa, Integer p){
			stock = new HashMap <String,Integer>();
			stock.put("Steak",s);
			stock.put("Chicken",c);
			stock.put("Salad", sa);
			stock.put("Pizza", p);
		}
		
		public void setStock(String item, Integer num){
			if (stock.containsKey(item))
				stock.put(item,num);
		}
		
		public int getStock(String item){
			return stock.get(item);
		}
		
		public boolean useStock(String item){
			if (stock.get(item) > 0){
				stock.put(item, stock.get(item)-1);
				return true;
			}
			return false;
		}
		
		public boolean isEmpty(){
			boolean emp = true;
			for (Map.Entry<String, Integer> m : stock.entrySet()){
				if (m.getValue() > 0)
					emp = false;
			}
			return emp;
		}
	}
}

