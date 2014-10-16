package Role;

import java.util.Timer;



import SimCity.interfaces.Person;

public class Role
{
    protected Person myPerson;
    protected Timer timer= new Timer();
    public boolean active = false;
    public void setPerson(Person a) 
    {
        myPerson = a;
    }
    public Person getPersonAgent() 
    {
        return myPerson;
    } 
    //so other agents or role players can send you Person messages. 
    public void stateChanged() 
    {
        myPerson.stateChanged();
    }
	/*public Person getMyPerson() {
		return myPerson;
	}*/
	/*public void setMyPerson(PersonAgent myPerson) {
		this.myPerson = myPerson;
	}*/
	
} 