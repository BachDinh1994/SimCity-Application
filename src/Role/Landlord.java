package Role;

import java.util.ArrayList;
import java.util.List;

import SimCity.interfaces.Lord;
import SimCity.interfaces.Resident;
import SimCity.test.mock.EventLog;
import SimCity.test.mock.LoggedEvent;

public class Landlord extends ResidentRole implements Lord{

	
	//Data
		public EventLog log;
		String name;
		public List<Payment> payments = new ArrayList<Payment>();
		public enum PayState{pending,finished};
		public List<MyResident> myResidents = new ArrayList<MyResident>();
		public boolean rentDay = false;


		//Messages
		
		public void msgRentDay(){
			log.add(new LoggedEvent("receive msgRentDay"));
			rentDay = true;
			stateChanged();
		}
		
		
		public void msgHereIsRent(Resident resident, double rent){
			log.add(new LoggedEvent("receive msgHereIsRent and the rent is "+rent+" dollars"));
			payments.add(new Payment(resident,rent));
			stateChanged();
		}



		//Schedulers

		public boolean pickAndExecuteAnAction1(){
			
			if(rentDay){
				rentDay =false;
				getRents();
				return true;
			}
			
			
			
			
			for(Payment p:payments){
				if(p.state==PayState.pending){
					p.state=PayState.finished;
					for(MyResident mr: myResidents){
						if(mr.resident == p.resident && mr.rent == p.amount && mr.paid == false){
							acceptPayment(p,mr);

						}
					}
					return true;
				}
			}
			return false;
		}




		//Actions
		
		public void getRents(){
			
			for(MyResident mr:myResidents){
				
				mr.resident.msgRentIsDue(this, mr.rent);
			}
		}
		
		
		public void acceptPayment(Payment p, MyResident mr){
			//myPerson.wealth += p.amount;
			mr.paid = true;
			mr.resident.msgPaymentAccepted(this,p.amount);
		}


		public void askResidentToPay(MyResident mr){
			mr.resident.msgRentIsDue(this,mr.rent);
		}


		//Utilities
		public Landlord(String name){
			super(name);
			this.name = name;
			log = new EventLog();
		}


		public void addResident(Resident r,double rent){
			myResidents.add(new MyResident(r,rent));
		}


		public String toString(){
			return name;
		}
	
	
	
	
	
	
	
	
	
	
	
}
