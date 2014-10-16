package SimCity.interfaces;

import Role.Landlord.PayState;

public interface Lord {

	//Messages
	public abstract void msgHereIsRent(Resident resident, double rent);

	//Actions
	public abstract void acceptPayment(Payment p, MyResident mr);

	public abstract void askResidentToPay(MyResident mr);

	public abstract void addResident(Resident r, double rent);

	public class MyResident{
		public Resident resident;
		public double rent;
		public boolean paid = false;

		public MyResident(Resident resident, double rent){
			this.resident = resident;
			this.rent = rent;
		}

	}



	public class Payment{
		public Resident resident;
		public double amount;
		public PayState state = PayState.pending;

		public Payment(Resident r, double a){
			this.resident=r;
			this.amount = a;
		}
	}
}