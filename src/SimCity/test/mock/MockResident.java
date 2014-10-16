package SimCity.test.mock;

import SimCity.LiveGui;
import SimCity.ResidentGui;
import SimCity.interfaces.Lord;
import SimCity.interfaces.Resident;

public class MockResident extends Mock implements Resident{

	public EventLog log;
	public MockResident(String name) {
		super(name);
		log = new EventLog();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgAtTable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtCooking() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtBed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtHomePos() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtKitchen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtFrige() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtDoor() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGoToSleep() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgDoneCooking(Food food) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGotHungry() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgFinishEating(Food food) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgRentIsDue(Lord l, double rent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgPaymentAccepted(Lord l,double amount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgExitHome() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void eatFood(Food f) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void backToHomePosition() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eatOut() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cookAtHome() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void payRent(RentDue d) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void sleep() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void setLord(Lord l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(LiveGui g) {
		// TODO Auto-generated method stub
		
	}

}
