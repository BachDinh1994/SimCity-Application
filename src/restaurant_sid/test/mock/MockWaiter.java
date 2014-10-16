package restaurant_sid.test.mock;

import restaurant_sid.SidCashierAgent;
import restaurant_sid.SidCustomerRole;
import restaurant_sid.SidHostAgent;
import restaurant_sid.interfaces.Customer;
import restaurant_sid.interfaces.Waiter;

public class MockWaiter extends Mock implements Waiter{
	
	public EventLog log;
	public SidCashierAgent cashier;
	public Customer customer;
	
	public MockWaiter(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		log = new EventLog();
	}
	
	@Override
	public void msgHereIsTheBill(double value, int tNum) {
		log.add(new LoggedEvent("Received bill"));
		customer.msgHereIsCheck(value);
	}
	
	public void msgHereIsCash(Customer c, double value){
		log.add(new LoggedEvent("Got money from customer"));
		cashier.msgCustomerPaid(c, value);
		c.msgHereIsChange(0.01);
	}
	
	public void msgCantPay(Customer c, double value){
		log.add(new LoggedEvent("Customer can't pay"));
	}

	@Override
	public void msgOutOfChoice(String meal) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderReady(String meal, int table) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHost(SidHostAgent sidHostAgent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgOrderReceived(SidCustomerRole sidCustomerRole, String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgSitAtTable(SidCustomerRole sidCustomerRole, int number) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgRemoveCustomer(SidCustomerRole c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgGoOnBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgReadytoOrder(SidCustomerRole sidCustomerRole) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgIWantCheck(SidCustomerRole sidCustomerRole) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeavingTable(SidCustomerRole sidCustomerRole) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void WantToBreak() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtTable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgAtCook() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getWNum() {
		// TODO Auto-generated method stub
		return 0;
	}

}
