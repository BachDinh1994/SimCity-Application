package busstop.test.mock;

import busstop.gui.BusPassengerGui;
import busstop.interfaces.Bus;
import busstop.interfaces.BusPassenger;
import busstop.interfaces.TicketMachine;

public class MockBusPassenger extends Mock implements BusPassenger {

	public MockBusPassenger(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgEnteredStop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtMachine() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgRegistered(double money) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReceivedFare(int spot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgInLine() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGetOnBus(Bus b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOnBus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgBusAtStop(TicketMachine bs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeftStop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void goToMachine() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerAtStop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void payFare() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getOnBus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tellBusStatus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void leaveStop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGui(BusPassengerGui bpGui) {
		// TODO Auto-generated method stub
		
	}

}
