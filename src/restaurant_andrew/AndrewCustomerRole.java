package restaurant_andrew;

import restaurant_andrew.gui.CustomerGui;
import restaurant_andrew.interfaces.Cashier;
import restaurant_andrew.interfaces.Customer;
import restaurant_andrew.interfaces.Waiter;
import Role.Role;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

/**
 * Restaurant customer agent.
 */
public class AndrewCustomerRole extends Role implements Customer {
	private final double STARTING_MONEY = 30.00;
	private double money = STARTING_MONEY;
	private double billCost = 0;
	private String name;
	private int decisionTime = 3000;
	private int eatingTime = 5000;
	Timer timer = new Timer();
	private CustomerGui customerGui;

	// agent correspondents
	private AndrewHostAgent host;
	private Waiter waiter;
	private Cashier cashier;
	
	// from actions
	State s = State.idle;
	public enum State {idle, waiting, debatingLeaving, stillWaiting, seated, decided, ordered, eating, paying};
	// from messages
	Event e = Event.none;
	public enum Event {none, restaurantFull, hungry, following, decided, ordering, gotFood, done, gotChange, leaving};

	Semaphore waitingForSeat = new Semaphore(0, true);
	Semaphore atTable = new Semaphore(0, true);
	Semaphore waitingForWaiter = new Semaphore(0, true);
	
	String choice;
	
	AndrewMenu menu;
		
	/**
	 * Constructor for CustomerAgent class
	 *
	 * @param name name of the customer
	 */
	public AndrewCustomerRole(String name){
		super();
		
		this.name = name;
		
		switch (name) {
			case "Steak":
				money = 16.00;
				break;
			case "Chicken":
				money = 11.00;
				break;
			case "Pizza":
				money = 8.00;
				break;
			case "Salad":
				money = 6.00;
				break;
			case "Poor":
				money = 6.00;
				break;
			case "Flake":
				money = 1.00;
				break;
		}
	}

	/**
	 * hack to establish connection to other agents.
	 */
	public void setHost(AndrewHostAgent host) {
		this.host = host;
	}
	public void setWaiter(Waiter w) {
		this.waiter = w;
	}

	public String getCustomerName() {
		return name;
	}
	// Messages

	public void msgGotHungry() {//from animation
		e = Event.hungry;
		stateChanged();
	}

	public void msgRestaurantFull() {

		e = Event.restaurantFull;
		stateChanged();
	}
	
	public void msgDecideToStay() {
		
		if (e == Event.restaurantFull)
			e = Event.hungry;
		stateChanged();
	}
	
	public void msgFollowMe(Waiter w, AndrewMenu menu) {
		// DoFollowWaiter(); // animation
		setWaiter(w);
		
		e = Event.following;
		this.menu = menu;
		stateChanged();
	}
	public void msgDecided() {
		
		e = Event.decided;
		stateChanged();
	}
	public void msgNoValidChoices() {
		
		e = Event.leaving;
		stateChanged();
	}
	public void msgWhatDoYouWant() {
		
		e = Event.ordering;
		waitingForWaiter.release();
		stateChanged();
	}
	public void msgOutOfFood(AndrewMenu menu) {
		
		e = Event.following;
		this.menu = menu;
		stateChanged();
	}
	public void msgHereIsFood() {
		
		e = Event.gotFood;
		choice = choice.substring(0, choice.length() - 1); // remove question mark
		stateChanged();
	}
	public void msgHereIsBill(Cashier cashier, double cost) {
		
		this.cashier = cashier;
		billCost = cost;
		stateChanged();
	}
	public void msgHereIsChange(double change) {
		
		money += change;
		e = Event.gotChange;
		stateChanged();
	}
	public void msgFinished() {
		
		e = Event.done;
		choice = null;
		stateChanged();
	}
	public void msgAnimationFinishedGoToSeat() {
		//from animation
		atTable.release();
		stateChanged();
	}
	public void msgAnimationFinishedLeaveRestaurant() {
		//from animation
		e = Event.none;
		stateChanged();
	}

	/**
	 * Scheduler.  Determine what action is called for, and do it.
	 */
	public boolean pickAndExecuteAnAction() {
		if (s == State.idle) {
			if (e == Event.hungry) {
				s = State.waiting;
				goToRestaurant();
			}
		}
		if (s == State.waiting) {
			if (e == Event.restaurantFull) {
				s = State.debatingLeaving;
				decideToLeave();
			}
			if (e == Event.hungry) {
				s = State.stillWaiting;
			}
			if (e == Event.following) {
				s = State.seated;
				followWaiter();
			}
		}
		if (s == State.stillWaiting) {
			if (e == Event.following) {
				s = State.seated;
				followWaiter();
			}
		}
		if (s == State.seated) {
			if (e == Event.decided) {
				s = State.decided;
				callWaiter();
			}
			if (e == Event.leaving) {
				s = State.idle;
				leave();
			}
		}
		if (s == State.decided) {
			if (e == Event.ordering) {
				s = State.ordered;
				order();
			}
		}
		if (s == State.ordered) {
			if (e == Event.following) {
				s = State.seated;
				decideChoice();
			}
			if (e == Event.gotFood) {
				s = State.eating;
				eatFood();
			}
		}
		if (s == State.eating) {
			if (e == Event.done && billCost > 0) {
				s = State.paying;
				payBill();
			}
		}
		if (s == State.paying) {
			if (e == Event.gotChange) {
				s = State.idle;
				leave();
			}
		}
		
		return false;
	}

	// Actions

	private void goToRestaurant() {
		
		host.msgWantFood(this);//send our instance, so he can respond to us
	}
	
	private void followWaiter() {
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		decideChoice();
	}
	
	private void decideChoice() {
		
		timer.schedule(new TimerTask() {
			public void run() {
				int choiceNum;
				
				switch (name) {
					case "Steak":
						choice = "Steak?";
						break;
					case "Chicken":
						choice = "Chicken?";
						break;
					case "Pizza":
						choice = "Pizza?";
						break;
					case "Salad":
						choice = "Salad?";
						break;
					case "Flake":
						choiceNum = (int)(Math.random() * menu.getSize());
						choice = menu.getChoice(choiceNum) + "?";
						break;
					default:
						ArrayList<String> validChoices = new ArrayList<String>();
						for (int i = 0; i < menu.getSize(); i++) {
							if (menu.getPrice(i) <= money) validChoices.add(menu.getChoice(i));
						}
						if (validChoices.size() < 1) {
							msgNoValidChoices();
						}
						else {
							choiceNum = (int)(Math.random() * validChoices.size());
							choice = validChoices.get(choiceNum) + "?";
						}
				}
				msgDecided();
			}
		},
		decisionTime);
	}
	
	private void decideToLeave() {
		int leave = (int)(Math.random() * 2);
		if (leave == 1) leave();
		else msgDecideToStay();
	}
	
	private void callWaiter() {
		
		waiter.msgReadyToOrder(this);
		try {
			waitingForWaiter.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void order() {
		
		waiter.msgHereIsChoice(this, choice.substring(0, choice.length() - 1));
	}
	
	private void eatFood() {
		
		timer.schedule(new TimerTask() {
			public void run() {
				
				msgFinished();
			}
		},
		eatingTime);
	}
	
	private void payBill() {
		double paying;
		if (billCost > money) paying = money;
		else paying = billCost + .01;
		
		cashier.msgPayingBill(this, paying);
		money -= paying;
	}
	
	private void leave() {
		
		DoLeaveRestaurant(); // animation
		if (waiter != null) waiter.msgPaidAndLeaving(this);
		else host.msgLeaving(this);
		money = STARTING_MONEY;
		if (name.equals("Poor")) money = 7.00;
	}
	private void DoLeaveRestaurant() {
		customerGui.DoExitRestaurant();
	}

	// Accessors, etc.


	public String getName() {
		return name;
	}

	public String toString() {
		return "customer " + getName();
	}

	public void setGui(CustomerGui g) {
		customerGui = g;
	}

	public CustomerGui getGui() {
		return customerGui;
	}
	
	public String getChoice() {
		return choice;
	}

}

