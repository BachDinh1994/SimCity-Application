package Role;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import SimCity.LiveGui;
import SimCity.ResidentGui;
import SimCity.interfaces.Lord;
import SimCity.interfaces.Resident;
import SimCity.test.mock.EventLog;
import SimCity.test.mock.LoggedEvent;


public class ResidentRole extends Role implements Resident{
	
	
	//Data
	    public EventLog log;
		String name;
		LiveGui gui;
		Lord myLord;
		double rent = 1;
		public List<RentDue> rentDues = new ArrayList<RentDue>();
		//public List<OrderDue> orderDues = new ArrayList<OrderDue>();
		//public List<Order> orders = new ArrayList<Order>();
		public enum DueState{pending,paid};
		//public enum OrderState{processing,finished};
		public enum FoodState{noAction,doneCooked,beingEaten,doneEating,beingCooked};
		public enum ResidentState{eating,doneEating,exitHome,noAction,goSleeping,getUp};
		public ResidentState state=ResidentState.noAction;
		//HashMap<String,Food> foods = new HashMap<String, Food>();
		List<Food> foods = new ArrayList<Food>();
		public boolean isHungry = false;
		private Semaphore atTable = new Semaphore(0,true);
		private Semaphore atCooking = new Semaphore(0,true);
		private Semaphore atBed = new Semaphore(0,true);
		private Semaphore atHomePos = new Semaphore(0,true);
		private Semaphore atKitchen = new Semaphore(0,true);
		private Semaphore atFrige = new Semaphore(0,true);
		private Semaphore atDoor = new Semaphore(0,true);
		private Semaphore atLord = new Semaphore(0,true);
		boolean cookAtHome = false;
		




		//Messages
		public void msgAtLord(){
			
			atLord.release();
		}
		public void msgCookAtHome(){
			cookAtHome = true;
			
			stateChanged();
		}
		public void msgAtTable(){
			log.add(new LoggedEvent("receive msgAtTable"));
			atTable.release();
		}
		public void msgAtCooking(){
			log.add(new LoggedEvent("receive msgAtCooking"));
			atCooking.release();
		}
		public void msgAtBed(){
			log.add(new LoggedEvent("receive msgAtBed"));
			atBed.release();
		}
		public void msgAtHomePos(){
			log.add(new LoggedEvent("receive msgAtHomePos"));
			atHomePos.release();
		}
		public void msgAtKitchen(){
			log.add(new LoggedEvent("receive msgAtKitchen"));
			atKitchen.release();
		}
		public void msgAtFrige(){
			log.add(new LoggedEvent("receive msgAtFrige"));
			atFrige.release();
		}
		public void msgAtDoor(){
			log.add(new LoggedEvent("receive msgAtDoor"));
			atDoor.release();
		}
		
		public void msgGoToSleep(){
			log.add(new LoggedEvent("receive msgGoToSleep"));
			state=ResidentState.goSleeping;
			stateChanged();
		}

		public void msgDoneCooking(Food food){
			log.add(new LoggedEvent("receive msgDoneCooking"));
			food.state=FoodState.doneCooked;
			state = ResidentState.eating;
			stateChanged();
		}

		public void msgGotHungry(){
			log.add(new LoggedEvent("receive msgGotHungry"));
			isHungry=true;
			stateChanged();
		}

		public void msgFinishEating(Food food){
			log.add(new LoggedEvent("receive msgFinishEating"));
			food.state=FoodState.doneEating;
			state=ResidentState.doneEating;
			stateChanged();
		}

		public void msgRentIsDue(Lord l,double rent){
			log.add(new LoggedEvent("receive msgRentIsDue"));
			rentDues.add(new RentDue(l,rent));
			stateChanged();
		}

		public void msgPaymentAccepted(Lord lord,double amount){
			log.add(new LoggedEvent("receive msgPaymentAccepted"));
			if(amount==rent && lord == myLord){
				myPerson.getWealth().setCash(myPerson.getWealth().getCash()-amount);
			}
			stateChanged();
		}

		
		public void msgExitHome(){
			log.add(new LoggedEvent("receive msgExitHome"));
			state = ResidentState.exitHome;
			stateChanged();
			
		}
		
		
		
		/*public void msgICanFulfill(ArrayList<String> items, ArrayList<Integer> amount, boolean complete){
			if(!complete){
				
			}
			stateChanged();
		}*/

		/*public void msgHereIsDelivery(double amtDue, ArrayList<String> items, ArrayList<Integer> itemAmts, Delivery delivery){
			orderDues.add(new OrderDue(delivery,amtDue));
			orders.add(new Order(items,itemAmts));
			stateChanged();
		}*/



		//Scheduler

		public boolean pickAndExecuteAnAction(){


			for(Food f:foods){
				if(state == ResidentState.eating && f.state==FoodState.doneCooked){
					f.state=FoodState.beingEaten;
					state= ResidentState.noAction;
					eatFood(f);
					return true;
				}
				if(state == ResidentState.doneEating && f.state == FoodState.doneEating){
					f.state = FoodState.noAction;
					state = ResidentState.noAction;
					backToHomePosition();
					return true;
				}
			}
			
			if(state == ResidentState.goSleeping){
				state = ResidentState.noAction;
				sleep();
				return true;
			}
			
			if(state == ResidentState.getUp){
				state =ResidentState.noAction;
				backToHomePosition();
				return true;
			}
			
			if(cookAtHome){
				
				cookAtHome = false;
				cookAtHome();
				return true;
			}
			
			
			if(isHungry){
				isHungry=false;
				if((int)(Math.random()*2)==0){
					eatOut();
				}
				else{
					cookAtHome();
				}
				return true;
			}
			
			for(RentDue d:rentDues){
				if(d.state==DueState.pending){
					d.state = DueState.paid;
					
					//Residents only pay rents to their landlord and ignore anyone else who //askes for rents (probably a fraud)
					if(d.receiver == myLord && d.amount==rent){
						payRent(d);
						
						return true;
					}
				}
			}
			
			/*for(OrderDue d:orderDues){
				if(d.state==DueState.pending){
					d.state = DueState.paid;
					payOrder(d);
					return true;

				}
			}

			for(Order o:orders){
				if(o.state==OrderState.processing){
					o.state=OrderState.finished;
					addFood(o);
					return true;
				}
			}*/
			return false;
		}







		//Action

		public void eatFood(final Food f){
			
			log.add(new LoggedEvent("Go to the stove to bring the food to table"));
			gui.goToCooking();
			try{
				atCooking.acquire();
			}catch(InterruptedException e){
				e.printStackTrace();				
			}
			log.add(new LoggedEvent("Bring the food to table now"));
			gui.goToTable();
			try{
				atTable.acquire();
			}catch(InterruptedException e){
				e.printStackTrace();				
			}
			log.add(new LoggedEvent("Eat food now"));
			timer.schedule(new TimerTask(){
				
				public void run(){
					msgFinishEating(f);
				}
				
			},f.eatTime);
		}

		
		public void backToHomePosition(){
			log.add(new LoggedEvent("Go back to my home position."));
			gui.goToHomePosition();
			
		}
		
		public void eatOut(){
			
			log.add(new LoggedEvent("Leave home and eat in the restaurant."));
			gui.exitHome();
			try{
				atDoor.acquire();
			}catch(InterruptedException e){
				e.printStackTrace();				
			}
		}

		public void cookAtHome(){
			
			log.add(new LoggedEvent("Go to the kitchen."));
			gui.goToKitchen(); //Gui
			
			try{
				atKitchen.acquire();
			}catch(InterruptedException e){
				e.printStackTrace();				
			}
			log.add(new LoggedEvent("Go to the refrigerator to get some raw material first"));
			gui.goToFrige();
			try{
				atFrige.acquire();
			}catch(InterruptedException e){
				e.printStackTrace();				
			}
			final Food food = foods.get((int)(Math.random()*foods.size()));
			food.amount--;
			log.add(new LoggedEvent("Walk to the stove to do the cooking"));
			gui.goToCooking();
			try{
				atCooking.acquire();
			}catch(InterruptedException e){
				e.printStackTrace();				
			}
			//Timer Task
			log.add(new LoggedEvent("Doing the cooking right now."));
			timer.schedule(new TimerTask(){
				public void run(){
					msgDoneCooking(food);
				}
			},food.cookTime);
		}

		public void payRent(RentDue d){
			
			log.add(new LoggedEvent("pay the landlord "+d.amount+ " dollars for rents"));
			gui.goToLandlord();
			try{
				atLord.acquire();
			}catch(InterruptedException e){
				e.printStackTrace();				
			}
			(d.receiver).msgHereIsRent(this,d.amount);
			gui.goToHomePosition();
		}

		/*public void payOrder(OrderDue d){
			(d.receiver).msgHereIsMoney(this,d.amount);
			log.add(new LoggedEvent("pay "+d.amount+ " dollars for the order"));
		}*/


		
		public void sleep(){
			log.add(new LoggedEvent("Time to sleep. Going to the bed."));
			gui.goToBed();
			try{
				atBed.acquire();
			}catch(InterruptedException e){
				e.printStackTrace();				
			}
		}
		

		
		/*public void addFood(Order o){
			for(int i=0;i<o.items.size();i++){
				for(Food f:foods){
					if(f.foodName.equals(o.items.get(i))){
						f.amount+=o.itemAmt.get(i);
					}
					else{
						foods.add(new Food(o.items.get(i),o.itemAmt.get(i),10000,5000));
					}
				}
			}
		}*/



		//Utilities

		public ResidentRole( Lord l){
			super();
			myLord = l;
		}
		
		
		public ResidentRole(String name){
			super();
			this.name=name;
			log = new EventLog();
			foods.add(new Food("Steak",10,5000,5000));
			foods.add(new Food("Chicken",10,5000,5000));
			foods.add(new Food("Salad",10,5000,5000));
			foods.add(new Food("Pizza",10,5000,5000));
		}

		public void setLord(Lord l){
			myLord=l;
		}


		


		





		//Inner Class:
		
		/*
		class RentDue{
			Landlord receiver;
			double amount;
			DueState state;

			RentDue(Landlord r,double amount){
				this.receiver=r;
				this.amount=amount;
				state=DueState.pending;
			}
		}
		
		
		class OrderDue{
			Delivery receiver;
			double amount;
			DueState state;
			
			OrderDue(Delivery r,double amount){
				this.receiver=r;
				this.amount=amount;
				state=DueState.pending;
			}
		}
		
		class Food{
			
			String foodName;
			int amount;
			int cookTime;
			int eatTime;
			FoodState state=FoodState.noAction;
			
			Food(String foodName,int amount,int cookTime,int eatTime){
				this.foodName = foodName;
				this.amount = amount;
				this.cookTime = cookTime;
				this.eatTime = eatTime;
			}
		}
		
		

		class Order{
			ArrayList<String> items;
			ArrayList<Integer> itemAmt;
			OrderState state = OrderState.processing;
			
			Order(ArrayList<String> items,ArrayList<Integer> itemAmt){
				
				this.items=items;
				this.itemAmt=itemAmt;
			}

		}

	*/

		public void setGui(LiveGui g) {
			// TODO Auto-generated method stub
			/*if(g==null){
				System.out.println("fuck");
			}*/
			this.gui=g;
		}
		
		public String toString(){
			return name;
		}
		
}
