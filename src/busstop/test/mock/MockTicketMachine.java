package busstop.test.mock;

import java.util.ArrayList;

import busstop.BusPassengerRole;
import busstop.TicketMachineAgent.MyBus;
import busstop.TicketMachineAgent.MyPassenger;
import busstop.interfaces.Bus;
import busstop.interfaces.BusPassenger;
import busstop.interfaces.TicketMachine;

public class MockTicketMachine extends Mock implements TicketMachine {

	public MockTicketMachine(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void msgRegistering(BusPassengerRole bp, TicketMachine stop) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgHereIsFare(BusPassenger bp, double money) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgArriving(Bus b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtCurb(Bus b, ArrayList<BusPassengerRole> leavingPassengers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeavingStop(Bus b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgExiting(BusPassenger bp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public MyBus findBus(Bus b) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MyPassenger findPassenger(BusPassenger bp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeBus(Bus b) {
		// TODO Auto-generated method stub
		
	}

}
