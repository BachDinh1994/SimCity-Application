package SimCity.test.mock;

import Role.Role;
import SimCity.Gui;
import SimCity.PersonGui;
import SimCity.interfaces.Person;

	
public class MockPerson extends Mock implements Person{
		
	private Wealth wealth;	
	
	public MockPerson(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		wealth = new Wealth();
	}

	
	
	public Wealth getWealth() {
		return wealth;
	}
	public void setWealth(Wealth wealth) {
		this.wealth = wealth;
	}
	
	

	@Override
	public void addRole(Role r) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setGui(PersonGui g) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void addGui(Gui gui) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void stateChanged() {
		// TODO Auto-generated method stub
		
	}
}
