package bank.test;

import bank.BankAgent;
import bank.BankRobber;
import bank.gui.BankRobberGUI;
import bank.test.mock.MockBankCustomer;
import bank.test.mock.MockBankHost;
import bank.test.mock.MockTeller;
import junit.framework.TestCase;

public class RobberTest extends TestCase{
	MockBankHost host;
	MockTeller first_teller;
	MockTeller second_teller;
	MockTeller third_teller;
	MockTeller slow_teller;
	BankRobber robber;
	BankRobberGUI gui;
	
	
	public void setUp() throws Exception{
		super.setUp();
		
		host = new MockBankHost("host");
		first_teller = new MockTeller("low");
		second_teller = new MockTeller("medium");
		third_teller = new MockTeller("high");
		slow_teller = new MockTeller("slow");
		
		robber = new BankRobber(host);
		robber.name = "test";
		
		gui = new BankRobberGUI(robber);
		robber.setGUI(gui);
	}
	/*
	 * In this test, the teller turns away an unarmed robber
	 */
	public void testUnarmedRobber(){
		//Setup
		robber.setArmed(false);
		//Pre-conditions
		assertEquals("Robber's log should be empty, but it isn't.",0,robber.log.size());
		assertEquals("Host's log should be empty, but it isn't.",0,host.log.size());
		
		assertEquals("Robber's state should be Entering, but instead it is " + robber.getAState().toString(), "Entering",robber.getAState());
		assertTrue("Robber should be unarmed, but he isn't", robber.getWState().equals("Unarmed"));
		
		//Step 1 
		assertTrue("Robber's scheduler should have returned true, but it didn't.", robber.pickAndExecuteAnAction());
		
		//Post-conditions of Step 1, pre-conditions of Step 2
		assertTrue("Host's log should read \"Turned away unarmed robber\", instead it reads: " + host.log.getLastLoggedEvent().toString(),
				host.log.containsString("Turned away unarmed robber"));
		assertTrue("Robber's log should read \"Robbery failed, leaving bank\", instead it reads: " + robber.log.getLastLoggedEvent().toString(),
				robber.log.containsString("Robbery failed, leaving bank"));
		assertEquals("Robber's state should be Leaving, but instead it is " + robber.getAState(),"Leaving",robber.getAState());
		
		//Step 2 
		assertTrue("Robber's scheduler should have returned true, but it didn't.", robber.pickAndExecuteAnAction());
		
		//Post-conditions of Step 2
		assertTrue("Host's log should read \"Done getting robbed\", instead it reads: " + host.log.getLastLoggedEvent().toString(),
				host.log.containsString("Done getting robbed"));
	}
	/*
	 * In this test, an armed robber absconds with the money of one teller. Using slow teller so we can test intermediate states.  
	 */
	public void testOneRobberSlowTeller(){
		//Setup
		robber.setArmed(true);
		host.addBanker(slow_teller);
		//Pre-conditions
		assertEquals("Robber's log should be empty, but it isn't.",0,robber.log.size());
		assertEquals("Host's log should be empty, but it isn't.",0,host.log.size());
		assertEquals("Teller's log should be empty, but isn't.",0,slow_teller.log.size());
		
		assertEquals("Robber's state should be Entering, but instead it is " + robber.getAState().toString(), "Entering",robber.getAState());
		assertTrue("Robber should be armed, but he isn't", robber.getWState().equals("Armed"));
		assertEquals("Robber should have no stolen cash, but he does.", 0.0, robber.getCash());
		
		assertEquals("Host should have one teller in his list of bankers, instead he has " + host.bankerList.size(),1,host.bankerList.size());
		
		//Step 1 
		assertTrue("Robber's scheduler should have returned true, but it didn't.", robber.pickAndExecuteAnAction());
		
		//Post-conditions of Step 1, pre-conditions of Step 2
		assertTrue("Host's log should read \"Got request for bankers, sending it\", instead it reads: " + host.log.getLastLoggedEvent().toString(),
				host.log.containsString("Got request for bankers, sending it"));
		assertTrue("Robber's log should read \"Got list of bankers, proceeding to rob\", instead it reads: " + robber.log.getLastLoggedEvent().toString(),
				robber.log.containsString("Got list of bankers, proceeding to rob"));
		assertEquals("Robber's state should be Robbing, but instead it is " + robber.getAState(),"Robbing",robber.getAState());
		assertEquals("Robber's numRobbed should equal 1, instead it is " + robber.getNumRobbed(),1,robber.getNumRobbed());
		
		//Step 2 
		assertTrue("Robber's scheduler should have returned true, but it didn't.", robber.pickAndExecuteAnAction());
		
		//Post-conditions of Step 2, pre-conditions of Step 3
		assertEquals("Robber's state should be Holding, but instead it is " + robber.getAState(),"Holding",robber.getAState());
		assertEquals("Robber's numRobbed should equal 1, instead it is " + robber.getNumRobbed(),1,robber.getNumRobbed());
		assertTrue("Teller's log should read\"Getting robbed for 50.0\", instead it reads: " + slow_teller.log.getLastLoggedEvent().toString(),
				slow_teller.log.containsString("Getting robbed for 50.0"));
		
		//Step 3 
		//msgHereIsCash(Banker,amount)
		robber.msgHereIsCash(slow_teller, 50);
		
		//Post-conditions of step 3, pre-conditions of step 1		
		assertTrue("Robber's log should read \"Received 50.0 from teller 1\", instead it reads: " + robber.log.getLastLoggedEvent().toString(),
				robber.log.containsString("Received 50.0 from teller 1"));
		assertEquals("Robber's numRobbed should equal 0, instead it is " + robber.getNumRobbed(),0,robber.getNumRobbed());
		assertEquals("Robber's state should be Leaving, but instead it is " + robber.getAState(),"Leaving",robber.getAState());
		assertEquals("Robber should have 50.0 stolen cash, but he doesn't.", 50.0, robber.getCash());
		
		//Step 4 
		assertTrue("Robber's scheduler should have returned true, but it didn't.", robber.pickAndExecuteAnAction());
		
		//Post-conditions of Step 2
		assertTrue("Host's log should read \"Done getting robbed\", instead it reads: " + host.log.getLastLoggedEvent().toString(),
				host.log.containsString("Done getting robbed"));
		assertTrue("Teller's log should read \"Done getting robbed\", instead it reads: " + slow_teller.log.getLastLoggedEvent().toString(),
				slow_teller.log.containsString("Done getting robbed"));
		
	}
	/*
	 * In this test, an armed robber absconds with the money of three tellers. Not testing intermediate states, just making sure robber
	 * can handle multiple tellers  
	 */
	public void testCompleteScenario(){
		//Setup
		robber.setArmed(true);
		host.addBanker(first_teller);
		host.addBanker(second_teller);
		host.addBanker(third_teller);
		//Pre-conditions
		assertEquals("Robber's log should be empty, but it isn't.",0,robber.log.size());
		assertEquals("Host's log should be empty, but it isn't.",0,host.log.size());
		assertEquals("First teller's log should be empty, but isn't.",0,first_teller.log.size());
		assertEquals("Second teller's log should be empty, but isn't.",0,second_teller.log.size());
		assertEquals("Third teller's log should be empty, but isn't.",0,third_teller.log.size());
		
		assertEquals("Robber's state should be Entering, but instead it is " + robber.getAState().toString(), "Entering",robber.getAState());
		assertTrue("Robber should be armed, but he isn't", robber.getWState().equals("Armed"));
		assertEquals("Robber should have no stolen cash, but he does.", 0.0, robber.getCash());
		
		assertEquals("Host should have three tellers in his list of bankers, instead he has " + host.bankerList.size(),3,host.bankerList.size());
		
		//Step 1 
		assertTrue("Robber's scheduler should have returned true, but it didn't.", robber.pickAndExecuteAnAction());
		
		//Post-conditions of Step 1, pre-conditions of Step 2
		assertTrue("Host's log should read \"Got request for bankers, sending it\", instead it reads: " + host.log.getLastLoggedEvent().toString(),
				host.log.containsString("Got request for bankers, sending it"));
		assertTrue("Robber's log should read \"Got list of bankers, proceeding to rob\", instead it reads: " + robber.log.getLastLoggedEvent().toString(),
				robber.log.containsString("Got list of bankers, proceeding to rob"));
		assertEquals("Robber's state should be Robbing, but instead it is " + robber.getAState(),"Robbing",robber.getAState());
		assertEquals("Robber's numRobbed should equal 3, instead it is " + robber.getNumRobbed(),3,robber.getNumRobbed());
		
		//Step 2 
		assertTrue("Robber's scheduler should have returned true, but it didn't.", robber.pickAndExecuteAnAction());
		
		//Post-conditions of Step 2, pre-conditions of Step 3
		assertTrue("First teller's log should read \"Getting robbed for 10.0\", instead it reads: " + first_teller.log.getLastLoggedEvent().toString(),
				first_teller.log.containsString("Getting robbed for 10.0"));
		assertTrue("Robber's log should read \"Received 10.0 from teller 3\", instead it reads: " + robber.log.getLastLoggedEvent().toString(),
				robber.log.containsString("Received 10.0 from teller 3"));
		assertEquals("Robber's numRobbed should equal 2, instead it is " + robber.getNumRobbed(),2,robber.getNumRobbed());
		assertEquals("Robber's state should be Robbing, but instead it is " + robber.getAState(),"Robbing",robber.getAState());
		assertEquals("Robber should have 10.0 stolen cash, but he doesn't.", 10.0, robber.getCash());
		
		//Step 3 
		assertTrue("Robber's scheduler should have returned true, but it didn't.", robber.pickAndExecuteAnAction());
				
		//Post-conditions of Step 3, pre-conditions of Step 4
		assertTrue("Robber's log should read \"Received 100.0 from teller 2\", instead it reads: " + robber.log.getLastLoggedEvent().toString(),
				robber.log.containsString("Received 100.0 from teller 2"));
		assertEquals("Robber's numRobbed should equal 1, instead it is " + robber.getNumRobbed(),1,robber.getNumRobbed());
		assertEquals("Robber's state should be Robbing, but instead it is " + robber.getAState(),"Robbing",robber.getAState());
		assertEquals("Robber should have 110.0 stolen cash, but he doesn't.", 110.0, robber.getCash());
		
		//Step 4
		assertTrue("Robber's scheduler should have returned true, but it didn't.", robber.pickAndExecuteAnAction());
				
		//Post-conditions of Step 4, pre-conditions of Step 5
		assertTrue("Robber's log should read \"Received 1000.0 from teller 1\", instead it reads: " + robber.log.getLastLoggedEvent().toString(),
				robber.log.containsString("Received 1000.0 from teller 1"));
		assertEquals("Robber's numRobbed should equal 0, instead it is " + robber.getNumRobbed(),0,robber.getNumRobbed());
		assertEquals("Robber's state should be Leaving, but instead it is " + robber.getAState(),"Leaving",robber.getAState());
		assertEquals("Robber should have 1110.0 stolen cash, but he doesn't.", 1110.0, robber.getCash());
		
		//Step 4 
		assertTrue("Robber's scheduler should have returned true, but it didn't.", robber.pickAndExecuteAnAction());
		
		//Post-conditions of Step 2
		assertTrue("Host's log should read \"Done getting robbed\", instead it reads: " + host.log.getLastLoggedEvent().toString(),
				host.log.containsString("Done getting robbed"));
		assertTrue("First teller's log should read \"Done getting robbed\", instead it reads: " + first_teller.log.getLastLoggedEvent().toString(),
				first_teller.log.containsString("Done getting robbed"));
		assertTrue("Second teller's log should read \"Done getting robbed\", instead it reads: " + second_teller.log.getLastLoggedEvent().toString(),
				second_teller.log.containsString("Done getting robbed"));
		assertTrue("Last teller's log should read \"Done getting robbed\", instead it reads: " + third_teller.log.getLastLoggedEvent().toString(),
				third_teller.log.containsString("Done getting robbed"));
	}
	
}

