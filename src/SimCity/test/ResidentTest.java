package SimCity.test;


import Role.ResidentRole;
import Role.ResidentRole.FoodState;
import Role.ResidentRole.ResidentState;


import Role.ResidentRole.FoodState;
import Role.ResidentRole.ResidentState;
import Role.ResidentRole;

import SimCity.ResidentGui;
import SimCity.interfaces.Resident.Food;
import SimCity.test.mock.MockLandlord;
import SimCity.test.mock.MockPerson;
import junit.framework.TestCase;

public class ResidentTest extends TestCase{
	
	//these are instantiated for each test separately via the setUp() method.
		ResidentRole resident;
		ResidentGui gui;
		MockLandlord landlord;
		MockPerson person;
		
		
		/**
		 * This method is run before each test. You can use it to instantiate the class variables
		 * for your agent and mocks, etc.
		 */
		public void setUp() throws Exception{
			super.setUp();
			resident = new ResidentRole("Rancho");
			gui = new ResidentGui(resident);
			landlord = new MockLandlord("Rain");
			person = new MockPerson("person");
			resident.setLord(landlord);
			resident.setPerson(person);
			resident.setGui(gui);
			
		}
		
		
		
		
		
		
		
		
		public void testResidentNormScenario(){
			
			assertEquals("Resident's log should be empty, instead it reads: " + resident.log.toString(), 0, resident.log.size());
			
			assertFalse("Resident's scheduler should return false, but it didn't", resident.pickAndExecuteAnAction());
			
			resident.msgGoToSleep();
			
			assertTrue("Resident's log should contain \"receive msgGoToSleep\", but instead it reads: " + resident.log.getLastLoggedEvent().toString(),resident.log.containsString("receive msgGoToSleep"));
			
			assertTrue("Resident's state should change to goSleeping but didn't",resident.state==ResidentState.goSleeping);
			
			resident.msgGotHungry();
			
			assertTrue("Resident's log should contain \"receive msgGotHungry\", but instead it reads: " + resident.log.getLastLoggedEvent().toString(),resident.log.containsString("receive msgGotHungry"));
			
			assertTrue("Resident's state should change to goSleeping but didn't",resident.isHungry);
			
			resident.msgExitHome();
			
			assertTrue("Resident's log should contain \"receive msgExitHome\", but instead it reads: " + resident.log.getLastLoggedEvent().toString(),resident.log.containsString("receive msgExitHome"));
			
			assertTrue("Resident's state should change to goSleeping but didn't",resident.state==ResidentState.exitHome);
			
			Food food = new Food("Steak",5,1000,1000);
			
			assertTrue("Food's state should initially be noAction but didn't.",food.state==FoodState.noAction);
			
			resident.msgDoneCooking(food);
			
			assertTrue("Resident's log should contain \"receive msgDoneCooking\", but instead it reads: " + resident.log.getLastLoggedEvent().toString(),resident.log.containsString("receive msgDoneCooking"));
			
			assertTrue("Resident's state should change to goSleeping but didn't",resident.state==ResidentState.eating);
			
			assertTrue("Food's state should change to doneCooked but didn't.",food.state==FoodState.doneCooked);
			
			resident.msgFinishEating(food);
			
			assertTrue("Resident's log should contain \"receive msgFinishEating\", but instead it reads: " + resident.log.getLastLoggedEvent().toString(),resident.log.containsString("receive msgFinishEating"));
			
			assertTrue("Resident's state should change to doneEating but didn't",resident.state==ResidentState.doneEating);
			
		}
		
		
		
		
		
		
		
		public void testLandlordResidentInteractionScenario(){
			
			resident.getPersonAgent().getWealth().setCash(100);
			
			assertEquals("Resident initially has 100 dollars cash but instead has"+resident.getPersonAgent().getWealth().getCash(),100.0,resident.getPersonAgent().getWealth().getCash());
			
			assertEquals("Resident's log should be empty, instead it reads: " + resident.log.toString(), 0, resident.log.size());
			
			assertEquals("Resident should have no rent dues, instead it has "+ resident.rentDues.size(), 0, resident.rentDues.size());
						
			assertEquals("MockLandlord's log should be empty instead it reads: " + landlord.log.toString(), 0, landlord.log.size());
			
			assertFalse("Resident's scheduler should return false, but it didn't", resident.pickAndExecuteAnAction());

			resident.msgRentIsDue(landlord,10);
			
			assertEquals("Resident should have 1 rent dues, instead it has "+ resident.rentDues.size(), 1, resident.rentDues.size());
			
			assertTrue("Resident's log should contain \"receive msgRentIsDue\", but instead it reads: " + resident.log.getLastLoggedEvent().toString(),resident.log.containsString("receive msgRentIsDue"));
			
			assertTrue("Resident's scheduler should return True, but it didn't", resident.pickAndExecuteAnAction());
			
			assertTrue("Resident's log should contain \"pay the landlord 10.0 dollars for rents\", but instead it reads: " + resident.log.getLastLoggedEvent().toString(),resident.log.containsString("pay the landlord 10.0 dollars for rents"));
			
			assertTrue("MockLandlord's log should contain \"receive msgHereIsRent\", but instead it reads: "+ landlord.log.getLastLoggedEvent().toString(),landlord.log.containsString("receive msgHereIsRent"));
			
			resident.msgPaymentAccepted(landlord,10);
			
			assertEquals("Resident initially has 100 dollars cash but instead has"+resident.getPersonAgent().getWealth().getCash(),90.0,resident.getPersonAgent().getWealth().getCash());
			
			
		}	
		
		
		
		
		
		
		
		
		
		
		
		
		

}
