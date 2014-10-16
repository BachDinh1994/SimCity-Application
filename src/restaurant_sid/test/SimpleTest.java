package restaurant_sid.test;

import restaurant_sid.SidCashierAgent;
import restaurant_sid.SidCookAgent;
import restaurant_sid.SidMarketAgent;
import restaurant_sid.test.mock.MockCashier;
import restaurant_sid.test.mock.MockCustomer;
import restaurant_sid.test.mock.MockWaiter;
import junit.framework.*;

public class SimpleTest extends TestCase{

	SidCashierAgent cashier;
	SidCookAgent cook;
	MockWaiter waiter;
	MockCustomer customer;
	
	SidMarketAgent market;
	MockCashier cashier2;
	
	public void setUp() throws Exception{
		super.setUp();
		
		cashier = new SidCashierAgent("cashier");
		cashier2 = new MockCashier("thief");
		waiter= new MockWaiter("waiter");
		customer = new MockCustomer("customer");
		
		market = new SidMarketAgent("market");
		cook = new SidCookAgent("clyde");
		
	}
	
	public void testMarketCashier(){
		//Preconditions
		assertEquals("Market should have no moneyOwed. It does.", market.getMoneyOwed().size(), 0);
		
		cashier2.msgFoodDelivered(market, "Steak", 1);
		
		assertEquals("Market should have 1 entry in moneyOwed. It doesn't", market.getMoneyOwed().size(),1);
		assertEquals("Market's entry in moneyOwed should be for 15.99. Instead, it is: " + market.getMoneyOwed().get(0), market.getMoneyOwed().get(0),15.99);
		
		market.msgInventoryLow(cook, "Steak");
		
		assertFalse("Market's scheduler should have returned false(money is owed), but it didn't.", market.pickAndExecuteAnAction());
		
		cashier2.msgFoodDelivered(market, "Steak", 1);
		assertEquals("Market should have no entry in moneyOwed. It doesn't", market.getMoneyOwed().size(),0);
		
		assertTrue("Market's scheduler should have returned true(money is not owed, there's a pending order), but it didn't.", market.pickAndExecuteAnAction());
	}
}
