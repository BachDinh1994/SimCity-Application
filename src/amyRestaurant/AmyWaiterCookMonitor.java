package amyRestaurant;
import java.util.ArrayList;
import java.util.List;

import amyRestaurant.AmyCookAgent.CookOrder;
import amyRestaurant.AmyCookAgent.OrderState;
import amyRestaurant.interfaces.AmyWaiter;

public class AmyWaiterCookMonitor extends Object 
{
	private AmyCookAgent cook;
    private int count = 0;
 	public List<CookOrder> orders;
    
    synchronized public void insert(String foodToCook, int forLocation, AmyWaiter myWaiter)
    {
        insert_item(foodToCook,forLocation,myWaiter);
        count++;
        if(count == 1) 
        {
            System.out.println("\tNot Empty, notify");
            notify();                               // Not empty, notify a waiting customer
        }
        cook.stateChanged();
    }
    synchronized public CookOrder remove(CookOrder o) 
    {
        while(count == 0)
            try
        	{ 
                System.out.println("\tEmpty, waiting");
                wait(5000);                         // Empty, wait to consume
            } catch (InterruptedException ex) {};
        remove_item(o);
        count--;
        return o;
    }
	private void insert_item(String foodToCook, int forLocation, AmyWaiter myWaiter)
    {
		CookOrder foodOrder = new CookOrder(); //copy the order class
		foodOrder.state = OrderState.raw;
		foodOrder.choice = foodToCook;
		foodOrder.tableNum = forLocation;
		foodOrder.myWaiter = myWaiter;
		cook.amyCookGui.goToWaiter();
        orders.add(foodOrder);
    }
    private void remove_item(CookOrder o)
    {
        orders.remove(o);
    }
	public AmyWaiterCookMonitor(AmyCookAgent cook)
	{
        orders = new ArrayList<CookOrder>();
        this.cook = cook;
    }
}