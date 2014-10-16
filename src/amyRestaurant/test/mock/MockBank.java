package amyRestaurant.test.mock;



import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import amyRestaurant.AmyBankAgent.accountMoney;
import amyRestaurant.interfaces.AmyBank;
import amyRestaurant.interfaces.AmyCashier;
import amyRestaurant.interfaces.AmyCook;
import amyRestaurant.interfaces.AmyMarket;
import amyRestaurant.interfaces.AmyWaiter;



/**
 * A sample MockCustomer built to unit test a CashierAgent.
 *
 * @author Monroe Ekilah
 *
 */
public class MockBank extends Mock implements AmyBank {

	public MockBank(String name) {
		super(name);

	}
	private Hashtable<AmyCashier, Double> bankAccount = new Hashtable<AmyCashier, Double>();
	public List<accountMoney> acct = new ArrayList<accountMoney>();
	public EventLog log = new EventLog();{
		log.clear();
	}

	@Override
	public Hashtable<AmyCashier, Double> getBankAccount() {
		// TODO Auto-generated method stub
		return bankAccount;
	}

	@Override
	public void setAccount(AmyCashier c, double money) {
		// TODO Auto-generated method stub
		bankAccount.put(c, money);
	}

	@Override
	public void msgNeedMoney(AmyCashier amyCashier, Double bill) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("received msgNeedMoney from cashier for: $" + bill));
		bankAccount.put(amyCashier, bankAccount.get(amyCashier)-bill);
		amyCashier.msgHereIsBankMoney(bill);
	}
	
}
