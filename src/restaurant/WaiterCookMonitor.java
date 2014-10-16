package restaurant;
import java.util.ArrayList;
import java.util.List;

import restaurant.CookAgent.Order;
import restaurant.gui.FoodGui;
import restaurant.interfaces.Waiters;

public class WaiterCookMonitor extends Object 
{
	private CookAgent cook;
    private int count = 0;
 	public List<Order> orders;
    
    synchronized public void insert(Waiters w,String choice,int table,FoodGui foodGui) 
    {
        insert_item(w,choice,table,foodGui);
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
	private void insert_item(Waiters w,String choice,int table,FoodGui foodGui)
    {
        orders.add(new Order(w,choice,table,foodGui));
    }
    private void remove_item(Order o)
    {
        orders.remove(o);
    }
	public WaiterCookMonitor(CookAgent cook)
	{
        orders = new ArrayList<Order>();
        this.cook = cook;
    }
}