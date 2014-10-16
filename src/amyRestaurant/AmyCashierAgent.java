package amyRestaurant;

import agent.Agent;
import amyRestaurant.interfaces.AmyBank;
import amyRestaurant.interfaces.AmyCashier;
import amyRestaurant.interfaces.AmyCustomer;
import amyRestaurant.interfaces.AmyMarket;
import amyRestaurant.interfaces.AmyWaiter;
import amyRestaurant.test.mock.EventLog;
import amyRestaurant.test.mock.LoggedEvent;

import java.util.*;



/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class AmyCashierAgent extends Agent  implements AmyCashier{


	public EventLog log = new EventLog();{
		log.clear();
	}
	public enum checkState
	{New,Pending,cantPay,Paying, Done};

	public class Check{
		public String food;
		public checkState state = checkState.New;
		public AmyWaiter myWaiter;
		public int tableNum;
		public double money;
		public AmyCustomer myCust;

		Check(AmyWaiter w, AmyCustomer c, String f, int t, double m){
			myWaiter = w;
			myCust = c;
			food = f;
			tableNum = t;
			money = m;
			state = checkState.New;
		}
	}

	public enum billState
	{New,none, Done, Pending,paying, goToBank, needToPay};
	public class Bill{
		AmyMarket myMarket;
		double amt;
		String food;
		public billState state = billState.New;

		Bill(AmyMarket m, String f,  double a){
			myMarket = m;
			amt = a;
			food = f;
			state = billState.New;
		}
		public AmyMarket getMarket(){
			return myMarket;
		}
		public Double getAmt(){
			return amt;
		}

		public String getFood(){
			return food;
		}
	}

	public List<Bill> moneyToPay =  Collections.synchronizedList(new ArrayList<Bill>());


	public List<Check> BookKeeping =Collections.synchronizedList( new ArrayList<Check>());
	private String name;
	private Double storeBank = 0.0;
	private AmyBank myBank;

	public Hashtable<AmyCustomer, Double> pastCustDebt = new Hashtable<AmyCustomer, Double>();
	public Hashtable<String, Double> cost = new Hashtable<String, Double>();
	{cost.put("Chicken", 10.99 );
	cost.put("Steak", 15.99);
	cost.put("Salad", 5.99);
	cost.put("Pizza",8.99);}

	public AmyCashierAgent(String name) {
		super();

		this.name = name;
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}



	public void msgYouHaveToPay(Double loan, AmyMarket amyMarket){
		
		log.add(new LoggedEvent("Received msgYouHaveToPay"));
		
		for(Bill bill: moneyToPay){
			if(bill.myMarket.equals(amyMarket) && bill.state.equals(billState.Pending)){
				bill.amt = loan;
				bill.state = billState.goToBank;
				stateChanged();
			}
		}
	}


	public void msgCreateCheck(String food, int table, AmyWaiter amyWaiter, AmyCustomer cust){
		log.add(new LoggedEvent("Received msgCreateCheck, food: " + food + " table#: " + 
				table + " waiter: " + amyWaiter.getName() + " cust: " + cust.getName()));

		BookKeeping.add(new Check(amyWaiter, cust,food, table, cost.get(food)));
		stateChanged();
	}

	public void msgWarning(AmyMarket amyMarket){
		//Do("I'm sorry");
		synchronized(moneyToPay)
		{	for(Bill bill: moneyToPay){
			if(bill.myMarket.equals(amyMarket) && bill.state.equals(billState.Pending)){
				bill.state = billState.none;
				storeBank = 0.0;
				stateChanged();
			}
		}
		}
	}

	public void msgCantPay(AmyCustomer cust, double debt){
		log.add(new LoggedEvent("Received msgCantPay from customer " + cust.getName() + " paid: " + debt));
		synchronized(BookKeeping){
		for(Check check: BookKeeping){
			check.money = check.money - debt;
			check.state = checkState.cantPay;
			storeBank = storeBank + debt;
			stateChanged();
		}}
	}


	public void hereIsMoney(AmyCustomer cust, double cash){
		log.add(new LoggedEvent("Received hereIsMoney from customer " + cust.getName() + " paid: " + cash));
		synchronized(BookKeeping){
			for(Check check: BookKeeping){
				if(check.myCust.equals(cust)){
					check.state = checkState.Paying;
					storeBank = storeBank + cash;
					stateChanged();
				}
			}
		}}

	public void msgHereIsBill(Double bill, String food, AmyMarket amyMarket){
		log.add(new LoggedEvent("Received msgHereIsBill from " + amyMarket.getName() + " bill: " + bill));
		moneyToPay.add(new Bill(amyMarket, food, bill));
		stateChanged();

	}


	public void msgThankYou(AmyMarket amyMarket){
		log.add(new LoggedEvent("Received msgThankYou from market " + amyMarket.getName()));
		Do("received cerification of payment from " + amyMarket.getName());
	}

	public void msgHereIsBankMoney(Double money){
		log.add(new LoggedEvent("Received bank money from bank : " + money)); 
		storeBank = storeBank + money;
		for(Bill bill: moneyToPay){
			if(bill.state == billState.needToPay){
				bill.state = billState.paying;

			}
		}
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */


		for (Check cCheck: BookKeeping){
			if(cCheck.state == checkState.New){
				cCheck.state = checkState.Pending;
				calculateCheck(cCheck);
				return true;
			}
		}

		for (Check cCheck: BookKeeping){
			if(cCheck.state == checkState.cantPay){
				cCheck.state = checkState.Pending;
				checkDebt(cCheck);
				return true;
			}
		}

		for (Check cCheck: BookKeeping){
			if(cCheck.state == checkState.Paying){
				cCheck.state = checkState.Done;
				paymentReceived(cCheck);
				return true;
			}
		}


		for(Bill bill: moneyToPay){
			if(bill.state == billState.New){
				bill.state = billState.Pending;
				payMarket(bill);
				return true;

			}

		}

		for(Bill bill:moneyToPay){
			if(bill.state == billState.goToBank){
				bill.state = billState.needToPay;
				goToBank(bill);
				return true;
			}
		}

		for(Bill bill:moneyToPay){
			if(bill.state == billState.paying){
				bill.state = billState.Pending;
				pay(bill);
				return true;
			}
		}


		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}

	// Actions

	private void calculateCheck(Check check){

		if(pastCustDebt.containsKey(check.myCust)){
			check.money = check.money + pastCustDebt.get(check.myCust)*-1;			
		}
		Do("money:" + check.money);
		check.myWaiter.msgHereIsCheck(check.myCust, check.food, check.money, check.tableNum);

	}
	private void checkDebt(Check check){

		if(pastCustDebt.containsKey(check.myCust)){ //previous debt customer
			check.myCust.msgCleanDishes();
			Do("You had a warning, go clean dishes to pay for the debt");
			pastCustDebt.remove(check.myCust)	;	// remove from debt
		}
		else
		{
			//give warning
			pastCustDebt.put(check.myCust,  (-1*check.money));
			Do("You have a warning. Please pay the debt on your next visit");
			check.myCust.msgWarning();

		}
		BookKeeping.remove(check); // remove from checks	

	}

	private void goToBank(Bill loan){
		//get loan
		Do("I need to take out money from bank");
		myBank.msgNeedMoney(this, loan.amt + 100.0);
	}

	private void paymentReceived(Check check){
		//fully paid 
		log.add(new LoggedEvent("payment received verified")); 
		Do("Thank you, " + check.myCust + "." + " Please comeBack");
		check.myCust.msgThankYou();
		BookKeeping.remove(check);
		if(pastCustDebt.containsKey(check.myCust)){
			pastCustDebt.remove(check.myCust);
		}


	}

	private void payMarket(Bill bill){
		if(bill.amt <= storeBank){
			//can pay;
			storeBank = storeBank - bill.amt;
			bill.myMarket.msgHereIsPayment((double)bill.amt, this);
			bill.state = billState.Done;
			log.add(new LoggedEvent("Cashier paying the full amount of the bill: " + bill + " remaining: " + storeBank ));
			Do("Here is payment, " + bill.myMarket.getName() );
		}
		else{
			//cant pay;
			bill.myMarket.msgCantPay(storeBank, bill.food, this); // give what it can pay
			log.add(new LoggedEvent("can't pay full amount to " + bill.myMarket.getName()));
			Do("Can't pay full amount, " + bill.myMarket.getName() );
			storeBank = 0.0;
		}
	}

	private void pay(Bill bill){

		storeBank = storeBank - bill.amt;
		bill.myMarket.msgHereIsPayment((double)bill.amt, this);
		log.add(new LoggedEvent("Cashier paying the full amount of the bill: " + bill + " remaining: " + storeBank ));
		Do("Here is payment, " + bill.myMarket.getName() );
		bill.state = billState.Done;

	}



	//utilities
	public void setBank(AmyBank amyBank){
		myBank = amyBank;
	}
	public AmyBank getBank(){
		return myBank;
	}
	public void setStoreBank(Double money){
		storeBank = money;

	}
	public Double getStoreBank(){
		return storeBank;

	}

}

