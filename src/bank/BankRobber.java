package bank;

import java.util.List;
import java.util.concurrent.Semaphore;

import Role.Role;
import agent.Agent;
import bank.gui.BankRobberGUI;
import bank.interfaces.BCustomer;
import bank.interfaces.BHost;
import bank.interfaces.Banker;
import bank.interfaces.Robber;
import bank.test.mock.EventLog;
import bank.test.mock.LoggedEvent;

public class BankRobber extends Role implements Robber {
	
	private List<Banker> tellers = null;
	public enum WeaponState {Armed, Unarmed} //Check this to see if a robber is armed. Can be checked by both user and civilians [so they know to react]
	WeaponState wState = WeaponState.Unarmed;
	public enum ActionState {Entering, Holding, Robbing, Moving, Leaving} //For use in scheduler
	ActionState aState = ActionState.Entering;
	
	//private int greedLevel = (int)((Math.random() * 9) + 1); //Integer used to time the duration of a stay
	private double stolenCash; //Amount of money stolen
	private int numRobbed = 0; //Variable to track how many people are being robbed
	
	private BHost host;
	
	public EventLog log;
	//GUI 
	private BankRobberGUI bRobberGui;
	private Semaphore atTeller = new Semaphore(0,true);
	
	public BankRobber(BHost h){
		host = h;
		log = new EventLog();
		name = "";
		//Check the "inventory" of the Person for a weapon, set the state accordingly
	}
	//MESSAGES
	@Override
	public void msgHereIsCash(Banker b, double money) {
		log.add(new LoggedEvent("Received " + money + " from teller " + numRobbed));
		stolenCash += money;
		numRobbed--;
		if (numRobbed > 0)
			aState = ActionState.Robbing;
		else 
			aState = ActionState.Leaving;
		stateChanged();
	}

	@Override
	public void msgHereIsCash(BCustomer c, double money) {
		//Not implemented right now, but could be if we want to rob customers too
		stolenCash += money;
		numRobbed--;
	}
	
	public void msgHereAreBankers(List<Banker> l){
		if (l.size() != 0){
			log.add(new LoggedEvent("Got list of bankers, proceeding to rob"));
			aState = ActionState.Robbing;
			tellers = l;
			numRobbed = tellers.size();
		} else {
			log.add(new LoggedEvent("No tellers at bank, leaving"));
			aState = ActionState.Leaving;
		}
		stateChanged();
	}
	
	public void msgGoAway(){
		log.add(new LoggedEvent("Robbery failed, leaving bank"));
		aState = ActionState.Leaving;
		//Change variables that made him rob the bank
		stateChanged();
	}
	
	public void msgAtTeller(){
		//release semaphore
		atTeller.release();
	}
	
	//ACTIONS
	
	private void HoldUpBank(){
		aState = ActionState.Holding;
		host.msgGiveMeBankers(this);
	}
	private void RobBank(){
		Banker t = tellers.get(tellers.size()-numRobbed);
		aState = ActionState.Moving;
		bRobberGui.goSeeTeller(t.getPosition());
		try{
			atTeller.acquire();
		} catch (InterruptedException e){
			e.printStackTrace();
		}
		aState = ActionState.Holding;
		t.msgGetRobbed(this);
	}
	private void LeaveBank(){ 
		//Called @end of greed period
		//tell GUI to leave the bank
		if (tellers != null){
			tellers = null;
		}
		bRobberGui.leaveTeller();
		host.msgResumeActivity();
	}
	public boolean pickAndExecuteAnAction() {
		if (bRobberGui == null){
			return true;
		}
		if (aState == ActionState.Entering){
			HoldUpBank(); 
			return true;
		}
		if (aState == ActionState.Robbing && numRobbed > 0){
			RobBank();
			return true;
		}
		if (aState == ActionState.Leaving){
			LeaveBank();
			return true;
		}
		return false;
	}
	
	//Getters/Setters
	public String getWState(){
		return wState.toString();
	}
	public void setGUI(BankRobberGUI g){
		bRobberGui = g;
	}
	
	//For testing purposes only
	public void setArmed(boolean armed){
		if (armed)
			wState = WeaponState.Armed;
		else 
			wState = WeaponState.Unarmed;
	}
	public String getAState(){
		return aState.toString();
	}
	public int getNumRobbed(){
		return numRobbed;
	}
	public double getCash(){
		return stolenCash;
	}
	public String name = "none";

}
