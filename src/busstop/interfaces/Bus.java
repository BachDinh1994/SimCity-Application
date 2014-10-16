package busstop.interfaces;

import java.util.ArrayList;

import SimCity.BusOutsideGui;
import busstop.BusPassengerRole;
import busstop.BusAgent.MyPassenger;
import busstop.TicketMachineAgent;
import busstop.gui.BusInsideGui;

public interface Bus {
	
	public abstract void msgArrivedAtStop(String stop);

	public abstract void msgComeToCurb();

	public abstract void msgLeavingBus(BusPassenger bp);

	public abstract void msgStayingInBus(BusPassenger bp);

	public abstract void msgAtCurb();

	public abstract void msgHereAreNewPassengers(
			ArrayList<BusPassengerRole> passengerList);

	public abstract void msgOutOfStop();

	public abstract void arrivingAtStop();

	public abstract void goToCurb();

	public abstract void arrivingAtCurb();

	public abstract void tellNewPassengers();

	public abstract void leavingStop();

	public abstract void goToNextStop();

	public abstract MyPassenger findPassenger(BusPassenger bp);

	public abstract boolean allAnswered();

	public abstract void nextStop();

	public abstract void setInsideGui(BusInsideGui biGui);

	public abstract void setOutsideGui(BusOutsideGui boGui);
	
	public abstract BusInsideGui getInsideGui();
	
	public abstract BusOutsideGui getOutsideGui();

	public abstract void addStop(TicketMachineAgent tm);

}