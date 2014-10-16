package amyRestaurant.interfaces;




/**
 * Restaurant Host Agent
 */
//We only have 2 types of agents in this prototype. A customer and an agent that
//does all the rest. Rather than calling the other agent a waiter, we called him
//the HostAgent. A Host is the manager of a restaurant who sees that all
//is proceeded as he wishes.
public interface AmyCook{

	public abstract void msgHereIsOrder(String foodToCook, int forLocation, AmyWaiter myWaiter);

	public abstract void msgWeHave(String food, int amt,  boolean complete);

	public abstract void msgWeAreOut(AmyMarket amyMarket, String food);
	
	public abstract String getName();

	public abstract void msgHereIsDelivery(String food, Integer amt);
	

}

