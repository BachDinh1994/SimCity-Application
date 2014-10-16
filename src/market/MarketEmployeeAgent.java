package market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;


import market.gui.MarketEmployeeGui;
import agent.Agent;

public class MarketEmployeeAgent extends Agent {
	
	String name;
	MarketAgent myManager;
	MarketEmployeeGui myGui;
	private Semaphore retrieveItems = new Semaphore(0,true);

	//Order contains customer info, the order
	public List<Order> orderList = Collections.synchronizedList(new ArrayList<Order>());
	//maps where the food is in the storage
	
	
	public MarketEmployeeAgent(String type) {
		super();
		this.name = type;
	}
	
	
	//messages
	public void msgGetThis(Map<String,Integer> supply, String choice, Object cust){
		orderList.add(new Order(supply, choice, cust));
		stateChanged();
	}
	
	public void msgDoneRetrieving(){ //from animation
		print("done retrieving");
		retrieveItems.release();
	}
	

	
	
	//scheduler
	protected boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		/*if(!orderList.isEmpty()){
			gatherItems(orderList.get(0));
			return true;
		}*/
		for (Order o: orderList)
		{
			if(o.state == itemState.done){
				orderComplete(o);
				orderList.remove(o);
				return true;
			}
		}
		for (Order o: orderList)
		{
			if(o.state == itemState.newOrder){
				o.state = itemState.retreiving;
				gatherItems(o);
				return true;
			}
		}
		
		
		
		return false;
	}

	

	//action
	private void gatherItems(Order items){
		//tell gui to grab it
		myGui.getItems(items.order, items.choice);
		try {
			retrieveItems.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		items.state = itemState.done;
		stateChanged();
		
	}
	
	
	private void orderComplete(Order o) {
		// TODO Auto-generated method stub
		myManager.msgRetrievalComplete(o.customer);
		
	}

	
	
	
	
	
	
	
	
	public class Coord{
    	public int x;
    	public int y;
    	Coord(int xloc, int yloc){
    		x = xloc;
    		y = yloc;
    	}
    }
	
	enum itemState{newOrder, retreiving, done};
	public class Order 
	{
		public Map<String, Integer> order;
		public String choice;
		public Object customer;
		public itemState state;
		public Order(Map<String,Integer> order2,String choice2, Object c1)
		{
			state = itemState.newOrder;
			order = order2;
			choice = choice2;
			customer = c1;
		}
	}
	
	
	public void setMyManager(MarketAgent m)
	{
		myManager = m;
	}
    
	public void setGui(MarketEmployeeGui g)
	{
		myGui = g;
	}
    
	public String getName() 
	{
		return name;
	}
	
	
	
	
}
