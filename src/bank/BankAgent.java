package bank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import bank.gui.TellerGui;
import bank.interfaces.BCustomer;
import bank.interfaces.BHost;
import bank.interfaces.Banker;
import bank.interfaces.Robber;
import bank.test.mock.EventLog;
import bank.test.mock.LoggedEvent;
import Role.Role;
import agent.Agent;

public class BankAgent extends Role implements Banker{
	//Lists and Data 
	public Map<BCustomer,Double> pendingTransactions;
	public Map<BCustomer,Double> pendingLoans;
	public List<BankAccount> accountsListed;

	//Other Runtime Variables
	public enum TellerState{NotWorking,Free, Lending, Busy, Robbed};
	private TellerState state = TellerState.NotWorking;
	BHost host; //set up on initialization
	Robber robber;//For use in robberies
	double totalMoney;
	private String name;
	private boolean cooperated;
	private Semaphore atDesk = new Semaphore(0,true);
	
	//GUI stuff
	private TellerGui gui;
	private int position;
	
	public EventLog log;
	
	public BankAgent(String name){
		super();
		this.name = name;
		position = 0;
		pendingTransactions = Collections.synchronizedMap(new HashMap<BCustomer,Double>());
		pendingLoans = Collections.synchronizedMap(new HashMap<BCustomer,Double>());
		accountsListed = Collections.synchronizedList(new ArrayList<BankAccount>());
		log = new EventLog();
		cooperated = false;
	}
	
	public void msgAtDesk(){
		atDesk.release();
	}
	
	public void	msgChangeBalance(double amount, BCustomer c){
			state = TellerState.Busy;
			pendingTransactions.put(c, amount);
			stateChanged();
	}
	public void msgHereIsAccount(BankAccount act){
		synchronized(accountsListed){
			for (BankAccount a : accountsListed){
				if (a.getCustomer().equals(act.getCustomer())){
					accountsListed.remove(a);
				}
			}
		}
		accountsListed.add(act);
	}
	public void msgOpenAccount(double amount, BCustomer c){
			accountsListed.add(new BankAccount(c,amount));
	}
	public void msgIWantALoan(double amount, BCustomer c){
			if (amount > 0){
				pendingLoans.put(c,amount);
				state = TellerState.Lending;
			} else {
				state = TellerState.Free;
			}
			stateChanged();
	}
	public void msgGetRobbed(Robber r){
		log.add(new LoggedEvent("Got robber message"));
		robber = r;
		state = TellerState.Robbed;
		stateChanged();
	}
	public void msgResumeActivity(){
		log.add(new LoggedEvent("Returning to work"));
		cooperated = false;
		robber = null;
		state = TellerState.Free;
		stateChanged();
	}
	private void handleTransaction(BankAccount account, double amount){
			BCustomer c = account.getCustomer();
			double balanceChange = account.getBalance() + amount;
			if (balanceChange > account.getBalance()){
				account.setBalance(balanceChange);
				c.msgHereIsCash(0);
				host.msgImFree(this,account);
				state = TellerState.Free;
			}else if (balanceChange >= 0){
				account.setBalance(balanceChange);
				c.msgHereIsCash(-amount);
				host.msgImFree(this, account);
				state = TellerState.Free;
			}else{
				c.msgDoYouWantALoan(account.getBalance());
				account.setBalance(0);
			}
	}
	
	private void giveLoan(BankAccount account, double loan){
		account.setAccountStatus(false);
		account.setLoanAmount(loan);
		account.getCustomer().msgHereIsCash(loan);
		
	}
		
	private void assessLoan(BankAccount account, double payment){
		if (payment > 0){
			account.setLoanAmount(account.getLoanAmount()-payment);
			if (account.getLoanAmount() > 0){
				account.setBalance(account.getLoanAmount());
				account.setLoanAmount(0);
				account.setAccountStatus(true);
			}else {
				account.getCustomer().msgAccountClosed();
				host.msgImFree(this, account);
			}
		} else {
			account.getCustomer().msgAccountClosed();
			host.msgImFree(this, account);
		}
	}
	
	private void SurrenderCash(){
		double totalMoney = 0;
		for (BankAccount a : accountsListed){
			totalMoney += a.getBalance();
			a.setBalance(0);
			host.msgAccountDrained(a);
		}
		if (!cooperated && robber != null){
			robber.msgHereIsCash(this, totalMoney);
			cooperated = true;
		}
	}
	
	private void goToDesk(){
		gui.moveToDesk();
		try {
			atDesk.acquire();
		} catch (InterruptedException e){
			e.printStackTrace();
		}
		host.msgImFree(this);
		state = TellerState.Free;
	}
		
	public boolean pickAndExecuteAnAction() {	//We could implement a time-based interest calculation method up here if we wanted to
		if (!name.equals("test") && gui == null)
			return true;
		if (state == TellerState.NotWorking){
			//Check if PersonAgent requires this role
			//If so
			goToDesk();
			return true;
		}
		if (state == TellerState.Robbed){
			SurrenderCash();
			return true;
		}
		if (state == TellerState.Busy){
			synchronized(pendingTransactions){
				for (BCustomer c : pendingTransactions.keySet()){
					synchronized(accountsListed){
						for (BankAccount a : accountsListed){
							if (a.getCustomer().equals(c)){
								if (a.getStatus() == true){
									handleTransaction(a,pendingTransactions.get(c));
									pendingTransactions.remove(c);
									return true;
								} else {
									assessLoan(a,pendingTransactions.get(c));
									return true;
								}
							}
						}
					}
				}
			}
		}
		if (state == TellerState.Lending){
			synchronized(pendingLoans){
				for (BCustomer c: pendingLoans.keySet()){
					synchronized(accountsListed){
						for(BankAccount a: accountsListed){
							if (a.getCustomer().equals(c)){
								giveLoan(a,pendingLoans.get(c));
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public void setPosition(int p){
		position = p;
	}
	public int getPosition(){
		return position;
	}
	
	public void setGui(TellerGui g){
		gui = g;
	}
	
	public void setHost(BHost h){
		host = h;
	}
	
	/*For testing purposes only*/
	public void setWorking(boolean active){
		if (active)
			state = TellerState.Free;
		else 
			state = TellerState.NotWorking;
	}
	public String getState(){
		return state.toString();
	}
}
