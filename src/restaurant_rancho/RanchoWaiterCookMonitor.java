package restaurant_rancho;
import java.util.ArrayList;
import java.util.List;

import restaurant_rancho.RanchoCook.Order;
import restaurant_rancho.interfaces.Waiter;

public class RanchoWaiterCookMonitor extends Object 
{
	private RanchoCook cook;
    private int count = 0;
 	public List<Order> orders;
    
    synchronized public void insert(Waiter w, String c, int t)
    {
        insert_item(w,c,t);
        count++;
        if(count == 1) 
        {
            System.out.println("\tNot Empty, notify");
            notify();                               // Not empty, notify a waiting customer
        }
        cook.stateChanged();
    }
    synchronized public Order remove(Order o) 
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
	private void insert_item(Waiter w, String c, int t)
    {
        orders.add(new Order(w,c,t));
    }
    private void remove_item(Order o)
    {
        orders.remove(o);
    }
	public RanchoWaiterCookMonitor(RanchoCook cook)
	{
        orders = new ArrayList<Order>();
        this.cook = cook;
    }
}