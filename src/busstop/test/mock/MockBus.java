package busstop.test.mock;

import java.util.ArrayList;

import SimCity.BusOutsideGui;
import busstop.BusAgent.MyPassenger;
import busstop.BusPassengerRole;
import busstop.TicketMachineAgent;
import busstop.gui.BusInsideGui;
import busstop.interfaces.Bus;
import busstop.interfaces.BusPassenger;

public class MockBus extends Mock implements Bus {

	public MockBus(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgArrivedAtStop(String stop) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgComeToCurb() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeavingBus(BusPassenger bp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgStayingInBus(BusPassenger bp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtCurb() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereAreNewPassengers(
			ArrayList<BusPassengerRole> passengerList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOutOfStop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void arrivingAtStop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void goToCurb() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void arrivingAtCurb() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tellNewPassengers() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void leavingStop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void goToNextStop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MyPassenger findPassenger(BusPassenger bp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean allAnswered() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void nextStop() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setInsideGui(BusInsideGui biGui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOutsideGui(BusOutsideGui boGui) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BusInsideGui getInsideGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BusOutsideGui getOutsideGui() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addStop(TicketMachineAgent tm) {
		// TODO Auto-generated method stub
		
	}

}
