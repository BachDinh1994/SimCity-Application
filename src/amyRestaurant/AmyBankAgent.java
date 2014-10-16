package amyRestaurant;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import Role.Role;
import agent.Agent;
import amyRestaurant.interfaces.AmyBank;
import amyRestaurant.interfaces.AmyCashier;


/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public class AmyBankAgent extends Agent implements AmyBank {


	
	private Hashtable<AmyCashier, Double> bankAccount = new Hashtable<AmyCashier, Double>();
	public List<accountMoney> acct = new ArrayList<accountMoney>();

	private String name;


	public class accountMoney{
		AmyCashier amyCashier;
		Double money;
	}
	

	public Hashtable<AmyCashier, Double> getBankAccount() {
		// TODO Auto-generated method stub
		return bankAccount;
	}

	public void setAccount(AmyCashier c, double money){
		bankAccount.put(c, money);
	}

	public AmyBankAgent(String type) {
		super();
		this.name = type;
	}

	public String getName() {
		return name;
	}

	
	
	
	public void msgNeedMoney(AmyCashier amyCashier, Double bill){
		
		accountMoney ac = new accountMoney();
		ac.amyCashier = amyCashier;
		double bal = 	bankAccount.get(amyCashier) - bill;
		bankAccount.put(amyCashier, bal);
		ac.money = bill;

		acct.add(ac);
		stateChanged();
	}


	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */


		if(!acct.isEmpty()){
			giveMoney(acct.get(0));
			acct.remove(0);
			return true;
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}
	
	private void giveMoney(accountMoney ac){
		ac.amyCashier.msgHereIsBankMoney(ac.money);
		Do("Here is the requested amount, " + ac.amyCashier.getName());
	}




}