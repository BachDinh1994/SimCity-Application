package SimCity.test;

import java.util.Timer;

import agent.StringUtil;



import SimCity.Gui;
import SimCity.interfaces.Person;

public class Role
{
    protected Person myPerson;
    protected Gui myGui;
    protected String name;
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
    
    public Gui getGui() 
  	{
  		return myGui;
  	}
  	public void setGui(Gui g) 
  	{
  		myGui = g;
  	}
      
    
    public void setInActve(){
    	active =false;
    }
    public void setActive(){
    	active = true;
    }
    
    public boolean isActive(){
    	return active;
    }
    
    /**
     * The simulated action code
     */
    protected void Do(String msg) {
        print(msg, null);
    }

    /**
     * Print message
     */
    protected void print(String msg) {
        print(msg, null);
    }

    /**
     * Print message with exception stack trace
     */
    protected void print(String msg, Throwable e) {
        StringBuffer sb = new StringBuffer();
        sb.append(getName());
        sb.append(": ");
        sb.append(msg);
        sb.append("\n");
        if (e != null) {
            sb.append(StringUtil.stackTraceString(e));
        }
        System.out.print(sb.toString());
    }
    

	private Object getName() {
		// TODO Auto-generated method stub
		return name;
	}
	
	/*public Person getMyPerson() {
		return myPerson;
	}*/
	/*public void setMyPerson(PersonAgent myPerson) {
		this.myPerson = myPerson;
	}*/
	
} 