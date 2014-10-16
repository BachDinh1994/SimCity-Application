package restaurant_andrew;
import java.util.ArrayList;
import java.util.List;

import restaurant_andrew.AndrewCookAgent.Order;

public class AndrewWaiterCookMonitor extends Object 
{
	private AndrewCookAgent cook;
    private int count = 0;
 	public List<Order> orders;
    
    synchronized public void insert(AndrewWaiterRole w, String choice, int table)
    {
        insert_item(w,choice,table);
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
	private void insert_item(AndrewWaiterRole w, String choice, int table)
    {
        orders.add(new Order(w,choice,table));
    }
    private void remove_item(Order o)
    {
        orders.remove(o);
    }
	public AndrewWaiterCookMonitor(AndrewCookAgent cook)
	{
        orders = new ArrayList<Order>();
        this.cook = cook;
    }
}