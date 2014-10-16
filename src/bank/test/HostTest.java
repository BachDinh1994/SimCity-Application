package bank.test;

import bank.BankAccount;
import bank.BankHost;
import bank.test.mock.MockBankCustomer;
import bank.test.mock.MockRobber;
import bank.test.mock.MockTeller;
import junit.framework.*;

public class HostTest extends TestCase{
	private BankHost host;
	private MockBankCustomer customer1,customer2;
	private MockTeller teller1,passer;
	private MockRobber robber;
	
	
	public void setUp() throws Exception{
		super.setUp();
		
		host = new BankHost("host");
		customer1 = new MockBankCustomer("customer");
		customer2 = new MockBankCustomer("customer2");
		teller1 = new MockTeller("teller");
		passer = new MockTeller("passer");
		robber = new MockRobber("armed");
	
	}
	/*
	 * This tests the Host in a condition where a customer gets in line, and a teller subsequently becomes free. 
	 * The banker should create a new account for the customer. 
	 */
	public void testOneNewCustomerOneTeller(){
		//Preconditions
		host.setWorking(true);
		
		assertEquals("Host's log should be empty, instead it reads: " + host.log.toString(), 0, host.log.size());
		assertEquals("Host should have no customers in its list, instead it has " + host.customers.size(), 0, host.customers.size());
		assertEquals("Host should have no tellers in its list, instead it has " + host.tellers.size(), 0, host.tellers.size());
		assertEquals("Host should have no accounts in it, but instead it has " + host.accounts.size(), 0, host.accounts.size());
		
		assertEquals("MockBankCustomer's log should be empty instead it reads: " + customer1.log.toString(), 0, customer1.log.size());
		assertEquals("MockTeller's log should be empty, instead it reads: " + teller1.log.toString(), 0, teller1.log.size());
		
		//Step 1 of test
		//msgJoiningLine(BankCustomer)
		host.msgJoiningLine(customer1);
		
		//Post-conditions of step 1		
		assertTrue("Host's log should contain \"Added customer to waiting line\", but instead it reads: " + host.log.getLastLoggedEvent().toString(),host.log.containsString("Added customer to waiting line"));
		assertEquals("Host's list of customers should have one entry in it, but instead it has " + host.customers.size(), 1, host.customers.size());
		
		//Step 2 of test 
		assertFalse("Host's scheduler should return false, but it didn't", host.pickAndExecuteAnAction());
		
		//Step 3 of test
		//msgImFree(Banker)
		host.msgImFree(teller1);
		
		//Post-conditions of step 3 & Pre-conditions of step 4 
		assertTrue("Host's log should contain \"Added teller to available tellers\", but instead it reads: " + host.log.getLastLoggedEvent().toString(),host.log.containsString("Added teller to available tellers"));
		
		assertEquals("Host should have one customer in its list, instead it has " + host.customers.size(), 1, host.customers.size());
		assertEquals("Host should have one teller in its list, instead it has " + host.tellers.size(), 1, host.tellers.size());
		
		//Step 4 of test 
		assertTrue("Host's scheduler should return true, but it didn't", host.pickAndExecuteAnAction());
		
		//Post-conditions of step 4
		assertEquals("Host should have no customers in its list, instead it has " + host.customers.size(), 0, host.customers.size());
		assertEquals("Host should have no tellers in its list, instead it has " + host.tellers.size(), 0, host.tellers.size());
		assertTrue("Customer's log should contain \"Got message to go see banker\", but instead it reads: " + customer1.log.getLastLoggedEvent().toString(), customer1.log.containsString("Got message to go see banker"));
		assertTrue("Teller's log should contain \"Added new account with 0.0 dollars\", but instead it reads: " + teller1.log.getLastLoggedEvent().toString(),teller1.log.containsString("Added new account with 0.0 dollars"));
	}
	
	/*
	 * This tests a customer returning to perform some transaction to his account
	 */
	public void testOneReturningCustomerOneTeller(){
		//Preconditions
		host.setWorking(true);
		
		assertEquals("Host's log should be empty, instead it reads: " + host.log.toString(), 0, host.log.size());
		assertEquals("Host should have no customers in its list, instead it has " + host.customers.size(), 0, host.customers.size());
		assertEquals("Host should have no tellers in its list, instead it has " + host.tellers.size(), 0, host.tellers.size());
		assertEquals("Host should have no accounts in it, but instead it has " + host.accounts.size(), 0, host.accounts.size());
		
		assertEquals("MockBankCustomer's log should be empty instead it reads: " + customer1.log.toString(), 0, customer1.log.size());
		assertEquals("MockTeller's log should be empty, instead it reads: " + teller1.log.toString(), 0, teller1.log.size());

		//Step 1 of test 
		//msgImFree(Banker,BankAccount)
		host.msgImFree(teller1, new BankAccount(customer1, 0));
		
		//Post-conditions of step 1
		assertEquals("Host should have 1 teller in its list, instead it has " + host.tellers.size(), 1, host.tellers.size());
		assertEquals("Host should have 1 account in its list, instead it has " + host.accounts.size(),1,host.accounts.size());
		
		//Step 2 of test
		assertFalse("Host's scheduler should return false, but it didn't.", host.pickAndExecuteAnAction());
		
		//Step 3 of test
		//msgJoiningLine(BCustomer)
		host.msgJoiningLine(customer1);
		
		//Post-conditions of step 3
		assertEquals("Host should have 1 customer in its list, instead it has " + host.customers.size(),1,host.customers.size());
		
		//Step 4 of test 
		assertTrue("Host's scheduler should have returned true, but it didn't", host.pickAndExecuteAnAction());
		
		//Post-conditions of step 4
		assertEquals("Host should have no customers in its list, instead it has " + host.customers.size(), 0, host.customers.size());
		assertEquals("Host should have no tellers in its list, instead it has " + host.tellers.size(), 0, host.tellers.size());
		assertEquals("Host should still have an account in its list, instead it has " + host.accounts.size(), 1, host.accounts.size());
		assertTrue("Customer's log should contain \"Got message to go see banker\", but instead it reads: " + customer1.log.getLastLoggedEvent().toString(), customer1.log.containsString("Got message to go see banker"));
		assertTrue("Teller's log should contain \"Updated accounts with new information\", but instead it reads: " + teller1.log.getLastLoggedEvent().toString(),teller1.log.containsString("Updated accounts with new information"));
		
	}
	/*
	 * This tests the Host in a condition where two customers enter the line, and a teller must be assigned to deal with each in succession
	 */
	public void testTwoCustomerOneTeller(){
		//Preconditions
			host.setWorking(true);
			
			assertEquals("Host's log should be empty, instead it reads: " + host.log.toString(), 0, host.log.size());
			assertEquals("Host should have no customers in its list, instead it has " + host.customers.size(), 0, host.customers.size());
			assertEquals("Host should have no tellers in its list, instead it has " + host.tellers.size(), 0, host.tellers.size());
			
			assertEquals("MockBankCustomer's log should be empty instead it reads: " + customer1.log.toString(), 0, customer1.log.size());
			assertEquals("MockTeller's log should be empty, instead it reads: " + passer.log.toString(), 0, passer.log.size());
			
			//Step 1 of test
			//msgJoiningLine(BankCustomer)
			host.msgJoiningLine(customer1);
			
			//Post-conditions of step 1		
			assertTrue("Host's log should contain \"Added customer to waiting line\", but instead it reads: " + host.log.getLastLoggedEvent().toString(),host.log.containsString("Added customer to waiting line"));
			assertEquals("Host's list of customers should have one entry in it, but instead it has " + host.customers.size(), 1, host.customers.size());
			
			//Step 2 of test
			//msgJoiningLine(BankCustomer)
			host.msgJoiningLine(customer2);

			//Post-conditions of step 2
			assertTrue("Host's log should contain \"Added customer to waiting line\", but instead it reads: " + host.log.getLastLoggedEvent().toString(),host.log.containsString("Added customer to waiting line"));
			assertEquals("Host's list of customers should have two entries in it, but instead it has " + host.customers.size(), 2, host.customers.size());
			
			//Step 3 of test 
			assertFalse("Host's scheduler should return false, but it didn't", host.pickAndExecuteAnAction());
			

			//Post-conditions of step 3
			assertTrue("First customer's log should contain \"Told to wait in line at position 0\", instead it reads: " + customer1.log.getLastLoggedEvent().toString(),customer1.log.containsString("Told to wait in line at position 0"));
			assertTrue("Second customer's log should contain \"Told to wait in line at position 1\", instead it reads: " + customer2.log.getLastLoggedEvent().toString(),customer2.log.containsString("Told to wait in line at position 1"));
			
			//Step 4 of test
			//msgImFree(Banker)
			host.msgImFree(passer);
			
			//Post-conditions of step 3 & Pre-conditions of step 4 
			assertTrue("Host's log should contain \"Added teller to available tellers\", but instead it reads: " + host.log.getLastLoggedEvent().toString(),host.log.containsString("Added teller to available tellers"));
			
			assertEquals("Host should have two customers in its list, instead it has " + host.customers.size(), 2, host.customers.size());
			assertEquals("Host should have one teller in its list, instead it has " + host.tellers.size(), 1, host.tellers.size());
			
			//Step 4 of test 
			assertTrue("Host's scheduler should return true, but it didn't", host.pickAndExecuteAnAction());
			
			//Post-conditions of step 4
			assertEquals("Host should have one customer in its list, instead it has " + host.customers.size(), 1, host.customers.size());
			assertEquals("Host should have no tellers in its list, instead it has " + host.tellers.size(), 0, host.tellers.size());
			assertTrue("Teller's log should contain \"Added new account with 0.0 dollars\", but instead it reads: " + passer.log.getLastLoggedEvent().toString(), passer.log.containsString("Added new account with 0.0 dollars"));
			assertTrue("Customer's log should contain \"Got message to go see banker\", but instead it reads: " + customer1.log.getLastLoggedEvent().toString(), customer1.log.containsString("Got message to go see banker"));
			
			//Step 5 of test
			assertFalse("Host's scheduler should have returned false, but it didn't", host.pickAndExecuteAnAction());
			
			//Post-conditions of step 5
			assertTrue("Customer's log should contain \"Told to wait in line at position 0\", instead it reads: " + customer2.log.getLastLoggedEvent().toString(),customer2.log.containsString("Told to wait in line at position 0"));
			
			//Step 6 of test
			//msgImFree(Banker)
			host.msgImFree(passer);
			
			//Post-conditions of step 6 & Pre-conditions of step 7 
			assertTrue("Host's log should contain \"Added teller to available tellers\", but instead it reads: " + host.log.getLastLoggedEvent().toString(),host.log.containsString("Added teller to available tellers"));
			
			assertEquals("Host should have one customer in its list, instead it has " + host.customers.size(), 1, host.customers.size());
			assertEquals("Host should have one teller in its list, instead it has " + host.tellers.size(), 1, host.tellers.size());
			
			//Step 7 of test 
			assertTrue("Host's scheduler should return true, but it didn't", host.pickAndExecuteAnAction());
			
			//Post-conditions of step 7
			assertEquals("Host should have no customers in its list, instead it has " + host.customers.size(), 0, host.customers.size());
			assertEquals("Host should have no tellers in its list, instead it has " + host.tellers.size(), 0, host.tellers.size());
			assertTrue("Second customer's log should contain \"Got message to go see banker\", but instead it reads: " + customer2.log.getLastLoggedEvent().toString(), customer2.log.containsString("Got message to go see banker"));
			assertTrue("Second teller's log should contain \"Added new account with 0.0 dollars\", but instead it reads: " + passer.log.getLastLoggedEvent().toString(), passer.log.containsString("Added new account with 0.0 dollars"));
	}
	/*
	 * This tests the Host in a condition where two available hosts must deal with two available customers. It inverts the order seen
	 * in the other tests and also makes sure the Host can appropriately delegate tasks to multiple tellers.
	 */
	public void testOneTellerTwoCustomer(){
		//Preconditions
		host.setWorking(true);
		
		assertEquals("Host's log should be empty, instead it reads: " + host.log.toString(), 0, host.log.size());
		assertEquals("Host should have no customers in its list, instead it has " + host.customers.size(), 0, host.customers.size());
		assertEquals("Host should have no tellers in its list, instead it has " + host.tellers.size(), 0, host.tellers.size());
		assertEquals("Host should have no accounts in its list, instead it has " + host.accounts.size(), 0, host.accounts.size());
		
		assertEquals("MockBankCustomer's log should be empty instead it reads: " + customer1.log.toString(), 0, customer1.log.size());
		assertEquals("MockTeller's log should be empty, instead it reads: " + teller1.log.toString(), 0, teller1.log.size());
		
		//Step 1 of test
		//msgImFree(Banker)
		host.msgImFree(teller1);
		
		//Post-conditions of Step 1 
		assertTrue("Host's log should contain \"Added teller to available tellers\", but instead it reads: " + host.log.getLastLoggedEvent().toString(),host.log.containsString("Added teller to available tellers"));
		
		assertEquals("Host should have no customers in its list, instead it has " + host.customers.size(), 0, host.customers.size());
		assertEquals("Host should have one teller in its list, instead it has " + host.tellers.size(), 1, host.tellers.size());
		
		//Step 2 of test
		//msgImFree(Banker)
		host.msgImFree(passer);
		
		//Post-conditions of Step 2
		assertTrue("Host's log should contain \"Added teller to available tellers\", but instead it reads: " + host.log.getLastLoggedEvent().toString(),host.log.containsString("Added teller to available tellers"));
		
		assertEquals("Host should have no customers in its list, instead it has " + host.customers.size(), 0, host.customers.size());
		assertEquals("Host should have two tellers in its list, instead it has " + host.tellers.size(), 2, host.tellers.size());
		
		//Step 3 of test
		assertFalse("Host's scheduler should return false, but it didn't.", host.pickAndExecuteAnAction());
		
		//Step 4 of test 
		host.msgJoiningLine(customer1);
		
		//Post-conditions of Step 4
		assertTrue("Host's log should contain \"Added customer to waiting line\", but instead it reads: " + host.log.getLastLoggedEvent().toString(),host.log.containsString("Added customer to waiting line"));
		assertEquals("Host's list of customers should have one entry in it, but instead it has " + host.customers.size(), 1, host.customers.size());
		
		//Step 5 of test 
		assertTrue("Host's scheduler should return true, but it didn't.", host.pickAndExecuteAnAction());
		
		//Post-conditions of Step 5
		assertEquals("Host should have no customers in its list, instead it has " + host.customers.size(), 0, host.customers.size());
		assertEquals("Host should have one teller in its list, instead it has " + host.tellers.size(), 1, host.tellers.size());
		assertTrue("Customer's log should contain \"Got message to go see banker\", but instead it reads: " + customer1.log.getLastLoggedEvent().toString(), customer1.log.containsString("Got message to go see banker"));
		
		//Step 6 of test 
		host.msgJoiningLine(customer2);
		
		//Post-conditions of Step 6
		assertTrue("Host's log should contain \"Added customer to waiting line\", but instead it reads: " + host.log.getLastLoggedEvent().toString(),host.log.containsString("Added customer to waiting line"));
		assertEquals("Host's list of customers should have one entry in it, but instead it has " + host.customers.size(), 1, host.customers.size());
		
		//Step 7 of test 
		assertTrue("Host's scheduler should return true, but it didn't.", host.pickAndExecuteAnAction());
		
		//Post-conditions of Step 7
		assertEquals("Host should have no customers in its list, instead it has " + host.customers.size(), 0, host.customers.size());
		assertEquals("Host should have no tellers in its list, instead it has " + host.tellers.size(), 0, host.tellers.size());
		assertTrue("Customer's log should contain \"Got message to go see banker\", but instead it reads: " + customer2.log.getLastLoggedEvent().toString(), customer2.log.containsString("Got message to go see banker"));
		
	}
	/*
	 * A robbery occurs while there are no customers in the bank. Makes sure that the host appropriately interacts with the robber.
	 */
	public void testSimpleRobbery(){
		//Preconditions
		host.setWorking(true);
		
		assertEquals("Host's log should be empty, instead it reads: " + host.log.toString(), 0, host.log.size());
		assertEquals("Host should have no customers in its list, instead it has " + host.customers.size(), 0, host.customers.size());
		assertEquals("Host should have no tellers in its list, instead it has " + host.tellers.size(), 0, host.tellers.size());
		assertEquals("Host should have no accounts in it, but instead it has " + host.accounts.size(), 0, host.accounts.size());
		
		//Step 0 [Precondition fulfillment]
		//msgImFree(Banker)
		host.msgImFree(teller1);
		
		//Post-condition of Step 0
		assertEquals("Host should have one teller in its list, instead it has " + host.tellers.size(), 1, host.tellers.size());
		
		//Step 1 
		//msgGiveMeBankers(Robber)
		host.msgGiveMeBankers(robber);
		
		//Post-conditions of Step 1, pre-conditions of step 2
		assertFalse("Host should no longer be working. He is.", host.isWorking());
		
		//Step 2 
		assertTrue("Host's scheduler should return true, but it didn't.", host.pickAndExecuteAnAction());
		
		//Post-conditions of Step 2, pre-conditions of step 3
		assertTrue("Robber's log should contain \"Got list with 1 bankers\", but instead it reads: " + robber.log.getLastLoggedEvent().toString(),
				robber.log.containsString("Got list with 1 bankers"));
		
		//Step 3 
		//msgResumeActivity()
		host.msgResumeActivity();
		
		assertTrue("Host should resume working, but he hasn't.",host.isWorking());
		
		
	}
	/*
	 * A robbery occurs while there are customers in the bank. Makes sure that the host appropriately suspends interactions while being robbed.
	 */	
	public void testComplexRobbery(){
		//Preconditions
		host.setWorking(true);
		
		assertEquals("Host's log should be empty, instead it reads: " + host.log.toString(), 0, host.log.size());
		assertEquals("Host should have no customers in its list, instead it has " + host.customers.size(), 0, host.customers.size());
		assertEquals("Host should have no tellers in its list, instead it has " + host.tellers.size(), 0, host.tellers.size());
		assertEquals("Host should have no accounts in it, but instead it has " + host.accounts.size(), 0, host.accounts.size());
		
		//Step 0 [Precondition fulfillment]
		//msgImFree(Banker)
		host.msgImFree(teller1);
		
		//Post-condition of Step 0
		assertEquals("Host should have one teller in its list, instead it has " + host.tellers.size(), 1, host.tellers.size());
		
		//Step 1 
		//msgJoiningLine(BCustomer)
		host.msgJoiningLine(customer1);
		
		//Post-conditions of Step 1
		assertTrue("Host's log should contain \"Added customer to waiting line\", but instead it reads: " + host.log.getLastLoggedEvent().toString(),
				host.log.containsString("Added customer to waiting line"));
		assertEquals("Host's list of customers should have one entry in it, but instead it has " + host.customers.size(), 
				1, host.customers.size());
				
		//Step 2
		//msgGiveMeBankers(Robber)
		host.msgGiveMeBankers(robber);
		
		//Post-conditions of Step 2, pre-conditions of step 3
		assertFalse("Host should no longer be working. He is.", host.isWorking());
		
		//Step 3 
		assertTrue("Host's scheduler should return true, but it didn't.", host.pickAndExecuteAnAction());
		
		//Post-conditions of Step 2, pre-conditions of step 3
		assertTrue("Robber's log should contain \"Got list with 1 bankers\", but instead it reads: " + robber.log.getLastLoggedEvent().toString(),
				robber.log.containsString("Got list with 1 bankers"));
		assertFalse("Host should still not be working, but he is.",host.isWorking());
		
		//Step 4 
		//msgJoiningLine(BCustomer)
		host.msgJoiningLine(customer2);
		
		//Post-conditions of Step 4
		assertTrue("Host's log should contain \"Added customer to waiting line\", but instead it reads: " + host.log.getLastLoggedEvent().toString(),
				host.log.containsString("Added customer to waiting line"));
		assertEquals("Host's list of customers should have two entries in it, but instead it has " + host.customers.size(), 
				2, host.customers.size());
		
		//Step 5 
		assertFalse("Host's scheduler should return false, but it didn't.", host.pickAndExecuteAnAction());
		
		//No post-conditions to check
		
		//Step 6
		//msgResumeActivity()
		host.msgResumeActivity();
		
		//Post-conditions of step 6, pre-conditions of step 7
		assertTrue("Host should resume working, but he hasn't.",host.isWorking());
		
		//Step 7
		assertTrue("Host's scheduler should return true, but it didn't.", host.pickAndExecuteAnAction());
	}
}
