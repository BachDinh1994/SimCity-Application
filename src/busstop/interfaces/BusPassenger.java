package busstop.interfaces;

import busstop.gui.BusPassengerGui;

public interface BusPassenger {

	public abstract void msgEnteredStop();

	public abstract void msgAtMachine();

	public abstract void msgRegistered(double money);

	public abstract void msgReceivedFare(int spot);

	public abstract void msgInLine();

	public abstract void msgGetOnBus(Bus b);

	public abstract void msgOnBus();

	public abstract void msgBusAtStop(TicketMachine bs);

	public abstract void msgLeftStop();

	public abstract boolean pickAndExecuteAnAction();

	public abstract void goToMachine();

	public abstract void registerAtStop();

	public abstract void payFare();

	public abstract void getOnBus();

	public abstract void tellBusStatus();

	public abstract void leaveStop();

	public abstract void setGui(BusPassengerGui bpGui);

}