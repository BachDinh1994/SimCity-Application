package market;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import restaurant.CashierAgent;
import restaurant.CookAgent;

import SimCity.Address;
import SimCity.DeliveryTruckInCityGui;
import agent.Agent;

import market.MarketAgent.dCheck;
import market.gui.DeliveryTruckGui;
import market.gui.MarketEmployeeGui;


public class DeliveryTruckAgent extends Agent{
	DeliveryTruckGui myGui;
	DeliveryTruckInCityGui myInCityGui;
	private Address myAddress;

	private Semaphore atHome = new Semaphore(0,true);
	private Semaphore atPickUpArea = new Semaphore(0,true);
	private Semaphore onDelivery = new Semaphore(0,true);
	
	MarketAgent myBoss;
	CookAgent cook;
	Timer timer = new Timer();

	List<delivery> Deliveries = new ArrayList<delivery>();
	private String name;
	enum deliveryState {newDelivery, onRoute, delivering, delivered, receivedPayment, none, done};
	public class delivery{
		public dCheck dCheck;
		public Double amtPaid;
		public deliveryState dState;
		public delivery(dCheck checkToDeliver){
			dCheck = checkToDeliver;
			amtPaid = 0.0;
			dState = deliveryState.newDelivery;
		}
	}
	

	public DeliveryTruckAgent(String type) {
		super();
		this.name = type;
	}
	
	//messages
	public void msgAtPickUpArea(){
		Do("at the pickup area");
		atPickUpArea.release();
		stateChanged();
	}
	
	public void msgAtDelivery(){
		Do("at the delivery site");		
		onDelivery.release();
		stateChanged();
	}
	
	public void msgAtMarket(){
		Do("at the market");
		atHome.release();
		stateChanged();
		
	}
	
	public void msgHereIsPayment(Double bill, CashierAgent cashierAgent)
	{
		print("Got payment from cashier");
		
		for (delivery d: Deliveries){
			if(d.dCheck.custContact.myBoss instanceof CashierAgent){
				CashierAgent cashier = cashierAgent;
				if(cashier.equals(cashierAgent)){
					d.amtPaid = bill;
					d.dState = deliveryState.receivedPayment;
				}
			}
		}
		
		stateChanged();
	}

	public void msgDeliverThis(dCheck custCheck) {
		// TODO Auto-generated method stub
		Do("received delivery order");
		myGui.msgCreatePackage();
		delivery newDelivery = new delivery(custCheck);
		Deliveries.add(newDelivery);
		stateChanged();
	}



	//scheduler
	protected boolean pickAndExecuteAnAction() {
		// TODO Auto-generated method stub
		
		
		for (delivery d: Deliveries){
			if(d.dState == deliveryState.delivering){
				d.dState = deliveryState.delivered;
				givingItems(d);
				return true;
			}
		}
		
		for (delivery d: Deliveries){
			if(d.dState == deliveryState.receivedPayment){
				d.dState = deliveryState.onRoute;
				backToMarket(d);
				return true;
			}
		}
		
		for(delivery d: Deliveries){
			if (d.dState == deliveryState.done){
				giveCheckToCashier(d);
				Deliveries.remove(d);
				return true;
				
			}
		}
		for (delivery d: Deliveries){
			if(d.dState == deliveryState.newDelivery){
				d.dState = deliveryState.onRoute;
				deliverThis(d);
				return true;
			}
		}
		
		
		return false;
	}

	private void giveCheckToCashier(delivery d){
		Do("giving money to market");
		myBoss.msgHereIsBill(d.amtPaid, d.dCheck);
		
		
	}
	
	private void backToMarket(delivery d) {
		// TODO got payment goes back to market
		//tells gui to go back
		Do("finished delivery, going back to market");
		myInCityGui.goBackToMarket();
		try {
			atHome.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		d.dState = deliveryState.done;

		//then tells gui the route and goes to delivery
		
	}

	private void givingItems(final delivery d) {
		// delivery truck gives the order and waits for bill
		timer.schedule(new TimerTask()
		{
			public void run()
			{
				if(d.dCheck.custContact.myIdentity instanceof CookAgent){
					Do("gave cook the delivery " + d.dCheck.choice);
					cook = (CookAgent) d.dCheck.custContact.myIdentity;
					cook.msgHereIsDelivery(d.dCheck);
				}
			}
		},4000);
		
	}


	//action
	private void deliverThis(delivery job){
		//delivery truck goes to the pickup area
		Do("go to pickup area");
		myGui.goToPickUp();
		try {
			atPickUpArea.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Do("at pickup area");
		
		//then tells gui the route and goes to delivery
		myGui.goToDelivery(job.dCheck.custContact.myAddress);
		myInCityGui.goToDelivery(job.dCheck.custContact.myAddress);
		Do("on route");
		
		try{
			onDelivery.acquire();
					} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		job.dState = deliveryState.delivering;
		Do("at the delivery location");
		
	}
	
	public void setGui(DeliveryTruckGui g)
	{
		myGui = g;
	}
	public void setCityGui(DeliveryTruckInCityGui g)
	{
		myInCityGui = g;
	}
	public DeliveryTruckGui getGui()
	{
		return myGui;
	}
		
	public void setMyBoss(MarketAgent m){
		myBoss =m;
	}
	
	public String getName() 
	{
		return name;
	}

	public void setMyAddress(Address address) {
		// TODO Auto-generated method stub
		myAddress = address;
	}
	public Address getMyAddress() {
		// TODO Auto-generated method stub
		return myAddress;
	}

}
