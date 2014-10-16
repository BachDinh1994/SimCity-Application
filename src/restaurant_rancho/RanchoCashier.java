package restaurant_rancho;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import restaurant_rancho.interfaces.Cashier;
import restaurant_rancho.interfaces.Customer;
import restaurant_rancho.interfaces.Market;
import restaurant_rancho.interfaces.Waiter;
import restaurant_rancho.test.mock.EventLog;
import agent.Agent;

public class RanchoCashier extends Agent implements Cashier{

	Map<String,Food> priceList=new HashMap<String,Food>();
	private String name;
	private List<Check> checkList= Collections.synchronizedList(new ArrayList<Check>());
	public List<Payment> payList= Collections.synchronizedList(new ArrayList<Payment>());
	public List<Debt> debtList = Collections.synchronizedList(new ArrayList<Debt>());
	
	public enum CheckState{pending,finished};
	public enum PaymentState{pending,finished};
	public enum DebtState{pending, waiting,finished};
	
	private RanchoManager manager;
	
	private double wealth=50;
	public EventLog log = new EventLog();
	
	
	public RanchoCashier(String name){
		super();
		this.name=name;
		priceList.put("Steak",new Food("Steak",15.99));
		priceList.put("Chicken", new Food("Chicken",10.99));
		priceList.put("Salad", new Food("Salad",5.99));
		priceList.put("Pizza", new Food("Pizza",8.99));
		
	}
	
	

	//messages

	/* (non-Javadoc)
	 * @see restaurant.Cashier#msgComputeBill(restaurant.WaiterAgent, java.lang.String, int)
	 */
	@Override
	public void msgComputeBill(Waiter w,String choice, int table) {
		
		checkList.add(new Check(table,choice,w));
		stateChanged();
	}
	
	/* (non-Javadoc)
	 * @see restaurant.Cashier#msgHereIsPayment(restaurant.CustomerAgent, double, double)
	 */
	@Override
	public void msgHereIsPayment(Customer cust,double check, double cash) {
		payList.add(new Payment(cust,check,cash));
		stateChanged();
	}
	
	
	@Override
	public void msgPayMarket(Market market,String choice, int fulfill) {
		// TODO Auto-generated method stub
		debtList.add(new Debt(market,choice,fulfill));
		stateChanged();
	}


	
	
	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	@Override
	public boolean pickAndExecuteAnAction() {
		synchronized(checkList){
			for(Check c:checkList){
				if(c.state==CheckState.pending){
					giveWaiterBill(c);
					return true;
				}
			}
		}
		
		
		synchronized(payList){
			for(Payment p:payList){
				if(p.state==PaymentState.pending){
					giveCustomerChange(p);
					return true;
				}
			}
		}
		
		synchronized(debtList){
			for(Debt d:debtList){
				if(d.state==DebtState.pending){
					payMarket(d);
					return true;
				}
				
				if(d.state==DebtState.waiting){
					double price=priceList.get(d.choice).price;
					double flow=price*d.amount;
					if(wealth>=flow){
						payMarket(d);
						return true;
					}
				}
			}
		}
		
		
		
		return false;
	}
	
	
	
	
	
	
	//actions
	
	
	
	private void payMarket(Debt d) {
		// TODO Auto-generated method stub
		double price=priceList.get(d.choice).price;
		double flow=price*d.amount;
		if(wealth<flow){
			print("I am short of money. But I'll pay "+d.market+" "+flow+" dollars as soon as I have enough money.");
			d.state=DebtState.waiting;
			return;
		}
		wealth-=flow;
		d.state=DebtState.finished;
		print("Paid "+d.market+" "+flow+" dollars for "+d.amount+" "+d.choice);
		d.market.msgHereIsPayment(flow);
	}



	private void giveCustomerChange(Payment p) {
		p.state=PaymentState.finished;
		wealth+=p.checkAmount;
		double change=p.cash-p.checkAmount;
		print("Giving "+p.customer+" change for "+change);
		p.customer.msgHereIsChange(change);
	}


	private void giveWaiterBill(Check c) {
		c.state=CheckState.finished;
		print("Giving the check to "+c.waiter);
		double price=priceList.get(c.getChoice()).price;
		c.getWaiter().msgCheckReady(price,c.getTable());
	}





//inner class
	
	
	public class Debt{
		private Market market;
		private String choice;
		private int amount;
		private DebtState state;
		
		Debt(Market market2,String choice,int amount){
			this.market=market2;
			this.choice=choice;
			this.amount=amount;
			state=DebtState.pending;
		}

		public Market getMarket() {
			return market;
		}

		public void setMarket(RanchoMarket market) {
			this.market = market;
		}

		public String getChoice() {
			return choice;
		}

		public void setChoice(String choice) {
			this.choice = choice;
		}

		public int getAmount() {
			return amount;
		}

		public void setAmount(int amount) {
			this.amount = amount;
		}
	}
	
	
	
	
	public class Payment{
		
		private Customer customer;
		private double checkAmount;
		private double cash;
		private PaymentState state;
		
		Payment(Customer customer, double amount,double cash){
			this.customer=customer;
			this.checkAmount=amount;
			this.cash=cash;
			this.state=PaymentState.pending;
		}
		
		
		public Customer getCustomer() {
			return customer;
		}

		public void setCustomer(Customer customer) {
			this.customer = customer;
		}

		public double getCheckAmount() {
			return checkAmount;
		}

		public void setCheckAmount(double checkAmount) {
			this.checkAmount = checkAmount;
		}

		public double getCash() {
			return cash;
		}

		public void setCash(double cash) {
			this.cash = cash;
		}

		
	}
	
	
	public class Check{
		private int table;
		private String choice;
		private Waiter waiter=null;
		private CheckState state;
		
		Check(int table,String choice,Waiter waiter){
			this.table=table;
			this.choice=choice;
			this.waiter=waiter;
			state=CheckState.pending;
		}

		public int getTable() {
			return table;
		}

		public void setTable(int table) {
			this.table = table;
		}

		public String getChoice() {
			return choice;
		}

		public void setChoice(String choice) {
			this.choice = choice;
		}

		public Waiter getWaiter() {
			return waiter;
		}

		public void setWaiter(Waiter waiter) {
			this.waiter = waiter;
		}
		
	}
	
	
	
	
	private static class Food{
		private String choice;
		private double price;
		
		
		public Food(String choice, double price){
			this.choice=choice;
			this.price=price;
		}
		
	}


	/* (non-Javadoc)
	 * @see restaurant.Cashier#getName()
	 */
	@Override
	public String getName(){
		return name;
	}

	
	public String toString(){
		return name;
	}
	


	public double getWealth() {
		return wealth;
	}



	public void setWealth(double wealth) {
		this.wealth = wealth;
	}



	public List<Check> getCheckList() {
		return checkList;
	}



	public void setCheckList(List<Check> checkList) {
		this.checkList = checkList;
	}



	public void setManager(RanchoManager c) {
		// TODO Auto-generated method stub
		this.manager = c;
	}


	
}
