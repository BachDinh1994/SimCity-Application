package market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import restaurant.interfaces.Customers;

import SimCity.Address;
import SimCity.PersonAgent;
import SimCity.myContact;
import agent.Agent;
import market.MarketAgent.dCheck;
import market.gui.MarketCustomerGui;
import market.interfaces.Market;

public class MarketCustomerAgent extends Agent {

	
	PersonAgent myself;
	
	String name;
	MarketCustomerGui myGui;
	myContact customerContact;
	MarketAgent myMarket;
	dCheck check;
	double cash = 100.0;
	boolean goToNextMarket = false;

	public MarketCustomerAgent(String type) {
		super();
		this.name = type;
	}
	//myTab
	
	public Map<String, Integer> groceryList = new HashMap<String, Integer>();
	//holds grocery
	public Map<String, Integer> myBag = new HashMap<String, Integer>();
	public String grocery;

	public enum AgentState
	{none, enteredMarket, waitingInLine, frontOfLine, orderGrocery, gotGrocery, payMarket, waitForGroceries, goToNextMarket, done};
	private AgentState myState = AgentState.none;
	//messages
	
	private Semaphore atFrontLine = new Semaphore(0,true);
	
	
	public void msgEnteredMarket(MarketAgent market){
		//from the person
		myMarket = market;
		myMarket.Line.get(0).available = false;
		myGui.getInLine(myMarket.Line.get(0));
	
	}
	
	public void msgFrontOfLine(){
		//from animation
		Do("front of the line, ready to order");
		myState = AgentState.frontOfLine;
		stateChanged();

	}
	
	public void msgPayASAP() {
		// TODO Auto-generated method stub
		print("WARNED by market: pay asap! Should go to bank");
	}
	
	public void msgHereIsQuote(Map<String, Integer> order, String choice,
			boolean fulfilled) {
		// TODO Auto-generated method stub
		print ("waiting for grocery");
		myState = AgentState.waitForGroceries;
		if(!fulfilled){
			goToNextMarket = true;
			print ("Cannot fulfill all orders. Go to a different market after this");
		}
		stateChanged();
		
	}

	public void msgOutOfStock(String choice) {
		// TODO Auto-generated method stub
		print ("Cannot fulfill " + choice + " from current market. Next Market");
		myState = AgentState.goToNextMarket;
		stateChanged();
	}
    	
	
	public void msgHereIsOrder(dCheck myCheck){
		//check received from market or whomever
		String food = myCheck.choice;
		myBag.put(food, myCheck.order.get(food));
		check = myCheck;
		myState = AgentState.gotGrocery;
		stateChanged();
	}

	
	
	//scheduler
	protected boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		/*if(!orderList.isEmpty()){
			gatherItems(orderList.get(0));
			return true;
		}*/
		if(myState == AgentState.frontOfLine){
			myState = AgentState.orderGrocery;
			Do("I'm in here");
			callMarketOwner();
			return true;
		}
		
		if(myState == AgentState.gotGrocery){
			myState = AgentState.payMarket;
			payMarket();
			return true;
		}
		if (myState == AgentState.done){
			myState = AgentState.none;
			goHome();
			return true;
		}
		
		return false;
	}

	

	//action
	
	
	private void payMarket() {
		// TODO Auto-generated method stub
		print("paying market");
		Double amtPaid = 0.0;
		
		if(check.amtDue > cash){
			myself.addTab(check.amtDue - cash, myMarket);
			amtPaid = cash;
			cash = 0.0;
		}
		else{
			amtPaid = check.amtDue;
			cash = cash- amtPaid;
		}
	

		myMarket.msgHereIsBill(amtPaid, check);
		myState = AgentState.done;
		stateChanged();
	}
	
	private void goHome(){
		myGui.goHome();
	}

	private void callMarketOwner() {
		// TODO Auto-generated method stub
		Do("Giving order to Market");
		grocery = "Chicken";
		groceryList.put("Chicken", 2);
		myMarket.msgCallOrder(groceryList, grocery, customerContact);
	}
	
	public void setMarket(MarketAgent m){
		myMarket = m;
	}
	
	public void setMyGroceryList(Map<String, Integer> list, String item){
		//this is from the personAgent
		groceryList = list;
		grocery = item;
	}

	public void setGui(MarketCustomerGui g)
	{
		myGui = g;
	}
	
	public String getName() 
	{
		return name;
	}

	public class lineSpot{
		int place; //what place in line you are
		boolean isOccupied;
	}

	
	
	public void setMyInfo(myContact information){
		this.customerContact = information; 
	}
	
	public myContact getMyInfo(){
		return customerContact;
	}
	
	
}
