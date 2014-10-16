package SimCity;

import agent.Agent;
import amyRestaurant.AmyCustomerRole;
import amyRestaurant.AmyNormalWaiterRole;
import amyRestaurant.AmyWaiterRole;
import bank.BankAgent;
import bank.BankCustomer;
import bank.BankHost;
import bank.BankRobber;
import SimCity.interfaces.Person;

import java.util.*;

import restaurant_andrew.AndrewCustomerRole;
import restaurant_andrew.AndrewNormalWaiterRole;
import restaurant_andrew.AndrewWaiterRole;
import restaurant_rancho.RanchoCustomerRole;
import restaurant_rancho.RanchoManager;
import restaurant_rancho.RanchoNormalWaiterRole;
import restaurant_rancho.RanchoWaiterRole;
import restaurant_sid.SidCustomerRole;
import restaurant_sid.SidNormalWaiterRole;
import restaurant_sid.SidWaiterRole;
import Role.CustomerRole;
import Role.Landlord;
import Role.NormalWaiterRole;
import Role.ResidentRole;
import Role.Role;
import Role.WaiterRole;

public class PersonAgent extends Agent implements Person 
{
	Clock clock;
	public PersonGui gui;
	List<Gui> guis = new ArrayList<Gui>();
	public List<Role> roles = Collections.synchronizedList(new ArrayList<Role>()); //all the different roles
	String name;
	private Wealth wealth = new Wealth();
	enum stateOfNourishment {unknown, normal, hungry, ravenous, full, bursting}
 	enum stateOfLocation {unknown, home, restaurant}
 	enum stateOfWealth {unknown, poor, adequate, rich}
	Building home; //the building he lives in {apt or house}
	Vehicle vehicle;  // {car or null} <- null == has to take bus
	int time; //some reference to the system.time
	int hungerLevelTime; //some reference to how agent gets hungry based on when it ate and how long time has elapsed 
	stateOfPerson stateP;
 	String destination;
	HashMap <String, Coordinates> Map; //mapping the world from address to coordinates
	enum toDo {startWork, endWork}; HashMap <toDo, Integer> importantTime; //keeps track of important time related schedules 
 	HashMap <String, Contact> Directory; //directory to all the places the person can go to
 	Map <Object, Double> myTab = new HashMap<Object, Double>(); //keeps track of who the person owes money to;
 	
 	//Messages
 	void updateStateOfNourishment (stateOfNourishment nour)
 	{
        stateP.SoN = nour;
 	}
 	void updateStateOfWealth (stateOfWealth wealth)
 	{
        stateP.SoW = wealth;
 	}
 	/*void updateStateOfLocation (stateOfLocation loc)
 	{
        stateP.SoL = loc;
        if(loc == atHome)
        {
             for(Role r: Roles)
             {
			       if(!r.containsType(resident)
			       {
			             r.addRole(new ResidentRole());
			       }
			       if (r.type !=resident && r.isActive())
			       {
			            r.setActive(false);
			       } 
              }
        }
        else if (loc == atWork) 
        {
             for(Role r: Roles)
             {
                   if(!r.containsType(employee)
                   {
                         r.addRole(new employeeRole());
                   }
                   if (r.type !=employee&& r.isActive())
                   {
                        r.setActive(false);
                   } 
              }
        }
        else if (loc == atBustStop) 
        {
             for(Role r: Roles)
             {
                   if(!r.containsType(busPassenger)
                   {
                         r.addRole(new busPassenger());
                   }
                   if (r.type != bussPassenger&& r.isActive())
                   {
                        r.setActive(false);
                   } 
              }
        }
        else if (loc == atMarket) 
        {
             for(Role r: Roles)
             {
                   if(!r.containsType(marketCustomer)
                   {
                         r.addRole(new marketCustomer());
                   }
                   if (r.type !=marketCustomer&& r.isActive())
                   {
                        r.setActive(false);
                   } 
              }
        }
        else if (loc == atBank) 
        {
             for(Role r: Roles)
             {
                   if(!r.containsType(bankCustomer)
                   {
                         r.addRole(new bankCustomer());
                   }
                   if (r.type !=bankCustomer && r.isActive())
                   {
                        r.setActive(false);
                   } 
              }
        }
	}*/
 	public PersonAgent(String name, Clock c)
 	{
 		clock = c;
 		this.name = name;
 	}
	@Override
	protected boolean pickAndExecuteAnAction() 
	{
		
		//Person Rules
		int time = clock.getHour();
		if (time < 12)
		{
			//return (morningSchedule); 
		}
		else if (12 < time && time < 6) 
		{
			//return (afternoonSchedule);
	 	}
		else if (6 < time && time < 9) //night schedule
		{
			//return (nightSchedule);
		}
		
		
		boolean anytrue = false;
		synchronized(roles)
		{
			for (Role r:roles)
			{
				if (r instanceof RanchoManager && r.active)
				{
					anytrue = anytrue || ((RanchoManager)r).pickAndExecuteAnAction();
				}
				if (r instanceof BankCustomer && r.active)
				{
					anytrue = anytrue || ((BankCustomer)r).pickAndExecuteAnAction();
				}
				if (r instanceof CustomerRole && r.active)
				{
					anytrue = anytrue || ((CustomerRole)r).pickAndExecuteAnAction();
				}
				if (r instanceof ResidentRole && r.active)
				{
					anytrue = anytrue || ((ResidentRole)r).pickAndExecuteAnAction();
				}
				if (r instanceof Landlord && r.active){
					System.out.println(clock.getHour());
					anytrue = anytrue || ((ResidentRole)r).pickAndExecuteAnAction();
					anytrue = anytrue || ((Landlord)r).pickAndExecuteAnAction1();
					System.out.println(anytrue);
				}
				if (r instanceof WaiterRole && r.active)
				{
					anytrue = anytrue || ((WaiterRole)r).pickAndExecuteAnAction();
				}
				if (r instanceof SidWaiterRole && r.active)
				{
					anytrue = anytrue || ((SidWaiterRole)r).pickAndExecuteAnAction();
				}
				if (r instanceof SidCustomerRole && r.active)
				{
					anytrue = anytrue || ((SidCustomerRole)r).pickAndExecuteAnAction();
				}
				if (r instanceof RanchoWaiterRole && r.active)
				{
					anytrue = anytrue || ((RanchoWaiterRole)r).pickAndExecuteAnAction();
				}
				if (r instanceof RanchoCustomerRole && r.active)
				{
					anytrue = anytrue || ((RanchoCustomerRole)r).pickAndExecuteAnAction();
				}
				if (r instanceof AndrewWaiterRole && r.active)
				{
					anytrue = anytrue || ((AndrewWaiterRole)r).pickAndExecuteAnAction();
				}
				if (r instanceof AndrewCustomerRole && r.active)
				{
					anytrue = anytrue || ((AndrewCustomerRole)r).pickAndExecuteAnAction();
				}
				if (r instanceof AmyWaiterRole && r.active)
				{
					anytrue = anytrue || ((AmyWaiterRole)r).pickAndExecuteAnAction();
				}
				if (r instanceof AmyCustomerRole && r.active)
				{
					anytrue = anytrue || ((AmyCustomerRole)r).pickAndExecuteAnAction();
				}
				if (r instanceof NormalWaiterRole && r.active)
				{
					anytrue = anytrue || ((NormalWaiterRole)r).pickAndExecuteAnAction();
				}
				if (r instanceof AndrewNormalWaiterRole && r.active)
				{
					anytrue = anytrue || ((AndrewNormalWaiterRole)r).pickAndExecuteAnAction();
				}
				if (r instanceof AmyNormalWaiterRole && r.active)
				{
					anytrue = anytrue || ((AmyNormalWaiterRole)r).pickAndExecuteAnAction();
				}
				if (r instanceof RanchoNormalWaiterRole && r.active)
				{
					anytrue = anytrue || ((RanchoNormalWaiterRole)r).pickAndExecuteAnAction();
				}
				if (r instanceof SidNormalWaiterRole && r.active)
				{
					anytrue = anytrue || ((SidNormalWaiterRole)r).pickAndExecuteAnAction();
				}
				if (r instanceof BankAgent)
				{
					anytrue = anytrue || ((BankAgent)r).pickAndExecuteAnAction();
				}
				/*if (r instanceof BankRobber){
					anytrue = anytrue || ((BankRobber)r).pickAndExecuteAnAction();
				}*/
			}
		}
		if(anytrue)
		{
			return true;
		}
		return false;
	}
	/*boolean morningSchedule()
	{
		 //only cares about if person got to work and if the person ate
		 if(stateP.SoL != atWork){
		  if((importantTime.get(startWork) - time ) >= 60) //has one hour until work time
		  {    
		     if(stateP.SoN == hungry || starving){ //if hungry and has time
		     {
		        if(stateP.SoW != wealthy){ //if not wealthy
		                   stateP.SoN = hungryPending; 
		                   goCookAtHome(); //cook food at home
		                   return T;
		        }
		        else{ //if wealthy
		                  stateP.SoN = hungryPending;
		                  goToRestaurant(); //go to restaurant
		                  return T;
		        }
		     }
		     else if (stateP.SoN == bursting) //need exercise
		     {
		             stateP.SoN = burstingPending;
		             walkToWork(); //to get exercise
		             return T;
		     }
		     }
		  else if ( 10 < (importantTime.get(startWork) - time ) <20 ){
		     if (stateP.SoN == hungry)
		     {
		           stateP.SoN = hungryPending;
		           goToWork(); //skip breakfast
		           return T;
		     }
		     else if (stateP.SoN == starving)
		     {   
		           if(stateP.SoW == wealthy) //be late to work
		           {
		                 goToWorkLateEat(); //eat food, go to Work late
		                 stateP.SoN = starvingPending;
		           }
		           else if (stateP.SoW == adequate) 
		           {
		                 if (myVehicle == car){
		                        //if has a car
		                        goToFastFood(); //fast 
		                        stateP.SoN = starvingPending;
		                 }
		           }
		           else{
		                  stateP.SoN = starvingPending;
		                  goToWork();
		           }
		    }
		    else{ //not hungry go to work
		           stateP.SoL = atWorkPending;
		           goToWork();
		    }
		  }
		  else //less than 10min until workStart
		  {
		      //still not work
		      tellBossLate();
		      stateP.SoL = atWorkPending;
		  }
		 }
		 else 
		 {
		   //at work
		   return F;
		   //work role takes over
		 }
	}*/
	public void addRole(Role r) 
	{
		roles.add(r);
		r.setPerson(this);
	}
	public void setGui(PersonGui g)
	{
		gui = g;
	}
	/*public static class Wealth
	{
		double cash;//Cash on hand
		double cashNeeded;//This is determined by the agent's normal role
	    double savings;//Cash in bank
	    double salary;//Money made weekly
	    double loan;//Money owed to the bank
	    
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
	}*/
	public static class Building
	{
		String name;
	    enum type {home, apt, restaurant};
	    Person owner;
	    List<PersonAgent> occupants;
	}
	public static class Vehicle
	{
	    String name;
	    enum type {car, bus, truck};
	    Person owner;
	    Coordinate destination;
	    class Coordinate
	    {
	         int x;
	         int y;
	     }
	 }
	public static class stateOfPerson
	{
	    stateOfNourishment SoN; 
	    stateOfLocation SoL;
	    stateOfWealth SoW;
	}
	public static class Coordinates
	{
		public Coordinates(int i, int j) 
		{
			x=i;
			y=j;
		}
		int x;  
		int y;
	}
	public void addGui(Gui gui)
	{
		guis.add(gui);
	}

	public Wealth getWealth() {
		return wealth;
	}
	public void setWealth(Wealth wealth) {
		this.wealth = wealth;
	}

	public static class Contact
	{ 
		String name; //name of restaurant, name of market...etc
	    String destination; //address
	    Object personOfContact; //host, marketAgent, BankHost.. etc
	}
	
	
	public void addTab(double debt, Object loaner) {
		// TODO Auto-generated method stub
		/*add money that the person owes so that at the end of the
		 *  day goes to the bank to get enough money
		 */
		if(myTab.containsKey(loaner)){
			myTab.put(loaner, myTab.get(loaner) + debt);
		}
		else{
			myTab.put(loaner, debt);
		}
	}
	@Override
	public PersonGui getGui() {
		// TODO Auto-generated method stub
		return gui;
	}
	
	public Clock getClock(){
		return clock;
	}
}