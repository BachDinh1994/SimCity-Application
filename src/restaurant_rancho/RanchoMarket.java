package restaurant_rancho;


import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import restaurant_rancho.interfaces.Market;
import agent.Agent;


public class RanchoMarket extends Agent implements Market{

	Map<String,Food> foods=new HashMap<String,Food>();
	private String name;
	private RanchoCook currentCook;
	private AgentState state=AgentState.noAction;
	private enum AgentState{noAction,fulfilling};
	private String currentChoice;
	private int currentFulfill;
	private int currentOrder;
	private double money=0;
	private RanchoCashier cashier;

	
	public RanchoMarket(String name){
		super();
		this.name=name;
		foods.put("Steak",new Food("Steak",3,5000));
		foods.put("Chicken", new Food("Chicken",3,4000));
		foods.put("Salad", new Food("Salad",3,3000));
		foods.put("Pizza", new Food("Pizza",3,2000));
	}
	
	
	//messages

	@Override
	public void msgFulfillMe(RanchoCook cook,String choice, int i) {
		
		if(foods.containsKey(choice)){
			this.currentOrder=i;
			this.currentChoice=choice;
			this.currentCook=cook;
			this.currentFulfill=Math.min(foods.get(choice).amount,i);
			
			foods.get(choice).amount-=currentFulfill;
			this.state=AgentState.fulfilling;
			stateChanged();
		}
		
	}
	
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	@Override
	protected boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		if(state==AgentState.fulfilling){
			fulfill();
			state=AgentState.noAction;
			return true;
		}
		return false;
	}
	
	
	
	//actions
	
	
	
	
	
	
	private void fulfill() {
		// TODO Auto-generated method stub

		print("Fulfill "+currentFulfill+" "+currentChoice);
		if(currentOrder!=currentFulfill){
			print("Cannot fulfill "+(currentOrder-currentFulfill)+" "+currentChoice);
		}
		Timer timer = new Timer();
		timer.schedule(new TimerTask(){

			@Override
			public void run() {
				currentCook.msgHereIsFulfillment(currentChoice,currentFulfill);
			}
			
		},foods.get(currentChoice).time);
		
		cashier.msgPayMarket(this,currentChoice, currentFulfill);
	}



	//inner class


	private static class Food{
		private String choice;
		private int amount;
		private int time;
		
		public Food(String choice,int amount,int time){
			this.choice=choice;
			this.amount=amount;
			this.time=time;
		}
		
	}


	@Override
	public String getName(){
		return name;
	}


	@Override
	public void setCashier(RanchoCashier c) {
		// TODO Auto-generated method stub
		this.cashier=c;
	}


	@Override
	public void msgHereIsPayment(double flow) {
		// TODO Auto-generated method stub
		print("received payments from cashier: "+flow+" dollars.");
		money+=flow;
	}


	
	@Override
	public String toString(){
		return name;
	}
}
