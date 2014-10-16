package SimCity.interfaces;



import Role.ResidentRole.DueState;
import Role.ResidentRole.FoodState;
import SimCity.LiveGui;

public interface Resident {
	//Messages
	
		public abstract void msgAtLord();
	
		public abstract void msgCookAtHome();
	
		public abstract void msgAtTable();

		public abstract void msgAtCooking();

		public abstract void msgAtBed();

		public abstract void msgAtHomePos();

		public abstract void msgAtKitchen();

		public abstract void msgAtFrige();

		public abstract void msgAtDoor();

		public abstract void msgGoToSleep();

		public abstract void msgDoneCooking(Food food);

		public abstract void msgGotHungry();

		public abstract void msgFinishEating(Food food);

		public abstract void msgRentIsDue(Lord l, double rent);

		public abstract void msgPaymentAccepted(Lord l,double amount);

		public abstract void msgExitHome();

		public abstract boolean pickAndExecuteAnAction();

		public abstract void eatFood(Food f);

		public abstract void backToHomePosition();

		public abstract void eatOut();

		public abstract void cookAtHome();

		public abstract void payRent(RentDue d);

		//public abstract void payOrder(OrderDue d);

		public abstract void sleep();

		//public abstract void addFood(Order o);

		public abstract void setLord(Lord l);

		public abstract void setGui(LiveGui g);
		
		public class RentDue{
			public Lord receiver;
			public double amount;
			public DueState state;

			public RentDue(Lord r,double amount){
				this.receiver=r;
				this.amount=amount;
				state=DueState.pending;
			}
		}
		
		
		/*public class OrderDue{
			public Delivery receiver;
			public double amount;
			public DueState state;
			
			public OrderDue(Delivery r,double amount){
				this.receiver=r;
				this.amount=amount;
				state=DueState.pending;
			}
		}*/
		
		public class Food{
			
			public String foodName;
			public int amount;
			public int cookTime;
			public int eatTime;
			public FoodState state=FoodState.noAction;
			
			public Food(String foodName,int amount,int cookTime,int eatTime){
				this.foodName = foodName;
				this.amount = amount;
				this.cookTime = cookTime;
				this.eatTime = eatTime;
			}
		}
		
		

		/*public class Order{
			public ArrayList<String> items;
			public ArrayList<Integer> itemAmt;
			public OrderState state = OrderState.processing;
			
			public Order(ArrayList<String> items,ArrayList<Integer> itemAmt){
				
				this.items=items;
				this.itemAmt=itemAmt;
			}
		}*/
}
