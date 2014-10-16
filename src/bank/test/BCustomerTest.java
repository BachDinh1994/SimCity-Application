package bank.test;

import junit.framework.TestCase;
import bank.BankCustomer;
import bank.gui.BCustomerGui;
import bank.test.mock.MockBankHost;
import bank.test.mock.MockTeller;

public class BCustomerTest extends TestCase{
	BankCustomer customer;
	BCustomerGui myGui;
	MockBankHost host;
	MockTeller teller, lender;
	
	public void setUp() throws Exception{
		super.setUp();
		
		customer = new BankCustomer("test");
		host = new MockBankHost("host");
		teller = new MockTeller("teller");
		lender = new MockTeller("lender");
	
		myGui = new BCustomerGui(customer);
	}
	/*
	 * In this test, the customer deposits money into his account 
	 */
	public void testOneDepositScenario(){
		//Pre-conditions
		customer.setHost(host);
		customer.setGui(myGui);
		customer.setMoneyVariables(100, 10); //The Customer has 100 dollars on him, but only needs 10. So he should deposit 90.
		teller.host = host;
		
		
		//Check all the logs
		assertEquals("Customer's log should be empty, but it isn't.", 0, customer.log.size());
		assertEquals("Customer's state should be set to None, instead it is " + customer.getState(), "None", customer.getState());
		
		assertEquals("Host's log should be empty, but it isn't.", 0, host.log.size());
		assertEquals("Teller's log should be empty, but it isn't.", 0, teller.log.size());
		
		
		
		//Step 1 of test
		//msgGoSeeTeller(Banker)
		customer.msgGoSeeTeller(teller);
		
		//Post-conditions of step 1 
		assertTrue("Customer's log should contain \"Got message to see teller\", but instead it reads: " + customer.log.getLastLoggedEvent().toString(), 
				customer.log.containsString("Got message to see teller"));
		assertEquals("Customer's state should have changed to Entering, but instead it is " + customer.getState(), 
				"Entering", customer.getState());
		
		//Step 2 of test
		assertTrue("Customer's scheduler should have returned true, but it didn't.", customer.pickAndExecuteAnAction());
		
		//Post-conditions of step 2
		assertTrue("Teller's log should contain \"Got request to change an account balance\", instead it reads " + teller.log.getLastLoggedEvent().toString(), teller.log.containsString("Got request to change an account balance"));
		assertEquals("Customer's state should have changed to Leaving, but instead it is " + customer.getState(), 
				"Leaving", customer.getState());
		assertTrue("Customer's log should contain \"Got cash, leaving\", but instead it is " + customer.log.getLastLoggedEvent().toString(), 
				customer.log.containsString("Got cash, leaving"));
		assertEquals("Customer's cash should now be 10.0, instead it is " + customer.getCash(), 10.0, customer.getCash());
		
		
	}
	
	/*
	 * In this scenario, a customer arrives with not enough money to last him the week, so he withdraws money from his account.
	 */
	public void testOneWithdrawalScenario(){
		//Pre-conditions
		customer.setHost(host);
		customer.setGui(myGui);
		customer.setMoneyVariables(10, 100); //The Customer has 10 dollars on him, but needs 100. So he should withdraw 90.
		teller.host = host;
		
		
		//Check all the logs
		assertEquals("Customer's log should be empty, but it isn't.", 0, customer.log.size());
		assertEquals("Customer's state should be set to None, instead it is " + customer.getState(), "None", customer.getState());
		
		assertEquals("Host's log should be empty, but it isn't.", 0, host.log.size());
		assertEquals("Teller's log should be empty, but it isn't.", 0, teller.log.size());
		
		
		
		//Step 1 of test
		//msgGoSeeTeller(Banker)
		customer.msgGoSeeTeller(teller);
		
		//Post-conditions of step 1 
		assertTrue("Customer's log should contain \"Got message to see teller\", but instead it reads: " + customer.log.getLastLoggedEvent().toString(), 
				customer.log.containsString("Got message to see teller"));
		assertEquals("Customer's state should have changed to Entering, but instead it is " + customer.getState(),
				"Entering", customer.getState());
		
		//Step 2 of test
		assertTrue("Customer's scheduler should have returned true, but it didn't.", customer.pickAndExecuteAnAction());
		
		//Post-conditions of step 2
		assertTrue("Teller's log should contain \"Got request to change an account balance\", instead it reads " + teller.log.getLastLoggedEvent().toString(), teller.log.containsString("Got request to change an account balance"));
		assertEquals("Customer's state should have changed to Leaving, but instead it is " + customer.getState(),
				"Leaving", customer.getState());
		assertTrue("Customer's log should contain \"Got cash, leaving\", but instead it is " + customer.log.getLastLoggedEvent().toString(), 
				customer.log.containsString("Got cash, leaving"));
		assertEquals("Customer's cash should now be 100.0, instead it is " + customer.getCash(), 100.0, customer.getCash());
		
	}
	
	/*
	 * In this test, the customer lacks the money necessary for the week, and also lacks the money necessary in his account. So he takes a loan.
	 * Then, we allow the customer to come back with more money and repay the loan.
	 */
	public void testOneLoanScenario(){
		//Pre-conditions
		customer.setHost(host);
		customer.setGui(myGui);
		customer.setMoneyVariables(0, 100); //The Customer has no dollars on him, but needs 100. So he should take out a loan 100.
		lender.host = host;
		
		
		//Check all the logs
		assertEquals("Customer's log should be empty, but it isn't.", 0, customer.log.size());
		assertEquals("Customer's state should be set to None, instead it is " + customer.getState(), "None", customer.getState());
		
		assertEquals("Host's log should be empty, but it isn't.", 0, host.log.size());
		assertEquals("Teller's log should be empty, but it isn't.", 0, lender.log.size());
		
		
		
		//Step 1 of test
		//msgGoSeeTeller(Banker)
		customer.msgGoSeeTeller(lender);
		
		//Post-conditions of step 1 
		assertTrue("Customer's log should contain \"Got message to see teller\", but instead it reads: " + customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Got message to see teller"));
		assertEquals("Customer's state should have changed to Entering, but instead it is " + customer.getState(), "Entering", customer.getState());
		
		//Step 2 of test
		assertTrue("Customer's scheduler should have returned true, but it didn't.", customer.pickAndExecuteAnAction());
		
		//Post-conditions of step 2
		assertTrue("Teller's log should contain \"Got request to change an account balance\", instead it reads " + lender.log.getLastLoggedEvent().toString(),
				lender.log.containsString("Got request to change an account balance"));
		assertEquals("Customer's state should have changed to ConsideringLoan, but instead it is " + customer.getState(), 
				"ConsideringLoan", customer.getState());
		assertTrue("Customer's log should contain \"Thinking of a loan\", but instead it is " + customer.log.getLastLoggedEvent().toString(),
				customer.log.containsString("Thinking of a loan"));
		
		//Step 3 of test 
		assertTrue("Customer's scheduler should have returned true, but it didn't.", customer.pickAndExecuteAnAction());
		
		//Post-conditions of step 3
		assertTrue("Teller's log should contain \"Received request for loan of 100.0 dollars\", but instead it reads: " + lender.log.getLastLoggedEvent().toString(), 
				lender.log.containsString("Received request for loan of 100.0 dollars"));
		assertEquals("Customer should have 100.0 dollars, but instead it has " + customer.getCash(),100.0,customer.getCash());
		assertEquals("Customer's state should be Leaving, instead it is " + customer.getState(), "Leaving", customer.getState());
		
		//Pre-conditions of step 4
		customer.setMoneyVariables(100, 0);//Customer now has enough money to pay the loan back, so the next run-through should end the loan
		
		//step 4 of test
		//msgGoSeeTeller(Banker)
		customer.msgGoSeeTeller(lender);
		
		//Post-conditions of step 4
		assertEquals("Customer's state should have changed to Entering, but instead it is " + customer.getState(),
				"Entering", customer.getState());
		
		//step 5 of test 
		assertTrue("Customer's scheduler should have returned true, but it didn't.", customer.pickAndExecuteAnAction());
		
		//Post-conditions of step 5
		assertEquals("Customer should have no more cash, instead it has " + customer.getCash(),0.0,customer.getCash());
		assertTrue("Customer's log should contain \"Got cash, leaving\", but instead it is " + customer.log.getLastLoggedEvent().toString(), 
				customer.log.containsString("Got cash, leaving"));
		assertEquals("Customer's state should be Leaving, instead it is " + customer.getState(), "Leaving", customer.getState());
	}
}
