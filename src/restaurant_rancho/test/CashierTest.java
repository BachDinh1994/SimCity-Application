package restaurant_rancho.test;

import restaurant_rancho.RanchoCashier;
import restaurant_rancho.test.mock.MockCustomer;
import restaurant_rancho.test.mock.MockMarket;
import restaurant_rancho.test.mock.MockWaiter;
import junit.framework.*;


public class CashierTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	RanchoCashier cashier;
	MockWaiter waiter;
	MockCustomer customer1;
	MockCustomer customer2;
	MockMarket market1;
	MockMarket market2;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();		
		cashier = new RanchoCashier("cashier");		
		customer1 = new MockCustomer("mockcustomer1");	
		customer2 = new MockCustomer("mockcustomer2");
		waiter = new MockWaiter("mockwaiter");
		market1 = new MockMarket("mockmarket1");
		market2 = new MockMarket("mockmarket2");
	}	
	
	
	
	
	
	
	/**
	 * This tests the cashier under very simple terms: one customer is ready to pay the exact bill.
	 */
	public void testOneNormalCustomerScenario()
	{
		//setUp() runs first before this test!
		
		customer1.cashier = cashier;//You can do almost anything in a unit test.			
		
		//check preconditions	
		
		
		assertFalse("Cashier's scheduler should have returned false (no actions to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
		
		assertEquals("CashierAgent should have 50 as initial wealth but didn't.",cashier.getWealth(),50.00);
		
		
		
		
		//1st message
		
		cashier.msgComputeBill(waiter, "Steak", 1);
		
		assertEquals("CashierAgent should release statechange but didn't",cashier.getStateChange().availablePermits(),2);
		
		assertEquals("CashierAgent should record the correct waiter but didn't",cashier.getCheckList().get(0).getWaiter(),waiter);
		
		assertEquals("CashierAgent should record the correct price for Steak but didn't",cashier.getCheckList().get(0).getChoice(),"Steak");
		
		assertEquals("CashierAgent should record the correct table number but didn't",cashier.getCheckList().get(0).getTable(),1);
		
		assertTrue("Cashier's scheduler should have returned true (no actions to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
		
		
		
		
		
		//2nd message
		cashier.msgHereIsPayment(customer1, 12.00,20.00);
		
		assertEquals("CashierAgent should release statechange but didn't",cashier.getStateChange().availablePermits(),3);
		
		assertEquals("Cashier should record the correct customer, but it didn't.",cashier.payList.get(0).getCustomer(),customer1);
		
		assertEquals("Cashier should record the correct checkAmount, but it didn't.",cashier.payList.get(0).getCheckAmount(),12.00);
		
		assertEquals("Cashier should record the correct cash, but it didn't.",cashier.payList.get(0).getCash(),20.00);
		
		assertTrue("Cashier's scheduler should have returned true (no actions to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
		
		assertEquals("Cashier right now should have 50+12=62 dollars but didn't",cashier.getWealth(),62.00);
		
		
		
		
		
		//Final checking
		assertFalse("Cashier's scheduler should have returned false, but didn't.", 
					cashier.pickAndExecuteAnAction());
		

		assertTrue("MockWaiter should have logged an event for receiving \'msgCheckReady' with the correct balance, but his last event logged reads instead: " 
				+ waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("received msgCheckReady. check is 15.99 and table is 1"));
	
			
		assertTrue("MockCustomer should have logged \"received msgHereIsChange\" but didn't. His log reads instead: " 
				+ customer1.log.getLastLoggedEvent().toString(), customer1.log.containsString("received msgHereIsChange from cashier and change amount is 8.0"));
	
		
		
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		
	
	}//end one normal customer scenario
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * one order fulfilled by one market. Normal scenario
	 */
	public void testOneMarketNormalScenario(){
		//3rd message
				cashier.msgPayMarket(market1, "Steak", 2);
				
				assertEquals("CashierAgent should release statechange but didn't",cashier.getStateChange().availablePermits(),2);
				
				assertEquals("Cashier should record the correct market, but it didn't.",cashier.debtList.get(0).getMarket(),market1);
				
				assertEquals("Cashier should record 'Steak', but it didn't.",cashier.debtList.get(0).getChoice(),"Steak");
				
				assertEquals("Cashier should record the fulfill amount 2, but it didn't.",cashier.debtList.get(0).getAmount(),2);
				
				assertTrue("Cashier's scheduler should have returned true (no actions to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
				
				assertEquals("Cashier right now should have 50-31.98=18.02 dollars but didn't",cashier.getWealth(),18.02);
				
				
				assertFalse("Cashier's scheduler should have returned false, but didn't.", 
						cashier.pickAndExecuteAnAction());
				
				assertTrue("MockMarket should have logged msgHereIsPayment but didn't. His log reads instead: "
						+market1.log.getLastLoggedEvent().toString(), market1.log.containsString("received msgHereIsPayment and payment is 31.98 dollars."));
				
				assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
						cashier.pickAndExecuteAnAction());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * test whether the cashier works appropriately when one order is fulfilled by two markets
	 */
	
	public void testTwoMarketsNormalScenario(){
		//3rd message
				cashier.msgPayMarket(market1, "Steak", 1);
				
				assertEquals("CashierAgent should release statechange but didn't",cashier.getStateChange().availablePermits(),2);
				
				assertEquals("Cashier should record the correct market, but it didn't.",cashier.debtList.get(0).getMarket(),market1);
				
				assertEquals("Cashier should record 'Steak', but it didn't.",cashier.debtList.get(0).getChoice(),"Steak");
				
				assertEquals("Cashier should record the fulfill amount 1, but it didn't.",cashier.debtList.get(0).getAmount(),1);
				
				assertTrue("Cashier's scheduler should have returned true (no actions to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
				
				assertEquals("Cashier right now should have 50-15.99=34.01 dollars but didn't",cashier.getWealth(),34.01);
				
				
				assertFalse("Cashier's scheduler should have returned false, but didn't.", 
						cashier.pickAndExecuteAnAction());
				
				assertTrue("MockMarket should have logged msgHereIsPayment but didn't. His log reads instead: "
						+market1.log.getLastLoggedEvent().toString(), market1.log.containsString("received msgHereIsPayment and payment is 15.99 dollars."));
				
				assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
						cashier.pickAndExecuteAnAction());
				
				
				
				cashier.msgPayMarket(market2, "Steak", 1);
				
				assertEquals("CashierAgent should release statechange but didn't",cashier.getStateChange().availablePermits(),3);
				
				assertEquals("Cashier should record the correct market, but it didn't.",cashier.debtList.get(1).getMarket(),market2);
				
				assertEquals("Cashier should record 'Steak', but it didn't.",cashier.debtList.get(0).getChoice(),"Steak");
				
				assertEquals("Cashier should record the fulfill amount 1, but it didn't.",cashier.debtList.get(0).getAmount(),1);
				
				assertTrue("Cashier's scheduler should have returned true (no actions to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
				
				assertFalse("Cashier's scheduler should have returned false, but didn't.", 
						cashier.pickAndExecuteAnAction());
				
				assertTrue("MockMarket should have logged msgHereIsPayment but didn't. His log reads instead: "
						+market1.log.getLastLoggedEvent().toString(), market1.log.containsString("received msgHereIsPayment and payment is 15.99 dollars."));
				
				assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
						cashier.pickAndExecuteAnAction());
				
	}
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * test whether the cashier will stop paying the market if he is short of money
	 */
	
	public void testOneMarketShortOfMoneyScenario(){
		cashier.setWealth(0.00);
		
		cashier.msgPayMarket(market1, "Steak", 3);
		
		assertEquals("CashierAgent should release statechange but didn't",cashier.getStateChange().availablePermits(),2);
		
		assertEquals("Cashier should record the correct market, but it didn't.",cashier.debtList.get(0).getMarket(),market1);
		
		assertEquals("Cashier should record 'Steak', but it didn't.",cashier.debtList.get(0).getChoice(),"Steak");
		
		assertEquals("Cashier should record the fulfill amount 3, but it didn't.",cashier.debtList.get(0).getAmount(),3);
		
		assertTrue("Cashier's scheduler should have returned true (no actions to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
		
		
		assertFalse("Cashier's scheduler should have returned false, but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		assertTrue("MockMarket should have logged msgHereIsPayment but didn't. His log reads instead: "
				+market1.log.getLastLoggedEvent().toString(), !market1.log.getLastLoggedEvent().toString().contains("received msgHereIsPayment and payment is 47.97 dollars."));
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * test whether the cash will keep his word and pay off his debt as soon as he has enough money
	 */
	public void testOnePayOffDebtScenario(){
		cashier.setWealth(0.00);
		
		cashier.msgPayMarket(market2, "Steak", 2);
		
		assertEquals("CashierAgent should release statechange but didn't",cashier.getStateChange().availablePermits(),2);
		
		assertEquals("Cashier should record the correct market, but it didn't.",cashier.debtList.get(0).getMarket(),market2);
		
		assertEquals("Cashier should record 'Steak', but it didn't.",cashier.debtList.get(0).getChoice(),"Steak");
		
		assertEquals("Cashier should record the fulfill amount 2, but it didn't.",cashier.debtList.get(0).getAmount(),2);
		
		assertTrue("Cashier's scheduler should have returned true (no actions to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
		
		
		assertFalse("Cashier's scheduler should have returned false, but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		assertTrue("MockMarket should have logged msgHereIsPayment but didn't. His log reads instead: "
				+market2.log.getLastLoggedEvent().toString(), !market2.log.getLastLoggedEvent().toString().contains("received msgHereIsPayment and payment is 31.98 dollars."));
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		cashier.msgHereIsPayment(customer1, 40.00,50.00);
		
		
		
		assertEquals("CashierAgent should release statechange but didn't",cashier.getStateChange().availablePermits(),3);
		
		
		assertTrue("Cashier's scheduler should have returned true (no actions to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
		
		assertEquals("Cashier right now should have 40.00 dollars but didn't",cashier.getWealth(),40.00);
		
		assertTrue("Cashier's scheduler should have returned true, but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		
		assertTrue("MockMarket should have logged msgHereIsPayment but didn't. His log reads instead: "
				+market2.log.getLastLoggedEvent().toString(), market2.log.containsString("received msgHereIsPayment and payment is 31.98 dollars."));
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		
	}
	
	
	
	
	
	
	
	/*
	 * test whether msgHereIsPayments works for the scenario when there are multiple payments at the same time
	 */
	public void testMultiplePaymentsScenario(){
		
		
		
		
		cashier.msgHereIsPayment(customer1, 20.00, 30.00);
		
		assertEquals("CashierAgent should release statechange but didn't",cashier.getStateChange().availablePermits(),2);
		
		assertEquals("Cashier should record the correct customer, but it didn't.",cashier.payList.get(0).getCustomer(),customer1);
		
		assertEquals("Cashier should record the correct checkAmount, but it didn't.",cashier.payList.get(0).getCheckAmount(),20.00);
		
		assertEquals("Cashier should record the correct cash, but it didn't.",cashier.payList.get(0).getCash(),30.00);
		
		assertTrue("Cashier's scheduler should have returned true (no actions to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
		
		assertEquals("Cashier right now should have 50+20=70 dollars but didn't",cashier.getWealth(),70.00);
		
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
		
		cashier.msgHereIsPayment(customer2, 15.00, 20.00);
		
		assertEquals("CashierAgent should release statechange but didn't",cashier.getStateChange().availablePermits(),3);
		
		assertEquals("Cashier should record the correct customer, but it didn't.",cashier.payList.get(1).getCustomer(),customer2);
		
		assertEquals("Cashier should record the correct checkAmount, but it didn't.",cashier.payList.get(1).getCheckAmount(),15.00);
		
		assertEquals("Cashier should record the correct cash, but it didn't.",cashier.payList.get(1).getCash(),20.00);
		
		assertTrue("Cashier's scheduler should have returned true (no actions to do on a bill from a waiter), but didn't.", cashier.pickAndExecuteAnAction());
		
		assertEquals("Cashier right now should have 70+15=85 dollars but didn't",cashier.getWealth(),85.00);
	
		assertFalse("Cashier's scheduler should have returned false (no actions left to do), but didn't.", 
				cashier.pickAndExecuteAnAction());
	
	
	}
	
	
	
	
	
	
	
	
}
