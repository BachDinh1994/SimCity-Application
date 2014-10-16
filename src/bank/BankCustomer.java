package bank;

import java.util.concurrent.Semaphore;

import bank.test.mock.LoggedEvent;
import bank.gui.BCustomerGui;
import bank.interfaces.BCustomer;
import bank.interfaces.BHost;
import bank.interfaces.Banker;
import bank.test.mock.EventLog;
import Role.Role;
import agent.Agent;

public class BankCustomer extends Role implements BCustomer{

	public enum CustState {None, Acted, Waiting, Entering, ChangingBalance, ConsideringLoan, Leaving};
	private CustState state = CustState.None;
	
	//These three come from OrdinaryPerson
	public String name;
	private double cash;
	private double cashNeeded;
	private double salary;

	//This comes from the GUI
	private BHost host;
	
	//This is set by the host
	private Banker myAgent; 
	
	//These two stay with Banker
	private double balance; 
	private double moneyOwed;
	private int linePosition;
	private BCustomerGui bcgui;
	private Semaphore atTeller = new Semaphore(0,true);
	public EventLog log;
	
	public BankCustomer(String name){
		super();
		this.name = name;
		log = new EventLog();
	}
	
	public void msgWaitInLine(int pos){
		log.add(new LoggedEvent("Waiting in line at position " + pos));
		linePosition = pos;
		state = CustState.Waiting;
		stateChanged();
	}
	public void msgAtTeller(){
		atTeller.release();
	}
	public void msgGoSeeTeller(Banker ag){
		log.add(new LoggedEvent("Got message to see teller"));
		myAgent = ag;
		state = CustState.Entering;
		stateChanged();
	}
	public void msgAccountClosed(){
		//Change the conditions that made the OrdinaryPerson come to the bank
		log.add(new LoggedEvent("Account was closed, leaving"));
		state = CustState.Leaving;
		stateChanged();
	}
	public void msgDoYouWantALoan(double amountReceived){
		log.add(new LoggedEvent("Thinking of a loan"));
		balance = 0;
		cash += amountReceived;
		state = CustState.ConsideringLoan;
		stateChanged();
	}
	public void msgHereIsCash(double amt){
		log.add(new LoggedEvent("Got cash, leaving"));
		cash += amt;
		state = CustState.Leaving;
		stateChanged();
	}
	
	private void JoinLine(){
		host.msgJoiningLine(this);
		state = CustState.Acted;
	}
	
	private void WaitInLine(){
		bcgui.waitInLine(linePosition);
		state = CustState.Acted;
	}
	
	private void EnterBank(){
		//tell GUI to go to the Teller, state changes to ChangingBalance when the BC arrives
		state = CustState.ChangingBalance;
		bcgui.goSeeTeller(myAgent.getPosition());
		try {
			atTeller.acquire();
		} catch (InterruptedException e){
			e.printStackTrace();
		}
		double changeInCash = cash - cashNeeded;
		if (changeInCash > 0)
			cash -= changeInCash;
		myAgent.msgChangeBalance(changeInCash,this);
	}
	private void ConsiderLoan(){
		//Check if there are any other options [ie. if it's almost payday, if this OrdinaryPerson has another account somewhere]
		double changeInCash = cashNeeded - cash;
		if (changeInCash > 10){
			myAgent.msgIWantALoan(changeInCash,this);
		}else{
			myAgent.msgIWantALoan(0,this);
			state = CustState.Leaving;
		}
	}
	private void LeaveBank(){
		//tell GUI to make the customer leave the bank, revert back to OrdinaryPerson
		bcgui.leaveTeller();
	}
	
	public boolean pickAndExecuteAnAction(){
		if (state == CustState.None){
			JoinLine();
			return true;
		}
		if (state == CustState.Waiting){
			WaitInLine();
			return true;
		}
		if (state == CustState.Entering){
			EnterBank();
			return true;
		}
		if (state == CustState.ConsideringLoan){
			ConsiderLoan();
			return true;
		}
		if (state == CustState.Leaving){
			LeaveBank();
			return true;
		}
		return false;
	}
	
	public void setHost(BHost h){
		host = h;
	}
	
	public void setGui(BCustomerGui g){
		bcgui = g;
	}
	
	/*For testing purposes only*/
	public String getState(){
		return state.toString();
	}
	public void setMoneyVariables(double c, double cN){
		cash = c;
		cashNeeded = cN;
	}
	public double getCash(){
		return cash;
	}
}
