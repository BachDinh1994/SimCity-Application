package restaurant_andrew.test;

import restaurant_andrew.AndrewCashierAgent;
import restaurant_andrew.AndrewCashierAgent.BillState;
import restaurant_andrew.AndrewCashierAgent.Debtor;
import restaurant_andrew.test.mock.MockCustomer;
import restaurant_andrew.test.mock.MockMarket;
import restaurant_andrew.test.mock.MockWaiter;
import junit.framework.*;

public class CashierTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	AndrewCashierAgent cashier;
	MockWaiter waiter;
	MockCustomer customer;
	MockMarket market;
	MockMarket market1;
	MockMarket market2;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		cashier = new AndrewCashierAgent("cashier");		
		customer = new MockCustomer("mockcustomer");
		waiter = new MockWaiter("mockwaiter");
		market = new MockMarket("mockmarket");
		market1 = new MockMarket("mockmarket1");
		market2 = new MockMarket("mockmarket2");
	}	
	/**
	 * This tests the cashier where one customer is ready to pay the exact bill.
	 */
	public void testOneNormalCustomerScenario()
	{
		customer.cashier = cashier;
		
		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.", cashier.bills.size(), 0);		
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: " + cashier.log.toString(), 0, cashier.log.size());
		//step 1 of the test
		cashier.msgCalculateBill(waiter, customer, "Steak");
		//check postconditions for step 1 and preconditions for step 2
        assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: " + waiter.log.toString(), 0, waiter.log.size());
		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.bills.size(), 1);
		assertTrue("Cashier's \"bill\" list should contain a bill with the right customer in it. It doesn't.", cashier.bills.get(0).c == customer);
		assertTrue("Cashier's \"bill\" list should contain a bill with the right waiter in it. It doesn't.", cashier.bills.get(0).w == waiter);
		assertTrue("Cashier's \"bill\" list should contain a bill with the right choice in it. It doesn't.", cashier.bills.get(0).choice == "Steak");
		assertTrue("Cashier's scheduler should have returned true (calculating bill), but didn't.", cashier.pickAndExecuteAnAction());
		assertTrue("Cashier's \"bill\" list should contain a bill of price = 15.99. It contains something else instead: $" + cashier.bills.get(0).cost, cashier.bills.get(0).cost == 15.99);
		assertEquals("MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: " + waiter.log.toString(), 0, waiter.log.size());
		assertEquals("MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: " + customer.log.toString(), 0, customer.log.size());
		assertTrue("Cashier's scheduler should have returned true (sending bill), but didn't.", cashier.pickAndExecuteAnAction());
		assertTrue("MockWaiter's event log should read \"Received HereIsBill for mockcustomer. Total = $15.99\" after the Cashier's scheduler is called for the second time. Instead, the MockWaiter's event log reads: " + waiter.log.toString(), waiter.log.containsString("Received HereIsBill for mockcustomer. Total = 15.99"));
		assertEquals("MockCustomer should have an empty event log after the Cashier's scheduler is called for the second time. Instead, the MockCustomer's event log reads: " + customer.log.toString(), 0, customer.log.size());
		//step 2 of the test
		cashier.msgPayingBill(customer, 15.99);
		//check postconditions for step 2 and preconditions for step 3
		assertEquals("MockWaiter should have an event log of size 1 after the Cashier's scheduler is called for the third time. Instead, the MockWaiter's event log is size " + waiter.log.size(), 1, waiter.log.size());
		assertEquals("MockCustomer should have an empty event log after the Cashier's scheduler is called for the third time. Instead, the MockCustomer's event log reads: " + customer.log.toString(), 0, customer.log.size());
		assertTrue("Cashier's scheduler should have returned true (getting change), but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("Cashier should have 0 debtors in it. It doesn't.", cashier.debtors.size(), 0);
		assertTrue("Cashier's \"bill\" list should contain a bill with cost == paidMoney. It doesn't.", cashier.bills.get(0).cost == cashier.bills.get(0).paidMoney);
		assertEquals("MockWaiter should have an event log of size 1 after the Cashier's scheduler is called for the fourth time. Instead, the MockWaiter's event is size " + waiter.log.size(), 1, waiter.log.size());
		assertTrue("MockCustomer's event log should read \"Received HereIsChange from cashier. Change = 0\" after the Cashier's scheduler is called for the fourth time. Instead, the MockCustomer's event log reads: " + customer.log.toString(), customer.log.containsString("Received HereIsChange from cashier. Change = 0"));
		assertTrue("Cashier's scheduler should have returned true (clearing list), but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("MockWaiter should have an event log of size 1 after the Cashier's scheduler is called for the fourth time. Instead, the MockWaiter's event log is size " + waiter.log.size(), 1, waiter.log.size());
		assertEquals("MockCustomer should have an event log of size 1 after the Cashier's scheduler is called for the fourth time. Instead, the MockCustomer's event log is size " + customer.log.size(), 1, customer.log.size());
		assertEquals("Cashier should have 0 bills in it. It doesn't.", cashier.bills.size(), 0);
		assertFalse("Cashier's scheduler should have returned false (no action), but didn't.", cashier.pickAndExecuteAnAction());
	}
	/**
	 * This tests the cashier where one customer is ready to pay the bill with change.
	 */
	public void testTwoCustomerWithChange()
	{
		customer.cashier = cashier;
		
		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.", cashier.bills.size(), 0);		
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: " + cashier.log.toString(), 0, cashier.log.size());
		//step 1 of the test
		cashier.msgCalculateBill(waiter, customer, "Steak");
		//check postconditions for step 1 and preconditions for step 2
        assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: " + waiter.log.toString(), 0, waiter.log.size());
		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.bills.size(), 1);
		assertTrue("Cashier's \"bill\" list should contain a bill with the right customer in it. It doesn't.", cashier.bills.get(0).c == customer);
		assertTrue("Cashier's \"bill\" list should contain a bill with the right waiter in it. It doesn't.", cashier.bills.get(0).w == waiter);
		assertTrue("Cashier's \"bill\" list should contain a bill with the right choice in it. It doesn't.", cashier.bills.get(0).choice == "Steak");
		assertTrue("Cashier's scheduler should have returned true (calculating bill), but didn't.", cashier.pickAndExecuteAnAction());
		assertTrue("Cashier's \"bill\" list should contain a bill of price = 15.99. It contains something else instead: $" + cashier.bills.get(0).cost, cashier.bills.get(0).cost == 15.99);
		assertEquals("MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: " + waiter.log.toString(), 0, waiter.log.size());
		assertEquals("MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: " + customer.log.toString(), 0, customer.log.size());
		assertTrue("Cashier's scheduler should have returned true (sending bill), but didn't.", cashier.pickAndExecuteAnAction());
		assertTrue("MockWaiter's event log should read \"Received HereIsBill for mockcustomer. Total = $15.99\" after the Cashier's scheduler is called for the second time. Instead, the MockWaiter's event log reads: " + waiter.log.toString(), waiter.log.containsString("Received HereIsBill for mockcustomer. Total = 15.99"));
		assertEquals("MockCustomer should have an empty event log after the Cashier's scheduler is called for the second time. Instead, the MockCustomer's event log reads: " + customer.log.toString(), 0, customer.log.size());
		//step 2 of the test
		cashier.msgPayingBill(customer, 16.99);
		//check postconditions for step 2 and preconditions for step 3
		assertEquals("MockWaiter should have an event log of size 1 after the Cashier's scheduler is called for the third time. Instead, the MockWaiter's event log is size " + waiter.log.size(), 1, waiter.log.size());
		assertEquals("MockCustomer should have an empty event log after the Cashier's scheduler is called for the third time. Instead, the MockCustomer's event log reads: " + customer.log.toString(), 0, customer.log.size());
		assertTrue("Cashier's scheduler should have returned true (getting change), but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("Cashier should have 0 debtors in it. It doesn't.", cashier.debtors.size(), 0);
		assertTrue("Cashier's \"bill\" list should contain a bill with paidMoney - cost > 0. Instead, it equals " + (cashier.bills.get(0).paidMoney - cashier.bills.get(0).cost), cashier.bills.get(0).paidMoney - cashier.bills.get(0).cost > 0);
		assertEquals("MockWaiter should have an event log of size 1 after the Cashier's scheduler is called for the fourth time. Instead, the MockWaiter's event is size " + waiter.log.size(), 1, waiter.log.size());
		 // rounding errors on the next test. manually typed in.
		assertTrue("MockCustomer's event log should read \"Received HereIsChange from cashier. Change = 0.9999999999999982\" after the Cashier's scheduler is called for the fourth time. Instead, the MockCustomer's event log reads: " + customer.log.toString(), customer.log.containsString("Received HereIsChange from cashier. Change = 0.9999999999999982"));
		assertTrue("Cashier's scheduler should have returned true (clearing list), but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("MockWaiter should have an event log of size 1 after the Cashier's scheduler is called for the fourth time. Instead, the MockWaiter's event log is size " + waiter.log.size(), 1, waiter.log.size());
		assertEquals("MockCustomer should have an event log of size 1 after the Cashier's scheduler is called for the fourth time. Instead, the MockCustomer's event log is size " + customer.log.size(), 1, customer.log.size());
		assertEquals("Cashier should have 0 bills in it. It doesn't.", cashier.bills.size(), 0);
		assertFalse("Cashier's scheduler should have returned false (no action), but didn't.", cashier.pickAndExecuteAnAction());
	}
	/**
	 * This tests the cashier where one customer is underpaying for the first time.
	 */
	public void testThreeNewDebtor()
	{
		customer.cashier = cashier;
		
		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.", cashier.bills.size(), 0);		
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: " + cashier.log.toString(), 0, cashier.log.size());
		//step 1 of the test
		cashier.msgCalculateBill(waiter, customer, "Steak");
		//check postconditions for step 1 and preconditions for step 2
        assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: " + waiter.log.toString(), 0, waiter.log.size());
		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.bills.size(), 1);
		assertTrue("Cashier's \"bill\" list should contain a bill with the right customer in it. It doesn't.", cashier.bills.get(0).c == customer);
		assertTrue("Cashier's \"bill\" list should contain a bill with the right waiter in it. It doesn't.", cashier.bills.get(0).w == waiter);
		assertTrue("Cashier's \"bill\" list should contain a bill with the right choice in it. It doesn't.", cashier.bills.get(0).choice == "Steak");
		assertTrue("Cashier's scheduler should have returned true (calculating bill), but didn't.", cashier.pickAndExecuteAnAction());
		assertTrue("Cashier's \"bill\" list should contain a bill of price = 15.99. It contains something else instead: $" + cashier.bills.get(0).cost, cashier.bills.get(0).cost == 15.99);
		assertEquals("MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: " + waiter.log.toString(), 0, waiter.log.size());
		assertEquals("MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: " + customer.log.toString(), 0, customer.log.size());
		assertTrue("Cashier's scheduler should have returned true (sending bill), but didn't.", cashier.pickAndExecuteAnAction());
		assertTrue("MockWaiter's event log should read \"Received HereIsBill for mockcustomer. Total = $15.99\" after the Cashier's scheduler is called for the second time. Instead, the MockWaiter's event log reads: " + waiter.log.toString(), waiter.log.containsString("Received HereIsBill for mockcustomer. Total = 15.99"));
		assertEquals("MockCustomer should have an empty event log after the Cashier's scheduler is called for the second time. Instead, the MockCustomer's event log reads: " + customer.log.toString(), 0, customer.log.size());
		//step 2 of the test
		cashier.msgPayingBill(customer, 12.99);
		//check postconditions for step 2 and preconditions for step 3
		assertEquals("MockWaiter should have an event log of size 1 after the Cashier's scheduler is called for the third time. Instead, the MockWaiter's event log is size " + waiter.log.size(), 1, waiter.log.size());
		assertEquals("MockCustomer should have an empty event log after the Cashier's scheduler is called for the third time. Instead, the MockCustomer's event log reads: " + customer.log.toString(), 0, customer.log.size());
		assertTrue("Cashier's scheduler should have returned true (getting change), but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("Cashier should have 1 debtor in it. It doesn't.", cashier.debtors.size(), 1);
		assertTrue("Cashier's \"bill\" list should contain a bill with cost > paidMoney. It doesn't.", cashier.bills.get(0).cost > cashier.bills.get(0).paidMoney);
		assertEquals("MockWaiter should have an event log of size 1 after the Cashier's scheduler is called for the fourth time. Instead, the MockWaiter's event is size " + waiter.log.size(), 1, waiter.log.size());
		assertTrue("MockCustomer's event log should read \"Received HereIsChange from cashier. Change = 0\" after the Cashier's scheduler is called for the fourth time. Instead, the MockCustomer's event log reads: " + customer.log.toString(), customer.log.containsString("Received HereIsChange from cashier. Change = 0"));
		assertTrue("Cashier's scheduler should have returned true (clearing list), but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("MockWaiter should have an event log of size 1 after the Cashier's scheduler is called for the fourth time. Instead, the MockWaiter's event log is size " + waiter.log.size(), 1, waiter.log.size());
		assertEquals("MockCustomer should have an event log of size 1 after the Cashier's scheduler is called for the fourth time. Instead, the MockCustomer's event log is size " + customer.log.size(), 1, customer.log.size());
		assertEquals("Cashier should have 0 bills in it. It doesn't.", cashier.bills.size(), 0);
		assertFalse("Cashier's scheduler should have returned false (no action), but didn't.", cashier.pickAndExecuteAnAction());
	}
	/**
	 * This tests the cashier where one customer pays off their previous debt exactly.
	 */
	public void testThreeOldDebtor()
	{
		customer.cashier = cashier;
		cashier.debtors.add(cashier.new Debtor(customer, 1));
		
		//check preconditions
		assertEquals("Cashier should have 0 bills in it. It doesn't.", cashier.bills.size(), 0);
		assertEquals("Cashier should have 1 debtor in it. It doesn't.", cashier.debtors.size(), 1);
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsBill is called. Instead, the Cashier's event log reads: " + cashier.log.toString(), 0, cashier.log.size());
		//step 1 of the test
		cashier.msgCalculateBill(waiter, customer, "Steak");
		//check postconditions for step 1 and preconditions for step 2
        assertEquals("MockWaiter should have an empty event log before the Cashier's scheduler is called. Instead, the MockWaiter's event log reads: " + waiter.log.toString(), 0, waiter.log.size());
		assertEquals("Cashier should have 1 bill in it. It doesn't.", cashier.bills.size(), 1);
		assertTrue("Cashier's \"bill\" list should contain a bill with the right customer in it. It doesn't.", cashier.bills.get(0).c == customer);
		assertTrue("Cashier's \"bill\" list should contain a bill with the right waiter in it. It doesn't.", cashier.bills.get(0).w == waiter);
		assertTrue("Cashier's \"bill\" list should contain a bill with the right choice in it. It doesn't.", cashier.bills.get(0).choice == "Steak");
		assertTrue("Cashier's scheduler should have returned true (calculating bill), but didn't.", cashier.pickAndExecuteAnAction());
		assertTrue("Cashier's \"bill\" list should contain a bill of price = 15.99. It contains something else instead: $" + cashier.bills.get(0).cost, cashier.bills.get(0).cost == 15.99);
		assertEquals("MockWaiter should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockWaiter's event log reads: " + waiter.log.toString(), 0, waiter.log.size());
		assertEquals("MockCustomer should have an empty event log after the Cashier's scheduler is called for the first time. Instead, the MockCustomer's event log reads: " + customer.log.toString(), 0, customer.log.size());
		assertTrue("Cashier's scheduler should have returned true (sending bill), but didn't.", cashier.pickAndExecuteAnAction());
		assertTrue("MockWaiter's event log should read \"Received HereIsBill for mockcustomer. Total = $15.99\" after the Cashier's scheduler is called for the second time. Instead, the MockWaiter's event log reads: " + waiter.log.toString(), waiter.log.containsString("Received HereIsBill for mockcustomer. Total = 15.99"));
		assertEquals("MockCustomer should have an empty event log after the Cashier's scheduler is called for the second time. Instead, the MockCustomer's event log reads: " + customer.log.toString(), 0, customer.log.size());
		//step 2 of the test
		cashier.msgPayingBill(customer, 16.99);
		//check postconditions for step 2 and preconditions for step 3
		assertEquals("MockWaiter should have an event log of size 1 after the Cashier's scheduler is called for the third time. Instead, the MockWaiter's event log is size " + waiter.log.size(), 1, waiter.log.size());
		assertEquals("MockCustomer should have an empty event log after the Cashier's scheduler is called for the third time. Instead, the MockCustomer's event log reads: " + customer.log.toString(), 0, customer.log.size());
		assertTrue("Cashier's scheduler should have returned true (getting change), but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("Cashier should have 0 debtors in it. It doesn't.", cashier.debtors.size(), 0);
		assertTrue("Cashier's \"bill\" list should contain a bill with cost < paidMoney. It doesn't.", cashier.bills.get(0).cost < cashier.bills.get(0).paidMoney);
		assertEquals("MockWaiter should have an event log of size 1 after the Cashier's scheduler is called for the fourth time. Instead, the MockWaiter's event is size " + waiter.log.size(), 1, waiter.log.size());
		assertTrue("MockCustomer's event log should read \"Received HereIsChange from cashier. Change = 0\" after the Cashier's scheduler is called for the fourth time. Instead, the MockCustomer's event log reads: " + customer.log.toString(), customer.log.containsString("Received HereIsChange from cashier. Change = 0"));
		assertTrue("Cashier's scheduler should have returned true (clearing list), but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("MockWaiter should have an event log of size 1 after the Cashier's scheduler is called for the fourth time. Instead, the MockWaiter's event log is size " + waiter.log.size(), 1, waiter.log.size());
		assertEquals("MockCustomer should have an event log of size 1 after the Cashier's scheduler is called for the fourth time. Instead, the MockCustomer's event log is size " + customer.log.size(), 1, customer.log.size());
		assertEquals("Cashier should have 0 bills in it. It doesn't.", cashier.bills.size(), 0);
		assertFalse("Cashier's scheduler should have returned false (no action), but didn't.", cashier.pickAndExecuteAnAction());
	}
	/**
	 * This tests the normal cashier/market interaction.
	 */
	public void testFiveNormalMarketBill()
	{
		market.cashier = cashier;
		
		//check preconditions
		assertEquals("Cashier should have 0 market bills in it. It doesn't.", cashier.marketBills.size(), 0);
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsMBill is called. Instead, the Cashier's event log reads: " + cashier.log.toString(), 0, cashier.log.size());
		//test
		cashier.msgHereIsMBill(market, 12);
		//check postconditions
		assertEquals("MockMarket should have an empty event log before the Cashier's scheduler is called. Instead, the MockMarket's event log reads: " + market.log.toString(), 0, market.log.size());
		assertEquals("Cashier should have 1 market bill in it. It doesn't.", cashier.marketBills.size(), 1);
		assertTrue("Cashier's scheduler should have returned true (paying bill), but didn't.", cashier.pickAndExecuteAnAction());
		assertTrue("MockMarket's event log should read \"Received PayBill. Total = 12\" after the Cashier's scheduler is called. Instead, the MockCustomer's event log reads: " + market.log.toString(), market.log.containsString("Received PayBill. Total = 12"));
		assertEquals("Cashier should have 0 market bills in it. It doesn't.", cashier.marketBills.size(), 0);
		assertFalse("Cashier's scheduler should have returned false (no action), but didn't.", cashier.pickAndExecuteAnAction());
	}
	/**
	 * This tests the cashier/market interaction where the bill is split between two markets.
	 */
	public void testSixSplitMarketBill()
	{
		market.cashier = cashier;
		
		//check preconditions
		assertEquals("Cashier should have 0 market bills in it. It doesn't.", cashier.marketBills.size(), 0);
		assertEquals("CashierAgent should have an empty event log before the Cashier's HereIsMBill is called. Instead, the Cashier's event log reads: " + cashier.log.toString(), 0, cashier.log.size());
		//test
		cashier.msgHereIsMBill(market1, 12);
		cashier.msgHereIsMBill(market2, 12);
		//check postconditions
		assertEquals("MockMarket1 should have an empty event log before the Cashier's scheduler is called. Instead, MockMarket1's event log reads: " + market1.log.toString(), 0, market1.log.size());
		assertEquals("MockMarket2 should have an empty event log before the Cashier's scheduler is called. Instead, MockMarket2's event log reads: " + market2.log.toString(), 0, market2.log.size());
		assertEquals("Cashier should have 2 market bills in it. It doesn't.", cashier.marketBills.size(), 2);
		assertTrue("Cashier's scheduler should have returned true (paying bill), but didn't.", cashier.pickAndExecuteAnAction());
		assertTrue("MockMarket1's event log should read \"Received PayBill. Total = 12\" after the Cashier's scheduler is called for the first time. Instead, MockMarket1's event log reads: " + market1.log.toString(), market1.log.containsString("Received PayBill. Total = 12"));
		assertEquals("MockMarket2 should have an empty event log before the Cashier's scheduler is called for the first time. Instead, MockMarket2's event log reads: " + market2.log.toString(), 0, market2.log.size());
		assertEquals("Cashier should have 1 market bill in it. It doesn't.", cashier.marketBills.size(), 1);
		assertTrue("Cashier's scheduler should have returned true (paying bill), but didn't.", cashier.pickAndExecuteAnAction());
		assertEquals("MockMarket1 should have an event log of size 1 after the Cashier's scheduler is called for the second time. Instead, MockMarket1's event log is size " + market1.log.size(), 1, market1.log.size());
		assertTrue("MockMarket2's event log should read \"Received PayBill. Total = 12\" after the Cashier's scheduler is called for the second time. Instead, MockMarket2's event log reads: " + market2.log.toString(), market2.log.containsString("Received PayBill. Total = 12"));
		assertEquals("Cashier should have 0 market bills in it. It doesn't.", cashier.marketBills.size(), 0);
		assertFalse("Cashier's scheduler should have returned false (no action), but didn't.", cashier.pickAndExecuteAnAction());
	}
}
