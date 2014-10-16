package SimCity.interfaces;

import Role.Role;
import SimCity.Gui;
import SimCity.PersonGui;


public interface Person {

	
	public abstract void addRole(Role r);

	public abstract void setGui(PersonGui g);
	
	public abstract PersonGui getGui();

	public abstract void addGui(Gui gui);

	public abstract void stateChanged();

	public abstract Wealth getWealth();
	
	public static class Wealth
	{
		double cash = 100;
	    double savings = 100;
	    double salary = 100;
	    double loan = 100;
	    double cashNeeded = 100;

	    
	    public double getCash() {
			return cash;
		}
	    
		public void setCash(double cash) {
			this.cash = cash;
		}
		public double getCashNeeded(){
			return this.cashNeeded;
		}
		public void setCashNeeded(double x){
			this.cashNeeded = x;
		}
		public double getSavings() {
			return savings;
		}
		
		public void setSavings(double savings) {
			this.savings = savings;
		}
		
		public double getSalary() {
			return salary;
		}
		
		public void setSalary(double salary) {
			this.salary = salary;
		}
		
		public double getLoan() {
			return loan;
		}
		
		public void setLoan(double loan) {
			this.loan = loan;
		}
	}



}