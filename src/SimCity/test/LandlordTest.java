package SimCity.test;

import Role.Landlord;
import Role.Landlord.PayState;
import SimCity.test.mock.MockPerson;
import SimCity.test.mock.MockResident;
import junit.framework.TestCase;

public class LandlordTest extends TestCase{
	
	MockResident resident;
	Landlord landlord;
	MockPerson person;
	
	/**
	 * This method is run before each test. You can use it to instantiate the class variables
	 * for your agent and mocks, etc.
	 */
	public void setUp() throws Exception{
		super.setUp();
		resident = new MockResident("Rancho");
		landlord = new Landlord("Rain");
		person = new MockPerson("person");
		landlord.addResident(resident,10);
		resident.setLord(landlord);
		landlord.setPerson(person);
		
		
	}
	
	public void testLandlordResidentInteractionScenario(){
		
		
		
		assertEquals("Landlord's log should be empty, instead it reads: " + landlord.log.toString(), 0, landlord.log.size());		
		
		assertFalse("landlord's scheduler should return false, but it didn't", landlord.pickAndExecuteAnAction());
		
		assertFalse("Landlord's rentDay boolean should set to False now, but it didn't", landlord.rentDay);
		
		
		
		
		
		landlord.msgRentDay();
		
		assertTrue("Landlord's log should contain \"receive msgRentDay\", but instead it reads: " + landlord.log.getLastLoggedEvent().toString(),landlord.log.containsString("receive msgRentDay"));
		
		assertTrue("Landlord's rentDay boolean should set to true now, but it didn't", landlord.rentDay);
		
		assertTrue("landlord's scheduler should return true, but it didn't", landlord.pickAndExecuteAnAction());
		
		assertTrue("Landlord's rentDay boolean should set to false now, but it didn't", !landlord.rentDay);
		
		assertFalse("landlord's scheduler should return false, but it didn't", landlord.pickAndExecuteAnAction());
		
		
		
		
		
		
		
		assertEquals("Landlord should have no payments processing, instead it has "+ landlord.payments.size(), 0, landlord.payments.size());
		
		assertEquals("landlord should have one resident, instead it has "+ landlord.myResidents.size(), 1, landlord.myResidents.size());
		
		assertEquals("MockResident's log should be empty instead it reads: " + resident.log.toString(), 0, resident.log.size());
		
		
		
		
		landlord.msgHereIsRent(resident, 10);
		
		assertTrue("Landlord's log should contain \"receive msgHereIsRent and the rent is 10.0 dollars\", but instead it reads: " + landlord.log.getLastLoggedEvent().toString(),landlord.log.containsString("receive msgHereIsRent and the rent is 10.0 dollars"));
		
		assertEquals("Landlord should have 1 payment processing, instead it has "+ landlord.payments.size(), 1, landlord.payments.size());
		
		assertTrue("The state of MyResident should be not paid, but didn't",!landlord.myResidents.get(0).paid);
		
		assertTrue("The state of the payment should be pending, but didn't", landlord.payments.get(0).state==PayState.pending);
		
		
		
		
		assertTrue("landlord's scheduler should return true, but it didn't", landlord.pickAndExecuteAnAction());
		
		assertTrue("The state of MyResident should be paid, but didn't",landlord.myResidents.get(0).paid);
		
		assertTrue("The state of the payment should be finished, but didn't", landlord.payments.get(0).state==PayState.finished);
		
		
		assertFalse("landlord's scheduler should return false, but it didn't", landlord.pickAndExecuteAnAction());
	}
	
	

}
