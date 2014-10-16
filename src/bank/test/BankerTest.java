package bank.test;

import java.util.ArrayList;
import java.util.HashMap;

import bank.BankAccount;
import bank.BankAgent;
import bank.interfaces.BCustomer;
import bank.test.mock.MockBankCustomer;
import bank.test.mock.MockBankHost;
import bank.test.mock.MockRobber;
import junit.framework.TestCase;

public class BankerTest extends TestCase {
	private BankAgent teller;
	private MockBankCustomer deposit,withdraw,poor;
	private MockBankHost host;
	private MockRobber robber;
	
	public void setUp() throws Exception{
		super.setUp();
		
		teller = new BankAgent("teller");
		deposit = new MockBankCustomer("deposit");
		withdraw = new MockBankCustomer("withdraw");
		poor = new MockBankCustomer("poor");
		host = new MockBankHost("host");
		robber = new MockRobber("armed");
	}
	/*
	 * In this test, the teller is responsible for interacting with a new customer, opening an account for them, and allowing a deposit
	 */
	public void testOneDeposit(){
		//Pre-conditions
		teller.setWorking(true);
		teller.setHost(host);
		
		assertEquals("Teller's state should be free, but instead it is: " + teller.getState(),"Free",teller.getState());
		assertEquals("Teller's state should be free, but instead it is: " + teller.getState(),"Free",teller.getState());
		assertEquals("Teller should have no pending transactions, but it has: " +teller.pendingTransactions.size(),0,teller.pendingTransactions.size());
		
		assertEquals("Teller should have no pending loans, but it has " + teller.pendingLoans.size(),0,teller.pendingTransactions.size());
		assertEquals("Teller should have no accountsListed, but it has " + teller.accountsListed.size(),0,teller.accountsListed.size());
		
		//Step 0 of test - Host sends the teller a new account for the incoming customer
		teller.msgHereIsAccount(new BankAccount(deposit,0));
		
		//Post-conditions of step 0 
		assertEquals("Teller should have one account listed, instead it has " + teller.accountsListed.size(),1,teller.accountsListed.size());
		assertEquals("Teller's only account should have a balance of 0, instead it has " + teller.accountsListed.get(0).getBalance(), 
				0.0,teller.accountsListed.get(0).getBalance());
		
		//Step 1 of test
		//msgGoSeeTeller(Banker)
		deposit.msgGoSeeTeller(teller);
		
		//Post-conditions of step 1 
		assertEquals("Teller's state should be busy, but instead it is: " + teller.getState(),"Busy",teller.getState());
		assertEquals("Teller should have one pending transaction, but it has: " +teller.pendingTransactions.size(),
				1,teller.pendingTransactions.size());
		assertEquals("Teller's transaction should be to deposit 100 dollars, instead it is for " + teller.pendingTransactions.get(deposit), 
				100.0, teller.pendingTransactions.get(deposit));
		
		//Step 2 of test
		assertTrue("Teller's scheduler should return true, but it doesn't.", teller.pickAndExecuteAnAction());
		
		//Post-conditions of step 2 
		assertEquals("Teller's state should be free, but instead it is: " + teller.getState(),"Free",teller.getState());
		assertEquals("Teller should have no pending transactions, but it has: " +teller.pendingTransactions.size(),0,teller.pendingTransactions.size());
		assertTrue("Host's log should have a message stating \"Updated account, and added teller\", instead it reads: " + host.log.getLastLoggedEvent().toString(), 
				host.log.containsString("Updated account, and added teller"));
		assertTrue("Customer's log should have a message stating \"Got 0.0 dollars, leaving bank.\", instead it reads: " + deposit.log.getLastLoggedEvent().toString(),
				deposit.log.containsString("Got 0.0 dollars, leaving bank."));
		assertEquals("Customer's account stored in teller should be updated with the correct balance of 100.0, instead it has " + teller.accountsListed.get(0).getBalance(), 100.0, teller.accountsListed.get(0).getBalance());
		
	}
	/*
	 * In this test, a returning customer makes a withdrawal from his account. 
	 */
	public void testOneWithdrawal(){
		//Pre-conditions
		teller.setWorking(true);
		teller.setHost(host);
		
		assertEquals("Teller's state should be free, but instead it is: " + teller.getState(),"Free",teller.getState());
		assertEquals("Teller's state should be free, but instead it is: " + teller.getState(),"Free",teller.getState());
		assertEquals("Teller should have no pending transactions, but it has: " +teller.pendingTransactions.size(),0,teller.pendingTransactions.size());
		
		assertEquals("Teller should have no pending loans, but it has " + teller.pendingLoans.size(),0,teller.pendingTransactions.size());
		assertEquals("Teller should have no accountsListed, but it has " + teller.accountsListed.size(),0,teller.accountsListed.size());
		
		//Step 0 of test - Teller has an account for this customer already 
		teller.accountsListed.add(new BankAccount(withdraw,100));
		
		//Post-conditions of step 0 
		assertEquals("Teller should have one account listed, instead it has " + teller.accountsListed.size(),1,teller.accountsListed.size());
		assertEquals("Teller's only account should have a balance of 100, instead it has " + teller.accountsListed.get(0).getBalance(), 
				100.0,teller.accountsListed.get(0).getBalance());
		
		//Step 1 of test
		//msgGoSeeTeller(Banker)
		withdraw.msgGoSeeTeller(teller);
		
		//Post-conditions of step 1 
		assertEquals("Teller's state should be busy, but instead it is: " + teller.getState(),"Busy",teller.getState());
		assertEquals("Teller should have one pending transaction, but it has: " +teller.pendingTransactions.size(),
				1,teller.pendingTransactions.size());
		assertEquals("Teller's transaction should be to withdraw 100 dollars, instead it is for " + teller.pendingTransactions.get(withdraw), 
				-100.0, teller.pendingTransactions.get(withdraw));
		
		//Step 2 of test
		assertTrue("Teller's scheduler should return true, but it doesn't.", teller.pickAndExecuteAnAction());
		
		//Post-conditions of step 2 
		assertEquals("Teller's state should be free, but instead it is: " + teller.getState(),"Free",teller.getState());
		assertEquals("Teller should have no pending transactions, but it has: " +teller.pendingTransactions.size(),0,teller.pendingTransactions.size());
		assertTrue("Host's log should have a message stating \"Updated account, and added teller\", instead it reads: " + host.log.getLastLoggedEvent().toString(), 
				host.log.containsString("Updated account, and added teller"));
		assertTrue("Customer's log should have a message stating \"Got 100.0 dollars, leaving bank.\", instead it reads: " + withdraw.log.getLastLoggedEvent().toString(),
				withdraw.log.containsString("Got 100.0 dollars, leaving bank."));
		assertEquals("Customer's account stored in teller should be updated with the correct balance of 0.0, instead it has " + teller.accountsListed.get(0).getBalance(), 
				0.0, teller.accountsListed.get(0).getBalance());
		
	}	
	
	public void testTakingALoan(){
		//Pre-conditions
		teller.setWorking(true);
		teller.setHost(host);
		
		assertEquals("Teller's state should be free, but instead it is: " + teller.getState(),"Free",teller.getState());
		assertEquals("Teller's state should be free, but instead it is: " + teller.getState(),"Free",teller.getState());
		assertEquals("Teller should have no pending transactions, but it has: " +teller.pendingTransactions.size(),0,teller.pendingTransactions.size());
		
		assertEquals("Teller should have no pending loans, but it has " + teller.pendingLoans.size(),0,teller.pendingTransactions.size());
		assertEquals("Teller should have no accountsListed, but it has " + teller.accountsListed.size(),0,teller.accountsListed.size());
		
		//Step 0 of test - Teller has an account for this customer already 
		teller.accountsListed.add(new BankAccount(poor,50));
		
		//Post-conditions of step 0 
		assertEquals("Teller should have one account listed, instead it has " + teller.accountsListed.size(),1,teller.accountsListed.size());
		assertEquals("Teller's only account should have a balance of 50, instead it has " + teller.accountsListed.get(0).getBalance(), 
				50.0,teller.accountsListed.get(0).getBalance());
		
		//Step 1 of test
		//msgGoSeeTeller(Banker)
		poor.msgGoSeeTeller(teller);
		
		//Post-conditions of step 1 
		assertEquals("Teller's state should be busy, but instead it is: " + teller.getState(),"Busy",teller.getState());
		assertEquals("Teller should have one pending transaction, but it has: " +teller.pendingTransactions.size(),
				1,teller.pendingTransactions.size());
		assertEquals("Teller's transaction should be to withdraw 150 dollars, instead it is for " + teller.pendingTransactions.get(poor), 
				-150.0, teller.pendingTransactions.get(poor));
		
		//Step 2 of test
		assertTrue("Teller's scheduler should return true, but it doesn't.", teller.pickAndExecuteAnAction());
		
		//Post-conditions of step 2 
		assertEquals("Teller's state should be Lending, but instead it is: " + teller.getState(),"Lending",teller.getState());
		assertEquals("Teller should have no pending transactions, but it has: " +teller.pendingTransactions.size(),0,teller.pendingTransactions.size());
		assertTrue("Customer's log should have a message stating \"Taking out loan for 100.0 dollars\", instead it reads: " + poor.log.getLastLoggedEvent().toString(),
				poor.log.containsString("Taking out loan for 100.0 dollars"));
		assertEquals("Customer's account stored in teller should be updated with the correct balance of 0.0, instead it has " + teller.accountsListed.get(0).getBalance(), 
				0.0, teller.accountsListed.get(0).getBalance());
		assertEquals("Teller should have one pending loan, but it has " + teller.pendingLoans.size(), 1, teller.pendingLoans.size());
		
		//Step 3 of test
		assertTrue("Teller's scheduler should return true, but it doesn't.", teller.pickAndExecuteAnAction());
		
		//Post-conditions of step 3
		assertEquals("Teller should have 1 pending loan, but it has " + teller.pendingLoans.size(), 1, teller.pendingLoans.size());
		assertFalse("The customer's account should be closed, but it isn't.", teller.accountsListed.get(0).getStatus());
		assertEquals("The account's moneyOwed should be 100.0, but instead it is " + teller.accountsListed.get(0).getLoanAmount(), 
				100.0, teller.accountsListed.get(0).getLoanAmount());
		assertTrue("Customer's log should contain \"Got 100.0 dollars, leaving bank.\", instead it reads: " + poor.log.getLastLoggedEvent().toString(),
				poor.log.containsString("Got 100.0 dollars, leaving bank."));		
	}
	
	public void testSimpleRobbery(){
		//Pre-conditions
		teller.setWorking(true);
		teller.setHost(host);
		host.addBanker(teller);
		
		assertEquals("Teller's state should be free, but instead it is: " + teller.getState(),"Free",teller.getState());
		assertEquals("Teller's state should be free, but instead it is: " + teller.getState(),"Free",teller.getState());
		assertEquals("Teller should have no pending transactions, but it has: " +teller.pendingTransactions.size(),0,teller.pendingTransactions.size());
		
		assertEquals("Teller should have no pending loans, but it has " + teller.pendingLoans.size(),0,teller.pendingTransactions.size());
		assertEquals("Teller should have no accountsListed, but it has " + teller.accountsListed.size(),0,teller.accountsListed.size());
		
		//Step 0 of test - Teller accounts loaded already 
		teller.accountsListed.add(new BankAccount(poor,50));
		teller.accountsListed.add(new BankAccount(deposit,100));
		
		//Step 1 of test
		teller.msgGetRobbed(robber);
		
		//Post-conditions of step 1, pre-conditions of step 2
		assertTrue("Teller's log should contain \"Got robber message\", instead it reads: " + teller.log.getLastLoggedEvent().toString(),
				teller.log.containsString("Got robber message"));
		assertEquals("Teller's state should be Robbed, but instead it is " + teller.getState(), "Robbed",teller.getState());
		
		//Step 2 of test
		assertTrue("Teller's scheduler should have returned true, but it didn't.", teller.pickAndExecuteAnAction());
		
		//Post-conditions of step 2, pre-conditions of step 3
		assertEquals("Teller's first account should have 0 balance, instead it has " + teller.accountsListed.get(0).getBalance(),
				0.0, teller.accountsListed.get(0).getBalance());
		assertEquals("Teller's second account should have 0 balance, instead it has " + teller.accountsListed.get(1).getBalance(),
				0.0, teller.accountsListed.get(0).getBalance());
		assertTrue("Host's log should have \"Drained account of 0.0\", instead it reads: " + host.log.getLastLoggedEvent().toString(), 
				host.log.containsString("Drained account of 0.0"));
		
		//Step 3 of test 
		//msgResumeActivity()
		host.msgResumeActivity();
		assertEquals("Teller's state should be Free, but instead it is " + teller.getState(), "Free",teller.getState());
		
		
	}
}
