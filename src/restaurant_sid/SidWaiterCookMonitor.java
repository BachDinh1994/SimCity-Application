package restaurant_sid;
import java.util.ArrayList;
import java.util.List;

import restaurant_sid.SidCookAgent.Order;

public class SidWaiterCookMonitor extends Object 
{
	private SidCookAgent cook;
    private int count = 0;
 	public List<Order> orders;
    
    synchronized public void insert(SidWaiterRole w, String choice, int tNum) 
    {
        insert_item(w,choice,tNum);
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
	private void insert_item(SidWaiterRole w, String choice, int tNum) 
    {
        orders.add(new Order(w,choice,tNum));
    }
    private void remove_item(Order o)
    {
        orders.remove(o);
    }
	public SidWaiterCookMonitor(SidCookAgent cook)
	{
        orders = new ArrayList<Order>();
        this.cook = cook;
    }
}