package restaurant_rancho;

import java.util.ArrayList;
import java.util.List;

import restaurant_rancho.gui.ManagerGui;
import agent.Agent;
import Role.Role;

public class RanchoManager extends Role{
	
	String name;
	boolean dayOff = false;
	public enum PayStatus{unpaid, paid};
	public enum PaymentStatus{pending,finished};
	List<MyStaff> staffs = new ArrayList<MyStaff>();
	List<Payment> payments = new ArrayList<Payment>();
	RanchoHost host;
	RanchoCook cook;
	RanchoCashier cashier;
	ManagerGui gui;
	
	public RanchoManager(String name){
		super();
		this.name = name;
	}
	
	
	public void msgDayOff(){
		System.out.println("receive msgDayOff");
		dayOff = true;
		for(MyStaff m:staffs){
			m.status = PayStatus.unpaid;
			System.out.println("1");
		}
		stateChanged();
	}
	
	
	
	
	
	
	
	public boolean pickAndExecuteAnAction(){
		
		if(dayOff){
			System.out.println("2");
			dayOff = false;
			tellStaffLeave();
			return true;
		}
		
		for(Payment p: payments){
			if(p.status==PaymentStatus.pending){
				payMoney(p);
				return true;
			}
		}
		
		
		
		return false;
	}
	
	
	
	
	
	
	
	
	
	private void payMoney(Payment p) {
		// TODO Auto-generated method stub
		p.status = PaymentStatus.finished;
		if(p.jobTitle.equals("Waiter")){
			((RanchoWaiterRole)p.receiver).msgHereIsSalary(p.amount);
		}
	}


	private void tellStaffLeave() {
		// TODO Auto-generated method stub
		for(MyStaff m: staffs){
			if(m.jobTitle.equals("Waiter")){
				System.out.println("3");
				host.giveWaiterBreak((RanchoWaiterRole)m.person);
				((RanchoWaiterRole)m.person).msgYouCanLeave();
			}
		}
	}









	class MyStaff{
		PayStatus status;
		Role person;
		String jobTitle;
		double salary;
		
		MyStaff(Role r){
			this.status = PayStatus.paid;
			
			
			
			if(r instanceof RanchoWaiterRole){
				this.person = (RanchoWaiterRole)r;
				this.jobTitle = "Waiter";
				this.salary = 1;
			}
			//later cashier and cook should be added
		}
		
	}
	
	class Payment{
		
		PaymentStatus status;
		Role receiver;
		double amount;
		String jobTitle;
		
		Payment(MyStaff s){
			this.receiver = s.person;
			this.amount = s.salary;
			status = PaymentStatus.pending;	
			jobTitle = s.jobTitle;
		}
		
		
	}
	
	public void addStaff(Role r){
		staffs.add(new MyStaff(r));
	}


	public void msgImLeaving(Role r) {
		// TODO Auto-generated method stub
		for(MyStaff m: staffs){
			if(m.person == r && m.status ==PayStatus.unpaid){
				payments.add(new Payment(m));
				stateChanged();
				return;
			}
		}
		
		
	}


	public void setHost(RanchoHost host) {
		// TODO Auto-generated method stub
		this.host = host;
		
	}


	public void setCook(RanchoCook cook) {
		// TODO Auto-generated method stub
		this.cook = cook;
	}


	public void setCashier(RanchoCashier cashier) {
		// TODO Auto-generated method stub
		this.cashier = cashier;
	}


	public void setGui(ManagerGui g) {
		// TODO Auto-generated method stub
		this.gui = g;
	}

}
