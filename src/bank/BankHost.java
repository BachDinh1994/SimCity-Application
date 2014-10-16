package bank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import bank.gui.HostGui;
import bank.interfaces.BCustomer;
import bank.interfaces.BHost;
import bank.interfaces.Banker;
import bank.interfaces.Robber;
import bank.test.mock.EventLog;
import bank.test.mock.LoggedEvent;
import Role.Role;
import agent.Agent;

public class BankHost extends Agent implements BHost{
	public EventLog log;
	
	private String name;
	private Semaphore atDesk = new Semaphore(0,true);
	private boolean working, cooperated;
	
	public List<BCustomer> customers;
	public List<Banker> tellers;
	public List<BankAccount> accounts;
	
	private HostGui hgui = null;
	private Robber robber = null;
	
	
	public BankHost(String name){
		this.name = name;
		log = new EventLog();
		working = false;
		cooperated = false;
		customers = Collections.synchronizedList(new ArrayList<BCustomer>());
		tellers = Collections.synchronizedList(new ArrayList<Banker>());
		accounts = Collections.synchronizedList(new ArrayList<BankAccount>());
	}
	public void msgAtDesk(){//Called from GUI
		atDesk.release();
	}
	public void msgJoiningLine(BCustomer c){
		log.add(new LoggedEvent("Added customer to waiting line"));
		if (!customers.contains(c))
			customers.add(c);
		stateChanged();
	}
	public void msgImFree(Banker t){
		log.add(new LoggedEvent("Added teller to available tellers"));
		tellers.add(t);
		stateChanged();
	}
	public void msgImFree(Banker t, BankAccount a){
		log.add(new LoggedEvent("Updated accounts and added available teller"));
		synchronized(accounts){
			for (BankAccount act : accounts){
				if (act.getCustomer().equals(a.getCustomer()))
					accounts.remove(act);
			}
		}
		accounts.add(a);
		tellers.add(t);
		stateChanged();
	}

	@Override
	public void msgGiveMeBankers(Robber r) {
		robber = r; 
		cooperated = false;
		setWorking(false);
		stateChanged();
	}
	
	public void msgAccountDrained(BankAccount a){
		log.add(new LoggedEvent("Drained an account of " + a.getBalance()));
		synchronized(accounts){
			for (BankAccount act : accounts){
				if (act.getCustomer().equals(a.getCustomer()))
					accounts.remove(act);
			}
		}
		accounts.add(a);
		stateChanged();
	}
	
	public void msgResumeActivity(){
		robber = null;
		setWorking(true);
		stateChanged();
	}

	private void sendCustomer(BCustomer cust, Banker agent){
		customers.remove(cust);
		tellers.remove(agent); 
		boolean match = false;
		synchronized(accounts){
			for (BankAccount a: accounts){
				if (a.getCustomer().equals(cust)){
					agent.msgHereIsAccount(a);
					match = true;
				}
			}
		}
		if (match == false){
			agent.msgOpenAccount(0, cust);
		}
		cust.msgGoSeeTeller(agent);
	}
	
	private void delayCustomer(BCustomer cust){
		cust.msgWaitInLine(customers.indexOf(cust));
	}
	
	private void getRobbed(){
		if (robber.getWState().equals("Armed")){
			robber.msgHereAreBankers(tellers);
			cooperated = true;
		} else {
			int decision = (int)Math.random();
			if (decision == 0){
				robber.msgGoAway();
				setWorking(true);
			}else{
				robber.msgHereAreBankers(tellers);
				cooperated = true;
			}
		}
	}
	
	private void resumeBank(){
		cooperated = false;
		for (Banker teller : tellers){
			teller.msgResumeActivity();
		}
	}
	
	private void GoToDesk(){
		hgui.moveToDesk();
		try {
			atDesk.acquire();
		} catch (InterruptedException e){
			e.printStackTrace();
		}
		setWorking(true);
		stateChanged();
	}

	public boolean pickAndExecuteAnAction(){
		if (!isWorking()){
			//Implement check to see if he should be working [From PersonAgent]
			//if so 
			if (robber == null){
				GoToDesk();
				return true;
			} else if (!cooperated){
				getRobbed();
				return true;
			}
			
			return false;
			//if not he and he's not getting robbed he should leave the bank 
			//if not and he is getting robbed 
		}
		if (cooperated){
			resumeBank();
			return true;
		}
		synchronized(customers){
			for (BCustomer c : customers){
				synchronized(tellers){
					for (Banker t : tellers){
						sendCustomer(c,t); //Maybe replace those with c and t respectively
						return true;
					}
				}
				delayCustomer(c);
			}
		}
		return false;
	}
	
	public void setGui(HostGui h){
		hgui = h;
	}
	
	/*Methods for Testing*/
	public boolean isWorking() {
		return working;
	}
	public void setWorking(boolean working) { 
		this.working = working;
	}


}
