package busstop.interfaces;

import java.util.ArrayList;

import busstop.BusAgent;
import busstop.BusPassengerRole;
import busstop.TicketMachineAgent;
import busstop.TicketMachineAgent.MyBus;
import busstop.TicketMachineAgent.MyPassenger;

public interface TicketMachine {

	public abstract void msgRegistering(BusPassengerRole bp,
			TicketMachineAgent stop);

	public abstract void msgHereIsFare(BusPassenger bp, double money);

	public abstract void msgArriving(BusAgent b);

	public abstract void msgAtCurb(BusAgent b,
			ArrayList<BusPassengerRole> leavingPassengers);

	public abstract void msgLeavingStop(BusAgent b);

	public abstract void msgExiting(BusPassenger bp);

	public abstract boolean pickAndExecuteAnAction();

	public abstract MyBus findBus(BusAgent b);

	public abstract MyPassenger findPassenger(BusPassenger bp);

	public abstract void removeBus(BusAgent b);

}