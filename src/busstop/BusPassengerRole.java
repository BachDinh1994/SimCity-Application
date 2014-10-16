package busstop;

import java.util.HashMap;
import java.util.concurrent.Semaphore;

import Role.Role;
import busstop.gui.BusPassengerGui;
import busstop.interfaces.Bus;
import busstop.interfaces.BusPassenger;
import busstop.interfaces.TicketMachine;

public class BusPassengerRole extends Role implements BusPassenger {
	
	String name;
	double money;
	double fare;
	int spot;
	
    String currentStop; // name of the TicketMachine it goes into
    HashMap<String, TicketMachineAgent /*bus stop*/ > routeMap;
    String dest; // name of TicketMachine it wants to get off at; different from final destination of the Person
    Bus b = null;
    pState s = pState.inactive;
    public enum pState {atStop, goingToMachine, atMachine, registering, registered, paying, paid, goingToLine, inLine, gettingOnBus, onBus, staying, gettingOff, offBus, inactive};
    
    int spotInLine;
    
    public Semaphore waitingForBus = new Semaphore(0, true);
    public Semaphore waitingOnBus = new Semaphore(0, true);
    
    public BusPassengerGui bpGui;
    
    public BusPassengerRole(String name, double money) {
    	this.name = name;
    	this.money = money;
    }

	@Override
	public void msgEnteredStop() { // from BusPassengerGui
        s = pState.atStop;
    	stateChanged();
    }
	@Override
	public void msgAtMachine() { // from BusPassengerGui
        s = pState.atMachine;
    	stateChanged();
	}
	@Override
	public void msgRegistered(double money) { // from TicketMachine
		fare = money;
        s = pState.registered;
    	stateChanged();
	}
	@Override
	public void msgReceivedFare(int spot) { // from TicketMachine
		this.spot = spot;
        s = pState.paid;
    	stateChanged();
	}
	@Override
	public void msgInLine() { // from BusPassengerGui
		s = pState.inLine;
    	stateChanged();
	}
	@Override
	public void msgGetOnBus(Bus b) { // from BusAgent
        s = pState.gettingOnBus;
        this.b = b;
    	stateChanged();
    }
	@Override
	public void msgOnBus() { // from BusPassengerGui
		s = pState.onBus;
		try {
			waitingOnBus.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	stateChanged();
	}
	@Override
	public void msgBusAtStop(TicketMachine bs) { // from BusAgent
        if (bs == routeMap.get(dest)) {
        	currentStop = dest;
        	s = pState.gettingOff;
        }
        else s = pState.staying;
    	stateChanged();
    }
	@Override
	public void msgLeftStop() { // from BusPassengerGui
		currentStop = null;
		s = pState.inactive;
    	stateChanged();
	}
    
	@Override
	public boolean pickAndExecuteAnAction() {
        if (s == pState.atStop) { goToMachine(); return true; }
        if (s == pState.atMachine) { registerAtStop(); return true; }
        if (s == pState.registered) { payFare(); return true; }
        if (s == pState.paid) { goToLine(); return true; }
        if (s == pState.gettingOnBus) { getOnBus(); return true; }
	    if (s == pState.staying || s == pState.gettingOff) { tellBusStatus(); return true; }
        if (s == pState.offBus) { leaveStop(); return true; }
		return false;
	}

	@Override
	public void goToMachine() {
    	bpGui.DoGoToMachine();
    	s = pState.goingToMachine;
    }
	@Override
	public void registerAtStop() {
    	routeMap.get(currentStop).msgRegistering(this, routeMap.get(dest));
    	s = pState.registering;
    }
	@Override
	public void payFare() {
    	routeMap.get(currentStop).msgHereIsFare(this, fare);
    	s = pState.paying;
    }
    private void goToLine() {
		bpGui.DoGoToLine(spot);
		s = pState.goingToLine;
	}
	@Override
	public void getOnBus() {
    	bpGui.DoGetOnBus();
        currentStop = null;
    }
	@Override
	public void tellBusStatus() {
        if (s == pState.staying) { b.msgStayingInBus(this); s = pState.onBus; }
        else {
            b.msgLeavingBus(this);
            s = pState.offBus;
            b = null;
        }
    }
	@Override
	public void leaveStop() {
    	bpGui.DoLeaveStop();
    }

    @Override
	public void setGui(BusPassengerGui bpGui) {
    	this.bpGui = bpGui;
    }
    
}
