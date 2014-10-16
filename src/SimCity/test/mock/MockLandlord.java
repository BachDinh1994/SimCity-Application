package SimCity.test.mock;

import SimCity.interfaces.Lord;
import SimCity.interfaces.Resident;

public class MockLandlord extends Mock implements Lord{

	public EventLog log;
	public MockLandlord(String name) {
		super(name);
		log = new EventLog();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgHereIsRent(Resident resident, double rent) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("receive msgHereIsRent"));
	}

	@Override
	public void acceptPayment(Payment p, MyResident mr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void askResidentToPay(MyResident mr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addResident(Resident r, double rent) {
		// TODO Auto-generated method stub
		
	}

}
