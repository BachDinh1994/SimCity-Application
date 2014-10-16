package busstop;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import busstop.gui.BusInsideGui;
import busstop.interfaces.Bus;
import busstop.interfaces.BusPassenger;
import SimCity.BusOutsideGui;
import agent.Agent;

public class BusAgent extends Agent implements Bus {
	
	public int num;
	
    ArrayList<MyPassenger> passengers = new ArrayList<MyPassenger>();
    public class MyPassenger {
    	BusPassengerRole bp; pState s;
    	public MyPassenger(BusPassengerRole bp) {this.bp = bp; s = pState.entered;}
    }
    public enum pState {entered, riding, leaving, staying};
    public int currentStop;
    public int destination;
    public ArrayList<TicketMachineAgent> stops = new ArrayList<TicketMachineAgent>();
    bState s;
    public enum bState {atStop, messagedStop, goingToCurb, atCurb, readyToBoard, boarded, readyToGo, leaving, outOfStop, enRoute};
    
    public Semaphore waitingForResponse = new Semaphore(0);
    public Semaphore inTransit = new Semaphore(0);
    public Semaphore atCurb = new Semaphore(0);
    public Semaphore outOfStop = new Semaphore(0);
    
    public BusOutsideGui boGui;
    public BusInsideGui biGui;
    
    public BusAgent(int n) {
    	num = n;
    	currentStop = n;
    	destination = n;
    }
    
	@Override
	public void msgArrivedAtStop(String stop) { // from BusOutsideGui
    	inTransit.release();
    	s = bState.atStop;
    	stateChanged();
    }
	@Override
	public void msgComeToCurb() { // from TicketMachine
    	s = bState.goingToCurb;
    	stateChanged();
	}
	@Override
	public void msgLeavingBus(BusPassenger bp) { // from BusPassenger
    	findPassenger(bp).s = pState.leaving;
    	if (allAnswered()) waitingForResponse.release();
    	stateChanged();
    }
	@Override
	public void msgStayingInBus(BusPassenger bp) { // from BusPassenger
    	findPassenger(bp).s = pState.staying;
    	if (allAnswered()) waitingForResponse.release();
    	stateChanged();
    }
	@Override
	public void msgAtCurb() { // from BusInsideGui
		atCurb.release();
    	s = bState.atCurb;
    	stateChanged();
	}
	@Override
	public void msgHereAreNewPassengers(ArrayList<BusPassengerRole> passengerList) { // from TicketMachine
    	for (BusPassengerRole bp : passengerList) { passengers.add(new MyPassenger(bp)); }
    	s = bState.boarded;
    	stateChanged();
    }
	@Override
	public void msgOutOfStop() { // from BusInsideGui
		outOfStop.release();
        s = bState.outOfStop;
		stateChanged();
	}

	protected boolean pickAndExecuteAnAction() {
	    if (s == bState.atStop) { arrivingAtStop(); return true; }
	    if (s == bState.goingToCurb) { goToCurb(); return true; }
	    if (s == bState.atCurb) { arrivingAtCurb(); return true; }
	    if (s == bState.boarded) { tellNewPassengers(); return true; }
	    if (s == bState.readyToGo) { leavingStop(); return true; }
	    if (s == bState.outOfStop) { goToNextStop(); return true; }
	    
		return false;
	}

	@Override
	public void arrivingAtStop() {
        currentStop = destination;
        stops.get(currentStop).msgArriving(this);
        s = bState.messagedStop;
    }
	@Override
	public void goToCurb() {
    	biGui.DoGoToCurb();
    	try {
			atCurb.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
	@Override
	public void arrivingAtCurb() {
		ArrayList<BusPassengerRole> leavingPassengers = new ArrayList<BusPassengerRole>();
    	if (passengers.size() > 0) {
	        for (MyPassenger mp : passengers)
	            mp.bp.msgBusAtStop(stops.get(currentStop));
	        try {
				waitingForResponse.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	        for (MyPassenger mp : passengers) {
	            if (mp.s == pState.leaving) {
	            	leavingPassengers.add(mp.bp);
	            	passengers.remove(mp);
	            }
	        }
    	}
        stops.get(currentStop).msgAtCurb(this, leavingPassengers);
        s = bState.readyToBoard;
    }
	@Override
	public void tellNewPassengers() {
        for (MyPassenger mp : passengers) {
            if (mp.s == pState.entered) {
            	mp.bp.msgGetOnBus(this);
            	mp.s = pState.riding;
            }
        }
        s = bState.readyToGo;
    }
	@Override
	public void leavingStop() {
    	biGui.DoLeaveStop();
    	stops.get(currentStop).msgLeavingStop(this);
        nextStop();
        s = bState.leaving;
		try {
			outOfStop.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    @Override
	public void goToNextStop() {
    	s = bState.enRoute;
    	boGui.DoGoToStop(((Integer)destination).toString());
		try {
			inTransit.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
	@Override
	public MyPassenger findPassenger(BusPassenger bp) {
    	for (MyPassenger mp : passengers) {
    		if (mp.bp == bp) {return mp;}
    	}
		return null;
    }
	@Override
	public boolean allAnswered() {
    	for (MyPassenger mp : passengers) {
    		if (mp.s != pState.leaving || mp.s != pState.staying) return false;
    	}
    	return true;
    }
	@Override
	public void nextStop() {
    	destination++;
    	if (destination > 3) destination = 0;
    }
    
    @Override
	public void setInsideGui(BusInsideGui biGui) {
    	this.biGui = biGui;
    }
   	@Override
	public void setOutsideGui(BusOutsideGui boGui) {
    	this.boGui = boGui;
    }
	@Override
	public BusInsideGui getInsideGui() {
		return biGui;
	}
	@Override
	public BusOutsideGui getOutsideGui() {
		return boGui;
	}
	@Override
	public void addStop(TicketMachineAgent tm) {
		stops.add(tm);
	}
}
