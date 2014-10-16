package busstop;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;

import busstop.BusAgent;
import busstop.interfaces.BusPassenger;
import busstop.interfaces.TicketMachine;
import SimCity.Building;
import agent.Agent;

public class TicketMachineAgent extends Agent implements TicketMachine {
	
	Building bd;
	public int number;
	double cash = 0;
	
	ArrayList<MyPassenger> waitList = new ArrayList<MyPassenger>();
	HashMap<String, Double> prices = new HashMap<String, Double>();
	public class MyPassenger {
		BusPassengerRole bpr; pState s; TicketMachine dest;
		public MyPassenger(BusPassengerRole bp, TicketMachine dest) { this.bpr = bp; s = pState.registering; this.dest = dest; }
	}
	public enum pState {registering, registered, paying, paid};
	public ArrayList<BusPassengerRole> exitingPassengers = new ArrayList<BusPassengerRole>();
    public ArrayList<MyBus> buses = new ArrayList<MyBus>(); // assume all buses that go to this station run the same route
    public class MyBus {
    	BusAgent b; bState s;
    	public MyBus(BusAgent b) {
    		this.b = b; s = bState.arrived;
    		bd.myBuildingPanel.addBus(bd, b, b.num);
    	}
    }
    public enum bState {arrived, approaching, atCurb, boarding, left};
    
	public TicketMachineAgent(Building bd, int num) {
		this.bd = bd;
		number = num;
		for (int i = 0; i < 3; i++) {
			if (num > 3) num = 0;
			prices.put(((Integer)num).toString(), (double)i);
		}
	}

	@Override
	public void msgRegistering(BusPassengerRole bp, TicketMachineAgent stop) {
        waitList.add(new MyPassenger(bp, stop)); // automatically sets state to pState.registering
    	stateChanged();
    }
	@Override
	public void msgHereIsFare(BusPassenger bp, double money) {
		findPassenger(bp).s = pState.paying;
        cash += money;
    	stateChanged();
    }
	@Override
	public void msgArriving(BusAgent b) {
        buses.add(new MyBus(b)); // automatically sets state to bState.arrived
    	stateChanged();
    }
	@Override
	public void msgAtCurb(BusAgent b, ArrayList<BusPassengerRole> leavingPassengers) {
        exitingPassengers.addAll(leavingPassengers);
        findBus(b).s = bState.atCurb;
    	stateChanged();
    }
	@Override
	public void msgLeavingStop(BusAgent b) {
		MyBus mb = findBus(b);
		mb.s = bState.left;
    	stateChanged();
    }
	@Override
	public void msgExiting(BusPassenger bp) {
        exitingPassengers.remove(bp);
    	stateChanged();
    }
    
	@Override
	public boolean pickAndExecuteAnAction() {
		try {
			for (MyPassenger mp : waitList) {
				if (mp.s == pState.registering) { registerPassenger(mp); return true; }
			}
			for (MyPassenger mp : waitList) {
				if (mp.s == pState.paying) { collectFare(mp); return true; }
			}
			for (MyBus mb : buses) {
				if (mb.s == bState.boarding) return false;
			}
			for (MyBus mb : buses) {
				if (mb.s == bState.arrived) { callToCurb(mb); return true; }
			}
			for (MyBus mb : buses) {
				if (mb.s == bState.atCurb) { givePassengers(mb); return true; }
			}
			for (MyBus mb : buses) {
				if (mb.s == bState.left) { buses.remove(mb); return true; }
			}
		} catch (ConcurrentModificationException e) { return false; }
    	
    	return false;
    }
    
    private void registerPassenger(MyPassenger mp) {
    	mp.bpr.msgRegistered(prices.get(mp.dest));
    	mp.s = pState.registered;
	}
    private void collectFare(MyPassenger mp) {
    	mp.bpr.msgReceivedFare(waitList.indexOf(mp));
    	mp.s = pState.paid;
	}
    private void givePassengers(MyBus mb) {
		ArrayList<BusPassengerRole> passengers = new ArrayList<BusPassengerRole>();
		for (MyPassenger mp : waitList) {
			passengers.add(mp.bpr);
		}
        mb.b.msgHereAreNewPassengers(passengers);
        mb.s = bState.boarding;
        waitList.clear(); // or just recreate the list
	}
	private void callToCurb(MyBus mb) {
        mb.b.msgComeToCurb();
        mb.s = bState.approaching;
	}

	@Override
	public MyBus findBus(BusAgent b) {
    	for (MyBus mb : buses) {
    		if (mb.b == b) {return mb;}
    	}
		return null;
    }
	@Override
	public MyPassenger findPassenger(BusPassenger bp) {
    	for (MyPassenger mp : waitList) {
    		if (mp.bpr == bp) {return mp;}
    	}
		return null;
    }
	@Override
	public void removeBus(BusAgent b) {
    	for (MyBus mb : buses) {
    		if (mb.b == b) { buses.remove(mb); break; }
    	}
    }
	public void addBus(BusAgent b) {
		buses.add(new MyBus(b));
	}

}
